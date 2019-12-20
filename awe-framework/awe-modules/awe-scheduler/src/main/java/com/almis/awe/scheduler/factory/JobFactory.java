package com.almis.awe.scheduler.factory;

import com.almis.awe.exception.AWException;
import com.almis.awe.scheduler.enums.JobType;
import com.almis.awe.scheduler.job.execution.ProgressJob;
import com.almis.awe.scheduler.job.execution.TimeoutJob;
import com.almis.awe.scheduler.job.scheduled.CommandJob;
import com.almis.awe.scheduler.job.scheduled.MaintainJob;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import static com.almis.awe.scheduler.constant.TaskConstants.TASK_SEPARATOR;

@Log4j2
public class JobFactory {

  private static int instanceId = 1;

  // Private constructor
  private JobFactory() {
  }

  /**
   * Gets the correct job with the given job type
   *
   * @return IExecutionJob
   * @throws AWException
   */
  public static JobDetail getInstance(JobType jobType, JobDataMap dataMap) throws AWException {
    switch (jobType) {
      case JOB_COMMAND:
        return JobBuilder.newJob().ofType(CommandJob.class)
          .withIdentity(dataMap.get("id") + TASK_SEPARATOR + instanceId++, "COMMAND")
          .setJobData(dataMap)
          .build();
      case JOB_MAINTAIN:
        return JobBuilder.newJob().ofType(MaintainJob.class)
          .withIdentity(dataMap.get("id") + TASK_SEPARATOR + instanceId++, "MAINTAIN")
          .setJobData(dataMap).build();
      case JOB_TIMEOUT:
        return JobBuilder.newJob().ofType(TimeoutJob.class)
          .withIdentity((String) dataMap.get("id"), "TIMEOUT")
          .setJobData(dataMap)
          .build();
      case JOB_PROGRESS:
        return JobBuilder.newJob().ofType(ProgressJob.class)
          .withIdentity((String) dataMap.get("id"), "PROGRESS")
          .setJobData(dataMap)
          .build();
      default:
        log.error("The job type does not exist: {}", jobType);
        throw new AWException("The job type does not exist");
    }
  }
}
