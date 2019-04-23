/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.button;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * ContextButton Class
 *
 * Used to add an context menu option with XStream
 *
 *
 * @author Pablo GARCIA - 31/MAY/2013
 */
@XStreamAlias("context-button")
public class ContextButton extends Button {

  private static final long serialVersionUID = -3273278205411047836L;

  /**
   * Default constructor
   */
  public ContextButton() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ContextButton(ContextButton other) throws AWException {
    super(other);
  }

  @Override
  public ContextButton copy() throws AWException {
    return new ContextButton(this);
  }

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_EMPTY;
  }

  @Override
  @JsonIgnore
  public String getComponentTag() {
    return AweConstants.TEMPLATE_EMPTY;
  }
}
