package com.almis.awe.builder.screen.chart;

import com.almis.awe.builder.enumerates.*;
import com.almis.awe.builder.screen.base.AbstractComponentBuilder;
import com.almis.awe.model.entities.screen.component.chart.Chart;
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
public class ChartBuilder extends AbstractComponentBuilder<ChartBuilder, Chart> {

  private ChartType chartType;
  private ServerAction serverAction;
  private Stacking stacking;
  private InitialLoad initialLoad;
  private ChartAxis zoomType;
  private Integer max;
  private IconLoading iconLoading;
  private String formatDataLabels;
  private String help;
  private String helpImage;
  private String label;
  private String style;
  private String subtitle;
  private String targetAction;
  private String theme;
  private boolean autoload;
  private Integer autorefresh;
  private boolean enableDataLabels;
  private boolean inverted;
  private boolean stockChart;
  private boolean visible;
  private ChartLegendBuilder chartLegend;
  private ChartTooltipBuilder chartTooltip;

  @Override
  public Chart build() {
    return build(new Chart());
  }

  @Override
  public Chart build(Chart chart) {
    super.build(chart)
      .setFormatDataLabels(getFormatDataLabels())
      .setSubTitle(getSubtitle())
      .setTheme(getTheme())
      .setEnableDataLabels(isEnableDataLabels())
      .setInverted(isInverted())
      .setStockChart(isStockChart());

    if (getChartType() != null) {
      chart.setType(getChartType().toString());
    }

    if (getServerAction() != null) {
      chart.setServerAction(getServerAction().toString());
    }

    if (getStacking() != null) {
      chart.setStacking(getStacking().toString());
    }

    if (getInitialLoad() != null) {
      chart.setInitialLoad(getInitialLoad().toString());
    }

    if (getZoomType() != null) {
      chart.setZoomType(getZoomType().toString());
    }

    if (getIconLoading() != null) {
      chart.setIconLoading(getIconLoading().toString());
    }

    if (getChartLegend() != null) {
      chart.setChartLegend(getChartLegend().build());
    }

    if (getChartTooltip() != null) {
      chart.setChartTooltip(getChartTooltip().build());
    }

    return chart;
  }

  /**
   * Add chart parameter list
   *
   * @param chartSerie
   * @return
   */
  public ChartBuilder addChartParameter(ChartParameterBuilder... chartParameter) {
    addAllElements(chartParameter);
    return this;
  }

  /**
   * Add chart serie list
   *
   * @param chartSerie
   * @return
   */
  public ChartBuilder addChartSerieList(ChartSerieBuilder... chartSerie) {
    addAllElements(chartSerie);
    return this;
  }

  /**
   * Add x axis
   *
   * @param axis
   * @return
   */
  public ChartBuilder addAxis(AxisBuilder... axis) {
    addAllElements(axis);
    return this;
  }
}
