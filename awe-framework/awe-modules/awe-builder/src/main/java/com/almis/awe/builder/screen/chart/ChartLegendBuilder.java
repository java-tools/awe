package com.almis.awe.builder.screen.chart;

import com.almis.awe.builder.enumerates.Align;
import com.almis.awe.builder.enumerates.ChartLayout;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.chart.ChartLegend;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class ChartLegendBuilder extends AweBuilder<ChartLegendBuilder> {

  private Align align;
  private ChartLayout chartLayout;
  private String label;
  private Integer borderWidth;
  private Boolean enabled;
  private Boolean floating;
  private List<ChartParameterBuilder> chartParameterList;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public ChartLegendBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    chartParameterList = new ArrayList<>();
  }

  @Override
  public ChartLegendBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    ChartLegend chartLegend = new ChartLegend();

    chartLegend.setId(getId());

    if (getAlign() != null) {
      chartLegend.setAlign(getAlign().toString());
    }

    if (getChartLayout() != null) {
      chartLegend.setLayout(getChartLayout().toString());
    }

    if (getBorderWidth() != null) {
      chartLegend.setBorderWidth(String.valueOf(getBorderWidth()));
    }

    if (getLabel() != null) {
      chartLegend.setLabel(getLabel());
    }

    if (isEnabled() != null) {
      chartLegend.setEnabled(String.valueOf(isEnabled()));
    }

    if (isFloating() != null) {
      chartLegend.setFloating(String.valueOf(isFloating()));
    }

    for (ChartParameterBuilder chartParameterBuilder : chartParameterList) {
      addElement(chartLegend, chartParameterBuilder.build(chartLegend));
    }

    return chartLegend;
  }

  /**
   * Get align
   *
   * @return
   */
  public Align getAlign() {
    return align;
  }

  /**
   * Set align
   *
   * @param align
   * @return
   */
  public ChartLegendBuilder setAlign(Align align) {
    this.align = align;
    return this;
  }

  /**
   * Get chart layout
   *
   * @return
   */
  public ChartLayout getChartLayout() {
    return chartLayout;
  }

  /**
   * Set chart layout
   *
   * @param chartLayout
   * @return
   */
  public ChartLegendBuilder setChartLayout(ChartLayout chartLayout) {
    this.chartLayout = chartLayout;
    return this;
  }

  /**
   * Get border width
   *
   * @return
   */
  public Integer getBorderWidth() {
    return borderWidth;
  }

  /**
   * Set border width
   *
   * @param borderWidth
   * @return
   */
  public ChartLegendBuilder setBorderWidth(Integer borderWidth) {
    this.borderWidth = borderWidth;
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
  public ChartLegendBuilder setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * Is enabled
   *
   * @return
   */
  public Boolean isEnabled() {
    return enabled;
  }

  /**
   * Set enabled
   *
   * @param enabled
   * @return
   */
  public ChartLegendBuilder setEnabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Is floating
   *
   * @return
   */
  public Boolean isFloating() {
    return floating;
  }

  /**
   * Set floating
   *
   * @param floating
   * @return
   */
  public ChartLegendBuilder setFloating(Boolean floating) {
    this.floating = floating;
    return this;
  }

  /**
   * Get chart parameter list
   *
   * @return
   */
  public List<ChartParameterBuilder> getChartParameterList() {
    return chartParameterList;
  }

  /**
   * Add chart parameter list
   *
   * @param chartParameter
   * @return
   */
  public ChartLegendBuilder addChartParameter(ChartParameterBuilder... chartParameter) {
    if (chartParameter != null) {
      if (this.chartParameterList == null) {
        this.chartParameterList = new ArrayList<>();
      }
      this.chartParameterList.addAll(Arrays.asList(chartParameter));
    }
    return this;
  }
}
