package com.almis.awe.builder.screen.base;

import com.almis.awe.model.entities.screen.component.chart.AbstractChart;
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
public abstract class AbstractChartBuilder<T extends AbstractChartBuilder, I extends AbstractChart> extends AweBuilder<T, I> {

  /**
   * Add chart parameter
   *
   * @param chartParameter
   * @return
   */
  public T addChartParameter(T... chartParameter) {
    addAllElements(chartParameter);
    return (T) this;
  }
}
