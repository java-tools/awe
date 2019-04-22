package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

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
public class ChartAxis extends ChartModel {

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
  private String labelRotation;

  // Tick interval of axis label
  @XStreamAlias("tick-interval")
  @XStreamAsAttribute
  private String tickInterval;

  // Allow decimals on ticks
  @XStreamAlias("allow-decimal")
  @XStreamAsAttribute
  private String allowDecimal;

  // Set opposite to axis
  @XStreamAlias("opposite")
  @XStreamAsAttribute
  private String opposite;

  /**
   * Default constructor
   */
  public ChartAxis() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ChartAxis(ChartAxis other) throws AWException {
    super(other);
    this.labelFormat = other.labelFormat;
    this.formatterFunction = other.formatterFunction;
    this.labelRotation = other.labelRotation;
    this.tickInterval = other.tickInterval;
    this.allowDecimal = other.allowDecimal;
    this.opposite = other.opposite;
  }

  @Override
  public ChartAxis copy() throws AWException {
    return new ChartAxis(this);
  }

  /**
   * Retrive label format
   *
   * @return labelFormat
   */
  public String getLabelFormat() {
    return labelFormat;
  }

  /**
   * Store label format
   *
   * @param labelFormat Label format
   */
  public void setLabelFormat(String labelFormat) {
    this.labelFormat = labelFormat;
  }

  /**
   * @return the formatterFunction
   */
  public String getFormatterFunction() {
    return formatterFunction;
  }

  /**
   * @param formatterFunction the formatterFunction to set
   */
  public void setFormatterFunction(String formatterFunction) {
    this.formatterFunction = formatterFunction;
  }

  /**
   * Retrieve rotation of axis labels
   *
   * @return labelRotation
   */
  public String getLabelRotation() {
    return labelRotation;
  }

  /**
   * Set rotation to axis labels
   *
   * @param labelRotation Label rotation
   */
  public void setLabelRotation(String labelRotation) {
    this.labelRotation = labelRotation;
  }

  /**
   * Retrieve tick intervals
   *
   * @return tickInterval
   */
  public String getTickInterval() {
    return tickInterval;
  }

  /**
   * Set interval to axis tick
   *
   * @param tickInterval Tick interval
   */
  public void setTickInterval(String tickInterval) {
    this.tickInterval = tickInterval;
  }

  /**
   * Retrive flag allow decimals on axis
   *
   * @return allowDecimal
   */
  public String getAllowDecimal() {
    return allowDecimal;
  }

  /**
   * Set flag to enable decimal values on axis
   *
   * @param allowDecimal Allow decimals
   */
  public void setAllowDecimal(String allowDecimal) {
    this.allowDecimal = allowDecimal;
  }

  /**
   * If allow decimals on axis
   *
   * @return allowDecimal flag
   */
  public boolean isAllowDecimal() {
    return "true".equalsIgnoreCase(getAllowDecimal());
  }

  /**
   * Retrieve flag axis is opposite
   *
   * @return opposite
   */
  public String getOpposite() {
    return opposite;
  }

  /**
   * Set flag opposite axis
   *
   * @param opposite Is opposite
   */
  public void setOpposite(String opposite) {
    this.opposite = opposite;
  }

  /**
   * If axis is opposite
   *
   * @return flag opposite
   */
  public boolean isOpposite() {
    return "true".equalsIgnoreCase(getOpposite());
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
      labelNode.put(ChartConstants.ROTATION, Float.valueOf(getLabelRotation()));
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
    if (getAllowDecimal() != null) {
      axisNode.put(ChartConstants.ALLOW_DECIMALS, isAllowDecimal());
    }

    // Axis opposite
    if (getOpposite() != null) {
      axisNode.put(ChartConstants.OPPOSITE, isOpposite());
    }

    // Update model with chart parameters
    addParameters(axisNode);

    return axisNode;
  }
}
