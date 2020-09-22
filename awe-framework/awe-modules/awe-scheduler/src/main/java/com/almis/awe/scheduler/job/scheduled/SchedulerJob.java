package com.almis.awe.scheduler.job.scheduled;

import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import com.almis.awe.scheduler.constant.JobConstants;
import com.almis.awe.scheduler.service.JobService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;

@Log4j2
@Getter
@Setter
public abstract class SchedulerJob implements InterruptableJob {

  private Task task;
  private TaskExecution execution;

  // Autowired services
  private JobService jobService;

  /**
   * Constructor
   *
   * @param jobService Job service
   */
  public SchedulerJob(JobService jobService) {
    this.jobService = jobService;
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
    setTask((Task) dataMap.get(JobConstants.TASK));

    try {
      setExecution(jobService.startTask(task));
      Future<ServiceData> result = jobService.executeJob(getTask(), getExecution(), dataMap);
      jobService.launchBatch(this, result);
    } catch (CancellationException exc) {
      log.warn("[SCHEDULER] Task was cancelled: {}", context.getTrigger().getKey().toString(), exc);
    } catch (Exception exc) {
      log.error("[SCHEDULER] Error on task execution: {}", context.getTrigger().getKey().toString(), exc);
      throw new JobExecutionException(exc.getMessage(), exc);
    }
  }

  @Override
  public void interrupt() {
    // Do nothing
  }
}
