package com.almis.awe.model.entities.services;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * ServiceMicroservice Class
 * Used to parse the tag 'microservice' in file Services.xml with XStream
 * This file contains a Microservice call using the default Microservice URL and the service parameters
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("microservice")
public class ServiceMicroservice extends AbstractServiceRest {

  private static final long serialVersionUID = 7493053120314893763L;

  // Service name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  @Override
  public ServiceMicroservice copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public String getLauncherClass() {
    return AweConstants.MICROSERVICE_CONNECTOR;
  }
}
