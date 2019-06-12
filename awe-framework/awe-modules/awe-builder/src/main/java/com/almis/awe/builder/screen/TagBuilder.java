package com.almis.awe.builder.screen;

import com.almis.awe.builder.screen.base.AbstractTagBuilder;
import com.almis.awe.builder.screen.component.TagAttributes;
import com.almis.awe.model.entities.screen.Tag;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author dfuentes
 */
@Getter(AccessLevel.PRIVATE)
public class TagBuilder extends AbstractTagBuilder<TagBuilder, Tag> {

  private TagAttributes tagAttributes;

  public TagBuilder() {
    this.tagAttributes = new TagAttributes(this);
  }

  @Override
  public Tag build() {
    return build(new Tag());
  }

  @Override
  public Tag build(Tag tag) {
    tag = super.build(tag);
    getTagAttributes().asTag(tag);

    return tag;
  }

  /**
   * Set source
   *
   * @param source source
   * @return Builder
   */
  public TagBuilder setSource(String source) {
    getTagAttributes().setSource(source);
    return this;
  }

  /**
   * Set text
   *
   * @param text text
   * @return Builder
   */
  public TagBuilder setText(String text) {
    getTagAttributes().setText(text);
    return this;
  }
}
