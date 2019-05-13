package com.almis.awe.model.entities.queues;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * ResponseMessage Class
 * Response message XML Wrapper
 *
 * @author Pablo GARCIA - 31/OCT/2013
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("response-message")
public class ResponseMessage extends JmsMessage {

  private static final long serialVersionUID = 2837769427817615607L;

  // JmsMessage status
  @XStreamAlias("message-status")
  private MessageStatus status;

  @Override
  public ResponseMessage copy() throws AWException {
    return this.toBuilder()
      .parameters(ListUtil.copyList(getParameters()))
      .wrapper(ListUtil.copyElement(getWrapper()))
      .build();
  }
}
