package com.almis.awe.scheduler.constant;

/**
 *
 * @author dfuentes
 */
public class CronConstants {

  private CronConstants(){}

  // Cron Pattern constants
  public static final String CRON_PARAMETER_REPEAT_TYPE = "RptTyp";
  public static final String CRON_PARAMETER_REPEAT_NUMBER = "RptNum";
  public static final String CRON_PARAMETER_CALENDAR_IDE = "IdeCal";
  public static final String CRON_PARAMETER_INITIAL_DATE = "IniDat";
  public static final String CRON_PARAMETER_INITIAL_TIME = "IniTim";
  public static final String CRON_PARAMETER_END_DATE = "EndDat";
  public static final String CRON_PARAMETER_END_TIME = "EndTim";
  public static final String CRON_PARAMETER_DATE = "schExeDate";
  public static final String CRON_PARAMETER_TIME = "schExeTime";
  
  //Cron window parameter constants
  public static final String CRON_PARAMETER_YEARS = "years";
  public static final String CRON_PARAMETER_MONTHS = "months";
  public static final String CRON_PARAMETER_WEEKS = "weeks";
  public static final String CRON_PARAMETER_DAYS = "days";
  public static final String CRON_PARAMETER_WEEKDAYS = "weekDays";
  public static final String CRON_PARAMETER_HOURS = "hours";
  public static final String CRON_PARAMETER_MINUTES = "minutes";
  public static final String CRON_PARAMETER_SECONDS = "seconds";

  // Special characters constants
  public static final String SEPARATOR = " ";
  public static final String FIRST = "1";
  public static final String DEFAULT = "0";
  public static final String ALL = "*";
  public static final String IGNORE = "?";
  public static final String NONE = "";
  public static final String DATE_REGEX = ":";
  public static final String SLASH = "/";

  // Boolean as number constants
  public static final int TRUE = 1;
  public static final int FALSE = 0;
}
