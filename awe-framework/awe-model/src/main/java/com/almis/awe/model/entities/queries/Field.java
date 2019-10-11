package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Field Class
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Table Fields from queries and maintain
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("field")
public class Field extends SqlField {

  private static final long serialVersionUID = 7587109759292448862L;

  // Query to be used as field
  @XStreamAlias("query")
  @XStreamAsAttribute
  private String query;

  @Override
  public Field copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public String toString() {
    String field = null;
    // Field as Subquery
    if (getQuery() != null) {
      field = "query(" + getQuery() + ")";
    } else if (getVariable() != null) {
      // Field as variable
      field = "variable(" + getVariable() + ")";
    } else {
      // Standard field
      field = getTable() != null ? getTable() + "." + getId() : getId();
    }

    return applyFieldModifiers(field) + super.toString();
  }
}
