package com.almis.awe.builder.screen.chart;

import com.almis.awe.builder.screen.base.AbstractChartBuilder;
import com.almis.awe.model.entities.screen.component.chart.ChartSerie;
import com.almis.awe.model.type.ChartType;
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
public class ChartSerieBuilder extends AbstractChartBuilder<ChartSerieBuilder, ChartSerie> {

  private ChartType type;
  private String xValue;
  private String yValue;
  private String zValue;
  private String color;
  private String label;
  private String xAxis;
  private String yAxis;
  private String drillDownSerie;
  private boolean drillDown;

  @Override
  public ChartSerie build() {
    return build(new ChartSerie());
  }

  @Override
  public ChartSerie build(ChartSerie chartSerie) {
    super.build(chartSerie)
      .setXValue(getXValue())
      .setYValue(getYValue())
      .setZValue(getZValue())
      .setColor(getColor())
      .setXAxis(getXAxis())
      .setYAxis(getYAxis())
      .setDrillDown(isDrillDown())
      .setDrillDownSerie(getDrillDownSerie())
      .setLabel(getLabel());

    if (getType() != null) {
      chartSerie.setType(getType().toString());
    }

    return chartSerie;
  }
}
