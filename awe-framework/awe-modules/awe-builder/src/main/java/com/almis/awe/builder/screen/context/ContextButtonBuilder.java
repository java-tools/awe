package com.almis.awe.builder.screen.context;

import com.almis.awe.builder.enumerates.ButtonType;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.builder.screen.button.ButtonActionBuilder;
import com.almis.awe.builder.screen.button.ButtonBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.button.ContextButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dfuentes
 */
public class ContextButtonBuilder extends ButtonBuilder {

  private ButtonType buttonType;
  private String label;
  private String icon;
  private String size;
  private String style;
  private String value;
  private String help;
  private String helpImage;
  private List<AweBuilder> elements;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public ContextButtonBuilder() throws AWException {
    super();
  }


  @Override
  public void initializeElements() {
    elements = new ArrayList<>();
  }

  @Override
  public ButtonBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    ContextButton button = new ContextButton();

    button.setId(getId());

    if (getButtonType() != null) {
      button.setType(getButtonType().toString());
    }

    if (getLabel() != null) {
      button.setLabel(getLabel());
    }

    if (getIcon() != null) {
      button.setIcon(getIcon());
    }

    if (getSize() != null) {
      button.setSize(getSize());
    }

    if (getStyle() != null) {
      button.setStyle(getStyle());
    }

    if (getValue() != null) {
      button.setValue(getValue());
    }

    if (getHelp() != null) {
      button.setHelp(getHelp());
    }

    if (getHelpImage() != null) {
      button.setHelpImage(getHelpImage());
    }

    for (AweBuilder aweBuilder : getElementList()) {
      addElement(button, aweBuilder.build(button));
    }

    return button;
  }

  /**
   * Get button type
   *
   * @return
   */
  @Override
  public ButtonType getButtonType() {
    return buttonType;
  }

  /**
   * Set button type
   *
   * @param buttonType
   * @return
   */
  @Override
  public ContextButtonBuilder setButtonType(ButtonType buttonType) {
    this.buttonType = buttonType;
    return this;
  }

  /**
   * Get label
   *
   * @return
   */
  @Override
  public String getLabel() {
    return label;
  }

  /**
   * Set label
   *
   * @param label
   * @return
   */
  @Override
  public ContextButtonBuilder setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * Get icon
   *
   * @return
   */
  @Override
  public String getIcon() {
    return icon;
  }

  /**
   * Set icon
   *
   * @param icon
   * @return
   */
  @Override
  public ContextButtonBuilder setIcon(String icon) {
    this.icon = icon;
    return this;
  }

  /**
   * Get size
   *
   * @return
   */
  @Override
  public String getSize() {
    return size;
  }

  /**
   * Set size
   *
   * @param size
   * @return
   */
  @Override
  public ContextButtonBuilder setSize(String size) {
    this.size = size;
    return this;
  }

  /**
   * Get style
   *
   * @return
   */
  @Override
  public String getStyle() {
    return style;
  }

  /**
   * Set style
   *
   * @param style
   * @return
   */
  @Override
  public ContextButtonBuilder setStyle(String style) {
    this.style = style;
    return this;
  }

  /**
   * Get value
   *
   * @return
   */
  @Override
  public String getValue() {
    return value;
  }

  /**
   * Set value
   *
   * @param value
   * @return
   */
  @Override
  public ContextButtonBuilder setValue(String value) {
    this.value = value;
    return this;
  }

  /**
   * Get help
   *
   * @return
   */
  @Override
  public String getHelp() {
    return help;
  }

  /**
   * Set help
   *
   * @param help
   * @return
   */
  @Override
  public ContextButtonBuilder setHelp(String help) {
    this.help = help;
    return this;
  }

  /**
   * Get help image
   *
   * @return
   */
  @Override
  public String getHelpImage() {
    return helpImage;
  }

  /**
   * Set help image
   *
   * @param helpImage
   * @return
   */
  @Override
  public ContextButtonBuilder setHelpImage(String helpImage) {
    this.helpImage = helpImage;
    return this;
  }

  /**
   * Add button action
   *
   * @param buttonActionBuilder
   * @return
   */
  @Override
  public ContextButtonBuilder addButtonAction(ButtonActionBuilder... buttonActionBuilder) {
    if (buttonActionBuilder != null) {
      this.elements.addAll(Arrays.asList(buttonActionBuilder));
    }
    return this;
  }

  /**
   * Add dependency
   *
   * @param dependencyBuilder
   * @return
   */
  @Override
  public ContextButtonBuilder addDependency(DependencyBuilder... dependencyBuilder) {
    if (dependencyBuilder != null) {
      this.elements.addAll(Arrays.asList(dependencyBuilder));
    }
    return this;
  }

  /**
   * Add contextButton
   *
   * @param contextButtonBuilders
   * @return
   */
  public ContextButtonBuilder addContextButton(ContextButtonBuilder... contextButtonBuilders) {
    if (contextButtonBuilders != null) {
      this.elements.addAll(Arrays.asList(contextButtonBuilders));
    }
    return this;
  }

  /**
   * Add contextSeparator
   *
   * @param contextSeparatorBuilders
   * @return
   */
  public ContextButtonBuilder addContextSeparator(ContextSeparatorBuilder... contextSeparatorBuilders) {
    if (contextSeparatorBuilders != null) {
      this.elements.addAll(Arrays.asList(contextSeparatorBuilders));
    }
    return this;
  }

  /**
   * Get element list
   *
   * @return
   */
  @Override
  public List<AweBuilder> getElementList() {
    return this.elements;
  }
}
