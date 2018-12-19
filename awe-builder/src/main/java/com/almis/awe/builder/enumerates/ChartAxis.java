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
public enum ChartAxis {
  ALL("all"),
  X_AXIS("xAxis"),
  Y_AXIS("yAxis");
  
  private final String zoomType;
  
  private ChartAxis(String zoomType) {
    this.zoomType = zoomType;
  }

  /**
   * Equals method
   *
   * @param zoomType
   * @return
   */
  public boolean equalsStr(String zoomType) {
    return this.zoomType.equals(zoomType);
  }

  @Override
  public String toString() {
    return this.zoomType;
  }
}
