package com.almis.awe.builder.screen.dependency;

import com.almis.awe.builder.screen.base.AbstractActionBuilder;
import com.almis.awe.model.entities.screen.component.action.DependencyAction;

/**
 * @author dfuentes
 */
public class DependencyActionBuilder extends AbstractActionBuilder<DependencyActionBuilder, DependencyAction> {
  @Override
  public DependencyAction build() {
    return build(new DependencyAction());
  }
}
