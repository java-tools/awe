package com.almis.awe.scheduler.service;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import com.almis.awe.scheduler.bean.task.TaskParameter;
import com.almis.awe.scheduler.dao.TaskDAO;
import com.almis.awe.service.MaintainService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobDataMap;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.concurrent.Future;

import static com.almis.awe.scheduler.constant.JobConstants.TASK_LAUNCHER;

/**
 * @author dfuentes
 */
@Log4j2
public class MaintainJobService extends JobService {

  /**
   * Autowired constructor
   *
   * @param executionService Timeout service
   * @param maintainService  Maintain service
   */
  public MaintainJobService(ExecutionService executionService, MaintainService maintainService, QueryUtil queryUtil, TaskDAO taskDAO, ApplicationEventPublisher eventPublisher) {
    super(executionService, maintainService, queryUtil, taskDAO, eventPublisher);
  }

  /**
   * Execute Job
   *
   * @param task Task to execute
   * @param execution Execution
   * @param dataMap Job data map
   * @return Service data with execution data
   */
  @Async("schedulerJobPool")
  public Future<ServiceData> executeJob(Task task, TaskExecution execution, JobDataMap dataMap) {
    // Start logging
    startLogging(execution);

    // Log job start
    log.info("[{}] Maintain job started: {}", task.getTrigger().getKey().toString(), task.getAction());

    // Initialize database to the one stored on the current task
    ObjectNode parameters = getQueryUtil().getParameters(task.getDatabase(), "1", "0");

    // Insert task parameters
    for (TaskParameter taskParameter : task.getParameterList()) {
      if ("2".equalsIgnoreCase(taskParameter.getSource())) {
        parameters.put(taskParameter.getName(), getProperty(taskParameter.getValue()));
      } else {
        parameters.put(taskParameter.getName(), taskParameter.getValue());
      }
    }

    // Set default parameters
    parameters.put("database", task.getDatabase());
    parameters.put("launcher", (String) dataMap.get(TASK_LAUNCHER));

    ServiceData serviceData;
    try {
      serviceData = getMaintainService().launchPrivateMaintain(task.getAction(), parameters);
    } catch (AWException exc) {
      serviceData = new ServiceData()
        .setType(exc.getType())
        .setTitle(exc.getTitle())
        .setMessage(exc.getMessage());
    }

    // End logging
    endLogging();

    return new AsyncResult<>(serviceData);
  }
}
