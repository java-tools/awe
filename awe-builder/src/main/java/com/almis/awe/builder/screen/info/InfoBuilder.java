/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.info;

import com.almis.awe.builder.enumerates.InitialLoad;
import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.builder.screen.TagBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.Info;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class InfoBuilder extends AweBuilder<InfoBuilder> {

  private InitialLoad initialLoad;
  private ServerAction serverAction;
  private Boolean autoload;
  private Boolean autorefresh;
  private String dropdownStyle;
  private String icon;
  private String label;
  private String name;
  private String property;
  private String session;
  private String style;
  private String targetAction;
  private String title;
  private String unit;
  private String value;
  private Integer max;
  private List<AweBuilder> elements;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public InfoBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    elements = new ArrayList<>();
  }

  @Override
  public InfoBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Info info = new Info();
    info.setId(getId());
    info.setDropdownStyle(getDropdownStyle());
    info.setIcon(getIcon());
    info.setLabel(getLabel());
    info.setName(getName());
    info.setStyle(getStyle());
    info.setTargetAction(getTargetAction());
    info.setTitle(getTitle());
    info.setUnit(getUnit());
    info.setValue(getValue());
    info.setAutoload(getValueAsString(isAutoload()));
    info.setAutorefresh(getValueAsString(isAutorefresh()));
    info.setMax(getValueAsString(getMax()));

    if (getInitialLoad() != null) {
      info.setInitialLoad(getInitialLoad().toString());
    }

    if (getServerAction() != null) {
      info.setServerAction(getServerAction().toString());
    }

    if (getProperty() != null) {
      //TODO check where to add this value
    }

    if (getSession() != null) {
      //TODO check where to add this value
    }

    for (AweBuilder aweBuilder : getElementList()) {
      addElement(info, aweBuilder.build(info));
    }

    return info;
  }

  /**
   * Get initial load
   *
   * @return
   */
  public InitialLoad getInitialLoad() {
    return initialLoad;
  }

  /**
   * Set initial load
   *
   * @param initialLoad
   * @return this
   */
  public InfoBuilder setInitialLoad(InitialLoad initialLoad) {
    this.initialLoad = initialLoad;
    return this;
  }

  /**
   * Get server action
   *
   * @return
   */
  public ServerAction getServerAction() {
    return serverAction;
  }

  /**
   * Set server action
   *
   * @param serverAction
   * @return this
   */
  public InfoBuilder setServerAction(ServerAction serverAction) {
    this.serverAction = serverAction;
    return this;
  }

  /**
   * Is autoload
   *
   * @return
   */
  public Boolean isAutoload() {
    return autoload;
  }

  /**
   * Set autoload
   *
   * @param autoload
   * @return this
   */
  public InfoBuilder setAutoload(Boolean autoload) {
    this.autoload = autoload;
    return this;
  }

  /**
   * Is autorefresh
   *
   * @return
   */
  public Boolean isAutorefresh() {
    return autorefresh;
  }

  /**
   * Set autorefreshs
   *
   * @param autorefresh
   * @return this
   */
  public InfoBuilder setAutorefresh(Boolean autorefresh) {
    this.autorefresh = autorefresh;
    return this;
  }

  /**
   * Get dropdown style
   *
   * @return
   */
  public String getDropdownStyle() {
    return dropdownStyle;
  }

  /**
   * Set dropdown style
   *
   * @param dropdownStyle
   * @return this
   */
  public InfoBuilder setDropdownStyle(String dropdownStyle) {
    this.dropdownStyle = dropdownStyle;
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
   * @return this
   */
  public InfoBuilder setIcon(String icon) {
    this.icon = icon;
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
   * @return this
   */
  public InfoBuilder setLabel(String label) {
    this.label = label;
    return this;
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
   * @return this
   */
  public InfoBuilder setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get property
   *
   * @return
   */
  public String getProperty() {
    return property;
  }

  /**
   * Set property
   *
   * @param property
   * @return this
   */
  public InfoBuilder setProperty(String property) {
    this.property = property;
    return this;
  }

  /**
   * Get session
   *
   * @return
   */
  public String getSession() {
    return session;
  }

  /**
   * Set session
   *
   * @param session
   * @return this
   */
  public InfoBuilder setSession(String session) {
    this.session = session;
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
   * @return this
   */
  public InfoBuilder setStyle(String style) {
    this.style = style;
    return this;
  }

  /**
   * Get target action
   *
   * @return
   */
  public String getTargetAction() {
    return targetAction;
  }

  /**
   * Set target action
   *
   * @param targetAction
   * @return this
   */
  public InfoBuilder setTargetAction(String targetAction) {
    this.targetAction = targetAction;
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
   * @return this
   */
  public InfoBuilder setTitle(String title) {
    this.title = title;
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
   * @return this
   */
  public InfoBuilder setUnit(String unit) {
    this.unit = unit;
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
   * @return this
   */
  public InfoBuilder setValue(String value) {
    this.value = value;
    return this;
  }

  /**
   * Get max
   *
   * @return
   */
  public Integer getMax() {
    return max;
  }

  /**
   * Set max
   *
   * @param max
   * @return this
   */
  public InfoBuilder setMax(Integer max) {
    this.max = max;
    return this;
  }

  /**
   * Add info
   *
   * @param info
   * @return
   */
  public InfoBuilder addInfo(InfoBuilder... info) {
    if (info != null) {
      this.elements.addAll(Arrays.asList(info));
    }
    return this;
  }

  /**
   * Add info button
   *
   * @param infoButton
   * @return
   */
  public InfoBuilder addInfoButton(InfoButtonBuilder... infoButton) {
    if (infoButton != null) {
      this.elements.addAll(Arrays.asList(infoButton));
    }
    return this;
  }

  /**
   * Add info criteria
   *
   * @param infoCriteria
   * @return
   */
  public InfoBuilder addInfoCriteria(InfoCriteriaBuilder... infoCriteria) {
    if (infoCriteria != null) {
      this.elements.addAll(Arrays.asList(infoCriteria));
    }
    return this;
  }

  /**
   * Add tag
   *
   * @param tag
   * @return
   */
  public InfoBuilder addTag(TagBuilder... tag) {
    if (tag != null) {
      this.elements.addAll(Arrays.asList(tag));
    }
    return this;
  }

  /**
   * Add dependency
   *
   * @param dependencyBuilder
   * @return
   */
  public InfoBuilder addDependency(DependencyBuilder... dependencyBuilder) {
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
    return elements;
  }
}
