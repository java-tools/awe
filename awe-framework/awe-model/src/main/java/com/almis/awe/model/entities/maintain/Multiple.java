/*
 * Package definition
 */
package com.almis.awe.model.entities.maintain;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Multiple Class
 *
 * Used to parse the file Maintain.xml with XStream
 *
 *
 * Target for multiple update records. Generates a query which allows add new records into the table
 *
 *
 * @author Pablo GARCIA - 15/MAR/2012
 */
@XStreamAlias("multiple")
public class Multiple extends MaintainQuery {

  private static final long serialVersionUID = -9159746386649780828L;
  // Audit table name
  @XStreamAlias("grid")
  @XStreamAsAttribute
  private String grid = null;
  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.MULTIPLE;

  /**
   * Default constructor
   */
  public Multiple() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Multiple(Multiple other) throws AWException {
    super(other);
    this.grid = other.grid;
  }

  @Override
  public Multiple copy() throws AWException {
    return new Multiple(this);
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
   * Returns the grid id
   *
   * @return the grid id
   */
  public String getGrid() {
    return grid;
  }

  /**
   * Stores the grid id
   *
   * @param grid the grid to set
   */
  public void setGrid(String grid) {
    this.grid = grid;
  }
}
