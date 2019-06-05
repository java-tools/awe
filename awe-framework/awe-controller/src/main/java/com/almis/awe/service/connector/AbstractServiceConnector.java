package com.almis.awe.service.connector;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.services.ServiceInputParameter;
import com.almis.awe.model.type.ParameterType;
import com.almis.awe.model.util.data.DateUtil;
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
  private <T> T getParameterBeanValue( Class<T> beanClass, Map<String, Object> paramsMap) throws AWException {
    T parameterBean;
    try {
      // Generate row bean
      parameterBean = beanClass.newInstance();
    } catch (Exception exc) {
      throw new AWException("Error converting datalist into a bean list", "Cannot create instance of " + beanClass.getSimpleName(), exc);
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
    List<T> list = new ArrayList<>();
    T parameterBean;

    // Set field value if found in row
    for (Field field : beanClass.getDeclaredFields()) {
      if (paramsMap.containsKey(field.getName())) {
        List valueList = (List) paramsMap.get(field.getName());

        // Initialize list if first defined
        if (list.size() < valueList.size()) {
          for (int i = 0, t = valueList.size(); i < t; i++) {
            // Initialize list
            try {
              // Generate row bean
              parameterBean = beanClass.newInstance();
              list.add(parameterBean);
            } catch (Exception exc) {
              throw new AWException("Error converting datalist into a bean list", "Cannot create instance of " + beanClass.getSimpleName(), exc);
            }
          }
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
}
