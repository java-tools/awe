package com.almis.awe.scheduler.builder.cron;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.scheduler.bean.calendar.Schedule;
import com.almis.awe.scheduler.bean.trigger.CronPattern;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.quartz.CronScheduleBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.almis.awe.scheduler.constant.CronConstants.*;

/**
 * @author dfuentes
 */
@Data
@Log4j2
public class CronPatternBuilder implements Serializable {
  // Schedule
  private Schedule schedule;

  /**
   * CronPattern
   *
   * @param schedule
   */
  public CronPatternBuilder(Schedule schedule) {
    this.schedule = schedule;
  }

  /**
   * Sets the value of each parameter depending on the selected value of 'Repeat
   * type' wich can be: minutes,hours,days,week or years
   *
   * @throws AWException
   */
  public CronScheduleBuilder build() throws AWException {
    CronPattern cronPattern = new CronPattern();

    // get the repeat number for the selected parameter
    Integer repeatNumber = schedule.getRepeatNumber();
    // Set parameters to create the cron trigger
    switch (schedule.getRepeatType()) {
      // days
      case 3:
        setTime(cronPattern);
        setDayOfWeek(cronPattern, schedule.getWeekDayList(), schedule.getWeekList(), IGNORE);
        cronPattern.setMonth(getListAsString(schedule.getMonthList(), ALL));
        setDayOfMonth(cronPattern, repeatEach(repeatNumber));
        log.debug("[SCHEDULER][CRON PATTERN] Pattern parameters loaded for \"days\" cron pattern");
        break;

      // months
      case 4:
        setTime(cronPattern);
        setDayOfWeek(cronPattern, schedule.getWeekDayList(), schedule.getWeekList(), IGNORE);
        cronPattern.setMonth(repeatEach(repeatNumber));
        setDayOfMonth(cronPattern, getListAsString(schedule.getDayList(), cronPattern.getDayOfWeek().equalsIgnoreCase(IGNORE) ? FIRST : IGNORE));
        log.debug("[SCHEDULER][CRON PATTERN] Pattern parameters loaded for \"months\" cron pattern");
        break;

      // years
      case 5:
        setTime(cronPattern);
        setDate(cronPattern, repeatEach(repeatNumber));
        log.debug("[SCHEDULER][CRON PATTERN] Pattern parameters loaded for \"years\" cron pattern");
        break;

      // once
      case 6:
        setTime(cronPattern);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        setDate(cronPattern, String.valueOf(cal.get(Calendar.YEAR)));
        log.debug("[SCHEDULER][CRON PATTERN] Pattern parameters loaded for \"once\" cron pattern");
        break;

      // custom
      case 7:
        setTime(cronPattern);
        setDate(cronPattern, ALL);
        log.debug("[SCHEDULER][CRON PATTERN] Pattern parameters loaded for \"custom\" cron pattern");
        break;

      // else
      default:
        log.error("[SCHEDULER][CRON PATTERN] The selected type of cron pattern to load is not valid");
        throw new AWException("The selected option is not valid");
    }

    return CronScheduleBuilder.cronSchedule(cronPattern.buildCronString());
  }

  /**
   * Returns a comma separated String of the given parameter
   *
   * @param emptyValue the optional character to retrieve if none of the days of
   *                   the week is selected
   * @return String
   */
  private String getListAsString(List<String> list, String emptyValue) {
    if (list != null) {
      String value = String.join(",", list);
      if (value.isEmpty()) {
        return emptyValue;
      } else {
        return value;
      }
    } else {
      return emptyValue;
    }
  }


  /**
   * Set days of month parameter checking the days of week parameter
   *
   * @param dayOfMonth
   */
  private void setDayOfMonth(CronPattern cronPattern, String dayOfMonth) {
    cronPattern.setDayOfMonth(dayOfMonth);
  }

  /**
   * Set days of week parameter checking the days of month parameter
   *
   * @param daysOfWeek
   * @param weeks
   */
  private void setDayOfWeek(CronPattern cronPattern, List<String> daysOfWeek, List<String> weeks, String defaultDayOfWeek) {
    String dayOfWeekPattern;
    if (isNullOrEmpty(daysOfWeek) && isNullOrEmpty(weeks)) {
      dayOfWeekPattern = defaultDayOfWeek;
    } else if (isNullOrEmpty(daysOfWeek)) {
      dayOfWeekPattern = String.join(",", weeks);
    } else if (isNullOrEmpty(weeks)) {
      dayOfWeekPattern = String.join(",", daysOfWeek);
    } else {
      dayOfWeekPattern = daysOfWeek.stream().map(day -> weeks.stream().map(week -> day + week).collect(Collectors.joining(","))).collect(Collectors.joining(","));
    }

    // Set pattern
    cronPattern.setDayOfWeek(dayOfWeekPattern);
  }

  private boolean isNullOrEmpty(List<String> list) {
    return list == null || list.isEmpty();
  }

  /**
   * Returns a String with the pattern to repeat a parameter a number of times
   * with a starting point
   *
   * @param start
   * @param repeatEach
   * @return String
   */
  public String repeatEach(int start, int repeatEach) {
    return start + SLASH + repeatEach;
  }

  /**
   * Returns a String with the pattern to repeat a parameter a number of times
   *
   * @param repeatEach
   * @return String
   */
  private String repeatEach(int repeatEach) {
    return ALL + SLASH + repeatEach;
  }

  /**
   * Sets the date of execution
   *
   * @param option number of time to repeat
   */
  private void setDate(CronPattern cronPattern, String option) {
    String year;
    String month;
    String day;
    List<String> weekday;
    String defaultDayOfWeek;
    // Get execution date criterion value
    Date date = schedule.getDateTime();

    // Check if the date is before current date
    if (date != null && date.before(new Date())) {
      date = new Date();
    } // if the value is null then check values from individual values

    if (date == null) {
      year = getListAsString(schedule.getYearList(), option);
      month = getListAsString(schedule.getMonthList(), ALL);
      weekday = schedule.getWeekDayList();
      defaultDayOfWeek = IGNORE;
      day = getListAsString(schedule.getDayList(), IGNORE);
    } else {
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      year = String.valueOf(cal.get(Calendar.YEAR));
      month = String.valueOf(cal.get(Calendar.MONTH) + 1);
      weekday = new ArrayList<>();
      defaultDayOfWeek = IGNORE;
      day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    } // set values
    cronPattern.setYear(year);
    cronPattern.setMonth(month);
    setDayOfWeek(cronPattern, weekday, schedule.getWeekList(), defaultDayOfWeek);
    setDayOfMonth(cronPattern, day);

    // Fix for day of week
    if (IGNORE.equalsIgnoreCase(cronPattern.getDayOfMonth()) &&
      IGNORE.equalsIgnoreCase(cronPattern.getDayOfWeek())) {
      cronPattern.setDayOfWeek(ALL);
    }
  }

  /**
   * Sets the time of execution
   *
   * @return String
   */
  private void setTime(CronPattern cronPattern) {
    String hour;
    String min;
    String sec;
    // Get execution time criterion value
    String time = schedule.getDate() == null ? null : DateUtil.dat2WebTime(schedule.getDateTime());
    // if the value is null the check values from individual hour, minute and
    // second values
    if (time == null) {
      hour = getListAsString(schedule.getHourList(), DEFAULT);
      min = getListAsString(schedule.getMinuteList(), DEFAULT);
      sec = getListAsString(schedule.getSecondList(), DEFAULT);
    } else {
      hour = time.split(DATE_REGEX)[0];
      min = time.split(DATE_REGEX)[1];
      sec = time.split(DATE_REGEX)[2];
    }
    // set hour, minute and second values
    cronPattern.setSeconds(sec);
    cronPattern.setMinutes(min);
    cronPattern.setHours(hour);
  }
}
