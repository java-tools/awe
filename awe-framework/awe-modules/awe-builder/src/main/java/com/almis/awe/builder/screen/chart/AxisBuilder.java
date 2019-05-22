package com.almis.awe.builder.screen.chart;

import com.almis.awe.builder.enumerates.AxisDataType;
import com.almis.awe.builder.enumerates.FormatterFunction;
import com.almis.awe.builder.screen.base.AbstractChartBuilder;
import com.almis.awe.model.entities.screen.component.chart.ChartAxis;
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
public class AxisBuilder extends AbstractChartBuilder<AxisBuilder, ChartAxis> {

  private boolean allowDecimal;
  private boolean opposite;
  private String label;
  private String labelFormat;
  private String tickInterval;
  private Float labelRotation;
  private FormatterFunction formatterFunction;
  private AxisDataType type;

  @Override
  public ChartAxis build() {
    return build(new ChartAxis());
  }

  @Override
  public ChartAxis build(ChartAxis axis) {
    super.build(axis)
      .setAllowDecimal(isAllowDecimal())
      .setLabelFormat(getLabelFormat())
      .setTickInterval(getTickInterval())
      .setLabelRotation(getLabelRotation())
      .setOpposite(isOpposite())
      .setLabel(getLabel());

    if (getFormatterFunction() != null) {
      axis.setFormatterFunction(getFormatterFunction().toString());
    }

    if (getType() != null) {
      axis.setType(getType().toString());
    }

    return axis;
  }
}
