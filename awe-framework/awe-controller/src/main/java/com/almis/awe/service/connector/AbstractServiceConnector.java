package com.almis.awe.service.connector;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.services.ServiceInputParameter;
import com.almis.awe.model.type.ParameterType;
import com.almis.awe.model.util.data.DateUtil;

import java.util.*;

/**
 * Abstract class that contains general method implementations for all ServiceConnectors
 *
 * @author jbellon
 */
abstract class AbstractServiceConnector extends ServiceConfig implements ServiceConnector {

  /**
   * Extract parameters values
   *
   * @param paramsFromXml         XML parameters
   * @param paramsMapFromRequest  Map parameters
   * @param paramsToInvoke        Parameters to invoke
   * @param paramsClassesToInvoke Classes to invoke
   * @throws AWException Error extracting parameters
   */
  public void extractParameters(List<ServiceInputParameter> paramsFromXml, Map<String, Object> paramsMapFromRequest, Object[] paramsToInvoke, Class[] paramsClassesToInvoke) throws AWException {
    Integer iteratingParam = 0;
    String paramNameToInvoke = "";

    // Add parameters into the parameter object list
    for (ServiceInputParameter param : paramsFromXml) {
      if (param != null) {
        paramNameToInvoke = param.getName();
        if (param.isList()) {
          paramsToInvoke[iteratingParam] = getParameterValueList(param, paramsMapFromRequest);
          paramsClassesToInvoke[iteratingParam] = List.class;
        } else {
          Object paramValue = paramsMapFromRequest.get(paramNameToInvoke);
          paramsToInvoke[iteratingParam] = getParameterValue(param, paramValue);
          paramsClassesToInvoke[iteratingParam] = getParameterClass(param);
        }
        iteratingParam++;
      } else {
        throw new AWException(getLocale("ERROR_TITLE_WEB_SERVICE_CALL"),
          getLocale("ERROR_MESSAGE_PARAMETER_NOT_FOUND", paramNameToInvoke));
      }
    }
  }

  /**
   * Get parameter class
   * @param parameter Parameter
   */
  public Class getParameterClass(ServiceInputParameter parameter) {
    switch (ParameterType.valueOf(parameter.getType())) {
      case INTEGER:
        return Integer.class;
      case LONG:
        return Long.class;
      case FLOAT:
        return Float.class;
      case DOUBLE:
        return Double.class;
      case DATE:
      case TIME:
      case TIMESTAMP:
        return Date.class;
      case BOOLEAN:
        return Boolean.class;
      case OBJECT:
        return Object.class;
      case STRING:
      default:
        return String.class;
    }
  }

  /**
   * Extract parameters values
   *
   * @param parameter             Parameter
   * @param paramValue            Parameter value
   */
  public Object getParameterValue(ServiceInputParameter parameter, Object paramValue) {
    switch (ParameterType.valueOf(parameter.getType())) {
      case INTEGER:
      case LONG:
      case FLOAT:
      case DOUBLE:
      case BOOLEAN:
        return "".equals(paramValue) ? null : paramValue;
      case DATE:
      case TIME:
      case TIMESTAMP:
        return "".equals(paramValue) ? null : DateUtil.web2Date((String) paramValue);
      case OBJECT:
      case STRING:
      default:
        return paramValue;
    }
  }

  /**
   * Returns the value array list
   *
   * @param parameter parameter name
   * @param paramsMap map with parameters
   * @return Service call string
   */
  public List<Object> getParameterValueList(ServiceInputParameter parameter, Map<String, Object> paramsMap) {

    // Variable definition
    List parameterList = new ArrayList();
    Object parameterValue;
    if (paramsMap.containsKey(parameter.getName()) && !"".equals(paramsMap.get(parameter.getName()))) {
      parameterValue = paramsMap.get(parameter.getName());
      if (parameterValue instanceof Collection) {
        for (Object parameterValueElement : (Collection) parameterValue) {
          parameterList.add(getParameterValue(parameter, parameterValueElement));
        }
      } else {
        parameterList.add(getParameterValue(parameter, parameterValue));
      }
    } else {
      parameterList = new ArrayList();
    }
    return parameterList;
  }
}
