package com.almis.awe.model.entities.services;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * ServiceRest Class
 *
 * Used to parse the tag 'rest' in file Services.xml with XStream
 * This file contains a rest call using the default REST URL and the service parameters
 *
 *
 * @author Pablo GARCIA - 18/MAY/2018
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("rest")
public class ServiceRest extends AbstractServiceRest {

  private static final long serialVersionUID = 7493053120314893763L;

  // Service server
  @XStreamAlias("server")
  @XStreamAsAttribute
  private String server;

  @Override
  public ServiceRest copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public String getLauncherClass() {
    return AweConstants.REST_CONNECTOR;
  }
}
