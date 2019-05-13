package com.almis.awe.builder.screen;

import com.almis.awe.builder.screen.base.AbstractTagBuilder;
import com.almis.awe.model.entities.screen.component.Resizable;
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
public class ResizableBuilder extends AbstractTagBuilder<ResizableBuilder, Resizable> {

  private String directions;

  @Override
  public Resizable build() {
    return build(new Resizable());
  }

  @Override
  public Resizable build(Resizable resizable) {
    return super.build(resizable)
      .setDirections(getDirections());
  }
}
