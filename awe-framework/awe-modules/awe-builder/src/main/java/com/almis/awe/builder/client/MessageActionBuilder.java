package com.almis.awe.builder.client;

import com.almis.awe.model.type.AnswerType;

/**
 * Message action builder
 * @author pgarcia
 */
public class MessageActionBuilder extends ClientActionBuilder<MessageActionBuilder> {

  private static final String TYPE = "message";

  /**
   * Empty constructor
   */
  public MessageActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with type, title and message
   * @param type Message type (OK, WARNING, ERROR, INFO)
   * @param title Message title
   * @param description Message description
   */
  public MessageActionBuilder(AnswerType type, String title, String description) {
    setType(TYPE)
      .addParameter("type", type.toString())
      .addParameter("title", title)
      .addParameter(TYPE, description);
  }
}
