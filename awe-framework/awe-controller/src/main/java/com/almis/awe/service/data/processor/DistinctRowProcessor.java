/*
 * Package definition
 */
package com.almis.awe.service.data.processor;

import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.CompareRow;
import com.almis.awe.model.dto.SortColumn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TotalizeRowProcessor class
 */
public class DistinctRowProcessor implements RowProcessor {
  private List<SortColumn> distinctList;

  /**
   * Set distinct list
   *
   * @param distinctList distinct list
   * @return distinct row processor
   */
  public DistinctRowProcessor setDistinctList(List<SortColumn> distinctList) {
    this.distinctList = distinctList;
    return this;
  }

  /**
   * Process row list
   *
   * @param rowList row list
   * @return row list processed
   */
  public List<Map<String, CellData>> process(List<Map<String, CellData>> rowList) {
    CompareRow comparator = new CompareRow(this.distinctList);
    List<Map<String, CellData>> newRows = new ArrayList<>();
    for (Map<String, CellData> row : rowList) {
      if (!in(newRows, row, comparator)) {
        newRows.add((Map<String, CellData>) ((HashMap<String, CellData>) row).clone());
      }
    }

    // Set rows and records
    return newRows;
  }

  /**
   * Check if a row is inside another list
   *
   * @param list       Row list
   * @param rowToCheck row to check
   * @param comparator row comparator
   */
  private boolean in(List<Map<String, CellData>> list, Map<String, CellData> rowToCheck, CompareRow comparator) {
    for (Map<String, CellData> row : list) {
      if (comparator.compare(row, rowToCheck) == 0) {
        return true;
      }
    }
    return false;
  }
}
