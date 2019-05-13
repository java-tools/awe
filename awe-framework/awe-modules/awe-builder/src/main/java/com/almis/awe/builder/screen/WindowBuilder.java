package com.almis.awe.builder.screen;

import com.almis.awe.builder.screen.base.AbstractTagBuilder;
import com.almis.awe.model.entities.screen.component.Window;
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
public class WindowBuilder extends AbstractTagBuilder<WindowBuilder, Window> {

  private boolean maximize;
  private String icon;

  @Override
  public Window build() {
    return build(new Window());
  }

  @Override
  public Window build(Window window) {
    return (Window) super.build(window)
      .setMaximize(isMaximize())
      .setIcon(getIcon());
  }
}
