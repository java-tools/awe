/*
 * Package definition
 */
package com.almis.awe.model.entities.queues;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * MessageStatus Class
 *
 * Used to parse the tag 'message-status' in file Queues.xml with XStream
 *
 *
 * This class is used to instantiate message status for a queue
 *
 *
 * @author Pablo GARCIA - 31/OCT/2013
 */
@XStreamAlias("message-status")
public class MessageStatus extends XMLWrapper {

  private static final long serialVersionUID = -9066175072222213277L;

  // Status type parameter
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  // Status type translate
  @XStreamAlias("translate")
  @XStreamAsAttribute
  private String translate;
  // Status title parameter
  @XStreamAlias("title")
  @XStreamAsAttribute
  private String title;

  // Status description parameter
  @XStreamAlias("description")
  @XStreamAsAttribute
  private String description;

  /**
   * Default constructor
   */
  public MessageStatus() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public MessageStatus(MessageStatus other) throws AWException {
    super(other);
    this.type = other.type;
    this.translate = other.translate;
    this.title = other.title;
    this.description = other.description;
  }

  /**
   * Retrieve status type parameter
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Store status type parameter
   *
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Retrieve status translation enumerated
   *
   * @return the translate
   */
  public String getTranslate() {
    return translate;
  }

  /**
   * Store status translation enumerated
   *
   * @param translate the translate to set
   */
  public void setTranslate(String translate) {
    this.translate = translate;
  }

  /**
   * Retrieve status title parameter
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Store status title parameter
   *
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Retrieve status description parameter
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Store status description parameter
   *
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }
}
