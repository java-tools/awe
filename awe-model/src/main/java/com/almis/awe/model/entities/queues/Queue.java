package com.almis.awe.model.entities.queues;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Queue Class
 *
 * Used to parse the tag 'service' in file Services.xml with XStream
 *
 *
 * This file contains the list of application service
 *
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@XStreamAlias("queue")
public class Queue extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = 5332690961169706019L;
  // Queue identifier
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id;
  // Queue request message
  @XStreamAlias("request-message")
  private RequestMessage request;
  // Queue response message
  @XStreamAlias("response-message")
  private ResponseMessage response;

  /**
   * Constructor
   */
  public Queue() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Queue(Queue other) throws AWException {
    this.id = other.id;
    this.request = other.request == null ? null : new RequestMessage(other.request);
    this.response = other.response == null ? null : new ResponseMessage(other.response);
  }

  /**
   * Returns the service identifier
   *
   * @return Queue identifier
   */
  public String getId() {
    return id;
  }

  /**
   * Stores the service identifier
   *
   * @param id Queue identifier
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns if identifier belongs to the element
   *
   * @param ide
   * @return true if the identifier belongs to the element
   */
  @Override
  public boolean isElement(String ide) {
    return this.getId().equals(ide);
  }

  /**
   * Return the XML Element Key
   *
   * @return the elementKey
   */
  @Override
  public String getElementKey() {
    return this.getId();
  }

  /**
   * Retrieve queue request message
   *
   * @return the request
   */
  public RequestMessage getRequest() {
    return request;
  }

  /**
   * Store queue request message
   *
   * @param request the request to set
   */
  public void setRequest(RequestMessage request) {
    this.request = request;
  }

  /**
   * Retrieve queue response message
   *
   * @return the response
   */
  public ResponseMessage getResponse() {
    return response;
  }

  /**
   * Store queue response message
   *
   * @param response the response to set
   */
  public void setResponse(ResponseMessage response) {
    this.response = response;
  }

  @Override
  public Queue copy() throws AWException {
    return new Queue(this);
  }
}
