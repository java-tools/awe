package com.almis.awe.scheduler.job.scheduled;

import com.almis.awe.scheduler.service.CommandJobService;
import lombok.extern.log4j.Log4j2;

/**
 * @author pvidal
 */
@Log4j2
public class CommandJob extends SchedulerJob {
  /**
   * Autowired constructor
   *
   * @param jobService Command job service
   */
  public CommandJob(CommandJobService jobService) {
    super(jobService);
  }
}
