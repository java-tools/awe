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
public enum AxisDataType {
  
  CATEGORY("category"),
  DATETIME("datetime"),
  LINEAR("linear"),
  LOGARITHMIC("logarithmic");
          
  private final String dataType;

  private AxisDataType(String dataType) {
    this.dataType = dataType;
  }

  /**
   * Equals method
   *
   * @param dataType
   * @return
   */
  public boolean equalsStr(String dataType) {
    return this.dataType.equals(dataType);
  }

  @Override
  public String toString() {
    return this.dataType;
  }
  
}
