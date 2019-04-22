/*
 * Package definition
 */
package com.almis.awe.model.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * SortColumn Class
 *
 * Bean class with sort column definition
 *
 *
 * @author Pablo GARCIA - 18/Jan/2016
 */
public class SortColumn {

  private String columnId;
  private String direction;

  /**
   * Constructor
   *
   * @param field     Field to sort
   * @param direction Direction (ASC, DESC)
   */
  public SortColumn(String field, String direction) {
    this.columnId = field;
    this.direction = direction != null ? direction.toUpperCase() : null;
  }

  /**
   * Constructor
   *
   * @param sortColumn Column to be sorted
   */
  public SortColumn(ObjectNode sortColumn) {
    this.columnId = sortColumn.get("id").asText();
    this.direction = sortColumn.get("direction").asText().toUpperCase();
  }

  /**
   * Retrieve the column to be sorted
   *
   * @return the columnId
   */
  public String getColumnId() {
    return columnId;
  }

  /**
   * Set the column to be sorted
   *
   * @param columnId the columnId to set
   * @return this
   */
  public SortColumn setColumnId(String columnId) {
    this.columnId = columnId;
    return this;
  }

  /**
   * Get the column sort direction
   *
   * @return the direction
   */
  public String getDirection() {
    return direction;
  }

  /**
   * Set the column sort direction
   *
   * @param direction the direction to set
   * @return this
   */
  public SortColumn setDirection(String direction) {
    this.direction = direction;
    return this;
  }

  /**
   * Bean to string
   *
   * @return Bean stringified
   */
  @Override
  public String toString() {
    return "(" + this.columnId + ", " + this.direction + ")";
  }

}
