/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.container;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * WizardPanel Class
 *
 * Used to parse a wizard panel with XStream
 *
 *
 * Generates an wizard panel
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("wizard-panel")
public class WizardPanel extends Container {

  private static final long serialVersionUID = 3530144017985374473L;

  /**
   * Default constructor
   */
  public WizardPanel() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public WizardPanel(WizardPanel other) throws AWException {
    super(other);
  }

  @Override
  public WizardPanel copy() throws AWException {
    return new WizardPanel(this);
  }

  /**
   * Retrieve component tag (to be overriden)
   *
   * @return
   */
  @Override
  public String getComponentTag() {
    return "wizard-panel";
  }
}
