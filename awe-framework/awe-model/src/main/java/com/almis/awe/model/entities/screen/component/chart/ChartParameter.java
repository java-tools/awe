package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.ChartParameterType;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

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
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("chart-parameter")
public class ChartParameter extends AbstractChart {

  private static final long serialVersionUID = -7098280839924260785L;

  // Parameter name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  // Parameter value
  @JsonIgnore
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  @Override
  public ChartParameter copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
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
