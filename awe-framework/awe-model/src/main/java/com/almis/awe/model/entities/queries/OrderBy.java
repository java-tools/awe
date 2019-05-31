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
 * OrderBy Class
 * Used to parse the files Queries.xml with XStream
 * Generates an order by statement
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("order-by")
public class OrderBy implements Copyable {

  private static final long serialVersionUID = -1686732994427665136L;

  // Field name
  @XStreamAlias("field")
  @XStreamAsAttribute
  private String field;

  // Table of the field
  @XStreamAlias("table")
  @XStreamAsAttribute
  private String table;

  // Order type (ASC, DESC)
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  // Nulls treatment (FIRST, LAST)
  @XStreamAlias("nulls")
  @XStreamAsAttribute
  private String nulls;

  @Override
  public OrderBy copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public String toString() {
    String fieldTable = getTable() != null ? getTable() + "." + getField() : getField();
    String nullsValue = getNulls() != null ? " NULLS " + getNulls() : "";
    return getType() != null ? fieldTable + " " + getType() + nullsValue: fieldTable;
  }
}
