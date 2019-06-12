package com.almis.awe.builder.screen.button;

import com.almis.awe.builder.screen.base.AbstractActionBuilder;
import com.almis.awe.model.entities.screen.component.action.ButtonAction;

/**
 * @author dfuentes
 */
public class ButtonActionBuilder extends AbstractActionBuilder<ButtonActionBuilder, ButtonAction> {
  @Override
  public ButtonAction build() {
    return build(new ButtonAction());
  }
}
