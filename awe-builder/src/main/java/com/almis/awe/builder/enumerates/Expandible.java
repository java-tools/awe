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
public enum Expandible {

  VERTICAL("vertical"),
  HORIZONTAL("horizontal");

  private final String expandible;

  private Expandible(String expandible) {
    this.expandible = expandible;
  }

  /**
   * Equals method
   *
   * @param expandible
   * @return
   */
  public boolean equalsStr(String expandible) {
    return this.expandible.equals(expandible);
  }

  @Override
  public String toString() {
    return this.expandible;
  }
}
