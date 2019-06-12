package com.almis.awe.builder.screen.criteria;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.base.AbstractComponentBuilder;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class TextViewCriteriaBuilder extends AbstractComponentBuilder<TextViewCriteriaBuilder, AbstractCriteria> {
  @Override
  public AbstractCriteria build() {
    return (AbstractCriteria) super.build(new Criteria())
      .setComponentType(Component.TEXT_VIEW.toString());
  }
}
