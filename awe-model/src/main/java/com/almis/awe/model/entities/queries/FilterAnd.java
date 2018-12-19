/*
 * Package definition
 */
package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.UnionType;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * FilterAnd Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Filter group concatenated with 'AND'
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("and")
public class FilterAnd extends FilterGroup {

  /**
   * Default constructor
   */
  public FilterAnd() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public FilterAnd(FilterAnd other) throws AWException {
    super(other);
  }

  @Override
  public FilterAnd copy() throws AWException {
    return new FilterAnd(this);
  }

  /**
   * Returns the filter group union type
   *
   * @return Filter group union type
   */
  @Override
  public String getUnion() {
    return UnionType.AND.toString();
  }
}
