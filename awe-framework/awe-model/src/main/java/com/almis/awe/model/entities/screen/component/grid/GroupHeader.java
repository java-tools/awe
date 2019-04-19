/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.grid;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.List;

/*
 * File Imports
 */

/**
 * GroupHeader Class
 *
 * Used to add an GROUP header to a grid with XStream
 *
 *
 * @author Pablo GARCIA - 31/MAY/2013
 */
@XStreamAlias("group-header")
public class GroupHeader extends Element {

  private static final long serialVersionUID = 5469384624049029571L;
  // Position of GROUP header in the grid
  @XStreamOmitField
  private String position = null;

  /**
   * Default constructor
   */
  public GroupHeader() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public GroupHeader(GroupHeader other) throws AWException {
    super(other);
    this.position = other.position;
  }

  @Override
  public GroupHeader copy() throws AWException {
    return new GroupHeader(this);
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
