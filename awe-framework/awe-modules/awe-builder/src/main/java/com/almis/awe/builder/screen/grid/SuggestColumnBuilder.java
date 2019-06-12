package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.base.AbstractColumnBuilder;
import com.almis.awe.builder.screen.component.SuggestAttributes;
import com.almis.awe.model.entities.screen.component.grid.Column;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class SuggestColumnBuilder extends AbstractColumnBuilder<SuggestColumnBuilder, Column> {

  private SuggestAttributes suggestAttributes;

  public SuggestColumnBuilder() {
    super();
    this.suggestAttributes = new SuggestAttributes(this);
  }

  @Override
  public Column build() {
    Column column = (Column) getSuggestAttributes().asSuggest(new Column());

    return (Column) super.build(column)
      .setComponentType(Component.SUGGEST.toString());
  }

  /**
   * Set check target
   *
   * @param target target
   * @return This
   */
  public SuggestColumnBuilder setCheckTarget(String target) {
    getSuggestAttributes().setCheckTarget(target);
    return this;
  }

  /**
   * Set strict
   *
   * @param strict strict
   * @return This
   */
  public SuggestColumnBuilder setStrict(boolean strict) {
    getSuggestAttributes().setStrict(strict);
    return this;
  }

  /**
   * Set timeout
   *
   * @param timeout timeout
   * @return This
   */
  public SuggestColumnBuilder setTimeout(Integer timeout) {
    getSuggestAttributes().setTimeout(timeout);
    return this;
  }
}
