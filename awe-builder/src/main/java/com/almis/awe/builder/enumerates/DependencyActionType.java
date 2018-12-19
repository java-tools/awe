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
public enum DependencyActionType {
  
  ADD_CLASS("add-class"),
  ADD_ROW("add-row"),
  ADD_ROW_DOWN("add-row-down"),
  ADD_ROW_UP("add-row-up"),
  BACK("back"),
  CANCEL_ROW("cancel-row"),
  CHANGE_LANGUAGE("change-language"),
  CHANGE_THEME("change-theme"),
  CHECK_LEAF_SELECTED("check-leaf-selected"),
  CHECK_ONE_SELECTED("check-one-selected"),
  CHECK_PARENT_EXPANDED("check-parent-expanded"),
  CHECK_RECORDS_GENERATED("check-records-generated"),
  CHECK_RECORDS_SAVED("check-records-saved"),
  CHECK_SOME_SELECTED("check-some-selected"),
  CLOSE("close"),
  CLOSE_CANCEL("close-cancel"),
  CONFIRM("confirm"),
  CONFIRM_EMPTY_DATA("confirm-empty-data"),
  CONFIRM_NOT_UPDATED_DATA("confirm-not-updated-data"),
  CONFIRM_UPDATED_DATA("confirm-updated-data"),
  COPY_ROW("copy-row"),
  COPY_ROW_DOWN("copy-row-down"),
  COPY_ROW_UP("copy-row-up"),
  DELETE_ROW("delete-row"),
  DIALOG("dialog"),
  DISABLE_DEPENDENCIES("disable-dependencies"),
  ENABLE_DEPENDENCIES("enable-dependencies"),
  FILTER("filter"),
  FORWARD("forward"),
  GET_LOCALS("get-locals"),
  LOGOUT("logout"),
  NEXT_STEP("next-step"),
  PREV_STEP("prev-step"),
  PRINT("print"),
  RELOAD_LANGUAGE("reload-language"),
  REMOVE_CLASS("remove-class"),
  RESET("reset"),
  RESIZE("resize"),
  RESTORE("restore"),
  RESTORE_TARGET("restore-target"),
  SAVE_ROW("save-row"),
  SCREEN("screen"),
  RELOAD("reload"),
  SELECT_ALL_ROWS("select-all-rows"),
  SELECT_FIRST_ROW("select-first-row"),
  SELECT_LAST_ROW("select-last-row"),
  SERVER("server"),
  SERVER_DOWNLOAD("server-download"),
  SERVER_PRINT("server-print"),
  START_LOAD("start-load"),
  TOGGLE_MENU("toggle-menu"),
  TOGGLE_NAVBAR("toggle-navbar"),
  UNSELECT_ALL_ROWS("unselect-all-rows"),
  VALIDATE("validate"),
  VALUE("value");
  
  private final String dependencyAction;

  private DependencyActionType(String dependencyAction) {
    this.dependencyAction = dependencyAction;
  }

  /**
   * Equals method
   *
   * @param dependencyAction
   * @return
   */
  public boolean equalsStr(String dependencyAction) {
    return this.dependencyAction.equals(dependencyAction);
  }

  @Override
  public String toString() {
    return this.dependencyAction;
  }
}
