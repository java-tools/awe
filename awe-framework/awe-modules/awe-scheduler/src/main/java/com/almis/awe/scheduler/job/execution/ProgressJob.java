package com.almis.awe.scheduler.job.execution;

import com.almis.awe.scheduler.bean.event.SchedulerTaskProgressEvent;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import lombok.extern.log4j.Log4j2;
import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.UnableToInterruptJobException;
import org.springframework.context.ApplicationEventPublisher;

import static com.almis.awe.scheduler.constant.JobConstants.TASK_JOB_AVERAGE_TIME;
import static com.almis.awe.scheduler.constant.JobConstants.TASK_JOB_EXECUTION;

@Log4j2
public class ProgressJob implements InterruptableJob {

  // Autowired services
  private final ApplicationEventPublisher eventPublisher;

  /**
   * Autowired constructor
   *
   * @param eventPublisher Event publisher
   */
  public ProgressJob(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Override
  public void execute(JobExecutionContext context) {
    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
    TaskExecution execution = (TaskExecution) dataMap.get(TASK_JOB_EXECUTION);
    Integer averageTime = (Integer) dataMap.get(TASK_JOB_AVERAGE_TIME);

    // Publish progress event
    eventPublisher.publishEvent(new SchedulerTaskProgressEvent(this, execution, averageTime));
  }

  @Override
  public void interrupt() throws UnableToInterruptJobException {
    // Do nothing
  }
}
