package com.almis.awe.model.entities;

import com.almis.awe.exception.AWException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Global Class
 *
 * Used to parse an element with name, value and label inside an XML with XStream
 *
 * @author Pablo GARCIA - 24/JUN/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("global")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = {"markdown"})
public class Global implements XMLNode, Copyable {

  private static final long serialVersionUID = 5073568362212574171L;

  // Name of the element
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  // Value of the element
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  // Label of the element
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label;

  // Global content as markdown
  private String markdown;

  @JsonIgnore
  @Override
  public String getElementKey() {
    return getName();
  }

  @Override
  public Global copy() throws AWException {
    return this.toBuilder().build();
  }
}
