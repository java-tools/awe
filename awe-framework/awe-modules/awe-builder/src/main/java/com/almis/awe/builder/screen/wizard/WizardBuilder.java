/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.wizard;

import com.almis.awe.builder.enumerates.InitialLoad;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.panelable.Wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class WizardBuilder extends AweBuilder<WizardBuilder> {

  private InitialLoad initialLoad;
  private String targetAction;
  private String label;
  private List<WizardPanelBuilder> wizardPanelList;
  private List<DependencyBuilder> dependencyList;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public WizardBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    wizardPanelList = new ArrayList<>();
    dependencyList = new ArrayList<>();
  }

  @Override
  public WizardBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Wizard wizard = new Wizard();

    wizard.setId(getId());

    if (getInitialLoad() != null) {
      wizard.setInitialLoad(getInitialLoad().toString());
    }

    if (getTargetAction() != null) {
      wizard.setTargetAction(getTargetAction());
    }

    if (getLabel() != null) {
      wizard.setLabel(getLabel());
    }

    for (WizardPanelBuilder wizardPanelBuilder : getWizardPanelList()) {
      addElement(wizard, wizardPanelBuilder.build(wizard));
    }

    for (DependencyBuilder dependencyBuilder : getDependencyList()) {
      addElement(wizard, dependencyBuilder.build(wizard));
    }

    return wizard;
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
  public WizardBuilder setInitialLoad(InitialLoad initialLoad) {
    this.initialLoad = initialLoad;
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
  public WizardBuilder setTargetAction(String targetAction) {
    this.targetAction = targetAction;
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
  public WizardBuilder setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * Add wizard panel
   *
   * @param wizardPanelBuilder
   * @return
   */
  public WizardBuilder addWizardPanel(WizardPanelBuilder... wizardPanelBuilder) {
    if (wizardPanelBuilder != null) {
      if (this.wizardPanelList == null) {
        this.wizardPanelList = new ArrayList<>();
      }
      this.wizardPanelList.addAll(Arrays.asList(wizardPanelBuilder));
    }
    return this;
  }

  /**
   * Get wizard panel list
   *
   * @return
   */
  public List<WizardPanelBuilder> getWizardPanelList() {
    return wizardPanelList;
  }

  /**
   * Add dependency
   *
   * @param dependency
   * @return
   */
  public WizardBuilder addDependency(DependencyBuilder... dependency) {
    if (dependency != null) {
      if (this.dependencyList == null) {
        this.dependencyList = new ArrayList<>();
      }
      this.dependencyList.addAll(Arrays.asList(dependency));
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
