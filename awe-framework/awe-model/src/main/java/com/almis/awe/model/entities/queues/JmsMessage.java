package com.almis.awe.model.entities.queues;

import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * JmsMessage Class
 *
 * Superclass of MessageRequest and MessageResponse
 * @author Pablo GARCIA - 31/OCT/2013
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamInclude({RequestMessage.class, ResponseMessage.class})
public abstract class JmsMessage implements Copyable {

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

  // Default char separator
  private static final String DEFAULT_SEPARATOR = ",";

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
}
