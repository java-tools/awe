package com.almis.awe.builder.screen.chart;

import com.almis.awe.builder.enumerates.Align;
import com.almis.awe.builder.enumerates.ChartLayout;
import com.almis.awe.builder.screen.base.AbstractChartBuilder;
import com.almis.awe.model.entities.screen.component.chart.ChartLegend;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author dfuentes
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ChartLegendBuilder extends AbstractChartBuilder<ChartLegendBuilder, ChartLegend> {

  private Align align;
  private ChartLayout chartLayout;
  private String label;
  private Integer borderWidth;
  private boolean enabled;
  private boolean floating;

  @Override
  public ChartLegend build() {
    return build(new ChartLegend());
  }

  @Override
  public ChartLegend build(ChartLegend chartLegend) {
    super.build(chartLegend)
      .setFloating(isFloating())
      .setEnabled(isEnabled())
      .setLabel(getLabel());

    if (getAlign() != null) {
      chartLegend.setAlign(getAlign().toString());
    }

    if (getChartLayout() != null) {
      chartLegend.setLayout(getChartLayout().toString());
    }

    if (getBorderWidth() != null) {
      chartLegend.setBorderWidth(getBorderWidth());
    }

    return chartLegend;
  }
}
