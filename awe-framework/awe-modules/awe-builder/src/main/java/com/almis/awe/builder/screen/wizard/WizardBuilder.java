package com.almis.awe.builder.screen.wizard;

import com.almis.awe.builder.screen.base.AbstractComponentBuilder;
import com.almis.awe.model.entities.screen.component.panelable.Wizard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author dfuentes
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class WizardBuilder extends AbstractComponentBuilder<WizardBuilder, Wizard> {

  @Override
  public Wizard build() {
    return build(new Wizard());
  }

  /**
   * Add wizard panel
   *
   * @param wizardPanelBuilder
   * @return
   */
  public WizardBuilder addWizardPanel(WizardPanelBuilder... wizardPanelBuilder) {
    addAllElements(wizardPanelBuilder);
    return this;
  }
}
