package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.ChartParameterType;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ChartParameter Class
 * <p>
 * Used to parse a chart parameter or parameter list with XStream
 * <p>
 * <p>
 * Generates an Chart widget
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
   * @param model Map with controller attributes of parent
   */
  public void addParameterModel(Map<String, Object> model) {
    if (model != null) {
      model.put(getName(), getParameterValue(model));
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
   * @param model Parent node
   * @return Parameter element string
   */
  @JsonValue
  public Object getParameterValue(Map<String, Object> model) {
    // Add dependency element value
    if (this.getType() != null) {
      switch (ChartParameterType.valueOf(getType().toUpperCase())) {
        case BOOLEAN:
          return Boolean.parseBoolean(getValue());
        case INTEGER:
          return Integer.valueOf(getValue());
        case LONG:
          return Long.valueOf(getValue());
        case FLOAT:
          return Float.valueOf(getValue());
        case DOUBLE:
          return Double.valueOf(getValue());
        case ARRAY:
          return getParameterArray(model);
        case OBJECT:
          return getParameterObject(model);
        case NULL:
          return null;
        case STRING:
        default:
          return getValue();
      }
    }
    return getValue();
  }

  /**
   * Returns the chart parameter list for a json arrayNode
   *
   * @param model Json node parent
   * @return ArrayNode
   */
  public List<Object> getParameterArray(Map<String, Object> model) {
    // Get array node if it has already the parameter
    List modelList = (List) (model.containsKey(getName()) ? model.get(getName()) : new ArrayList<>());

    // Get chart parameter list
    List<ChartParameter> parameterList = getElementList();
    for (ChartParameter parameter : parameterList) {
      modelList.add(parameter.getParameterValue(model));
    }
    return modelList;
  }

  /**
   * Returns the chart parameter list for a json ObjectNode
   *
   * @param model Json node parent
   * @return ObjectNode
   */
  public Map<String, Object> getParameterObject(Map<String, Object> model) {
    // Get array node if it has already the parameter
    Map<String, Object> objectMap = (Map<String, Object>) (model.containsKey(this.getName()) ? model.get(this.getName()) : new HashMap<>());

    // Get elements (columns)
    List<ChartParameter> parameterList = getElementList();
    for (ChartParameter parameter : parameterList) {
      objectMap.put(parameter.getName(), parameter.getParameterValue(objectMap));
    }

    return objectMap;
  }
}
