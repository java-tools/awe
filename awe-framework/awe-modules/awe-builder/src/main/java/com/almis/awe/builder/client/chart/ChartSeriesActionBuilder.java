package com.almis.awe.builder.client.chart;

import com.almis.awe.builder.client.ClientActionBuilder;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.screen.component.chart.ChartSerie;

import java.util.List;

/**
 * Replace columns action builder
 *
 * @author pgarcia
 */
public abstract class ChartSeriesActionBuilder<T> extends ClientActionBuilder<T> {

  /**
   * Empty constructor
   * @param type Type
   */
  public ChartSeriesActionBuilder(String type) {
    setType(type);
  }

  /**
   * Constructor with target and column list
   *
   * @param type Type
   * @param target Target
   * @param series Serie list
   */
  public ChartSeriesActionBuilder(String type, String target, List<ChartSerie> series) {
    setType(type);
    setTarget(target);
    addParameter("series", series);
  }

  /**
   * Constructor with address and column list
   *
   * @param type Type
   * @param address Target
   * @param series  Serie list
   */
  public ChartSeriesActionBuilder(String type, ComponentAddress address, List<ChartSerie> series) {
    setType(type);
    setAddress(address);
    addParameter("series", series);
  }
}
