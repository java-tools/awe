/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.data;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.screen.Message;
import com.almis.awe.model.entities.screen.component.Component;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * File Imports
 */

/**
 * ScreenAction Class
 *
 * Used to parse screen actions with XStream
 *
 *
 * Parent class for ButtonAction, Menu and Option classes
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScreenData {

  // Screen components
  private List<ScreenComponent> components = new ArrayList<>();

  // Screen data messages
  private Map<String, Message> messages = new HashMap<>();

  // Screen properties
  private Map<String, String> screenProps = new HashMap<>();

  // Screen actions
  private List<ClientAction> actions = new ArrayList<>();

  /**
   * @return the controller
   */
  public List<ScreenComponent> getComponents() {
    return components;
  }

  /**
   * Set components
   *
   * @param components the controller to set
   * @return this
   */
  public ScreenData setComponents(List<ScreenComponent> components) {
    this.components = components;
    return this;
  }

  /**
   * Add a component
   *
   * @param component Component
   * @return this
   */
  public ScreenData addComponent(ScreenComponent component) {
    // Add element
    components.add(component);
    return this;
  }

  /**
   * Add a component
   *
   * @param key        Component key
   * @param controller Component controller
   * @param model      Component model
   * @return this
   */
  public ScreenData addComponent(String key, Component controller, ComponentModel model) {
    // Add element
    components.add(new ScreenComponent()
            .setId(key)
            .setController(controller)
            .setModel(model));
    return this;
  }

  /**
   * @return the messages
   */
  public Map<String, Message> getMessages() {
    return messages;
  }

  /**
   * @param messages the messages to set
   */
  public void setMessages(Map<String, Message> messages) {
    this.messages = messages;
  }

  /**
   * Add a message
   *
   * @param key     Message key
   * @param message Message text
   */
  public void addMessage(String key, Message message) {
    // Add element
    messages.put(key, message);
  }

  /**
   * Add a error
   *
   * @param error Error to add as message
   */
  public void addError(AWException error) {
    // Add element
    actions.add(new ClientAction("message")
            .addParameter(AweConstants.ACTION_MESSAGE_TYPE_ATTRIBUTE, error.getType().toString())
            .addParameter(AweConstants.ACTION_MESSAGE_TITLE_ATTRIBUTE, error.getTitle())
            .addParameter(AweConstants.ACTION_MESSAGE_DESCRIPTION_ATTRIBUTE, error.getMessage())
            .setAsync(true)
            .setSilent(true));
  }

  /**
   * @return the screen attributes
   */
  @JsonGetter("screen")
  public Map<String, String> getScreenProperties() {
    return screenProps;
  }

  /**
   * @param screenProps the properties to set
   */
  public void setScreenProperties(Map<String, String> screenProps) {
    this.screenProps = screenProps;
  }

  /**
   * Get action list
   *
   * @return Action list
   */
  @JsonGetter("actions")
  public List<ClientAction> getActionList() {
    return actions;
  }

  /**
   * Set action list
   *
   * @param actionList Action list
   * @return this
   */
  public ScreenData setActionList(List<ClientAction> actionList) {
    this.actions = actionList;
    return this;
  }

  /**
   * Add an action
   *
   * @param action Action to add
   */
  public void addAction(ClientAction action) {
    // Add element
    actions.add(action);
  }
}
