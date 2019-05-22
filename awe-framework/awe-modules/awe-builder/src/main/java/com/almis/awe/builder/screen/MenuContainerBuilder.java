package com.almis.awe.builder.screen;

import com.almis.awe.builder.enumerates.MenuType;
import com.almis.awe.builder.screen.base.AweBuilder;
import com.almis.awe.model.entities.screen.component.MenuContainer;
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
public class MenuContainerBuilder extends AweBuilder<MenuContainerBuilder, MenuContainer> {

  private String style;
  private MenuType type;

  @Override
  public MenuContainer build() {
    return build(new MenuContainer());
  }

  @Override
  public MenuContainer build(MenuContainer menuContainer) {
    super.build(menuContainer)
      .setStyle(getStyle());

    if (getType() != null) {
      menuContainer.setType(getType().toString());
    }

    return menuContainer;
  }
}
