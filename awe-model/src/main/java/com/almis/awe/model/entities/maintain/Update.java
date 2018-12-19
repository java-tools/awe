/*
 * Package definition
 */
package com.almis.awe.model.entities.maintain;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Update Class
 *
 * Used to parse the file Maintain.xml with XStream
 *
 *
 * Target for update records. Generates a query which allows to update records from the table
 *
 *
 * @author Ismael SERRANO - 28/JUN/2010
 */
@XStreamAlias("update")
public class Update extends MaintainQuery {

  private static final long serialVersionUID = -6440038028978617354L;
  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.UPDATE;

  /**
   * Default constructor
   */
  public Update() {
  }

  /**
   * @param other
   */
  public Update(Update other) throws AWException {
    super(other);
  }

  @Override
  public Update copy() throws AWException {
    return new Update(this);
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
