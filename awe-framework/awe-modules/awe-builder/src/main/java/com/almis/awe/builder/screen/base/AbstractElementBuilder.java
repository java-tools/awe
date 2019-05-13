package com.almis.awe.builder.screen.base;

import com.almis.awe.model.entities.Element;
import lombok.Getter;

/**
 * @author dfuentes
 */
@Getter
public abstract class AbstractElementBuilder<T, I extends Element> extends AweBuilder<T, I> {

  private String label;
  private String title;
  private String style;
  private String type;
  private String help;
  private String helpImage;

  @Override
  public I build(I element) {
    super.build(element)
      .setLabel(getLabel())
      .setTitle(getTitle())
      .setStyle(getStyle())
      .setType(getType())
      .setHelp(getHelp())
      .setHelpImage(getHelpImage());

    return element;
  }

  /**
   * Set the label
   * @param label Label
   * @return This
   */
  public T setLabel(String label) {
    this.label = label;
    return (T) this;
  }

  /**
   * Set the title
   * @param title title
   * @return This
   */
  public T setTitle(String title) {
    this.title = title;
    return (T) this;
  }

  /**
   * Set the style
   * @param style style
   * @return This
   */
  public T setStyle(String style) {
    this.style = style;
    return (T) this;
  }

  /**
   * Set the type
   * @param type Type
   * @return This
   */
  public T setType(String type) {
    this.type = type;
    return (T) this;
  }

  /**
   * Set the help label
   * @param help  Help label
   * @return This
   */
  public T setHelp(String help) {
    this.help = help;
    return (T) this;
  }

  /**
   * Set the helpImage
   * @param helpImage helpImage
   * @return This
   */
  public T setHelpImage(String helpImage) {
    this.helpImage = helpImage;
    return (T) this;
  }
}
