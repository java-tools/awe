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
 * Commit Class
 *
 * Used to parse the file Maintain.xml with XStream
 *
 *
 * Target for launching a commit
 *
 *
 * @author Pablo GARCIA - 09/NOV/2010
 */
@XStreamAlias("commit")
public class Commit extends MaintainQuery {

  private static final long serialVersionUID = 1396295624485675536L;
  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.COMMIT;

  /**
   * Default constructor
   */
  public Commit() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Commit(Commit other) throws AWException {
    super(other);
  }

  @Override
  public Commit copy() throws AWException {
    return new Commit(this);
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
