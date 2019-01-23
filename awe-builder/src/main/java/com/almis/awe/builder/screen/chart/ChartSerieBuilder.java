/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.chart;

import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.chart.ChartSerie;
import com.almis.awe.model.type.ChartType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class ChartSerieBuilder extends AweBuilder<ChartSerieBuilder>{

  private ChartType type;
  private String xValue, yValue, zValue, color, label, xAxis, yAxis, drilldownSerie;
  private Boolean drilldown;
  private List<ChartParameterBuilder> chartParameterList;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public ChartSerieBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    this.chartParameterList = new ArrayList<>();
  }

  @Override
  public ChartSerieBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    ChartSerie chartSerie = new ChartSerie();
    
    chartSerie.setId(getId());
    
    if( getType() != null){
      chartSerie.setType(getType().toString());
    }
    
    if(getxValue() != null){
      chartSerie.setxValue(getxValue());
    }
    
    if(getyValue() != null){
      chartSerie.setyValue(getyValue());
    }
    
    if(getzValue() != null){
      chartSerie.setzValue(getzValue());
    }
    
    if(getColor() != null){
      chartSerie.setColor(getColor());
    }
    
    if(getLabel() != null){
      chartSerie.setLabel(getLabel());
    }
    
    if( getxAxis() != null){
      chartSerie.setxAxis(getxAxis());
    }
    
    if( getyAxis() != null){
      chartSerie.setyAxis(getyAxis());
    }
    
    if( isDrilldown() != null){
      chartSerie.setDrillDown(String.valueOf(isDrilldown()));
    }
    
    if(getDrilldownSerie() != null){
      chartSerie.setDrillDownSerie(getDrilldownSerie());
    }
    
    for(ChartParameterBuilder chartParameterBuilder : getChartParameterList()){
      addElement(chartSerie, chartParameterBuilder.build(chartSerie));
    }
    
    return chartSerie;
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
  public ChartSerieBuilder setType(ChartType type) {
    this.type = type;
    return this;
  }

  /**
   * Get value
   *
   * @return
   */
  public String getxValue() {
    return xValue;
  }

  /**
   * Set value
   *
   * @param xValue
   * @return
   */
  public ChartSerieBuilder setxValue(String xValue) {
    this.xValue = xValue;
    return this;
  }

  /**
   * Get Y value
   *
   * @return
   */
  public String getyValue() {
    return yValue;
  }

  /**
   * Set Y value
   *
   * @param yValue
   * @return
   */
  public ChartSerieBuilder setyValue(String yValue) {
    this.yValue = yValue;
    return this;
  }

  /**
   * Get Z value
   *
   * @return
   */
  public String getzValue() {
    return zValue;
  }

  /**
   * Set Z value
   *
   * @param zValue
   * @return
   */
  public ChartSerieBuilder setzValue(String zValue) {
    this.zValue = zValue;
    return this;
  }

  /**
   * Get color
   *
   * @return
   */
  public String getColor() {
    return color;
  }

  /**
   * Set color
   *
   * @param color
   * @return
   */
  public ChartSerieBuilder setColor(String color) {
    this.color = color;
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
  public ChartSerieBuilder setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * Get X axis
   *
   * @return
   */
  public String getxAxis() {
    return xAxis;
  }

  /**
   * Set X axis
   *
   * @param xAxis
   * @return
   */
  public ChartSerieBuilder setxAxis(String xAxis) {
    this.xAxis = xAxis;
    return this;
  }

  /**
   * Get Y axis
   *
   * @return
   */
  public String getyAxis() {
    return yAxis;
  }

  /**
   * Set Y axis
   *
   * @param yAxis
   * @return
   */
  public ChartSerieBuilder setyAxis(String yAxis) {
    this.yAxis = yAxis;
    return this;
  }

  /**
   * Is drilldown
   *
   * @return
   */
  public Boolean isDrilldown() {
    return drilldown;
  }

  /**
   * Set drilldown
   *
   * @param drilldown
   * @return
   */
  public ChartSerieBuilder setDrilldown(Boolean drilldown) {
    this.drilldown = drilldown;
    return this;
  }

  /**
   * Get drilldown serie
   *
   * @return
   */
  public String getDrilldownSerie() {
    return drilldownSerie;
  }

  /**
   * Set drilldown serie
   *
   * @param drilldownSerie
   * @return this
   */
  public ChartSerieBuilder setDrilldownSerie(String drilldownSerie) {
    this.drilldownSerie = drilldownSerie;
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
   * Add chart parameter
   *
   * @param chartParameter
   * @return
   */
  public ChartSerieBuilder addChartParameter(ChartParameterBuilder chartParameter) {
    if( this.chartParameterList == null){
      this.chartParameterList = new ArrayList<>();
    }
    this.chartParameterList.add(chartParameter);
    return this;
  }

  /**
   * Set chart parameter list
   *
   * @param chartParameterList
   * @return
   * @throws AWException
   */
  public ChartSerieBuilder setChartParameterList(List<ChartParameterBuilder> chartParameterList) throws AWException {
    if( chartParameterList != null){
      this.chartParameterList = chartParameterList;
    }else{
      throw new AWException("The given parameter list cannot be null.");
    }
    return this;
  }
}
