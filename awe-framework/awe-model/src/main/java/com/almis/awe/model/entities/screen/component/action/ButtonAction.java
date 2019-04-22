/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.action;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * ButtonAction Class
 *
 * Used to parse button actions with XStream
 *
 *
 * ButtonAction class extends from ScreenAction
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("button-action")
public class ButtonAction extends ScreenAction {

  private static final long serialVersionUID = -7768601748285062450L;

  /**
   * Default constructor
   */
  public ButtonAction() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ButtonAction(ButtonAction other) throws AWException {
    super(other);
  }

  @Override
  public ButtonAction copy() throws AWException {
    return new ButtonAction(this);
  }
}
