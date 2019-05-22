package com.almis.awe.model.entities.services;

import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * ServiceParameter Class
 * Abstract class for Service Parameters
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamInclude({ServiceInputParameter.class, ServiceOutputParameter.class})
abstract class ServiceParameter implements Copyable {

  private static final long serialVersionUID = -4097764290823490551L;

  // Parameter type
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  // Parameter static value
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;
}
