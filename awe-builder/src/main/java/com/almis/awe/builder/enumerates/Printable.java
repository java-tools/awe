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
public enum Printable {
  ALL("all"),
  EXCEL("excel"),
  FALSE("false"),
  TAB("tab"),
  TRUE("true");
  
  private final String printable;

  private Printable(String printable) {
    this.printable = printable;
  }

  /**
   * Equals method
   *
   * @param printable
   * @return
   */
  public boolean equalsStr(String printable) {
    return this.printable.equals(printable);
  }

  @Override
  public String toString() {
    return this.printable;
  }
  
}
