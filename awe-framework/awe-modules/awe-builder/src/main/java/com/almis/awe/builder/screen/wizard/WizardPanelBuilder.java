package com.almis.awe.builder.screen.wizard;

import com.almis.awe.builder.screen.base.AbstractTagBuilder;
import com.almis.awe.model.entities.screen.component.container.WizardPanel;
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
public class WizardPanelBuilder extends AbstractTagBuilder<WizardPanelBuilder, WizardPanel> {
  @Override
  public WizardPanel build() {
    return build(new WizardPanel());
  }
}
