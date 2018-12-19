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
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @author Pablo GARCIA, Pablo VIDAL - 11/JUN/2014
 */
@XStreamAlias("message")
public class Message extends Element {

  private static final long serialVersionUID = 5151876057457019598L;

  // Message description
  @XStreamAlias("message")
  @XStreamAsAttribute
  private String message = null;

  // Component title
  @XStreamAlias("title")
  @XStreamAsAttribute
  private String title = null;

  /**
   * Default constructor
   */
  public Message() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Message(Message other) throws AWException {
    super(other);
    this.message = other.message;
    this.title = other.title;
  }

  @Override
  public Message copy() throws AWException {
    return new Message(this);
  }

  /**
   * Returns the message description
   *
   * @return Message description
   */
  @JsonGetter("message")
  public String getMessage() {
    return message;
  }

  /**
   * Stores the message description
   *
   * @param message Messasge description
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Returns the title
   *
   * @return the title
   */
  @JsonGetter("title")
  public String getTitle() {
    return title;
  }

  /**
   * Stores the title
   *
   * @param title the icon to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Get elementKey
   *
   * @return key
   */
  @Override
  public String getElementKey() {
    return this.getId();
  }

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_EMPTY;
  }
}
