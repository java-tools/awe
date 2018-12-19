/*
 * Package definition
 */
package com.almis.awe.model.entities.maintain;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
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
@XStreamAlias("serve")
public class Serve extends MaintainQuery {

  private static final long serialVersionUID = 8015249032478505352L;
  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.SERVE;

  /**
   * Default constructor
   */
  public Serve() {
  }

  /**
   * Copy constr
   *
   * @param other
   */
  public Serve(Serve other) throws AWException {
    super(other);
  }

  @Override
  public Serve copy() throws AWException {
    return new Serve(this);
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
}
