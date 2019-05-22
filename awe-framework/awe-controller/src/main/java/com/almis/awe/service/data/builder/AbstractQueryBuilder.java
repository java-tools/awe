package com.almis.awe.service.data.builder;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.model.dto.SortColumn;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.util.data.QueryUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract query builder
 */
public abstract class AbstractQueryBuilder extends ServiceConfig implements QueryBuilder {

  // Autowired services
  protected QueryUtil queryUtil;

  protected ObjectNode parameters;
  protected Map<String, QueryParameter> variables;
  private Query query;
  private ComponentAddress address;
  protected Integer variableIndex;
  protected List<SortColumn> componentSortList;

  /**
   * Autowired constructor
   * @param queryUtil Query utilities
   */
  @Autowired
  public AbstractQueryBuilder(QueryUtil queryUtil) {
    this.queryUtil = queryUtil;
  }

  /**
   * Retrieve query
   *
   * @return Query
   */
  protected Query getQuery() {
    return query;
  }

  /**
   * Store query
   *
   * @param query Query
   * @return this
   */
  public QueryBuilder setQuery(Query query) {
    this.query = query;
    return this;
  }

  /**
   * Get component address
   *
   * @return Address
   */
  public ComponentAddress getAddress() {
    return address;
  }

  /**
   * Set component address
   *
   * @param address Address
   * @return this
   */
  public AbstractQueryBuilder setAddress(ComponentAddress address) {
    this.address = address;
    return this;
  }

  /**
   * Get index
   *
   * @return Variable index
   */
  public Integer getVariableIndex() {
    return this.variableIndex;
  }

  /**
   * Retrieve query generated variables
   *
   * @return Query parameters
   * @throws AWException Error retrieving query parameters
   */
  public Map<String, QueryParameter> getVariables() throws AWException {
    // Throws exception if query is not defined
    if (getQuery() == null) {
      throw new NullPointerException(getElements().getLocale("ERROR_TITLE_NOT_DEFINED", "query"));
    }

    // Generate variables
    if (variables == null) {
      variables = queryUtil.getVariableMap(getQuery(), getParameters());
    }

    // Get generated variables
    return variables;
  }

  /**
   * Set variables
   *
   * @param parameterMap Parameter map
   * @return this
   */
  public AbstractQueryBuilder setVariables(Map<String, QueryParameter> parameterMap) {
    if (variables == null) {
      variables = parameterMap;
    } else {
      parameterMap.putAll(variables);
      variables = parameterMap;
    }
    return this;
  }

  /**
   * Retrieve query generated variables
   *
   * @return Query parameters
   */
  public ObjectNode getParameters() {
    // Get generated variables
    return parameters;
  }

  /**
   * Set parameters
   *
   * @param parameters Parameter map
   * @return this
   */
  public AbstractQueryBuilder setParameters(ObjectNode parameters) {
    this.parameters = parameters;
    return this;
  }

  /**
   * Generate the sortlist from component sort
   *
   * @param sortList Component sort
   */
  protected void addComponentSort(ArrayNode sortList) {
    // Create list if it doesn't exist
    componentSortList = queryUtil.getSortList(sortList);
  }

  /**
   * Generic variable value call
   *
   * @param varValue Variable
   * @return Variable as string
   * @throws AWException Variable retrieval was wrong
   */
  protected String getVariableAsString(JsonNode varValue) throws AWException {
    String val;

    try {
      if (varValue == null || varValue.isNull()) {
        val = null;
      } else if (varValue.isValueNode()) {
        val = varValue.asText();
      } else if (varValue.isTextual()) {
        val = varValue.textValue();
      } else {
        val = varValue.toString();
      }
    } catch (Exception exc) {
      throw new AWException(getElements().getLocale("ERROR_TITLE_GENERATING_VARIABLE_VALUE"), getElements().getLocale("ERROR_MESSAGE_GENERATING_VARIABLE_VALUE", varValue.toString()), exc);
    }

    // Return variable value
    return val;
  }

  /**
   * Extracts all values inside of QueryParameters to use as arguments while calling the service
   *
   * @param variables query's variable map
   * @return map with values extracted
   */
  protected Map<String, Object> extractValuesFromParameters(Map<String, QueryParameter> variables) {
    Map<String, Object> extracted = new HashMap<>();

    for (Map.Entry<String, QueryParameter> entry : variables.entrySet()) {
      Object value = transformFromJsonNode(entry.getValue());
      extracted.put(entry.getKey(), value);
    }
    return extracted;
  }

  /**
   * Get parameter from JSON
   *
   * @param param Parameter
   * @return Object
   */
  protected Object transformFromJsonNode(QueryParameter param) {
    JsonNode jsonValue = param.getValue();
    if (jsonValue == null || jsonValue.isNull()) {
      return null;
    }

    // If is list, retrieve list
    if (jsonValue.isArray()) {
      List objectList = new ArrayList<>();
      for (JsonNode node : jsonValue) {
        objectList.add(getNodeValue(node, param));
      }
      return objectList;
    } else {
      // Retrieve as value
      return getNodeValue(jsonValue, param);
    }
  }

  /**
   * Get node value with the right class
   *
   * @param node      Node
   * @param parameter Parameter
   * @return Node value
   */
  private Object getNodeValue(JsonNode node, QueryParameter parameter) {
    if (node == null || node.isNull()) {
      return null;
    } else {
      if (node.isNumber()) {
        switch (parameter.getType()) {
          case INTEGER:
            return node.asInt();
          case LONG:
            return node.asLong();
          case FLOAT:
            return (float) node.asDouble();
          case DOUBLE:
            return node.asDouble();
          default:
            return node.asText();
        }
      } else {
        switch (parameter.getType()) {
          case BOOLEAN:
            return node.asBoolean();
          case OBJECT:
            return node;
          default:
            return node.asText();
        }
      }
    }
  }
}
