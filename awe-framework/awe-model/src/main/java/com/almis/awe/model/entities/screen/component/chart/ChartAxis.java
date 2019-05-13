package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * ChartAxis Class
 *
 * Used to parse a chart Axis tag with XStream
 *
 *
 * Generates an Chart widget
 *
 *
 * @author Pablo VIDAL - 21/OCT/2014
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
public class ChartAxis extends AbstractChart {

  private static final long serialVersionUID = -870412966991859326L;

  // Label format of axis
  @XStreamAlias("label-format")
  @XStreamAsAttribute
  private String labelFormat;

  // Label format of axis with predefined functions
  @XStreamAlias("formatter-function")
  @XStreamAsAttribute
  private String formatterFunction;

  // Label rotation of axis
  @XStreamAlias("label-rotation")
  @XStreamAsAttribute
  private Float labelRotation;

  // Tick interval of axis label
  @XStreamAlias("tick-interval")
  @XStreamAsAttribute
  private String tickInterval;

  // Allow decimals on ticks
  @XStreamAlias("allow-decimal")
  @XStreamAsAttribute
  private Boolean allowDecimal;

  // Set opposite to axis
  @XStreamAlias("opposite")
  @XStreamAsAttribute
  private Boolean opposite;

  @Override
  public ChartAxis copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Returns if allow decimals
   * @return Allow decimals
   */
  public boolean isAllowDecimal() {
    return allowDecimal != null && allowDecimal;
  }

  /**
   * Returns if is opposite
   * @return Is opposite
   */
  public boolean isOpposite() {
    return opposite != null && opposite;
  }

  /**
   * Get Axis Model
   *
   * @return Axis model
   */
  public ObjectNode getModel() {

    // Variable definition
    JsonNodeFactory factory = JsonNodeFactory.instance;
    ObjectNode axisNode = factory.objectNode();

    // ----------- labels node in axis ------------
    // --------------------------------------------------
    ObjectNode labelNode = factory.objectNode();

    // Set axis title
    if (getLabel() != null) {
      ObjectNode nodeTitle = factory.objectNode();
      nodeTitle.put(ChartConstants.TEXT, getLabel());
      axisNode.set(ChartConstants.TITLE, nodeTitle);
    }

    // Set label format
    if (getLabelFormat() != null) {
      labelNode.put(ChartConstants.FORMAT, getLabelFormat());
    }

    // Set label format
    if (getFormatterFunction() != null) {
      labelNode.put(ChartConstants.FORMATTER, getFormatterFunction());
    }

    // Set label rotation
    if (getLabelRotation() != null) {
      labelNode.put(ChartConstants.ROTATION, getLabelRotation());
    }

    // Set labels node
    axisNode.set(ChartConstants.LABELS, labelNode);
    // ----------------------------------------------------

    // Set axis type
    if (getType() != null) {
      axisNode.put(ChartConstants.TYPE, getType());
    }

    // Set tick-interval
    if (getTickInterval() != null) {
      axisNode.put(ChartConstants.TICK_INTERVAL, Integer.valueOf(getTickInterval()));
    }

    // Set allow-decimal
    axisNode.put(ChartConstants.ALLOW_DECIMALS, isAllowDecimal());

    // Axis opposite
    axisNode.put(ChartConstants.OPPOSITE, isOpposite());

    // Update model with chart parameters
    addParameters(axisNode);

    return axisNode;
  }
}
