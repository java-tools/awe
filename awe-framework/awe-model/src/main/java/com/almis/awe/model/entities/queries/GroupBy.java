package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * GroupBy Class
 * Used to parse the files Queries.xml with XStream
 * Generates a group by statement inside a query
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("group-by")
public class GroupBy implements Copyable {

  private static final long serialVersionUID = 7419599738628447856L;

  // Field to group by
  @XStreamAlias("field")
  @XStreamAsAttribute
  private String field;

  // Table of the field to group by
  @XStreamAlias("table")
  @XStreamAsAttribute
  private String table;

  // Function of the field to group by
  @XStreamAlias("function")
  @XStreamAsAttribute
  private String function;

  @Override
  public GroupBy copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public String toString() {
    String fieldTable = getTable() != null ? getTable() + "." + getField() : getField();
    return getFunction() != null ? getFunction() + "(" + fieldTable + ")" : fieldTable;
  }
}
