package com.almis.awe.model.entities.services;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLNode;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Class
 * Used to parse the tag 'service' in file Services.xml with XStream
 * This file contains the list of application service
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamInclude({ServiceJava.class, ServiceMicroservice.class, ServiceRest.class})
@XStreamAlias("service")
public class Service implements XMLNode, Copyable {

  private static final long serialVersionUID = 2296142713995556697L;

  // Service identifier
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id;

  // Launch phase
  @XStreamAlias("launch-phase")
  @XStreamAsAttribute
  private String launchPhase;

  // Service type (web, java, etc)
  @XStreamImplicit
  private List<ServiceType> type;

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
   * @param type service type
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

  @JsonIgnore
  @Override
  public String getElementKey() {
    return this.getId();
  }

  @Override
  public Service copy() throws AWException {
    return this.toBuilder()
      .type(ListUtil.copyList(type))
      .build();
  }
}
