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
public enum TargetType {
  
  ATTRIBUTE("attribute"),
  CHART_OPTIONS("chart-option"),
  DISABLE("disable"),
  DISABLE_AUTOREFRESH("disable-autorefresh"),
  ENABLE("enable"),
  ENABLE_AUTOREFRESH("enable-autorefresh"),
  FORMAT_NUMBER("format-number"),
  HIDE("hide"),
  HIDE_COLUMN("hide-column"),
  ICON("icon"),
  INPUT("input"),
  LABEL("label"),
  NONE("none"),
  SET_EDITABLE("set-editable"),
  SET_INVISIBLE("set-invisible"),
  SET_OPTIONAL("set-optional"),
  SET_READONLY("set-readonly"),
  SET_REQUIRED("set-required"),
  SET_VISIBLE("set-visible"),
  SHOW("show"),
  SHOW_COLUMN("show-column"),
  SPECIFIC("specific"),
  UNIT("unit"),
  VALIDATE("validate");
  
  private final String value;

  TargetType(String value) {
    this.value = value;
  }

  /**
   * Compares current value with the given string
   *
   * @param value
   * @return
   */
  public boolean equalsStr(String value) {
    return this.value.equals(value);
  }

  @Override
  public String toString() {
    return this.value;
  }
}
