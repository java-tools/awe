/*
 * Package definition
 */
package com.almis.awe.model.entities.services;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.ArrayList;
import java.util.List;

/*
 * File Imports
 */

/**
 * ServiceInputParameter Class
 *
 * Used to parse the tag 'service_parameter' in file Services.xml with XStream
 *
 *
 * This class is used to instantiate input parameters for a service
 *
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@XStreamAlias("service-parameter")
public class ServiceInputParameter extends ServiceParameter {

  private static final long serialVersionUID = -2359724085691380754L;

  // Parameter name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name = "";

  // Parameter value
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  // Parameter value
  @XStreamOmitField
  private List<String> valueList = new ArrayList<>();

  // Parameter value
  @XStreamAlias("list")
  @XStreamAsAttribute
  private boolean list;

  /**
   * Default constructor
   */
  public ServiceInputParameter() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ServiceInputParameter(ServiceInputParameter other) {
    super(other);
    this.name = other.name;
    this.value = other.value;
    this.list = other.list;
  }

  /**
   * Returns the parameter name
   *
   * @return Parameter name
   */
  public String getName() {
    return name;
  }

  /**
   * Stores the paramete name
   *
   * @param name Parameter name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the parameter value
   *
   * @return Parameter value
   */
  @Override
  public String getValue() {
    return value;
  }

  /**
   * Stores the parameter value
   *
   * @param value Parameter value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Returns if the parameter is a list
   *
   * @return Parameter is a list
   */
  public boolean isList() {
    return list;
  }

  /**
   * Set parameter as list
   *
   * @param list Parameter is a list
   */
  public void setList(boolean list) {
    this.list = list;
  }

  /**
   * Returns the value list
   *
   * @return the valueList
   */
  public List<String> getValueList() {
    return valueList;
  }

  /**
   * Stores the value list
   *
   * @param valueList the valueList to set
   */
  public void setValueList(List<String> valueList) {
    this.valueList = valueList;
  }

  @Override
  public ServiceInputParameter copy() throws AWException {
    return new ServiceInputParameter(this);
  }
}
