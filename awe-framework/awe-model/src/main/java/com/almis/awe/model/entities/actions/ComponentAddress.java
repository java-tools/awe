package com.almis.awe.model.entities.actions;

import com.almis.awe.model.constant.AweConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.Serializable;

/**
 * @author pgarcia and pvidal
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComponentAddress implements Serializable {

  private static final long serialVersionUID = 5241963594540611025L;

  private String application;
  private String session;
  private String view;
  private String screen;
  private String component;
  private String row;
  private String column;

  /**
   * Copy constructor
   *
   * @param other
   */
  public ComponentAddress(ComponentAddress other) {
    this.application = other.application;
    this.session = other.session;
    this.view = other.view;
    this.screen = other.screen;
    this.component = other.component;
    this.row = other.row;
    this.column = other.column;
  }

  /**
   * Constructor
   *
   * @param address Object node with address
   */
  public ComponentAddress(ObjectNode address) {
    super();

    // Get view if available
    if (address.has(AweConstants.ADDRESS_SESSION)) {
      this.session = address.get(AweConstants.ADDRESS_SESSION).asText();
    }

    // Get view if available
    if (address.has(AweConstants.ADDRESS_VIEW)) {
      this.view = address.get(AweConstants.ADDRESS_VIEW).asText();
    }

    // Get component if available
    if (address.has(AweConstants.ADDRESS_COMPONENT)) {
      this.component = address.get(AweConstants.ADDRESS_COMPONENT).asText();
    }

    // Get column if available
    if (address.has(AweConstants.ADDRESS_COLUMN)) {
      this.column = address.get(AweConstants.ADDRESS_COLUMN).asText();
    }

    // Get row if available
    if (address.has(AweConstants.ADDRESS_ROW)) {
      this.row = address.get(AweConstants.ADDRESS_ROW).asText();
    }
  }

  /**
   * Constructor
   *
   * @param view      Component view
   * @param component Component id
   */
  public ComponentAddress(String view, String component) {
    super();
    this.view = view;
    this.component = component;
  }

  /**
   * Constructor
   *
   * @param view      Component view
   * @param component Component id
   * @param row       Row
   * @param column    Column
   */
  public ComponentAddress(String view, String component, String row, String column) {
    super();
    this.view = view;
    this.component = component;
    this.row = row;
    this.column = column;
  }

  /**
   * Constructor
   *
   * @param application Application
   * @param session     Session token
   * @param view        Component view
   * @param screen      Screen name
   * @param component   Component id
   */
  public ComponentAddress(String application, String session, String view, String screen, String component) {
    super();
    this.application = application;
    this.session = session;
    this.view = view;
    this.screen = screen;
    this.component = component;
  }

  /**
   * Constructor
   *
   * @param application Application
   * @param session     Session token
   * @param view        Component view
   * @param screen      Screen name
   * @param component   Component id
   * @param row         Row
   * @param column      Column
   */
  public ComponentAddress(String application, String session, String view, String screen, String component, String row, String column) {
    super();
    this.application = application;
    this.session = session;
    this.view = view;
    this.screen = screen;
    this.component = component;
    this.row = row;
    this.column = column;
  }

  /**
   * Retrieves the component session
   *
   * @return the session
   */
  public String getSession() {
    return session;
  }

  /**
   * Stores the component session
   *
   * @param session the session to set
   */
  public void setSession(String session) {
    this.session = session;
  }

  /**
   * Retrieves the component view
   *
   * @return the view
   */
  public String getView() {
    return view;
  }

  /**
   * Stores the component view
   *
   * @param view View to set
   */
  public void setView(String view) {
    this.view = view;
  }

  /**
   * Retrieves the screen name
   *
   * @return the screen
   */
  public String getScreen() {
    return screen;
  }

  /**
   * Store the screen of component
   *
   * @param screen of component
   */
  public void setScreen(String screen) {
    this.screen = screen;
  }

  /**
   * Retrieves the component id
   *
   * @return the component
   */
  public String getComponent() {
    return component;
  }

  /**
   * Stores the component id
   *
   * @param component the component to set
   */
  public void setComponent(String component) {
    this.component = component;
  }

  /**
   * @return the application
   */
  public String getApplication() {
    return application;
  }

  /**
   * @param application the application to set
   */
  public void setApplication(String application) {
    this.application = application;
  }

  /**
   * Retrieve row of component
   *
   * @return id of row
   */
  public String getRow() {
    return row;
  }

  /**
   * Store the row of component
   *
   * @param row Row to set
   */
  public void setRow(String row) {
    this.row = row;
  }

  /**
   * Retrieve column of component
   *
   * @return column of component
   */
  public String getColumn() {
    return column;
  }

  /**
   * Store column of component
   *
   * @param column Column to set
   */
  public void setColumn(String column) {
    this.column = column;
  }
}
