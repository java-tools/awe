package com.almis.awe.model.entities.screen.component.widget;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * Widget Class
 * Used to parse a widget tag with XStream
 * Generates an screen widget
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("widget")
public class Widget extends AbstractWidget {

  private static final long serialVersionUID = 7140906386094836409L;

  @Override
  public Widget copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Returns the parameter element list for JSON serialization
   *
   * @return Parameter list
   * @throws AWException Error retrieving converter
   */
  @JsonGetter("parameters")
  public Map<String, Object> getWidgetParametersConverter() throws AWException {
    return super.getParameterObject();
  }

  @JsonIgnore
  @Override
  public String getComponentTag() {
    return getType();
  }
}
