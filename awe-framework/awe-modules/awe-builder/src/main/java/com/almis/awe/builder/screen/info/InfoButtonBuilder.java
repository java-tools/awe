package com.almis.awe.builder.screen.info;

import com.almis.awe.builder.screen.base.AbstractButtonBuilder;
import com.almis.awe.model.entities.screen.component.button.InfoButton;
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
public class InfoButtonBuilder extends AbstractButtonBuilder<InfoButtonBuilder, InfoButton> {

  private String infoStyle;
  private String title;
  private String type;
  private String unit;

  @Override
  public InfoButton build() {
    return build(new InfoButton());
  }

  @Override
  public InfoButton build(InfoButton button) {
    return (InfoButton) super.build(button)
      .setInfoStyle(getInfoStyle())
      .setUnit(getUnit())
      .setTitle(getTitle())
      .setType(getType());
  }
}
