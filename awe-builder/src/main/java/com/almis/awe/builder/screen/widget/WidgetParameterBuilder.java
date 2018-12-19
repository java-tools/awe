/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.widget;

import com.almis.awe.builder.enumerates.DataType;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.WidgetParameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class WidgetParameterBuilder extends AweBuilder<WidgetParameterBuilder> {

  private DataType type;
  private String value;
  private List<WidgetParameterBuilder> widgetParameterList;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public WidgetParameterBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    widgetParameterList = new ArrayList<>();
  }

  @Override
  public WidgetParameterBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    WidgetParameter widgetParameter = new WidgetParameter();

    widgetParameter.setId(getId());

    if (getType() != null) {
      widgetParameter.setType(getType().toString());
    }

    if (getValue() != null) {
      widgetParameter.setValue(getValue());
    }

    for (WidgetParameterBuilder widgetParameterBuilder : getWidgetParameterList()) {
      addElement(widgetParameter, widgetParameterBuilder.build(widgetParameter));
    }

    return widgetParameter;
  }

  /**
   * Get type
   *
   * @return
   */
  public DataType getType() {
    return type;
  }

  /**
   * Set type
   *
   * @param type
   * @return
   */
  public WidgetParameterBuilder setType(DataType type) {
    this.type = type;
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
  public WidgetParameterBuilder setValue(String value) {
    this.value = value;
    return this;
  }

  /**
   * Get widget parameter list
   *
   * @return
   */
  public List<WidgetParameterBuilder> getWidgetParameterList() {
    return widgetParameterList;
  }

  /**
   * add widget parameter
   *
   * @param widgetParameterList
   * @return
   */
  public WidgetParameterBuilder addWidgetParameter(WidgetParameterBuilder... widgetParameterList) {
    if (widgetParameterList != null) {
      if (this.widgetParameterList == null) {
        this.widgetParameterList = new ArrayList<>();
      }
      this.widgetParameterList.addAll(Arrays.asList(widgetParameterList));
    }
    return this;
  }
}
