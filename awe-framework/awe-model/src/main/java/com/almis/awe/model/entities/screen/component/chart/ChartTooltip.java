package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.CrosshairType;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("chart-tooltip")
public class ChartTooltip extends AbstractChart {

  private static final long serialVersionUID = 8810846715874354742L;
  private static final String CROSSHAIRS_TEXT = "crosshairs";

  // Enable tooltip in chart
  @XStreamAlias("enabled")
  @XStreamAsAttribute
  private Boolean enabled;

  // Set crosshair lines in chart
  @XStreamAlias(CROSSHAIRS_TEXT)
  @XStreamAsAttribute
  private String crosshairs;

  // Number of decimals in tooltip
  @XStreamAlias("number-decimals")
  @XStreamAsAttribute
  private Integer numberDecimals;

  // Suffix string in tooltip
  @XStreamAlias("suffix")
  @XStreamAsAttribute
  private String suffix;

  // Preffix string in tooltip
  @XStreamAlias("prefix")
  @XStreamAsAttribute
  private String prefix;

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
  private Boolean shared;

  @Override
  public ChartTooltip copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
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
  public boolean isShared() {
    return shared != null && shared;
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
    tooltipNode.put("enabled", isEnabled());

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
      tooltipNode.put(ChartConstants.VALUE_DECIMALS, getNumberDecimals());
    }

    // Add prefix
    if (getPrefix() != null) {
      tooltipNode.put(ChartConstants.PREFIX, getPrefix());
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
    tooltipNode.put(ChartConstants.SHARED, isShared());

    // Add extra parameters
    addParameters(tooltipNode);

    return tooltipNode;
  }
}
