/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.button;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * ContextSeparator Class
 *
 * Used to add an context menu separator with XStream
 *
 *
 * @author Pablo GARCIA - 06/JUN/2013
 */
@XStreamAlias("context-separator")
public class ContextSeparator extends Button {

  private static final long serialVersionUID = 5664473708570111319L;

  /**
   * Default constructor
   */
  public ContextSeparator() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ContextSeparator(ContextSeparator other) throws AWException {
    super(other);
  }

  @Override
  public ContextSeparator copy() throws AWException {
    return new ContextSeparator(this);
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
    return AweConstants.NO_TAG;
  }

  /**
   * Sets separator property as true for JSON serialization
   *
   * @return true
   */
  @JsonGetter("separator")
  public boolean getSeparatorConverter() {
    return true;
  }
}
