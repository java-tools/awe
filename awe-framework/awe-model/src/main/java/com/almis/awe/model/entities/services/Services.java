/*
 * Package definition
 */
package com.almis.awe.model.entities.services;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * File Imports
 */

/**
 * Services Class
 *
 * Used to parse the tag 'service' in file Services.xml with XStream
 * This file contains the list of application service
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@XStreamAlias("services")
public class Services extends XMLWrapper {

  private static final long serialVersionUID = -1070995945881299823L;

  // Service list
  @XStreamImplicit(itemFieldName = "service")
  private List<Service> serviceList;

  /**
   * Default constructor
   */
  public Services() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Services(Services other) throws AWException {
    super(other);
    if (other.serviceList != null) {
      this.serviceList = new ArrayList<>();
      for (Service service : other.serviceList) {
        this.serviceList.add(new Service(service));
      }
    }
  }

  @Override
  public List<Service> getBaseElementList() {
    return serviceList == null ? Collections.emptyList() : serviceList;
  }
}
