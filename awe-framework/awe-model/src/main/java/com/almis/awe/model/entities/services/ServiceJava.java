/*
 * Package definition
 */
package com.almis.awe.model.entities.services;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * ServiceJava Class
 *
 * Used to parse the tag 'java' in file Services.xml with XStream
 *
 *
 * This file contains a 'Java Service', which means a method inside a classname
 *
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@XStreamAlias("java")
public class ServiceJava extends XMLWrapper implements ServiceType {

  private static final long serialVersionUID = -6528311355783839256L;

  // Java service classname
  @XStreamAlias("classname")
  @XStreamAsAttribute
  private String className;

  // Java service method
  @XStreamAlias("method")
  @XStreamAsAttribute
  private String method;

  // Input Parameter List
  @XStreamImplicit
  private List<ServiceInputParameter> parameters;

  /**
   * Default constructor
   */
  public ServiceJava() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ServiceJava(ServiceJava other) throws AWException {
    super(other);
    this.className = other.className;
    this.method = other.method;
    this.parameters = ListUtil.copyList(other.parameters);
  }

  @Override
  public ServiceJava copy() throws AWException {
    return new ServiceJava(this);
  }

  /**
   * Returns the classname
   *
   * @return Classname
   */
  public String getClassName() {
    return className;
  }

  /**
   * Stores the classname
   *
   * @param classname Classname
   */
  public void setClassName(String classname) {
    this.className = classname;
  }

  /**
   * Returns the method
   *
   * @return Method
   */
  public String getMethod() {
    return method;
  }

  /**
   * Stores the method
   *
   * @param method Method
   */
  public void setMethod(String method) {
    this.method = method;
  }

  /**
   * Returns the service parameter list
   *
   * @return Parameter list
   */
  @Override
  public List<ServiceInputParameter> getParameterList() {
    return parameters;
  }

  /**
   * Stores the service parameter list
   *
   * @param parameters Service parameter list
   */
  @Override
  public void setParameterList(List<ServiceInputParameter> parameters) {
    this.parameters = parameters;
  }

  /**
   * Retrieve launcher class
   *
   * @return Class implementation
   */
  @Override
  public String getLauncherClass() {
    return AweConstants.JAVA_CONNECTOR;
  }
}
