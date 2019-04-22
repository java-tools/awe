/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.action;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.menu.Option;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;

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
@XStreamInclude({ButtonAction.class, DependencyAction.class, Option.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"visible", "contextMenu", "dependencies", "serverAction", "iconLoading", "max", "name"})
public class ScreenAction extends Element {

  private static final long serialVersionUID = -4350464649844679396L;

  // Target frame (for navigation)
  @XStreamAlias("context")
  @XStreamAsAttribute
  private String screenContext = null;

  // Specific object target (for destination target actions)
  @XStreamAlias("target")
  @XStreamAsAttribute
  private String target = null;

  // Silent action
  @XStreamAlias("silent")
  @XStreamAsAttribute
  private String silent = null;

  // Async action
  @XStreamAlias("async")
  @XStreamAsAttribute
  private String async = null;

  // Value action
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value = null;

  // Name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name = null;

  // Server Action
  @XStreamAlias("server-action")
  @XStreamAsAttribute
  private String serverAction = null;

  // Target Action
  @XStreamAlias("target-action")
  @XStreamAsAttribute
  private String targetAction = null;

  /**
   * Default constructor
   */
  public ScreenAction() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ScreenAction(ScreenAction other) throws AWException {
    super(other);
    this.screenContext = other.screenContext;
    this.target = other.target;
    this.silent = other.silent;
    this.async = other.async;
    this.value = other.value;
    this.name = other.name;
    this.serverAction = other.serverAction;
    this.targetAction = other.targetAction;
  }

  @Override
  public ScreenAction copy() throws AWException {
    return new ScreenAction(this);
  }

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_EMPTY;
  }

  /**
   * Returns the action target
   *
   * @return Action target
   */
  @JsonGetter("target")
  public String getTarget() {
    return target;
  }

  /**
   * Stores the action target
   *
   * @param target Action target
   * @return Screen Action
   */
  public <T extends ScreenAction> T setTarget(String target) {
    this.target = target;
    return (T) this;
  }

  /**
   * Returns the screen context (target anchor in navigation)
   *
   * @return Screen Context
   */
  @JsonGetter("context")
  public String getScreenContext() {
    return screenContext;
  }

  /**
   * Stores the screen context (target anchor in navigation)
   *
   * @param screenContext Screen context
   * @return Screen Action
   */
  public <T extends ScreenAction> T setScreenContext(String screenContext) {
    this.screenContext = screenContext;
    return (T) this;
  }

  /**
   * Return the value list serialized in JSON
   *
   * @return value list string
   */
  @JsonGetter("parameters")
  public ObjectNode getActionValues() {
    ObjectNode actionValues = JsonNodeFactory.instance.objectNode();

    // Add option into parameters
    if (getName() != null) {
      actionValues.put("target", getName());
    }

    // Add target
    if (getTarget() != null) {
      actionValues.put("target", getTarget());
    }

    // Add action name
    if (getServerAction() != null) {
      actionValues.put("serverAction", getServerAction());
    }

    // Add target into values
    if (getTargetAction() != null) {
      actionValues.put("targetAction", getTargetAction());
    }

    // Generate parameter list in a string
    return actionValues;
  }

  /**
   * Returns the default action for JSON serialization
   *
   * @return Default Action
   */
  @JsonGetter("type")
  public String getServerActionConverter() {
    if (this.getType() != null) {
      return this.getType();
    } else if (this.getServerAction() != null) {
      return this.getServerAction();
    } else {
      return "server";
    }
  }

  /**
   * Retrieve the silent
   *
   * @return the silent
   */
  public String getSilent() {
    return silent;
  }

  /**
   * Retrieve the silent for JSON serialization
   *
   * @return the silent
   */
  @JsonGetter("silent")
  public Boolean getSilentConverter() {
    return Boolean.parseBoolean(this.getSilent());
  }

  /**
   * Store the silent attribute
   *
   * @param silent the silent to set
   * @return Screen action
   */
  public <T extends ScreenAction> T setSilent(String silent) {
    this.silent = silent;
    return (T) this;
  }

  /**
   * Retrieve the async attribute
   *
   * @return the async
   */
  public String getAsync() {
    return async;
  }

  /**
   * Retrieve the async attribute for JSON serialization
   *
   * @return the async attribute
   */
  @JsonGetter("async")
  public Boolean getAsyncConverter() {
    return Boolean.parseBoolean(this.getAsync());
  }

  /**
   * Store the async attribute
   *
   * @param async the async to set
   * @return Screen action
   */
  public <T extends ScreenAction> T setAsync(String async) {
    this.async = async;
    return (T) this;
  }

  /**
   * Retrieve the value attribute
   *
   * @return the value
   */
  @JsonGetter("value")
  public String getValue() {
    return value;
  }

  /**
   * Store the value attribute
   *
   * @param value Action value
   * @return Screen action
   */
  public <T extends ScreenAction> T setValue(String value) {
    this.value = value;
    return (T) this;
  }

  /**
   * Get action name
   *
   * @return Action name
   */
  @JsonGetter("name")
  public String getName() {
    return name;
  }

  /**
   * Set action name
   *
   * @param name Action name
   * @return Action
   */
  public <T extends ScreenAction> T setName(String name) {
    this.name = name;
    return (T) this;
  }

  /**
   * Get server action
   *
   * @return server action
   */
  @JsonIgnore
  public String getServerAction() {
    return serverAction;
  }

  /**
   * Set server action
   *
   * @param serverAction Server action
   * @return Action
   */
  public <T extends ScreenAction> T setServerAction(String serverAction) {
    this.serverAction = serverAction;
    return (T) this;
  }

  /**
   * Get target action
   *
   * @return target action
   */
  @JsonIgnore
  public String getTargetAction() {
    return targetAction;
  }

  /**
   * Set target action
   *
   * @param targetAction target action
   * @return Action
   */
  public <T extends ScreenAction> T setTargetAction(String targetAction) {
    this.targetAction = targetAction;
    return (T) this;
  }
}
