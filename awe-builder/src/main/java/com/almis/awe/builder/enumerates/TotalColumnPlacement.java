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
public enum TotalColumnPlacement{

  LEFT("left"),
  RIGHT("right");

  private final String placement;

  private TotalColumnPlacement(String placement) {
    this.placement = placement;
  }

  /**
   * Compares current value with the given string
   *
   * @param initialLoad
   * @return
   */
  public boolean equalsStr(String initialLoad) {
    return this.placement.equals(placement);
  }

  @Override
  public String toString() {
    return this.placement;
  }
}
