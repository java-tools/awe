package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.base.AbstractColumnBuilder;
import com.almis.awe.model.entities.screen.component.grid.Column;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class TimeColumnBuilder extends AbstractColumnBuilder<TimeColumnBuilder, Column> {
  @Override
  public Column build() {
    return (Column) super.build(new Column())
      .setComponentType(Component.TEXT_VIEW.toString());
  }
}
