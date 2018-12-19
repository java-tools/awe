package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Concat Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 *
 *
 * Wrapper around elements that should be concatenated from queries and maintain
 *
 *
 * @author Jorge BELLON - 07/SEP/2017
 */
@XStreamAlias("concat")
public class Concat extends Field {

  private static final long serialVersionUID = -3675559309732593756L;

  /**
   * Default constructor
   */
  public Concat() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Concat(Concat other) throws AWException {
    super(other);
  }

  @Override
  public Concat copy() throws AWException {
    return new Concat(this);
  }
}
