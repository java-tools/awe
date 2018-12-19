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
public enum DateViewMode {
  DAYS("days"),
  MONTHS("months"),
  YEARS("years");
  
  private final String viewMode;
  
  private DateViewMode(String viewMode) {
    this.viewMode = viewMode;
  }

  /**
   * Equals method
   *
   * @param viewMode
   * @return
   */
  public boolean equalsStr(String viewMode) {
    return this.viewMode.equals(viewMode);
  }

  @Override
  public String toString() {
    return this.viewMode;
  }
}
