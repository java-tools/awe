package com.almis.awe.service.connector;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.services.ServiceInputParameter;
import com.almis.awe.model.type.ParameterType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
          extractParameterList(iteratingParam, param, paramsMapFromRequest, paramsToInvoke);
          paramsClassesToInvoke[iteratingParam] = List.class;
        } else {
          Object paramValue = paramsMapFromRequest.get(paramNameToInvoke);
          extractParameterValue(iteratingParam, param, paramValue, paramsToInvoke, paramsClassesToInvoke);
        }
        iteratingParam++;
      } else {
        throw new AWException(getLocale("ERROR_TITLE_WEB_SERVICE_CALL"),
          getLocale("ERROR_MESSAGE_PARAMETER_NOT_FOUND", paramNameToInvoke));
      }
    }
  }

  /**
   * Extract parameters values
   *
   * @param iteratingParam       XML parameters
   * @param parameter            Parameter
   * @param paramsMapFromRequest Map parameters
   * @param paramsToInvoke       Parameters to invoke
   */
  public void extractParameterList(Integer iteratingParam, ServiceInputParameter parameter, Map<String, Object> paramsMapFromRequest, Object[] paramsToInvoke) {
    switch (ParameterType.valueOf(parameter.getType())) {
      case INTEGER:
        paramsToInvoke[iteratingParam] = getParameterValueList(parameter.getName(), Integer.class, paramsMapFromRequest);
        break;
      case LONG:
        paramsToInvoke[iteratingParam] = getParameterValueList(parameter.getName(), Long.class, paramsMapFromRequest);
        break;
      case FLOAT:
        paramsToInvoke[iteratingParam] = getParameterValueList(parameter.getName(), Float.class, paramsMapFromRequest);
        break;
      case DOUBLE:
        paramsToInvoke[iteratingParam] = getParameterValueList(parameter.getName(), Double.class, paramsMapFromRequest);
        break;
      case DATE:
      case TIME:
      case TIMESTAMP:
        paramsToInvoke[iteratingParam] = getParameterValueList(parameter.getName(), Date.class, paramsMapFromRequest);
        break;
      case BOOLEAN:
        paramsToInvoke[iteratingParam] = getParameterValueList(parameter.getName(), Boolean.class, paramsMapFromRequest);
        break;
      case OBJECT:
        paramsToInvoke[iteratingParam] = getParameterValueList(parameter.getName(), Object.class, paramsMapFromRequest);
        break;
      case STRING:
      default:
        paramsToInvoke[iteratingParam] = getParameterValueList(parameter.getName(), String.class, paramsMapFromRequest);
        break;
    }
  }

  /**
   * Extract parameters values
   *
   * @param iteratingParam        Parameter index
   * @param parameter             Parameter
   * @param paramValue            Parameter value
   * @param paramsToInvoke        Parameters to invoke
   * @param paramsClassesToInvoke Classes to invoke
   */
  public void extractParameterValue(Integer iteratingParam, ServiceInputParameter parameter, Object paramValue, Object[] paramsToInvoke, Class[] paramsClassesToInvoke) {
    switch (ParameterType.valueOf(parameter.getType())) {
      case INTEGER:
        paramsToInvoke[iteratingParam] = "".equals(paramValue) ? null : (Integer) paramValue;
        paramsClassesToInvoke[iteratingParam] = Integer.class;
        break;
      case LONG:
        paramsToInvoke[iteratingParam] = "".equals(paramValue) ? null : (Long) paramValue;
        paramsClassesToInvoke[iteratingParam] = Long.class;
        break;
      case FLOAT:
        paramsToInvoke[iteratingParam] = "".equals(paramValue) ? null : (Float) paramValue;
        paramsClassesToInvoke[iteratingParam] = Float.class;
        break;
      case DOUBLE:
        paramsToInvoke[iteratingParam] = "".equals(paramValue) ? null : (Double) paramValue;
        paramsClassesToInvoke[iteratingParam] = Double.class;
        break;
      case DATE:
      case TIME:
      case TIMESTAMP:
        paramsToInvoke[iteratingParam] = "".equals(paramValue) ? null : (Date) paramValue;
        paramsClassesToInvoke[iteratingParam] = Date.class;
        break;
      case BOOLEAN:
        paramsToInvoke[iteratingParam] = "".equals(paramValue) ? null : (Boolean) paramValue;
        paramsClassesToInvoke[iteratingParam] = Boolean.class;
        break;
      case OBJECT:
        paramsToInvoke[iteratingParam] = paramValue;
        paramsClassesToInvoke[iteratingParam] = Object.class;
        break;
      case STRING:
      default:
        paramsToInvoke[iteratingParam] = paramValue;
        paramsClassesToInvoke[iteratingParam] = String.class;
        break;
    }
  }

  /**
   * Returns the value array list
   *
   * @param name      parameter name
   * @param className parameter object type
   * @param paramsMap map with parameters
   * @return Service call string
   */
  public List<Object> getParameterValueList(String name, Class className, Map<String, Object> paramsMap) {

    // Variable definition
    List parameterList;
    Object parameterValue;
    if (paramsMap.containsKey(name) && !"".equals(paramsMap.get(name))) {
      parameterValue = paramsMap.get(name);
      if (parameterValue instanceof List) {
        parameterList = (List) parameterValue;
      } else {
        parameterList = new ArrayList();
        parameterList.add(className.cast(parameterValue));
      }
    } else {
      parameterList = new ArrayList();
    }
    return parameterList;
  }
}
