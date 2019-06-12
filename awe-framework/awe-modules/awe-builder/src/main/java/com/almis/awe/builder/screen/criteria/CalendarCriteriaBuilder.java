package com.almis.awe.builder.screen.criteria;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.enumerates.DateViewMode;
import com.almis.awe.builder.screen.base.AbstractCriteriaBuilder;
import com.almis.awe.builder.screen.component.CalendarAttributes;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class CalendarCriteriaBuilder extends AbstractCriteriaBuilder<CalendarCriteriaBuilder, AbstractCriteria> {

  private CalendarAttributes calendarAttributes;

  public CalendarCriteriaBuilder() {
    super();
    this.calendarAttributes = new CalendarAttributes(this);
  }

  @Override
  public AbstractCriteria build() {
    AbstractCriteria criterion = getCalendarAttributes().asCalendar(new Criteria());

    return (AbstractCriteria) super.build(criterion)
      .setComponentType(Component.DATE.toString());
  }

  /**
   * Set the date format
   *
   * @param dateFormat date format
   * @return This
   */
  public CalendarCriteriaBuilder setDateFormat(String dateFormat) {
    getCalendarAttributes().setDateFormat(dateFormat);
    return this;
  }

  /**
   * Set show today button
   *
   * @param showTodayButton show today button
   * @return This
   */
  public CalendarCriteriaBuilder setDateShowTodayButton(boolean showTodayButton) {
    getCalendarAttributes().setDateShowTodayButton(showTodayButton);
    return this;
  }

  /**
   * Set the view mode
   *
   * @param viewMode date view mode
   * @return This
   */
  public CalendarCriteriaBuilder setDateViewMode(DateViewMode viewMode) {
    getCalendarAttributes().setDateViewMode(viewMode);
    return this;
  }

  /**
   * Set show future dates
   *
   * @param showFutureDates Show future dates
   * @return This
   */
  public CalendarCriteriaBuilder setShowFutureDates(boolean showFutureDates) {
    getCalendarAttributes().setShowFutureDates(showFutureDates);
    return this;
  }

  /**
   * Set show weekends
   *
   * @param showWeekends show weekends
   * @return This
   */
  public CalendarCriteriaBuilder setShowWeekends(boolean showWeekends) {
    getCalendarAttributes().setShowWeekends(showWeekends);
    return this;
  }
}
