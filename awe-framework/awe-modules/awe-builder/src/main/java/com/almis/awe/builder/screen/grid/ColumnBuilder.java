package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.enumerates.Align;
import com.almis.awe.builder.enumerates.DataType;
import com.almis.awe.builder.screen.base.AbstractCriteriaBuilder;
import com.almis.awe.model.entities.screen.component.grid.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author dfuentes
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ColumnBuilder extends AbstractCriteriaBuilder<ColumnBuilder, Column> {

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

  @Override
  public Column build() {
    return build(new Column());
  }

  @Override
  public Column build(Column column) {
    super.build(column)
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
}
