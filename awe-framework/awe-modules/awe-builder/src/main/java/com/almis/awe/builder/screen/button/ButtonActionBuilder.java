package com.almis.awe.builder.screen.button;

import com.almis.awe.builder.screen.base.AbstractActionBuilder;
import com.almis.awe.model.entities.screen.component.action.ButtonAction;
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
public class ButtonActionBuilder extends AbstractActionBuilder<ButtonActionBuilder, ButtonAction> {
  @Override
  public ButtonAction build() {
    return build(new ButtonAction());
  }
}
