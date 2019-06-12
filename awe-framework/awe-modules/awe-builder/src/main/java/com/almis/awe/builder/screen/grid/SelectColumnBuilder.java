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
public class SelectColumnBuilder extends AbstractColumnBuilder<SelectColumnBuilder, Column> {

  private SelectAttributes selectAttributes;

  public SelectColumnBuilder() {
    super();
    this.selectAttributes = new SelectAttributes(this);
  }

  @Override
  public Column build() {
    Column column = (Column) getSelectAttributes().asSelect(new Column());

    return (Column) super.build(column)
      .setComponentType(Component.SELECT.toString());
  }

  /**
   * Set optional
   *
   * @param optional optional
   * @return This
   */
  public SelectColumnBuilder setGroup(boolean optional) {
    getSelectAttributes().setOptional(optional);
    return this;
  }
}
