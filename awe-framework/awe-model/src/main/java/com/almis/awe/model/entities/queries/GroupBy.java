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
 * GroupBy Class
 * Used to parse the files Queries.xml with XStream
 * Generates a group by statement inside a query
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
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

  @Override
  public GroupBy copy() throws AWException {
    return this.toBuilder().build();
  }
}
