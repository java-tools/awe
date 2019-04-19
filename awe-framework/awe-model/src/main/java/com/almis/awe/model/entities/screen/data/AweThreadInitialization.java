/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.model.entities.screen.data;

import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.type.LoadType;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author pgarcia
 */
public class AweThreadInitialization {
  private LoadType initialLoadType = LoadType.QUERY;
  private String target;
  private String columnId;
  private String componentId;
  private DataList initializationOutput;
  private ObjectNode parameters;

  /**
   * Get initial load type
   *
   * @return Initial load type
   */
  public LoadType getInitialLoadType() {
    return initialLoadType;
  }

  /**
   * Set initial load type
   *
   * @param initialLoadType Load type
   * @return Thread
   */
  public AweThreadInitialization setInitialLoadType(LoadType initialLoadType) {
    this.initialLoadType = initialLoadType;
    return this;
  }

  /**
   * Get target query/enumerated
   *
   * @return Target
   */
  public String getTarget() {
    return target;
  }

  /**
   * Set target query/enumerated
   *
   * @param target Target
   * @return Thread
   */
  public AweThreadInitialization setTarget(String target) {
    this.target = target;
    return this;
  }

  /**
   * Get component id
   *
   * @return Component id
   */
  public String getComponentId() {
    return componentId;
  }

  /**
   * Set component id
   *
   * @param componentId Component id
   * @return Thread
   */
  public AweThreadInitialization setComponentId(String componentId) {
    this.componentId = componentId;
    return this;
  }

  /**
   * Retrieve initialization output
   *
   * @return initialization output
   */
  public DataList getInitializationOutput() {
    return initializationOutput;
  }

  /**
   * Store initialization output
   *
   * @param initializationOutput initialization output
   * @return Thread
   */
  public AweThreadInitialization setInitializationOutput(DataList initializationOutput) {
    this.initializationOutput = initializationOutput;
    return this;
  }

  /**
   * Retrieve parameters
   *
   * @return parameters
   */
  public ObjectNode getParameters() {
    return parameters;
  }

  /**
   * Store parameters
   *
   * @param parameters Parameters
   * @return Thread
   */
  public AweThreadInitialization setParameters(ObjectNode parameters) {
    this.parameters = parameters;
    return this;
  }

  /**
   * Retrieve column id
   *
   * @return Column identifier
   */
  public String getColumnId() {
    return columnId;
  }

  /**
   * Store column id
   *
   * @param columnId Column identifier
   * @return Thread
   */
  public AweThreadInitialization setColumnId(String columnId) {
    this.columnId = columnId;
    return this;
  }
}
