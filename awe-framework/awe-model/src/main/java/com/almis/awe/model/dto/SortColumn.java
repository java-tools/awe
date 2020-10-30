/*
 * Package definition
 */
package com.almis.awe.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

/**
 * SortColumn Class
 *
 * Bean class with sort column definition
 *
 *
 * @author Pablo GARCIA - 18/Jan/2016
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
}
