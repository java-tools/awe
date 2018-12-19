/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.container;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.screen.component.Component;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.thoughtworks.xstream.annotations.XStreamInclude;

/**
 * Container Class
 *
 * Panel class
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */

@XStreamInclude({AccordionItem.class, TabContainer.class, WizardPanel.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public abstract class Container extends Component {

  private static final long serialVersionUID = -7152960016229207319L;

  /**
   * Default constructor
   */
  public Container() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Container(Container other) throws AWException {
    super(other);
  }

  @JsonIgnore
  @Override
  public String getHelpTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_HELP_CONTAINER;
  }
}
