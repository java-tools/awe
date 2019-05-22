package com.almis.awe.model.entities.screen.component.grid;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * GroupHeader Class
 *
 * Used to add an GROUP header to a grid with XStream
 *
 *
 * @author Pablo GARCIA - 31/MAY/2013
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("group-header")
public class GroupHeader extends Element {

  private static final long serialVersionUID = 5469384624049029571L;

  // Position of GROUP header in the grid
  @XStreamOmitField
  private String position;

  @Override
  public GroupHeader copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Set column POSITION
   *
   * @param position Position to be set
   */
  public void setPosition(String position) {
    this.position = position;
  }

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_EMPTY;
  }

  /**
   * Retrieves starting column's name for JSON serialization
   *
   * @return name
   */
  @JsonGetter("startColumnName")
  public String getStartColumnNameConverter() {
    List<Column> childColumnList = this.getElementsByType(Column.class);
    if (!childColumnList.isEmpty()) {
      return childColumnList.get(0).getName();
    }
    return null;
  }

  /**
   * Retrieves columns number for JSON serialization
   *
   * @return number column number
   */
  @JsonGetter("numberOfColumns")
  public Integer getColumnNumberConverter() {
    List<Column> childColumnList = this.getElementsByType(Column.class);
    if (!childColumnList.isEmpty()) {
      return childColumnList.size();
    }
    return null;
  }

  /**
   * Retrieves title's text for JSON serialization
   *
   * @return title
   */
  @JsonGetter("titleText")
  public String getTitleTextConverter() {
    return this.getLabel();
  }
}
