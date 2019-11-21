/*
 * Package definition
 */
package com.almis.awe.model.dto;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * CompareRow Class
 *
 * Used to compare two rows in sort service data
 *
 *
 * @author Pablo VIDAL - 09/may/2013
 */
public class CompareRow implements Comparator<Map<String, CellData>> {

  private final List<SortColumn> sortList;
  private boolean nullsFirst;

  /**
   * Constructor with sort list
   *
   * @param sortList sorted list
   */
  public CompareRow(List<SortColumn> sortList) {
    this.sortList = sortList;
    this.nullsFirst = false;
  }

  /**
   * Constructor with sort list
   *
   * @param sortList sorted list
   * @param nullsFirst sorted list
   */
  public CompareRow(List<SortColumn> sortList, boolean nullsFirst) {
    this.sortList = sortList;
    this.nullsFirst = nullsFirst;
  }

  /**
   * Compare two DataList rows
   *
   * @param row1 Fist row
   * @param row2 Row to compare with
   * @return Compare diff
   */
  @Override
  public int compare(Map<String, CellData> row1, Map<String, CellData> row2) {

    int orderRow = 0;
    int indexSort = 0;
    int sizeList = sortList.size();

    // For each field, apply celldata comparator
    while (orderRow == 0 && indexSort < sizeList) {
      SortColumn sortColumn = sortList.get(indexSort);
      CellData field1 = row1.get(sortColumn.getColumnId());
      CellData field2 = row2.get(sortColumn.getColumnId());

      // Avoid null data
      field1 = field1 == null ? new CellData() : field1;
      field2 = field2 == null ? new CellData() : field2;

      if ("asc".equalsIgnoreCase(sortColumn.getDirection())) {
        orderRow = new CompareCellData(sortColumn.getDirection(), nullsFirst).compare(field1, field2);
      } else if ("desc".equalsIgnoreCase(sortColumn.getDirection())) {
        orderRow = new CompareCellData(sortColumn.getDirection(), nullsFirst).compare(field2, field1);
      }
      indexSort++;
    }
    return orderRow;
  }
}
