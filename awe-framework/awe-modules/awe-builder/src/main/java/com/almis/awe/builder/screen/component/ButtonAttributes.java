package com.almis.awe.builder.screen.component;

import com.almis.awe.builder.enumerates.ButtonType;
import com.almis.awe.builder.screen.base.AbstractAttributes;
import com.almis.awe.builder.screen.base.AbstractButtonBuilder;
import com.almis.awe.model.entities.screen.component.button.AbstractButton;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter(AccessLevel.PRIVATE)
@Setter
@Accessors(chain = true)
public class ButtonAttributes<B extends AbstractButtonBuilder> extends AbstractAttributes<B> {
  private ButtonType buttonType;
  private String value;

  public ButtonAttributes(B builder) {
    super(builder);
  }

  /**
   * Build attributes in criterion
   *
   * @param element Criterion
   * @param <E>
   * @return Element with attributes
   */
  public <E extends AbstractButton> E asButton(E element) {
    E button = (E) element
      .setValue(getValue());

    if (getButtonType() != null) {
      button.setButtonType(getButtonType().toString());
    }

    return button;
  }

  /**
   * Retrieve builder
   *
   * @return Builder
   */
  @Override
  public B builder() {
    return parent;
  }
}
