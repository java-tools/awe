package com.almis.awe.builder.screen.component;

import com.almis.awe.builder.screen.TagBuilder;
import com.almis.awe.builder.screen.base.AbstractAttributes;
import com.almis.awe.model.entities.screen.Tag;
import lombok.Getter;

@Getter
public class TagAttributes<B extends TagBuilder> extends AbstractAttributes<B> {
  private String text;
  private String source;

  public TagAttributes(B builder) {
    super(builder);
  }

  /**
   * Build attributes in element
   * @param element Element
   * @param <E>
   * @return Element with attributes
   */
  public <E extends Tag> E asTag(E element) {
    element
      .setValue(getText())
      .setSource(getSource());

    return element;
  }

  /**
   * Set text
   * @param text
   * @return
   */
  public B setText(String text) {
    this.text = text;
    return parent;
  }

  /**
   * Set source
   * @param source
   * @return
   */
  public B setSource(String source) {
    this.source = source;
    return parent;
  }
}
