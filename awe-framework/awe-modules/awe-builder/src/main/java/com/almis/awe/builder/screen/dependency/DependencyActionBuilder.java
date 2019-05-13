package com.almis.awe.builder.screen.dependency;

import com.almis.awe.builder.screen.base.AbstractActionBuilder;
import com.almis.awe.model.entities.screen.component.action.DependencyAction;
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
public class DependencyActionBuilder extends AbstractActionBuilder<DependencyActionBuilder, DependencyAction> {
  @Override
  public DependencyAction build() {
    return build(new DependencyAction());
  }
}
