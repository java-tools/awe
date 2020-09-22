package com.almis.awe.scheduler.service;

import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import com.almis.awe.scheduler.bean.task.TaskParameter;
import com.almis.awe.scheduler.dao.CommandDAO;
import com.almis.awe.scheduler.dao.TaskDAO;
import com.almis.awe.service.MaintainService;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobDataMap;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.concurrent.Future;

/**
 * @author dfuentes
 */
@Log4j2
public class CommandJobService extends JobService {

  // Autowired services
  private final CommandDAO commandDAO;

  // Locales
  private static final String SCHEDULER_BATCH_LAUNCH_TITLE = "SCHEDULER_BATCH_LAUNCH_TITLE";
  private static final String SCHEDULER_BATCH_LAUNCH_MESSAGE = "SCHEDULER_BATCH_LAUNCH_MESSAGE";
  private static final String SCHEDULER_BATCH_LAUNCH_ERROR_TITLE = "SCHEDULER_BATCH_LAUNCH_ERROR_TITLE";
  private static final String SCHEDULER_BATCH_LAUNCH_ERROR_MESSAGE = "SCHEDULER_BATCH_LAUNCH_ERROR_MESSAGE";

  /**
   * Autowired constructor
   *
   * @param executionService Timeout service
   */
  public CommandJobService(ExecutionService executionService, MaintainService maintainService, QueryUtil queryUtil, TaskDAO taskDAO, ApplicationEventPublisher eventPublisher, CommandDAO commandDAO) {
    super(executionService, maintainService, queryUtil, taskDAO, eventPublisher);
    this.commandDAO = commandDAO;
  }

  @Async("schedulerJobPool")
  public Future<ServiceData> executeJob(Task task, TaskExecution execution, JobDataMap dataMap) {
    // Start logging
    startLogging(execution);

    // Log job start
    log.info("[{}] Command job started: {}", task.getTrigger().getKey().toString(), task.getAction());

    ServiceData result = new ServiceData();
    // Execute task
    if (execute(task, getTimeout(task))) {
      result
        .setTitle(getLocale(SCHEDULER_BATCH_LAUNCH_TITLE))
        .setMessage(getLocale(SCHEDULER_BATCH_LAUNCH_MESSAGE));
    } else {
      result
        .setType(AnswerType.ERROR)
        .setTitle(SCHEDULER_BATCH_LAUNCH_ERROR_TITLE)
        .setMessage(SCHEDULER_BATCH_LAUNCH_ERROR_MESSAGE);
    }

    // End logging
    endLogging();

    return new AsyncResult<>(result);
  }

  /**
   * Execute shell command
   *
   * @param task
   * @param timeout
   * @return
   */
  private boolean execute(Task task, final long timeout) {
    return execute(task, task.getParameterList().stream().map(TaskParameter::getValue).toArray(String[]::new), timeout);
  }

  /**
   * Execute shell command
   *
   * @param commandTask
   * @param envp
   * @param timeout
   * @return
   */
  private boolean execute(Task commandTask, String[] envp, final long timeout) {
    return commandDAO.runCommand(commandTask, envp, timeout) == 0;
  }
}
