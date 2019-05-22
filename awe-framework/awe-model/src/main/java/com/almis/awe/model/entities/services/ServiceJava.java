package com.almis.awe.model.entities.services;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * ServiceJava Class
 *
 * Used to parse the tag 'java' in file Services.xml with XStream
 *
 * This file contains a 'Java Service', which means a method inside a classname
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("java")
public class ServiceJava implements ServiceType {

  private static final long serialVersionUID = -6528311355783839256L;

  // Java service classname
  @XStreamAlias("classname")
  @XStreamAsAttribute
  private String className;

  // Java service method
  @XStreamAlias("method")
  @XStreamAsAttribute
  private String method;

  // Java service qualifier bean (Spring)
  @XStreamAlias("qualifier")
  @XStreamAsAttribute
  private String qualifier;

  // Input Parameter List
  @XStreamImplicit
  private List<ServiceInputParameter> parameterList;

  @Override
  public ServiceJava copy() throws AWException {
    return this.toBuilder()
      .parameterList(ListUtil.copyList(getParameterList()))
      .build();
  }

  /**
   * Retrieve launcher class
   *
   * @return Class implementation
   */
  @Override
  public String getLauncherClass() {
    return AweConstants.JAVA_CONNECTOR;
  }
}
