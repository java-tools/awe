/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.dependency;

import com.almis.awe.builder.enumerates.*;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.action.Dependency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class DependencyBuilder extends AweBuilder<DependencyBuilder> {

  private ServerAction serverAction;
  private SourceType sourceType;
  private TargetType targetType;
  private DependencyType type;
  private Boolean async, invert, silent, initial;
  private String formule, label, targetAction, value;
  private List<AweBuilder> elements;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public DependencyBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    this.elements = new ArrayList<>();
  }

  @Override
  public DependencyBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Dependency dependency = new Dependency();

    dependency.setId(getId());

    if (getServerAction() != null) {
      dependency.setServerAction(getServerAction().toString());
    }

    if (getSourceType() != null) {
      dependency.setSourceType(getSourceType().toString());
    }

    if (getTargetType() != null) {
      dependency.setTargetType(getTargetType().toString());
    }

    if (getType() != null) {
      dependency.setType(getType().toString());
    }

    if (isAsync() != null) {
      dependency.setAsync(String.valueOf(isAsync()));
    }

    if (isInvert() != null) {
      dependency.setInvert(String.valueOf(isInvert()));
    }

    if (isSilent() != null) {
      dependency.setSilent(String.valueOf(isSilent()));
    }

    if (isInitial() != null) {
      dependency.setInitial(String.valueOf(isInitial()));
    }

    if (getFormule() != null) {
      dependency.setFormule(getFormule());
    }

    if (getLabel() != null) {
      dependency.setLabel(getLabel());
    }

    if (getTargetAction() != null) {
      dependency.setTargetAction(getTargetAction());
    }

    if (getValue() != null) {
      dependency.setValue(getValue());
    }

    for (AweBuilder aweBuilder : getElementList()) {
      addElement(dependency, aweBuilder.build(dependency));
    }

    return dependency;
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
   * @return
   */
  public DependencyBuilder setServerAction(ServerAction serverAction) {
    this.serverAction = serverAction;
    return this;
  }

  /**
   * Get source type
   *
   * @return
   */
  public SourceType getSourceType() {
    return sourceType;
  }

  /**
   * Set source type
   *
   * @param sourceType
   * @return
   */
  public DependencyBuilder setSourceType(SourceType sourceType) {
    this.sourceType = sourceType;
    return this;
  }

  /**
   * Get target type
   *
   * @return
   */
  public TargetType getTargetType() {
    return targetType;
  }

  /**
   * Set target type
   *
   * @param targetType
   * @return
   */
  public DependencyBuilder setTargetType(TargetType targetType) {
    this.targetType = targetType;
    return this;
  }

  /**
   * Get type
   *
   * @return
   */
  public DependencyType getType() {
    return type;
  }

  /**
   * Set type
   *
   * @param type
   * @return
   */
  public DependencyBuilder setType(DependencyType type) {
    this.type = type;
    return this;
  }

  /**
   * Is async
   *
   * @return
   */
  public Boolean isAsync() {
    return async;
  }

  /**
   * Set async
   *
   * @param async
   * @return
   */
  public DependencyBuilder setAsync(Boolean async) {
    this.async = async;
    return this;
  }

  /**
   * Is invert
   *
   * @return
   */
  public Boolean isInvert() {
    return invert;
  }

  /**
   * Set invert
   *
   * @param invert
   * @return
   */
  public DependencyBuilder setInvert(Boolean invert) {
    this.invert = invert;
    return this;
  }

  /**
   * Is silent
   *
   * @return
   */
  public Boolean isSilent() {
    return silent;
  }

  /**
   * Set silent
   *
   * @param silent
   * @return
   */
  public DependencyBuilder setSilent(Boolean silent) {
    this.silent = silent;
    return this;
  }

  /**
   * Is initial
   *
   * @return
   */
  public Boolean isInitial() {
    return initial;
  }

  /**
   * Set initial
   *
   * @param initial
   * @return
   */
  public DependencyBuilder setInitial(Boolean initial) {
    this.initial = initial;
    return this;
  }

  /**
   * Get formule
   *
   * @return
   */
  public String getFormule() {
    return formule;
  }

  /**
   * Set formule
   *
   * @param formule
   * @return
   */
  public DependencyBuilder setFormule(String formule) {
    this.formule = formule;
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
  public DependencyBuilder setLabel(String label) {
    this.label = label;
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
  public DependencyBuilder setTargetAction(String targetAction) {
    this.targetAction = targetAction;
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
  public DependencyBuilder setValue(String value) {
    this.value = value;
    return this;
  }

  /**
   * Add dependency action
   *
   * @param dependencyAction
   * @return
   */
  public DependencyBuilder addDependencyAction(DependencyActionBuilder... dependencyAction) {
    if (dependencyAction != null) {
      this.elements.addAll(Arrays.asList(dependencyAction));
    }
    return this;
  }

  /**
   * Add dependency element
   *
   * @param dependencyElement
   * @return
   */
  public DependencyBuilder addDependencyElement(DependencyElementBuilder... dependencyElement) {
    if (dependencyElement != null) {
      this.elements.addAll(Arrays.asList(dependencyElement));
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
