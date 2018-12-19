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
public enum OnClose {
  ACCEPT("accept"),
  REJECT("reject");
  
  private final String onClose;
  
  private OnClose(String onClose) {
    this.onClose = onClose;
  }

  /**
   * Equals method
   *
   * @param onClose
   * @return
   */
  public boolean equalsStr(String onClose) {
    return this.onClose.equals(onClose);
  }

  @Override
  public String toString() {
    return this.onClose;
  }  
}
