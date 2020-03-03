package com.almis.awe.model.entities.screen.component.button;

import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.Info;
import com.almis.awe.model.entities.screen.component.action.ButtonAction;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Button Class
 *
 * Used to parse the Button Component in a screen with XStream
 * Launches contained Button Actions when it is clicked
 *
 * @author Pablo GARCIA - 24/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamInclude({Button.class, Info.class, InfoButton.class, ContextButton.class, ContextSeparator.class})
public abstract class AbstractButton extends AbstractCriteria {

  private static final long serialVersionUID = 4537762712903942049L;

  // Template name
  protected static final String BUTTON = "button";

  // Browser Action to launch if clicked (Grid Buttons)
  @XStreamAlias("browser-action")
  @XStreamAsAttribute
  private String browserAction;

  // Button Type (button, submit, reset)
  @XStreamAlias("button-type")
  @XStreamAsAttribute
  private String buttonType;
  
  // Cancel queue or not (default true)
  @XStreamAlias("cancel")
  @XStreamAsAttribute
  private String cancel;

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
   * Returns the action list
   *
   * @return Action list
   */
  @JsonGetter("actions")
  public List<ButtonAction> getActionConverter() {

    List<ButtonAction> actionList = new ArrayList<>();

    for (Element element : getElementList()) {
      if (element instanceof ButtonAction) {
        actionList.add((ButtonAction) element);
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
