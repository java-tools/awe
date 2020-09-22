package com.almis.awe.scheduler.job.scheduled;

import com.almis.awe.scheduler.service.MaintainJobService;
import lombok.extern.log4j.Log4j2;

/**
 * Class implements Quartz job Launch a batch thread
 *
 * @author pvidal
 */
@Log4j2
public class MaintainJob extends SchedulerJob {
  /**
   * Autowired constructor
   *
   * @param jobService
   */
  public MaintainJob(MaintainJobService jobService) {
    super(jobService);
  }
}
