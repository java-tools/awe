package com.almis.awe.builder.screen.base;

import com.almis.awe.builder.enumerates.ButtonType;
import com.almis.awe.builder.screen.button.ButtonActionBuilder;
import com.almis.awe.builder.screen.component.ButtonAttributes;
import com.almis.awe.model.entities.screen.component.button.AbstractButton;
import lombok.Getter;

/**
 * @author dfuentes
 */
@Getter
public abstract class AbstractButtonBuilder<T extends AbstractButtonBuilder, I extends AbstractButton> extends AbstractComponentBuilder<T, I> {

  private ButtonAttributes buttonAttributes;

  public AbstractButtonBuilder() {
    super();
    this.buttonAttributes = new ButtonAttributes(this);
  }

  @Override
  public I build(I button) {
    button = super.build(button);
    getButtonAttributes().asButton(button);
    return button;
  }

  /**
   * Add button action
   *
   * @param buttonActionBuilder
   * @return
   */
  public T addButtonAction(ButtonActionBuilder... buttonActionBuilder) {
    addAllElements(buttonActionBuilder);
    return (T) this;
  }

  /**
   * Set button type
   * @param buttonType button type
   * @return Builder
   */
  public T setButtonType(ButtonType buttonType) {
    getButtonAttributes().setButtonType(buttonType);
    return (T) this;
  }

  /**
   * Set button value
   * @param value button value
   * @return Builder
   */
  public T setValue(String value) {
    getButtonAttributes().setValue(value);
    return (T) this;
  }
}
