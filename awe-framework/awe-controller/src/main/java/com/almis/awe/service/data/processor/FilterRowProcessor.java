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
    List<Map<String, CellData>> newRows = new ArrayList<>();
    for (Map<String, CellData> row : rowList) {
      boolean filterPassed = false;
      for (Map.Entry<String, String> entry: this.filterMap.entrySet()) {
        if (row.containsKey(entry.getKey()) && row.get(entry.getKey()).getStringValue().toLowerCase().contains(entry.getValue().toLowerCase())) {
          filterPassed = true;
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
