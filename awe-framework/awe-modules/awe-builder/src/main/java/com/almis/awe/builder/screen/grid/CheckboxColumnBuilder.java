package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.base.AbstractColumnBuilder;
import com.almis.awe.builder.screen.component.CheckboxRadioAttributes;
import com.almis.awe.model.entities.screen.component.grid.Column;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class CheckboxColumnBuilder extends AbstractColumnBuilder<CheckboxColumnBuilder, Column> {

  private CheckboxRadioAttributes checkboxRadioAttributes;

  public CheckboxColumnBuilder() {
    super();
    this.checkboxRadioAttributes = new CheckboxRadioAttributes(this);
  }

  @Override
  public Column build() {
    Column column = (Column) getCheckboxRadioAttributes().asCheckboxRadio(new Column());

    return (Column) super.build(column)
      .setComponentType(Component.CHECKBOX.toString());
  }

  /**
   * Set the group
   *
   * @param group Group
   * @return This
   */
  public CheckboxColumnBuilder setGroup(String group) {
    getCheckboxRadioAttributes().setGroup(group);
    return this;
  }

  /**
   * Set checked
   *
   * @param checked Group
   * @return This
   */
  public CheckboxColumnBuilder setChecked(boolean checked) {
    getCheckboxRadioAttributes().setChecked(checked);
    return this;
  }
}
