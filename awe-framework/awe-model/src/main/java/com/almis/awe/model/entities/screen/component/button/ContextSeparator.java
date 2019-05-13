package com.almis.awe.model.entities.screen.component.button;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * ContextSeparator Class
 *
 * Used to add an context menu separator with XStream
 *
 *
 * @author Pablo GARCIA - 06/JUN/2013
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("context-separator")
public class ContextSeparator extends AbstractButton {

  private static final long serialVersionUID = 5664473708570111319L;

  @Override
  public ContextSeparator copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_EMPTY;
  }

  @Override
  @JsonIgnore
  public String getComponentTag() {
    return AweConstants.TEMPLATE_EMPTY;
  }

  /**
   * Sets separator property as true for JSON serialization
   *
   * @return true
   */
  @JsonGetter("separator")
  public boolean isSeparator() {
    return true;
  }

  @JsonIgnore
  @Override
  public String getHelpTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_HELP_EMPTY;
  }
}
