/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.Message;

/**
 * @author dfuentes
 */
public class MessageBuilder extends AweBuilder<MessageBuilder> {

  private String message;
  private String title;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public MessageBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    // No need to initialize elements
  }

  @Override
  public MessageBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Message builder = new Message();
    builder.setId(getId());
    builder.setMessage(getMessage());

    if (getTitle() != null) {
      builder.setTitle(getTitle());
    }

    return builder;
  }

  /**
   * Get message
   *
   * @return
   */
  public String getMessage() {
    return message;
  }

  /**
   * Set message
   *
   * @param message
   */
  public MessageBuilder setMessage(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get title
   *
   * @return
   */
  public String getTitle() {
    return title;
  }

  /**
   * Set title
   *
   * @param title
   */
  public MessageBuilder setTitle(String title) {
    this.title = title;
    return this;
  }
}
