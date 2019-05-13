package com.almis.awe.builder.screen;

import com.almis.awe.builder.screen.base.AbstractElementBuilder;
import com.almis.awe.model.entities.screen.View;
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
public class ViewBuilder extends AbstractElementBuilder<ViewBuilder, View> {

  private String name;

  @Override
  public View build() {
    return build(new View());
  }

  @Override
  public View build(View view) {
    return super.build(view)
      .setName(getName());
  }
}
