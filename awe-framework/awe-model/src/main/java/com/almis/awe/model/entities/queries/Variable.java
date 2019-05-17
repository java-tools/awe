package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Variable Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Parses a variable in the query
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("variable")
public class Variable implements Copyable {

  private static final long serialVersionUID = 7027384293892109171L;

  // Variable identifier
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id;

  // Variable type
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  // Variable value (static)
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  // Session variable identifier
  @XStreamAlias("session")
  @XStreamAsAttribute
  private String session;

  // Property identifier
  @XStreamAlias("property")
  @XStreamAsAttribute
  private String property;

  // Variable name (from request)
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  // Optional variable
  @XStreamAlias("optional")
  @XStreamAsAttribute
  private Boolean optional;

  // Variable is from an audit field
  @XStreamOmitField
  private boolean audit;

  // Variable ignores case
  @XStreamOmitField
  private boolean ignoreCase;

  // Variable is trimmed
  @XStreamOmitField
  private boolean trim;

  // Variable is defined by sequence
  @XStreamOmitField
  private String sequence;

  @Override
  public Variable copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(getId()).append(": ");

    // Value
    if (getValue() != null) builder.append(getValue());

    // Session
    if (getSession() != null) builder.append("session(").append(getSession()).append(")");

    // Property
    if (getProperty() != null) builder.append("property(").append(getProperty()).append(")");

    // Parameter
    if (getName() != null) builder.append("parameter(").append(getName()).append(")");

    return builder.append(" [").append(getType()).append("]").toString();
  }
}
