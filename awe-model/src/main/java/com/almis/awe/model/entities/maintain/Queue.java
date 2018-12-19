/*
 * Package definition
 */
package com.almis.awe.model.entities.maintain;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/*
 * File Imports
 */

/**
 * Serve Class
 *
 * Used to parse the file Maintain.xml with XStream
 *
 *
 * Target for calling service
 *
 *
 * @author Pablo GARCIA - 09/NOV/2010
 */
@XStreamAlias("queue")
public class Queue extends MaintainQuery {

  private static final long serialVersionUID = -8215821861086129388L;

  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.QUEUE;

  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  /**
   * Default constructor
   */
  public Queue() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Queue(Queue other) throws AWException {
    super(other);
    this.name = other.name;
  }

  @Override
  public Queue copy() throws AWException {
    return new Queue(this);
  }

  /**
   * Returns the maintain type
   *
   * @return Maintain type
   */
  @Override
  public MaintainType getMaintainType() {
    return maintainType;
  }

  /**
   * Get queue name
   *
   * @return Queue name
   */
  public String getName() {
    return name;
  }

  /**
   * Set queue name
   *
   * @param name queue name
   */
  public void setName(String name) {
    this.name = name;
  }
}
