package com.almis.awe.model.util.data;

/*
 * File Imports
 */

import com.almis.awe.model.component.AweElements;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.util.datetime.FastDateFormat;

import java.text.ParseException;
import java.util.*;

/**
 * DateUtil Class
 * Date Utilities for AWE
 *
 * @author Pablo GARCIA and Pablo Vidal - 13/JUL/2014
 */
@Log4j2
public final class DateUtil {

  /**
   * Private constructor to enclose the default one
   */
  private DateUtil() {
  }

  /* Util name */
  private static final String UTILITY_NAME = "DATE UTILITY";

  /* Date in Web Format */
  private static final FastDateFormat DATE_FORMAT_WEB = FastDateFormat.getInstance("dd/MM/yyyy");

  /* Date in Js Format */
  private static final FastDateFormat DATE_FORMAT_JS = FastDateFormat.getInstance("MM/dd/yyyy");

  /* Date in SQL Format */
  private static final FastDateFormat DATE_FORMAT_SQL = FastDateFormat.getInstance("yyyy-MM-dd");

  /* Date in Web Service Format */
  private static final FastDateFormat DATE_FORMAT_WBS = FastDateFormat.getInstance("yyyy-MM-dd");

  /* Date in RDB Format */
  private static final FastDateFormat DATE_FORMAT_RDB = FastDateFormat.getInstance("dd-MMM-yyyy", Locale.ENGLISH);

  /* Timestamp in SQL Format */
  private static final FastDateFormat TMST_FORMAT_SQL_MS = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.S");

  /* Time in Web Format */
  private static final FastDateFormat TIME_FORMAT_WEB = FastDateFormat.getInstance("HH:mm:ss");

  /* Timestamp in Web Format */
  private static final FastDateFormat TMST_FORMAT_WEB = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss");

  /* Timestamp in Js Format */
  private static final FastDateFormat TMST_FORMAT_JS = FastDateFormat.getInstance("MM/dd/yyyy HH:mm:ss");

  /* Timestamp in Web Format with milliseconds */
  private static final FastDateFormat TMST_FORMAT_WEB_MS = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss.S");

  /* Timestamp in Web Format with milliseconds */
  private static final FastDateFormat JSON_DATE = FastDateFormat.getInstance("yyyy-MM-dd@HH:mm:ss.SSSZ");

  /**
   * Transforms a web date into a SQL Date
   *
   * @param date Web Date
   * @return SQL Date
   * @throws ParseException Parse error
   */
  public static java.sql.Date web2SqlDate(String date) throws ParseException {

    /* Variable definition */
    java.sql.Date sqlDat;
    java.util.Date webDat;

    /* Parse initial date */
    webDat = DATE_FORMAT_WEB.parse(date);

    /* Convert to sql Date */
    sqlDat = java.sql.Date.valueOf(DATE_FORMAT_SQL.format(webDat));

    /* Return sql Date */
    return sqlDat;
  }

  /**
   * Transforms a web timestamp into a SQL Timestamp
   *
   * @param time Web Timestamp
   * @return SQL Date
   * @throws ParseException Parse error
   */
  public static java.sql.Time web2SqlTime(String time) throws ParseException {

    /* Variable definition */
    java.sql.Time sqlDat;
    java.util.Date webDat;

    /* Parse initial date */
    webDat = TIME_FORMAT_WEB.parse(time);

    /* Convert to sql Time */
    sqlDat = java.sql.Time.valueOf("00:00:00");
    sqlDat.setTime(webDat.getTime());

    /* Return sql Date */
    return sqlDat;
  }

  /**
   * Transforms a web date into a SQL Timestamp
   *
   * @param date Web Date
   * @return SQL Date
   * @throws ParseException Parse error
   */
  public static java.sql.Timestamp webDate2SqlTimestamp(String date) throws ParseException {

    // Variable definition
    java.sql.Timestamp sqlDat;
    java.util.Date webDat;

    // Parse initial date
    webDat = DATE_FORMAT_WEB.parse(date);

    // Convert to sql Time
    sqlDat = java.sql.Timestamp.valueOf(TMST_FORMAT_SQL_MS.format(webDat));
    sqlDat.setTime(webDat.getTime());

    // Return sql Date
    return sqlDat;
  }

  /**
   * Transforms a web timestamp into a SQL Timestamp
   *
   * @param timestamp Web Timestamp
   * @return SQL Date
   * @throws ParseException Parse error
   */
  public static java.sql.Timestamp web2SqlTimestamp(String timestamp) throws ParseException {

    /* Variable definition */
    java.sql.Timestamp sqlDat;
    java.util.Date webDat;

    /* Parse initial date */
    webDat = TMST_FORMAT_WEB.parse(timestamp);

    /* Convert to sql Time */
    sqlDat = java.sql.Timestamp.valueOf(TMST_FORMAT_SQL_MS.format(webDat));
    sqlDat.setTime(webDat.getTime());

    /* Return sql Date */
    return sqlDat;
  }

  /**
   * Transforms a web date into a Date
   *
   * @param date Web Date
   * @return SQL Date
   */
  public static java.util.Date web2Date(String date) {

    /* Variable definition */
    java.util.Date webDat = null;

    try {
      /* Parse initial date */
      if (date != null && !"".equalsIgnoreCase(date)) {
        webDat = DATE_FORMAT_WEB.parse(date);
      }
    } catch (Exception exc) {
      log.error("[{}] Error parsing WEB date to date -{}-", UTILITY_NAME, date, exc);
    }

    /* Return sql Date */
    return webDat;
  }

  /**
   * Transforms a web time into a Date
   *
   * @param date Web Date
   * @return SQL Date
   */
  public static java.util.Date web2Time(String date) {

    /* Variable definition */
    java.util.Date webDat;

    try {
      /* Parse initial date */
      webDat = TIME_FORMAT_WEB.parse(date);
    } catch (Exception exc) {
      webDat = null;
      log.error("[{}] Error parsing WEB TIME to date -{}-", UTILITY_NAME, date, exc);
    }

    /* Return sql Date */
    return webDat;
  }

  /**
   * Transforms a web timestamp into a Date
   *
   * @param date Web Date
   * @return SQL Date
   */
  public static java.util.Date web2Timestamp(String date) {

    /* Variable definition */
    java.util.Date webDat = null;

    try {
      if (date != null) {
        /* Parse initial date */
        webDat = TMST_FORMAT_WEB.parse(date);
      }
    } catch (Exception exc) {
      log.error("[{}] Error parsing WEB TIMESTAMP to date -{}-", UTILITY_NAME, date, exc);
    }

    /* Return sql Date */
    return webDat;
  }

  /**
   * Transforms a web timestamp with ms into a Date
   *
   * @param date Web Date
   * @return SQL Date
   */
  public static java.util.Date web2TimestampWithMs(String date) {

    /* Variable definition */
    java.util.Date webDat;

    try {
      /* Parse initial date */
      webDat = TMST_FORMAT_WEB_MS.parse(date);
    } catch (Exception exc) {
      webDat = null;
      log.error("[{}] Error parsing WEB TIMESTAMP WITH MS to date -{}-", UTILITY_NAME, date, exc);
    }

    /* Return sql Date */
    return webDat;
  }

  /**
   * Transforms a date into a SQL Date
   *
   * @param date Date
   * @return SQL Date
   */
  public static java.sql.Date dat2SqlDate(java.util.Date date) {
    /* Return sql Date */
    return java.sql.Date.valueOf(DATE_FORMAT_SQL.format(date));
  }

  /**
   * Transforms a sql timestamp into a java Date
   *
   * @param timestamp Date
   * @return SQL Date
   */
  public static java.util.Date sqlTimestamp2Date(java.sql.Timestamp timestamp) {

    Date dateValue = null;

    if (timestamp != null) {
      dateValue = new java.util.Date(timestamp.getTime());
    }
    return dateValue;
  }

  /**
   * Transforms a sql date into a java Date
   *
   * @param date Date
   * @return SQL Date
   */
  public static java.util.Date sqlDate2Date(java.sql.Date date) {

    Date dateValue = null;

    if (date != null) {
      dateValue = new java.util.Date(date.getTime());
    }
    return dateValue;
  }

  /**
   * Transforms a date into a SQL Time
   *
   * @param date Date
   * @return SQL Timestamp
   */
  public static java.sql.Timestamp dat2SqlTime(java.util.Date date) {
    /* Return sql timestamp */
    return java.sql.Timestamp.valueOf(TMST_FORMAT_SQL_MS.format(date));
  }

  /**
   * Transforms a date into a SQL Time string
   *
   * @param date Date
   * @return SQL Timestamp
   */
  public static String dat2SqlTimeString(java.util.Date date) {
    /* Return sql timestamp */
    return TMST_FORMAT_SQL_MS.format(date);
  }

  /**
   * Transforms a date into a Web Timestamp
   *
   * @param date Date
   * @return WEB Timestamp
   */
  public static String dat2WebTimestamp(java.util.Date date) {
    /* Convert to web timestamp */
    return TMST_FORMAT_WEB.format(date);
  }

  /**
   * Transforms a date into a Web Timestamp with milliseconds
   *
   * @param date Date
   * @return WEB Timestamp
   */
  public static String dat2WebTimestampMs(java.util.Date date) {
    /* Convert to web timestamp */
    return TMST_FORMAT_WEB_MS.format(date);
  }

  /**
   * Transforms a date into a Web Time
   *
   * @param date Date
   * @return WEB Time
   */
  public static String dat2WebTime(java.util.Date date) {
    /* Convert to web timestamp */
    return TIME_FORMAT_WEB.format(date);
  }

  /**
   * Transforms a date into a Web Date
   *
   * @param date Date
   * @return WEB Time
   */
  public static String dat2WebDate(java.util.Date date) {
    /* Convert to web timestamp */
    return DATE_FORMAT_WEB.format(date);
  }

  /**
   * Transforms a java Date into a date in milliseconds (Used in charts)
   *
   * @param date Date
   * @return string date in milliseconds
   */
  public static String dat2DateMs(java.util.Date date) {
    // Convert to date milliseconds
    return String.valueOf(date.getTime());
  }

  /**
   * Transforms a SQL String date into a web date
   *
   * @param date SQL String Date
   * @return Web date formatted
   */
  public static String sql2WebDate(String date) {

    /* Variable definition */
    java.util.Date webDat;
    String outDat;

    try {
      /* Parse initial date */
      webDat = TMST_FORMAT_SQL_MS.parse(date);
      outDat = DATE_FORMAT_WEB.format(webDat);
    } catch (Exception exc) {
      outDat = date;
      log.error("[{}] Error parsing SQL date to WEB date -{}-", UTILITY_NAME, date, exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Transforms a SQL String date into a java date
   *
   * @param date SQL String Date
   * @return Web date formatted
   * @throws ParseException Parse error
   */
  public static java.util.Date sql2JavaDate(String date) throws ParseException {
    /* Return web date string */
    return TMST_FORMAT_SQL_MS.parse(date);
  }

  /**
   * Transforms a SQL String date (without time) into a java date
   *
   * @param date SQL String Date
   * @return Web date formatted
   * @throws ParseException Parse error
   */
  public static java.util.Date sqlDate2JavaDate(String date) throws ParseException {
    /* Return web date string */
    return DATE_FORMAT_SQL.parse(date);
  }

  /**
   * Transforms a WBS String date into a java date
   *
   * @param date SQL String Date
   * @return Web date formatted
   * @throws ParseException Parse error
   */
  public static java.util.Date wbs2JavaDate(String date) throws ParseException {
    /* Return web date string */
    return DATE_FORMAT_WBS.parse(date);
  }

  /**
   * Transforms a RDB String date into a java date
   *
   * @param date SQL String Date
   * @return Web date formatted
   * @throws ParseException Parse error
   */
  public static java.util.Date rdb2Date(String date) throws ParseException {
    /* Return web date string */
    return DATE_FORMAT_RDB.parse(date);
  }

  /**
   * Transforms a SQL String date into a web time
   *
   * @param date SQL String Date
   * @return Web time formatted
   * @throws ParseException Parse error
   */
  public static String sql2WebTime(String date) throws ParseException {

    /* Variable definition */
    java.util.Date webDat;
    String outDat;

    try {
      /* Parse initial date */
      webDat = TMST_FORMAT_SQL_MS.parse(date);
      outDat = TIME_FORMAT_WEB.format(webDat);
    } catch (Exception exc) {
      outDat = date;
      log.error("[{}] Error parsing SQL date to WEB TIME -{}-", UTILITY_NAME, date, exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Transforms a SQL String date into a web timestamp
   *
   * @param date SQL String Date
   * @return Web timestamp formatted
   * @throws ParseException Parse error
   */
  public static String sql2WebTimestamp(String date) throws ParseException {

    /* Variable definition */
    java.util.Date webDat;
    String outDat;

    try {
      /* Parse initial date */
      webDat = TMST_FORMAT_SQL_MS.parse(date);
      outDat = TMST_FORMAT_WEB.format(webDat);
    } catch (Exception exc) {
      outDat = date;
      log.error("[{}] Error parsing SQL date to WEB TIMESTAMP -{}-", UTILITY_NAME, date, exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Transforms a SQL String date into a js date
   *
   * @param date SQL String Date
   * @return Web date formatted
   * @throws ParseException Parse error
   */
  public static String sql2JsDate(String date) throws ParseException {

    /* Variable definition */
    java.util.Date webDat;
    String outDat;

    try {
      /* Parse initial date */
      webDat = TMST_FORMAT_SQL_MS.parse(date);
      outDat = DATE_FORMAT_JS.format(webDat);
    } catch (Exception exc) {
      outDat = date;
      log.error("[{}] Error parsing SQL date to JS date -{}-", UTILITY_NAME, date, exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Parse sql date to java date in milliseconds
   *
   * @param val Sql date
   * @return Java date in milliseconds
   */
  public static String sql2DateMs(String val) {
    String dateMs = null;
    try {
      Date date = sql2JavaDate(val);
      dateMs = dat2DateMs(date);
    } catch (ParseException ex) {
      log.error("[{}] Error parsing SQL date to Java date in milliseconds -{}-", UTILITY_NAME, ex);
    }
    return dateMs;
  }

  /**
   * Transforms a SQL String date into a js timestamp
   *
   * @param date SQL String Date
   * @return Web timestamp formatted
   * @throws ParseException Parse error
   */
  public static String sql2JsTimestamp(String date) throws ParseException {

    /* Variable definition */
    java.util.Date webDat;
    String outDat;

    try {
      /* Parse initial date */
      webDat = TMST_FORMAT_SQL_MS.parse(date);
      outDat = TMST_FORMAT_JS.format(webDat);
    } catch (Exception exc) {
      outDat = date;
      log.error("[{}] Error parsing SQL date to JS TIMESTAMP -{}-", UTILITY_NAME, date, exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Transforms a SQL String date into a web date
   *
   * @param date SQL String Date
   * @return Web date formatted
   */
  public static String sqlDat2WebDate(java.sql.Date date) {

    /* Variable definition */
    java.util.Date webDat = dat2SqlDate(date);
    String outDat = null;

    try {
      /* Parse initial date */
      outDat = DATE_FORMAT_WEB.format(webDat);
    } catch (Exception exc) {
      log.error("[{}] Error parsing SQL date to WEB date -{}-", UTILITY_NAME, date.toString(), exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Transforms a SQL String date into a web time
   *
   * @param date SQL String Date
   * @return Web time formatted
   */
  public static String sqlDat2WebTime(java.sql.Time date) {

    /* Variable definition */
    java.util.Date webDat = dat2SqlTime(date);
    String outDat = null;

    try {
      /* Parse initial date */
      outDat = TIME_FORMAT_WEB.format(webDat);
    } catch (Exception exc) {
      log.error("[{}] Error parsing SQL date to WEB TIME -{}-", UTILITY_NAME, date.toString(), exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Transforms a SQL String date into a web timestamp
   *
   * @param date SQL String Date
   * @return Web timestamp formatted
   */
  public static String sqlDat2WebTimestamp(java.sql.Timestamp date) {

    /* Variable definition */
    java.util.Date webDat = dat2SqlTime(date);
    String outDat = null;

    try {
      /* Parse initial date */
      outDat = TMST_FORMAT_WEB.format(webDat);
    } catch (Exception exc) {
      log.error("[{}] Error parsing SQL date to WEB TIMESTAMP -{}-", UTILITY_NAME, date.toString(), exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Transforms a SQL String date into a web date
   *
   * @param date SQL String Date
   * @return Web date formatted
   */
  public static String sqlDat2JsDate(java.sql.Date date) {

    /* Variable definition */
    java.util.Date webDat = dat2SqlDate(date);
    String outDat = null;

    try {
      /* Parse initial date */
      outDat = DATE_FORMAT_JS.format(webDat);
    } catch (Exception exc) {
      log.error("[{}] Error parsing SQL date to JS date -{}-", UTILITY_NAME, date.toString(), exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Transforms a SQL String date into a js timestamp
   *
   * @param date SQL String Date
   * @return Web timestamp formatted
   */
  public static String sqlDat2JsTimestamp(java.sql.Timestamp date) {

    /* Variable definition */
    java.util.Date webDat = dat2SqlTime(date);
    String outDat = null;

    try {
      /* Parse initial date */
      outDat = TMST_FORMAT_JS.format(webDat);
    } catch (Exception exc) {
      log.error("[{}] Error parsing SQL date to JS TIMESTAMP -{}-", UTILITY_NAME, date.toString(), exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Transforms a Date into a js date
   *
   * @param date SQL String Date
   * @return Web timestamp formatted
   */
  public static String dat2JsDate(java.util.Date date) {
    /* Return js date */
    return DATE_FORMAT_JS.format(date);
  }

  /**
   * Transforms a Date into a js timestamp
   *
   * @param date SQL String Date
   * @return Web timestamp formatted
   */
  public static String dat2JsTimestamp(java.util.Date date) {
    /* Return web date string */
    return TMST_FORMAT_JS.format(date);
  }

  /**
   * Transforms a SQL String date into a web timestamp with Milliseconds
   *
   * @param date SQL String Date
   * @return Web timestamp formatted
   */
  public static String sqlDat2WebTimestampWithMs(java.sql.Timestamp date) {

    /* Variable definition */
    String outDat = null;

    try {
      java.util.Date webDat = dat2SqlTime(date);

      /* Parse initial date */
      outDat = TMST_FORMAT_WEB_MS.format(webDat);

    } catch (Exception exc) {
      log.error("[{}] Error parsing SQL date to WEB TIMESTAMP WITH MS -{}-", UTILITY_NAME, date.toString(), exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Transforms a web date into a web service date
   *
   * @param date (Web formatted)
   * @return Web Service date formatted
   */
  public static String web2WbsDate(String date) {

    /* Variable definition */
    java.util.Date webDat;
    String outDat;

    try {
      /* Parse initial date */
      webDat = DATE_FORMAT_WEB.parse(date);
      outDat = DATE_FORMAT_WBS.format(webDat);
    } catch (Exception exc) {
      outDat = date;
      log.error("[{}] Error parsing WEB date to WBS date -{}-", UTILITY_NAME, date, exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Transforms a date into a web service date
   *
   * @param date (Web formatted)
   * @return Web Service date formatted
   */
  public static String dat2WbsDate(java.util.Date date) {

    /* Variable definition */
    String outDat;

    try {
      /* Parse initial date */
      outDat = DATE_FORMAT_WBS.format(date);
    } catch (Exception exc) {
      outDat = date.toString();
      log.error("[{}] Error parsing date to WBS date -{}-", UTILITY_NAME, date, exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Transforms a web date into a RDB date (23-OCT-1978)
   *
   * @param date (Web formatted)
   * @return RDB date formatted
   */
  public static String web2RdbDate(String date) {

    /* Variable definition */
    java.util.Date webDat;
    String outDat;

    try {
      /* Parse initial date */
      webDat = DATE_FORMAT_WEB.parse(date);

      /* Generate rdb date string */
      outDat = DATE_FORMAT_RDB.format(webDat);
    } catch (Exception exc) {
      outDat = date;
      log.error("[{}] Error parsing WEB date to RDB date -{}-", UTILITY_NAME, date, exc);
    }

    /* Return rdb date string in UPPERCASE */
    return outDat.toUpperCase();
  }

  /**
   * Transforms a web service date into a web date
   *
   * @param date (Web service formatted)
   * @return Web date formatted
   * @throws ParseException Parse error
   */
  public static String wbs2WebDate(String date) throws ParseException {

    /* Variable definition */
    java.util.Date wbsDat;
    String outDat;

    try {
      /* Parse initial date */
      wbsDat = DATE_FORMAT_WBS.parse(date);
      outDat = DATE_FORMAT_WEB.format(wbsDat);
    } catch (Exception exc) {
      outDat = date;
      log.error("[{}] Error parsing WBS date to WEB date -{}-", UTILITY_NAME, date, exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Transforms a web service date into a js date
   *
   * @param date (Web service formatted)
   * @return Web date formatted
   * @throws ParseException Parse error
   */
  public static String wbs2JsDate(String date) throws ParseException {

    /* Variable definition */
    java.util.Date wbsDat;
    String outDat;

    try {
      /* Parse initial date */
      wbsDat = DATE_FORMAT_WBS.parse(date);
      outDat = DATE_FORMAT_JS.format(wbsDat);
    } catch (Exception exc) {
      outDat = date;
      log.error("[{}] Error parsing WBS date to JS date -{}-", UTILITY_NAME, date, exc);
    }

    /* Return web date string */
    return outDat;
  }

  /**
   * Returns system date in SQL format
   *
   * @return String SQL system date
   */
  public static String getSystemDate() {

    /* Variable definition */
    String sysDat;
    Calendar currentDate = Calendar.getInstance();

    /* Convert to sql Date */
    sysDat = TMST_FORMAT_SQL_MS.format(currentDate.getTime());

    /* Return sql Date */
    return sysDat;
  }

  /**
   * Returns Calendar object for string Date with SQL format
   *
   * @param strSqlDate SQL Date
   * @return Calendar object
   */
  public static Calendar getCalendarDate(String strSqlDate) {

    // Variable definition
    Calendar cal = Calendar.getInstance();

    // Set date
    cal.setTime(web2Date(strSqlDate));

    return cal;
  }

  /**
   * Returns true if date is an WBS date
   *
   * @param date (Web service formatted)
   * @return Is an WBS date
   */
  public static boolean isWbsDate(String date) {
    return isXXX(date, DATE_FORMAT_WBS);
  }

  /**
   * Returns true if date is an SQL date
   *
   * @param date (Web service formatted)
   * @return Is an SQL date
   */
  public static boolean isSqlDate(String date) {
    return isXXX(date, TMST_FORMAT_SQL_MS);
  }

  /**
   * Returns true if date is an Web timestamp
   *
   * @param date (Web service formatted)
   * @return Is an WBS date
   */
  public static boolean isWebTimestamp(String date) {
    return isXXX(date, TMST_FORMAT_WEB);
  }

  /**
   * Returns true if date is an Web date with milliseconds
   *
   * @param date (Web service formatted)
   * @return Is an WBS date
   */
  public static boolean isWebTimestampWithMs(String date) {
    return isXXX(date, TMST_FORMAT_WEB_MS);
  }

  /**
   * Returns true if date is json date
   *
   * @param date (Web service formatted)
   * @return Is an WBS date
   */
  public static boolean isJsonDate(String date) {
    return isXXX(date, JSON_DATE);
  }

  /**
   * Returns true if date is an WBS date
   *
   * @param date (Web service formatted)
   * @return Is an WBS date
   */
  private static boolean isXXX(String date, FastDateFormat format) {
    try {
      format.parse(date);
      return true;
    } catch (Exception exc) {
      return false;
    }
  }

  /**
   * Transforms a JSON Date to a Date object
   *
   * @param date JSON date
   * @return Date OBJECT
   * @throws ParseException Parse error
   */
  public static Date jsonDate(String date) throws ParseException {
    /* Return web date string */
    return JSON_DATE.parse(date);
  }

  /**
   * Transforms a JSON Date from a Date object
   *
   * @param date JSON date
   * @return Date OBJECT
   */
  public static String jsonDate(Date date) {
    /* Return web date string */
    return JSON_DATE.format(date);
  }

  /**
   * Build java Date object from date criteria with Time criteria
   *
   * @param date Date criteria [dd/MM/yyyy]
   * @param time Time criteria [HH:mm:ss]
   * @return Object date from criterions or null value if any criterion are null
   * with format [dd/MM/yyyy HH:mm:ss]
   */
  public static Date getDateWithTimeFromCriteria(String date, String time) {
    Date fullDate = null;
    try {
      fullDate = TMST_FORMAT_WEB.parse(date + " " + time);
    } catch (ParseException ex) {
      log.error("Parsing error.", ex);
    }
    return fullDate;
  }

  /**
   * Build java Date object from date criteria with Time criteria
   *
   * @param date Date criteria [dd/MM/yyyy]
   * @param time Time criteria [HH:mm:ss]
   * @return Object date from criteria or null value if any criterion is null
   * with format [dd/MM/yyyy HH:mm:ss]
   */
  public static Date addTimeToDate(Date date, String time) {
    Date fullDate = date;
    try {
      if (time != null && !time.isEmpty()) {
        fullDate = TMST_FORMAT_WEB.parse(dat2WebDate(date) + " " + time);
      }
    } catch (ParseException ex) {
      log.error("Parsing error.", ex);
    }
    return fullDate;
  }

  /**
   * TransformColumn a date from a format to another date format
   *
   * @param dateIn     Input date
   * @param formatFrom Initial date format
   * @param formatTo   Final date format
   * @return Date formatted
   */
  public static String generic2Date(String dateIn, String formatFrom, String formatTo) {

    /* Variable definition */
    String outDat = null;
    java.util.Date auxDat;

    try {
      // Create format from and format to
      FastDateFormat formatFromFdt = FastDateFormat.getInstance(formatFrom);
      FastDateFormat formatToFdt = FastDateFormat.getInstance(formatTo);

      /* Parse initial date */
      auxDat = formatFromFdt.parse(dateIn);
      outDat = formatToFdt.format(auxDat);

    } catch (Exception ex) {
      outDat = dateIn;
      log.error("[{}] Error parsing generic date from {2} to {3} -{}-", UTILITY_NAME, dateIn, formatFrom, formatTo, ex);
    }

    return outDat;
  }

  /**
   * Transforms a RDB date into a web date (23-OCT-1978)
   *
   * @param date (Web formatted)
   * @return RDB date formatted
   */
  public static String rdbDate2Web(String date) {

    /* Variable definition */
    java.util.Date webDat;
    String outDat;

    try {
      String[] dateArray = date.split("-");
      String month = dateArray[1];
      dateArray[1] = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();

      date = ("" + Arrays.asList(dateArray)).replaceAll("(^.|.$)", "").replace(", ", "-");

      /* Parse initial date */
      webDat = DATE_FORMAT_RDB.parse(date);

      /* Generate rdb date string */
      outDat = DATE_FORMAT_WEB.format(webDat);
    } catch (Exception exc) {
      outDat = date;
      log.error("[{}] Error parsing RDB date to WEB date -{}-", UTILITY_NAME, date, exc);
    }

    /* Return rdb date string in UPPERCASE */
    return outDat.toUpperCase();
  }

  /**
   * Transforms a RDB date into a web date (23-OCT-1978)
   *
   * @param date (Web formatted)
   * @return RDB date formatted
   */
  public static String rdbDate2String(java.util.Date date) {

    /* Variable definition */
    String outDat;

    try {

      /* Generate rdb date string */
      outDat = DATE_FORMAT_RDB.format(date);
    } catch (Exception exc) {
      outDat = date.toString();
      log.error("[{}] Error parsing RDB date to WEB date -{}-", UTILITY_NAME, date.toString(), exc);
    }

    /* Return rdb date string in UPPERCASE */
    return outDat.toUpperCase();
  }

  /**
   * Retrieve how much time has elapsed from milliseconds
   *
   * @param milliseconds Difference in milliseconds
   * @param elements     AWE Elements
   * @return Date since
   */
  public static String elapsedTime(Long milliseconds, AweElements elements) {
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    calendar.setTimeInMillis(milliseconds);
    int mYear = calendar.get(Calendar.YEAR) - 1970;
    int mMonth = calendar.get(Calendar.MONTH);
    int mDay = calendar.get(Calendar.DAY_OF_MONTH) - 1;
    int mHour = calendar.get(Calendar.HOUR);

    if (mYear > 1) {
      return elements.getLocaleWithLanguage("YEARS", elements.getLanguage(), mYear);
    } else if (mYear > 0) {
      return elements.getLocaleWithLanguage("YEAR", elements.getLanguage(), 1);
    } else if (mMonth > 1) {
      return elements.getLocaleWithLanguage("MONTHS", elements.getLanguage(), mMonth);
    } else if (mMonth > 0) {
      return elements.getLocaleWithLanguage("MONTH", elements.getLanguage(), 1);
    } else if (mDay > 14) {
      return elements.getLocaleWithLanguage("WEEKS", elements.getLanguage(), Math.floorDiv(mDay, 7));
    } else if (mDay > 7) {
      return elements.getLocaleWithLanguage("WEEK", elements.getLanguage(), 1);
    } else if (mDay > 0) {
      return elements.getLocaleWithLanguage("DAYS", elements.getLanguage(), mDay);
    } else if (mHour > 0) {
      return elements.getLocaleWithLanguage("HOURS", elements.getLanguage(), mHour);
    } else if (calendar.get(Calendar.MINUTE) > 0) {
      return elements.getLocaleWithLanguage("MINUTES", elements.getLanguage(), calendar.get(Calendar.MINUTE));
    } else if (calendar.get(Calendar.SECOND) > 0) {
      return elements.getLocaleWithLanguage("SECONDS", elements.getLanguage(), calendar.get(Calendar.SECOND));
    } else {
      return elements.getLocaleWithLanguage("MILLISECONDS", elements.getLanguage(), milliseconds);
    }
  }

  /**
   * Retrieve how much time has elapsed since a date
   *
   * @param date     Date to check
   * @param elements AWE Elements
   * @return Date since
   */
  public static String dateSince(java.util.Date date, AweElements elements) {
    return elements.getLocaleWithLanguage("TIME_AGO", elements.getLanguage(), elapsedTime(new Date().getTime() - date.getTime(), elements));
  }

  /**
   * Autodetects date format and applies a format into java date
   *
   * @param dateString String date
   * @return Date formatted
   */
  public static Date autoDetectDateFormat(String dateString) {
    try {
      if (isJsonDate(dateString)) {
        return jsonDate(dateString);
      } else if (isSqlDate(dateString)) {
        return sql2JavaDate(dateString);
      } else if (isWbsDate(dateString)) {
        return wbs2JavaDate(dateString);
      } else if (isWebTimestamp(dateString)) {
        return web2Timestamp(dateString);
      } else if (isWebTimestampWithMs(dateString)) {
        return web2TimestampWithMs(dateString);
      }
    } catch (ParseException exc) {
      log.error("Parsing date {}", dateString, exc);
    }
    return null;
  }

}
