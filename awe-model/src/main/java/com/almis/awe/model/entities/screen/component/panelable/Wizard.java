/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.panelable;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Wizard Class
 *
 * Used to parse a wizard with XStream
 *
 *
 * Generates an screen wizard
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("wizard")
public class Wizard extends Panelable {

  private static final long serialVersionUID = -6523633454585300507L;

  /**
   * Default constructor
   */
  public Wizard() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Wizard(Wizard other) throws AWException {
    super(other);
  }

  @Override
  public Wizard copy() throws AWException {
    return new Wizard(this);
  }

  /**
   * Retrieve component tag
   *
   * @return
   */
  @Override
  public String getComponentType() {
    return "wizard";
  }
}
