package com.almis.awe.service.data.processor;

import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.CompareRow;
import com.almis.awe.model.dto.SortColumn;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TotalizeRowProcessor class
 */
public class SortRowProcessor implements RowProcessor {
  private List<SortColumn> sortList;

  /**
   * Set sort list
   *
   * @param sortList sort list
   * @return sort row processor
   */
  public SortRowProcessor setSortList(List<SortColumn> sortList) {
    this.sortList = sortList;
    return this;
  }

  /**
   * Process row list
   *
   * @param rowList row list
   * @return row list processed
   */
  public List<Map<String, CellData>> process(List<Map<String, CellData>> rowList) {
    Collections.sort(rowList, new CompareRow(sortList));
    return rowList;
  }
}
