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
public enum DataType {
  ARRAY("array"),
  BOOLEAN("boolean"),
  DOUBLE("double"),
  FLOAT("float"),
  INTEGER("integer"),
  LONG("long"),
  NULL("null"),
  OBJECT("object"),
  STRING("string");
          
  private final String dataType;

  private DataType(String dataType) {
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
