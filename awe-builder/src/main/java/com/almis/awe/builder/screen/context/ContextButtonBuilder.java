/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.context;

import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.builder.screen.button.ButtonBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.button.ContextButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dfuentes
 */
public class ContextButtonBuilder extends ButtonBuilder {

  private List<AweBuilder> elements;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public ContextButtonBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    super.initializeElements();
    elements = new ArrayList<>();
  }

  @Override
  public ContextButtonBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    ContextButton button = (ContextButton) super.build(element);

    for (AweBuilder aweBuilder : getElementList()) {
      addElement(button, aweBuilder.build(button));
    }

    return button;
  }

  /**
   * Add context button
   *
   * @param contextButton
   *
   * @return
   */
  public ContextButtonBuilder addContextButton(ContextButtonBuilder... contextButton) {
    if (contextButton != null) {
      this.elements.addAll(Arrays.asList(contextButton));
    }
    return this;
  }

  /**
   * Add context separator
   *
   * @param contextSeparator
   *
   * @return
   */
  public ContextButtonBuilder addContextSeparator(ContextSeparatorBuilder... contextSeparator) {
    if (contextSeparator != null) {
      this.elements.addAll(Arrays.asList(contextSeparator));
    }
    return this;
  }

  /**
   * Get element list
   *
   * @return
   */
  public List<AweBuilder> getElementList() {
    return this.elements;
  }
}
