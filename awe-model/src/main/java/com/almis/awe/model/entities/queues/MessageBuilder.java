package com.almis.awe.model.entities.queues;

import com.almis.awe.model.component.XStreamSerializer;
import com.almis.awe.model.dto.RequestWrapper;
import com.almis.awe.model.type.ParameterType;
import com.almis.awe.model.type.QueueMessageType;
import com.almis.awe.model.type.QueueMessageWrapperType;
import com.almis.awe.model.util.data.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.context.WebApplicationContext;

import javax.jms.*;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Message creator with correlation id
 */
public class MessageBuilder implements MessageCreator {

  private QueueMessageType type;
  private RequestMessage request;
  private Map<String, Object> valueList;
  private Message message;
  private WebApplicationContext context;
  private XStreamSerializer serializer;
  private String selector = null;

  /**
   * Constructor
   *
   * @param context    Application context
   * @param serializer XML Serializer
   */
  @Autowired
  public MessageBuilder(WebApplicationContext context, XStreamSerializer serializer) {
    this.context = context;
    this.serializer = serializer;
  }

  /**
   * Get message type
   *
   * @return Message type
   */
  public QueueMessageType getType() {
    return type;
  }

  /**
   * Set message type
   *
   * @param type Type (as string)
   * @return this
   */
  public MessageBuilder setType(String type) {
    this.type = QueueMessageType.valueOf(type);
    return this;
  }

  /**
   * Get request message
   *
   * @return Request
   */
  public RequestMessage getRequest() {
    return request;
  }

  /**
   * Set request message
   *
   * @param request Request message
   * @return this
   */
  public MessageBuilder setRequest(RequestMessage request) {
    this.request = request;
    return this;
  }

  /**
   * Get parameters
   *
   * @return parameters
   */
  public Map<String, Object> getValueList() {
    return valueList;
  }

  /**
   * Set parameters
   *
   * @param valueList parameters
   * @return this
   */
  public MessageBuilder setValueList(Map<String, Object> valueList) {
    this.valueList = valueList;
    return this;
  }

  /**
   * Get message selector
   *
   * @return Selector
   */
  public String getSelector() {
    return selector;
  }

  /**
   * Set message selector
   *
   * @param selector Selector
   * @return this
   */
  public MessageBuilder setSelector(String selector) {
    this.selector = selector;
    return this;
  }

  /**
   * Get message
   *
   * @return Message
   */
  public Message getMessage() {
    return message;
  }

  @Override
  public Message createMessage(Session session) throws JMSException {
    // Create message
    switch (QueueMessageType.valueOf(getRequest().getType())) {
      case MAP:
        message = parseMapParameters(session.createMapMessage());
        break;
      case TEXT:
      default:
        message = parseTextParameters(session.createTextMessage());
    }

    // Add selector if defined
    if (selector != null) {
      message.setJMSType(selector);
    }

    // Set Message ID as correlation id
    message.setJMSCorrelationID(message.getJMSMessageID());
    return message;
  }

  /**
   * Parse text message parameters
   *
   * @param message Message to send
   * @return Text message
   */
  private Message parseTextParameters(TextMessage message) throws JMSException {
    // Variable definition
    StringBuilder messageBuilder = new StringBuilder();
    String separator;

    // Wrapper object
    if (getRequest().getWrapper() != null) {
      // Get XML from wrapper object
      messageBuilder.append(getWrapperValue(getRequest().getWrapper()));

      // Parameter list
    } else if (getRequest().getParameters() != null) {
      // For each parameter retrieve text value
      for (MessageParameter parameter : getRequest().getParameters()) {
        separator = messageBuilder.length() == 0 ? "" : StringUtil.toPlainText(getRequest().getSeparator());
        messageBuilder.append(separator);
        if (parameter.isList()) {
          messageBuilder.append(parameter.getParameterValueListText(getValueList(), getRequest().getSeparator()));
        } else {
          messageBuilder.append(parameter.getParameterValueText(getValueList()));
        }
      }
    }

    // Add text parameters to message
    message.setText(messageBuilder.toString());
    return message;
  }

  /**
   * Parse text message parameters
   *
   * @param message Message to send
   * @return Map message
   */
  private Message parseMapParameters(MapMessage message) throws JMSException {
    // Wrapper object
    if (getRequest().getWrapper() != null) {
      Object requestWrapper;
      // Get XML from wrapper object
      requestWrapper = getWrapperValue(getRequest().getWrapper());
      if (requestWrapper instanceof RequestWrapper) {
        message.setObject(getRequest().getWrapper().getName(), requestWrapper);
      } else {
        message.setString(getRequest().getWrapper().getName(), (String) requestWrapper);
      }
    }

    // Parameter list
    if (getRequest().getParameters() != null) {
      for (MessageParameter parameter : getRequest().getParameters()) {
        if (parameter.isList()) {
          message.setObject(parameter.getId(), parameter.getParameterValue(getValueList()));
        } else {
          switch (ParameterType.valueOf(parameter.getType())) {
            case INTEGER:
              message.setInt(parameter.getId(), (Integer) parameter.getParameterValue(getValueList()));
              break;
            case LONG:
              message.setLong(parameter.getId(), (Long) parameter.getParameterValue(getValueList()));
              break;
            case FLOAT:
              message.setFloat(parameter.getId(), (Float) parameter.getParameterValue(getValueList()));
              break;
            case DOUBLE:
              message.setDouble(parameter.getId(), (Double) parameter.getParameterValue(getValueList()));
              break;
            case DATE:
            case TIME:
            case TIMESTAMP:
              message.setObject(parameter.getId(), parameter.getParameterValue(getValueList()));
              break;
            case STRING:
            default:
              message.setString(parameter.getId(), (String) parameter.getParameterValue(getValueList()));
              break;
          }
        }
      }
    }

    // Retrieve message
    return message;
  }

  /**
   * Parse text message parameters
   *
   * @param wrapper Wrapper
   * @return Wrapper value
   */
  private Object getWrapperValue(MessageWrapper wrapper) throws JMSException {

    Object wrapperOutput;
    try {
      // Get wrapper instance
      RequestWrapper requestWrapper = getWrapperInstance(wrapper);

      // Add parameters to wrapper
      requestWrapper.setParameters(valueList);

      // Generate output depending on wrapper type
      switch (QueueMessageWrapperType.valueOf(wrapper.getType())) {
        case XML:
          // Obtain xml
          Writer writer = new StringWriter();
          serializer.writeXmlFromObject(Class.forName(wrapper.getClassName()), requestWrapper, writer);
          wrapperOutput = writer.toString();
          break;
        case OBJECT:
        default:
          wrapperOutput = requestWrapper;
          break;
      }
    } catch (ClassNotFoundException exc) {
      throw new JMSException(exc.getMessage());
    }

    return wrapperOutput;
  }

  /**
   * Get wrapper instance
   *
   * @param wrapper Wrapper
   * @return Wrapper instance
   */
  private RequestWrapper getWrapperInstance(MessageWrapper wrapper) throws JMSException {

    RequestWrapper requestWrapper;
    // Try to get a bean first
    if (context.containsBean(wrapper.getClassName())) {
      requestWrapper = (RequestWrapper) context.getBean(wrapper.getClassName());
    } else {
      try {
        Class wrapperClass = Class.forName(wrapper.getClassName());
        requestWrapper = (RequestWrapper) wrapperClass.newInstance();
      } catch (Exception exc) {
        throw new JMSException("Wrapper class not found: " + wrapper.getClassName());
      }
    }

    // Class generation
    return requestWrapper;
  }
}