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
public enum Event {
  
  ADD_ROW("add-row"),
  AFTER_ADD_ROW("after-add-row"),
  AFTER_CANCEL_ROW("after-cancel-row"),
  AFTER_DELETE_ROW("after-delete-row"),
  AFTER_SAVE_ROW("after-save-row"),
  BEFORE_CANCEL_ROW("before-cancel-row"),
  BEFORE_SAVE_ROW("before-save-row"),
  CANCEL_ROW("before-row"),
  CHANGE("change"),
  CHECK_ONE_SELECTED("check-one-selected"),
  CHECK_RECORDS_GENERATED("check-records-generated"),
  CHECK_RECORDS_SAVED("check-records-saved"),
  CHECK_SOME_SELECTED("check-some-selected"),
  CLICK("click"),
  DELETE_ROW("delete-row"),
  SAVE_ROW("save-row"),
  SELECT_ROW("select-row"),
  ZOOM("zoom");
  
  private final String event;

  private Event(String event) {
    this.event = event;
  }

  /**
   * Equals method
   *
   * @param event
   * @return
   */
  public boolean equalsStr(String event) {
    return this.event.equals(event);
  }

  @Override
  public String toString() {
    return this.event;
  }
  
}
