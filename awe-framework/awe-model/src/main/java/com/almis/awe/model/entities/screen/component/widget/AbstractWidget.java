package com.almis.awe.model.entities.screen.component.widget;

import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.Component;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Widget Decorator
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@XStreamInclude({Widget.class, WidgetParameter.class})
public abstract class AbstractWidget extends Component {

  /**
   * Returns the parameter element list for JSON serialization
   *
   * @return Parameter list
   */
  @JsonGetter("parameters")
  public Map<String, Object> getParameterObject() {

    // Variable definition
    Map<String, Object> parameterMap = new HashMap<>();

    // Get elements (columns)
    for (Element element : getElementList()) {
      if (element instanceof WidgetParameter) {
        WidgetParameter param = (WidgetParameter) element;
        if (param.getName() != null) {
          parameterMap.put(param.getName(), param.getParameterValue());
        }
      }
    }

    // Return parameter list
    return parameterMap;
  }

  @JsonIgnore
  @Override
  public <T extends Element> List<T> getElementList() {
    return super.getElementList();
  }
}
