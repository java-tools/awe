/*
 * Package definition
 */
package com.almis.awe.model.entities.actions;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Call Class
 *
 * Used to parse the Actions.xml file with XStream
 *
 *
 * This class is used to parse a call
 *
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
public class Call extends XMLWrapper {

  private static final long serialVersionUID = -3441589614416031154L;
  // Service called
  @XStreamAlias("service")
  @XStreamAsAttribute
  private String service;

  /**
   * Default constructor
   */
  public Call() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Call(Call other) throws AWException {
    super(other);
    this.service = other.service;
  }

  /**
   * Returns the service name
   *
   * @return Service name
   */
  public String getService() {
    return service;
  }

  /**
   * Stores the service name
   *
   * @param service Service name
   */
  public void setService(String service) {
    this.service = service;
  }
}
