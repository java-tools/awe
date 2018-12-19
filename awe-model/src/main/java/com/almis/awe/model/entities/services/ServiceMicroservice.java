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
 * ServiceMicroservice Class
 *
 * Used to parse the tag 'microservice' in file Services.xml with XStream
 *
 *
 * This file contains a Microservice call using the default Microservice URL and the service parameters
 *
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@XStreamAlias("microservice")
public class ServiceMicroservice extends AbstractServiceRest {

  private static final long serialVersionUID = 7493053120314893763L;

  // Service name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  /**
   * Default constructor
   */
  public ServiceMicroservice() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ServiceMicroservice(ServiceMicroservice other) throws AWException {
    super(other);
    this.name = other.name;
  }

  @Override
  public ServiceMicroservice copy() throws AWException {
    return new ServiceMicroservice(this);
  }

  /**
   * Microservice name
   *
   * @return Microservice name
   */
  public String getName() {
    return name;
  }

  /**
   * Stores the microservice name
   *
   * @param name Microservice name
   */
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getLauncherClass() {
    return AweConstants.MICROSERVICE_CONNECTOR;
  }
}
