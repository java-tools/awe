package com.almis.awe.builder.screen.criteria;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.base.AbstractCriteriaBuilder;
import com.almis.awe.builder.screen.component.NumericAttributes;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class NumericCriteriaBuilder extends AbstractCriteriaBuilder<NumericCriteriaBuilder, AbstractCriteria> {

  private NumericAttributes numericAttributes;

  public NumericCriteriaBuilder() {
    super();
    this.numericAttributes = new NumericAttributes(this);
  }

  @Override
  public AbstractCriteria build() {
    AbstractCriteria criterion = getNumericAttributes().asNumeric(new Criteria());

    return (AbstractCriteria) super.build(criterion)
      .setComponentType(Component.NUMERIC.toString());
  }

  /**
   * Set number format
   *
   * @param format number format
   * @return This
   */
  public NumericCriteriaBuilder setNumberFormat(String format) {
    getNumericAttributes().setNumberFormat(format);
    return this;
  }

  /**
   * Set show slider
   *
   * @param showSlider Show slider
   * @return This
   */
  public NumericCriteriaBuilder setShowSlider(boolean showSlider) {
    getNumericAttributes().setShowSlider(showSlider);
    return this;
  }
}
