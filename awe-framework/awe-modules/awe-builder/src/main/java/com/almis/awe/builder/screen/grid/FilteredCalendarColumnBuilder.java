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
public class FilteredCalendarColumnBuilder extends AbstractColumnBuilder<FilteredCalendarColumnBuilder, Column> {

  private CalendarAttributes calendarAttributes;

  public FilteredCalendarColumnBuilder() {
    super();
    this.calendarAttributes = new CalendarAttributes(this);
  }

  @Override
  public Column build() {
    Column column = (Column) getCalendarAttributes().asCalendar(new Column());

    return (Column) super.build(column)
      .setComponentType(Component.FILTERED_CALENDAR.toString());
  }

  /**
   * Set the date format
   *
   * @param dateFormat date format
   * @return This
   */
  public FilteredCalendarColumnBuilder setDateFormat(String dateFormat) {
    getCalendarAttributes().setDateFormat(dateFormat);
    return this;
  }

  /**
   * Set show today button
   *
   * @param showTodayButton show today button
   * @return This
   */
  public FilteredCalendarColumnBuilder setDateShowTodayButton(boolean showTodayButton) {
    getCalendarAttributes().setDateShowTodayButton(showTodayButton);
    return this;
  }

  /**
   * Set the view mode
   *
   * @param viewMode date view mode
   * @return This
   */
  public FilteredCalendarColumnBuilder setDateViewMode(DateViewMode viewMode) {
    getCalendarAttributes().setDateViewMode(viewMode);
    return this;
  }

  /**
   * Set show future dates
   *
   * @param showFutureDates Show future dates
   * @return This
   */
  public FilteredCalendarColumnBuilder setShowFutureDates(boolean showFutureDates) {
    getCalendarAttributes().setShowFutureDates(showFutureDates);
    return this;
  }

  /**
   * Set show weekends
   *
   * @param showWeekends show weekends
   * @return This
   */
  public FilteredCalendarColumnBuilder setShowWeekends(boolean showWeekends) {
    getCalendarAttributes().setShowWeekends(showWeekends);
    return this;
  }
}
