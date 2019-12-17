package com.almis.awe.scheduler.listener;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.scheduler.bean.event.SchedulerTaskFinishedEvent;
import com.almis.awe.scheduler.bean.event.SchedulerTaskStartedEvent;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.job.scheduled.SchedulerJob;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import static com.almis.awe.scheduler.constant.JobConstants.TASK;
import static com.almis.awe.scheduler.constant.ListenerConstants.JOB_LISTENER_NAME;

/**
 * @author dfuentes
 */
@Log4j2
public class SchedulerJobListener extends ServiceConfig implements JobListener {

  // Autowired services
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  public SchedulerJobListener(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Override
  public String getName() {
    return JOB_LISTENER_NAME;
  }

  @Override
  public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
    // Fire event
    if (jobExecutionContext.getJobInstance() instanceof SchedulerJob) {
      Task task = (Task) jobExecutionContext.getJobDetail().getJobDataMap().get(TASK);
      eventPublisher.publishEvent(new SchedulerTaskStartedEvent(this, task));
    }
  }

  @Override
  public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
    // DO nothing
  }

  @Override
  public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException exception) {
    // Publish event
    if (jobExecutionContext.getJobInstance() instanceof SchedulerJob) {
      SchedulerJob job = (SchedulerJob) jobExecutionContext.getJobInstance();
      eventPublisher.publishEvent(new SchedulerTaskFinishedEvent(this, job.getTask(), job.getExecution(), job.getExecution().getExecutionTime()));
    }
  }
}
