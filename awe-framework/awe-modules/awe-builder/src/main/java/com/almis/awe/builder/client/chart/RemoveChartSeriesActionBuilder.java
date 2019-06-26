package com.almis.awe.builder.client.chart;

import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.screen.component.chart.ChartSerie;

import java.util.List;

/**
 * Replace columns action builder
 *
 * @author pgarcia
 */
public class RemoveChartSeriesActionBuilder extends ChartSeriesActionBuilder<RemoveChartSeriesActionBuilder> {

  private static final String TYPE = "remove-chart-series";

  /**
   * Empty constructor
   */
  public RemoveChartSeriesActionBuilder() {
    super(TYPE);
  }

  /**
   * Constructor with target and column list
   *
   * @param target Target
   * @param series Serie list
   */
  public RemoveChartSeriesActionBuilder(String target, List<ChartSerie> series) {
    super(TYPE, target, series);
  }

  /**
   * Constructor with target and column list
   *
   * @param target Target
   * @param series Serie list
   */
  public RemoveChartSeriesActionBuilder(String target, ChartSerie... series) {
    super(TYPE, target, series);
  }

  /**
   * Constructor with address and column list
   *
   * @param address Target
   * @param series  Serie list
   */
  public RemoveChartSeriesActionBuilder(ComponentAddress address, List<ChartSerie> series) {
    super(TYPE, address, series);
  }

  /**
   * Constructor with address and column list
   *
   * @param address Target
   * @param series  Serie list
   */
  public RemoveChartSeriesActionBuilder(ComponentAddress address, ChartSerie... series) {
    super(TYPE, address, series);
  }
}
