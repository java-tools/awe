package com.almis.awe.scheduler.builder.cron;

import com.almis.awe.exception.AWException;
import com.almis.awe.scheduler.bean.calendar.Schedule;
import lombok.extern.log4j.Log4j2;
import org.quartz.SimpleScheduleBuilder;

@Log4j2
public class SimplePatternBuilder {
  // Schedule
  private Schedule schedule;

  /**
   * SimplePattern
   *
   * @param schedule
   */
  public SimplePatternBuilder(Schedule schedule) {
    this.schedule = schedule;
  }

  /**
   * Sets the value of each parameter depending on the selected value of 'Repeat
   * type' wich can be: minutes,hours,days,week or years
   *
   * @throws AWException
   */
  public SimpleScheduleBuilder build() throws AWException {
    SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();

    // get the repeat number for the selected parameter
    Integer repeatNumber = schedule.getRepeatNumber();
    // Set parameters to create the cron trigger
    switch (schedule.getRepeatType()) {

      // seconds
      case 0:
        scheduleBuilder.withIntervalInSeconds(repeatNumber).repeatForever();
        log.debug("[CRON PATTERN] Pattern parameters loaded for \"seconds\" cron pattern");
        break;

      // minutes
      case 1:
        scheduleBuilder.withIntervalInMinutes(repeatNumber).repeatForever();
        log.debug("[CRON PATTERN] Pattern parameters loaded for \"minutes\" cron pattern");
        break;

      // hours
      case 2:
        scheduleBuilder.withIntervalInHours(repeatNumber).repeatForever();
        log.debug("[CRON PATTERN] Pattern parameters loaded for \"hours\" cron pattern");
        break;

      // else
      default:
        log.error("[CRON PATTERN] The selected type of cron pattern to load is not valid");
        throw new AWException("The selected option is not valid");
    }

    return scheduleBuilder;
  }
}
