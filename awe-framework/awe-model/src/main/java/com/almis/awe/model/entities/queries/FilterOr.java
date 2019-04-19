/*
 * Package definition
 */
package com.almis.awe.model.entities.queries;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.UnionType;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * FilterOr Class
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Filter group concatenated with 'OR'
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("or")
public class FilterOr extends FilterGroup {

  private static final long serialVersionUID = -4696818779067777019L;

  /**
   * Default constructor
   */
  public FilterOr() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public FilterOr(FilterOr other) throws AWException {
    super(other);
  }

  @Override
  public FilterOr copy() throws AWException {
    return new FilterOr(this);
  }

  /**
   * Returns the filter group union type
   *
   * @return Filter group union type
   */
  @Override
  public String getUnion() {
    return UnionType.OR.toString();
  }
}
