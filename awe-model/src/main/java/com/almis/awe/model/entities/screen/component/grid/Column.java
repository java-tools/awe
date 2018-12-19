package com.almis.awe.model.entities.screen.component.grid;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/*
 * File Imports
 */

/**
 * Column Class
 * Used to parse a grid column with XStream
 * Column class extends from Criteria
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("column")
@JsonInclude(Include.NON_NULL)
public class Column extends Criteria {

  // Serial UID
  private static final long serialVersionUID = 1L;

  // Column FIELD name to sort
  @XStreamAlias("sort-field")
  @XStreamAsAttribute
  private String field = null;

  // Column POSITION in grid
  @XStreamOmitField
  private String position = null;

  // Column WIDTH in pixels
  @XStreamAlias("width")
  @XStreamAsAttribute
  private String width = null;

  // Column WIDTH in characters
  @XStreamAlias("charlength")
  @XStreamAsAttribute
  private String charLength = null;

  // Column ALIGN
  @XStreamAlias("align")
  @XStreamAsAttribute
  private String align = null;

  // Column is SORTABLE
  @XStreamAlias("sortable")
  @XStreamAsAttribute
  private String sortable = null;

  // Column is MOVABLE
  @XStreamAlias("movable")
  @XStreamAsAttribute
  private String movable = null;

  // Column is not VISIBLE
  @XStreamAlias("hidden")
  @XStreamAsAttribute
  private String hidden = null;

  // Column is SENDABLE
  @XStreamAlias("sendable")
  @XStreamAsAttribute
  private String sendable = null;

  // Totalise TYPE
  @XStreamAlias("summary-type")
  @XStreamAsAttribute
  private String summaryType = null;

  // Formatter
  @XStreamAlias("formatter")
  @XStreamAsAttribute
  private String formatter = null;

  // Formatter
  @XStreamAlias("format-options")
  @XStreamAsAttribute
  private String formatOptions = null;

  // Column is FROZEN
  @XStreamAlias("frozen")
  @XStreamAsAttribute
  private String frozen = null;

  // Column is page break
  @XStreamAlias("pagebreak")
  @XStreamAsAttribute
  private String pagebreak;

  // Dialog to open
  @XStreamAlias("dialog")
  @XStreamAsAttribute
  private String dialog;

  // Sort by this column
  @XStreamOmitField
  private String sortColumn = null;

  /**
   * Default constructor
   */
  public Column() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Column(Column other) throws AWException {
    super(other);
    this.field = other.field;
    this.position = other.position;
    this.width = other.width;
    this.charLength = other.charLength;
    this.align = other.align;
    this.sortable = other.sortable;
    this.movable = other.movable;
    this.hidden = other.hidden;
    this.sendable = other.sendable;
    this.summaryType = other.summaryType;
    this.formatter = other.formatter;
    this.formatOptions = other.formatOptions;
    this.frozen = other.frozen;
    this.pagebreak = other.pagebreak;
    this.dialog = other.dialog;
  }

  @Override
  public Column copy() throws AWException {
    return new Column(this);
  }

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_EMPTY;
  }

  @Override
  @JsonIgnore
  public String getComponentTag() {
    return AweConstants.NO_TAG;
  }

  /**
   * Returns the FIELD name
   *
   * @return Field Name
   */
  @JsonGetter("sortField")
  public String getField() {
    if (this.field == null) {
      return "";
    } else {
      return this.field;
    }
  }

  /**
   * Returns the FIELD name for JSON serialization
   *
   * @return Field Name
   */
  // @JsonGetter("index")
  // public String getFieldConverter() {
  // if (this.getField() != null && !this.getField().isEmpty()) {
  // return this.getField();
  // }
  // return null;
  // }

  /**
   * Stores the FIELD name
   *
   * @param field Field name
   * @return Column
   */
  public Column setField(String field) {
    this.field = field;
    return this;
  }

  /**
   * Returns the column WIDTH in pixels
   *
   * @return Column WIDTH in pixels
   */
  public String getWidth() {
    return this.width;
  }

  /**
   * Returns the column WIDTH in pixels for JSON serialization
   *
   * @return Column WIDTH in pixels
   */
  @JsonGetter("width")
  public String getWidthConverter() {
    if (this.getWidth() != null && !this.getWidth().isEmpty()) {
      return this.getWidth();
    }
    return null;
  }

  /**
   * Stores the column WIDTH in pixels
   *
   * @param width Column WIDTH in pixels
   * @return Column
   */
  public Column setWidth(String width) {
    this.width = width;
    return this;
  }

  /**
   * Get column POSITION
   *
   * @return POSITION
   */
  @JsonGetter("position")
  public String getPosition() {
    return this.position;
  }

  /**
   * Set column POSITION
   *
   * @param position Column position
   * @return Column
   */
  public Column setPosition(String position) {
    this.position = position;
    return this;
  }

  /**
   * Returns the column ALIGN
   *
   * @return the ALIGN
   */
  public String getAlign() {
    return align;
  }

  /**
   * Returns the column ALIGN for JSON serialization
   *
   * @return the ALIGN
   */
  @JsonGetter("align")
  public String getAlignConverter() {
    if (this.getAlign() != null && !this.getAlign().isEmpty()) {
      return this.getAlign();
    }
    return null;
  }

  /**
   * Stores the column ALIGN
   *
   * @param align the ALIGN to set
   * @return Column
   */
  public Column setAlign(String align) {
    this.align = align;
    return this;
  }

  /**
   * Returns the criteria javascript COMPONENT for JSON serialization
   *
   * @return Javascript COMPONENT name
   */
  @JsonGetter("component")
  public String getComponentConverter() {
    return this.getComponentType();
  }

  /**
   * Returns the text of the column
   *
   * @return Text of the column
   */
  @JsonIgnore
  public String getText() {
    if (this.getLabel() != null) {
      return this.getLabel();
    } else {
      return "";
    }
  }

  /**
   * Returns the column WIDTH in characters for JSON serialization
   *
   * @return Width in characters
   */
  @JsonIgnore
  public String getCharLength() {
    if (this.charLength == null) {
      return "0";
    } else {
      return this.charLength;
    }
  }

  /**
   * Returns the column WIDTH in characters
   *
   * @return Width in characters
   */
  @JsonGetter("charlength")
  public String getCharLengthConverter() {
    if (this.getCharLength() != null && !this.getCharLength().isEmpty()) {
      return this.getCharLength();
    }
    return null;
  }

  /**
   * Stores the column WIDTH in characters
   *
   * @param charLength Width in characters
   * @return Column
   */
  public Column setCharLength(String charLength) {
    this.charLength = charLength;
    return this;
  }

  /**
   * Returns if the column is SORTABLE
   *
   * @return Column is SORTABLE
   */
  public String getSortable() {
    return sortable;
  }

  /**
   * Stores if the column is SORTABLE
   *
   * @param sortable Column is SORTABLE
   * @return Column
   */
  public Column setSortable(String sortable) {
    this.sortable = sortable;
    return this;
  }

  /**
   * Check if column is SORTABLE
   *
   * @return Column is SORTABLE
   */
  @JsonGetter("sortable")
  public boolean isSortable() {
    return getSortable() == null || "true".equalsIgnoreCase(getSortable());
  }

  /**
   * Returns if the column is MOVABLE
   *
   * @return Column is MOVABLE
   */
  public String getMovable() {
    return movable;
  }

  /**
   * Stores if the column is MOVABLE
   *
   * @param movable Column is MOVABLE
   * @return Column
   */
  public Column setMovable(String movable) {
    this.movable = movable;
    return this;
  }

  /**
   * Check if column is MOVABLE
   *
   * @return Column is MOVABLE
   */
  public boolean isMovable() {
    return getMovable() != null && "true".equalsIgnoreCase(getMovable());
  }

  /**
   * Returns if the column is VISIBLE or not
   *
   * @return The column is VISIBLE or not
   */
  public String getHidden() {
    return hidden;
  }

  /**
   * Stores if the column is VISIBLE or not
   *
   * @param hidden The column is VISIBLE or not
   * @return Column
   */
  public Column setHidden(String hidden) {
    this.hidden = hidden;
    return this;
  }

  /**
   * Return if column is HIDDEN
   *
   * @return column is HIDDEN
   */
  @JsonGetter("hidden")
  public boolean isHidden() {
    return "true".equalsIgnoreCase(getHidden());
  }

  /**
   * Returns if column is SENDABLE
   *
   * @return Column is SENDABLE
   */
  public String getSendable() {
    return sendable;
  }

  /**
   * Return if values of column are send
   *
   * @return boolean column is send
   */
  @JsonGetter("sendable")
  public boolean isSendable() {
    return getSendable() == null || "true".equalsIgnoreCase(getSendable());
  }

  /**
   * Stores if column is SENDABLE
   *
   * @param sendable Sendable string ("false" for not SENDABLE)
   * @return Column
   */
  public Column setSendable(String sendable) {
    this.sendable = sendable;
    return this;
  }

  /**
   * Retrieve FROZEN attribute
   *
   * @return FROZEN attribute
   */
  public String getFrozen() {
    return frozen;
  }

  /**
   * Check if column is FROZEN
   *
   * @return Column is FROZEN
   */
  @JsonGetter("frozen")
  public boolean isFrozen() {
    return "true".equalsIgnoreCase(getFrozen());
  }

  /**
   * Stores if column is FROZEN
   *
   * @param frozen Frozen string ("false" for not FROZEN)
   * @return Column
   */
  public Column setFrozen(String frozen) {
    this.frozen = frozen;
    return this;
  }

  /**
   * Returns the summary function (for SHOW_TOTALS)
   *
   * @return Summary function
   */
  @JsonGetter("summaryType")
  public String getSummaryType() {
    return summaryType;
  }

  /**
   * Stores the summary function (for SHOW_TOTALS)
   *
   * @param summaryType Summary function
   * @return Column
   */
  public Column setSummaryType(String summaryType) {
    this.summaryType = summaryType;
    return this;
  }

  /**
   * Returns the FORMATTER VALUE
   *
   * @return Formatter VALUE
   */
  @JsonGetter("formatter")
  public String getFormatter() {
    return formatter;
  }

  /**
   * Stores the FORMATTER VALUE
   *
   * @param formatter Formatter VALUE
   * @return Column
   */
  public Column setFormatter(String formatter) {
    this.formatter = formatter;
    return this;
  }

  /**
   * Returns the FORMATTER options
   *
   * @return Formatter options
   */
  @JsonGetter("formatOptions")
  public String getFormatOptions() {
    return formatOptions;
  }

  /**
   * Stores the FORMATTER options
   *
   * @param formatOptions Formatter options
   * @return Column
   */
  public Column setFormatOptions(String formatOptions) {
    this.formatOptions = formatOptions;
    return this;
  }

  /**
   * Retrieve pagebreak
   *
   * @return Page break
   */
  public String getPagebreak() {
    return pagebreak;
  }

  /**
   * Column is pagebreaker
   *
   * @return Page break
   */
  @JsonGetter("pagebreak")
  public boolean isPagebreak() {
    return "true".equalsIgnoreCase(getPagebreak());
  }

  /**
   * Set pagebreak
   *
   * @param pagebreak Page break
   * @return Column
   */
  public Column setPagebreak(String pagebreak) {
    this.pagebreak = pagebreak;
    return this;
  }

  /**
   * @return the dialog
   */
  @JsonGetter("dialog")
  public String getDialog() {
    return dialog;
  }

  /**
   * @param dialog the dialog to set
   * @return Column
   */
  public Column setDialog(String dialog) {
    this.dialog = dialog;
    return this;
  }

  /**
   * Set column value
   *
   * @param value Criteria static VALUE
   * @return column
   */
  public Column setColumnValue(String value) {
    setValue(value);
    return this;
  }

  /**
   * Set column name
   *
   * @param name Parameter name
   * @return column
   */
  public Column setColumnName(String name) {
    setName(name);
    return this;
  }

  /**
   * Set column label
   *
   * @param label Column label
   * @return column
   */
  public Column setColumnLabel(String label) {
    setLabel(label);
    return this;
  }

  @Override
  public String getElementKey() {
    return this.getName();
  }

  @JsonIgnore
  @Override
  public String getHelpTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_HELP_COLUMN;
  }

  /**
   * Get sort column
   *
   * @return Sort column
   */
  @JsonIgnore
  public String getSortColumn() {
    return sortColumn;
  }

  /**
   * @return the dialog
   */
  @JsonGetter("sort")
  public ObjectNode getSortColumnHandler() {
    if (sortColumn != null) {
      ObjectNode sortDirection = JsonNodeFactory.instance.objectNode();
      sortDirection.put(AweConstants.SORT_DIRECTION, sortColumn.toLowerCase());
      return sortDirection;
    }
    return null;
  }

  /**
   * Set sort column
   *
   * @param sortColumn Sort column
   * @return this
   */
  public Column setSortColumn(String sortColumn) {
    this.sortColumn = sortColumn;
    return this;
  }
}
