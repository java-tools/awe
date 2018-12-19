/*
 * Package definition
 */
package com.almis.awe.model.entities.screen;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Message Class
 *
 * Used to parse a message tag with XStream
 *
 *
 * Generates a piece of code with literals that can be used to retrieve confirmation title and descriptions
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("view")
public class View extends Element {

  private static final long serialVersionUID = 5250926094818702640L;
  // View name (identifier)
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name = null;

  /**
   * Default constructor
   */
  public View() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public View(View other) throws AWException {
    super(other);
    this.name = other.name;
  }

  @Override
  public View copy() throws AWException {
    return new View(this);
  }

  /**
   * Returns the view name
   *
   * @return Parameter name
   */
  @JsonGetter("name")
  public String getName() {
    return name;
  }

  /**
   * Stores the view name
   *
   * @param name View name
   * @return View
   */
  public View setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Retrieve element template
   *
   * @return Element template
   */
  @Override
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_VIEW;
  }
}
