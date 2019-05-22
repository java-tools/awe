package com.almis.awe.builder.screen.base;

import com.almis.awe.builder.enumerates.ButtonType;
import com.almis.awe.builder.screen.button.ButtonActionBuilder;
import com.almis.awe.model.entities.screen.component.button.AbstractButton;
import lombok.Getter;

/**
 *
 * @author dfuentes
 */
@Getter
public abstract class AbstractButtonBuilder<T, I extends AbstractButton> extends AbstractComponentBuilder<T, I> {

  private ButtonType buttonType;
  private String value;

  @Override
  public I build(I button) {
    super.build(button)
      .setValue(getValue());

    if (getButtonType() != null) {
      button.setType(getButtonType().toString());
    }

    return button;
  }

  /**
   * Set the button type
   * @param buttonType Type
   * @return This
   */
  public T setButtonType(ButtonType buttonType) {
    this.buttonType = buttonType;
    return (T) this;
  }

  /**
   * Set the button value
   * @param value Value
   * @return This
   */
  public T setValue(String value) {
    this.value = value;
    return (T) this;
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
}
