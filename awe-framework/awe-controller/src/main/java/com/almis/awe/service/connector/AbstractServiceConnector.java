package com.almis.awe.service.connector;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.services.ServiceInputParameter;
import com.almis.awe.model.type.ParameterType;
import com.almis.awe.model.util.data.DateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Abstract class that contains general method implementations for all ServiceConnectors
 *
 * @author jbellon
 */
abstract class AbstractServiceConnector extends ServiceConfig implements ServiceConnector {

  private static final String CANT_CREATE_INSTANCE = "Can't create instance of ";

  /**
   * Extract parameters values
   *
   * @param paramsFromXml         XML parameters
   * @param paramsMapFromRequest  Map parameters
   * @param paramsToInvoke        Parameters to invoke
   * @param paramsClassesToInvoke Classes to invoke
   * @throws AWException Error extracting parameters
   */
  void extractParameters(List<ServiceInputParameter> paramsFromXml, Map<String, Object> paramsMapFromRequest, Object[] paramsToInvoke, Class[] paramsClassesToInvoke) throws AWException {
    Integer iteratingParam = 0;

    // Add parameters into the parameter object list
    for (ServiceInputParameter param : paramsFromXml) {
      extractParameter(param, paramsMapFromRequest, paramsToInvoke, paramsClassesToInvoke, iteratingParam++);
    }
  }

  /**
   * Extract parameters values
   *
   * @param parameter             XML parameter
   * @param paramsMapFromRequest  Map parameters
   * @param paramsToInvoke        Parameters to invoke
   * @param paramsClassesToInvoke Classes to invoke
   * @param index                 Parameter index
   * @throws AWException Error extracting parameters
   */
  private void extractParameter(ServiceInputParameter parameter, Map<String, Object> paramsMapFromRequest, Object[] paramsToInvoke, Class[] paramsClassesToInvoke, Integer index) throws AWException {
    if (parameter.getBeanClass() != null) {
      Class beanClass = getParameterClass(parameter);
      if (parameter.isList()) {
        paramsToInvoke[index] = getParameterBeanListValue(beanClass, paramsMapFromRequest);
        paramsClassesToInvoke[index] = List.class;
      } else if ("JSON".equalsIgnoreCase(parameter.getType())) {
        paramsToInvoke[index] = getParameterJsonBeanValue(parameter, beanClass, paramsMapFromRequest);
        paramsClassesToInvoke[index] = beanClass;
      } else {
        paramsToInvoke[index] = getParameterBeanValue(beanClass, paramsMapFromRequest);
        paramsClassesToInvoke[index] = beanClass;
      }
    } else {
      if (parameter.isList()) {
        paramsToInvoke[index] = getParameterListValue(parameter, paramsMapFromRequest);
        paramsClassesToInvoke[index] = List.class;
      } else {
        paramsToInvoke[index] = getParameterValue(parameter, paramsMapFromRequest.get(parameter.getName()));
        paramsClassesToInvoke[index] = getParameterClass(parameter);
      }
    }
  }

  /**
   * Get parameter class
   * @param parameter Parameter
   */
  private Class getParameterClass(ServiceInputParameter parameter) {
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
        try {
          return Class.forName(parameter.getBeanClass());
        } catch (Exception exc) {
          return Object.class;
        }
      case JSON:
        try {
          return Class.forName(parameter.getBeanClass());
        } catch (Exception exc) {
          return JsonNode.class;
        }
      case STRING:
      default:
        return String.class;
    }
  }

  /**
   * Extract parameters values
   *
   * @param parameter      Parameter
   * @param parameterValue Parameter value
   */
  private Object getParameterValue(ServiceInputParameter parameter, Object parameterValue) {
    switch (ParameterType.valueOf(parameter.getType())) {
      case INTEGER:
      case LONG:
      case FLOAT:
      case DOUBLE:
      case BOOLEAN:
        return "".equals(parameterValue) ? null : parameterValue;
      case DATE:
      case TIME:
      case TIMESTAMP:
        return "".equals(parameterValue) ? null : DateUtil.web2Date((String) parameterValue);
      case OBJECT:
      case STRING:
      default:
        return parameterValue;
    }
  }

  /**
   * Returns the value array list
   *
   * @param parameter parameter name
   * @param paramsMap map with parameters
   * @return Service call string
   */
  private List getParameterListValue(ServiceInputParameter parameter, Map<String, Object> paramsMap) {

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

  /**
   * Retrieve parameter as bean value
   * @param beanClass Bean class
   * @param paramsMap Parameter map
   * @param <T> Bean type
   * @return Bean value
   * @throws AWException
   */
  private <T> T getParameterJsonBeanValue(ServiceInputParameter parameter, Class<T> beanClass, Map<String, Object> paramsMap) throws AWException {
    try {
      if (paramsMap.get(parameter.getName()) != null) {
        // Generate row bean
        return new ObjectMapper().treeToValue((JsonNode) paramsMap.get(parameter.getName()), beanClass);
      } else {
        return null;
      }
    } catch (Exception exc) {
      throw new AWException("Error converting json parameter to object", CANT_CREATE_INSTANCE + beanClass.getSimpleName(), exc);
    }
  }

  /**
   * Retrieve parameter as bean value from JSON
   * @param beanClass Bean class
   * @param paramsMap Parameter map
   * @param <T> Bean type
   * @return Bean value
   * @throws AWException
   */
  private <T> T getParameterBeanValue( Class<T> beanClass, Map<String, Object> paramsMap) throws AWException {
    T parameterBean;
    try {
      // Generate row bean
      parameterBean = beanClass.getConstructor().newInstance();
    } catch (Exception exc) {
      throw new AWException("Error converting datalist into a bean list", CANT_CREATE_INSTANCE + beanClass.getSimpleName(), exc);
    }

    // Set field value if found in row
    for (Field field : beanClass.getDeclaredFields()) {
      if (paramsMap.containsKey(field.getName())) {
        PropertyAccessor parameterBeanAccesor = PropertyAccessorFactory.forDirectFieldAccess(parameterBean);
        parameterBeanAccesor.setPropertyValue(field.getName(), paramsMap.get(field.getName()));
      }
    }

    return parameterBean;
  }

  /**
   * Retrieve parameter as bean list value
   * @param beanClass Bean class
   * @param paramsMap Parameter map
   * @param <T> Bean type
   * @return Bean list
   * @throws AWException
   */
  private <T> List<T> getParameterBeanListValue(Class<T> beanClass, Map<String, Object> paramsMap) throws AWException {
    List<T> list = null;

    // Set field value if found in row
    for (Field field : beanClass.getDeclaredFields()) {
      if (paramsMap.containsKey(field.getName())) {
        List valueList = (List) paramsMap.get(field.getName());
        if (list == null) {
          list = initializeList(valueList, beanClass);
        }

        // Store value in bean list
        int index = 0;
        for (Object value : valueList) {
          T listBean = list.get(index++);
          PropertyAccessor listBeanAccessor = PropertyAccessorFactory.forDirectFieldAccess(listBean);
          listBeanAccessor.setPropertyValue(field.getName(), value);
        }
      }
    }

    return list;
  }

  /**
   * Initialize bean list
   * @param valueList Value list
   * @param beanClass Bean class
   * @param <T>
   * @return Initialized bean list
   * @throws AWException
   */
  private <T> List<T> initializeList(List valueList, Class<T> beanClass) throws AWException {
    List<T> beanList = new ArrayList<>();
    // Initialize list if first defined
    if (beanList.size() < valueList.size()) {
      for (int i = 0, t = valueList.size(); i < t; i++) {
        // Initialize list
        try {
          // Generate row bean
          T parameterBean = beanClass.getConstructor().newInstance();
          beanList.add(parameterBean);
        } catch (Exception exc) {
          throw new AWException("Error converting datalist into a bean list", CANT_CREATE_INSTANCE + beanClass.getSimpleName(), exc);
        }
      }
    }
    return beanList;
  }
}
