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
public enum Align {
  
  CENTER("center"),
  RIGHT("right"),
  LEFT("left");
  
  private final String value;

  private Align(String value) {
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
