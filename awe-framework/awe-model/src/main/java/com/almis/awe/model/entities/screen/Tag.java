package com.almis.awe.model.entities.screen;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Tag Class
 * Used to parse screen actions with XStream
 * Default HTML tag
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("tag")
public class Tag extends Element {

  private static final long serialVersionUID = 9180792194108625980L;
  // Tag text
  @XStreamAlias("text")
  private String value = null;

  /**
   * Default constructor
   */
  public Tag() {
    super();
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Tag(Tag other) throws AWException {
    super(other);
    this.value = other.value;
  }

  @Override
  public Tag copy() throws AWException {
    return new Tag(this);
  }

  /**
   * Returns the tag text
   *
   * @return Tag Text
   */
  public String getValue() {
    return value;
  }

  /**
   * Stores the tag text
   *
   * @param value Tag Text
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Retrieve element template
   *
   * @return Element template
   */
  @Override
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_TAG;
  }
}
