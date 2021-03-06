package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.*;
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
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("constant")
public class Constant extends SqlField {

  // Variable type
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  // Variable value (static)
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  @Override
  public Constant copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public String toString() {
    String field;
    String typeValue = getType() == null ? "STRING" : getType();
    // Generate string representation
    switch (typeValue) {
      case "INTEGER":
      case "FLOAT":
      case "LONG":
      case "DOUBLE":
      case "BOOLEAN":
      case "OBJECT":
        field = getValue();
        break;
      default:
        field = "\"" + getValue() + "\"";
        break;
    }

    return applyFieldModifiers(field) + super.toString();
  }
}
