package com.almis.awe.scheduler.bean.calendar;

import com.almis.awe.model.util.data.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class Schedule implements Serializable {
  private Integer repeatType;
  private Integer repeatNumber;
  private Integer calendarId;
  private Date initialDate;
  private String initialTime;
  private Date endDate;
  private String endTime;
  private Date date;
  private String time;
  private List<String> yearList;
  private List<String> monthList;
  private List<String> weekList;
  private List<String> dayList;
  private List<String> weekDayList;
  private List<String> hourList;
  private List<String> minuteList;
  private List<String> secondList;

  public Date getInitialDateTime() {
    return calculateDate(initialDate, initialTime);
  }

  public Date getEndDateTime() {
    return calculateDate(endDate, endTime);
  }

  public Date getDateTime() {
    return calculateDate(date, time);
  }

  /**
   * Calculate full date from date and time
   *
   * @param date Date
   * @param time Time
   * @return Full date
   */
  private Date calculateDate(Date date, String time) {
    Date fullDate = null;
    if (date != null) {
      fullDate = date;
      if (time != null && !time.isEmpty()) {
        fullDate = DateUtil.addTimeToDate(fullDate, time);
      }
    }
    return fullDate;
  }

}
