/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.View;

/**
 *
 * @author dfuentes
 */
public class ViewBuilder extends AweBuilder<ViewBuilder> {

  private String name;
  private String style;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public ViewBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    // No need to initialize elements
  }

  @Override
  public ViewBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    View view = new View();

    view.setId(getId());

    if (getName() != null) {
      view.setName(getName());
    }

    if (getStyle() != null) {
      view.setStyle(getStyle());
    }

    return view;
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
  public ViewBuilder setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get Style
   *
   * @return
   */
  public String getStyle() {
    return style;
  }

  /**
   * Set style
   *
   * @param style
   * @return
   */
  public ViewBuilder setStyle(String style) {
    this.style = style;
    return this;
  }
}
