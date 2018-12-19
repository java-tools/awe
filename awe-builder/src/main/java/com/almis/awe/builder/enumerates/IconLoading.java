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
public enum IconLoading {
  CARPET("carpet"),
  CIRCLEBAR("circlebar"),
  CIRCLES("circles"),
  CUBES("cubes"),
  CUSTOM("custom"),
  DOTS("dots"),
  FOLDING("folding"),
  ICON("icon"),
  NONE("none"),
  SPINNER("spinner"),
  SQUARE("square"),
  SQUAREBAR("squarebar");
  
  private final String iconLoading;

  private IconLoading(String iconLoading) {
    this.iconLoading = iconLoading;
  }

  /**
   * Equals method
   *
   * @param iconLoading
   * @return
   */
  public boolean equalsStr(String iconLoading) {
    return this.iconLoading.equals(iconLoading);
  }

  @Override
  public String toString() {
    return this.iconLoading;
  }
}
