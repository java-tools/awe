package com.almis.awe.builder.screen.base;

import com.almis.awe.builder.enumerates.Expandible;
import com.almis.awe.builder.screen.component.ElementAttributes;
import com.almis.awe.model.entities.Element;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author dfuentes
 */
@Getter(AccessLevel.PRIVATE)
@Accessors(chain = true)
public abstract class AbstractElementBuilder<T, I extends Element> extends AweBuilder<T, I> {

  private ElementAttributes attributes;

  public AbstractElementBuilder() {
    this.attributes = new ElementAttributes(this);
  }

  @Override
  public I build(I tag) {
    tag = super.build(tag);
    getAttributes().asElement(tag);

    return tag;
  }

  /**
   * Set label
   *
   * @param label Label
   * @return Builder
   */
  public T setLabel(String label) {
    getAttributes().setLabel(label);
    return (T) this;
  }

  /**
   * Set title
   *
   * @param title Title
   * @return Builder
   */
  public T setTitle(String title) {
    getAttributes().setTitle(title);
    return (T) this;
  }

  /**
   * Set style (classes)
   *
   * @param style Style
   * @return Builder
   */
  public T setStyle(String style) {
    getAttributes().setStyle(style);
    return (T) this;
  }

  /**
   * Set type
   *
   * @param type Type
   * @return Builder
   */
  public T setType(String type) {
    getAttributes().setType(type);
    return (T) this;
  }

  /**
   * Set help label
   *
   * @param help Help label
   * @return Builder
   */
  public T setHelp(String help) {
    getAttributes().setHelp(help);
    return (T) this;
  }

  /**
   * Set help image
   *
   * @param helpImage Help image
   * @return Builder
   */
  public T setHelpImage(String helpImage) {
    getAttributes().setHelpImage(helpImage);
    return (T) this;
  }

  /**
   * Set expandible
   *
   * @param expandible Expandible
   * @return Builder
   */
  public T setExpandible(Expandible expandible) {
    getAttributes().setExpandible(expandible);
    return (T) this;
  }
}
