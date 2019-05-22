package com.almis.awe.model.entities.screen.component.pivottable;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pvidal
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PivotSorter {

  private Map<String, JsonNode> sorterMap = new HashMap<>();

  /**
   * Add sorter to Map
   *
   * @param columnName Column name
   * @param sortValues Sort values
   */
  public void addSorter(String columnName, JsonNode sortValues) {
    sorterMap.put(columnName, sortValues);
  }
}
