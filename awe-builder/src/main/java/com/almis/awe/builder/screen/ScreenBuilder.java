package com.almis.awe.builder.screen;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.menu.Option;
import com.almis.awe.model.entities.screen.Screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dfuentes
 */
public class ScreenBuilder extends AweBuilder<ScreenBuilder> {

  private AweElements aweElements;

  private List<AweBuilder> elements;
  private Boolean keepCriteria;
  private String help;
  private String helpImage;
  private String label;
  private String template;
  private String onLoad;
  private String onUnload;
  private String target;
  private String menuType = AweConstants.PRIVATE_MENU;

  /**
   * Screen builder constructor
   *
   * @throws AWException
   */
  public ScreenBuilder(AweElements aweElements) throws AWException {
    super();
    this.aweElements = aweElements;
  }

  @Override
  public void initializeElements() {
    this.elements = new ArrayList<>();
  }

  @Override
  public ScreenBuilder setParent() {
    return this;
  }

  /**
   * Specific build that returns a service data with a screen client action.
   *
   * @return
   *
   * @throws AWException
   */
  public ServiceData build() throws AWException {
    ServiceData serviceData = new ServiceData();

    //Generate screen
    Screen screen = (Screen) build(new Screen());

    //Override screen in cache
    aweElements.setScreen(screen);

    //Modify menu options with the new screen
    Menu menu = generateOptionMenu(screen);

    //Override menu in cache
    aweElements.setMenu(getMenuType(), menu);

    // Override menu in cache
    ClientAction clientAction = new ClientAction("screen");

    clientAction.setTarget(screen.getId());

    serviceData.addClientAction(clientAction);

    return serviceData;
  }

  /**
   * Generate option menu
   *
   * @param screen
   *
   * @return
   */
  private Menu generateOptionMenu(Screen screen) throws AWException {
    // Generate option
    Option option = new Option()
      .setScreen(screen.getId())
      .setInvisible(true)
      .setName(screen.getId());

    // Add option and return menu
    return new Menu(aweElements.getMenu(getMenuType()).addElement(option));
  }

  @Override
  public Element build(Element element) {
    Screen screen = (Screen) element;

    screen.setId(getId());

    if (isKeepCriteria() != null) {
      screen.setKeepCriteria(String.valueOf(isKeepCriteria()));
    }
    if (getHelp() != null) {
      screen.setHelp(getHelp());
    }
    if (getHelpImage() != null) {
      screen.setHelpImage(getHelpImage());
    }
    if (getLabel() != null) {
      screen.setLabel(getLabel());
    }
    if (getTemplate() != null) {
      screen.setTemplate(getTemplate());
    }
    if (getOnLoad() != null) {
      screen.setOnLoad(getOnLoad());
    }
    if (getOnUnload() != null) {
      screen.setOnUnload(getOnUnload());
    }
    if (getTarget() != null) {
      screen.setTarget(getTarget());
    }

    for (AweBuilder aweBuilder : elements) {
      addElement(screen, aweBuilder.build(screen));
    }

    return screen;
  }

  /**
   * Is keep criteria
   *
   * @return
   */
  public Boolean isKeepCriteria() {
    return keepCriteria;
  }

  /**
   * Set keep criteria
   *
   * @param keepCriteria
   *
   * @return
   */
  public ScreenBuilder setKeepCriteria(Boolean keepCriteria) {
    this.keepCriteria = keepCriteria;
    return this;
  }

  /**
   * Get help
   *
   * @return
   */
  public String getHelp() {
    return help;
  }

  /**
   * Set help
   *
   * @param help
   *
   * @return
   */
  public ScreenBuilder setHelp(String help) {
    this.help = help;
    return this;
  }

  /**
   * Get help image
   *
   * @return
   */
  public String getHelpImage() {
    return helpImage;
  }

  /**
   * Set help image
   *
   * @param helpImage
   *
   * @return
   */
  public ScreenBuilder setHelpImage(String helpImage) {
    this.helpImage = helpImage;
    return this;
  }

  /**
   * Get label
   *
   * @return
   */
  public String getLabel() {
    return label;
  }

  /**
   * Set label
   *
   * @param label
   *
   * @return
   */
  public ScreenBuilder setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * Get template
   *
   * @return
   */
  public String getTemplate() {
    return template;
  }

  /**
   * Set template
   *
   * @param template
   *
   * @return
   */
  public ScreenBuilder setTemplate(String template) {
    this.template = template;
    return this;
  }

  /**
   * Get onload
   *
   * @return
   */
  public String getOnLoad() {
    return onLoad;
  }

  /**
   * Set onLoad
   *
   * @param onLoad
   *
   * @return
   */
  public ScreenBuilder setOnLoad(String onLoad) {
    this.onLoad = onLoad;
    return this;
  }

  /**
   * Get onUnload
   *
   * @return
   */
  public String getOnUnload() {
    return onUnload;
  }

  /**
   * Set onUnload
   *
   * @param onUnload
   *
   * @return
   */
  public ScreenBuilder setOnUnload(String onUnload) {
    this.onUnload = onUnload;
    return this;
  }

  /**
   * Get target
   *
   * @return
   */
  public String getTarget() {
    return target;
  }

  /**
   * Set target
   *
   * @param target
   *
   * @return
   */
  public ScreenBuilder setTarget(String target) {
    this.target = target;
    return this;
  }

  /**
   * Add tag
   *
   * @param tag
   *
   * @return
   */
  public ScreenBuilder addTag(TagBuilder... tag) {
    if (tag != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(tag));
    }
    return this;
  }

  /**
   * Add message
   *
   * @param message
   *
   * @return
   */
  public ScreenBuilder addMessage(MessageBuilder... message) {
    if (message != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(message));
    }
    return this;
  }

  /**
   * get element list
   *
   * @return
   */
  public List<AweBuilder> getElementList() {
    return this.elements;
  }

  /**
   * Get menu type
   * @return Menu type
   */
  public String getMenuType() {
    return menuType;
  }

  /**
   * Set menu type
   * @param menuType Menu type
   * @return this
   */
  public ScreenBuilder setMenuType(String menuType) {
    this.menuType = menuType;
    return this;
  }
}
