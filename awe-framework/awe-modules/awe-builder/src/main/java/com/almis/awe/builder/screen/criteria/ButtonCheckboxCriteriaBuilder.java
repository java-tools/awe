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
public class ButtonCheckboxCriteriaBuilder extends AbstractCriteriaBuilder<ButtonCheckboxCriteriaBuilder, AbstractCriteria> {

  private CheckboxRadioAttributes checkboxRadioAttributes;

  public ButtonCheckboxCriteriaBuilder() {
    super();
    this.checkboxRadioAttributes = new CheckboxRadioAttributes(this);
  }

  @Override
  public AbstractCriteria build() {
    AbstractCriteria criterion = getCheckboxRadioAttributes().asCheckboxRadio(new Criteria());

    return (AbstractCriteria) super.build(criterion)
      .setComponentType(Component.BUTTON_CHECKBOX.toString());
  }

  /**
   * Set the group
   *
   * @param group Group
   * @return This
   */
  public ButtonCheckboxCriteriaBuilder setGroup(String group) {
    getCheckboxRadioAttributes().setGroup(group);
    return this;
  }

  /**
   * Set checked
   *
   * @param checked Group
   * @return This
   */
  public ButtonCheckboxCriteriaBuilder setChecked(boolean checked) {
    getCheckboxRadioAttributes().setChecked(checked);
    return this;
  }
}
