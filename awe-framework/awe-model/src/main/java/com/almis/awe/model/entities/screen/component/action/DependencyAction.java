package com.almis.awe.model.entities.screen.component.action;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * DependencyAction Class
 *
 * Used to parse dependency actions with XStream
 *
 *
 * DependencyAction class extends from AbstractAction
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("dependency-action")
public class DependencyAction extends AbstractAction {
  private static final long serialVersionUID = -8550175354070722535L;

  @Override
  public DependencyAction copy() throws AWException {
    return this.toBuilder().build();
  }
}
