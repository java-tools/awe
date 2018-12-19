/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.tab;

import com.almis.awe.builder.enumerates.InitialLoad;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.panelable.Tab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class TabBuilder extends AweBuilder<TabBuilder> {

  private InitialLoad initialLoad;
  private Boolean maximize;
  private String style, targetAction;
  private List<TabContainerBuilder> tabContainerList;
  private List<DependencyBuilder> dependencyList;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public TabBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    tabContainerList = new ArrayList<>();
    dependencyList = new ArrayList<>();
  }

  @Override
  public TabBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Tab tab = new Tab();

    tab.setId(getId());

    if (getInitialLoad() != null) {
      tab.setInitialLoad(getInitialLoad().toString());
    }

    if (isMaximize() != null) {
      tab.setMaximize(String.valueOf(isMaximize()));
    }

    if (getStyle() != null) {
      tab.setStyle(getStyle());
    }

    if (getTargetAction() != null) {
      tab.setTargetAction(getTargetAction());
    }

    for (TabContainerBuilder tabContainerBuilder : getTabContainerList()) {
      addElement(tab, tabContainerBuilder.build(tab));
    }
    
    for (DependencyBuilder dependencyBuilder : getDependencyList()) {
      addElement(tab, dependencyBuilder.build(tab));
    }

    return tab;
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
   * @return
   */
  public TabBuilder setInitialLoad(InitialLoad initialLoad) {
    this.initialLoad = initialLoad;
    return this;
  }

  /**
   * Is maximize
   *
   * @return
   */
  public Boolean isMaximize() {
    return maximize;
  }

  /**
   * Set maximize
   *
   * @param maximize
   * @return
   */
  public TabBuilder setMaximize(Boolean maximize) {
    this.maximize = maximize;
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
  public TabBuilder setStyle(String style) {
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
   * @return
   */
  public TabBuilder setTargetAction(String targetAction) {
    this.targetAction = targetAction;
    return this;
  }

  /**
   * Get tab container
   *
   * @return
   */
  public List<TabContainerBuilder> getTabContainerList() {
    return tabContainerList;
  }

  /**
   * Add tab container
   *
   * @param tabContainerList
   * @return
   */
  public TabBuilder addTabContainerList(TabContainerBuilder... tabContainerList) {
    if (tabContainerList != null) {
      if (this.tabContainerList == null) {
        this.tabContainerList = new ArrayList<>();
      }
      this.tabContainerList.addAll(Arrays.asList(tabContainerList));
    }
    return this;
  }

  /**
   * Add dependencies
   *
   * @param dependencyBuilder
   * @return
   */
  public TabBuilder addDependency(DependencyBuilder... dependencyBuilder) {
    if (dependencyBuilder != null) {
      if (this.dependencyList == null) {
        this.dependencyList = new ArrayList<>();
      }
      this.dependencyList.addAll(Arrays.asList(dependencyBuilder));
    }
    return this;
  }

  /**
   * Get dependency list
   *
   * @return
   */
  public List<DependencyBuilder> getDependencyList() {
    return dependencyList;
  }

}
