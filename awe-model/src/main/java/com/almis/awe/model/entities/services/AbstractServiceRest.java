/*
 * Package definition
 */
package com.almis.awe.model.entities.services;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/*
 * File Imports
 */

/**
 * ServiceRest Class
 *
 * Used to parse the tag 'rest' in file Services.xml with XStream
 *
 *
 * This file contains a rest call using the default REST URL and the service parameters
 *
 *
 * @author Pablo GARCIA - 18/MAY/2018
 */
public abstract class AbstractServiceRest extends XMLWrapper implements ServiceType {

  private static final long serialVersionUID = 7493053120314893763L;

  // Service endpoint
  @XStreamAlias("endpoint")
  @XStreamAsAttribute
  private String endpoint;

  // Service http method
  @XStreamAlias("method")
  @XStreamAsAttribute
  private String method;

  // Service http method
  @XStreamAlias("content-type")
  @XStreamAsAttribute
  private String contentType;

  // Service response wrapper class
  @XStreamAlias("wrapper")
  @XStreamAsAttribute
  private String wrapper;

  // Input Parameter List
  @XStreamImplicit
  private List<ServiceInputParameter> parameters;

  /**
   * Default constructor
   */
  public AbstractServiceRest() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public AbstractServiceRest(AbstractServiceRest other) throws AWException {
    super(other);
    this.endpoint = other.endpoint;
    this.method = other.method;
    this.contentType = other.contentType;
    this.wrapper = other.wrapper;
    this.parameters = ListUtil.copyList(other.parameters);
  }

  /**
   * Returns the endpoint to access the rest service
   *
   * @return Endpoint
   */
  public String getEndpoint() {
    return endpoint;
  }

  /**
   * Stores the endpoint to access the rest service
   *
   * @param endpoint
   */
  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  /**
   * Returns the wrapper class to handle the response
   *
   * @return Wrapper
   */
  public String getWrapper() {
    return wrapper;
  }

  /**
   * Stores the wrapper class to handle the response
   *
   * @param wrapper
   */
  public void setWrapper(String wrapper) {
    this.wrapper = wrapper;
  }

  /**
   * Stores web method used to access the rest service (GET/POST/PUT/DELETE)
   *
   * @param method Web Services Server URL
   */
  public void setMethod(String method) {
    this.method = method;
  }

  /**
   * Returns web method (GET/POST/PUT/DELETE)
   *
   * @return method
   */
  public String getMethod() {
    return method;
  }

  /**
   * Get content type (JSON/URLENCODED)
   *
   * @return
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * Set content type (JSON/URLENCODED)
   *
   * @param contentType
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Retrieves the parameter list
   *
   * @return Parameter list
   */
  @Override
  public List<ServiceInputParameter> getParameterList() {
    return this.parameters;
  }

  /**
   * Stores parameter list
   *
   * @param parameters Parameter list
   */
  @Override
  public void setParameterList(List<ServiceInputParameter> parameters) {
    this.parameters = parameters;
  }
}
