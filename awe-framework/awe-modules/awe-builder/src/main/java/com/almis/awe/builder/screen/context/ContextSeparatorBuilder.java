package com.almis.awe.builder.screen.context;

import com.almis.awe.builder.screen.base.AweBuilder;
import com.almis.awe.model.entities.screen.component.button.ContextSeparator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author dfuentes
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ContextSeparatorBuilder extends AweBuilder<ContextSeparatorBuilder, ContextSeparator> {
  @Override
  public ContextSeparator build() {
    return build(new ContextSeparator());
  }
}
