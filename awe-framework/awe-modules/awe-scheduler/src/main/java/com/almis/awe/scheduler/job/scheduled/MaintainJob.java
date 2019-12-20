package com.almis.awe.scheduler.job.scheduled;

import com.almis.awe.scheduler.service.MaintainJobService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class implements Quartz job Launch a batch thread
 *
 * @author pvidal
 */
@Component
@Log4j2
public class MaintainJob extends SchedulerJob {
  /**
   * Autowired constructor
   *
   * @param jobService
   */
  @Autowired
  public MaintainJob(MaintainJobService jobService) {
    super(jobService);
  }
}
