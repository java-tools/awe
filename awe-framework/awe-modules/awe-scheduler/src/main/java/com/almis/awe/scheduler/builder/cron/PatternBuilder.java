package com.almis.awe.scheduler.builder.cron;

import com.almis.awe.exception.AWException;
import com.almis.awe.scheduler.bean.calendar.Schedule;
import lombok.extern.log4j.Log4j2;
import org.quartz.ScheduleBuilder;

/**
 * Default pattern builder
 */
@Log4j2
public class PatternBuilder {
  private Schedule schedule;

  /**
   * Constructor
   * @param schedule
   */
  public PatternBuilder(Schedule schedule) {
    this.schedule = schedule;
  }

  /**
   * Build schedule builder
   * @return
   * @throws AWException
   */
  public ScheduleBuilder build() throws AWException {
    switch (schedule.getRepeatType()) {
      case 0:
      case 1:
      case 2:
        return new SimplePatternBuilder(schedule).build();
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
        return new CronPatternBuilder(schedule).build();
      default:
        log.error("[SCHEDULER][PATTERN] The selected type of pattern to load is not valid: {}", schedule.getRepeatType());
        throw new AWException("The selected option is not valid");
    }
  }
}
