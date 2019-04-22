/*
 * Package definition
 */
package com.almis.awe.model.entities.services;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

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
@XStreamAlias("rest")
public class ServiceRest extends AbstractServiceRest {

  private static final long serialVersionUID = 7493053120314893763L;

  // Service server
  @XStreamAlias("server")
  @XStreamAsAttribute
  private String server;

  /**
   * Default constructor
   */
  public ServiceRest() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ServiceRest(ServiceRest other) throws AWException {
    super(other);
    this.server = other.server;
  }

  @Override
  public ServiceRest copy() throws AWException {
    return new ServiceRest(this);
  }

  /**
   * Returns the server to access the rest service
   *
   * @return Server
   */
  public String getServer() {
    return server;
  }

  /**
   * Stores the server to access the rest service
   *
   * @param server
   */
  public void setServer(String server) {
    this.server = server;
  }

  @Override
  public String getLauncherClass() {
    return AweConstants.REST_CONNECTOR;
  }
}
