package com.almis.awe.model.entities.screen.component.action;

import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.menu.Option;
import com.fasterxml.jackson.annotation.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * AbstractAction Class
 * Used to parse screen actions with XStream
 * Parent class for ButtonAction, Menu and Option classes
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"visible", "contextMenu", "dependencies", "serverAction", "iconLoading", "max", "name"})
@XStreamInclude({ButtonAction.class, DependencyAction.class, Option.class})
public abstract class AbstractAction extends Element {

  private static final long serialVersionUID = -4350464649844679396L;

  // Target frame (for navigation)
  @JsonProperty("context")
  @XStreamAlias("context")
  @XStreamAsAttribute
  private String screenContext;

  // Specific object target (for destination target actions)
  @XStreamAlias("target")
  @XStreamAsAttribute
  private String target;

  // Silent action
  @XStreamAlias("silent")
  @XStreamAsAttribute
  private Boolean silent;

  // Async action
  @XStreamAlias("async")
  @XStreamAsAttribute
  private Boolean async;

  // Value action
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  // Name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  // Server Action
  @XStreamAlias("server-action")
  @XStreamAsAttribute
  private String serverAction;

  // Target Action
  @XStreamAlias("target-action")
  @XStreamAsAttribute
  private String targetAction;

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_EMPTY;
  }

  /**
   * Returns if is async
   *
   * @return Is async
   */
  @JsonGetter("async")
  public boolean isAsync() {
    return async != null && async;
  }

  /**
   * Returns if is silent
   *
   * @return Is silent
   */
  @JsonGetter("silent")
  public boolean isSilent() {
    return silent != null && silent;
  }

  /**
   * Return the value list serialized in JSON
   *
   * @return value list string
   */
  @JsonGetter("parameters")
  public ActionParameters getActionValues() {
    return new ActionParameters()
      .setTarget(getTarget() != null ? getTarget() : getName())
      .setValue(getValue())
      .setLabel(getLabel())
      .setServerAction(getServerAction())
      .setTargetAction(getTargetAction());
  }

  /**
   * Returns the default action for JSON serialization
   *
   * @return Default Action
   */
  @JsonGetter("type")
  public String getServerActionConverter() {
    String serverActionValue = getType() != null ? getType() : getServerAction();
    return serverActionValue != null ? serverActionValue : "server";
  }

  @JsonIgnore
  @Override
  public String getElementKey() {
    return AweConstants.NO_KEY;
  }
}
