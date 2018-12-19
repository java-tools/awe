package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * GroupBy Class
 * Used to parse the files Queries.xml with XStream
 * Generates a group by statement inside a query
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("group-by")
public class GroupBy extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = 7419599738628447856L;

  // Field to group by
  @XStreamAlias("field")
  @XStreamAsAttribute
  private String field;

  // Table of the field to group by
  @XStreamAlias("table")
  @XStreamAsAttribute
  private String table;

  /**
   * Copy constructor
   *
   * @param other
   */
  public GroupBy(GroupBy other) {
    super(other);
    this.field = other.field;
    this.table = other.table;
  }

  /**
   * Returns the field to group by
   *
   * @return Field to group by
   */
  public String getField() {
    return field;
  }

  /**
   * Stores the field to group by
   *
   * @param field Field to group by
   */
  public void setField(String field) {
    this.field = field;
  }

  /**
   * Returns the table of the field to group by
   *
   * @return Table of the field to group by
   */
  public String getTable() {
    return table;
  }

  /**
   * Stores the table of the field to group by
   *
   * @param table the table to set
   */
  public void setTable(String table) {
    this.table = table;
  }

  @Override
  public GroupBy copy() throws AWException {
    return new GroupBy(this);
  }
}
