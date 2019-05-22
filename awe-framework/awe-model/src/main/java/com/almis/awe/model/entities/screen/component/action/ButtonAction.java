package com.almis.awe.model.entities.screen.component.action;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * ButtonAction Class
 *
 * Used to parse button actions with XStream
 *
 *
 * ButtonAction class extends from AbstractAction
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("button-action")
public class ButtonAction extends AbstractAction {

  private static final long serialVersionUID = -7768601748285062450L;

  @Override
  public ButtonAction copy() throws AWException {
    return this.toBuilder().build();
  }
}
