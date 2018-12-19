package com.almis.awe.model.component;

import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.type.AcceptedParameterType;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author pgarcia
 */
public class AweRequest {

  // Autowired services
  private Environment environment;
  private LogUtil logger;

  // Application encoding
  @Value("${application.encoding:UTF-8}")
  private String applicationEncoding;

  // Json encryption active
  @Value("${security.json.encryption:0}")
  private String jsonEncryptionActive;

  // Target action
  private String targetAction = null;

  // Parameters
  private ObjectNode parameters = null;

  /**
   * Autowired constructor
   * @param environment Environment
   * @param logger Logger
   */
  @Autowired
  public AweRequest(Environment environment, LogUtil logger) {
    this.environment = environment;
    this.logger = logger;
  }

  /**
   * Initialize parameters
   *
   * @param request Servlet request
   */
  public void init(HttpServletRequest request) {
    // Read parameters
    readRequestParameters(request);
  }

  /**
   * Initialize parameters with targetId
   *
   * @param targetId Action target
   * @param request  Servlet request
   */
  public void init(String targetId, HttpServletRequest request) {
    // Set target action
    setTargetAction(targetId);

    // Read parameters
    readRequestParameters(request);
  }

  /**
   * Initialize without parameters
   */
  public void init() {
    // Read parameters
    setParameterList(JsonNodeFactory.instance.objectNode());
  }

  /**
   * Initialize parameters
   *
   * @param parameters Request parameters
   */
  public void init(String parameters) {
    // Read parameters
    setParameterList(parameters);
  }

  /**
   * Initialize parameters with targetId
   *
   * @param targetId   Action target
   * @param parameters Request parameters
   */
  public void init(String targetId, String parameters) {
    // Set target action
    setTargetAction(targetId);

    // Read parameters
    setParameterList(parameters);
  }

  /**
   * Initialize parameters
   *
   * @param parameters Request parameters
   */
  public void init(ObjectNode parameters) {
    // Read parameters
    setParameterList(parameters);
  }

  /**
   * Read request parameters
   *
   * @param request Servlet request
   */
  private void readRequestParameters(HttpServletRequest request) {

    // Set character encoding
    try {
      request.setCharacterEncoding(applicationEncoding);
    } catch (UnsupportedEncodingException exc) {
      logger.log(AweRequest.class, Level.ERROR, "[awe Parameters] Unsupported encoding - {0}", exc, applicationEncoding);
    }

    // Get request parameter map
    Map<String, String[]> requestParameterMap = request.getParameterMap();
    ObjectNode acceptedParameters = JsonNodeFactory.instance.objectNode();

    // Read Accepted Parameter List
    for (AcceptedParameterType par : AcceptedParameterType.values()) {
      String strPar = environment.getProperty(par.toString());
      if (requestParameterMap.containsKey(strPar)) {
        acceptedParameters.put(strPar, request.getParameter(strPar));
      }
    }

    // Read JSON parameter list
    String parameterKey = environment.getProperty(AcceptedParameterType.PARAMETERS.toString());
    if (requestParameterMap.containsKey(parameterKey)) {
      // If communications are encoded, decode the parameterList
      String parameterList = acceptedParameters.get(parameterKey).asText();
      if (parameterList != null) {
        setParameterList(parameterList);
      }

      // Remove from accepted parameters
      acceptedParameters.remove(parameterKey);
    }

    // Put all decoded parameters
    setParameterList(acceptedParameters);
  }

  /**
   * Store the parameter list
   *
   * @param parameterList the parameterList to set
   */
  public void setParameterList(String parameterList) {
    // Action call
    ObjectMapper mapper = new ObjectMapper();
    try {
      // Decode the parameter list
      String decodedParameters = EncodeUtil.decodeTransmission(parameterList, "1".equalsIgnoreCase(jsonEncryptionActive));

      // Convert the parameter list string into a hashmap
      setParameterList((ObjectNode) mapper.reader().readTree(decodedParameters));
    } catch (Exception exc) {
      logger.log(AweRequest.class, Level.ERROR, "[Parse Parameters] Error parsing parameter list - {0}", exc, parameterList);
    }
  }

  /**
   * Stores the specific value of a parameter list in the internal parameter list
   *
   * @param name  Parameter name
   * @param valueList Parameter value
   */
  public void setParameter(String name, List<CellData> valueList) {
    ArrayNode nodeList = JsonNodeFactory.instance.arrayNode();
    if (valueList != null) {
      for (CellData value : valueList) {
        nodeList.add(getCellDataAsParameter(value));
      }
      setParameter(name, nodeList);
    }
  }

  /**
   * Stores the specific value of a parameter list in the internal parameter list
   *
   * @param name  Parameter name
   * @param valueList Parameter value
   */
  public void setParameter(String name, String... valueList) {
    ArrayNode nodeList = JsonNodeFactory.instance.arrayNode();
    if (valueList != null) {
      for (String value : valueList) {
        nodeList.add(value);
      }
      setParameter(name, nodeList);
    }
  }

  /**
   * Stores the specific value of a parameter in the internal parameter list
   *
   * @param name  Parameter name
   * @param value Parameter value
   */
  public void setParameter(String name, JsonNode value) {
    if (value != null) {
      getParameterList().set(name, value);
    }
  }

  /**
   * Stores the specific value of a parameter in the internal parameter list
   *
   * @param name  Parameter name
   * @param value Parameter value
   */
  public void setParameter(String name, String value) {
    if (value != null) {
      getParameterList().put(name, value);
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
    if (parameter != null) {
      parameterValue = parameter.textValue();
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

  /**
   * @return the targetAction
   */
  public String getTargetAction() {
    return targetAction;
  }

  /**
   * @param targetAction the targetAction to set
   */
  public void setTargetAction(String targetAction) {
    this.targetAction = targetAction;
  }
}
