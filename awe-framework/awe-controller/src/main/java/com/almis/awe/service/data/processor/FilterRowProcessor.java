package com.almis.awe.service.data.processor;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.FilterColumn;
import com.almis.awe.model.util.data.DataListUtil;

import java.util.List;
import java.util.Map;

/**
 * TotalizeRowProcessor class
 */
public class FilterRowProcessor implements RowProcessor {
  private List<FilterColumn> filterList;

  /**
   * Set filter list
   *
   * @param filterList filter list
   * @return filter row processor
   */
  public FilterRowProcessor setFilterList(List<FilterColumn> filterList) {
    this.filterList = filterList;
    return this;
  }

  /**
   * Process row list
   *
   * @param rowList row list
   * @return row list processed
   * @throws AWException AWE exception
   */
  public List<Map<String, CellData>> process(List<Map<String, CellData>> rowList) {
    DataList dataList = new DataList();
    dataList.setRows(rowList);
    DataListUtil.filterContains(dataList, filterList.toArray(new FilterColumn[0]));

    // Retrieve filtered rows
    return dataList.getRows();
  }
}
