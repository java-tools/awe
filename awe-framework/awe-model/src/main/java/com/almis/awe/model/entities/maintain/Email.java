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
 * Email Class
 *
 * Used to parse the file Maintain.xml with XStream
 * Target for launching an email send
 *
 * @author Pablo GARCIA - 09/NOV/2010
 */
@XStreamAlias("send-email")
public class Email extends MaintainQuery {

  private static final long serialVersionUID = 2606138177846420718L;
  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.EMAIL;

  /**
   * Default constructor
   */
  public Email() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Email(Email other) throws AWException {
    super(other);
  }

  @Override
  public Email copy() throws AWException {
    return new Email(this);
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
