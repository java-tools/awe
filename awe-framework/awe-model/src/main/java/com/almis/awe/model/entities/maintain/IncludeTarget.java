/*
 * Package definition
 */
package com.almis.awe.model.entities.maintain;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Serve Class
 *
 * Used to parse the file Maintain.xml with XStream
 *
 *
 * Target for calling services
 *
 *
 * @author Pablo GARCIA - 09/NOV/2010
 */
@XStreamAlias("include-target")
public class IncludeTarget extends MaintainQuery {

  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.INCLUDE;

  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  /**
   * Default constructor
   */
  public IncludeTarget() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public IncludeTarget(IncludeTarget other) throws AWException {
    super(other);
    this.name = other.name;
  }

  @Override
  public IncludeTarget copy() throws AWException {
    return new IncludeTarget(this);
  }

  /**
   * Returns the maintain type
   *
   * @return Maintan type
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
