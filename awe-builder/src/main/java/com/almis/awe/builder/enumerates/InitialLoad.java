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
public enum InitialLoad {
  ENUMERATED("enum"),
  QUERY("query"),
  VALUE("value");
  
  private final String initialLoad;
  
  private InitialLoad(String initialLoad){
    this.initialLoad = initialLoad;
  }

  /**
   * Equals method
   *
   * @param initialLoad
   * @return
   */
  public boolean equalsStr(String initialLoad) {
    return this.initialLoad.equals(initialLoad);
  }

  @Override
  public String toString() {
    return this.initialLoad;
  }
}
