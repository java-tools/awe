package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * ChartAxis Class
 * <p>
 * Used to parse a chart Axis tag with XStream
 * <p>
 * <p>
 * Generates an Chart widget
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
   *
   * @return Allow decimals
   */
  public boolean isAllowDecimal() {
    return allowDecimal != null && allowDecimal;
  }

  /**
   * Returns if is opposite
   *
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
  public Map<String, Object> getModel() {
    Map<String, Object> model = new HashMap<>();

    // Set axis title
    if (getLabel() != null) {
      model.put(ChartConstants.TITLE, getTextParameter(getLabel()));
    }

    // Set labels node
    model.put(ChartConstants.LABELS, getLabelParameter(getLabelFormat(), getFormatterFunction(), getLabelRotation()));
    // ----------------------------------------------------

    // Set axis type
    if (getType() != null) {
      model.put(ChartConstants.TYPE, getType());
    }

    // Set tick-interval
    if (getTickInterval() != null) {
      model.put(ChartConstants.TICK_INTERVAL, Integer.valueOf(getTickInterval()));
    }

    // Set allow-decimal
    model.put(ChartConstants.ALLOW_DECIMALS, isAllowDecimal());

    // Axis opposite
    model.put(ChartConstants.OPPOSITE, isOpposite());

    // Update model with chart parameters
    addParameters(model);

    return model;
  }
}
