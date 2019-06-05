package com.almis.awe.model.entities.services;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * ServiceInputParameter Class
 * Used to parse the tag 'service_parameter' in file Services.xml with XStream
 * This class is used to instantiate input parameters for a service
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("service-parameter")
public class ServiceInputParameter extends ServiceParameter {

  // Parameter name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  // Parameter value
  @XStreamOmitField
  private List<String> valueList;

  // Parameter value
  @XStreamAlias("list")
  @XStreamAsAttribute
  private Boolean list;

  // Parameter name
  @XStreamAlias("bean-class")
  @XStreamAsAttribute
  private String beanClass;

  /**
   * Returns if is list
   * @return Is list
   */
  public boolean isList() {
    return list != null && list;
  }

  @Override
  public ServiceInputParameter copy() throws AWException {
    return this.toBuilder().build();
  }
}
