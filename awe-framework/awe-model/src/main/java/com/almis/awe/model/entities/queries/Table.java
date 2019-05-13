package com.almis.awe.model.entities.queries;

import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Table Class
 *
 * Used to parse the files Queries.xml with XStream
 * Generates a table statement
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("table")
public class Table implements Copyable {

  private static final long serialVersionUID = 4859361797344642836L;

  // Table identifier (database )
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id;

  // Table subquery
  @XStreamAlias("query")
  @XStreamAsAttribute
  private String query;

  // Table owner (schema)
  @XStreamAlias("schema")
  @XStreamAsAttribute
  private String schema;

  // Table alias
  @XStreamAlias("alias")
  @XStreamAsAttribute
  private String alias;

  @Override
  public String toString() {
    if (this.getQuery() != null) {
      return new StringBuilder()
        .append("([SUBQUERY] ")
        .append(getQuery())
        .append(getAlias() != null ? ") as " + getAlias() : ")")
        .toString();
    } else {
      return new StringBuilder()
        .append(getSchema() != null ? getSchema() + "." : "")
        .append(getId())
        .append(getAlias() != null ? " " + getAlias() : "")
        .toString();
    }
  }

  @Override
  public Table copy() {
    return this.toBuilder().build();
  }
}
