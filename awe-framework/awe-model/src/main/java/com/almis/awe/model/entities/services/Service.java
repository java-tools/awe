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
import com.thoughtworks.xstream.annotations.XStreamInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Class
 * Used to parse the tag 'service' in file Services.xml with XStream
 * This file contains the list of application service
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@XStreamInclude({ServiceJava.class, ServiceMicroservice.class, ServiceRest.class})
@XStreamAlias("service")
public class Service extends XMLWrapper {

  private static final long serialVersionUID = 2296142713995556697L;

  // Service identifier
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id = "";

  // Launch phase
  @XStreamAlias("launch-phase")
  @XStreamAsAttribute
  private String launchPhase;
  // Service type (web, java, etc)
  @XStreamImplicit
  private List<ServiceType> type;

  /**
   * Default constructor
   */
  public Service() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Service(Service other) throws AWException {
    super(other);
    this.id = other.id;
    this.launchPhase = other.launchPhase;
    this.type = ListUtil.copyList(other.type);
  }

  /**
   * Returns the service identifier
   *
   * @return Service identifier
   */
  public String getId() {
    return id;
  }

  /**
   * Stores the service identifier
   *
   * @param id Service identifier
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns the defined service
   *
   * @return Defined service
   */
  public ServiceType getType() {
    return this.type.get(0);
  }

  /**
   * Stores the defined service
   *
   * @param type
   */
  public void setType(ServiceType type) {
    // Initialize type array if needed
    if (this.type == null) {
      this.type = new ArrayList<>();
    }

    // Store new service
    if (type != null) {
      if (!this.type.isEmpty()) {
        this.type.set(0, type);
      } else {
        this.type.add(type);
      }
    }
  }

  /**
   * Return launch phase from service
   *
   * @return the service launch phase
   */
  public String getLaunchPhase() {
    return launchPhase;
  }

  /**
   * Store the service launch phase
   *
   * @param launchPhase the launch Phase
   */
  public void setLaunchPhase(String launchPhase) {
    this.launchPhase = launchPhase;
  }

  /**
   * Returns if identifier belongs to the element
   *
   * @param id
   * @return true if the identifier belongs to the element
   */
  @Override
  public boolean isElement(String id) {
    return this.getId().equals(id);
  }

  /**
   * Return the XML Element Key
   *
   * @return the elementKey
   */
  @Override
  public String getElementKey() {
    return this.getId();
  }
}
