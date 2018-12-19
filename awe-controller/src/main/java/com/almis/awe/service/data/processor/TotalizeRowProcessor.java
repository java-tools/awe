/*
 * Package definition
 */
package com.almis.awe.service.data.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;

/**
 * TotalizeRowProcessor class
 */
public class TotalizeRowProcessor implements RowProcessor {
  private List<TotalizeColumnProcessor> totalizeList;

  /**
   * Set totalize list
   * @param totalizeList
   * @return
   */
  public TotalizeRowProcessor setTotalizeList(List<TotalizeColumnProcessor> totalizeList) {
    this.totalizeList = totalizeList;
    return this;
  }

  /**
   * Process row list
   * @param rowList
   * @return
   * @throws AWException
   */
  public List<Map<String, CellData>> process(List<Map<String, CellData>> rowList) throws AWException {
    List<Map<String, CellData>> totalizedList = new ArrayList<Map<String, CellData>>();

    // Fill final list with subtotals
    for (Map<String, CellData> row : rowList) {
      // Generate subtotal rows
      this.addSubtotals(row, totalizedList);

      // Add row
      totalizedList.add(row);
    }

    // If there are no rows, don't generate the bottom totals
    if (!rowList.isEmpty()) {
      // Generate total
      this.addSubtotals(null, totalizedList);
    }

    return totalizedList;
  }

  /**
   * Add subtotals
   * @param row
   * @param processedList
   * @throws AWException
   */
  private void addSubtotals(Map<String, CellData> row, List<Map<String, CellData>> processedList) throws AWException {
    Map<String, CellData> totalRow;
    Map<String, CellData> totalizeValues;
    Map<String, CellData> emptyRow = null;
    Map<String, String> totalizeKeys;

    // For each total processor
    for (TotalizeColumnProcessor totalizeProcessor : totalizeList) {
      // Check if add a new line
      boolean addLine = totalizeProcessor.checkNewLine(row);

      // If a totalize line has been inserted, add a line
      if (addLine) {
        totalizeProcessor.addNewLine(row, processedList);
      }

      // Process line
      totalizeProcessor.process(row);
    }

    // Add style value
    if (row != null && !row.containsKey(AweConstants.DATALIST_STYLE_FIELD)) {
      row.put(AweConstants.DATALIST_STYLE_FIELD, new CellData(""));
    }
  }
}
