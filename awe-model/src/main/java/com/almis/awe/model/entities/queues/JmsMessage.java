/*
 * Package definition
 */
package com.almis.awe.model.entities.queues;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;

import java.util.List;

/*
 * File Imports
 */

/**
 * JmsMessage Class
 *
 * Superclass of MessageRequest and MessageResponse
 *
 *
 * @author Pablo GARCIA - 31/OCT/2013
 */
@XStreamInclude({RequestMessage.class, ResponseMessage.class})
public abstract class JmsMessage extends XMLWrapper {

  private static final long serialVersionUID = -3391576372405613241L;
  // JmsMessage destination
  @XStreamAlias("destination")
  @XStreamAsAttribute
  private String destination;
  // JmsMessage type
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;
  // JmsMessage text separator
  @XStreamAlias("separator")
  @XStreamAsAttribute
  private String separator;
  // JmsMessage timeout
  @XStreamAlias("timeout")
  @XStreamAsAttribute
  private String timeout;
  // JmsMessage selector
  @XStreamAlias("selector")
  @XStreamAsAttribute
  private String selector;
  // JmsMessage wrapper
  @XStreamAlias("message-wrapper")
  private MessageWrapper wrapper;
  // Parameter List
  @XStreamImplicit
  private List<MessageParameter> parameters;

  /**
   * Default constructor
   */
  public JmsMessage() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public JmsMessage(JmsMessage other) throws AWException {
    super(other);
    this.destination = other.destination;
    this.type = other.type;
    this.separator = other.separator;
    this.timeout = other.timeout;
    this.selector = other.selector;
    this.wrapper = other.wrapper == null ? null : new MessageWrapper(other.wrapper);
    this.parameters = ListUtil.copyList(other.parameters);
  }

  // Default char separator
  private static final String DEFAULT_SEPARATOR = ",";

  /**
   * Returns the message parameter list
   *
   * @return JmsMessage parameter list
   */
  public List<MessageParameter> getParameters() {
    return parameters;
  }

  /**
   * Stores the message parameter list
   *
   * @param parameters JmsMessage parameter list
   */
  public void setParameters(List<MessageParameter> parameters) {
    this.parameters = parameters;
  }

  /**
   * Retrieve message destination queue
   *
   * @return the destination
   */
  public String getDestination() {
    return destination;
  }

  /**
   * Store message destination queue
   *
   * @param destination the destination to set
   */
  public void setDestination(String destination) {
    this.destination = destination;
  }

  /**
   * Retrieve message type
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Store message type
   *
   * @param type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Retrieve message object wrapper
   *
   * @return the wrapper
   */
  public MessageWrapper getWrapper() {
    return wrapper;
  }

  /**
   * Store message object wrapper
   *
   * @param wrapper the wrapper to set
   */
  public void setWrapper(MessageWrapper wrapper) {
    this.wrapper = wrapper;
  }

  /**
   * Retrieve the text message separator
   *
   * @return the separator
   */
  public String getSeparator() {
    if (separator != null && separator.isEmpty()) {
      this.separator = DEFAULT_SEPARATOR;
    }
    return this.separator;
  }

  /**
   * Store the text message separator
   *
   * @param separator the separator to set
   */
  public void setSeparator(String separator) {
    this.separator = separator;
  }

  /**
   * Retrieves the message timeout
   *
   * @return the timeout
   */
  public String getTimeout() {
    return timeout;
  }

  /**
   * Stores the message timeout
   *
   * @param timeout the timeout to set
   */
  public void setTimeout(String timeout) {
    this.timeout = timeout;
  }

  /**
   * Retrieve message selector
   *
   * @return the selector
   */
  public String getSelector() {
    return selector;
  }

  /**
   * Store message selector
   *
   * @param selector the selector to set
   */
  public void setSelector(String selector) {
    this.selector = selector;
  }
}
