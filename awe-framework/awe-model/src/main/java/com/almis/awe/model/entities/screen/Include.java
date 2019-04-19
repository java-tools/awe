/*
 * Package definition
 */
package com.almis.awe.model.entities.screen;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Include Class
 *
 * Used to include another screen target with XStream
 *
 *
 * Loads another target from another screen
 *
 *
 * @author Pablo GARCIA - 01/NOV/2011
 */
@XStreamAlias("include")
public class Include extends Element {

  private static final long serialVersionUID = 9060558303231661651L;

  // Menu which belongs the screen
  @XStreamAlias("target-screen")
  @XStreamAsAttribute
  private String targetScreen = null;

  // Initial target action (to load data from a query)
  @XStreamAlias("target-source")
  @XStreamAsAttribute
  private String targetSource = null;

  /**
   * Default constructor
   */
  public Include() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Include(Include other) throws AWException {
    super(other);
    this.targetScreen = other.targetScreen;
    this.targetSource = other.targetSource;
  }

  @Override
  public Include copy() throws AWException {
    return new Include(this);
  }

  /**
   * Returns the target screen id
   *
   * @return the target screen id
   */
  public String getTargetScreen() {
    return targetScreen;
  }

  /**
   * Stores the target screen id
   *
   * @param targetScreen the target screen id
   */
  public void setTargetScreen(String targetScreen) {
    this.targetScreen = targetScreen;
  }

  /**
   * Returns the target source id
   *
   * @return the target source id
   */
  public String getTargetSource() {
    return targetSource;
  }

  /**
   * Stores the target source id
   *
   * @param targetSource the target source id
   */
  public void setTargetSource(String targetSource) {
    this.targetSource = targetSource;
  }

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_CHILDREN;
  }
}
