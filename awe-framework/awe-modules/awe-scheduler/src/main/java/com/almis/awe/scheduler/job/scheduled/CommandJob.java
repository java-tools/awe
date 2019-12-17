package com.almis.awe.scheduler.job.scheduled;

import com.almis.awe.scheduler.service.CommandJobService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pvidal
 */
@Component
@Log4j2
public class CommandJob extends SchedulerJob {
  /**
   * Autowired constructor
   *
   * @param jobService
   */
  @Autowired
  public CommandJob(CommandJobService jobService) {
    super(jobService);
  }
}
