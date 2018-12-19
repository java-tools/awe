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
public enum FormatterFunction {
  FORMAT_CURRENCY_MAGNITUDE("formatCurrencyMagnitude");
  
  private final String formatterFunction;

  private FormatterFunction(String formatterFunction) {
    this.formatterFunction = formatterFunction;
  }

  /**
   * Equals method
   *
   * @param formatterFunction
   * @return
   */
  public boolean equalsStr(String formatterFunction) {
    return this.formatterFunction.equals(formatterFunction);
  }

  @Override
  public String toString() {
    return this.formatterFunction;
  }
}
