/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.button;

import com.almis.awe.builder.enumerates.ButtonType;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.button.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class ButtonBuilder extends AweBuilder<ButtonBuilder> {

  private ButtonType buttonType;
  private String label, icon, size, style, value;
  private String help, helpImage;
  private List<AweBuilder> elements;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public ButtonBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    elements = new ArrayList<>();
  }

  @Override
  public ButtonBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Button button = new Button();

    button.setId(getId());

    if (getButtonType() != null) {
      button.setType(getButtonType().toString());
    }

    if (getLabel() != null) {
      button.setLabel(getLabel());
    }

    if (getIcon() != null) {
      button.setIcon(getIcon());
    }

    if (getSize() != null) {
      button.setSize(getSize());
    }

    if (getStyle() != null) {
      button.setStyle(getStyle());
    }

    if (getValue() != null) {
      button.setValue(getValue());
    }

    if (getHelp() != null) {
      button.setHelp(getHelp());
    }

    if (getHelpImage() != null) {
      button.setHelpImage(getHelpImage());
    }

    for (AweBuilder aweBuilder : getElementList()) {
      addElement(button, aweBuilder.build(button));
    }

    return button;
  }

  /**
   * Get button type
   *
   * @return
   */
  public ButtonType getButtonType() {
    return buttonType;
  }

  /**
   * Set button type
   *
   * @param buttonType
   * @return
   */
  public ButtonBuilder setButtonType(ButtonType buttonType) {
    this.buttonType = buttonType;
    return this;
  }

  /**
   * Get label
   *
   * @return
   */
  public String getLabel() {
    return label;
  }

  /**
   * Set label
   *
   * @param label
   * @return
   */
  public ButtonBuilder setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * Get icon
   *
   * @return
   */
  public String getIcon() {
    return icon;
  }

  /**
   * Set icon
   *
   * @param icon
   * @return
   */
  public ButtonBuilder setIcon(String icon) {
    this.icon = icon;
    return this;
  }

  /**
   * Get size
   *
   * @return
   */
  public String getSize() {
    return size;
  }

  /**
   * Set size
   *
   * @param size
   * @return
   */
  public ButtonBuilder setSize(String size) {
    this.size = size;
    return this;
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
  public ButtonBuilder setStyle(String style) {
    this.style = style;
    return this;
  }

  /**
   * Get value
   *
   * @return
   */
  public String getValue() {
    return value;
  }

  /**
   * Set value
   *
   * @param value
   * @return
   */
  public ButtonBuilder setValue(String value) {
    this.value = value;
    return this;
  }

  /**
   * Get help
   *
   * @return
   */
  public String getHelp() {
    return help;
  }

  /**
   * Set help
   *
   * @param help
   * @return
   */
  public ButtonBuilder setHelp(String help) {
    this.help = help;
    return this;
  }

  /**
   * Get help image
   *
   * @return
   */
  public String getHelpImage() {
    return helpImage;
  }

  /**
   * Set help image
   *
   * @param helpImage
   * @return
   */
  public ButtonBuilder setHelpImage(String helpImage) {
    this.helpImage = helpImage;
    return this;
  }

  /**
   * Add button action
   *
   * @param buttonActionBuilder
   * @return
   */
  public ButtonBuilder addButtonAction(ButtonActionBuilder... buttonActionBuilder) {
    if (buttonActionBuilder != null) {
      this.elements.addAll(Arrays.asList(buttonActionBuilder));
    }
    return this;
  }

  /**
   * Add dependency
   *
   * @param dependencyBuilder
   * @return
   */
  public ButtonBuilder addDependency(DependencyBuilder... dependencyBuilder) {
    if (dependencyBuilder != null) {
      this.elements.addAll(Arrays.asList(dependencyBuilder));
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
