package com.almis.awe.builder.screen.base;

import com.almis.awe.builder.enumerates.Align;
import com.almis.awe.builder.enumerates.DataType;
import com.almis.awe.builder.screen.component.ColumnAttributes;
import com.almis.awe.model.entities.screen.component.grid.Column;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author dfuentes
 */
@Getter(AccessLevel.PRIVATE)
public abstract class AbstractColumnBuilder<T extends AbstractColumnBuilder, I extends Column> extends AbstractCriteriaBuilder<T, I> {

  private ColumnAttributes columnAttributes;

  public AbstractColumnBuilder() {
    this.columnAttributes = new ColumnAttributes<>(this);
  }

  @Override
  public I build(I column) {
    super.build(column);
    getColumnAttributes().asColumn(column);
    return column;
  }

  /**
   * Set the align
   *
   * @param align Align
   * @return This
   */
  public T setAlign(Align align) {
    getColumnAttributes().setAlign(align);
    return (T) this;
  }

  /**
   * Set the data type
   *
   * @param dataType data type
   * @return This
   */
  public T setDataType(DataType dataType) {
    getColumnAttributes().setDataType(dataType);
    return (T) this;
  }

  /**
   * Set the format options
   *
   * @param formatOptions Format options
   * @return This
   */
  public T setFormatOptions(String formatOptions) {
    getColumnAttributes().setFormatOptions(formatOptions);
    return (T) this;
  }

  /**
   * Set the formatter
   *
   * @param formatter Formatter
   * @return This
   */
  public T setFormatter(String formatter) {
    getColumnAttributes().setFormatter(formatter);
    return (T) this;
  }

  /**
   * Set the sort field
   *
   * @param sortField sort field
   * @return This
   */
  public T setSortField(String sortField) {
    getColumnAttributes().setSortField(sortField);
    return (T) this;
  }

  /**
   * Set the summary type
   *
   * @param summaryType summary type
   * @return This
   */
  public T setSummaryType(String summaryType) {
    getColumnAttributes().setSummaryType(summaryType);
    return (T) this;
  }

  /**
   * Set the column as frozen (blocked)
   *
   * @param frozen property
   * @return This
   */
  public T setFrozen(boolean frozen) {
    getColumnAttributes().setFrozen(frozen);
    return (T) this;
  }

  /**
   * Set the column as hidden
   *
   * @param hidden Column is hidden
   * @return This
   */
  public T setHidden(boolean hidden) {
    getColumnAttributes().setHidden(hidden);
    return (T) this;
  }

  /**
   * Set the the column as page break
   *
   * @param pageBreak column data break pages
   * @return This
   */
  public T setPagebreak(boolean pageBreak) {
    getColumnAttributes().setPageBreak(pageBreak);
    return (T) this;
  }

  /**
   * Set the column as sendable
   *
   * @param sendable Column is sendable
   * @return This
   */
  public T setSendable(boolean sendable) {
    getColumnAttributes().setSendable(sendable);
    return (T) this;
  }

  /**
   * Set the column as sortable
   *
   * @param sortable Column is sortable
   * @return This
   */
  public T setSortable(boolean sortable) {
    getColumnAttributes().setSortable(sortable);
    return (T) this;
  }

  /**
   * Set the column char length
   *
   * @param charLength Column size in characters
   * @return This
   */
  public T setCharLength(Integer charLength) {
    getColumnAttributes().setCharLength(charLength);
    return (T) this;
  }

  /**
   * Set the column width
   *
   * @param width Column width
   * @return This
   */
  public T setWidth(Integer width) {
    getColumnAttributes().setWidth(width);
    return (T) this;
  }
}
