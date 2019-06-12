package com.almis.awe.builder.screen.component;

import com.almis.awe.builder.enumerates.DateViewMode;
import com.almis.awe.builder.screen.base.AbstractAttributes;
import com.almis.awe.builder.screen.base.AbstractCriteriaBuilder;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CalendarAttributes<B extends AbstractCriteriaBuilder> extends AbstractAttributes<B> {
  private DateViewMode dateViewMode;
  private String dateFormat;
  private boolean dateShowTodayButton;
  private boolean showFutureDates;
  private boolean showWeekends;

  public CalendarAttributes(B builder) {
    super(builder);
  }

  /**
   * Build attributes in criterion
   *
   * @param element Criterion
   * @param <E>
   * @return Element with attributes
   */
  public <E extends AbstractCriteria> E asCalendar(E element) {
    E criteria = (E) element
      .setDateFormat(getDateFormat())
      .setShowTodayButton(isDateShowTodayButton())
      .setShowFutureDates(isShowFutureDates())
      .setShowWeekends(isShowWeekends());

    if (getDateViewMode() != null) {
      criteria.setDateViewMode(getDateViewMode().toString());
    }

    return criteria;
  }

  /**
   * Retrieve builder
   *
   * @return Builder
   */
  @Override
  public B builder() {
    return parent;
  }
}
