package com.almis.awe.model.util.data;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Log4j2
public class TimeUtil {

  private static final String UNIT_TIME_FORMAT_HOURS = "%dh";
  private static final String UNIT_TIME_FORMAT_MIN = "%dm";
  private static final String UNIT_TIME_FORMAT_SEC = "%ds";
  private static final String UNIT_TIME_FORMAT_MILI = "%dms";

  // Private constructor
  private TimeUtil() {
  }

  public static String formatTime(Integer milliseconds) {
    return formatTime(milliseconds, true);
  }

  /**
   * Formats the milliseconds as human readable String
   *
   * @param milliseconds
   * @param showMs Show milliseconds
   * @return String
   */
  public static String formatTime(Integer milliseconds, boolean showMs) {
    List<String> timeList = new ArrayList<>();
    if (milliseconds != null) {
      // format the time into an human readable format
      long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
      long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));
      long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));
      long ms = TimeUnit.MILLISECONDS.toMillis(milliseconds) - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(milliseconds));

      // Set the appropriate pattern for formatting the time to a human readable format
      if (hours > 0) {
        timeList.add(String.format(UNIT_TIME_FORMAT_HOURS, hours));
      }

      if (minutes > 0) {
        timeList.add(String.format(UNIT_TIME_FORMAT_MIN, minutes));
      }

      if (seconds > 0) {
        timeList.add(String.format(UNIT_TIME_FORMAT_SEC, seconds));
      }

      if (showMs && ms > 0) {
        timeList.add(String.format(UNIT_TIME_FORMAT_MILI, ms));
      }

      return String.join(" ", timeList);
    } else {
      return "0";
    }
  }
}
