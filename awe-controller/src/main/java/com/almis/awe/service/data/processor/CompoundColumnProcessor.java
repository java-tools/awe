/*
 * Package definition
 */
package com.almis.awe.service.data.processor;

import java.util.HashMap;
import java.util.Map;

import com.almis.awe.model.component.AweContextAware;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.queries.Compound;
import com.almis.awe.model.entities.queries.Computed;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
   * @return
   */
  public CompoundColumnProcessor setElements(AweElements elements) {
    this.elements = elements;
    return this;
  }

  /**
   * Retrieve Awe Elements
   * 
   * @return
   */
  private AweElements getElements() throws AWException {
    if (elements == null) {
      throw new AWException("No elements defined", "Define elements before building the compound processor");
    }
    return elements;
  }

  /**
   * Set variable map
   * 
   * @param variableMap
   * @return
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
          computedMap = new HashMap<String, ComputedColumnProcessor>();
        }
        computedMap.put(computed.getAlias(), computedProcessor);
      }
    }
    return this;
  }

  /**
   * Retrieve column identifier
   * @return
   */
  public String getColumnIdentifier() {
    return compound.getAlias();
  }

  /**
   * Process row
   * @param row
   */
  public CellData process(Map<String, CellData> row) throws AWException {
    CellData compoundCell = new CellData();
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode compoundData = JsonNodeFactory.instance.objectNode();
    if (compound.getComputedList() != null) {
      for (Computed computed : compound.getComputedList()) {

        // Computed alias
        String computedIdentifier = computed.getAlias();

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
