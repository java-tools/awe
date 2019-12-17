package com.almis.awe.scheduler.bean.calendar;

import lombok.Data;

import java.util.Date;

@Data
public class CalendarExcludedDate {
  private Integer id;
  private Integer calendarId;
  private Date date;
  private String description;
}
