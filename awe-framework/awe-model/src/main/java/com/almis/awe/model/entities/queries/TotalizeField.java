/*
 * Package definition
 */
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
 * TotalizeBy Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 *
 *
 * Fields which are going to be totalized
 *
 *
 * @author Pablo GARCIA - 22/SEP/2011
 */
@XStreamAlias("totalize-field")
public class TotalizeField extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = 7343595670534166534L;
  // Field to totalize
  @XStreamAlias("field")
  @XStreamAsAttribute
  private String field = null;

  /**
   * Copy constructor
   *
   * @param other
   */
  public TotalizeField(TotalizeField other) {
    super(other);
    this.field = other.field;
  }

  /**
   * Returns the field to filter (left side)
   *
   * @return Field to filter
   */
  public String getField() {
    return field;
  }

  /**
   * Stores the field to filter (left side)
   *
   * @param field Field to filter
   */
  public void setField(String field) {
    this.field = field;
  }

  @Override
  public TotalizeField copy() throws AWException {
    return new TotalizeField(this);
  }
}
