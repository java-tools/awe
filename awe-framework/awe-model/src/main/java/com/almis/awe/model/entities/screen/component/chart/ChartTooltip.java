package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.CrosshairType;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ChartTooltip Class
 * <p>
 * Used to parse a chart tooltip tag with XStream
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
   *
   * @return Is enabled
   */
  public boolean isEnabled() {
    return enabled == null || enabled;
  }

  /**
   * Returns is floating
   *
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
  public Map<String, Object> getModel() {

    // Variable definition
    Map<String, Object> model = new HashMap<>();
    model.put("enabled", isEnabled());

    // Add crosshairs
    if (getCrosshairs() != null) {
      // Crosshair type
      CrosshairType crosshairType = CrosshairType.valueOf(getCrosshairs().toUpperCase());
      List<Object> crosshairList = new ArrayList<>();
      model.put(CROSSHAIRS_TEXT, crosshairList);
      switch (crosshairType) {
        case XAXIS:
          crosshairList.add(Boolean.TRUE);
          crosshairList.add(Boolean.FALSE);
          break;
        case YAXIS:
          crosshairList.add(Boolean.FALSE);
          crosshairList.add(Boolean.TRUE);
          break;
        case ALL:
          crosshairList.add(Boolean.TRUE);
          crosshairList.add(Boolean.TRUE);
          break;
        default:
          crosshairList.add(Boolean.FALSE);
          crosshairList.add(Boolean.FALSE);
      }
    }

    // Add number of decimals
    if (getNumberDecimals() != null) {
      model.put(ChartConstants.VALUE_DECIMALS, getNumberDecimals());
    }

    // Add prefix
    if (getPrefix() != null) {
      model.put(ChartConstants.PREFIX, getPrefix());
    }

    // Add suffix
    if (getSuffix() != null) {
      model.put(ChartConstants.SUFFIX, getSuffix());
    }

    // Add point format
    if (getPointFormat() != null) {
      model.put(ChartConstants.POINT_FORMAT, getPointFormat());
    }

    // Add date format
    if (getDateFormat() != null) {
      model.put(ChartConstants.DATE_FORMAT, getDateFormat());
    }

    // Add shared tooltip
    model.put(ChartConstants.SHARED, isShared());

    // Add extra parameters
    addParameters(model);

    return model;
  }
}
