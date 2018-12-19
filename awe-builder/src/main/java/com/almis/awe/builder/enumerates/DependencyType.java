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
public enum DependencyType {

  AND("and"),
  OR("or");

  private final String dependencyType;

  private DependencyType(String dependencyType) {
    this.dependencyType = dependencyType;
  }

  /**
   * Equals method
   *
   * @param dependencyType
   * @return
   */
  public boolean equalsStr(String dependencyType) {
    return this.dependencyType.equals(dependencyType);
  }

  @Override
  public String toString() {
    return this.dependencyType;
  }
}
