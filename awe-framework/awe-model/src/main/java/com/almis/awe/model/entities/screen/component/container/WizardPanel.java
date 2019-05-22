package com.almis.awe.model.entities.screen.component.container;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * WizardPanel Class
 *
 * Used to parse a wizard panel with XStream
 *
 *
 * Generates an wizard panel
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("wizard-panel")
public class WizardPanel extends Container {

  private static final long serialVersionUID = 3530144017985374473L;

  @Override
  public WizardPanel copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Retrieve component tag (to be overriden)
   *
   * @return
   */
  @Override
  public String getComponentTag() {
    return "wizard-panel";
  }
}
