/*
 * Package definition
 */
package com.almis.awe.model.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

/**
 * FilterColumn Class
 *
 * Bean class with filter column definition
 *
 *
 * @author Pablo GARCIA - 18/Jan/2016
 */
@Data
public class FilterColumn {

  private String columnId;
  private String value;

  /**
   * Constructor
   *
   * @param field     Field to sort
   * @param value     Value to filter
   */
  public FilterColumn(String field, String value) {
    this.columnId = field;
    this.value = value;
  }

  /**
   * Constructor
   *
   * @param sortColumn Column to be sorted
   */
  public FilterColumn(ObjectNode sortColumn) {
    this.columnId = sortColumn.get("id").asText();
    this.value = sortColumn.get("value").asText();
  }
}
