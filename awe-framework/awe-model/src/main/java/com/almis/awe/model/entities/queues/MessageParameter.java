package com.almis.awe.model.entities.queues;

import com.almis.awe.model.entities.services.ServiceInputParameter;
import com.almis.awe.model.type.ParameterType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
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
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("message-parameter")
public class MessageParameter extends ServiceInputParameter {

  private static final long serialVersionUID = -3825265381738837148L;
  // Parameter id
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id;

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
      case FLOAT:
      case DOUBLE:
      case LONG:
      case DATE:
      case TIME:
      case TIMESTAMP:
        stringValue = value.toString();
        break;
      case STRING:
      default:
        stringValue = (String) value;
        break;
    }

    return stringValue;
  }

  @Override
  public MessageParameter copy() {
    return this.toBuilder().build();
  }
}
