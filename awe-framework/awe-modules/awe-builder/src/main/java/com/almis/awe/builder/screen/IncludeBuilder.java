package com.almis.awe.builder.screen;

import com.almis.awe.builder.screen.base.AweBuilder;
import com.almis.awe.model.entities.screen.Include;
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
public class IncludeBuilder extends AweBuilder<IncludeBuilder, Include> {

  private String targetScreen;
  private String targetSource;

  @Override
  public Include build() {
    return build(new Include());
  }

  @Override
  public Include build(Include include) {
    return super.build(include)
      .setTargetScreen(getTargetScreen())
      .setTargetSource(getTargetSource());
  }
}
