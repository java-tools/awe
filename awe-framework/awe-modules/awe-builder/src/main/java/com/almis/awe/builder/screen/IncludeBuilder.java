/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.Include;

/**
 *
 * @author dfuentes
 */
public class IncludeBuilder extends AweBuilder<IncludeBuilder> {

  private String targetScreen;
  private String targetSource;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public IncludeBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    // No need to initialize elements
  }

  @Override
  public IncludeBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Include include = new Include();

    include.setId(getId());

    if (getTargetScreen() != null) {
      include.setTargetScreen(getTargetScreen());
    }

    if (getTargetSource() != null) {
      include.setTargetSource(getTargetSource());
    }

    return include;
  }

  /**
   * Get target screen
   *
   * @return
   */
  public String getTargetScreen() {
    return targetScreen;
  }

  /**
   * Set target screen
   *
   * @param targetScreen
   * @return
   */
  public IncludeBuilder setTargetScreen(String targetScreen) {
    this.targetScreen = targetScreen;
    return this;
  }

  /**
   * Get target source
   *
   * @return
   */
  public String getTargetSource() {
    return targetSource;
  }

  /**
   * Set target source
   *
   * @param targetSource
   * @return
   */
  public IncludeBuilder setTargetSource(String targetSource) {
    this.targetSource = targetSource;
    return this;
  }
}
