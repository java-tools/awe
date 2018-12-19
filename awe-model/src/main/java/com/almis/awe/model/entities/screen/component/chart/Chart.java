package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.type.ChartType;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Chart Class
 *
 * Used to parse a chart tag with XStream
 *
 *
 * Generates an Chart widget
 *
 *
 * @author Pablo VIDAL - 20/OCT/2014
 */
@XStreamAlias("chart")
public class Chart extends ChartModel {

  private static final long serialVersionUID = -5778594007618151363L;

  // Chart subtitle
  @XStreamAlias("subtitle")
  @XStreamAsAttribute
  private String subTitle;

  // Flag stock chart
  @XStreamAlias("stock-chart")
  @XStreamAsAttribute
  private String stockChart;

  // Chart theme template
  @XStreamAlias("theme")
  @XStreamAsAttribute
  private String theme;

  // Inverted axis
  @XStreamAlias("inverted")
  @XStreamAsAttribute
  private String inverted;

  // Stack series of chart (Only column and area TYPE)
  @XStreamAlias("stacking")
  @XStreamAsAttribute
  private String stacking;

  // Flag to show data labels in series
  @XStreamAlias("enable-data-labels")
  @XStreamAsAttribute
  private String enableDataLabels;

  // Format data labels
  @XStreamAlias("format-data-labels")
  @XStreamAsAttribute
  private String formatDataLabels;

  // Flag to enable chart zoom
  @XStreamAlias("zoom-type")
  @XStreamAsAttribute
  private String zoomType;

  // Legend of chart
  @XStreamAlias("chart-legend")
  private ChartLegend chartLegend;

  // Tooltip of chart
  @XStreamAlias("chart-tooltip")
  private ChartTooltip chartTooltip;

  // Chart X Axis list
  @XStreamImplicit(itemFieldName = "x-axis")
  private List<ChartAxis> xAxisList;

  // Chart Y Axis list
  @XStreamImplicit(itemFieldName = "y-axis")
  private List<ChartAxis> yAxisList;

  // Chart Y Axis list
  @XStreamImplicit
  private List<ChartSerie> serieList;

  /**
   * Default constructor
   */
  public Chart() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Chart(Chart other) throws AWException {
    super(other);
    this.subTitle = other.subTitle;
    this.stockChart = other.stockChart;
    this.theme = other.theme;
    this.inverted = other.inverted;
    this.stacking = other.stacking;
    this.enableDataLabels = other.enableDataLabels;
    this.formatDataLabels = other.formatDataLabels;
    this.zoomType = other.zoomType;
    this.chartLegend = other.chartLegend == null ? null : new ChartLegend(other.chartLegend);
    this.chartTooltip = other.chartTooltip == null ? null : new ChartTooltip(other.chartTooltip);

    if (other.xAxisList != null) {
      this.xAxisList = new ArrayList<>();
      for (ChartAxis axis : other.xAxisList) {
        this.xAxisList.add(new ChartAxis(axis));
      }
    }

    if (other.yAxisList != null) {
      this.yAxisList = new ArrayList<>();
      for (ChartAxis axis : other.yAxisList) {
        this.yAxisList.add(new ChartAxis(axis));
      }
    }

    if (other.serieList != null) {
      this.serieList = new ArrayList<>();
      for (ChartSerie serie : other.serieList) {
        this.serieList.add(new ChartSerie(serie));
      }
    }
  }

  @Override
  public Chart copy() throws AWException {
    return new Chart(this);
  }

  /**
   * Retrieve chart subtitle
   *
   * @return Chart subtitle
   */
  @JsonIgnore
  public String getSubTitle() {
    return subTitle;
  }

  /**
   * Set chart subtitle
   *
   * @param subTitle Chart subtitle
   * @return Chart
   */
  public Chart setSubTitle(String subTitle) {
    this.subTitle = subTitle;
    return this;
  }

  /**
   * Retrive stockChart VALUE
   *
   * @return boolean stockChart
   */
  @JsonIgnore
  public String getStockChart() {
    return stockChart;
  }

  /**
   * Store stockChart flag
   *
   * @param stockChart A boolean string VALUE
   * @return Chart
   */
  public Chart setStockChart(String stockChart) {
    this.stockChart = stockChart;
    return this;
  }

  /**
   * Get if chart is a stock chart TYPE
   *
   * @return flag stockChart
   */
  @JsonIgnore
  public boolean isStockChart() {
    return "true".equalsIgnoreCase(getStockChart());
  }

  /**
   * Retrive theme of chart
   *
   * @return theme chart
   */
  @JsonIgnore
  public String getTheme() {
    return theme;
  }

  /**
   * Store theme attribute
   *
   * @param theme Chart theme
   * @return Chart
   */
  public Chart setTheme(String theme) {
    this.theme = theme;
    return this;
  }

  /**
   * Retrieve if chart has axis inverted
   *
   * @return flag inverted
   */
  @JsonIgnore
  public String getInverted() {
    return inverted;
  }

  /**
   * Store flag inverted
   *
   * @param inverted Chart axis inverted
   * @return Chart
   */
  public Chart setInverted(String inverted) {
    this.inverted = inverted;
    return this;
  }

  /**
   * Get if chart has its axis inverted
   *
   * @return flag inverted
   */
  @JsonIgnore
  public boolean isInverted() {
    return "true".equalsIgnoreCase(getInverted());
  }

  /**
   * Get the kind of stack Values "normal" or "percent" (Only apply column and area charts)
   *
   * @return stacking
   */
  @JsonIgnore
  public String getStacking() {
    return stacking;
  }

  /**
   * Store stacking TYPE of chart
   *
   * @param stacking Stack TYPE
   * @return Chart
   */
  public Chart setStacking(String stacking) {
    this.stacking = stacking;
    return this;
  }

  /**
   * Get flag enabled data labels
   *
   * @return flag data labels
   */
  @JsonIgnore
  public String getEnableDataLabels() {
    return enableDataLabels;
  }

  /**
   * Store flag data labels
   *
   * @param enableDataLabels Enable data labels
   * @return Chart
   */
  public Chart setEnableDataLabels(String enableDataLabels) {
    this.enableDataLabels = enableDataLabels;
    return this;
  }

  /**
   * Flat to show data labels in chart
   *
   * @return flag data labels
   */
  @JsonIgnore
  public boolean isEnabledDataLabels() {
    return "true".equalsIgnoreCase(getEnableDataLabels());
  }

  /**
   * Retrieve format data labels
   *
   * @return format
   */
  public String getFormatDataLabels() {
    return formatDataLabels;
  }

  /**
   * Store format for data labels
   *
   * @param formatDataLabels Format data labels
   * @return Chart
   */
  public Chart setFormatDataLabels(String formatDataLabels) {
    this.formatDataLabels = formatDataLabels;
    return this;
  }

  /**
   * Retrieve TYPE zoom by dragging the mouse Default none
   *
   * @return flag zoom
   */
  @JsonIgnore
  public String getZoomType() {
    return zoomType;
  }

  /**
   * Store zoom TYPE attribute
   *
   * @param zoomType Zoom type
   * @return Chart
   */
  public Chart setZoomType(String zoomType) {
    this.zoomType = zoomType;
    return this;
  }

  /**
   * Retrieve Chart legend
   *
   * @return chart legend
   */
  @JsonIgnore
  public ChartLegend getChartLegend() {
    return chartLegend;
  }

  /**
   * Store chart legend of char
   *
   * @param chartLegend Chart legend
   * @return Chart
   */
  public Chart setChartLegend(ChartLegend chartLegend) {
    this.chartLegend = chartLegend;
    return this;
  }

  /**
   * Retrieve tooltip chart
   *
   * @return tooltip chart
   */
  @JsonIgnore
  public ChartTooltip getChartTooltip() {
    return chartTooltip;
  }

  /**
   * Store tooltip object to char
   *
   * @param chartTooltip Chart tooltip
   * @return Chart
   */
  public Chart setChartTooltip(ChartTooltip chartTooltip) {
    this.chartTooltip = chartTooltip;
    return this;
  }

  /**
   * Retrieve list of xAxis
   *
   * @return list with xAxis
   */
  @JsonIgnore
  public List<ChartAxis> getxAxisList() {
    return xAxisList;
  }

  /**
   * Store the list of xAxis
   *
   * @param xAxisList X Axis list
   * @return Chart
   */
  public Chart setxAxisList(List<ChartAxis> xAxisList) {
    this.xAxisList = xAxisList;
    return this;
  }

  /**
   * Retrieve list of yAxis
   *
   * @return list with yAxis
   */
  @JsonIgnore
  public List<ChartAxis> getyAxisList() {
    return yAxisList;
  }

  /**
   * Store the list of xAxis
   *
   * @param yAxisList Y Axis list
   * @return Chart
   */
  public Chart setyAxisList(List<ChartAxis> yAxisList) {
    this.yAxisList = yAxisList;
    return this;
  }

  /**
   * Retrieve list series of chart
   *
   * @return Serie list
   */
  @JsonIgnore
  public List<ChartSerie> getSerieList() {
    return serieList;
  }

  /**
   * Retrieve series of chart
   *
   * @param serieList Serie list
   * @return Chart
   */
  public Chart setSerieList(List<ChartSerie> serieList) {
    this.serieList = serieList;
    return this;
  }

  /**
   * Check if is a 3D chart TYPE
   *
   * @return flag 3D chart
   */
  @JsonIgnore
  public boolean is3DChart() {
    return getType().toUpperCase().endsWith("3D");
  }

  /**
   * Retrieve chart model
   *
   * @return Chart model
   */
  @JsonGetter("chartModel")
  public ObjectNode getChartModel() {
    JsonNodeFactory factory = JsonNodeFactory.instance;
    ObjectNode chartNode = factory.objectNode();

    // Add node general chart options
    if (getType() != null) {
      // Check if is stock chart
      if (getStockChart() != null) {
        chartNode.put(ChartConstants.STOCK_CHART, isStockChart());
      }

      // Add chart general information
      chartNode.set(ChartConstants.CHART, getChartInfo());

      // Add plotOptions to chart
      chartNode.set(ChartConstants.PLOT_OPTIONS, getPlotOptions());
    }

    // Set theme
    if (getTheme() != null) {
      chartNode.put(ChartConstants.THEME, getTheme());
    }

    // Disable chart credits
    ObjectNode creditsNode = factory.objectNode();
    creditsNode.put(ChartConstants.ENABLED, false);
    chartNode.set(ChartConstants.CREDITS, creditsNode);

    // Add chat title
    if (getLabel() != null) {
      ObjectNode nodeTitle = JsonNodeFactory.instance.objectNode();
      nodeTitle.put(ChartConstants.TEXT, this.getLabel());
      chartNode.set(ChartConstants.TITLE, nodeTitle);
    }

    // Add chat subtitle
    if (getSubTitle() != null) {
      ObjectNode nodeSubTitle = JsonNodeFactory.instance.objectNode();
      nodeSubTitle.put(ChartConstants.TEXT, this.getSubTitle());
      chartNode.set(ChartConstants.SUBTITLE, nodeSubTitle);
    }

    // Add xAsis model
    if (getxAxisList() != null) {
      chartNode.set(ChartConstants.X_AXIS, this.getAxisModel(getxAxisList()));
    }
    // Add yAsis model
    if (getyAxisList() != null) {
      chartNode.set(ChartConstants.Y_AXIS, this.getAxisModel(getyAxisList()));
    }

    // Add series model
    if (getSerieList() != null) {
      chartNode.set(ChartConstants.SERIES, this.getSeriesModel(getSerieList(), false));

      // Add drilldown series model
      ObjectNode seriesDrilldown = JsonNodeFactory.instance.objectNode();
      seriesDrilldown.set(ChartConstants.SERIES, this.getSeriesModel(getSerieList(), true));
      chartNode.set(ChartConstants.DRILL_DOWN, seriesDrilldown);
    }

    // Add tooltip model
    if (getChartTooltip() != null) {
      chartNode.set(ChartConstants.TOOLTIP, this.getTooltipModel(getChartTooltip()));
    }

    // Add legend model
    if (getChartLegend() != null) {
      chartNode.set(ChartConstants.LEGEND, this.getLegendModel(getChartLegend()));
    }

    // Update model with chart parameters
    addParameters(chartNode);

    // Return string parameter list
    return chartNode;
  }

  /**
   * Get Json chart general node
   *
   * @return Json chart node general
   */
  @SuppressWarnings("incomplete-switch")
  private JsonNode getChartInfo() {
    // Variable definition
    JsonNodeFactory factory = JsonNodeFactory.instance;
    ObjectNode chartNode = factory.objectNode();

    // Set chart TYPE attributes
    // --------------------------------------------------------------------------
    ChartType chartType = ChartType.valueOf(this.getType().toUpperCase());
    if (this.getType() != null) {
      switch (chartType) {
        case COLUMN_3D:
          chartNode.put(ChartConstants.TYPE, ChartConstants.COLUMN);
          break;
        case PIE_3D:
        case DONUT:
        case DONUT_3D:
        case SEMICIRCLE:
          chartNode.put(ChartConstants.TYPE, ChartConstants.PIE);
          break;
        default:
          chartNode.put(ChartConstants.TYPE, chartType.toString().toLowerCase());
          break;
      }
    }

    // Set if inverted
    // --------------------------------------------------------------------------
    chartNode.put(ChartConstants.INVERTED, isInverted());

    // Check 3D options
    if (is3DChart()) {
      ObjectNode options3DNode = factory.objectNode();
      options3DNode.put(ChartConstants.ENABLED, true);
      switch (chartType) {
        case COLUMN_3D:
          options3DNode.set(ChartConstants.ALPHA, factory.numberNode(15));
          options3DNode.set(ChartConstants.BETA, factory.numberNode(15));
          break;

        case PIE_3D:
          options3DNode.set(ChartConstants.ALPHA, factory.numberNode(45));
          options3DNode.set(ChartConstants.BETA, factory.numberNode(0));
          break;

        case DONUT_3D:
          options3DNode.set(ChartConstants.ALPHA, factory.numberNode(45));
          break;

        default:
          options3DNode.put(ChartConstants.ENABLED, false);

      }
      chartNode.set(ChartConstants.OPTIONS3D, options3DNode);
    }

    // Set zoom TYPE
    if (this.getZoomType() != null) {
      chartNode.put(ChartConstants.ZOOM_TYPE, this.getZoomType());
    }

    return chartNode;
  }

  /**
   * Get Json plotOptions node
   *
   * @return Json plotOptions node
   */
  private JsonNode getPlotOptions() {
    // Variable definition
    JsonNodeFactory factory = JsonNodeFactory.instance;
    ObjectNode plotOptionsNode = factory.objectNode();
    ObjectNode charTypePlotOpt = factory.objectNode();
    ChartType chartType = ChartType.valueOf(this.getType().toUpperCase());

    // Stacked chart series
    // --------------------------------------------------------------------------
    if (this.getStacking() != null) {
      charTypePlotOpt.put(ChartConstants.STACKING, this.getStacking());
    }

    // 3D Charts plot options
    if (is3DChart()) {
      switch (chartType) {
        case PIE_3D:
          charTypePlotOpt.set(ChartConstants.DEPTH, factory.numberNode(35));
          break;
        case DONUT_3D:
          charTypePlotOpt.set(ChartConstants.INNER_SIZE, factory.numberNode(100));
          charTypePlotOpt.set(ChartConstants.DEPTH, factory.numberNode(45));
          break;
        default:
      }
    }

    // Data labels
    if (this.isEnabledDataLabels()) {
      ObjectNode dataLabelsNode = factory.objectNode();
      dataLabelsNode.put(ChartConstants.ENABLED, true);

      // Format data labels
      if (this.getFormatDataLabels() != null) {
        dataLabelsNode.put(ChartConstants.FORMAT, this.getFormatDataLabels());
      }

      charTypePlotOpt.set(ChartConstants.DATALABELS, dataLabelsNode);
    }

    // Set fields to plot options node
    switch (chartType) {
      case COLUMN_3D:
        plotOptionsNode.set(ChartConstants.COLUMN, charTypePlotOpt);
        break;
      case PIE:
      case PIE_3D:
        generateLegend(charTypePlotOpt);
        charTypePlotOpt.put(ChartConstants.CURSOR, ChartConstants.POINTER);
        charTypePlotOpt.put(ChartConstants.ALLOW_POINT_SELECT, true);
        plotOptionsNode.set(ChartConstants.PIE, charTypePlotOpt);
        break;

      case DONUT:
      case DONUT_3D:
        generateLegend(charTypePlotOpt);
        charTypePlotOpt.put(ChartConstants.CURSOR, ChartConstants.POINTER);
        charTypePlotOpt.put(ChartConstants.ALLOW_POINT_SELECT, true);
        charTypePlotOpt.put(ChartConstants.INNER_SIZE, "50%");
        plotOptionsNode.set(ChartConstants.PIE, charTypePlotOpt);
        break;

      case SEMICIRCLE:
        generateLegend(charTypePlotOpt);
        charTypePlotOpt.put(ChartConstants.CURSOR, ChartConstants.POINTER);
        charTypePlotOpt.put(ChartConstants.ALLOW_POINT_SELECT, true);
        // Set start and eng angle of circle
        charTypePlotOpt.set(ChartConstants.START_ANGLE, factory.numberNode(-90));
        charTypePlotOpt.set(ChartConstants.END_ANGLE, factory.numberNode(90));
        // Set center of semicircle
        ArrayNode centerArrayNode = factory.arrayNode();
        centerArrayNode.add("50%");
        centerArrayNode.add("75%");
        charTypePlotOpt.set(ChartConstants.CENTER, centerArrayNode);
        charTypePlotOpt.put(ChartConstants.INNER_SIZE, "50%");
        plotOptionsNode.set(ChartConstants.PIE, charTypePlotOpt);
        break;
      default:
        plotOptionsNode.set(chartType.toString().toLowerCase(), charTypePlotOpt);
    }

    return plotOptionsNode;
  }

  /**
   * Generate legend if defined
   * @param charTypePlotOpt Plot options
   */
  private void generateLegend(ObjectNode charTypePlotOpt) {
    if (this.chartLegend != null && this.chartLegend.isEnabled()) {
      charTypePlotOpt.put(ChartConstants.SHOW_IN_LEGEND, true);
    }
  }

  /**
   * Get json node controller for Axis element
   *
   * @param typeAxisList axis TYPE element list
   * @return Json node with Axis element
   */
  private JsonNode getAxisModel(List<ChartAxis> typeAxisList) {

    // Array with TYPE of Axis
    ArrayNode axisModel = JsonNodeFactory.instance.arrayNode();

    // Add axis controller attributes
    for (ChartAxis axis : typeAxisList) {
      axisModel.add(axis.getModel());
    }
    return axisModel;
  }

  /**
   * Get json node controller for Serie element
   *
   * @param serieList serie element list
   * @param drilldown flag inidicate serie drilldown TYPE
   * @return Json node with series element
   */
  private JsonNode getSeriesModel(List<ChartSerie> serieList, boolean drilldown) {

    // Array with chart series
    ArrayNode seriesModel = JsonNodeFactory.instance.arrayNode();

    // Add axis controller attributes
    for (ChartSerie serie : serieList) {
      if (serie.isDrillDownSerie() == drilldown) {
        seriesModel.add(serie.getModel());
      }
    }
    return seriesModel;
  }

  /**
   * Get json node controller for tooltip element
   *
   * @param chartTooltip Tooltip object
   * @return Json node with tooltip element
   */
  private JsonNode getTooltipModel(ChartTooltip chartTooltip) {
    // Get tooltip controller
    return chartTooltip.getModel();
  }

  /**
   * Get json node controller for legend element
   *
   * @param chartLegend Legend object
   * @return Json node with legend element
   */
  private JsonNode getLegendModel(ChartLegend chartLegend) {
    return chartLegend.getModel();
  }

  @JsonIgnore
  @Override
  public String getComponentTag() {
    return "chart";
  }

  @JsonIgnore
  @Override
  public String getHelpTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_HELP_CHART;
  }

  /**
   * Get print element list (to be overwritten)
   *
   * @param printElementList Print element list
   * @param label            Last label
   * @param parameters       Parameters
   * @param dataSuffix       Data suffix
   * @return Print bean
   */
  @JsonIgnore
  @Override
  public List<Element> getReportStructure(List<Element> printElementList, String label, ObjectNode parameters, String dataSuffix) {
    if (getLabel() == null) {
      setLabel(label);
    }
    printElementList.add(this);
    return printElementList;
  }
}
