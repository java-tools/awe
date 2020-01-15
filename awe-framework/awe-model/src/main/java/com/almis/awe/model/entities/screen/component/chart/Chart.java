package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.type.ChartType;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Chart Class
 * <p>
 * Used to parse a chart tag with XStream
 * <p>
 * <p>
 * Generates an Chart widget
 *
 * @author Pablo VIDAL - 20/OCT/2014
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("chart")
public class Chart extends AbstractChart {

  private static final long serialVersionUID = -5778594007618151363L;

  // Chart subtitle
  @JsonIgnore
  @XStreamAlias("subtitle")
  @XStreamAsAttribute
  private String subTitle;

  // Flag stock chart
  @JsonIgnore
  @XStreamAlias("stock-chart")
  @XStreamAsAttribute
  private Boolean stockChart;

  // Chart theme template
  @JsonIgnore
  @XStreamAlias("theme")
  @XStreamAsAttribute
  private String theme;

  // Inverted axis
  @JsonIgnore
  @XStreamAlias("inverted")
  @XStreamAsAttribute
  private Boolean inverted;

  // Stack series of chart (Only column and area TYPE)
  @JsonIgnore
  @XStreamAlias("stacking")
  @XStreamAsAttribute
  private String stacking;

  // Flag to show data labels in series
  @JsonIgnore
  @XStreamAlias("enable-data-labels")
  @XStreamAsAttribute
  private Boolean enableDataLabels;

  // Format data labels
  @JsonIgnore
  @XStreamAlias("format-data-labels")
  @XStreamAsAttribute
  private String formatDataLabels;

  // Flag to enable chart zoom
  @JsonIgnore
  @XStreamAlias("zoom-type")
  @XStreamAsAttribute
  private String zoomType;

  // Legend of chart
  @XStreamAlias("chart-legend")
  @JsonIgnore
  private ChartLegend chartLegend;

  // Tooltip of chart
  @JsonIgnore
  @XStreamAlias("chart-tooltip")
  private ChartTooltip chartTooltip;

  // Chart X Axis list
  @JsonIgnore
  @XStreamImplicit(itemFieldName = "x-axis")
  private List<ChartAxis> xAxisList;

  // Chart Y Axis list
  @JsonIgnore
  @XStreamImplicit(itemFieldName = "y-axis")
  private List<ChartAxis> yAxisList;

  // Chart Y Axis list
  @JsonIgnore
  @XStreamImplicit
  private List<ChartSerie> serieList;

  @Override
  public Chart copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
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
   * Returns if is stock chart
   *
   * @return Is stock chart
   */
  public boolean isStockChart() {
    return stockChart != null && stockChart;
  }

  /**
   * Returns if is inverted
   *
   * @return Is inverted
   */
  public boolean isInverted() {
    return inverted != null && inverted;
  }

  /**
   * Returns if data labels are enabled
   *
   * @return Data labels are enabled
   */
  public boolean isEnableDataLabels() {
    return enableDataLabels != null && enableDataLabels;
  }

  /**
   * Retrieve chart model
   *
   * @return Chart model
   */
  @JsonGetter("chartModel")
  public Map<String, Object> getChartModel() {
    Map<String, Object> chartModel = new HashMap<>();

    // Add node general chart options
    if (getType() != null) {
      // Check if is stock chart
      chartModel.put(ChartConstants.STOCK_CHART, isStockChart());

      // Add chart general information
      chartModel.put(ChartConstants.CHART, getChartInfo());

      // Add plotOptions to chart
      chartModel.put(ChartConstants.PLOT_OPTIONS, getPlotOptions());
    }

    // Set theme
    if (getTheme() != null) {
      chartModel.put(ChartConstants.THEME, getTheme());
    }

    // Disable chart credits
    Map<String, Object> creditsMap = new HashMap<>();
    creditsMap.put(ChartConstants.ENABLED, false);
    chartModel.put(ChartConstants.CREDITS, creditsMap);

    // Add chat title
    if (getLabel() != null) {
      chartModel.put(ChartConstants.TITLE, getTextParameter(getLabel()));
    }

    // Add chat subtitle
    if (getSubTitle() != null) {
      chartModel.put(ChartConstants.SUBTITLE, getTextParameter(getSubTitle()));
    }

    // Add xAsis model
    if (getXAxisList() != null) {
      chartModel.put(ChartConstants.X_AXIS, getAxisModel(getXAxisList()));
    }
    // Add yAsis model
    if (getYAxisList() != null) {
      chartModel.put(ChartConstants.Y_AXIS, getAxisModel(getYAxisList()));
    }

    // Add series model
    if (getSerieList() != null) {
      chartModel.put(ChartConstants.SERIES, this.getSeriesModel(getSerieList(), false));

      // Add drilldown series model
      Map<String, Object> seriesDrilldown = new HashMap<>();
      seriesDrilldown.put(ChartConstants.SERIES, getSeriesModel(getSerieList(), true));
      chartModel.put(ChartConstants.DRILL_DOWN, seriesDrilldown);
    }

    // Add tooltip model
    if (getChartTooltip() != null) {
      chartModel.put(ChartConstants.TOOLTIP, this.getTooltipModel(getChartTooltip()));
    }

    // Add legend model
    if (getChartLegend() != null) {
      chartModel.put(ChartConstants.LEGEND, this.getLegendModel(getChartLegend()));
    }

    // Update model with chart parameters
    addParameters(chartModel);

    // Return string parameter list
    return chartModel;
  }

  /**
   * Get Json chart general node
   *
   * @return Json chart node general
   */
  @SuppressWarnings("incomplete-switch")
  private Map<String, Object> getChartInfo() {
    Map<String, Object> chartInfo = new HashMap<>();

    // Set chart TYPE attributes
    // --------------------------------------------------------------------------
    ChartType chartType = ChartType.valueOf(this.getType().toUpperCase());
    if (this.getType() != null) {
      switch (chartType) {
        case COLUMN_3D:
          chartInfo.put(ChartConstants.TYPE, ChartConstants.COLUMN);
          break;
        case PIE_3D:
        case DONUT:
        case DONUT_3D:
        case SEMICIRCLE:
          chartInfo.put(ChartConstants.TYPE, ChartConstants.PIE);
          break;
        default:
          chartInfo.put(ChartConstants.TYPE, chartType.toString().toLowerCase());
          break;
      }
    }

    // Set if inverted
    // --------------------------------------------------------------------------
    chartInfo.put(ChartConstants.INVERTED, isInverted());

    // Check 3D options
    if (is3DChart()) {
      Map<String, Object> options3DNode = new HashMap<>();
      options3DNode.put(ChartConstants.ENABLED, true);
      switch (chartType) {
        case COLUMN_3D:
          options3DNode.put(ChartConstants.ALPHA, 15);
          options3DNode.put(ChartConstants.BETA, 15);
          break;

        case PIE_3D:
          options3DNode.put(ChartConstants.ALPHA, 45);
          options3DNode.put(ChartConstants.BETA, 0);
          break;

        case DONUT_3D:
          options3DNode.put(ChartConstants.ALPHA, 45);
          break;

        default:
          options3DNode.put(ChartConstants.ENABLED, false);

      }
      chartInfo.put(ChartConstants.OPTIONS3D, options3DNode);
    }

    // Set zoom TYPE
    if (this.getZoomType() != null) {
      chartInfo.put(ChartConstants.ZOOM_TYPE, this.getZoomType());
    }

    return chartInfo;
  }

  /**
   * Get Json plotOptions node
   *
   * @return Json plotOptions node
   */
  private Map<String, Object> getPlotOptions() {
    // Variable definition
    JsonNodeFactory factory = JsonNodeFactory.instance;
    Map<String, Object> plotOptionsNode = new HashMap<>();
    Map<String, Object> charTypePlotOpt = new HashMap<>();
    ChartType chartType = ChartType.valueOf(this.getType().toUpperCase());

    // Stacked chart series
    // --------------------------------------------------------------------------
    charTypePlotOpt.put(ChartConstants.STACKING, isStacking());

    // 3D Charts plot options
    if (is3DChart()) {
      switch (chartType) {
        case PIE_3D:
          charTypePlotOpt.put(ChartConstants.DEPTH, 35);
          break;
        case DONUT_3D:
          charTypePlotOpt.put(ChartConstants.INNER_SIZE, 100);
          charTypePlotOpt.put(ChartConstants.DEPTH, 45);
          break;
        default:
      }
    }

    // Data labels
    if (this.isEnableDataLabels()) {
      Map<String, Object> dataLabelsNode = new HashMap<>();
      dataLabelsNode.put(ChartConstants.ENABLED, true);

      // Format data labels
      if (this.getFormatDataLabels() != null) {
        dataLabelsNode.put(ChartConstants.FORMAT, this.getFormatDataLabels());
      }

      charTypePlotOpt.put(ChartConstants.DATALABELS, dataLabelsNode);
    }

    // Set fields to plot options node
    switch (chartType) {
      case COLUMN_3D:
        plotOptionsNode.put(ChartConstants.COLUMN, charTypePlotOpt);
        break;
      case PIE:
      case PIE_3D:
        generateLegend(charTypePlotOpt);
        charTypePlotOpt.put(ChartConstants.CURSOR, ChartConstants.POINTER);
        charTypePlotOpt.put(ChartConstants.ALLOW_POINT_SELECT, true);
        plotOptionsNode.put(ChartConstants.PIE, charTypePlotOpt);
        break;

      case DONUT:
      case DONUT_3D:
        generateLegend(charTypePlotOpt);
        charTypePlotOpt.put(ChartConstants.CURSOR, ChartConstants.POINTER);
        charTypePlotOpt.put(ChartConstants.ALLOW_POINT_SELECT, true);
        charTypePlotOpt.put(ChartConstants.INNER_SIZE, "50%");
        plotOptionsNode.put(ChartConstants.PIE, charTypePlotOpt);
        break;

      case SEMICIRCLE:
        generateLegend(charTypePlotOpt);
        charTypePlotOpt.put(ChartConstants.CURSOR, ChartConstants.POINTER);
        charTypePlotOpt.put(ChartConstants.ALLOW_POINT_SELECT, true);
        // Set start and eng angle of circle
        charTypePlotOpt.put(ChartConstants.START_ANGLE, factory.numberNode(-90));
        charTypePlotOpt.put(ChartConstants.END_ANGLE, factory.numberNode(90));
        // Set center of semicircle
        List<Object> centerArrayNode = new ArrayList<>();
        centerArrayNode.add("50%");
        centerArrayNode.add("75%");
        charTypePlotOpt.put(ChartConstants.CENTER, centerArrayNode);
        charTypePlotOpt.put(ChartConstants.INNER_SIZE, "50%");
        plotOptionsNode.put(ChartConstants.PIE, charTypePlotOpt);
        break;
      default:
        plotOptionsNode.put(chartType.toString().toLowerCase(), charTypePlotOpt);
    }

    return plotOptionsNode;
  }

  /**
   * Generate legend if defined
   *
   * @param charTypePlotOpt Plot options
   */
  private void generateLegend(Map<String, Object> charTypePlotOpt) {
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
  private List<Object> getAxisModel(List<ChartAxis> typeAxisList) {
    List<Object> axisModel = new ArrayList<>();

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
  private List<Object> getSeriesModel(List<ChartSerie> serieList, boolean drilldown) {

    // Array with chart series
    List<Object> seriesModel = new ArrayList<>();

    // Add axis controller attributes
    for (ChartSerie serie : serieList) {
      if (serie.isDrillDown() == drilldown) {
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
  private Map<String, Object> getTooltipModel(ChartTooltip chartTooltip) {
    // Get tooltip controller
    return chartTooltip.getModel();
  }

  /**
   * Get json node controller for legend element
   *
   * @param chartLegend Legend object
   * @return Json node with legend element
   */
  private Map<String, Object> getLegendModel(ChartLegend chartLegend) {
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

  /**
   * Is a stacking chart
   *
   * @return if is stack
   */
  public boolean isStacking() {
    return getStacking() != null;
  }
}
