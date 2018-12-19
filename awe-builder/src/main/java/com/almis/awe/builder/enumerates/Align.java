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
  
  private final String align;

  private Align(String align) {
    this.align = align;
  }

  /**
   * Equals method
   *
   * @param align
   * @return
   */
  public boolean equalsStr(String align) {
    return this.align.equals(align);
  }

  @Override
  public String toString() {
    return this.align;
  }
  
}
