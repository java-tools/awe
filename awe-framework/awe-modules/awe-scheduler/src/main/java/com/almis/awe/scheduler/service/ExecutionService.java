package com.almis.awe.scheduler.service;

import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import com.almis.awe.scheduler.enums.ReportType;
import com.almis.awe.scheduler.enums.TriggerType;
import com.almis.awe.scheduler.factory.JobFactory;
import com.almis.awe.scheduler.factory.ReportFactory;
import com.almis.awe.scheduler.factory.TriggerFactory;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.concurrent.Future;

import static com.almis.awe.scheduler.constant.JobConstants.*;
import static com.almis.awe.scheduler.constant.TaskConstants.TASK_SEPARATOR;
import static com.almis.awe.scheduler.enums.JobType.JOB_PROGRESS;
import static com.almis.awe.scheduler.enums.JobType.JOB_TIMEOUT;

/**
 * Task timeout service
 *
 * @author dfuentes
 */
@Log4j2
public class ExecutionService {

  // Autowired services
  private Scheduler scheduler;

  /**
   * Autowired constructor
   *
   * @param scheduler
   */
  @Autowired
  public ExecutionService(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  /**
   * Start timeout job
   *
   * @param execution
   * @param timeout
   * @param process
   */
  public void startTimeoutJob(TaskExecution execution, Integer timeout, Future process) {
    JobDataMap data = new JobDataMap();
    data.put("id", execution.getKey());
    data.put(TASK_JOB_TIMEOUT, timeout);
    data.put("process", process);

    try {
      scheduler.scheduleJob(
        JobFactory.getInstance(JOB_TIMEOUT, data),
        TriggerFactory.getInstance(TriggerType.TIMEOUT, data));
    } catch (Exception exc) {
      log.warn("[SCHEDULER] Timeout job was unable to be scheduled: {}", execution.getKey(), exc);
    }
  }

  /**
   * Start progress job
   *
   * @param execution
   * @param averageTime
   */
  public void startProgressJob(TaskExecution execution, Integer averageTime) {

    JobDataMap data = new JobDataMap();
    data.put("id", execution.getKey());
    data.put(TASK_JOB_EXECUTION, execution);
    data.put(TASK_JOB_AVERAGE_TIME, averageTime);

    try {
      scheduler.scheduleJob(
        JobFactory.getInstance(JOB_PROGRESS, data),
        TriggerFactory.getInstance(TriggerType.PROGRESS, data));
    } catch (Exception exc) {
      log.warn("[SCHEDULER] Progress job was unable to be scheduled: {}", execution.getKey(), exc);
    }
  }

  /**
   * Interrupt timeout timer
   *
   * @param execution
   */
  public void interruptExecutionJobs(TaskExecution execution) {
    JobKey timeoutJobKey = getJobKey(execution, "TIMEOUT");
    JobKey progressJobKey = getJobKey(execution, "PROGRESS");
    try {
      scheduler.interrupt(timeoutJobKey);
      scheduler.interrupt(progressJobKey);
      scheduler.deleteJobs(Arrays.asList(timeoutJobKey, progressJobKey));
    } catch (Exception exc) {
      log.warn("[SCHEDULER] Timeout and progress job were unable to be interrupted: {}", timeoutJobKey.getName(), exc);
    }
  }

  /**
   * Start report job
   *
   * @param task
   * @param execution
   */
  public void startReportJob(Task task, TaskExecution execution) {
    // If report send status doesn't match to execution status, exit
    if (!task.getReport().getReportSendStatus().contains(execution.getStatus().toString())) {
      return;
    }

    try {
      // Generate data map
      JobDataMap data = new JobDataMap();
      data.put(TASK, task);
      data.put(TASK_JOB_EXECUTION, execution);

      // Create report object
      JobDetail reportJob = ReportFactory.getInstance(ReportType.valueOf(task.getReportType()), data);

      // If there is a report job, schedule it
      if (reportJob != null) {
        // Schedule job
        scheduler.scheduleJob(reportJob, TriggerFactory.getInstance(TriggerType.REPORT));
      }
    } catch (Exception exc) {
      log.error("[SCHEDULER] Report job was unabled to be scheduled: {}", task.getTaskId(), exc);
    }
  }

  private JobKey getJobKey(TaskExecution execution, String group) {
    return new JobKey(execution.getTaskId() + TASK_SEPARATOR + execution.getExecutionId(), group);
  }
}
