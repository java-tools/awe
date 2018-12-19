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
public enum SourceType {

  ACTION("action"),
  CRITERIA_TEXT("criteria-text"),
  CRITERIA_VALUE("criteria-value"),
  FORMULE("formule"),
  LABEL("label"),
  LAUNCHER("launcher"),
  NONE("none"),
  QUERY("query"),
  RESET("reset"),
  VALUE("value");
  
  private final String sourceType;

  private SourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  /**
   * Compares current value with the given string
   *
   * @param sourceType
   * @return
   */
  public boolean equalsStr(String sourceType) {
    return this.sourceType.equals(sourceType);
  }

  @Override
  public String toString() {
    return this.sourceType;
  }

}
