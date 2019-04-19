/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.context;

import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.button.ContextSeparator;

/**
 *
 * @author dfuentes
 */
public class ContextSeparatorBuilder extends AweBuilder<ContextSeparatorBuilder> {

  private String name;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public ContextSeparatorBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    // No need to initialize elements
  }

  @Override
  public ContextSeparatorBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    ContextSeparator contextSeparator = new ContextSeparator();

    contextSeparator.setId(getId());

    if (getName() != null) {
      contextSeparator.setName(getName());
    }

    return contextSeparator;
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
  public ContextSeparatorBuilder setName(String name) {
    this.name = name;
    return this;
  }
}
