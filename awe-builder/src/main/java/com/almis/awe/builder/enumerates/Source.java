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
public enum Source {

  CENTER("center"),
  BUTTONS("buttons"),
  MODAL("modal"),
  HIDDEN("hidden");

  private final String source;

  private Source(String source) {
    this.source = source;
  }

  /**
   * Compares current value with the given string
   *
   * @param source
   * @return
   */
  public boolean equalsStr(String source) {
    return this.source.equals(source);
  }

  @Override
  public String toString() {
    return this.source;
  }
}
