/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.info;

import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.builder.screen.button.ButtonBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.button.InfoButton;

/**
 *
 * @author dfuentes
 */
public class InfoButtonBuilder extends ButtonBuilder {

  private String infoStyle;
  private String title;
  private String type;
  private String unit;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public InfoButtonBuilder() throws AWException {
    super();
  }

  @Override
  public InfoButtonBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    InfoButton button = new InfoButton();

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

    if(getInfoStyle() != null){
      button.setInfoStyle(getInfoStyle());
    }

    if(getTitle() != null){
      button.setTitle(getTitle());
    }

    if(getType() != null){
      button.setType(getType());
    }

    if(getUnit() != null){
      button.setUnit(getUnit());
    }

    for (AweBuilder aweBuilder : getElementList()) {
      addElement(button, aweBuilder.build(button));
    }

    return button;
  }

  /**
   * Get info style
   *
   * @return
   */
  public String getInfoStyle() {
    return infoStyle;
  }

  /**
   * Set info style
   *
   * @param infoStyle
   * @return
   */
  public InfoButtonBuilder setInfoStyle(String infoStyle) {
    this.infoStyle = infoStyle;
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
   * @return
   */
  public InfoButtonBuilder setTitle(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get type
   *
   * @return
   */
  public String getType() {
    return type;
  }

  /**
   * Set type
   *
   * @param type
   * @return
   */
  public InfoButtonBuilder setType(String type) {
    this.type = type;
    return this;
  }

  /**
   * Get unit
   *
   * @return
   */
  public String getUnit() {
    return unit;
  }

  /**
   * Set unit
   *
   * @param unit
   * @return
   */
  public InfoButtonBuilder setUnit(String unit) {
    this.unit = unit;
    return this;
  }
}
