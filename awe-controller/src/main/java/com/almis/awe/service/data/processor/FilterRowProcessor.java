/*
 * Package definition
 */
package com.almis.awe.service.data.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;

/**
 * TotalizeRowProcessor class
 */
public class FilterRowProcessor implements RowProcessor {
  private Map<String, String> filterMap;

  /**
   * Set filter map
   * @param filterMap
   * @return
   */
  public FilterRowProcessor setFilterMap(Map<String, String> filterMap) {
    this.filterMap = filterMap;
    return this;
  }

  /**
   * Process row list
   * @param rowList
   * @return
   * @throws AWException
   */
  public List<Map<String, CellData>> process(List<Map<String, CellData>> rowList) throws AWException {
    List<Map<String, CellData>> newRows = new ArrayList<Map<String, CellData>>();
    for (Map<String, CellData> row : rowList) {
      boolean filterPassed = true;
      for (String key: this.filterMap.keySet()) {
        if (row.containsKey(key)) {
          CellData cell = row.get(key);
          if (!cell.getStringValue().equalsIgnoreCase(this.filterMap.get(key))) {
            filterPassed = false;
          }
        }
      }
      // If filter has been passed, add the row
      if (filterPassed) {
        newRows.add(row);
      }
    }
    // Set rows and records
    return newRows;
  }
}
