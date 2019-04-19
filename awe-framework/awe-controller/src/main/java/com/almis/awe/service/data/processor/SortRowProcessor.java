package com.almis.awe.service.data.processor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.almis.awe.model.dto.SortColumn;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.CompareRow;

/**
 * TotalizeRowProcessor class
 */
public class SortRowProcessor implements RowProcessor {
  private List<SortColumn> sortList;

  /**
   * Set sort list
   * @param sortList
   * @return
   */
  public SortRowProcessor setSortList(List<SortColumn> sortList) {
    this.sortList = sortList;
    return this;
  }

  /**
   * Process row list
   * @param rowList
   * @return
   * @throws AWException
   */
  public List<Map<String, CellData>> process(List<Map<String, CellData>> rowList) throws AWException {
    Collections.sort(rowList, new CompareRow(sortList));
    return rowList;
  }
}
