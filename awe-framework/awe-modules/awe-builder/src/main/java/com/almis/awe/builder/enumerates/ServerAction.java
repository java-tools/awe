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
public enum ServerAction {

  APPLICATION_HELP("application-help"),
  APPLICATION_MANUAL("application-manual"),
  CONTROL("control"),
  CONTROL_CANCEL("control-cancel"),
  CONTROL_CONFIRM("control-confirm"),
  CONTROL_CONFIRM_CANCEL("control-confirm-cancel"),
  CONTROL_CONFIRM_MESSAGE("control-confirm-message"),
  CONTROL_EMPTY_MESSAGE("control-empty-message"),
  CONTROL_TARGET_MESSAGE("control-target-message"),
  CONTROL_UNIQUE_CANCEL("control-unique-cancel"),
  DATA("data"),
  DATA_SILENT("data-silent"),
  DATA_TARGET_MESSAGE("data-target-message"),
  DATA_MESSAGE_CENTERED("dataMessageCentered"),
  DATA_TREATMENT("dataTrt"),
  GET_FILE_MAINTAIN("get-file-maintain"),
  GET_SERVER_FILE("get-server-file"),
  HELP("help"),
  LOGIN("login"),
  LOGOUT("logout"),
  MAINTAIN("maintain"),
  MAINTAIN_ASYNC("maintain-async"),
  MAINTAIN_SILENT("maintain-silent"),
  MAINTAIN_TARGET_MESSAGE("maintain-target-message"),
  MAINTAIN_KUT_DELETE("maintainKutDelete"),
  MAINTAIN_KUT_MULTIPLE("maintainKutMultiple"),
  MAINTAIN_KUT_UPDATE("maintainKutUpdate"),
  MAINTAIN_TREATMENT("maintainTrt"),
  MAINTAIN_TREATMENT_SILENT("maintainTrtSilent"),
  REFRESH_MENU("refresh-menu"),
  RELOAD("reload"),
  SCREEN("screen"),
  SUBSCRIBE("subscribe"),
  UNIQUE("unique"),
  UPDATE_MODEL("update-model"),
  VALIDATE("validate"),
  VALUE("value"),
  VALUE_TARGET_MESSAGE("value-target-message"),
  VIEW_PDF_FILE("view-pdf-report");

  private final String value;

  ServerAction(String value) {
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
