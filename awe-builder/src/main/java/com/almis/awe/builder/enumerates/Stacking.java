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
public enum Stacking {
  NORMAL("normal"),
  PERCENT("percent");
  
  private final String stacking;
  
  private Stacking(String stacking) {
    this.stacking = stacking;
  }

  /**
   * Compares current value with the given string
   *
   * @param stacking
   * @return
   */
  public boolean equalsStr(String stacking) {
    return this.stacking.equals(stacking);
  }

  @Override
  public String toString() {
    return this.stacking;
  }
}
