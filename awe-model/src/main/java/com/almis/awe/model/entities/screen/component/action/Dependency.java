/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.action;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;

/**
 * Dependency Class
 *
 * Used to parse a criteria dependency with XStream
 *
 *
 * @author Pablo GARCIA - 05/AGO/2010
 */
@XStreamAlias("dependency")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"visible", "iconLoading"})
public class Dependency extends Element {

  private static final long serialVersionUID = -6213314601320091023L;

  // Dependency action
  @XStreamAlias("action")
  @XStreamAsAttribute
  private String action = null;

  // Dependency initial attribute
  @XStreamAlias("initial")
  @XStreamAsAttribute
  private String initial = null;

  // Invert dependency conditions
  @XStreamAlias("invert")
  @XStreamAsAttribute
  private String invert = null;

  // Dependency value
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value = null;

  // Dependency formule
  @XStreamAlias("formule")
  @XStreamAsAttribute
  private String formule = null;

  // Dependency source
  @XStreamAlias("source-type")
  @XStreamAsAttribute
  private String sourceType = null;

  // Dependency targe
  @XStreamAlias("target-type")
  @XStreamAsAttribute
  private String targetType = null;

  // Silent action
  @XStreamAlias("silent")
  @XStreamAsAttribute
  private String silent = null;

  // Async action
  @XStreamAlias("async")
  @XStreamAsAttribute
  private String async = null;

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
  public Dependency() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Dependency(Dependency other) throws AWException {
    super(other);
    this.action = other.action;
    this.initial = other.initial;
    this.invert = other.invert;
    this.value = other.value;
    this.formule = other.formule;
    this.sourceType = other.sourceType;
    this.targetType = other.targetType;
    this.silent = other.silent;
    this.async = other.async;
    this.serverAction = other.serverAction;
    this.targetAction = other.targetAction;
  }

  @Override
  public Dependency copy() throws AWException {
    return new Dependency(this);
  }

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_EMPTY;
  }

  /**
   * Returns the dependency action
   *
   * @return the action
   */
  @JsonGetter("action")
  public String getAction() {
    return action;
  }

  /**
   * Stores the dependency action
   *
   * @param action Dependency action
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * Returns the dependency value
   *
   * @return Dependency Value
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns the dependency value for JSON serialization
   *
   * @return Dependency Value
   */
  @JsonGetter("value")
  public String getValueConverter() {
    return this.getValue();
  }

  /**
   * Stores the dependency value
   *
   * @param value Dependency Value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Return type for JSON serialization
   *
   * @return type
   */
  @JsonGetter("type")
  public String getTypeConverter() {
    if (getType() != null) {
      return getType();
    }
    return "and";
  }

  /**
   * Returns the dependency target action
   *
   * @return Dependency target action
   */
  @JsonGetter("query")
  public String getTargetActionConverter() {
    return this.getTargetAction();
  }

  /**
   * Returns the dependency formule
   *
   * @return Dependency formule
   */
  @JsonGetter("formule")
  public String getFormule() {
    return formule;
  }

  /**
   * Stores the dependency formule
   *
   * @param formule Dependency formule
   */
  public void setFormule(String formule) {
    this.formule = formule;
  }

  /**
   * Retrieve the silent
   *
   * @return the silent
   */
  @JsonGetter("silent")
  public boolean isSilent() {
    return "true".equalsIgnoreCase(this.silent);
  }

  /**
   * Store the silent attribute
   *
   * @param silent the silent to set
   */
  public void setSilent(String silent) {
    this.silent = silent;
  }

  /**
   * Retrieve the async attribute
   *
   * @return the async
   */
  @JsonGetter("async")
  public boolean isAsync() {
    return "true".equalsIgnoreCase(this.async);
  }

  /**
   * Store the async attribute
   *
   * @param async the async to set
   */
  public void setAsync(String async) {
    this.async = async;
  }

  /**
   * Returns the dependency element list for JSON serialization
   *
   * @return Dependency element list
   */
  @JsonGetter("elements")
  public List<DependencyElement> getDependencyElementConverter() {
    // Return string parameter list
    return getElementsByType(DependencyElement.class);
  }

  /**
   * Returns the dependency action list for JSON serialization
   *
   * @return Dependency action list
   */
  @JsonGetter("actions")
  public List<DependencyAction> getDependencyActionConverter() {
    return getElementsByType(DependencyAction.class);
  }

  /**
   * Returns if criteria must launch the dependency at initial load
   *
   * @return If criteria must launch the dependency at initial load
   */
  public String getInitial() {
    return initial;
  }

  /**
   * Returns if criteria must launch the dependency at initial load
   *
   * @return If criteria must launch the dependency at initial load
   */
  @JsonGetter("initial")
  public boolean isInitial() {
    return "true".equalsIgnoreCase(initial);
  }

  /**
   * Stores if criteria must launch the dependency at initial load
   *
   * @param initial If criteria must launch the dependency at initial load
   */
  public void setInitial(String initial) {
    this.initial = initial;
  }

  /**
   * Returns if dependency conditions must be inverted
   *
   * @return If dependency conditions must be inverted
   */
  public String getInvert() {
    return invert;
  }

  /**
   * Stores if dependency conditions must be inverted
   *
   * @param invert If dependency conditions must be inverted
   */
  public void setInvert(String invert) {
    this.invert = invert;
  }

  /**
   * Returns if criteria must launch the dependency at initial load
   *
   * @return If criteria must launch the dependency at initial load
   */
  @JsonGetter("invert")
  public boolean isInvert() {
    return "true".equalsIgnoreCase(invert);
  }

  /**
   * Return the dependency source (query|criteria|value|formule)
   *
   * @return the sourceType
   */
  @JsonGetter("source")
  public String getSourceType() {
    return sourceType;
  }

  /**
   * Stores the dependency source (query|criteria|value|formule)
   *
   * @param sourceType the sourceType to set
   */
  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  /**
   * Returns the dependency target (input|label|unit|select)
   *
   * @return the targetType
   */
  @JsonGetter("target")
  public String getTargetType() {
    return targetType;
  }

  /**
   * Stores the dependency target (input|label|unit|select)
   *
   * @param targetType the targetType to set
   */
  public void setTargetType(String targetType) {
    this.targetType = targetType;
  }

  /**
   * Get server action
   *
   * @return server action
   */
  public String getServerAction() {
    return serverAction;
  }

  /**
   * Set server action
   *
   * @param serverAction Server action
   * @return Action
   */
  public Dependency setServerAction(String serverAction) {
    this.serverAction = serverAction;
    return this;
  }

  /**
   * Get target action
   *
   * @return target action
   */
  public String getTargetAction() {
    return targetAction;
  }

  /**
   * Set target action
   *
   * @param targetAction target action
   * @return Action
   */
  public Dependency setTargetAction(String targetAction) {
    this.targetAction = targetAction;
    return this;
  }
}
