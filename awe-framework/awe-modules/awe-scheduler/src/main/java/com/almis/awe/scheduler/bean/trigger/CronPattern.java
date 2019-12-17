package com.almis.awe.scheduler.bean.trigger;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.almis.awe.scheduler.constant.CronConstants.*;

/**
 * @author dfuentes
 */
@Data
@Log4j2
public class CronPattern implements Serializable {

  // Cron pattern values
  private String seconds = null;
  private String minutes = null;
  private String hours = null;
  private String dayOfMonth = null;
  private String month = null;
  private String dayOfWeek = null;
  private String year = null;

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
  public String repeatEach(int repeatEach) {
    return ALL + SLASH + repeatEach;
  }

  /**
   * Check and correct the set date and time if needed
   */
  private void checkDateTime() {
    Calendar cal = Calendar.getInstance();
    Calendar curDate = Calendar.getInstance();
    curDate.setTime(new Date());
    cal.setTime(new Date());

    try {
      //Create a new Date with the correct format from the variables
      DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

      //Create a date with the correct format from the current date
      curDate.setTime(format.parse(format.format(curDate.getTime())));

      // If the date is today
      setTimeToCalendar(cal, format);
    } catch (ParseException exc) {
      log.error("[SCHEDULER][CRON PATTERN] Error parsing time", exc);
    }
  }

  /**
   * Set time to calendar
   * @param calendar
   * @param format
   */
  private void setTimeToCalendar(Calendar calendar, DateFormat format) {
    try {
      calendar.setTime(format.parse(getDayOfMonth() + "/" + getMonth() + "/" + getYear() + " " + getHours() + ":" + getMinutes() + ":" + getSeconds()));
    } catch (ParseException ex) {
      log.debug("[SCHEDULER][CRON PATTERN] The time is a cron pattern, so it can't be compared to the current date. It's OK");
    }
  }

  /**
   * Builds and returns the cron pattern string
   *
   * @return String
   */
  public String buildCronString() {

    // Checks if the date and time are correct
    checkDateTime();

    // Create a blanck space separated cron pattern string, with the year
    // pattern if it is not null.
    String pattern = seconds + SEPARATOR + minutes + SEPARATOR + hours + SEPARATOR + dayOfMonth + SEPARATOR + month + SEPARATOR + dayOfWeek + (year == null ? "" : SEPARATOR + year);

    log.debug("[SCHEDULER][CRON PATTERN] Cron pattern generated {}", pattern);

    return pattern;
  }
}
