/*
 * Package definition
 */
package com.almis.awe.model.entities.services;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * ServiceOutputParameter Class
 *
 * Used to parse the tag 'response' in file Actions.xml with XStream
 *
 *
 * This file contains the list of system actions
 *
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@XStreamAlias("response")
public class ServiceOutputParameter extends ServiceParameter {

  private static final long serialVersionUID = -2555255456768476035L;

  /**
   * Copy constructor
   *
   * @param other
   */
  public ServiceOutputParameter(ServiceOutputParameter other) {
    super(other);
  }

  @Override
  public ServiceOutputParameter copy() throws AWException {
    return new ServiceOutputParameter(this);
  }
}
