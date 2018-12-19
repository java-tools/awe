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
  
  private final String sortMethod;

  private SortMethod(String sortMethod) {
    this.sortMethod = sortMethod;
  }

  /**
   * Compares current value with the given string
   *
   * @param sortMethod
   * @return
   */
  public boolean equalsStr(String sortMethod) {
    return this.sortMethod.equals(sortMethod);
  }

  @Override
  public String toString() {
    return this.sortMethod;
  }
}
