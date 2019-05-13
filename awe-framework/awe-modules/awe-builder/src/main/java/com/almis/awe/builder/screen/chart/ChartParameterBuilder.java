package com.almis.awe.builder.screen.chart;

import com.almis.awe.builder.enumerates.DataType;
import com.almis.awe.builder.screen.base.AbstractChartBuilder;
import com.almis.awe.model.entities.screen.component.chart.ChartParameter;
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
public class ChartParameterBuilder extends AbstractChartBuilder<ChartParameterBuilder, ChartParameter> {

  private DataType dataType;
  private String name;
  private String value;

  @Override
  public ChartParameter build() {
    return build(new ChartParameter());
  }

  @Override
  public ChartParameter build(ChartParameter chartParameter) {
    super.build(chartParameter)
      .setName(getName())
      .setValue(getValue());

    if (getDataType() != null) {
      chartParameter.setType(getDataType().toString());
    }

    return chartParameter;
  }
}
