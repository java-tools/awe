package com.almis.awe.model.entities.menu;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Menu Class
 *
 * Used to parse the files in 'menu' folder with XStream
 * Generates a menu
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("menu")
public class Menu extends Element {

  private static final long serialVersionUID = -786230210617881807L;

  // Option screen
  @JsonIgnore
  @XStreamAlias("screen")
  @XStreamAsAttribute
  private String screen;

  // Target frame (for navigation)
  @JsonIgnore
  @XStreamAlias("context")
  @XStreamAsAttribute
  private String screenContext;

  // Menu default action
  @JsonIgnore
  @XStreamAlias("default-action")
  @XStreamAsAttribute
  private String defaultAction;

  @Override
  public Menu copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Search an initial option by screen name
   *
   * @param screen Screen name
   * @return Option found
   */
  @JsonIgnore
  public Option getOptionByScreen(String screen) {
    Option found = null;
    List<Option> optionList = getElementList();
    // Search in child options
    for (Option child : optionList) {
      // Check module
      if (found == null) {
        found = child.getOptionByScreen(screen);
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
    Option found = null;
    // Search in child options
    List<Option> optionList = getElementList();
    for (Option child : optionList) {
      // Check module
      if (found == null) {
        found = child.getOptionByName(optionName);
      }
    }

    return found;
  }

  /**
   * Define all options parent
   */
  public void defineRelationship() {
    List<Option> options = getElementList();
    for (Option option : options) {
      // Check module
      option.defineRelationship();
    }
  }
}