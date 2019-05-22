package com.almis.awe.builder.screen;

import com.almis.awe.builder.screen.base.AweBuilder;
import com.almis.awe.model.entities.screen.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author dfuentes
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class MessageBuilder extends AweBuilder<MessageBuilder, Message> {

  private String message;
  private String title;

  @Override
  public Message build() {
    return build(new Message());
  }

  @Override
  public Message build(Message message) {
    return (Message) super.build(message)
      .setText(getMessage())
      .setTitle(getTitle());
  }
}
