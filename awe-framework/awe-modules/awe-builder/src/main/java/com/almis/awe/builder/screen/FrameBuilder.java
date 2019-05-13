package com.almis.awe.builder.screen;

import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.builder.screen.base.AweBuilder;
import com.almis.awe.model.entities.screen.component.Frame;
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
public class FrameBuilder extends AweBuilder<FrameBuilder, Frame> {

  private ServerAction serverAction;
  private String name;
  private String screen;
  private String screenVariable;
  private Boolean scroll;

  @Override
  public Frame build() {
    return build(new Frame());
  }

  @Override
  public Frame build(Frame frame) {
    super.build(frame)
      .setScreen(getScreen())
      .setScreenVariable(getScreenVariable())
      .setScroll(getScroll())
      .setName(getName());

    if (getServerAction() != null) {
      frame.setServerAction(getServerAction().toString());
    }

    return frame;
  }
}
