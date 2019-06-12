package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.base.AbstractColumnBuilder;
import com.almis.awe.builder.screen.component.NumericAttributes;
import com.almis.awe.model.entities.screen.component.grid.Column;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class NumericColumnBuilder extends AbstractColumnBuilder<NumericColumnBuilder, Column> {

  private NumericAttributes numericAttributes;

  public NumericColumnBuilder() {
    super();
    this.numericAttributes = new NumericAttributes(this);
  }

  @Override
  public Column build() {
    Column column = (Column) getNumericAttributes().asNumeric(new Column());

    return (Column) super.build(column)
      .setComponentType(Component.NUMERIC.toString());
  }

  /**
   * Set number format
   *
   * @param format number format
   * @return This
   */
  public NumericColumnBuilder setNumberFormat(String format) {
    getNumericAttributes().setNumberFormat(format);
    return this;
  }

  /**
   * Set show slider
   *
   * @param showSlider Show slider
   * @return This
   */
  public NumericColumnBuilder setShowSlider(boolean showSlider) {
    getNumericAttributes().setShowSlider(showSlider);
    return this;
  }
}
