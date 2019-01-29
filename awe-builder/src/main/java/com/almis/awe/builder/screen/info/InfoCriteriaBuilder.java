/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.info;

import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.builder.screen.criteria.CriteriaBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
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
    InfoCriteria criteria = new InfoCriteria();
    criteria.setId(getId());
    if(getInfoStyle() != null) {
      criteria.setInfoStyle(getInfoStyle());
    }

    if(getTitle() != null) {
      criteria.setTitle(getTitle());
    }

    if( getType() != null){
      criteria.setType(getType());
    }

    if (getDateViewMode() != null) {
      criteria.setDateViewMode(getDateViewMode().toString());
    }
    if (getComponent() != null) {
      criteria.setComponentType(getComponent().toString());
    }
    if (getInitialLoad() != null) {
      criteria.setInitialLoad(getInitialLoad().toString());
    }
    if (getPrintable() != null) {
      criteria.setPrintable(getPrintable().toString());
    }
    if (getServerAction() != null) {
      criteria.setServerAction(getServerAction().toString());
    }

    criteria.setAreaRows(getValueAsString(getAreaRows()));
    criteria.setMax(getValueAsString(getMax()));
    criteria.setTimeout(getValueAsString(getTimeout()));

    criteria.setUnit(getUnit());
    criteria.setHelp(getHelp());
    criteria.setHelpImage(getHelpImage());
    criteria.setCheckTarget(getCheckTarget());
    criteria.setDateFormat(getDateFormat());
    criteria.setDestination(getDestination());
    criteria.setGroup(getGroup());
    criteria.setIcon(getIcon());
    criteria.setLabel(getLabel());
    criteria.setLeftLabel(getLeftLabel());
    criteria.setMessage(getMessage());
    criteria.setNumberFormat(getNumberFormat());
    criteria.setPlaceholder(getPlaceholder());
    criteria.setProperty(getProperty());
    criteria.setSession(getSession());
    criteria.setSize(getSize());
    criteria.setSpecific(getSpecific());
    criteria.setStyle(getStyle());
    criteria.setTargetAction(getTargetAction());
    criteria.setValidation(getValidation());
    criteria.setValue(getValue());
    criteria.setVariable(getVariable());

    criteria.setAutoload(getValueAsString(isAutoload()));
    criteria.setAutorefresh(getValueAsString(isAutorefresh()));
    criteria.setCapitalize(getValueAsString(isCapitalize()));
    criteria.setCheckEmpty(getValueAsString(isCheckEmpty()));
    criteria.setCheckInitial(getValueAsString(isCheckInitial()));
    criteria.setChecked(getValueAsString(isChecked()));
    criteria.setShowTodayButton(getValueAsString(isDateShowTodayButton()));
    criteria.setOptional(getValueAsString(isOptional()));
    criteria.setReadonly(getValueAsString(isReadonly()));
    criteria.setShowFutureDates(getValueAsString(isShowFutureDates()));
    criteria.setShowSlider(getValueAsString(isShowSlider()));
    criteria.setShowWeekends(getValueAsString(isShowWeekends()));
    criteria.setStrict(getValueAsString(isStrict()));
    criteria.setVisible(getValueAsString(isVisible()));

    for (AweBuilder aweBuilder : getElementList()) {
      addElement(criteria, aweBuilder.build(criteria));
    }

    return criteria;
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
