
package com.almis.awe.builder.screen.tab;

import com.almis.awe.builder.screen.base.AbstractTagBuilder;
import com.almis.awe.model.entities.screen.component.container.TabContainer;
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
public class TabContainerBuilder extends AbstractTagBuilder<TabContainerBuilder, TabContainer> {
  @Override
  public TabContainer build() {
    return build(new TabContainer());
  }
}
