/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.info;

import com.almis.awe.builder.screen.button.ButtonBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.button.InfoButton;

/**
 *
 * @author dfuentes
 */
public class InfoButtonBuilder extends ButtonBuilder {

  private String infoStyle, title, type, unit;

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
  public void initializeElements() {
    super.initializeElements();
  }

  @Override
  public Element build(Element element) {
    InfoButton infoButton = (InfoButton) super.build(element);

    if(getInfoStyle() != null){
      infoButton.setInfoStyle(getInfoStyle());
    }

    if(getTitle() != null){
      infoButton.setTitle(getTitle());
    }

    if(getType() != null){
      infoButton.setType(getType());
    }

    if(getUnit() != null){
      infoButton.setUnit(getUnit());
    }

    return infoButton;
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
