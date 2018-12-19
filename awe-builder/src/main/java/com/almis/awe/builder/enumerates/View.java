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
public enum View {
  
  BASE("base"),
  REPORT("report");
  
  private final String view;

  private View(String view) {
    this.view = view;
  }

  /**
   * Compares current value with the given string
   *
   * @param view
   * @return
   */
  public boolean equalsStr(String view) {
    return this.view.equals(view);
  }

  @Override
  public String toString() {
    return this.view;
  }
  
}
