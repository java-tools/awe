package com.almis.awe.model.entities.menu;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.action.ScreenAction;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.List;

/**
 * Option Class
 * Used to parse the files in 'menu' folder with XStream
 * Generates a menu option
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("option")
@JsonIgnoreProperties({"invisible", "screen", "context", "silent", "async", "type", "target", "parameters", "value"})
public class Option extends ScreenAction implements Copyable {

  private static final long serialVersionUID = 2874594460515726127L;

  // Module of option
  @XStreamAlias("module")
  @XStreamAsAttribute
  private String module;

  // Option is not visible
  @XStreamAlias("invisible")
  @XStreamAsAttribute
  private boolean invisible;

  // Option is a separator
  @XStreamAlias("separator")
  @XStreamAsAttribute
  private boolean separator;

  // Option icon
  @XStreamAlias("icon")
  @XStreamAsAttribute
  private String icon = null;

  // Option screen
  @XStreamAlias("screen")
  @XStreamAsAttribute
  private String screen = null;

  // Option is restricted
  @XStreamOmitField
  private boolean restrictedOption = false;

  // screen action list
  @XStreamOmitField
  private List<ScreenAction> actionList;

  /**
   * Default constructor
   */
  public Option() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Option(Option other) throws AWException {
    super(other);
    this.module = other.module;
    this.invisible = other.invisible;
    this.separator = other.separator;
    this.icon = other.icon;
    this.screen = other.screen;
  }

  @Override
  public Option copy() throws AWException {
    return new Option(this);
  }

  /**
   * Returns the module name of option
   *
   * @return the module
   */
  @JsonGetter("module")
  public String getModule() {
    return module;
  }

  /**
   * Stores the module name
   *
   * @param module the module to set
   */
  public void setModule(String module) {
    this.module = module;
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
   * Returns if option is restricted or not
   *
   * @return Option is restricted
   */
  public boolean isRestricted() {
    return restrictedOption;
  }

  /**
   * Stores if option is restricted or not
   *
   * @param resOpt Option is restricted
   */
  public void setRestricted(boolean resOpt) {

    // Set restriction to actual option
    this.restrictedOption = resOpt;
    getLogger().debug("Setting restricted screen: {0}", new Object[]{this.getName()});

    // Set restriction to children option
    if (this.getElementList() != null) {
      for (Element element : this.getElementList()) {
        Option option = (Option) element;
        option.setRestricted(resOpt);
      }
    }
  }

  /**
   * Returns the screen target name
   *
   * @return Screen target name
   */
  @JsonIgnore
  public String getScreen() {
    return screen;
  }

  /**
   * Stores the screen target name
   *
   * @param screen Screen target name
   * @return Option
   */
  public Option setScreen(String screen) {
    this.screen = screen;
    return this;
  }

  /**
   * Returns if option is invisible or not
   *
   * @return Option is invisible
   */
  @JsonGetter("visible")
  public boolean isVisible() {
    return !isInvisible();
  }

  /**
   * Returns if option is invisible or not
   *
   * @return Option is invisible
   */
  @JsonIgnore
  public boolean isInvisible() {
    return invisible;
  }

  /**
   * Stores if option is invisible or not
   *
   * @param disOpt Option is invisible
   * @return Option
   */
  public Option setInvisible(boolean disOpt) {
    this.invisible = disOpt;
    return this;
  }

  /**
   * Option is a separator
   *
   * @return the separator
   */
  public boolean isSeparator() {
    return separator;
  }

  /**
   * Set option as separator
   *
   * @param separator the separator to set
   * @return Option
   */
  public Option setSeparator(boolean separator) {
    this.separator = separator;
    return this;
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
   * Retrieve Option list
   *
   * @return Option list
   */
  @JsonGetter("actions")
  public List<ScreenAction> getActions() {
    return actionList;
  }

  /**
   * Store action list
   *
   * @param actionList Action list
   * @return Option
   */
  public Option setActions(List<ScreenAction> actionList) {
    this.actionList = actionList;
    return this;
  }

  /**
   * Get option icon
   *
   * @return Option icon
   */
  @JsonGetter("icon")
  public String getIcon() {
    return icon;
  }

  /**
   * Set option icon
   *
   * @param icon Option icon
   * @return Option
   */
  public Option setIcon(String icon) {
    this.icon = icon;
    return this;
  }

  /**
   * Search an initial option by screen name
   *
   * @param screen Screen name
   * @return Option found
   */
  @JsonIgnore
  public Option getOptionByScreen(String screen) {

    // Variable definition
    Option option = null;

    // Found screen
    if (this.getScreen() != null && this.getScreen().equals(screen)) {
      option = this;
    } else {
      // Search in child options
      List<Option> optionList = getElementList();
      for (Option child : optionList) {
        // Check module
        if (option == null) {
          child.setParent(this);
          option = child.getOptionByScreen(screen);
        }
      }
    }
    return option;
  }

  /**
   * Search an initial option by name
   *
   * @param optionName Option name
   * @return Option found
   */
  @JsonIgnore
  public Option getOptionByName(String optionName) {

    // Variable definition
    Option option = null;

    // Found option
    if (this.getName() != null && this.getName().equals(optionName)) {
      option = this;
    } else {
      // Search in child options
      List<Option> optionList = getElementList();
      for (Option child : optionList) {
        // Check module
        if (option == null) {
          child.setParent(this);
          option = child.getOptionByName(optionName);
        }
      }
    }
    return option;
  }
}
