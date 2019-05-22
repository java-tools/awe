package com.almis.awe.model.entities.services;

import com.almis.awe.model.entities.XMLFile;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Services Class
 *
 * Used to parse the tag 'service' in file Services.xml with XStream
 * This file contains the list of application service
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("services")
public class Services implements XMLFile {

  private static final long serialVersionUID = -1070995945881299823L;

  // Service list
  @XStreamImplicit(itemFieldName = "service")
  private List<Service> serviceList;

  @Override
  public List<Service> getBaseElementList() {
    return serviceList == null ? new ArrayList<>() : serviceList;
  }
}
