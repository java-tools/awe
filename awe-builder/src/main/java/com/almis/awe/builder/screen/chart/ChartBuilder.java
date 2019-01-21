/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.chart;

import com.almis.awe.builder.enumerates.ChartAxis;
import com.almis.awe.builder.enumerates.IconLoading;
import com.almis.awe.builder.enumerates.InitialLoad;
import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.builder.enumerates.Stacking;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.builder.screen.context.ContextButtonBuilder;
import com.almis.awe.builder.screen.context.ContextSeparatorBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.chart.Chart;
import com.almis.awe.model.type.ChartType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class ChartBuilder extends AweBuilder<ChartBuilder> {

  private ChartType type;
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
  private Boolean autoload;
  private Boolean autorefresh;
  private Boolean enableDataLabels;
  private Boolean inverted;
  private Boolean stockChart;
  private Boolean visible;
  private List<AweBuilder> elements;
  private List<XAxisBuider> xAxisList;
  private List<YAxisBuider> yAxisList;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public ChartBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    elements = new ArrayList<>();
    xAxisList = new ArrayList<>();
    yAxisList = new ArrayList<>();
  }

  @Override
  public ChartBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Chart chart = new Chart();
    chart.setId(getId());
    chart.setFormatDataLabels(getFormatDataLabels());
    chart.setHelp(getHelp());
    chart.setHelpImage(getHelpImage());
    chart.setLabel(getLabel());
    chart.setStyle(getStyle());
    chart.setSubTitle(getSubtitle());
    chart.setTargetAction(getTargetAction());
    chart.setTheme(getTheme());
    chart.setAutoload(getValueAsString(isAutoload()));
    chart.setAutorefresh(getValueAsString(isAutorefresh()));
    chart.setEnableDataLabels(getValueAsString(isEnableDataLabels()));
    chart.setInverted(getValueAsString(isInverted()));
    chart.setStockChart(getValueAsString(isStockChart()));
    chart.setVisible(getValueAsString(isVisible()));
    chart.setMax(getValueAsString(getMax()));

    if (getType() != null) {
      chart.setType(getType().toString());
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

    for (AweBuilder aweBuilder : elements) {
      addElement(chart, aweBuilder.build(chart));
    }

    for (XAxisBuider axisBuider : xAxisList) {
      chart.getxAxisList().add((com.almis.awe.model.entities.screen.component.chart.ChartAxis) axisBuider.build(chart));
    }

    for (YAxisBuider axisBuider : yAxisList) {
      chart.getyAxisList().add((com.almis.awe.model.entities.screen.component.chart.ChartAxis) axisBuider.build(chart));
    }

    return chart;
  }

  /**
   * Get type
   *
   * @return
   */
  public ChartType getType() {
    return type;
  }

  /**
   * Set type
   *
   * @param type
   * @return
   */
  public ChartBuilder setType(ChartType type) {
    this.type = type;
    return this;
  }

  /**
   * Get server action
   *
   * @return
   */
  public ServerAction getServerAction() {
    return serverAction;
  }

  /**
   * Set server action
   *
   * @param serverAction
   * @return
   */
  public ChartBuilder setServerAction(ServerAction serverAction) {
    this.serverAction = serverAction;
    return this;
  }

  /**
   * Get stacking
   *
   * @return
   */
  public Stacking getStacking() {
    return stacking;
  }

  /**
   * Set stacking
   *
   * @param stacking
   * @return
   */
  public ChartBuilder setStacking(Stacking stacking) {
    this.stacking = stacking;
    return this;
  }

  /**
   * Get initial load
   *
   * @return
   */
  public InitialLoad getInitialLoad() {
    return initialLoad;
  }

  /**
   * Set initial load
   *
   * @param initialLoad
   * @return
   */
  public ChartBuilder setInitialLoad(InitialLoad initialLoad) {
    this.initialLoad = initialLoad;
    return this;
  }

  /**
   * Get zoom type
   *
   * @return
   */
  public ChartAxis getZoomType() {
    return zoomType;
  }

  /**
   * Set zoom type
   *
   * @param zoomType
   * @return
   */
  public ChartBuilder setZoomType(ChartAxis zoomType) {
    this.zoomType = zoomType;
    return this;
  }

  /**
   * Get max
   *
   * @return
   */
  public Integer getMax() {
    return max;
  }

  /**
   * Set max
   *
   * @param max
   * @return
   */
  public ChartBuilder setMax(Integer max) {
    this.max = max;
    return this;
  }

  /**
   * Get format data labels
   *
   * @return
   */
  public String getFormatDataLabels() {
    return formatDataLabels;
  }

  /**
   * Set format data labels
   *
   * @param formatDataLabels
   * @return
   */
  public ChartBuilder setFormatDataLabels(String formatDataLabels) {
    this.formatDataLabels = formatDataLabels;
    return this;
  }

  /**
   * Get help
   *
   * @return
   */
  public String getHelp() {
    return help;
  }

  /**
   * Set help
   *
   * @param help
   * @return
   */
  public ChartBuilder setHelp(String help) {
    this.help = help;
    return this;
  }

  /**
   * Get help image
   *
   * @return
   */
  public String getHelpImage() {
    return helpImage;
  }

  /**
   * Set help image
   *
   * @param helpImage
   * @return
   */
  public ChartBuilder setHelpImage(String helpImage) {
    this.helpImage = helpImage;
    return this;
  }

  /**
   * Get icon loading
   *
   * @return
   */
  public IconLoading getIconLoading() {
    return iconLoading;
  }

  /**
   * Set icon loading
   *
   * @param iconLoading
   * @return
   */
  public ChartBuilder setIconLoading(IconLoading iconLoading) {
    this.iconLoading = iconLoading;
    return this;
  }

  /**
   * Get label
   *
   * @return
   */
  public String getLabel() {
    return label;
  }

  /**
   * Set label
   *
   * @param label
   * @return
   */
  public ChartBuilder setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * Get style
   *
   * @return
   */
  public String getStyle() {
    return style;
  }

  /**
   * Set style
   *
   * @param style
   * @return
   */
  public ChartBuilder setStyle(String style) {
    this.style = style;
    return this;
  }

  /**
   * Get subtitle
   *
   * @return
   */
  public String getSubtitle() {
    return subtitle;
  }

  /**
   * Set subtitle
   *
   * @param subtitle
   * @return
   */
  public ChartBuilder setSubtitle(String subtitle) {
    this.subtitle = subtitle;
    return this;
  }

  /**
   * Get target action
   *
   * @return
   */
  public String getTargetAction() {
    return targetAction;
  }

  /**
   * Set target action
   *
   * @param targetAction
   * @return
   */
  public ChartBuilder setTargetAction(String targetAction) {
    this.targetAction = targetAction;
    return this;
  }

  /**
   * Get theme
   *
   * @return
   */
  public String getTheme() {
    return theme;
  }

  /**
   * Set theme
   *
   * @param theme
   * @return
   */
  public ChartBuilder setTheme(String theme) {
    this.theme = theme;
    return this;
  }

  /**
   * Is autoload
   *
   * @return
   */
  public Boolean isAutoload() {
    return autoload;
  }

  /**
   * Set autoload
   *
   * @param autoload
   * @return
   */
  public ChartBuilder setAutoload(Boolean autoload) {
    this.autoload = autoload;
    return this;
  }

  /**
   * Is autorefresh
   *
   * @return
   */
  public Boolean isAutorefresh() {
    return autorefresh;
  }

  /**
   * Set autorefresh
   *
   * @param autorefresh
   * @return
   */
  public ChartBuilder setAutorefresh(Boolean autorefresh) {
    this.autorefresh = autorefresh;
    return this;
  }

  /**
   * Is enable data labels
   *
   * @return
   */
  public Boolean isEnableDataLabels() {
    return enableDataLabels;
  }

  /**
   * Set enable data labels
   *
   * @param enableDataLabels
   * @return
   */
  public ChartBuilder setEnableDataLabels(Boolean enableDataLabels) {
    this.enableDataLabels = enableDataLabels;
    return this;
  }

  /**
   * Is inverted
   *
   * @return
   */
  public Boolean isInverted() {
    return inverted;
  }

  /**
   * Set inverted
   *
   * @param inverted
   * @return
   */
  public ChartBuilder setInverted(Boolean inverted) {
    this.inverted = inverted;
    return this;
  }

  /**
   * Is stock chart
   *
   * @return
   */
  public Boolean isStockChart() {
    return stockChart;
  }

  /**
   * Set stock chart
   *
   * @param stockChart
   * @return
   */
  public ChartBuilder setStockChart(Boolean stockChart) {
    this.stockChart = stockChart;
    return this;
  }

  /**
   * Is visible
   *
   * @return
   */
  public Boolean isVisible() {
    return visible;
  }

  /**
   * Set visible
   *
   * @param visible
   * @return
   */
  public ChartBuilder setVisible(Boolean visible) {
    this.visible = visible;
    return this;
  }

  /**
   * Add chart legend
   *
   * @param chartLegend
   * @return
   */
  public ChartBuilder addChartLegend(ChartLegendBuilder chartLegend) {
    this.elements.add(chartLegend);
    return this;
  }

  /**
   * Add chart parameter
   *
   * @param chartParameter
   * @return
   */
  public ChartBuilder addChartParameter(ChartParameterBuilder... chartParameter) {
    if (chartParameter != null) {
      this.elements.addAll(Arrays.asList(chartParameter));
    }
    return this;
  }

  /**
   * Add chart serie list
   *
   * @param chartSerie
   * @return
   */
  public ChartBuilder addChartSerieList(ChartSerieBuilder... chartSerie) {
    if (chartSerie != null) {
      this.elements.addAll(Arrays.asList(chartSerie));
    }
    return this;
  }

  /**
   * Add chart tooltip
   *
   * @param chartTooltip
   * @return
   */
  public ChartBuilder addChartTooltip(ChartTooltipBuilder... chartTooltip) {
    if (chartTooltip != null) {
      this.elements.addAll(Arrays.asList(chartTooltip));
    }
    return this;
  }

  /**
   * Add context button
   *
   * @param contextButton
   * @return
   */
  public ChartBuilder addContextButton(ContextButtonBuilder... contextButton) {
    if (contextButton != null) {
      this.elements.addAll(Arrays.asList(contextButton));
    }
    return this;
  }

  /**
   * Add context button
   *
   * @param contextSeparator
   * @return
   */
  public ChartBuilder addContextButton(ContextSeparatorBuilder... contextSeparator) {
    if (contextSeparator != null) {
      this.elements.addAll(Arrays.asList(contextSeparator));
    }
    return this;
  }

  /**
   * Add x axis
   *
   * @param xAxis
   * @return
   */
  public ChartBuilder addXAxis(XAxisBuider... xAxis) {
    if (xAxis != null) {
      if (this.xAxisList == null) {
        this.xAxisList = new ArrayList<>();
      }
      this.xAxisList.addAll(Arrays.asList(xAxis));
    }
    return this;
  }

  /**
   * Get x axis list
   *
   * @return
   */
  public List<XAxisBuider> getXAxisList() {
    return xAxisList;
  }

  /**
   * Add y axis
   *
   * @param yAxis
   * @return
   */
  public ChartBuilder addYAxis(YAxisBuider... yAxis) {
    if (yAxis != null) {
      if (this.yAxisList == null) {
        this.yAxisList = new ArrayList<>();
      }
      this.yAxisList.addAll(Arrays.asList(yAxis));
    }
    return this;
  }

  /**
   * Get Y axis list
   *
   * @return
   */
  public List<YAxisBuider> getYAxisList() {
    return yAxisList;
  }

  /**
   * Add dependency
   *
   * @param dependencyBuilder
   * @return
   */
  public ChartBuilder addDependency(DependencyBuilder... dependencyBuilder) {
    if (dependencyBuilder != null) {
      this.elements.addAll(Arrays.asList(dependencyBuilder));
    }
    return this;
  }

  /**
   * Get element list
   *
   * @return
   */
  public List<AweBuilder> getElementList() {
    return this.elements;
  }
}
