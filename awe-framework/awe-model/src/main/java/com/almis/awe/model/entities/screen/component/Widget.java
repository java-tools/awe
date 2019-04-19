/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.HashMap;
import java.util.Map;

/**
 * Widget Class
 *
 * Used to parse a widget tag with XStream
 *
 *
 * Generates an screen widget
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("widget")
public class Widget extends Component {

  private static final long serialVersionUID = 7140906386094836409L;

  /**
   * Default constructor
   */
  public Widget() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Widget(Widget other) throws AWException {
    super(other);
  }

  @Override
  public Widget copy() throws AWException {
    return new Widget(this);
  }

  /**
   * Returns the parameter element list for JSON serialization
   *
   * @return Parameter list
   * @throws AWException Error retrieving converter
   */
  @JsonGetter("parameters")
  public Map<String, Object> getWidgetParametersConverter() throws AWException {

    // Variable definition
    Map<String, Object> parameterMap = new HashMap<String, Object>();

    // Get elements (columns)
    if (this.getElementList() != null) {
      for (Element element : this.getElementList()) {
        if (element instanceof WidgetParameter) {
          WidgetParameter param = (WidgetParameter) element;
          if (param.getName() != null) {
            parameterMap.put(param.getName(), param.getParameterValue());
          }
        }
      }
    }

    // Return parameter list
    return parameterMap;
  }

  @JsonIgnore
  @Override
  public String getComponentTag() {
    return getType();
  }
}
