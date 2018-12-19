package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.CrosshairType;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * ChartTooltip Class
 *
 * Used to parse a chart tooltip tag with XStream
 *
 *
 * Generates an Chart widget
 *
 *
 * @author Pablo VIDAL - 21/OCT/2014
 */
@XStreamAlias("chart-tooltip")
public class ChartTooltip extends ChartModel {

  private static final long serialVersionUID = 8810846715874354742L;
  private static final String CROSSHAIRS_TEXT = "crosshairs";

  // Enable tooltip in chart
  @XStreamAlias("enabled")
  @XStreamAsAttribute
  private String enabled;

  // Set crosshair lines in chart
  @XStreamAlias(CROSSHAIRS_TEXT)
  @XStreamAsAttribute
  private String crosshairs;

  // Number of decimals in tooltip
  @XStreamAlias("number-decimals")
  @XStreamAsAttribute
  private String numberDecimals;

  // Suffix string in tooltip
  @XStreamAlias("suffix")
  @XStreamAsAttribute
  private String suffix;

  // Preffix string in tooltip
  @XStreamAlias("prefix")
  @XStreamAsAttribute
  private String preffix;

  // Point format in tooltip
  @XStreamAlias("point-format")
  @XStreamAsAttribute
  private String pointFormat;

  // Format for the date if the X axis is a datetime axis
  @XStreamAlias("date-format")
  @XStreamAsAttribute
  private String dateFormat;

  // Shared tooltip for multiple series
  @XStreamAlias("shared")
  @XStreamAsAttribute
  private String shared;

  /**
   * Default constructor
   */
  public ChartTooltip() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ChartTooltip(ChartTooltip other) throws AWException {
    super(other);
    this.enabled = other.enabled;
    this.crosshairs = other.crosshairs;
    this.numberDecimals = other.numberDecimals;
    this.suffix = other.suffix;
    this.preffix = other.preffix;
    this.pointFormat = other.pointFormat;
    this.dateFormat = other.dateFormat;
    this.shared = other.shared;
  }

  @Override
  public ChartTooltip copy() throws AWException {
    return new ChartTooltip(this);
  }

  /**
   * Retrieve flag to activate tooltip
   *
   * @return enabled flag
   */
  public String getEnabled() {
    return enabled;
  }

  /**
   * Set flag to activate tooltip
   *
   * @param enabled Tooltip enabled
   */
  public void setEnable(String enabled) {
    this.enabled = enabled;
  }

  /**
   * If tooltip is activated
   *
   * @return enabled flag
   */
  public boolean isEnabled() {
    return getEnabled() == null || "true".equalsIgnoreCase(getEnabled());
  }

  /**
   * Retrive crosshairs type
   *
   * @return crosshairs type
   */
  public String getCrosshairs() {
    return crosshairs;
  }

  /**
   * Store crosshairs type
   *
   * @param crosshairs Tooltip crosshairs
   */
  public void setCrosshairs(String crosshairs) {
    this.crosshairs = crosshairs;
  }

  /**
   * Retrive number of decimals in tooltip value
   *
   * @return number of decimals
   */
  public String getNumberDecimals() {
    return numberDecimals;
  }

  /**
   * Store number of decimal
   *
   * @param numberDecimals Tooltip number of decimals
   */
  public void setNumberDecimals(String numberDecimals) {
    this.numberDecimals = numberDecimals;
  }

  /**
   * Retrive string preffix in tooltip
   *
   * @return suffix
   */
  public String getSuffix() {
    return suffix;
  }

  /**
   * Store suffix in tooltip
   *
   * @param suffix Tooltip suffix
   */
  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  /**
   * Retrieve prefix in tooltip
   *
   * @return prefix tooltip prefix
   */
  public String getPreffix() {
    return preffix;
  }

  /**
   * Store prefix in tooltip
   *
   * @param preffix Tooltip prefix
   */
  public void setPreffix(String preffix) {
    this.preffix = preffix;
  }

  /**
   * Retrive point format
   *
   * @return point format
   */
  public String getPointFormat() {
    return pointFormat;
  }

  /**
   * Store point format
   *
   * @param pointFormat Tooltip point format
   */
  public void setPointFormat(String pointFormat) {
    this.pointFormat = pointFormat;
  }

  /**
   * Retrieve date format
   *
   * @return date format
   */
  public String getDateFormat() {
    return dateFormat;
  }

  /**
   * Store date format
   *
   * @param dateFormat Tooltip date format
   */
  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  /**
   * @return the shared
   */
  public String getShared() {
    return shared;
  }

  /**
   * @param shared the shared to set
   */
  public void setShared(String shared) {
    this.shared = shared;
  }

  /**
   * Get tooltip model
   *
   * @return tooltip model
   */
  public ObjectNode getModel() {

    // Variable definition
    JsonNodeFactory factory = JsonNodeFactory.instance;
    ObjectNode tooltipNode = factory.objectNode();

    // Add enable
    if (getEnabled() != null) {
      tooltipNode.put("enabled", isEnabled());
    }

    // Add crosshairs
    if (getCrosshairs() != null) {
      // Crosshair type
      CrosshairType crosshairType = CrosshairType.valueOf(getCrosshairs().toUpperCase());
      ArrayNode crosshairNode = factory.arrayNode();
      tooltipNode.set(CROSSHAIRS_TEXT, crosshairNode);
      switch (crosshairType) {
        case XAXIS:
          crosshairNode.add(Boolean.TRUE);
          crosshairNode.add(Boolean.FALSE);
          break;
        case YAXIS:
          crosshairNode.add(Boolean.FALSE);
          crosshairNode.add(Boolean.TRUE);
          break;
        case ALL:
          crosshairNode.add(Boolean.TRUE);
          crosshairNode.add(Boolean.TRUE);
          break;
        default:
          crosshairNode.add(Boolean.FALSE);
          crosshairNode.add(Boolean.FALSE);
      }
    }

    // Add number of decimals
    if (getNumberDecimals() != null) {
      tooltipNode.put(ChartConstants.VALUE_DECIMALS, Integer.valueOf(getNumberDecimals()));
    }

    // Add prefix
    if (getPreffix() != null) {
      tooltipNode.put(ChartConstants.PREFIX, getPreffix());
    }

    // Add suffix
    if (getSuffix() != null) {
      tooltipNode.put(ChartConstants.SUFFIX, getSuffix());
    }

    // Add point format
    if (getPointFormat() != null) {
      tooltipNode.put(ChartConstants.POINT_FORMAT, getPointFormat());
    }

    // Add date format
    if (getDateFormat() != null) {
      tooltipNode.put(ChartConstants.DATE_FORMAT, getDateFormat());
    }

    // Add shared tootltip
    if (getShared() != null) {
      tooltipNode.put(ChartConstants.SHARED, Boolean.valueOf(getShared()));
    }

    // Add extra parameters
    addParameters(tooltipNode);

    return tooltipNode;
  }
}
