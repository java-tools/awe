package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.type.ChartParameterType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;

/**
 * ChartParameter Class
 *
 * Used to parse a chart parameter or parameter list with XStream
 *
 *
 * Generates an Chart widget
 *
 *
 * @author Pablo VIDAL - 21/OCT/2014
 */
@XStreamAlias("chart-parameter")
public class ChartParameter extends Element {

  private static final long serialVersionUID = -7098280839924260785L;

  // Parameter name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  // Parameter value
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  /**
   * Default constructor
   */
  public ChartParameter() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ChartParameter(ChartParameter other) throws AWException {
    super(other);
    this.name = other.name;
    this.value = other.value;
  }

  @Override
  public ChartParameter copy() throws AWException {
    return new ChartParameter(this);
  }

  /**
   * Returns the parameter name.
   *
   * @return Parameter value
   */
  public String getName() {
    return this.name;
  }

  /**
   * Stores the parameter value
   *
   * @param name Parameter name
   * @return ChartParameter
   */
  public ChartParameter setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Returns the parameter value.
   *
   * @return Parameter value
   */
  @JsonIgnore
  public String getValue() {
    return this.value;
  }

  /**
   * Stores the parameter value
   *
   * @param value Parameter value
   * @return ChartParameter
   */
  public ChartParameter setValue(String value) {
    this.value = value;
    return this;
  }

  /**
   * Add chart parameter to node parent
   *
   * @param parentAttributes Json object with controller attributes of parent
   */
  public void addParameterModel(ObjectNode parentAttributes) {
    if (parentAttributes != null) {
      parentAttributes.set(this.getName(), this.getParameterValue(parentAttributes));
    }
  }

  /**
   * Get chart parameter by name
   *
   * @param name Parameter name
   * @return Parameter name
   */
  public ChartParameter getChartParameterByName(String name) {
    ChartParameter parameterFound = null;
    // Get chart parameter list
    List<ChartParameter> parameterList = getElementList();
    for (ChartParameter parameter : parameterList) {
      if (name.equalsIgnoreCase(parameter.getName())) {
        parameterFound = parameter;
      }
    }
    return parameterFound;
  }

  /**
   * Returns the chart parameter node
   *
   * @param parentAttributes Json node parent
   * @return Parameter element string
   */
  @JsonValue
  public JsonNode getParameterValue(ObjectNode parentAttributes) {

    JsonNode node = JsonNodeFactory.instance.nullNode();

    // Add dependency element value
    if (this.getType() != null) {
      switch (ChartParameterType.valueOf(this.getType().toUpperCase())) {
        case STRING:
          node = JsonNodeFactory.instance.textNode(this.getValue());
          break;
        case BOOLEAN:
          node = JsonNodeFactory.instance.booleanNode(Boolean.parseBoolean(this.getValue()));
          break;
        case INTEGER:
          node = JsonNodeFactory.instance.numberNode(Integer.valueOf(this.getValue()));
          break;
        case LONG:
          node = JsonNodeFactory.instance.numberNode(Long.valueOf(this.getValue()));
          break;
        case FLOAT:
          node = JsonNodeFactory.instance.numberNode(Float.valueOf(this.getValue()));
          break;
        case DOUBLE:
          node = JsonNodeFactory.instance.numberNode(Double.valueOf(this.getValue()));
          break;
        case ARRAY:
          node = this.getParameterArray(parentAttributes);
          break;
        case OBJECT:
          node = this.getParameterObject(parentAttributes);
          break;
        case NULL:
        default:
          node = JsonNodeFactory.instance.nullNode();
          break;
      }
    }
    return node;
  }

  /**
   * Returns the chart parameter list for a json arrayNode
   *
   * @param parentAttributes Json node parent
   * @return ArrayNode
   */
  public ArrayNode getParameterArray(ObjectNode parentAttributes) {
    // Get array node if it has already the parameter
    ArrayNode arrayNode = (ArrayNode) (parentAttributes.has(this.getName()) ? parentAttributes.get(this.getName()) : JsonNodeFactory.instance.arrayNode());

    // Get chart parameter list
    List<ChartParameter> parameterList = getElementList();
    for (ChartParameter parameter : parameterList) {
      arrayNode.add(parameter.getParameterValue(parentAttributes));
    }
    return arrayNode;
  }

  /**
   * Returns the chart parameter list for a json ObjectNode
   *
   * @param parentAttributes Json node parent
   * @return ObjectNode
   */
  public ObjectNode getParameterObject(ObjectNode parentAttributes) {
    // Get array node if it has already the parameter
    ObjectNode objectNode = (ObjectNode) (parentAttributes.has(this.getName()) ? parentAttributes.get(this.getName()) : JsonNodeFactory.instance.objectNode());

    // Get elements (columns)
    List<ChartParameter> parameterList = getElementList();
    for (ChartParameter parameter : parameterList) {
      objectNode.set(parameter.getName(), parameter.getParameterValue(objectNode));
    }

    return objectNode;
  }
}
