package com.almis.awe.model.entities.screen.component.grid;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Column Class
 * Used to parse a grid column with XStream
 * Column class extends from Criteria
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@ToString
@XStreamAlias("column")
@JsonInclude(Include.NON_NULL)
public class Column extends AbstractCriteria {

  // Serial UID
  private static final long serialVersionUID = 1L;

  // Column FIELD name to sort
  @JsonProperty("sortField")
  @XStreamAlias("sort-field")
  @XStreamAsAttribute
  private String field;

  // Column POSITION in grid
  @XStreamOmitField
  private String position;

  // Column WIDTH in pixels
  @XStreamAlias("width")
  @XStreamAsAttribute
  private Integer width;

  // Column WIDTH in characters
  @JsonProperty("charlength")
  @XStreamAlias("charlength")
  @XStreamAsAttribute
  private Integer charLength;

  // Column ALIGN
  @XStreamAlias("align")
  @XStreamAsAttribute
  private String align;

  // Column is SORTABLE
  @XStreamAlias("sortable")
  @XStreamAsAttribute
  private Boolean sortable;

  // Column is MOVABLE
  @XStreamAlias("movable")
  @XStreamAsAttribute
  private Boolean movable;

  // Column is not VISIBLE
  @XStreamAlias("hidden")
  @XStreamAsAttribute
  private Boolean hidden;

  // Column is SENDABLE
  @XStreamAlias("sendable")
  @XStreamAsAttribute
  private Boolean sendable;

  // Totalise TYPE
  @XStreamAlias("summary-type")
  @XStreamAsAttribute
  private String summaryType;

  // Formatter
  @XStreamAlias("formatter")
  @XStreamAsAttribute
  private String formatter;

  // Formatter
  @XStreamAlias("format-options")
  @XStreamAsAttribute
  private String formatOptions;

  // Column is FROZEN
  @XStreamAlias("frozen")
  @XStreamAsAttribute
  private Boolean frozen;

  // Column is page break
  @XStreamAlias("pagebreak")
  @XStreamAsAttribute
  private Boolean pagebreak;

  // Dialog to open
  @XStreamAlias("dialog")
  @XStreamAsAttribute
  private String dialog;

  // Sort by this column
  @XStreamOmitField
  private String sortColumn;

  @Override
  public Column copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
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
   * Returns if column is hidden
   * @return Column is hidden
   */
  @JsonGetter("hidden")
  public boolean isHidden() {
    return hidden != null && hidden;
  }

  /**
   * Returns if column is sendable
   * @return Column is sendable
   */
  @JsonGetter("sendable")
  public boolean isSendable() {
    return sendable == null || sendable;
  }


  /**
   * Returns if column is movable
   * @return Column is movable
   */
  @JsonGetter("movable")
  public boolean isMovable() {
    return movable == null || movable;
  }

  /**
   * Returns if column is sortable
   * @return Column is sortable
   */
  @JsonGetter("sortable")
  public boolean isSortable() {
    return sortable == null || sortable;
  }

  /**
   * Returns if column is frozen
   * @return Column is frozen
   */
  @JsonGetter("frozen")
  public boolean isFrozen() {
    return frozen != null && frozen;
  }

  /**
   * Returns if column is visible
   * @return Column is visible
   */
  @Override
  @JsonGetter("visible")
  public boolean isVisible() {
    return !isHidden();
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

  @JsonIgnore
  @Override
  public String getElementKey() {
    return getName();
  }

  @JsonIgnore
  @Override
  public String getHelpTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_HELP_COLUMN;
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
}
