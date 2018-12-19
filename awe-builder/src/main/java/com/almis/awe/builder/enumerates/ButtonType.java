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
public enum ButtonType {
  
  BUTTON("button"),
  RESET("reset"),
  SUBMIT("submit");

  private final String buttonType;

  private ButtonType(String buttonType) {
    this.buttonType = buttonType;
  }

  /**
   * Equals method
   *
   * @param buttonType
   * @return
   */
  public boolean equalsStr(String buttonType) {
    return this.buttonType.equals(buttonType);
  }

  @Override
  public String toString() {
    return this.buttonType;
  }
}
