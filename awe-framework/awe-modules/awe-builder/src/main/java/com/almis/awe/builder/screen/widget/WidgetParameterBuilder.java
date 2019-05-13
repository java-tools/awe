
package com.almis.awe.builder.screen.widget;

import com.almis.awe.builder.enumerates.DataType;
import com.almis.awe.builder.screen.base.AweBuilder;
import com.almis.awe.model.entities.screen.component.widget.WidgetParameter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author dfuentes
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class WidgetParameterBuilder extends AweBuilder<WidgetParameterBuilder, WidgetParameter> {

  private DataType type;
  private String value;

  @Override
  public WidgetParameter build() {
    return build(new WidgetParameter());
  }

  @Override
  public WidgetParameter build(WidgetParameter widgetParameter) {
    super.build(widgetParameter)
      .setValue(getValue());

    if (getType() != null) {
      widgetParameter.setType(getType().toString());
    }

    return widgetParameter;
  }

  /**
   * add widget parameter
   *
   * @param widgetParameterList
   * @return
   */
  public WidgetParameterBuilder addWidgetParameter(WidgetParameterBuilder... widgetParameterList) {
    addAllElements(widgetParameterList);
    return this;
  }
}
