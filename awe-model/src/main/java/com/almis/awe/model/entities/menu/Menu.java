package com.almis.awe.model.entities.menu;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;

/*
 * File Imports
 */

/**
 * Menu Class
 *
 * Used to parse the files in 'menu' folder with XStream
 * Generates a menu
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("menu")
public class Menu extends Element {

  private static final long serialVersionUID = -786230210617881807L;
  /**
   * Generates the menu initial tag
   */

  // Option screen
  @XStreamAlias("screen")
  @XStreamAsAttribute
  private String screen = null;

  // Target frame (for navigation)
  @XStreamAlias("context")
  @XStreamAsAttribute
  private String screenContext = null;

  // Menu default action
  @XStreamAlias("default-action")
  @XStreamAsAttribute
  private String defaultAction = null;

  /**
   * Default constructor
   */
  public Menu() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Menu(Menu other) throws AWException {
    super(other);
    this.screen = other.screen;
    this.screenContext = other.screenContext;
    this.defaultAction = other.defaultAction;
  }

  @Override
  public Menu copy() throws AWException {
    return new Menu(this);
  }

  /**
   * Returns the screen target name
   *
   * @return Screen target name
   */
  @JsonIgnore
  public String getScreen() {
    return screen;
  }

  /**
   * Stores the screen target name
   *
   * @param screen Screen target name
   * @return Menu
   */
  public Menu setScreen(String screen) {
    this.screen = screen;
    return this;
  }

  /**
   * Get screen context
   *
   * @return Screen context
   */
  @JsonIgnore
  public String getScreenContext() {
    return screenContext;
  }

  /**
   * Store screen context
   *
   * @param screenContext Screen context
   * @return Menu
   */
  public Menu setScreenContext(String screenContext) {
    this.screenContext = screenContext;
    return this;
  }

  /**
   * Get default action
   *
   * @return Default action
   */
  @JsonIgnore
  public String getDefaultAction() {
    return defaultAction;
  }

  /**
   * Store default action
   *
   * @param defaultAction Default action
   * @return Menu
   */
  public Menu setDefaultAction(String defaultAction) {
    this.defaultAction = defaultAction;
    return this;
  }

  /**
   * Search an initial option by screen name
   *
   * @param screen Screen name
   * @return Option found
   */
  @JsonIgnore
  public Option getOptionByScreen(String screen) {
    Option option = null;
    List<Option> optionList = getElementList();
    // Search in child options
    for (Option child : optionList) {
      // Check module
      if (option == null) {
        child.setParent(this);
        option = child.getOptionByScreen(screen);
      }
    }

    return option;
  }

  /**
   * Search an initial option by name
   *
   * @param optionName Option name
   * @return Option found
   */
  @JsonIgnore
  public Option getOptionByName(String optionName) {
    Option option = null;
    // Search in child options
    List<Option> optionList = this.getElementList();
    for (Option child : optionList) {
      // Check module
      if (option == null) {
        child.setParent(this);
        option = child.getOptionByName(optionName);
      }
    }

    return option;
  }
}