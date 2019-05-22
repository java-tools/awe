package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Union Class
 * Used to parse the files Queries.xml with XStream
 * Generates an union statement
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("union")
public class Union implements Copyable {

  private static final long serialVersionUID = 336661745474056448L;

  // query name
  @XStreamAlias("query")
  @XStreamAsAttribute
  private String query;

  // Order type (ALL)
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  @Override
  public Union copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder()
      .append(" UNION ")
      .append(getType() != null ? getType() + " " : "")
      .append(getQuery());

    return builder.toString();
  }
}
