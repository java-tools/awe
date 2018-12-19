/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.info;

import com.almis.awe.builder.screen.criteria.CriteriaBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.criteria.InfoCriteria;

/**
 *
 * @author dfuentes
 */
public class InfoCriteriaBuilder extends CriteriaBuilder {

  private String infoStyle, title, type;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public InfoCriteriaBuilder() throws AWException {
    super();
  }

  @Override
  public InfoCriteriaBuilder setParent() {
    return this;
  }

  @Override
  public void initializeElements() {
    super.initializeElements();
  }

  @Override
  public Element build(Element element) {
    InfoCriteria infoCriteria = (InfoCriteria) super.build(element);

    if(getInfoStyle() != null) {
      infoCriteria.setInfoStyle(getInfoStyle());
    }

    if(getTitle() != null) {
      infoCriteria.setTitle(getTitle());
    }

    if( getType() != null){
      infoCriteria.setType(getType());
    }

    return infoCriteria;
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
  public InfoCriteriaBuilder setInfoStyle(String infoStyle) {
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
  public InfoCriteriaBuilder setTitle(String title) {
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
  public InfoCriteriaBuilder setType(String type) {
    this.type = type;
    return this;
  }
}
