package com.almis.awe.model.entities.queues;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.type.ParameterType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * MessageParameter Class
 *
 * Used to parse the tag 'message-parameter' in file Queues.xml with XStream
 * This class is used to instantiate message parameters for a queue
 *
 * @author Pablo GARCIA - 31/OCT/2013
 */
@XStreamAlias("message-parameter")
public class MessageParameter extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = -3825265381738837148L;
  // Parameter id
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id;
  // Parameter type
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;
  // Parameter name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  // Parameter value
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;
  // Parameter value
  @XStreamOmitField
  private List<String> valueList;
  // Parameter value
  @XStreamAlias("list")
  @XStreamAsAttribute
  private String list;

  /**
   * Copy constructor
   *
   * @param other
   */
  public MessageParameter(MessageParameter other) {
    super(other);
    this.id = other.id;
    this.type = other.type;
    this.name = other.name;
    this.value = other.value;
    this.list = other.list;
  }

  /**
   * Returns the parameter name
   *
   * @return Parameter name
   */
  public String getName() {
    return name;
  }

  /**
   * Stores the paramete name
   *
   * @param name Parameter name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the parameter value
   *
   * @return Parameter value
   */
  public String getValue() {
    return value;
  }

  /**
   * Stores the parameter value
   *
   * @param value Parameter value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Returns if the parameter is a list
   *
   * @return Parameter is a list
   */
  public boolean isList() {
    return Boolean.parseBoolean(list);
  }

  /**
   * Set parameter as list
   *
   * @param list Parameter is a list
   */
  public void setList(String list) {
    this.list = list;
  }

  /**
   * Returns the value list
   *
   * @return the valueList
   */
  public List<String> getValueList() {
    return valueList;
  }

  /**
   * Stores the value list
   *
   * @param valueList the valueList to set
   */
  public void setValueList(List<String> valueList) {
    this.valueList = valueList;
  }

  /**
   * Retrieve parameter id
   *
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * Store parameter id
   *
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Retrieve parameter type
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Store parameter type
   *
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Retrieve parameter value list
   *
   * @param valueList Parameter values
   * @return array with parameter values
   */
  public List<Object> getParameterValueList(Map<String, Object> valueList) {
    // Variable definition

    List<Object> parameterList;
    if (valueList.containsKey(this.getName())) {
      parameterList = (ArrayList<Object>) valueList.get(this.getName());
    } else {
      parameterList = new ArrayList<>();
    }

    return parameterList;
  }

  /**
   * Retrieve parameter value
   *
   * @param valueList Parameter values
   * @return parameter value
   */
  public Object getParameterValue(Map<String, Object> valueList) {
    // Variable definition
    Object parameterValue = null;

    if (this.getValue() != null) {
      parameterValue = this.getValue();
    } else if (this.getName() != null) {
      parameterValue = valueList.get(this.getName());
    }

    return parameterValue;
  }

  /**
   * Retrieve parameter value list as text
   *
   * @param valueList Parameter values
   * @param separator Separator character
   * @return String with value list
   */
  public String getParameterValueListText(Map<String, Object> valueList, String separator) {
    // Variable definition
    StringBuilder builder = new StringBuilder();
    List<Object> parameterList = this.getParameterValueList(valueList);

    // Add list length
    builder.append(parameterList.size());

    // Add list values
    for (Object parameterValue : parameterList) {
      builder.append(separator + this.getStringValue(this.getType(), parameterValue));
    }

    return builder.toString();
  }

  /**
   * Retrieve parameter value
   *
   * @param valueList Parameter values
   * @return parameter value
   */
  public Object getParameterValueText(Map<String, Object> valueList) {
    // Variable definition
    Object objectValue = this.getParameterValue(valueList);

    // Return string value
    return this.getStringValue(this.getType(), objectValue);
  }

  /**
   * Retrieve parameter value as string
   *
   * @param type  Value type
   * @param value Value
   * @return parameter value as string
   */
  private String getStringValue(String type, Object value) {
    // Variable definition
    String stringValue;

    // Get text value for parameter
    switch (ParameterType.valueOf(type)) {
      case INTEGER:
        stringValue = ((Integer) value).toString();
        break;
      case FLOAT:
        stringValue = ((Float) value).toString();
        break;
      case DOUBLE:
        stringValue = ((Double) value).toString();
        break;
      case LONG:
        stringValue = ((Long) value).toString();
        break;
      case DATE:
      case TIME:
      case TIMESTAMP:
        stringValue = ((Date) value).toString();
        break;
      case STRING:
      default:
        stringValue = (String) value;
        break;
    }

    return stringValue;
  }

  @Override
  public MessageParameter copy() throws AWException {
    return new MessageParameter(this);
  }
}
