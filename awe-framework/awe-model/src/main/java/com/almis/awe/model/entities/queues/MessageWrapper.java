package com.almis.awe.model.entities.queues;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * MessageWrapper Class
 *
 * Used to parse the tag 'message-wrapper' in file Queues.xml with XStream
 * This class is used to instantiate message parameters for a queue
 *
 * @author Pablo GARCIA - 31/OCT/2013
 */
@Data
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("message-wrapper")
public class MessageWrapper implements Copyable {

  private static final long serialVersionUID = -5436753008930958151L;

  // Wrapper parameter name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  // Wrapper type
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  // Wrapper java classname
  @XStreamAlias("classname")
  @XStreamAsAttribute
  private String className;

  @Override
  public MessageWrapper copy() throws AWException {
    return this.toBuilder().build();
  }
}
