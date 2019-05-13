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
 * MessageStatus Class
 *
 * Used to parse the tag 'message-status' in file Queues.xml with XStream
 * This class is used to instantiate message status for a queue
 *
 * @author Pablo GARCIA - 31/OCT/2013
 */
@Data
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("message-status")
public class MessageStatus implements Copyable {

  // Status type parameter
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  // Status type translate
  @XStreamAlias("translate")
  @XStreamAsAttribute
  private String translate;

  // Status title parameter
  @XStreamAlias("title")
  @XStreamAsAttribute
  private String title;

  // Status description parameter
  @XStreamAlias("description")
  @XStreamAsAttribute
  private String description;

  @Override
  public MessageStatus copy() throws AWException {
    return this.toBuilder().build();
  }
}
