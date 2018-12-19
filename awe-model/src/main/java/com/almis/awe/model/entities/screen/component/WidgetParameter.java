/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.type.WidgetParameterType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * File Imports
 */

/**
 * WidgetParameter Class
 *
 * Used to parse a widget parameter or parameter group with XStream
 *
 *
 * Widget parameter or widget parameter list
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("widget-parameter")
public class WidgetParameter extends Component {

  private static final long serialVersionUID = 5567676802546395551L;
  // Parameter value
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value = null;

  /**
   * Default constructor
   */
  public WidgetParameter() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public WidgetParameter(WidgetParameter other) throws AWException {
    super(other);
    this.value = other.value;
  }

  @Override
  public WidgetParameter copy() throws AWException {
    return new WidgetParameter(this);
  }

  /**
   * Returns the parameter value. If label is defined, returns the label local. If variable is defined, returns the output defined variable.
   *
   * @return Parameter value
   */
  @JsonIgnore
  public String getValue() {
    return this.value;
  }

  /**
   * Stores the parameter value
   *
   * @param value Parameter value
   */
  public void setValue(String value) {
    this.value = value;
  }

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_EMPTY;
  }

  @Override
  @JsonIgnore
  public String getComponentTag() {
    return AweConstants.NO_TAG;
  }

  /**
   * Returns the parameter element for JSON serialization
   *
   * @return Parameter element
   */
  public Object getParameterValue() {
    // Add dependency element value
    if (this.getType() != null) {
      switch (WidgetParameterType.valueOf(this.getType().toUpperCase())) {
        case LABEL:
          return this.getValue();
        case STRING:
          return this.getValue();
        case BOOLEAN:
          return Boolean.parseBoolean(this.getValue());
        case INTEGER:
          return Integer.valueOf(this.getValue());
        case LONG:
          return Long.valueOf(this.getValue());
        case FLOAT:
          return Float.valueOf(this.getValue());
        case DOUBLE:
          return Double.valueOf(this.getValue());
        case ARRAY:
          return this.getParameterList();
        case OBJECT:
          return this.getParameterObject();
        case NULL:
        default:
          return null;
      }
    }
    return null;
  }

  /**
   * Returns the parameter element list for JSON serialization
   *
   * @return Parameter list
   */
  public List<Map<String, Object>> getParameterList() {
    // Return string parameter list
    List<Map<String, Object>> parameterList = new ArrayList<Map<String, Object>>();

    // Get elements (columns)
    if (this.getElementList() != null) {
      for (Element element : this.getElementList()) {
        if (element instanceof WidgetParameter) {
          WidgetParameter parameter = (WidgetParameter) element;
          Map<String, Object> parameterMap = new HashMap<String, Object>();
          if (parameter.getName() != null) {
            parameterMap.put(parameter.getName(), parameter.getParameterValue());
          }
          parameterList.add(parameterMap);
        }
      }
    }
    return parameterList;
  }

  /**
   * Returns the parameter element list
   *
   * @return Parameter list
   */
  public Map<String, Object> getParameterObject() {
    Map<String, Object> parameterMap = new HashMap<String, Object>();

    if (this.getElementList() != null) {
      for (Element element : this.getElementList()) {
        if (element instanceof WidgetParameter) {
          WidgetParameter parameter = (WidgetParameter) element;
          if (parameter.getName() != null) {
            parameterMap.put(parameter.getName(), parameter.getParameterValue());
          }
        }
      }
    }
    return parameterMap;
  }
}
