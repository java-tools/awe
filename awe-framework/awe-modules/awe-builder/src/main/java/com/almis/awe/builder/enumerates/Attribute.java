/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.enumerates;

/**
 *
 * @author dfuentes
 */
public enum Attribute {
  
  CURRENT_ROW("currentRow"),
  CURRENT_ROW_VALUE("currentRowValue"),
  EDITABLE("editable"),
  EMPTY_DATA_COLUMN("emptyDataColumn"),
  FOOTER_VALUE("footerValue"),
  FULL_DATA_COLUMN("fullDataColumn"),
  HAS_DATA_COLUMN("hasDataColumn"),
  LABEL("label"),
  MODIFIED_ROWS("modifiedRows"),
  NEXT_CURRENT_ROW("nextCurrentRow"),
  NEXT_CURRENT_ROW_VALUE("nextCurrentRowValue"),
  NEXT_ROW("nextRow"),
  NEXT_ROW_VALUE("nextRowValue"),
  PREVIOUS_CURRENT_ROW("prevCurrentRow"),
  PREVIOUS_CURRENT_ROW_VALUE("prevCurrentRowValue"),
  PREVIOUS_ROW("prevRow"),
  PREVIOUS_ROW_VALUE("prevRowValue"),
  REQUIRED("required"),
  SELECTED_ROW("selectedRow"),
  SELECTED_ROW_VALUE("seletedRowValue"),
  SELECTED_ROWS("selectedRows"),
  SELECTED_VALUES("selectedValues"),
  TEXT("text"),
  TOTAL_ROWS("totalRows"),
  TOTAL_VALUES("totalValues"),
  UNIT("unit"),
  VALUE("value"),
  VISIBLE("visible"),
  X("x"),
  X_MAX("xMax"),
  X_MIN("xMin"),
  Y("y"),
  Y_MAX("yMax"),
  Y_MIN("yMin");
  
  private final String value;

  private Attribute(String value) {
    this.value = value;
  }

  /**
   * Equals method
   *
   * @param value
   * @return
   */
  public boolean equalsStr(String value) {
    return this.value.equals(value);
  }

  @Override
  public String toString() {
    return this.value;
  }          
  
}
