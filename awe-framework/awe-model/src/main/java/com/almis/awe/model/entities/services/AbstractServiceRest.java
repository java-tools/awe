package com.almis.awe.model.entities.services;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


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
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class AbstractServiceRest implements ServiceType {

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
  private List<ServiceInputParameter> parameterList;

  // Security attributes
  private String authentication;
  private String username;
  private String password;
  private String token;
}
