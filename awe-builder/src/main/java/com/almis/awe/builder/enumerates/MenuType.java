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
public enum MenuType {

  VERTICAL("vertical"),
  HORIZONTAL("horizontal");

  private final String menuType;

  private MenuType(String menuType) {
    this.menuType = menuType;
  }

  /**
   * Equals method
   *
   * @param menuType
   * @return
   */
  public boolean equalsStr(String menuType) {
    return this.menuType.equals(menuType);
  }

  @Override
  public String toString() {
    return this.menuType;
  }
}
