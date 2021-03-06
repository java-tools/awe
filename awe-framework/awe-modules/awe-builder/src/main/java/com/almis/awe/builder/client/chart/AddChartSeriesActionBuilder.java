package com.almis.awe.builder.client.chart;

import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.screen.component.chart.ChartSerie;

import java.util.List;

/**
 * Replace columns action builder
 *
 * @author pgarcia
 */
public class AddChartSeriesActionBuilder extends ChartSeriesActionBuilder<AddChartSeriesActionBuilder> {

  private static final String TYPE = "add-chart-series";

  /**
   * Empty constructor
   */
  public AddChartSeriesActionBuilder() {
    super(TYPE);
  }

  /**
   * Constructor with target and chart serie list
   *
   * @param target Target
   * @param series Serie list
   */
  public AddChartSeriesActionBuilder(String target, List<ChartSerie> series) {
    super(TYPE, target, series);
  }

  /**
   * Constructor with target and chart serie array
   *
   * @param target Target
   * @param series Serie list
   */
  public AddChartSeriesActionBuilder(String target, ChartSerie... series) {
    super(TYPE, target, series);
  }

  /**
   * Constructor with address and chart serie list
   *
   * @param address Target
   * @param series  Serie list
   */
  public AddChartSeriesActionBuilder(ComponentAddress address, List<ChartSerie> series) {
    super(TYPE, address, series);
  }

  /**
   * Constructor with address and chart serie array
   *
   * @param address Target
   * @param series  Serie list
   */
  public AddChartSeriesActionBuilder(ComponentAddress address, ChartSerie... series) {
    super(TYPE, address, series);
  }
}
