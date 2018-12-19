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
public enum Condition {
  
  EQUALS("eq"),
  GREATER_THAN("gt"),
  GREATER_THAN_EQUALS("gte"),
  IN("in"),
  IS_EMPTY("is empty"),
  IS_NOT_EMPTY("is not empty"),
  LESS_THAN("lt"),
  LESS_THAN_EQUALS("lte"),
  NOT_EQUALS("ne");
  
  private final String condition;

  private Condition(String condition) {
    this.condition = condition;
  }

  /**
   * Equals method
   *
   * @param condition
   * @return
   */
  public boolean equalsStr(String condition) {
    return this.condition.equals(condition);
  }

  @Override
  public String toString() {
    return this.condition;
  }
}
