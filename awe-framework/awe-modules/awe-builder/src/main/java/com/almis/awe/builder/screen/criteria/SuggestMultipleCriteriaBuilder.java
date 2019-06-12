package com.almis.awe.builder.screen.criteria;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.base.AbstractCriteriaBuilder;
import com.almis.awe.builder.screen.component.SuggestAttributes;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class SuggestMultipleCriteriaBuilder extends AbstractCriteriaBuilder<SuggestMultipleCriteriaBuilder, AbstractCriteria> {

  private SuggestAttributes suggestAttributes;

  public SuggestMultipleCriteriaBuilder() {
    super();
    this.suggestAttributes = new SuggestAttributes(this);
  }

  @Override
  public AbstractCriteria build() {
    AbstractCriteria criterion = getSuggestAttributes().asSuggest(new Criteria());

    return (AbstractCriteria) super.build(criterion)
      .setComponentType(Component.SUGGEST_MULTIPLE.toString());
  }

  /**
   * Set check target
   *
   * @param target target
   * @return This
   */
  public SuggestMultipleCriteriaBuilder setCheckTarget(String target) {
    getSuggestAttributes().setCheckTarget(target);
    return this;
  }

  /**
   * Set strict
   *
   * @param strict strict
   * @return This
   */
  public SuggestMultipleCriteriaBuilder setStrict(boolean strict) {
    getSuggestAttributes().setStrict(strict);
    return this;
  }

  /**
   * Set timeout
   *
   * @param timeout timeout
   * @return This
   */
  public SuggestMultipleCriteriaBuilder setTimeout(Integer timeout) {
    getSuggestAttributes().setTimeout(timeout);
    return this;
  }
}
