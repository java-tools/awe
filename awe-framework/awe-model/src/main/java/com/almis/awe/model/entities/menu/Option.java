package com.almis.awe.model.entities.menu;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.screen.component.action.AbstractAction;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * Option Class
 * Used to parse the files in 'menu' folder with XStream
 * Generates a menu option
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Log4j2
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@XStreamAlias("option")
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties({"invisible", "screen", "context", "silent", "async", "type", "target", "parameters", "value"})
public class Option extends AbstractAction {

  private static final long serialVersionUID = 2874594460515726127L;

  // Module of option
  @XStreamAlias("module")
  @XStreamAsAttribute
  private String module;

  // Option is not visible
  @XStreamAlias("invisible")
  @XStreamAsAttribute
  private Boolean invisible;

  // Option is a separator
  @XStreamAlias("separator")
  @XStreamAsAttribute
  private Boolean separator;

  // Option icon
  @XStreamAlias("icon")
  @XStreamAsAttribute
  private String icon;

  // Option screen
  @XStreamAlias("screen")
  @XStreamAsAttribute
  private String screen;

  // Option is restricted
  @XStreamOmitField
  private boolean restricted;

  // screen action list
  @JsonProperty("actions")
  @XStreamOmitField
  private List<AbstractAction> actionList;

  // Parent option
  @JsonIgnore
  @XStreamOmitField
  private Option parent;

  @Override
  public Option copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Returns the option text
   *
   * @return Option text
   */
  public String getText() {
    return this.getLabel();
  }

  /**
   * Stores if option is restricted or not
   *
   * @param restricted Option is restricted
   */
  public void setRestricted(boolean restricted) {

    // Set restriction to actual option
    this.restricted = restricted;
    log.debug("Setting restricted screen: {}", getName());

    // Set restriction to children option
    List<Option> options = getElementList();
    for (Option option : options) {
      option.setRestricted(restricted);
    }
  }

  /**
   * Returns if option is invisible or not
   *
   * @return Option is invisible
   */
  @JsonIgnore
  public boolean isInvisible() {
    return getInvisible() != null && getInvisible();
  }

  /**
   * Returns if option is visible or not
   *
   * @return Option is visible
   */
  @JsonGetter("visible")
  public boolean isVisible() {
    return !isInvisible();
  }

  /**
   * Retrieve Option list
   *
   * @return Option list
   */
  @JsonGetter("options")
  public List<Option> getOptions() {
    return getElementList();
  }

  /**
   * Search an initial option by screen name
   *
   * @param screen Screen name
   * @return Option found
   */
  @JsonIgnore
  public Option getOptionByScreen(String screen) {
    // Found option
    Option found = null;
    if (screen.equalsIgnoreCase(getScreen())) {
      found = this;
    } else {
      // Search in child options
      for (Option child : getOptions()) {
        // Check module
        if (found == null) {
          found = child.getOptionByScreen(screen);
        }
      }
    }
    return found;
  }

  /**
   * Search an initial option by name
   *
   * @param optionName Option name
   * @return Option found
   */
  @JsonIgnore
  public Option getOptionByName(String optionName) {
    // Found option
    Option found = null;
    if (optionName.equalsIgnoreCase(getName())) {
      found = this;
    } else {
      // Search in child options
      for (Option child : getOptions()) {
        // Check module
        if (found == null) {
          found = child.getOptionByName(optionName);
        }
      }
    }
    return found;
  }

  /**
   * Define all options parent
   */
  public void defineRelationship() {
    // Search in child options
    for (Option child : getOptions()) {
      // Check module
      child.setParent(this);
      child.defineRelationship();
    }
  }
}
