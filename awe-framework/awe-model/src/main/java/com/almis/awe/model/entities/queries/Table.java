package com.almis.awe.model.entities.queries;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Table Class
 *
 * Used to parse the files Queries.xml with XStream
 * Generates a table statement
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("table")
public class Table extends XMLWrapper implements Copyable {

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

  /**
   * Empty constructor
   */
  public Table() {}

  /**
   * Copy constructor
   *
   * @param other
   */
  public Table(Table other) {
    super(other);
    this.id = other.id;
    this.query = other.query;
    this.schema = other.schema;
    this.alias = other.alias;
  }

  /**
   * Returns the table identifier (database identifier)
   *
   * @return Table identifier
   */
  public String getId() {
    return id;
  }

  /**
   * Stores the table identifier (database identifier)
   *
   * @param id Table identifier
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns the table query (query identifier)
   *
   * @return Query identifier
   */
  public String getQuery() {
    return query;
  }

  /**
   * Stores the table query (query identifier)
   *
   * @param query Query identifier
   */
  public void setQuery(String query) {
    this.query = query;
  }

  /**
   * Returns the table alias
   *
   * @return Table alias
   */
  public String getAlias() {
    return alias;
  }

  /**
   * Stores the table alias
   *
   * @param alias Table alias
   */
  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   * @return the schema
   */
  public String getSchema() {
    return schema;
  }

  /**
   * @param schema the schema to set
   */
  public void setSchema(String schema) {
    this.schema = schema;
  }

  @Override
  public String toString() {
    return this.getId() + (this.getAlias() != null ? " " + this.getAlias() : "");
  }

  @Override
  public Table copy() throws AWException {
    return new Table(this);
  }
}
