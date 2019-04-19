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
 * DependencyAction Class
 *
 * Used to parse dependency actions with XStream
 *
 *
 * DependencyAction class extends from ScreenAction
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("dependency-action")
public class DependencyAction extends ScreenAction {

  private static final long serialVersionUID = -8550175354070722535L;

  /**
   * Default constructor
   */
  public DependencyAction() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public DependencyAction(DependencyAction other) throws AWException {
    super(other);
  }

  @Override
  public DependencyAction copy() throws AWException {
    return new DependencyAction(this);
  }
}
