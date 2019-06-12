package com.almis.awe.builder.screen.component;

import com.almis.awe.builder.enumerates.Align;
import com.almis.awe.builder.enumerates.DataType;
import com.almis.awe.builder.screen.base.AbstractAttributes;
import com.almis.awe.builder.screen.base.AbstractColumnBuilder;
import com.almis.awe.model.entities.screen.component.grid.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ColumnAttributes<B extends AbstractColumnBuilder> extends AbstractAttributes<B> {
  private Align align;
  private DataType dataType;
  private String formatOptions;
  private String formatter;
  private String sortField;
  private String summaryType;
  private boolean frozen;
  private boolean hidden;
  private boolean pageBreak;
  private boolean sendable;
  private boolean sortable;
  private Integer charLength;
  private Integer width;

  public ColumnAttributes(B builder) {
    super(builder);
  }

  /**
   * Build attributes in column
   *
   * @param element column
   * @param <E>
   * @return Element with attributes
   */
  public <E extends Column> E asColumn(E element) {
    E column = (E) element
      .setFormatOptions(getFormatOptions())
      .setFormatter(getFormatter())
      .setField(getSortField())
      .setSummaryType(getSummaryType())
      .setFrozen(isFrozen())
      .setHidden(isHidden())
      .setPagebreak(isPageBreak())
      .setSendable(isSendable())
      .setSortable(isSortable())
      .setCharLength(getCharLength())
      .setWidth(getWidth());

    if (getAlign() != null) {
      column.setAlign(getAlign().toString());
    }

    if (getDataType() != null) {
      column.setType(getDataType().toString());
    }

    return column;
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
