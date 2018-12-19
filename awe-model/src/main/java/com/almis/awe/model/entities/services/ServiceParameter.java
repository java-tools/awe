/*
 * Package definition
 */
package com.almis.awe.model.entities.services;

/*
 * File Imports
 */

import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;

import java.io.Serializable;

/**
 * ServiceParameter Class
 *
 * Abstract class for Service Parameters
 *
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@XStreamInclude({ServiceInputParameter.class})
abstract class ServiceParameter extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = -4097764290823490551L;

  // Parameter type
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type = "";

  // Parameter static value
  @XStreamAlias("value")
  @XStreamAsAttribute
  private Serializable value;

  /**
   * Default constructor
   */
  public ServiceParameter() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ServiceParameter(ServiceParameter other) {
    super(other);
    this.type = other.type;
    this.value = other.value;
  }

  /**
   * Returns the parameter type
   *
   * @return Parameter Type
   */
  public String getType() {
    return type;
  }

  /**
   * Stores the parameter type
   *
   * @param type Parameter type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Returns the parameter static value
   *
   * @return Parameter static value
   */
  public Serializable getValue() {
    return value;
  }

  /**
   * Stores the parameter static value
   *
   * @param value Parameter static value
   */
  public void setValue(Serializable value) {
    this.value = value;
  }
}
