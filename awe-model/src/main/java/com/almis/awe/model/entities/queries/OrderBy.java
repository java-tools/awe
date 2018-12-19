package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * OrderBy Class
 * Used to parse the files Queries.xml with XStream
 * Generates an order by statement
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("order-by")
public class OrderBy extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = -1686732994427665136L;

  // Field name
  @XStreamAlias("field")
  @XStreamAsAttribute
  private String field = null;

  // Table of the field
  @XStreamAlias("table")
  @XStreamAsAttribute
  private String table = null;

  // Order type (ASC, DESC)
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type = null;

  /**
   * Default constructor
   */
  public OrderBy() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public OrderBy(OrderBy other) {
    super(other);
    this.field = other.field;
    this.table = other.table;
    this.type = other.type;
  }

  /**
   * Returns the field name
   *
   * @return Field name
   */
  public String getField() {
    return field;
  }

  /**
   * Stores the field name
   *
   * @param field Field name
   */
  public void setField(String field) {
    this.field = field;
  }

  /**
   * Returns the table of the field
   *
   * @return Table of the field
   */
  public String getTable() {
    return table;
  }

  /**
   * Stores the table of the field
   *
   * @param table Table of the field
   */
  public void setTable(String table) {
    this.table = table;
  }

  /**
   * Returns the order type (ASC, DESC)
   *
   * @return Order type
   */
  public String getType() {
    return type;
  }

  /**
   * Stores the order type (ASC, DESC)
   *
   * @param type Order type
   */
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public OrderBy copy() throws AWException {
    return new OrderBy(this);
  }
}
