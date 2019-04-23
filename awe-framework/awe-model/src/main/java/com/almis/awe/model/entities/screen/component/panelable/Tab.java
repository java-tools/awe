/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.panelable;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Tab Class
 *
 * Used to parse a tab with XStream
 *
 *
 * Generates an screen criteria
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("tab")
public class Tab extends Panelable {

  private static final long serialVersionUID = 6498566666548618060L;
  // Tab can be maximized or not
  @XStreamAlias("maximize")
  @XStreamAsAttribute
  private String maximize = null;

  /**
   * Default constructor
   */
  public Tab() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Tab(Tab other) throws AWException {
    super(other);
    this.maximize = other.maximize;
  }

  @Override
  public Tab copy() throws AWException {
    return new Tab(this);
  }

  /**
   * Retrieve component tag
   *
   * @return
   */
  @Override
  public String getComponentType() {
    return "tab";
  }

  /**
   * Returns if window maximizes or not
   *
   * @return Window has the maximize button or not (string)
   */
  @JsonIgnore
  public String getMaximize() {
    return maximize;
  }

  /**
   * Returns if window maximizes or not
   *
   * @param maximize Allow Maximize/Minimize screens (string)
   */
  public void setMaximize(String maximize) {
    this.maximize = maximize;
  }

  /**
   * Returns if tab maximizes or not
   *
   * @return Tab Is maximizable
   */
  @JsonGetter("maximize")
  public boolean isMaximizable() {
    return "true".equalsIgnoreCase(this.maximize);
  }
}
