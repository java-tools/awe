package com.almis.awe.builder.screen.chart;

import com.almis.awe.builder.enumerates.ChartAxis;
import com.almis.awe.builder.screen.base.AbstractChartBuilder;
import com.almis.awe.model.entities.screen.component.chart.ChartTooltip;
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
public class ChartTooltipBuilder extends AbstractChartBuilder<ChartTooltipBuilder, ChartTooltip> {

  private ChartAxis crosshairs;
  private String dateFormat;
  private String pointFormat;
  private String prefix;
  private String suffix;
  private boolean enabled;
  private boolean shared;
  private Integer numberDecimals;

  @Override
  public ChartTooltip build() {
    return build(new ChartTooltip());
  }

  @Override
  public ChartTooltip build(ChartTooltip chartTooltip) {
    super.build(chartTooltip)
      .setDateFormat(getDateFormat())
      .setPointFormat(getPointFormat())
      .setPrefix(getPrefix())
      .setSuffix(getSuffix())
      .setEnabled(isEnabled())
      .setShared(isShared())
      .setNumberDecimals(getNumberDecimals());

    if (getCrosshairs() != null) {
      chartTooltip.setCrosshairs(getCrosshairs().toString());
    }

    return chartTooltip;
  }
}
