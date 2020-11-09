package com.almis.awe.model.component;

import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.util.data.DateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.almis.awe.model.constant.AweConstants.SESSION_CONNECTION_HEADER;

/**
 * @author pgarcia
 */
@Data
@Accessors(chain = true)
public class AweRequest {

  // Application encoding
  @Value("${application.encoding:UTF-8}")
  private String applicationEncoding;

  // Target action
  private String targetAction = null;

  // Connection token
  private String token = null;

  // Parameters
  private ObjectNode parameters = null;

  // Request
  private final HttpServletRequest httpRequest;
  private final HttpServletResponse httpResponse;

  /**
   * Autowired constructor
   *
   * @param request  Request
   * @param response Response
   */
  public AweRequest(HttpServletRequest request, HttpServletResponse response) {
    this.httpRequest = request;
    this.httpResponse = response;

    // Set token
    setToken(request.getHeader(SESSION_CONNECTION_HEADER));
  }

  /**
   * Stores the specific value of a parameter in the internal parameter list
   *
   * @param name      Parameter name
   * @param valueList Parameter value
   */
  public void setParameter(@NotNull String name, Object... valueList) {
    getParameterList().set(name, getParameterValueFromList(valueList));
  }

  /**
   * Retrieve parameter value from list
   *
   * @param valueList Value list
   * @return Parameter value
   */
  private JsonNode getParameterValueFromList(Object... valueList) {
    if (valueList == null) {
      return getParameterValue(null);
    } else if (valueList.length > 1) {
      ArrayNode nodeList = JsonNodeFactory.instance.arrayNode();
      for (Object value : valueList) {
        nodeList.add(getParameterValue(value));
      }
      return nodeList;
    } else {
      return getParameterValue(valueList[0]);
    }
  }

  /**
   * Retrieve any parameter as JsonNode
   *
   * @param value Value
   * @return JsonNode of value
   */
  private JsonNode getParameterValue(Object value) {
    JsonNodeFactory factory = JsonNodeFactory.instance;
    ObjectMapper mapper = new ObjectMapper();
    if (value instanceof Date) {
      return factory.textNode(DateUtil.dat2WebTimestamp((Date) value));
    } else {
      return mapper.convertValue(value, JsonNode.class);
    }
  }

  /**
   * Store the parameter list
   *
   * @param parameterList the parameterList to set
   */
  public void setParameterList(ObjectNode parameterList) {
    if (parameterList != null) {
      getParameterList().setAll(parameterList);
    }
  }

  /**
   * Retrieve the parameter
   *
   * @param parameterId Parameter identifier
   * @return Parameter as json
   */
  public JsonNode getParameter(String parameterId) {
    return getParameterList().get(parameterId);
  }

  /**
   * Retrieve the parameter list
   *
   * @return parameter list
   */
  public ObjectNode getParameterList() {
    if (parameters == null) {
      parameters = JsonNodeFactory.instance.objectNode();
    }
    return parameters;
  }

  /**
   * Retrieve the parameter list to be modified without thread issues
   *
   * @return parameter list
   */
  public ObjectNode getParametersSafe() {
    return getParameterList().deepCopy();
  }


  /**
   * Retrieve the parameter
   *
   * @param parameterId Parameter identifier
   * @return Parameter as string
   */
  public String getParameterAsString(String parameterId) {
    String parameterValue = null;
    JsonNode parameter = getParameter(parameterId);
    if (parameter != null && !parameter.isNull()) {
      parameterValue = parameter.asText();
    }
    return parameterValue;
  }

  /**
   * Retrieve the parameter
   *
   * @param parameterId Parameter identifier
   * @return Parameter as cell data list
   */
  public List<CellData> getParameterAsCellDataList(String parameterId) {
    return getParameterAsCellDataList(getParameter(parameterId));
  }

  /**
   * Retrieve the parameter
   *
   * @param parameter Parameter
   * @return Parameter as cell data list
   */
  public List<CellData> getParameterAsCellDataList(JsonNode parameter) {
    List<CellData> parameterValueList = new ArrayList<>();
    if (parameter != null) {
      if (parameter.isArray()) {
        ArrayNode parameterList = (ArrayNode) parameter;
        for (JsonNode node : parameterList) {
          parameterValueList.add(getParameterAsCellData(node));
        }
      } else {
        parameterValueList.add(getParameterAsCellData(parameter));
      }
    }
    return parameterValueList;
  }

  /**
   * Retrieve the parameter
   *
   * @param parameterId Parameter identifier
   * @return Parameter as celldata
   */
  public CellData getParameterAsCellData(String parameterId) {
    return getParameterAsCellData(getParameter(parameterId));
  }

  /**
   * Retrieve the parameter as CellData
   *
   * @param parameter Parameter
   * @return Parameter as celldata
   */
  public CellData getParameterAsCellData(JsonNode parameter) {
    CellData parameterValue;
    if (parameter.isNull()) {
      parameterValue = new CellData();
    } else if (parameter.isInt()) {
      parameterValue = new CellData(parameter.asInt());
    } else if (parameter.isLong()) {
      parameterValue = new CellData(parameter.asLong());
    } else if (parameter.isFloat()) {
      parameterValue = new CellData(parameter.floatValue());
    } else if (parameter.isDouble()) {
      parameterValue = new CellData(parameter.doubleValue());
    } else if (parameter.isBoolean()) {
      parameterValue = new CellData(parameter.booleanValue());
    } else if (parameter.isTextual()) {
      parameterValue = new CellData(parameter.asText());
    } else {
      parameterValue = new CellData(parameter);
    }
    return parameterValue;
  }

  /**
   * Retrieve the CellData parameter as jsonnode
   *
   * @param cellData Parameter
   * @return Parameter as jsonNode
   */
  public JsonNode getCellDataAsParameter(CellData cellData) {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(cellData, JsonNode.class);
  }
}
