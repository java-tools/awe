package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * TotalizeBy Class
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Fields used to totalize
 *
 * @author Pablo GARCIA - 22/SEP/2011
 */
@XStreamAlias("totalize-by")
public class TotalizeBy extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = -5294879694115446878L;
  // Field to totalize
  @XStreamAlias("field")
  @XStreamAsAttribute
  private String field;

  /**
   * Copy constructor
   *
   * @param other
   */
  public TotalizeBy(TotalizeBy other) {
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
  public TotalizeBy copy() throws AWException {
    return new TotalizeBy(this);
  }
}
