package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * ChartLegend Class
 *
 * Used to parse a chart legend tag with XStream
 *
 *
 * Generates an Chart widget
 *
 *
 * @author Pablo VIDAL - 20/OCT/2014
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("chart-legend")
public class ChartLegend extends AbstractChart {

  private static final long serialVersionUID = 3276211655233499386L;

  // Flag enable chart legend
  @XStreamAlias("enabled")
  @XStreamAsAttribute
  private Boolean enabled;

  // Layout of the legend items
  @XStreamAlias("layout")
  @XStreamAsAttribute
  private String layout;

  // Align of chart legend
  @XStreamAlias("align")
  @XStreamAsAttribute
  private String align;

  // Vertical align of chart legend
  @XStreamAlias("verticalAlign")
  @XStreamAsAttribute
  private String verticalAlign;

  // Chart legend floating
  @XStreamAlias("floating")
  @XStreamAsAttribute
  private Boolean floating;

  // The width of the drawn border around the legend
  @XStreamAlias("border-width")
  @XStreamAsAttribute
  private Integer borderWidth;

  // Chart legend parameter list
  @XStreamImplicit
  private List<ChartParameter> legendParameterList;

  @Override
  public ChartLegend copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .legendParameterList(ListUtil.copyList(getLegendParameterList()))
      .build();
  }

  /**
   * Returns is enabled
   * @return Is enabled
   */
  public boolean isEnabled() {
    return enabled != null && enabled;
  }

  /**
   * Returns is floating
   * @return Is floating
   */
  public boolean isFloating() {
    return floating != null && floating;
  }

  /**
   * Get chart legend model
   *
   * @return Legend model
   */
  public ObjectNode getModel() {

    // Variable definition
    JsonNodeFactory factory = JsonNodeFactory.instance;
    ObjectNode legendNode = factory.objectNode();

    // Add enable
    legendNode.put(ChartConstants.ENABLED, isEnabled());

    // Layout type
    if (getLayout() != null) {
      legendNode.put(ChartConstants.LAYOUT, getLayout());
    }

    // Add legend title
    if (getLabel() != null) {
      ObjectNode nodeTitle = factory.objectNode();
      nodeTitle.put(ChartConstants.TEXT, getLabel());
      legendNode.set(ChartConstants.TITLE, nodeTitle);
    }

    // Add horizontal align
    if (getAlign() != null) {
      legendNode.put(ChartConstants.ALIGN, getAlign());
    }

    // Add vertical align
    if (getVerticalAlign() != null) {
      legendNode.put(ChartConstants.VERTICAL_ALIGN, getVerticalAlign());
    }

    // Add if legend is floating
    legendNode.put(ChartConstants.FLOATING, isFloating());

    // Add border width
    if (getBorderWidth() != null) {
      legendNode.set(ChartConstants.BORDER_WIDTH, factory.numberNode(Integer.valueOf(getBorderWidth())));
    }

    // Update model with chart parameters
    addParameters(legendNode);

    return legendNode;
  }
}
