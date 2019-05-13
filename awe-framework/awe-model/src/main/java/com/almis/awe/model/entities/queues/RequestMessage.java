package com.almis.awe.model.entities.queues;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * RequestMessage Class
 *
 * Request message XML Wrapper
 *
 * @author Pablo GARCIA - 31/OCT/2013
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("request-message")
public class RequestMessage extends JmsMessage {
  private static final long serialVersionUID = 290610745017277157L;

  @Override
  public RequestMessage copy() throws AWException {
    return this.toBuilder()
      .parameters(ListUtil.copyList(getParameters()))
      .wrapper(ListUtil.copyElement(getWrapper()))
      .build();
  }
}
