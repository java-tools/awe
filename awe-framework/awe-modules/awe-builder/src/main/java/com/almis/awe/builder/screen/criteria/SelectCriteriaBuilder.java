package com.almis.awe.builder.screen.criteria;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.base.AbstractCriteriaBuilder;
import com.almis.awe.builder.screen.component.SelectAttributes;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class SelectCriteriaBuilder extends AbstractCriteriaBuilder<SelectCriteriaBuilder, AbstractCriteria> {

  private SelectAttributes selectAttributes;

  public SelectCriteriaBuilder() {
    super();
    this.selectAttributes = new SelectAttributes(this);
  }

  @Override
  public AbstractCriteria build() {
    AbstractCriteria criterion = getSelectAttributes().asSelect(new Criteria());

    return (AbstractCriteria) super.build(criterion)
      .setComponentType(Component.SELECT.toString());
  }

  /**
   * Set optional
   *
   * @param optional optional
   * @return This
   */
  public SelectCriteriaBuilder setGroup(boolean optional) {
    getSelectAttributes().setOptional(optional);
    return this;
  }
}
