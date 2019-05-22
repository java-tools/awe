package com.almis.awe.model.entities.screen.component.widget;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.type.WidgetParameterType;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WidgetParameter Class
 * Used to parse a widget parameter or parameter group with XStream
 * Widget parameter or widget parameter list
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Accessors(chain = true)
@NoArgsConstructor
@XStreamAlias("widget-parameter")
public class WidgetParameter extends AbstractWidget {

  private static final long serialVersionUID = 5567676802546395551L;

  // Parameter value
  @JsonIgnore
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  @Override
  public WidgetParameter copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
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
    if (getType() != null) {
      switch (WidgetParameterType.valueOf(getType().toUpperCase())) {
        case LABEL:
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
          return getParameterList();
        case OBJECT:
          return getParameterObject();
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
    List<Map<String, Object>> parameterList = new ArrayList<>();

    // Get elements (columns)
    for (Element element : getElementList()) {
      if (element instanceof WidgetParameter) {
        WidgetParameter parameter = (WidgetParameter) element;
        Map<String, Object> parameterMap = new HashMap<>();
        if (parameter.getName() != null) {
          parameterMap.put(parameter.getName(), parameter.getParameterValue());
        }
        parameterList.add(parameterMap);
      }
    }

    return parameterList;
  }
}
