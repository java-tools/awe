package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.base.AbstractColumnBuilder;
import com.almis.awe.builder.screen.component.SelectAttributes;
import com.almis.awe.model.entities.screen.component.grid.Column;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class SelectMultipleColumnBuilder extends AbstractColumnBuilder<SelectMultipleColumnBuilder, Column> {

  private SelectAttributes selectAttributes;

  public SelectMultipleColumnBuilder() {
    super();
    this.selectAttributes = new SelectAttributes(this);
  }

  @Override
  public Column build() {
    Column column = (Column) getSelectAttributes().asSelect(new Column());

    return (Column) super.build(column)
      .setComponentType(Component.SELECT_MULTIPLE.toString());
  }

  /**
   * Set optional
   *
   * @param optional optional
   * @return This
   */
  public SelectMultipleColumnBuilder setGroup(boolean optional) {
    getSelectAttributes().setOptional(optional);
    return this;
  }
}
