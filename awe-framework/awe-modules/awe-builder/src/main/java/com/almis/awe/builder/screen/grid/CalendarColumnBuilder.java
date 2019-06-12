package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.enumerates.DateViewMode;
import com.almis.awe.builder.screen.base.AbstractColumnBuilder;
import com.almis.awe.builder.screen.component.CalendarAttributes;
import com.almis.awe.model.entities.screen.component.grid.Column;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class CalendarColumnBuilder extends AbstractColumnBuilder<CalendarColumnBuilder, Column> {

  private CalendarAttributes calendarAttributes;

  public CalendarColumnBuilder() {
    super();
    this.calendarAttributes = new CalendarAttributes(this);
  }

  @Override
  public Column build() {
    Column column = (Column) getCalendarAttributes().asCalendar(new Column());

    return (Column) super.build(column)
      .setComponentType(Component.DATE.toString());
  }

  /**
   * Set the date format
   *
   * @param dateFormat date format
   * @return This
   */
  public CalendarColumnBuilder setDateFormat(String dateFormat) {
    getCalendarAttributes().setDateFormat(dateFormat);
    return this;
  }

  /**
   * Set show today button
   *
   * @param showTodayButton show today button
   * @return This
   */
  public CalendarColumnBuilder setDateShowTodayButton(boolean showTodayButton) {
    getCalendarAttributes().setDateShowTodayButton(showTodayButton);
    return this;
  }

  /**
   * Set the view mode
   *
   * @param viewMode date view mode
   * @return This
   */
  public CalendarColumnBuilder setDateViewMode(DateViewMode viewMode) {
    getCalendarAttributes().setDateViewMode(viewMode);
    return this;
  }

  /**
   * Set show future dates
   *
   * @param showFutureDates Show future dates
   * @return This
   */
  public CalendarColumnBuilder setShowFutureDates(boolean showFutureDates) {
    getCalendarAttributes().setShowFutureDates(showFutureDates);
    return this;
  }

  /**
   * Set show weekends
   *
   * @param showWeekends show weekends
   * @return This
   */
  public CalendarColumnBuilder setShowWeekends(boolean showWeekends) {
    getCalendarAttributes().setShowWeekends(showWeekends);
    return this;
  }
}
