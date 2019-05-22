package com.almis.awe.builder.screen.context;

import com.almis.awe.builder.screen.base.AbstractButtonBuilder;
import com.almis.awe.model.entities.screen.component.button.ContextButton;
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
public class ContextButtonBuilder extends AbstractButtonBuilder<ContextButtonBuilder, ContextButton> {

  @Override
  public ContextButton build() {
    return build(new ContextButton());
  }
}
