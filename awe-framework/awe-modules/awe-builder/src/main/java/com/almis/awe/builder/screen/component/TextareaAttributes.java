package com.almis.awe.builder.screen.component;

import com.almis.awe.builder.screen.base.AbstractAttributes;
import com.almis.awe.builder.screen.base.AbstractCriteriaBuilder;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TextareaAttributes<B extends AbstractCriteriaBuilder> extends AbstractAttributes<B> {
  private Integer areaRows;

  public TextareaAttributes(B builder) {
    super(builder);
  }

  /**
   * Build attributes in criterion
   *
   * @param element Criterion
   * @param <E>
   * @return Element with attributes
   */
  public <E extends AbstractCriteria> E asTextarea(E element) {
    return (E) element
      .setAreaRows(getAreaRows());
  }
}
