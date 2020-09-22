package com.almis.awe.scheduler.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.model.util.data.TimeUtil;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import com.almis.awe.scheduler.dao.TaskDAO;
import com.almis.awe.scheduler.enums.TaskStatus;
import com.almis.awe.scheduler.job.scheduled.SchedulerJob;
import com.almis.awe.service.MaintainService;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;

import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;

/**
 * <h2>Job Service Class</h2>
 * <p>
 * Used to launch a maintain process as a scheduled batch
 * </p>
 *
 * @author pvidal
 */
@Log4j2
@Getter
public abstract class JobService extends ServiceConfig {

  // Autowired services
  private final ExecutionService executionService;
  private final MaintainService maintainService;
  private final QueryUtil queryUtil;
  private final TaskDAO taskDAO;
  private final ApplicationEventPublisher eventPublisher;

  @Value("${scheduler.task.timeout:1800}")
  private int defaultTimeout;

  // Locales
  private static final String SCHEDULER_ERROR_MESSAGE_TIMEOUT = "SCHEDULER_ERROR_MESSAGE_TIMEOUT";

  /**
   * Constructor
   */
  public JobService(ExecutionService executionService, MaintainService maintainService, QueryUtil queryUtil, TaskDAO taskDAO,
                    ApplicationEventPublisher eventPublisher) {
    this.executionService = executionService;
    this.maintainService = maintainService;
    this.queryUtil = queryUtil;
    this.taskDAO = taskDAO;
    this.eventPublisher = eventPublisher;
  }

  /**
   * Execute job
   *
   * @param task Task
   * @return Future job result
   * @throws InterruptedException
   */
  public abstract Future<ServiceData> executeJob(Task task, TaskExecution execution, JobDataMap dataMap) throws InterruptedException;

  /**
   * Start logging execution
   *
   * @param execution Execution to log
   */
  void startLogging(TaskExecution execution) {
    ThreadContext.put("execution", execution.getKey());
  }

  /**
   * End logging execution
   */
  void endLogging() {
    ThreadContext.remove("execution");
  }

  public TaskExecution startTask(Task task) throws AWException {
    // Mark task as started
    TaskExecution execution = taskDAO.startTask(task);

    // Start task progress
    startProgressThread(execution, taskDAO.getAverageTime(task.getTaskId()));

    // Return task execution
    return execution;
  }

  /**
   * Start timeout thread
   *
   * @param execution
   * @param timeout
   * @param process
   * @throws InterruptedException
   */
  private void startTimeoutThread(TaskExecution execution, Integer timeout, Future process) throws AWException {
    executionService.startTimeoutJob(execution, timeout, process);
    log.debug("[SCHEDULER][TIMEOUT] The timeout thread has been started {}.{}", execution.getTaskId(), execution.getExecutionId());
  }

  /**
   * Start progress thread
   *
   * @param execution
   * @param averageTime
   * @throws InterruptedException
   */
  private void startProgressThread(TaskExecution execution, Integer averageTime) throws AWException {
    executionService.startProgressJob(execution, averageTime);
    log.debug("[SCHEDULER][PROGRESS] The progress task has been started {}.{}", execution.getTaskId(), execution.getExecutionId());
  }

  /**
   * Get timeout value
   *
   * @param task
   * @return
   */
  protected Integer getTimeout(Task task) {
    Integer timeout = defaultTimeout;
    if (task.getExecutionTimeout() != null) {
      timeout = task.getExecutionTimeout();
    }
    return timeout;
  }

  /**
   * Start batch process
   *
   * @return Batch status
   */
  public ServiceData launchBatch(SchedulerJob job, Future<ServiceData> process) throws AWException {
    ServiceData serviceData = new ServiceData();
    TaskStatus status;

    // Start task timeout
    startTimeoutThread(job.getExecution(), getTimeout(job.getTask()), process);

    // Get current date in milliseconds and in Date formats
    try {
      // Launch process
      serviceData = process.get();

      // Initialize job status
      switch (serviceData.getType()) {
        case WARNING:
          status = TaskStatus.JOB_WARNING;
          break;
        case ERROR:
          status = TaskStatus.JOB_ERROR;
          break;
        case INFO:
          status = TaskStatus.JOB_INFO;
          break;
        default:
          status = TaskStatus.JOB_OK;
      }

      job.getTask().setStatus(status);
      job.getExecution().setDescription(serviceData.getMessage());
    } catch (CancellationException | InterruptedException exc) {
      job.getTask().setStatus(TaskStatus.JOB_INTERRUPTED);
      job.getExecution().setDescription(getLocale(SCHEDULER_ERROR_MESSAGE_TIMEOUT, TimeUtil.formatTime(getTimeout(job.getTask()) * 1000, false)));
      Thread.currentThread().interrupt();
    } catch (Exception exc) {
      job.getTask().setStatus(TaskStatus.JOB_ERROR);
      job.getTask().getReport().setReportMessage(exc.getMessage());
      job.getExecution().setDescription(serviceData.getMessage());
      log.error("[SCHEDULER][TASK_QUERY {}] Error on batch, process {}" + job.getTask().getAction(), job.getTask().getTrigger().getKey(), exc);
      throw new AWException(exc.toString(), exc);
    } finally {
      // Interrupt timer
      executionService.interruptExecutionJobs(job.getExecution());

      // End task
      job.setExecution(taskDAO.endTask(job.getTask(), job.getExecution()));

      // Send report
      executionService.startReportJob(job.getTask(), job.getExecution());
    }
    return serviceData;
  }
}
