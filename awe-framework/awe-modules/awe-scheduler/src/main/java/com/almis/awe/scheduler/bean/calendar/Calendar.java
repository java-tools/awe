package com.almis.awe.scheduler.bean.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.quartz.impl.calendar.HolidayCalendar;

import java.io.Serializable;
import java.util.List;

/**
 * @author dfuentes
 */
@Getter
@Setter
@Accessors(chain = true)
public class Calendar extends HolidayCalendar implements Serializable {

  private Integer calendarId;
  private String name;
  private boolean active;

  /**
   * Add a Set of dates to the calendar
   *
   * @param dates
   */
  public Calendar addExcludedDateSet(List<CalendarExcludedDate> dates) {
    dates.forEach(date -> addExcludedDate(date.getDate()));
    return this;
  }
}
