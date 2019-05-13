
package com.almis.awe.builder.screen.widget;

import com.almis.awe.builder.enumerates.WidgetComponent;
import com.almis.awe.builder.screen.base.AbstractComponentBuilder;
import com.almis.awe.model.entities.screen.component.widget.Widget;
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
public class WidgetBuilder extends AbstractComponentBuilder<WidgetBuilder, Widget> {

  private WidgetComponent component;

  @Override
  public Widget build() {
    return build(new Widget());
  }

  @Override
  public Widget build(Widget widget) {
    super.build(widget);

    if (getComponent() != null) {
      widget.setComponentType(getComponent().toString());
    }

    return widget;
  }

  /**
   * Add widget parameter
   *
   * @param widgetParameterBuilder
   * @return
   */
  public WidgetBuilder addWidgetParameter(WidgetParameterBuilder... widgetParameterBuilder) {
    addAllElements(widgetParameterBuilder);
    return this;
  }
}
