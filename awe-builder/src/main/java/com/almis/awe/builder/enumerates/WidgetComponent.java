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
public enum WidgetComponent {
  FILE_VIEWER("file_viewer");
          
  private final String component;

  private WidgetComponent(String component) {
    this.component = component;
  }

  /**
   * Compares current value with the given string
   *
   * @param component
   * @return
   */
  public boolean equalsStr(String component) {
    return this.component.equals(component);
  }

  @Override
  public String toString() {
    return this.component;
  }
}
