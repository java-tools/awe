package com.almis.awe.builder.screen.button;

import com.almis.awe.builder.screen.base.AbstractButtonBuilder;
import com.almis.awe.model.entities.screen.component.button.Button;
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
public class ButtonBuilder extends AbstractButtonBuilder<ButtonBuilder, Button> {
  @Override
  public Button build() {
    return build(new Button());
  }
}
