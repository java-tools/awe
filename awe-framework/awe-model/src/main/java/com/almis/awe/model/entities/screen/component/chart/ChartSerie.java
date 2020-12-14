package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ChartSerie Class
 * <p>
 * Used to parse a chart Serie tag with XStream
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
@XStreamAlias("chart-serie")
public class ChartSerie extends AbstractChart {

  private static final long serialVersionUID = -3249310197282122907L;

  // Color of serie
  @XStreamAlias("color")
  @XStreamAsAttribute
  private String color;

  // Index xAxis of serie
  @XStreamAlias("x-axis")
  @XStreamAsAttribute
  private String xAxis;

  // Index yAxis of serie
  @XStreamAlias("y-axis")
  @XStreamAsAttribute
  private String yAxis;

  // Point value of serie X
  @XStreamAlias("x-value")
  @XStreamAsAttribute
  private String xValue;

  // Point value of serie Y
  @XStreamAlias("y-value")
  @XStreamAsAttribute
  private String yValue;

  // Point value of serie Z
  @XStreamAlias("z-value")
  @XStreamAsAttribute
  private String zValue;

  // Id serie for drilldown
  @XStreamAlias("drilldown-serie")
  @XStreamAsAttribute
  private String drillDownSerie;

  // Flag if serie is type drilldown
  @XStreamAlias("drilldown")
  @XStreamAsAttribute
  private Boolean drillDown;

  // Chart serie data
  @XStreamImplicit
  private transient List<ChartSeriePoint> data;

  @Override
  public ChartSerie copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .data(ListUtil.copyList(getData()))
      .build();
  }

  /**
   * Retrieve Json model node
   *
   * @return Model node
   */
  public Map<String, Object> getModel() {

    // Variable definition
    Map<String, Object> model = new HashMap<>();

    // Add id serie
    if (StringUtils.isNotBlank(getId())) {
      model.put(ChartConstants.ID, getId());
    }

    // Add name of serie
    if (StringUtils.isNotBlank(getLabel())) {
      model.put(ChartConstants.NAME, getLabel());
    }

    // Add type of serie
    if (StringUtils.isNotBlank(getType())) {
      model.put(ChartConstants.TYPE, getType());
    }

    // Add color of serie
    if (StringUtils.isNotBlank(getColor())) {
      model.put(ChartConstants.COLOR, getColor());
    }

    // Add index xAxix
    if (StringUtils.isNotBlank(getXAxis())) {
      model.put(ChartConstants.X_AXIS, Integer.valueOf(getXAxis()));
    }

    // Add index yAxix
    if (StringUtils.isNotBlank(getYAxis())) {
      model.put(ChartConstants.Y_AXIS, Integer.valueOf(getYAxis()));
    }

    // Add xValue field name
    if (StringUtils.isNotBlank(getXValue())) {
      model.put(ChartConstants.X_VALUE, getXValue());
    }
    // Add yValue field name
    if (StringUtils.isNotBlank(getYValue())) {
      model.put(ChartConstants.Y_VALUE, getYValue());
    }
    // Add zValue field name
    if (StringUtils.isNotBlank(getZValue())) {
      model.put(ChartConstants.Z_VALUE, getZValue());
    }

    // Add drilldown serie id
    if (StringUtils.isNotBlank(getDrillDownSerie())) {
      model.put(ChartConstants.DRILL_DOWN, getDrillDownSerie());
    }

    // Add data to serie id
    if (getData() != null) {
      model.put(ChartConstants.DATA, getData());
    }

    // Add extra parameters
    addParameters(model);

    return model;
  }
}
