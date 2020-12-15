package com.almis.awe.model.util.data;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.model.dto.SortColumn;
import com.almis.awe.model.entities.maintain.MaintainQuery;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.entities.queries.Variable;
import com.almis.awe.model.type.ParameterType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;

/**
 * Abstract query builder
 */
public class QueryUtil extends ServiceConfig {

  @Value("${application.data.rowsPerPage:30}")
  private Long rowsPerPage;

  /**
   * Generate sort list
   *
   * @param sortArrayNode Sort parameter
   * @return Query parameter map
   */
  public List<SortColumn> getSortList(ArrayNode sortArrayNode) {
    List<SortColumn> sortList = new ArrayList<>();

    // If there are sort nodes, generate sortList
    for (JsonNode sortNode : sortArrayNode) {
      sortList.add(new SortColumn((ObjectNode) sortNode));
    }

    return sortList;
  }

  /**
   * Prepare query variables if not defined previously
   *
   * @param parameters Parameter list
   * @return Query parameter map
   * @throws AWException Error generating variables
   */
  public Map<String, QueryParameter> getDefaultVariableMap(ObjectNode parameters) throws AWException {
    Map<String, QueryParameter> variableMap = new HashMap<>();

    // Add sort variable
    JsonNode sortParameter = getRequestParameter(AweConstants.COMPONENT_SORT, parameters);
    if (sortParameter != null && sortParameter.isArray()) {
      ArrayNode sortList = (ArrayNode) sortParameter;
      variableMap.put(AweConstants.QUERY_SORT, new QueryParameter(sortList, true, ParameterType.OBJECT));
    }

    // Add page variable
    JsonNode pageParameter = getRequestParameter(AweConstants.COMPONENT_PAGE, parameters);
    if (pageParameter != null) {
      variableMap.put(AweConstants.QUERY_PAGE, new QueryParameter(pageParameter, false, ParameterType.LONG));
    } else {
      variableMap.put(AweConstants.QUERY_PAGE, new QueryParameter(JsonNodeFactory.instance.numberNode(1), false, ParameterType.LONG));
    }

    // Add max variable
    JsonNode maxParameter = getRequestParameter(AweConstants.COMPONENT_MAX, parameters);
    if (maxParameter != null && !maxParameter.isNull()) {
      variableMap.put(AweConstants.QUERY_MAX, new QueryParameter(maxParameter, false, ParameterType.LONG));
    } else {
      variableMap.put(AweConstants.QUERY_MAX, new QueryParameter(JsonNodeFactory.instance.numberNode(rowsPerPage), false, ParameterType.LONG));
    }

    // Add database variable
    JsonNode aliasParameter = getRequestParameter(AweConstants.COMPONENT_DATABASE, parameters);
    if (aliasParameter != null && !aliasParameter.isNull()) {
      variableMap.put(AweConstants.QUERY_DATABASE, new QueryParameter(aliasParameter, false, ParameterType.STRING));
    }

    return variableMap;
  }

  /**
   * Prepare query variables if not defined previously
   *
   * @param variables  Variable map
   * @param parameters Parameter list
   * @param index      Index
   * @return Query parameter map
   * @throws AWException Error generating variables
   */
  public Map<String, QueryParameter> getVariableMap(List<Variable> variables, ObjectNode parameters, Integer index) throws AWException {
    Map<String, QueryParameter> variableMap = getDefaultVariableMap(parameters);

    // Get defined variables
    for (Variable variable : variables) {
      if (index == null) {
        JsonNode value = getParameter(variable, parameters);
        boolean isList = variableIsList(variable, parameters);
        if (!variableMap.containsKey(variable.getId()) || allowVariable(variable, value)) {
          variableMap.put(variable.getId(), new QueryParameter(value, isList, ParameterType.valueOf(variable.getType())));
        }
      } else {
        JsonNode parameter = getParameter(variable, parameters);
        if (variableIsList(variable, parameters)) {
          ArrayNode parameterList = (ArrayNode) parameter;
          parameter = parameterList.get(index);
        }
        variableMap.put(variable.getId(), new QueryParameter(parameter, false, ParameterType.valueOf(variable.getType())));
      }
    }

    return variableMap;
  }

  /**
   * Check if add variable into variable map or not
   *
   * @param variable Variable
   * @param value    Json value
   * @return Add variable into variable map
   */
  private boolean allowVariable(Variable variable, JsonNode value) {
    switch (ParameterType.valueOf(variable.getType())) {
      case SYSTEM_DATE:
      case SYSTEM_TIME:
      case SYSTEM_TIMESTAMP:
        return true;
      default:
        return !variable.isOptional() || value != null && !value.isNull();
    }
  }

  /**
   * Prepare query variables if not defined previously
   *
   * @param query      Query
   * @param parameters Parameter list
   * @return Query parameter map
   * @throws AWException Error generating variables
   */
  public Map<String, QueryParameter> getVariableMap(Query query, ObjectNode parameters) throws AWException {
    return getVariableMap(query.getVariableDefinitionList() == null ? new ArrayList<>() : query.getVariableDefinitionList(), parameters, null);
  }

  /**
   * Prepare query variables if not defined previously
   *
   * @param variableMap Variable map
   * @param query       Query
   * @throws AWException Error generating variables
   */
  public void addToVariableMap(Map<String, QueryParameter> variableMap, Query query, ObjectNode parameters) throws AWException {
    Map<String, QueryParameter> queryParameterMap = getVariableMap(query, parameters);
    // Get defined variables
    for (Map.Entry<String, QueryParameter> entry : queryParameterMap.entrySet()) {
      if (!variableMap.containsKey(entry.getKey()) || (!isEmptyParameter(entry.getValue()))) {
        variableMap.put(entry.getKey(), entry.getValue());
      }
    }
  }

  /**
   * Prepare query variables if not defined previously
   *
   * @param dataList DataList
   */
  public void addDataListToRequestParameters(DataList dataList, ObjectNode parameters) {
    // Get defined variables
    List<String> columnList = DataListUtil.getColumnList(dataList);
    for (String columnId : columnList) {
      parameters.set(columnId, DataListUtil.getColumnAsArrayNode(dataList, columnId));
    }
  }

  /**
   * Get variable map for a single index without lists
   *
   * @param query      Query
   * @param parameters Parameter map
   * @return Query parameter map
   * @throws AWException Error generating variables
   */
  public Map<String, QueryParameter> getVariableMap(MaintainQuery query, ObjectNode parameters) throws AWException {
    // Ger variable map with parameter lists
    if (query.getVariableIndex() == null) {
      return getVariableMap((Query) query, null);
    } else {
      return getVariableMap(query.getVariableDefinitionList() == null ? new ArrayList<>() : query.getVariableDefinitionList(), parameters, query.getVariableIndex());
    }
  }

  /**
   * Prepare query variables if not defined previously
   *
   * @param query Query
   * @param page  Page number
   * @param max   Max elements per page
   * @return Query parameter map
   * @throws AWException Error generating variables
   */
  public Map<String, QueryParameter> getVariableMap(Query query, String page, String max) throws AWException {
    Map<String, QueryParameter> variableMap = getVariableMap(query, null);

    // Force page and max if not null
    return forcePageAndMax(variableMap, page, max);
  }

  /**
   * Prepare query variables if not defined previously
   *
   * @return Query parameter map
   */
  public ObjectNode getParameters() {
    return getRequest() != null ? getParameters(getRequest().getParametersSafe()) : JsonNodeFactory.instance.objectNode();
  }

  /**
   * Prepare query variables if not defined previously
   *
   * @param parameters Parameters
   * @return Query parameter map
   */
  public ObjectNode getParameters(ObjectNode parameters) {
    return parameters != null ? parameters : JsonNodeFactory.instance.objectNode();
  }

  /**
   * Prepare query variables if not defined previously
   *
   * @param alias Database alias
   * @param page  Page number
   * @param max   Max elements per page
   * @return Query parameter map
   */
  public ObjectNode getParameters(String alias, String page, String max) {
    // Force page and max if not null
    return getParameters(getParameters(), alias, page, max);
  }

  /**
   * Prepare query variables if not defined previously
   *
   * @param alias Database alias
   * @return Query parameter map
   */
  public ObjectNode getParameters(String alias) {
    // Force page and max if not null
    return getParameters(getParameters(), alias, null, null);
  }

  /**
   * Prepare query variables if not defined previously
   *
   * @param parameters Parameters
   * @param alias      Query alias
   * @param page       Page number
   * @param max        Max elements per page
   * @return Query parameter map
   */
  public ObjectNode getParameters(@NonNull ObjectNode parameters, String alias, String page, String max) {
    // Force page and max if not null
    return forceAliasPageAndMax(parameters, alias, page, max);
  }

  /**
   * Prepare query variables if not defined previously
   *
   * @param page Page number
   * @param max  Max elements per page
   * @return Query parameter map
   * @throws AWException Error generating variables
   */
  public Map<String, QueryParameter> getVariableMap(String page, String max) throws AWException {
    Map<String, QueryParameter> variableMap = getDefaultVariableMap(null);

    // Force page and max if not null
    return forcePageAndMax(variableMap, page, max);
  }

  /**
   * Prepare query variables if not defined previously
   *
   * @param variableMap Variable map
   * @param page        Page number
   * @param max         Max elements per page
   * @return Query parameter map
   */
  private Map<String, QueryParameter> forcePageAndMax(Map<String, QueryParameter> variableMap, String page, String max) {
    // Force page if not null
    if (page != null) {
      variableMap.put(AweConstants.QUERY_PAGE, new QueryParameter(JsonNodeFactory.instance.numberNode(Long.parseLong(page)), false, ParameterType.LONG));
    }

    // Force max if not null
    if (max != null) {
      variableMap.put(AweConstants.QUERY_MAX, new QueryParameter(JsonNodeFactory.instance.numberNode(Long.parseLong(max)), false, ParameterType.LONG));
    }

    return variableMap;
  }

  /**
   * Prepare query variables if not defined previously
   *
   * @param parameters Parameters
   * @param alias      Database alias
   * @param page       Page number
   * @param max        Max elements per page
   * @return Query parameter map
   */
  private ObjectNode forceAliasPageAndMax(ObjectNode parameters, String alias, String page, String max) {
    ObjectNode forcedParameters = parameters.deepCopy();
    // Force alias if not null
    if (alias != null) {
      forcedParameters.set(AweConstants.COMPONENT_DATABASE, JsonNodeFactory.instance.textNode(alias));
    }

    // Force page if not null
    if (page != null) {
      forcedParameters.set(AweConstants.COMPONENT_PAGE, JsonNodeFactory.instance.numberNode(Long.parseLong(page)));
    }

    // Force max if not null
    if (max != null) {
      forcedParameters.set(AweConstants.COMPONENT_MAX, JsonNodeFactory.instance.numberNode(Long.parseLong(max)));
    }

    return forcedParameters;
  }

  /**
   * Retrieve parameter
   *
   * @param variable   Variable
   * @param parameters Parameters
   * @return Parameter
   * @throws AWException Error retrieving variable value
   */
  public JsonNode getParameter(Variable variable, ObjectNode parameters) throws AWException {
    // Variable definition
    JsonNode parameter = variable.getName() != null ? getRequestParameter(variable.getName(), parameters) : null;
    boolean parseList = false;

    // Check value as static
    String stringParameter;
    if (variable.getSession() != null) {
      stringParameter = (String) getSession().getParameter(variable.getSession());
    } else if (variable.getProperty() != null) {
      stringParameter = getElements().getProperty(variable.getProperty());
    } else {
      stringParameter = variable.getValue();
    }

    // Check if string parameter is a list
    parseList = stringParameter != null && stringParameter.contains(",");

    // Retrieve parameter
    return getParameter(stringParameter, parameter, ParameterType.valueOf(variable.getType()), variable.getId(), parseList);
  }

  /**
   * Retrieve parameter as Json
   *
   * @param stringValue String value
   * @param parameter   Parameter
   * @param type        Type
   * @param variableId  Variable id
   * @param parseList   Try to parse parameter as list
   * @return Parameter as JSON
   * @throws AWException Error retrieving variable value
   */
  public JsonNode getParameter(String stringValue, JsonNode parameter, ParameterType type, String variableId, boolean parseList) throws AWException {
    JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    // Retrieve string value as list
    if (parseList) {
      String[] values = StringUtils.split(stringValue, ",");
      ArrayNode valueList = nodeFactory.arrayNode();
      for (String value : values) {
        valueList.add(value.trim());
      }
      parameter = valueList;
    }

    // If parameter is not json, generate it
    switch (type) {
      case DOUBLE:
      case FLOAT:
      case INTEGER:
      case LONG:
        parameter = getNumberParameter(parameter, stringValue, variableId, type);
        break;
      case OBJECT:
        parameter = getObjectParameter(parameter, stringValue);
        break;
      case DATE:
      case TIME:
      case TIMESTAMP:

      case STRINGN:
        parameter = getStringWithNullsParameter(parameter, stringValue);
        break;
      case SYSTEM_DATE:
      case SYSTEM_TIME:
      case SYSTEM_TIMESTAMP:
      case NULL:
        parameter = nodeFactory.nullNode();
        break;
      case STRING:
      case STRINGB:
      case STRINGL:
      case STRINGR:
      case STRING_ENCRYPT:
      case STRING_HASH_RIPEMD160:
      case STRING_HASH_PBKDF_2_W_HMAC_SHA_1:
      case STRING_HASH_SHA:
      default:
        parameter = getStringParameter(parameter, stringValue);
    }

    // Retrieve Json node
    return parameter;
  }

  private JsonNode getNumberParameter(JsonNode parameter, String stringParameter, String variableId, ParameterType type) {
    JsonNode output = parameter;
    if (parameter == null) {
      output = generateNumberNode(variableId, stringParameter, type);
    } else if (!parameter.isNumber() && !parameter.isArray() && parameter.isTextual()) {
      output = generateNumberNode(variableId, parameter.asText(), type);
    }
    return output;
  }

  private JsonNode getObjectParameter(JsonNode parameter, String stringParameter) throws AWException {
    JsonNode output = parameter;
    if (parameter == null && stringParameter != null) {
      try {
        output = new ObjectMapper().reader().readTree(stringParameter);
      } catch (IOException exc) {
        throw new AWException(getLocale("ERROR_MESSAGE_PARSING_OBJECT", stringParameter), exc);
      }
    }
    return output;
  }

  private JsonNode getStringWithNullsParameter(JsonNode parameter, String stringParameter) {
    JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
    JsonNode output = parameter;
    if (parameter == null) {
      if (stringParameter == null) {
        output = nodeFactory.nullNode();
      } else {
        output = nodeFactory.textNode(stringParameter);
      }
    }
    return output;
  }

  private JsonNode getStringParameter(JsonNode parameter, String stringParameter) {
    JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
    JsonNode output = parameter;
    if (parameter == null) {
      if (stringParameter == null) {
        output = nodeFactory.textNode("");
      } else {
        output = nodeFactory.textNode(stringParameter);
      }
    } else if (parameter.isNull()) {
      output = nodeFactory.textNode("");
    }
    return output;
  }

  /**
   * Retrieve parameter from request
   *
   * @param name       Parameter name
   * @param parameters Parameters list
   * @return Parameter
   * @throws AWException Error retrieving variable value
   */
  public JsonNode getRequestParameter(String name, ObjectNode parameters) throws AWException {
    if (parameters == null) {
      try {
        if (getRequest() == null) {
          return null;
        }
        // Retrieve Json node
        return getRequest().getParameter(name);
      } catch (Exception exc) {
        throw new AWException(getLocale("ERROR_TITLE_RETRIEVING_ELEMENT_PARAMETERS"), getLocale("ERROR_MESSAGE_READING_PARAMETER", name));
      }
    } else {
      return parameters.get(name);
    }

  }

  /**
   * Finds out if a variable value is a list
   *
   * @param variable   Variable
   * @param parameters Parameters
   * @return isList
   * @throws AWException AWE exception
   */
  public boolean variableIsList(@NonNull Variable variable, ObjectNode parameters) throws AWException {
    boolean list = false;

    if (variable.getName() != null) {
      JsonNode nodeValue = getRequestParameter(variable.getName(), parameters);
      list = nodeValue != null && nodeValue.isArray() && nodeValue instanceof ArrayNode;
    } else if (variable.getValue() != null) {
      list = variable.getValue().contains(",");
    }
    list = ParameterType.MULTIPLE_SEQUENCE.equals(ParameterType.valueOf(variable.getType())) || list;

    return list;
  }

  /**
   * Generate a number node from string
   *
   * @param value Value
   * @param type  Type
   * @return Number node
   */
  private JsonNode generateNumberNode(String name, String value, ParameterType type) {
    JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
    JsonNode parameter;

    if (value == null) {
      parameter = nodeFactory.nullNode();
    } else {
      try {
        switch (type) {
          case DOUBLE:
            parameter = nodeFactory.numberNode(Double.parseDouble(value));
            break;
          case FLOAT:
            parameter = nodeFactory.numberNode(Float.parseFloat(value));
            break;
          case LONG:
            parameter = nodeFactory.numberNode(Long.parseLong(value));
            break;
          case INTEGER:
          default:
            parameter = nodeFactory.numberNode(Integer.parseInt(value));
            break;
        }
      } catch (NumberFormatException exc) {
        // If a parameter is an unparseable number, set it to NULL
        getLogger().log(QueryUtil.class, Level.INFO, getLocale("INFO_MESSAGE_PARSING_NUMBER_NULL", name, value));
        parameter = nodeFactory.nullNode();
      }
    }
    return parameter;
  }

  /**
   * Check if a string variable is empty or not
   *
   * @param value String value
   * @return String is null or empty
   */
  public boolean isEmptyString(String value) {
    return isNullString(value) || value.isEmpty();
  }

  /**
   * Check if a string variable is null or not
   *
   * @param value String value
   * @return String is null or empty
   */
  public boolean isNullString(String value) {
    return value == null;
  }

  /**
   * Check if a jsonnode variable is empty or not
   *
   * @param variable Variable as json
   * @return Variable is empty
   */
  public boolean isEmptyVariable(JsonNode variable) {
    return isNullVariable(variable) || variable.asText().isEmpty();
  }

  /**
   * Check if a jsonnode variable is null or not
   *
   * @param variable Variable as json
   * @return Variable is null
   */
  public boolean isNullVariable(JsonNode variable) {
    return variable == null || variable.isNull();
  }

  /**
   * Check if a parameter variable is empty or not
   *
   * @param parameter Variable as json
   * @return Variable is empty
   */
  public boolean isEmptyParameter(QueryParameter parameter) {
    return parameter == null || isEmptyVariable(parameter.getValue());
  }

  /**
   * Retrieve database alias
   *
   * @param parameters Query parameters
   * @return Database alias
   */
  public String getDatabaseAlias(Map<String, QueryParameter> parameters) {
    // Alias is current database
    String alias = null;

    // Check if call refers to a specific database
    if (parameters != null && parameters.get(AweConstants.QUERY_DATABASE) != null && parameters.get(AweConstants.QUERY_DATABASE).getValue() != null) {
      alias = parameters.get(AweConstants.QUERY_DATABASE).getValue().asText();
    }

    // Retrieve alias
    return alias;
  }

  /**
   * Retrieve full sql statement as string
   *
   * @param sql        SQL Statement
   * @param parameters Parameter list
   * @return SQL statement
   */
  public String getFullSQL(String sql, List<Object> parameters) {
    return parameters
      .stream()
      .map(this::formatParameter)
      .reduce(sql, (fixed, binding) -> fixed.replaceFirst("\\?", Matcher.quoteReplacement(binding)));
  }

  /**
   * Format log parameters
   * @param binding Binding to format
   * @return Formatted parameter
   */
  private String formatParameter(Object binding) {
    if (binding instanceof String) {
      return MessageFormat.format("''{0}''", StringUtil.shortenText((String) binding, 25, "..."));
    } else if (binding instanceof Date) {
      return MessageFormat.format("(timestamp ''{0}'')", DateUtil.dat2SqlTimeString((Date) binding));
    }
    return binding.toString();
  }
}
