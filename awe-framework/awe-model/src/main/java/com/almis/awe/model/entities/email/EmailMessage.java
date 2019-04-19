/*
 * Package definition
 */
package com.almis.awe.model.entities.email;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * EmailMessage Class
 *
 * Used to parse the Email.xml file with XStream
 * This class is used to parse an email message (subject, body)
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
public class EmailMessage extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = 4316195653459469148L;

  // Query called
  @XStreamAlias("query")
  @XStreamAsAttribute
  private String query;

  // Local with the text
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label;

  // Variable value with the text
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  // Type of message (html/text)
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  /**
   * Default constructor
   */
  public EmailMessage() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public EmailMessage(EmailMessage other) {
    super(other);
    this.query = other.query;
    this.label = other.label;
    this.value = other.value;
    this.type = other.type;
  }

  /**
   * Returns the message query
   *
   * @return Message query
   */
  public String getQuery() {
    return query;
  }

  /**
   * Stores the message query
   *
   * @param query Message query
   */
  public void setQuery(String query) {
    this.query = query;
  }

  /**
   * Returns the message label (local with variables)
   *
   * @return Message label
   */
  public String getLabel() {
    return label;
  }

  /**
   * Stores the message label (local with variables)
   *
   * @param label Message label
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Returns the message type (text/html)
   *
   * @return Message type
   */
  public String getType() {
    return type;
  }

  /**
   * Stores the message type (text/html)
   *
   * @param type Message type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Returns the item value
   *
   * @return Item value
   */
  public String getValue() {
    return value;
  }

  /**
   * Stores the item value
   *
   * @param value Item value
   */
  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public EmailMessage copy() throws AWException {
    return new EmailMessage(this);
  }
}
