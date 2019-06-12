package com.almis.awe.builder.screen;

import com.almis.awe.builder.client.ScreenActionBuilder;
import com.almis.awe.builder.screen.base.AbstractElementBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.menu.Option;
import com.almis.awe.model.entities.screen.Screen;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author dfuentes
 */
@Getter
@Setter
@Accessors(chain = true)
public class ScreenBuilder extends AbstractElementBuilder<ScreenBuilder, Screen> {

  private boolean keepCriteria;
  private String template;
  private String onLoad;
  private String onUnload;
  private String target;
  private String menuType = AweConstants.PRIVATE_MENU;

  /**
   * Specific build that returns a service data with a screen client action.
   *
   * @return Client action with new screen generated
   * @throws AWException
   */
  public ServiceData buildClientAction(AweElements aweElements) throws AWException {
    // Generate screen
    Screen screen = build();

    // Override screen in cache
    aweElements.setScreen(screen);

    // Modify menu options with the new screen
    Menu menu = generateOptionMenu(screen, aweElements);

    // Add screen to menu
    aweElements.setMenu(getMenuType(), menu);

    // Generate client action to retrieve screen
    ClientAction clientAction = new ScreenActionBuilder(screen.getId()).build();

    return new ServiceData()
      .addClientAction(clientAction);
  }

  /**
   * Specific build that returns a service data with a screen client action.
   *
   * @return Generated screen
   */
  public Screen build() {
    return build(new Screen());
  }

  /**
   * Generate option menu
   *
   * @param screen      Screen
   * @param aweElements Awe Elements
   * @return
   */
  private Menu generateOptionMenu(Screen screen, AweElements aweElements) throws AWException {
    // Generate option
    Option option = Option.builder()
      .screen(screen.getId())
      .invisible(true)
      .name(screen.getId())
      .build();

    // Add option and return menu
    return aweElements.getMenu(getMenuType()).copy().addElement(option);
  }

  @Override
  public Screen build(Screen screen) {
    super.build(screen)
      .setKeepCriteria(isKeepCriteria())
      .setTemplate(getTemplate())
      .setOnLoad(getOnLoad())
      .setOnUnload(getOnUnload())
      .setTarget(getTarget());

    return screen;
  }

  /**
   * Add tag
   *
   * @param tag
   * @return
   */
  public ScreenBuilder addTag(TagBuilder... tag) {
    addAllElements(tag);
    return this;
  }

  /**
   * Add message
   *
   * @param message
   * @return
   */
  public ScreenBuilder addMessage(MessageBuilder... message) {
    addAllElements(message);
    return this;
  }
}
