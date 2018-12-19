/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.chart;

import com.almis.awe.builder.enumerates.ChartAxis;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.chart.ChartTooltip;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class ChartTooltipBuilder extends AweBuilder<ChartTooltipBuilder> {

  private ChartAxis crosshairs;
  private String dateFormat, pointFormat, prefix, suffix;
  private Boolean enabled, shared;
  private Integer numberDecimals;
  private List<ChartParameterBuilder> chartParameterList;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public ChartTooltipBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    chartParameterList = new ArrayList<>();
  }

  @Override
  public ChartTooltipBuilder setParent() {
    return this;
  }
  
  @Override
  public Element build(Element element) {
    ChartTooltip chartTooltip = new ChartTooltip();
    
    chartTooltip.setId(getId());
    
    if(getCrosshairs() != null){
      chartTooltip.setCrosshairs(getCrosshairs().toString());
    }
    
    if( getDateFormat() != null){
      chartTooltip.setDateFormat(getDateFormat());
    }
    
    if( getPointFormat() != null){
      chartTooltip.setPointFormat(getPointFormat());
    }
    
    if( getPrefix() != null){
      chartTooltip.setPreffix(getPrefix());
    }
    
    if( getSuffix() != null){
      chartTooltip.setSuffix(getSuffix());
    }
    
    if( isEnabled() != null){
      chartTooltip.setEnable(String.valueOf(isEnabled()));
    }
    
    if(isShared() != null){
      chartTooltip.setShared(String.valueOf(isShared()));
    }
    
    if( getNumberDecimals() != null){
      chartTooltip.setNumberDecimals(String.valueOf(getNumberDecimals()));
    }
    
    for(ChartParameterBuilder chartParameterBuilder : getChartParameterList()){
      addElement(chartTooltip, chartParameterBuilder.build(chartTooltip));
    }
    
    return chartTooltip;
  }

  /**
   * Get crosshairs
   *
   * @return
   */
  public ChartAxis getCrosshairs() {
    return crosshairs;
  }

  /**
   * Set crosshairs
   *
   * @param crosshairs
   * @return
   */
  public ChartTooltipBuilder setCrosshairs(ChartAxis crosshairs) {
    this.crosshairs = crosshairs;
    return this;
  }

  /**
   * Get date format
   *
   * @return
   */
  public String getDateFormat() {
    return dateFormat;
  }

  /**
   * Set date format
   *
   * @param dateFormat
   * @return
   */
  public ChartTooltipBuilder setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
    return this;
  }

  /**
   * Get point format
   *
   * @return
   */
  public String getPointFormat() {
    return pointFormat;
  }

  /**
   * Set point format
   *
   * @param pointFormat
   * @return
   */
  public ChartTooltipBuilder setPointFormat(String pointFormat) {
    this.pointFormat = pointFormat;
    return this;
  }

  /**
   * Get prefix
   *
   * @return
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * Set prefix
   *
   * @param prefix
   * @return
   */
  public ChartTooltipBuilder setPrefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  /**
   * Get suffix
   *
   * @return
   */
  public String getSuffix() {
    return suffix;
  }

  /**
   * Set suffix
   *
   * @param suffix
   * @return
   */
  public ChartTooltipBuilder setSuffix(String suffix) {
    this.suffix = suffix;
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
  public ChartTooltipBuilder setEnabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Is shared
   *
   * @return
   */
  public Boolean isShared() {
    return shared;
  }

  /**
   * Set shared
   *
   * @param shared
   * @return
   */
  public ChartTooltipBuilder setShared(Boolean shared) {
    this.shared = shared;
    return this;
  }

  /**
   * Get number decimals
   *
   * @return
   */
  public Integer getNumberDecimals() {
    return numberDecimals;
  }

  /**
   * Set number decimals
   *
   * @param numberDecimals
   * @return
   */
  public ChartTooltipBuilder setNumberDecimals(Integer numberDecimals) {
    this.numberDecimals = numberDecimals;
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
   * Add chart paramter
   *
   * @param chartParameter
   * @return
   */
  public ChartTooltipBuilder addChartParameter(ChartParameterBuilder chartParameter) {
    if (this.chartParameterList == null) {
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
  public ChartTooltipBuilder setChartParameterList(List<ChartParameterBuilder> chartParameterList) throws AWException {
    if (chartParameterList != null) {
      this.chartParameterList = chartParameterList;
    } else {
      throw new AWException("The given parameter list cannot be null.");
    }
    return this;
  }

}
