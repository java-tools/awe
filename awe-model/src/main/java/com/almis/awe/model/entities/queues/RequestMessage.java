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
 * RequestMessage Class
 *
 * Request message XML Wrapper
 *
 *
 * @author Pablo GARCIA - 31/OCT/2013
 */
@XStreamAlias("request-message")
public class RequestMessage extends JmsMessage {

  private static final long serialVersionUID = 290610745017277157L;

  /**
   * Default constructor
   */
  public RequestMessage() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public RequestMessage(RequestMessage other) throws AWException {
    super(other);
  }
}
