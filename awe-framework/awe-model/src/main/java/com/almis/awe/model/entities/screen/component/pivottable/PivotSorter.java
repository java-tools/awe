/**
 *
 */
package com.almis.awe.model.entities.screen.component.pivottable;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pvidal
 */
public class PivotSorter {

  private Map<String, JsonNode> pivotSorter;

  /**
   * Generate a pivot sorter
   */
  public PivotSorter() {
    pivotSorter = new HashMap<String, JsonNode>();
  }

  /**
   * @param pivotSorter Sorter
   */
  public PivotSorter(Map<String, JsonNode> pivotSorter) {
    super();
    this.pivotSorter = pivotSorter;
  }

  /**
   * @return the pivotSorter
   */
  public Map<String, JsonNode> getPivotSorter() {
    return pivotSorter;
  }

  /**
   * @param pivotSorter the pivotSorter to set
   */
  public void setPivotSorter(Map<String, JsonNode> pivotSorter) {
    this.pivotSorter = pivotSorter;
  }

  /**
   * Add sorter to Map
   *
   * @param columnName Column name
   * @param sortValues Sort values
   */
  public void addSorter(String columnName, JsonNode sortValues) {
    this.pivotSorter.put(columnName, sortValues);
  }
}
