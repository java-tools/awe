/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.chart;

import com.almis.awe.builder.enumerates.DataType;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.chart.ChartParameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class ChartParameterBuilder extends AweBuilder<ChartParameterBuilder> {

  private DataType dataType;
  private String name;
  private String value;
  private List<ChartParameterBuilder> chartParameterList;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public ChartParameterBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    chartParameterList = new ArrayList<>();
  }

  @Override
  public ChartParameterBuilder setParent() {
    return this;
  }
  
  @Override
  public Element build(Element element) {
    ChartParameter chartParameter = new ChartParameter();
    
    chartParameter.setId(getId());
    
    if(getDataType() != null){
      chartParameter.setType(getDataType().toString());
    }
    
    if(getName() != null){
      chartParameter.setName(getName());
    }
    
    if(getValue() != null){
      chartParameter.setValue(getValue());
    }
    
    for (ChartParameterBuilder chartParameterBuilder : chartParameterList){
      addElement(chartParameter, chartParameterBuilder.build(chartParameter));
    } 
    
    return chartParameter;
  }

  /**
   *  Get data type
   *
   * @return
   */
  public DataType getDataType() {
    return dataType;
  }

  /**
   * Set data type
   *
   * @param dataType
   * @return
   */
  public ChartParameterBuilder setDataType(DataType dataType) {
    this.dataType = dataType;
    return this;
  }

  /**
   * Get name
   *
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * Set name
   *
   * @param name
   * @return
   */
  public ChartParameterBuilder setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get value
   *
   * @return
   */
  public String getValue() {
    return value;
  }

  /**
   * Set value
   *
   * @param value
   * @return
   */
  public ChartParameterBuilder setValue(String value) {
    this.value = value;
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
  public ChartParameterBuilder addChartParameter(ChartParameterBuilder... chartParameter) {
    if (chartParameter != null) {
      if (this.chartParameterList == null) {
        this.chartParameterList = new ArrayList<>();
      }
      this.chartParameterList.addAll(Arrays.asList(chartParameter));
    }
    return this;
  }
}
