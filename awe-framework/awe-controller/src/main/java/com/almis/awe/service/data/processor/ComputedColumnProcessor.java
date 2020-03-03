/*
 * Package definition
 */
package com.almis.awe.service.data.processor;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.model.entities.queries.Computed;
import com.almis.awe.model.type.ParameterType;
import com.almis.awe.model.util.data.StringUtil;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Computed column class
 */
public class ComputedColumnProcessor implements ColumnProcessor {
  private Computed computed;
  private Map<String, QueryParameter> variableMap = null;
  private TransformCellProcessor transformProcessor;
  private TranslateCellProcessor translateProcessor;
  private AweElements elements;
  private boolean emptyOnNull;
  private String expression = null;

  /**
   * Set computed
   *
   * @param computed Computed field
   * @return Computed processor
   * @throws AWException Error adding computed field
   */
  public ComputedColumnProcessor setComputed(Computed computed) throws AWException {
    this.computed = computed;

    // Calculate transform
    if (computed.isTransform()) {
      transformProcessor = new TransformCellProcessor()
        .setElements(getElements())
        .setField(computed);
    }

    // Calculate translate
    if (computed.isTranslate()) {
      translateProcessor = new TranslateCellProcessor()
        .setElements(getElements())
        .setField(computed);
    }

    // Generate format matcher
    expression = computed.getFormat();
    return this;
  }

  /**
   * Set variable map
   *
   * @param variableMap Variable map
   * @return Computed processor
   */
  public ComputedColumnProcessor setVariableMap(Map<String, QueryParameter> variableMap) {
    this.variableMap = variableMap;
    return this;
  }

  /**
   * Set Awe Elements
   *
   * @param elements AWE Elements
   * @return Computed processor
   */
  public ComputedColumnProcessor setElements(AweElements elements) {
    this.elements = elements;
    emptyOnNull = Boolean.valueOf(elements.getProperty(AweConstants.PROPERTY_EMPTY_IF_NULL, "true"));
    return this;
  }

  /**
   * Retrieve Awe Elements
   *
   * @return Awe elements
   */
  private AweElements getElements() throws AWException {
    if (elements == null) {
      throw new AWException("No elements defined", "Define elements before building the computed processor");
    }
    return elements;
  }

  /**
   * Add a variable (stringified)
   *
   * @param name  variable name
   * @param value variable value (as string)
   * @return DataListBuilder
   */
  public ComputedColumnProcessor addVariable(String name, String value) {
    if (variableMap == null) {
      setVariableMap(new HashMap<>());
    }
    QueryParameter parameter = new QueryParameter(JsonNodeFactory.instance.textNode(value), false, ParameterType.STRING);
    variableMap.put(name, parameter);
    return this;
  }

  /**
   * Retrieve column identifier
   *
   * @return Column identifier
   */
  public String getColumnIdentifier() {
    return computed.getAlias();
  }

  /**
   * Process row
   *
   * @param row Data row
   * @throws AWException AWE exception
   */
  public CellData process(Map<String, CellData> row) throws AWException {

    // Replace the expression with values
    String computedExpression = computeExpression(row, expression);

    // Evaluate the expression if eval type is defined
    CellData evaluatedExpression = evaluateExpression(computedExpression);

    // Calculate transform
    if (transformProcessor != null) {
      evaluatedExpression = transformProcessor.process(evaluatedExpression);
    }

    // Calculate translate
    if (translateProcessor != null) {
      evaluatedExpression = translateProcessor.process(evaluatedExpression);
    }

    // Store computed in row
    return evaluatedExpression;
  }

  /**
   * Replace the expression with row values
   *
   * @param row   Row values
   * @param value Expression
   * @return Expression replaced
   */
  private String computeExpression(Map<String, CellData> row, String value) {
    // Create the matcher
    String computedExpression = value;
    Matcher formatMatcher = AweConstants.DATALIST_COMPUTED_WILDCARD.matcher(StringUtil.fixHTMLValue(expression));

    // Replace all expression variables
    while (formatMatcher.find()) {
      for (Integer matchIndex = 1, total = formatMatcher.groupCount(); matchIndex <= total; matchIndex++) {
        String variableKey = formatMatcher.group(matchIndex);
        String variableValue = "";
        CellData cell = row.get(variableKey);

        // Check if fill with variable or cell
        if (variableMap != null && variableMap.containsKey(variableKey)) {
          // Fill with variable value
          variableValue = variableMap.get(variableKey).getValue().asText();
        } else if (cell != null && !cell.getStringValue().isEmpty()) {
          // Fill with cell value
          variableValue = cell.getStringValue();
        } else if (computed.getNullValue() != null) {
          // Fill with null value
          variableValue = computed.getNullValue();
        }

        // If variable value is empty, empty the computed value
        if (variableValue.isEmpty() && emptyOnNull) {
          computedExpression = "";
        } else {
          // Replace value
          computedExpression = computedExpression.replace("[" + variableKey + "]", variableValue);
        }
      }
    }

    return computedExpression;
  }

  /**
   * Evaluate expression
   *
   * @param value Value
   * @return Evaluated expression
   * @throws AWException Error parsing value
   */
  private CellData evaluateExpression(String value) throws AWException {
    CellData evaluatedExpression = new CellData();
    if (computed.isEval()) {
      Object evaluated;
      try {
        evaluated = StringUtil.eval(value, elements.getApplicationContext().getBean(ScriptEngine.class));
      } catch (ScriptException exc) {
        throw new AWException(elements.getLocaleWithLanguage("ERROR_TITLE_EXPRESSION_EVALUATION", elements.getLanguage()),
          elements.getLocaleWithLanguage("ERROR_MESSAGE_EXPRESSION_EVALUATION", elements.getLanguage(), value), exc);
      }
      if (evaluated == null) {
        evaluatedExpression.setNull();
      } else if (evaluated instanceof Integer) {
        evaluatedExpression.setValue(evaluated);
      } else if (evaluated instanceof Float) {
        evaluatedExpression.setValue(evaluated);
      } else if (evaluated instanceof Long) {
        evaluatedExpression.setValue(evaluated);
      } else if (evaluated instanceof Double) {
        evaluatedExpression.setValue(evaluated);
      } else {
        evaluatedExpression.setValue(String.valueOf(evaluated));
      }
    } else {
      evaluatedExpression.setValue(value);
    }

    return evaluatedExpression;
  }
}
