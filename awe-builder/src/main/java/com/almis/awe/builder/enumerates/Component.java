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
public enum Component {
  BUTTON_CHECKBOX("button-checkbox"),
  BUTTON_RADIO("button-radio"),
  CHECKBOX("checkbox"),
  COLOR("color"),
  DATE("date"),
  FILTERED_CALENDAR("filtered-calendar"),
  HIDDEN("hidden"),
  MARKDOWN_EDITOR("markdown-editor"),
  NUMERIC("numeric"),
  PASSWORD("password"),
  PROGRESS("progress"),
  RADIO("radio"),
  SELECT("select"),
  SELECT_MULTIPLE("select-multiple"),
  SUGGEST("suggest"),
  SUGGEST_MULTIPLE("suggest-multiple"),
  TEXT("text"),
  TEXT_VIEW("text-view"),
  TEXTAREA("textarea"),
  TIME("time"),
  UPLOADER("uploader");
  
  private final String component;

  private Component(String component) {
    this.component = component;
  }

  /**
   * Equals method
   *
   * @param component
   * @return
   */
  public boolean equalsStr(String component) {
    return this.component.equals(component);
  }

  @Override
  public String toString() {
    return this.component;
  }
  
}
