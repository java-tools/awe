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
public enum ChartLayout {
  HORIZONTAL("horizontal"),
  VERTICAL("verical");
  
  private final String layout;

  private ChartLayout(String layout) {
    this.layout = layout;
  }

  /**
   * Equals method
   *
   * @param layout
   * @return
   */
  public boolean equalsStr(String layout) {
    return this.layout.equals(layout);
  }

  @Override
  public String toString() {
    return this.layout;
  }
  
}
