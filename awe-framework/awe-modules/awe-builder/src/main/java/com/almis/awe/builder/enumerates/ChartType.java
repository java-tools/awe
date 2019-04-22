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
public enum ChartType {
  AREA("area"),
  AREA_RANGE("area_range"),
  AREA_SPLINE("area_spline"),
  AREA_SPLINE_RANGE("area_spline_range"),
  BUBBLE("bubble"),
  COLUMN("column"),
  COLUMN_3D("column_3d"),
  DONUT("donut"),
  DONUT_3D("donut_3d"),
  LINE("line"),
  MIXED("mixed"),
  PIE("pie"),
  PIE_3D("pie_3d"),
  SCATTER("scatter"),
  SEMICIRCLE("semicircle"),
  SPLINE("spline");
  
  private final String value;

  private ChartType(String value) {
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
