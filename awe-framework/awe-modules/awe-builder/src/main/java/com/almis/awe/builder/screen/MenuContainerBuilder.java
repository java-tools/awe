/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen;

import com.almis.awe.builder.enumerates.MenuType;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.MenuContainer;

/**
 *
 * @author dfuentes
 */
public class MenuContainerBuilder extends AweBuilder<MenuContainerBuilder> {

  private String style;
  private MenuType type;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public MenuContainerBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    //
  }

  @Override
  public MenuContainerBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    MenuContainer menuContainer = new MenuContainer();

    menuContainer.setId(getId());

    if (getType() != null) {
      menuContainer.setType(getType().toString());
    }

    if (getStyle() != null) {
      menuContainer.setStyle(getStyle());
    }

    return menuContainer;
  }

  /**
   * Get style
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
  public MenuContainerBuilder setStyle(String style) {
    this.style = style;
    return this;
  }

  /**
   * Get type
   *
   * @return
   */
  public MenuType getType() {
    return type;
  }

  /**
   * Set type
   *
   * @param type
   * @return
   */
  public MenuContainerBuilder setType(MenuType type) {
    this.type = type;
    return this;
  }

}
