/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen;

import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.Frame;

/**
 *
 * @author dfuentes
 */
public class FrameBuilder extends AweBuilder<FrameBuilder> {

  private ServerAction serverAction;
  private String name;
  private String screen;
  private String screenVariable;
  private Boolean scroll;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public FrameBuilder() throws AWException {
    super();
  }

  /**
   * Initializes any needed elements
   */
  @Override
  public void initializeElements() {
    // No need to initialize elements
  }

  @Override
  public FrameBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Frame frame = new Frame();

    frame.setId(getId());

    if (getServerAction() != null) {
      frame.setServerAction(getServerAction().toString());
    }

    if (getName() != null) {
      frame.setName(getName());
    }

    if (getScreen() != null) {
      frame.setName(getScreen());
    }

    if (getScreenVariable() != null) {
      frame.setScreenVariable(getScreenVariable());
    }

    if (isScroll() != null) {
      frame.setScroll(String.valueOf(isScroll()));
    }

    return frame;
  }

  /**
   * Get server action
   *
   * @return
   */
  public ServerAction getServerAction() {
    return serverAction;
  }

  /**
   * Set server action
   *
   * @param serverAction
   * @return
   */
  public FrameBuilder setServerAction(ServerAction serverAction) {
    this.serverAction = serverAction;
    return this;
  }

  /**
   * Get name
   *
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * Set name
   *
   * @param name
   * @return
   */
  public FrameBuilder setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get screen
   *
   * @return
   */
  public String getScreen() {
    return screen;
  }

  /**
   * Set screen
   *
   * @param screen
   * @return
   */
  public FrameBuilder setScreen(String screen) {
    this.screen = screen;
    return this;
  }

  /**
   * Get screen variable
   *
   * @return
   */
  public String getScreenVariable() {
    return screenVariable;
  }

  /**
   * Set screen variable
   *
   * @param screenVariable
   * @return
   */
  public FrameBuilder setScreenVariable(String screenVariable) {
    this.screenVariable = screenVariable;
    return this;
  }

  /**
   * Is scroll
   *
   * @return
   */
  public Boolean isScroll() {
    return scroll;
  }

  /**
   * Set scroll
   *
   * @param scroll
   * @return
   */
  public FrameBuilder setScroll(Boolean scroll) {
    this.scroll = scroll;
    return this;
  }
}
