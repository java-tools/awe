package com.almis.awe.builder.screen.criteria;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.base.AbstractCriteriaBuilder;
import com.almis.awe.builder.screen.component.TextareaAttributes;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author pgarcia
 */
@Getter(AccessLevel.PRIVATE)
public class TextareaCriteriaBuilder extends AbstractCriteriaBuilder<TextareaCriteriaBuilder, AbstractCriteria> {

  private TextareaAttributes textareaAttributes;

  public TextareaCriteriaBuilder() {
    super();
    this.textareaAttributes = new TextareaAttributes(this);
  }

  @Override
  public AbstractCriteria build() {
    AbstractCriteria criterion = getTextareaAttributes().asTextarea(new Criteria());

    return (AbstractCriteria) super.build(criterion)
      .setComponentType(Component.TEXTAREA.toString());
  }

  /**
   * Set number of rows to show
   *
   * @param rows Rows to show
   * @return Builder
   */
  public TextareaCriteriaBuilder setAreaRows(Integer rows) {
    getTextareaAttributes().setAreaRows(rows);
    return this;
  }
}
