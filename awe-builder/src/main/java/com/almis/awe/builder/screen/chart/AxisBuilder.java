/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.chart;

import com.almis.awe.builder.enumerates.AxisDataType;
import com.almis.awe.builder.enumerates.FormatterFunction;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.chart.ChartAxis;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class AxisBuilder extends AweBuilder<AxisBuilder>{
  
  private Boolean allowDecimal;
  private Boolean opposite;
  private String label;
  private String labelFormat;
  private String tickInterval;
  private Float labelRotation;
  private FormatterFunction formatterFunction;
  private AxisDataType type;
  private List<ChartParameterBuilder> chartParameterList;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public AxisBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    chartParameterList = new ArrayList<>();
  }

  @Override
  public AxisBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    ChartAxis axis = new ChartAxis();
    
    axis.setId(getId());
    
    if(isAllowDecimal() != null){
      axis.setAllowDecimal(String.valueOf(isAllowDecimal()));
    }
    
    if (isOpposite()!= null){
      axis.setOpposite(String.valueOf(isOpposite()));
    }
    
    if (getLabel() != null){
      axis.setLabel(getLabel());
    }
    
    if (getLabelFormat() != null){
      axis.setLabelFormat(getLabelFormat());
    }
    
    if (getTickInterval() != null){
      axis.setTickInterval(getTickInterval());
    } 
    
    if (getLabelRotation() != null){
      axis.setLabelRotation(String.valueOf(getLabelRotation()));
    }
    
    if (getFormatterFunction() != null){
      axis.setFormatterFunction(getFormatterFunction().toString());
    }
    
    if (getType() != null){
      axis.setType(getType().toString());
    }
    
    for(ChartParameterBuilder chartParameterBuilder : chartParameterList){
      addElement(axis, chartParameterBuilder.build(axis));
    }
    
    return axis;
  }

  /**
   * Is allow decimal
   *
   * @return
   */
  public Boolean isAllowDecimal() {
    return allowDecimal;
  }

  /**
   * Set allow decimal
   *
   * @param allowDecimal
   * @return
   */
  public AxisBuilder setAllowDecimal(Boolean allowDecimal) {
    this.allowDecimal = allowDecimal;
    return this;
  }

  /**
   * Is opposite
   *
   * @return
   */
  public Boolean isOpposite() {
    return opposite;
  }

  /**
   * Set opposite
   *
   * @param opposite
   * @return
   */
  public AxisBuilder setOpposite(Boolean opposite) {
    this.opposite = opposite;
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
  public AxisBuilder setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * Get label format
   *
   * @return
   */
  public String getLabelFormat() {
    return labelFormat;
  }

  /**
   * Set label
   *
   * @param labelFormat
   * @return
   */
  public AxisBuilder setLabelFormat(String labelFormat) {
    this.labelFormat = labelFormat;
    return this;
  }

  /**
   * Get Tick interval
   *
   * @return
   */
  public String getTickInterval() {
    return tickInterval;
  }

  /**
   * Set tick interval
   *
   * @param tickInterval
   * @return
   */
  public AxisBuilder setTickInterval(String tickInterval) {
    this.tickInterval = tickInterval;
    return this;
  }

  /**
   * Get label rotation
   *
   * @return
   */
  public Float getLabelRotation() {
    return labelRotation;
  }

  /**
   * Set label rotation
   *
   * @param labelRotation
   * @return
   */
  public AxisBuilder setLabelRotation(Float labelRotation) {
    this.labelRotation = labelRotation;
    return this;
  }

  /**
   * Get formatter function
   *
   * @return
   */
  public FormatterFunction getFormatterFunction() {
    return formatterFunction;
  }

  /**
   * Set formatter function
   *
   * @param formatterFunction
   * @return
   */
  public AxisBuilder setFormatterFunction(FormatterFunction formatterFunction) {
    this.formatterFunction = formatterFunction;
    return this;
  }

  /**
   * Get type
   *
   * @return
   */
  public AxisDataType getType() {
    return type;
  }

  /**
   * Set type
   *
   * @param type
   * @return
   */
  public AxisBuilder setType(AxisDataType type) {
    this.type = type;
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
  public AxisBuilder addChartParameter(ChartParameterBuilder chartParameter) {
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
  public AxisBuilder setChartParameterList(List<ChartParameterBuilder> chartParameterList) throws AWException {
    if( chartParameterList != null){
      this.chartParameterList = chartParameterList;
    }else{
      throw new AWException("The given parameter list cannot be null.");
    }
    return this;
  }

}
