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
public enum SortMethod {
  ABSOLUTE("absolute"),
  NATURAL("natural");
  
  private final String value;

  SortMethod(String value) {
    this.value = value;
  }

  /**
   * Compares current value with the given string
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
