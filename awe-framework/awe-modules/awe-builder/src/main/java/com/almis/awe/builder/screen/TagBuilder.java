package com.almis.awe.builder.screen;

import com.almis.awe.builder.screen.base.AbstractTagBuilder;
import com.almis.awe.model.entities.screen.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author dfuentes
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class TagBuilder extends AbstractTagBuilder<TagBuilder, Tag> {

  private String text;
  private String source;

  @Override
  public Tag build() {
    return build(new Tag());
  }

  @Override
  public Tag build(Tag tag) {
    return super.build(tag)
      .setSource(getSource())
      .setValue(getText());
  }
}
