package com.almis.awe.builder.screen.criteria;

import com.almis.awe.builder.screen.base.AbstractCriteriaBuilder;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author dfuentes
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class CriteriaBuilder extends AbstractCriteriaBuilder<CriteriaBuilder, Criteria> {
  @Override
  public Criteria build() {
    return build(new Criteria());
  }
}
