/*
 * Package definition
 */
package com.almis.awe.service.data.processor;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweContextAware;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.model.entities.queries.Compound;
import com.almis.awe.model.entities.queries.Computed;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Computed column class
 */
public class CompoundColumnProcessor implements ColumnProcessor, AweContextAware {
  private Compound compound;
  private Map<String, QueryParameter> variableMap;
  private Map<String, ComputedColumnProcessor> computedMap;
  private AweElements elements;

  /**
   * Set Awe Elements
   * @param elements awe elements
   * @return compound column processor
   */
  public CompoundColumnProcessor setElements(AweElements elements) {
    this.elements = elements;
    return this;
  }

  /**
   * Set variable map
   * 
   * @param variableMap map with variable values
   * @return compound column processor
   */
  public CompoundColumnProcessor setVariableMap(Map<String, QueryParameter> variableMap) {
    this.variableMap = variableMap;
    return this;
  }

  /**
   * Set compound
   * @param compound Compound field
   * @return CompoundColumnProcessor
   * @throws AWException Error adding compound field
   */
  public CompoundColumnProcessor setCompound(Compound compound) throws AWException {
    this.compound = compound;
    if (compound.getComputedList() != null) {
      for (Computed computed : compound.getComputedList()) {

        // Calculate computed
        ComputedColumnProcessor computedProcessor = new ComputedColumnProcessor()
                .setElements(elements)
                .setComputed(computed);
        if (computedMap == null) {
          computedMap = new HashMap<>();
        }
        computedMap.put(computed.getIdentifier(), computedProcessor);
      }
    }
    return this;
  }

  /**
   * Retrieve column identifier
   * @return column identifier
   */
  public String getColumnIdentifier() {
    return compound.getIdentifier();
  }

  /**
   * Process row
   * @param row datalist row
   */
  public CellData process(Map<String, CellData> row) throws AWException {
    CellData compoundCell = new CellData();
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode compoundData = JsonNodeFactory.instance.objectNode();
    if (compound.getComputedList() != null) {
      for (Computed computed : compound.getComputedList()) {

        // Computed alias
        String computedIdentifier = computed.getIdentifier();

        // Calculate computed
        CellData computedData = computedMap.get(computedIdentifier)
                .setElements(elements)
                .setVariableMap(variableMap)
                .process(row);

        // Store computed data on compound
        JsonNode computedValue = mapper.valueToTree(computedData);
        compoundData.set(computedIdentifier, computedValue);
      }
    }
    compoundCell.setValue(compoundData);
    return compoundCell;
  }
}
