package com.almis.awe.builder.screen.info;

import com.almis.awe.builder.screen.base.AbstractCriteriaBuilder;
import com.almis.awe.model.entities.screen.component.criteria.InfoCriteria;
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
public class InfoCriteriaBuilder extends AbstractCriteriaBuilder<InfoCriteriaBuilder, InfoCriteria> {

  private String infoStyle;
  private String title;

  @Override
  public InfoCriteria build() {
    return build(new InfoCriteria());
  }

  @Override
  public InfoCriteria build(InfoCriteria criterion) {
    return (InfoCriteria) super.build(criterion)
      .setInfoStyle(getInfoStyle())
      .setTitle(getTitle());
  }
}
