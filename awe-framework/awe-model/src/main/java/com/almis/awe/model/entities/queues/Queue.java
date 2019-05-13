package com.almis.awe.model.entities.queues;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLNode;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Queue Class
 *
 * Used to parse the tag 'service' in file Services.xml with XStream
 * This file contains the list of application service
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Data
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("queue")
public class Queue implements XMLNode, Copyable {

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

  @JsonIgnore
  @Override
  public String getElementKey() {
    return getId();
  }

  @Override
  public Queue copy() throws AWException {
    return this.toBuilder()
      .request(ListUtil.copyElement(getRequest()))
      .response(ListUtil.copyElement(getResponse()))
      .build();
  }
}
