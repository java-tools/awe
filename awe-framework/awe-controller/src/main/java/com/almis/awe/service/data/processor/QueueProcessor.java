package com.almis.awe.service.data.processor;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.XStreamSerializer;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ResponseWrapper;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.queues.MessageParameter;
import com.almis.awe.model.entities.queues.MessageWrapper;
import com.almis.awe.model.entities.queues.ResponseMessage;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.type.QueueMessageType;
import com.almis.awe.model.type.QueueMessageWrapperType;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.service.data.builder.EnumBuilder;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generate service datalists
 */
public class QueueProcessor extends ServiceConfig {

  // Autowired services
  private XStreamSerializer serializer;

  /**
   * Autowired constructor
   * @param serializer
   */
  @Autowired
  public QueueProcessor(XStreamSerializer serializer) {
    this.serializer = serializer;
  }

  /**
   * Parse queue message
   * @param response Response
   * @param message Message
   * @return Service data
   * @throws AWException Error parsing response
   */
  public ServiceData parseResponseMessage(ResponseMessage response, Message message) throws AWException {
    ServiceData serviceData;

    try {
      if (message != null) {
        // Create message
        switch (QueueMessageType.valueOf(response.getType())) {
          case MAP:
            serviceData = parseMapMessage(response, (MapMessage) message);
            break;
          case TEXT:
            default:
            serviceData = parseTextMessage(response, (TextMessage) message);
        }
      } else {
        throw new AWException(getLocale("ERROR_TITLE_PARSING_RESPONSE_MESSAGE"),
                getLocale("ERROR_MESSAGE_PARSING_RESPONSE_MESSAGE"));
      }
    } catch (AWException exc) {
      throw exc;
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_PARSING_RESPONSE_MESSAGE"),
              getLocale("ERROR_MESSAGE_PARSING_RESPONSE_MESSAGE"), exc);
    }

    // Retrieve message parsed as datalist
    return serviceData;
  }

  /**
   * Parse text message parameters
   * @param response Response
   * @param message Message
   * @return Service data
   * @throws AWException Error parsing message as text
   */
  private ServiceData parseTextMessage(ResponseMessage response, TextMessage message) throws AWException {
    // Variable definition
    ServiceData serviceData = null;

    try {
      // Wrapper object
      if (response.getWrapper() != null) {
        // Get Datalist form wrapper
        serviceData = getWrapperOutput(response.getWrapper(), message.getText());
      } else if (response.getParameters() != null) {
        serviceData = new ServiceData();
        DataList dataList = new DataList();
        serviceData.setDataList(dataList);

        // Retrieve parameter values
        retrieveParameterValues(response, message.getText(), dataList);
      }
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_PARSING_MESSAGE_TEXT_PARAMETERS"),
              getLocale("ERROR_MESSAGE_PARSING_MESSAGE_TEXT_PARAMETERS"), exc);
    }

    return serviceData;
  }

  /**
   * Retrieve parameter values
   * @param response Response
   * @param message Message
   * @param dataList Datalist
   */
  private void retrieveParameterValues(ResponseMessage response, String message, DataList dataList) {
    // Split with separator
    String[] valueList = message.split(StringUtil.toPlainText(response.getSeparator()));

    // Generate index
    int valueIndex = 0;

    // For each parameter retrieve text value
    for (MessageParameter parameter : response.getParameters()) {
      // Retrieve parameter value
      if (valueIndex < valueList.length) {
        if (parameter.isList()) {
          // Generate list
          List<Object> list = new ArrayList<>();

          // Read value list size
          Integer size = Integer.valueOf(valueList[valueIndex]);

          // Increase index
          valueIndex++;

          // Read list values
          for (Integer listIndex = 0; listIndex < size; listIndex++) {
            // Read queue value and add it into the list
            list.add(valueList[valueIndex]);
            valueIndex++;
          }

          // Add list as column
          DataListUtil.addColumn(dataList, parameter.getId(), list);
        } else {
          // Generate list
          ArrayList<Object> list = new ArrayList<>();

          // Read queue value and add it into the list
          list.add(valueList[valueIndex]);

          // Increase index
          valueIndex++;

          // Add list as column
          DataListUtil.addColumn(dataList, parameter.getId(), list);
        }
      }
    }
  }

  /**
   * Parse map message parameters
   * @param response Response
   * @param message Message
   * @return Service data
   * @throws AWException Error parsing map message
   */
  private ServiceData parseMapMessage(ResponseMessage response, MapMessage message) throws AWException {
    // Variable definition
    ServiceData serviceData = null;

    try {
      // Add wrapper object
      if (response.getWrapper() != null) {
        // Get Service data from wrapper
        if (response.getWrapper().getName() != null) {
          serviceData = getWrapperOutput(response.getWrapper(), message.getString(response.getWrapper().getName()));
        } else {
          serviceData = getWrapperOutput(response.getWrapper(), message);
        }
      }

      // Generate parameters values
      if (response.getParameters() != null) {
        // Create service data if not defined
        serviceData = serviceData == null ? new ServiceData() : serviceData;
        DataList dataList = new DataList();
        serviceData.setDataList(dataList);

        // Retrieve parameter values
        retrieveParameterValues(response, message, dataList);
      }

      // Add message status
      if (response.getStatus() != null) {
        // Create service data if not defined
        serviceData = serviceData == null ? new ServiceData() : serviceData;

        // Set message status
        setMessageStatus(serviceData, response, message);
      }
    } catch (AWException exc) {
      throw exc;
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_PARSING_MESSAGE_MAP_PARAMETERS"),
              getLocale("ERROR_MESSAGE_PARSING_MESSAGE_MAP_PARAMETERS"), exc);
    }

    return serviceData;
  }

  /**
   * Retrieve parameter values
   * @param response Response
   * @param message Message
   * @param dataList Datalist
   * @throws AWException
   * @throws JMSException
   */
  private void retrieveParameterValues(ResponseMessage response, MapMessage message, DataList dataList) throws AWException, JMSException {
    // For each parameter retrieve text value
    for (MessageParameter parameter : response.getParameters()) {
      if (parameter.isList()) {
        // Retrieve parameter list
        retrieveParameterList(parameter, message, dataList);
      } else {
        // Generate list
        ArrayList<Object> list = new ArrayList<>();

        // Read queue value and add it into the list
        list.add(message.getObject(parameter.getName()));

        // Add list as column
        DataListUtil.addColumn(dataList, parameter.getId(), list);
      }
    }
  }

  /**
   * Retrieve parameter list
   * @param parameter Parameter
   * @param message Message
   * @param dataList Data list
   * @throws AWException
   * @throws JMSException
   */
  private void retrieveParameterList(MessageParameter parameter, MapMessage message, DataList dataList) throws AWException, JMSException {
    // Generate an array
    List<Object> list = new ArrayList<>();
    Object value = message.getObject(parameter.getName());

    if (value instanceof List) {
      // Parse list as arraylist
      list = (List<Object>) value;
    } else if (value instanceof Object[]) {
      // Cast value
      Object[] valueList = (Object[]) value;

      // Read list values
      for (Integer listIndex = 0, size = valueList.length; listIndex < size; listIndex++) {
        // Read queue value and add it into the list
        list.add(valueList[listIndex]);
      }
    } else if (value == null) {
      // Read value list size
      Integer size = message.getInt(parameter.getName());

      // Read list values
      for (Integer listIndex = 1; listIndex <= size; listIndex++) {
        // Read queue value and add it into the list
        list.add(message.getObject(parameter.getName() + listIndex));
      }
    } else {
      throw new AWException(getLocale("ERROR_TITLE_PARSING_MESSAGE_MAP_PARAMETERS"),
        getLocale("ERROR_MESSAGE_RESPONSE_PARAMETER_NOT_A_LIST", parameter.getName()));
    }

    // Add list as column
    DataListUtil.addColumn(dataList, parameter.getId(), list);
  }

  /**
   * Set message status
   * @param serviceData Service data
   * @param response Response
   * @param message Message
   * @throws AWException
   * @throws JMSException
   */
  private void setMessageStatus(ServiceData serviceData, ResponseMessage response, MapMessage message) throws AWException, JMSException {
    // Type of response
    String answerType = message.getString(response.getStatus().getType());
    if (response.getStatus().getTranslate() != null) {
      answerType = getBean(EnumBuilder.class)
        .setEnumerated(response.getStatus().getTranslate())
        .findLabel(answerType);
    }

    // Define service data output
    serviceData.setType(AnswerType.getEnum(answerType));

    // Set title if defined
    String title = message.getString(response.getStatus().getTitle());
    if (title != null) {
      serviceData.setTitle(title);
    }

    // Set message if defined
    String description = message.getString(response.getStatus().getDescription());
    if (title != null) {
      serviceData.setMessage(description);
    }
  }

  /**
   * Parse text message parameters
   *
   * @param wrapper Wrapper
   * @return Wrapper value
   */
  private ServiceData getWrapperOutput(MessageWrapper wrapper, String message) throws AWException {

    // Get wrapper instance
    ResponseWrapper responseWrapper;

    // Only valid for XML wrappers
    if (QueueMessageWrapperType.valueOf(wrapper.getType()).equals(QueueMessageWrapperType.OBJECT)) {
      // TODO: Generate locales
      throw new AWException("To wrap a map message as Object, avoid name attribute: " + wrapper.getClassName());
    }

    // Parse message as XML
    try {
      responseWrapper = (ResponseWrapper) serializer.getObjectFromXml(Class.forName(wrapper.getClassName()), new StringReader(message));
    } catch (ClassNotFoundException exc) {
      // TODO: Generate locales
      throw new AWException("Wrapper class not found: " + wrapper.getClassName(), exc);
    }

    return responseWrapper.toServiceData();
  }

  /**
   * Parse map message parameters
   *
   * @param wrapper Wrapper
   * @return Wrapper value
   */
  private ServiceData getWrapperOutput(MessageWrapper wrapper, MapMessage message) throws AWException {

    // Get wrapper instance
    ResponseWrapper responseWrapper = getWrapperInstance(wrapper);

    // Only valid for Object wrappers
    if (QueueMessageWrapperType.valueOf(wrapper.getType()).equals(QueueMessageWrapperType.XML)) {
      // TODO: Generate locales
      throw new AWException("To wrap a map message as XML, you need to define a name attribute: " + wrapper.getClassName());
    }

    // Parse message as Object (mapping all attributes to object attributes)
    String currentKey = null;
    try {
      List<String> mapKeys =  Collections.list(message.getMapNames());
      PropertyAccessor myAccessor = PropertyAccessorFactory.forDirectFieldAccess(responseWrapper);
      for (String key : mapKeys) {
        currentKey = key;
        if (myAccessor.isWritableProperty(key)) {
          myAccessor.setPropertyValue(key, message.getObject(key));
        }
      }
    } catch (Exception exc) {
      // TODO: Generate locales
      throw new AWException(getLocale("Error trying to update wrapper object: {0} in key {1}", wrapper.getClassName(), currentKey), exc);
    }


    return responseWrapper.toServiceData();
  }

  /**
   * Get wrapper instance
   *
   * @param wrapper Wrapper
   * @return Wrapper instance
   */
  private ResponseWrapper getWrapperInstance(MessageWrapper wrapper) throws AWException {

    ResponseWrapper responseWrapper;
    // Try to get a bean first
    if (containsBean(wrapper.getClassName())) {
      responseWrapper = (ResponseWrapper) getBean(wrapper.getClassName());
    } else {
      try {
        Class wrapperClass = Class.forName(wrapper.getClassName());
        responseWrapper = (ResponseWrapper) wrapperClass.newInstance();
      } catch (Exception exc) {
        // TODO: Generate locales
        throw new AWException("Wrapper class not found: " + wrapper.getClassName(), exc);
      }
    }

    // Class generation
    return responseWrapper;
  }
}
