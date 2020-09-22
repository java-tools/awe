package com.almis.awe.scheduler.job.report;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.service.MaintainService;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static com.almis.awe.scheduler.constant.JobConstants.TASK;
import static com.almis.awe.scheduler.constant.JobConstants.TASK_JOB_EXECUTION;

@Log4j2
public class MaintainReportJob extends ReportJob {

  // Autowired services
  private final QueryUtil queryUtil;
  private final MaintainService maintainService;

  /**
   * Autowired constructor
   *
   * @param queryUtil       Query utilities
   * @param maintainService Maintain service
   */
  public MaintainReportJob(QueryUtil queryUtil, MaintainService maintainService) {
    this.queryUtil = queryUtil;
    this.maintainService = maintainService;
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    // Store task and execution
    super.execute(context);

    // Store task and execution in parameters
    ObjectNode parameters = queryUtil.getParameters(getTask().getDatabase());
    parameters.set(TASK, JsonNodeFactory.instance.pojoNode(getTask()));
    parameters.set(TASK_JOB_EXECUTION, JsonNodeFactory.instance.pojoNode(getExecution()));

    try {
      maintainService.launchPrivateMaintain(getTask().getReport().getReportMaintainId(), parameters);
    } catch (AWException exc) {
      // Log error
      log.error("Report generation error for task {}", getTask().getTaskId(), exc);
    }
  }
}
