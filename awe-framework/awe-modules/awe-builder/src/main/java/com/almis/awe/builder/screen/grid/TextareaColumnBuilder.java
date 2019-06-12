package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.base.AbstractColumnBuilder;
import com.almis.awe.builder.screen.component.TextareaAttributes;
import com.almis.awe.model.entities.screen.component.grid.Column;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class TextareaColumnBuilder extends AbstractColumnBuilder<TextareaColumnBuilder, Column> {

  private TextareaAttributes textareaAttributes;

  public TextareaColumnBuilder() {
    super();
    this.textareaAttributes = new TextareaAttributes(this);
  }

  @Override
  public Column build() {
    Column column = (Column) getTextareaAttributes().asTextarea(new Column());

    return (Column) super.build(column)
      .setComponentType(Component.TEXTAREA.toString());
  }

  /**
   * Set number of rows to show
   *
   * @param rows Rows to show
   * @return Builder
   */
  public TextareaColumnBuilder setAreaRows(Integer rows) {
    getTextareaAttributes().setAreaRows(rows);
    return this;
  }
}
