/*
 * Package definition
 */
package com.almis.awe.model.entities.queues;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/*
 * File Imports
 */

/**
 * ResponseMessage Class
 *
 * Response message XML Wrapper
 *
 *
 * @author Pablo GARCIA - 31/OCT/2013
 */
@XStreamAlias("response-message")
public class ResponseMessage extends JmsMessage {

  private static final long serialVersionUID = 2837769427817615607L;
  // JmsMessage status
  @XStreamAlias("message-status")
  private MessageStatus status;

  /**
   * Default constructor
   */
  public ResponseMessage() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ResponseMessage(ResponseMessage other) throws AWException {
    super(other);
    this.status = other.status == null ? null : new MessageStatus(other.status);
  }

  /**
   * Retrieve the status message
   *
   * @return the status
   */
  public MessageStatus getStatus() {
    return status;
  }

  /**
   * Store the status message
   *
   * @param status the status to set
   */
  public void setStatus(MessageStatus status) {
    this.status = status;
  }
}
