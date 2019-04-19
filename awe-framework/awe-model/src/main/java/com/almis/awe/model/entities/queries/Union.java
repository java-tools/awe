package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Union Class
 * Used to parse the files Queries.xml with XStream
 * Generates an union statement
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("union")
public class Union extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = 336661745474056448L;

  // query name
  @XStreamAlias("query")
  @XStreamAsAttribute
  private String query = null;

  // Order type (ALL)
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type = null;

  /**
   * Default constructor
   */
  public Union() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Union(Union other) {
    super(other);
    this.query = other.query;
    this.type = other.type;
  }

  /**
   * Returns the query name
   *
   * @return Query name
   */
  public String getQuery() {
    return query;
  }

  /**
   * Stores the query name
   *
   * @param query Query name
   */
  public void setQuery(String query) {
    this.query = query;
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
  public Union copy() throws AWException {
    return new Union(this);
  }
}
