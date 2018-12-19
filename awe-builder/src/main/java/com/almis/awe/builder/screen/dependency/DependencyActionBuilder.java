/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.dependency;

import com.almis.awe.builder.enumerates.DependencyActionType;
import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.action.DependencyAction;

/**
 *
 * @author dfuentes
 */
public class DependencyActionBuilder extends AweBuilder<DependencyActionBuilder> {

  private DependencyActionType type;
  private ServerAction serverAction;
  private Boolean asynchronous, silent;
  private String context, target, targetAction, value;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public DependencyActionBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    //
  }

  @Override
  public DependencyActionBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    DependencyAction dependencyAction = new DependencyAction();

    dependencyAction.setId(getId());

    if (getType() != null) {
      dependencyAction.setType(getType().toString());
    }

    if (getServerAction() != null) {
      dependencyAction.setServerAction(getServerAction().toString());
    }

    if (isAsynchronous() != null) {
      dependencyAction.setAsync(String.valueOf(isAsynchronous()));
    }

    if (isSilent() != null) {
      dependencyAction.setSilent(String.valueOf(isSilent()));
    }

    if (getContext() != null) {
      dependencyAction.setScreenContext(getContext());
    }

    if (getTarget() != null) {
      dependencyAction.setTarget(getTarget());
    }

    if (getTargetAction() != null) {
      dependencyAction.setTargetAction(getTargetAction());
    }

    if (getValue() != null) {
      dependencyAction.setValue(getValue());
    }

    return dependencyAction;
  }

  /**
   * Get type
   *
   * @return
   */
  public DependencyActionType getType() {
    return type;
  }

  /**
   * Set type
   *
   * @param type
   */
  public DependencyActionBuilder setType(DependencyActionType type) {
    this.type = type;
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
   */
  public DependencyActionBuilder setServerAction(ServerAction serverAction) {
    this.serverAction = serverAction;
    return this;
  }

  /**
   * Is asynchronous
   *
   * @return
   */
  public Boolean isAsynchronous() {
    return asynchronous;
  }

  /**
   * Set asynchronous
   *
   * @param asynchronous
   */
  public DependencyActionBuilder setAsynchronous(Boolean asynchronous) {
    this.asynchronous = asynchronous;
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
   */
  public DependencyActionBuilder setSilent(Boolean silent) {
    this.silent = silent;
    return this;
  }

  /**
   * Get context
   *
   * @return
   */
  public String getContext() {
    return context;
  }

  /**
   * Set context
   *
   * @param context
   */
  public DependencyActionBuilder setContext(String context) {
    this.context = context;
    return this;
  }

  /**
   * Get target
   *
   * @return
   */
  public String getTarget() {
    return target;
  }

  /**
   * Set target
   *
   * @param target
   */
  public DependencyActionBuilder setTarget(String target) {
    this.target = target;
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
   */
  public DependencyActionBuilder setTargetAction(String targetAction) {
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
   */
  public DependencyActionBuilder setValue(String value) {
    this.value = value;
    return this;
  }
}
