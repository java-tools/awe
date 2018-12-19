/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.button;

import com.almis.awe.builder.enumerates.Action;
import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.action.ButtonAction;

/**
 *
 * @author dfuentes
 */
public class ButtonActionBuilder extends AweBuilder<ButtonActionBuilder>{
  
  private Action type;
  private ServerAction serverAction;
  private Boolean asynchronous, silent;
  private String targetAction, target, context, value;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public ButtonActionBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    asynchronous = false;
    silent = false;
  }

  @Override
  public ButtonActionBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    ButtonAction buttonAction = new ButtonAction();
    
    buttonAction.setId(getId());
    
    if( getType() != null){
    buttonAction.setType(getType().toString());
    }
    
    if( getServerAction() != null){
    buttonAction.setServerAction(getServerAction().toString());
    }
    
    if ( getTargetAction() != null){
      buttonAction.setTargetAction(getTargetAction());
    }
    
    if( getTarget() != null){
      buttonAction.setTarget(getTarget());
    }
    
    if(getValue() != null){
      buttonAction.setValue(getValue());
    }
    
    if( isAsynchronous() != null){
      buttonAction.setAsync(String.valueOf(isAsynchronous()));
    }
    
    if( isSilent()!= null){
      buttonAction.setSilent(String.valueOf(isSilent()));
    }
    
    if( getContext() != null){
      buttonAction.setScreenContext(getContext());
    }
    
    return buttonAction;
  }

  /**
   * Get type
   *
   * @return
   */
  public Action getType() {
    return type;
  }

  /**
   * Set type
   *
   * @param type
   * @return
   */
  public ButtonActionBuilder setType(Action type) {
    this.type = type;
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
  public ButtonActionBuilder setTargetAction(String targetAction) {
    this.targetAction = targetAction;
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
   * @return
   */
  public ButtonActionBuilder setServerAction(ServerAction serverAction) {
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
   * @return
   */
  public ButtonActionBuilder setAsynchronous(Boolean asynchronous) {
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
   * @return
   */
  public ButtonActionBuilder setSilent(Boolean silent) {
    this.silent = silent;
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
   * @return
   */
  public ButtonActionBuilder setTarget(String target) {
    this.target = target;
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
  public void setContext(String context) {
    this.context = context;
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
  public ButtonActionBuilder setValue(String value) {
    this.value = value;
    return this;
  }  
}
