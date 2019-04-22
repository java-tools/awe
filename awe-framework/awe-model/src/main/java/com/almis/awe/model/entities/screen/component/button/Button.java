/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.button;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.action.ButtonAction;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Button Class
 *
 * Used to parse the Button Component in a screen with XStream
 *
 *
 * Launches contained Button Actions when it is clicked
 *
 *
 * @author Pablo GARCIA - 24/JUN/2010
 */
@XStreamAlias("button")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Button extends Criteria {

  private static final long serialVersionUID = 4537762712903942049L;

  // Template name
  protected static String BUTTON = "button";

  // Browser Action to launch if clicked (Grid Buttons)
  @XStreamAlias("browser-action")
  @XStreamAsAttribute
  private String browserAction = null;

  // Button Type (button, submit, reset)
  @XStreamAlias("button-type")
  @XStreamAsAttribute
  private String buttonType = null;
  // Cancel queue or not (default true)
  @XStreamAlias("cancel")
  @XStreamAsAttribute
  private String cancel = null;

  /**
   * Default constructor
   */
  public Button() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Button(Button other) throws AWException {
    super(other);
    this.browserAction = other.browserAction;
    this.buttonType = other.buttonType;
    this.cancel = other.cancel;
  }

  @Override
  public Button copy() throws AWException {
    return new Button(this);
  }

  @Override
  @JsonIgnore
  public String getComponentTag() {
    return BUTTON;
  }

  /**
   * Returns the Button Type
   *
   * @return Button Type
   */
  public String getButtonType() {
    return buttonType;
  }

  /**
   * Returns the Button Type formatted for JSON
   *
   * @return Button Type
   */
  @JsonGetter("buttonType")
  public String getButtonTypeConverter() {
    return buttonType != null ? buttonType : BUTTON;
  }

  /**
   * Stores the Button Type
   *
   * @param buttonType Button Type
   */
  public void setButtonType(String buttonType) {
    this.buttonType = buttonType;
  }

  /**
   * Returns the Browser Action
   *
   * @return Browser Action
   */
  @JsonGetter("browserAction")
  public String getBrowserAction() {
    return browserAction;
  }

  /**
   * Stores the Browser Action
   *
   * @param browserAction Browser Action
   */
  public void setBrowserAction(String browserAction) {
    this.browserAction = browserAction;
  }

  /**
   * Returns the Cancel Policy
   *
   * @return Cancel Policy
   */
  public String getCancel() {
    return cancel;
  }

  /**
   * Stores the Cancel Policy
   *
   * @param cancel Cancel Policy
   */
  public void setCancel(String cancel) {
    this.cancel = cancel;
  }

  /**
   * Returns the action list
   *
   * @return Action list
   */
  @JsonGetter("actions")
  public List<ButtonAction> getActionConverter() {

    List<ButtonAction> actionList = new ArrayList<>();

    if (this.getElementList() != null) {
      for (Element element : this.getElementList()) {
        if (element instanceof ButtonAction) {
          actionList.add((ButtonAction) element);
        }
      }
    }

    return actionList;
  }

  @JsonIgnore
  @Override
  public String getHelpTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_HELP_BUTTON;
  }
}
