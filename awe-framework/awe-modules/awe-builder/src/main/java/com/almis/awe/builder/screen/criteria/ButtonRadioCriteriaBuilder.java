package com.almis.awe.builder.screen.criteria;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.base.AbstractCriteriaBuilder;
import com.almis.awe.builder.screen.component.CheckboxRadioAttributes;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class ButtonRadioCriteriaBuilder extends AbstractCriteriaBuilder<ButtonRadioCriteriaBuilder, AbstractCriteria> {

  private CheckboxRadioAttributes checkboxRadioAttributes;

  public ButtonRadioCriteriaBuilder() {
    super();
    this.checkboxRadioAttributes = new CheckboxRadioAttributes(this);
  }

  @Override
  public AbstractCriteria build() {
    AbstractCriteria criterion = getCheckboxRadioAttributes().asCheckboxRadio(new Criteria());

    return (Criteria) super.build(criterion)
      .setComponentType(Component.BUTTON_RADIO.toString());
  }

  /**
   * Set the group
   *
   * @param group Group
   * @return This
   */
  public ButtonRadioCriteriaBuilder setGroup(String group) {
    getCheckboxRadioAttributes().setGroup(group);
    return this;
  }

  /**
   * Set checked
   *
   * @param checked Group
   * @return This
   */
  public ButtonRadioCriteriaBuilder setChecked(boolean checked) {
    getCheckboxRadioAttributes().setChecked(checked);
    return this;
  }
}
