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
 * ChartSerie Class
 *
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
   * Returns is drill down
   * @return Is drill down
   */
  public boolean isDrillDown() {
    return drillDown != null && drillDown;
  }

  /**
   * Retrieve Json model node
   *
   * @return Model node
   */
  public ObjectNode getModel() {

    // Variable definition
    JsonNodeFactory factory = JsonNodeFactory.instance;
    ObjectNode serieNode = factory.objectNode();

    // Add id serie
    if (getId() != null) {
      serieNode.put(ChartConstants.ID, getId());
    }

    // Add name of serie
    if (getLabel() != null && !getLabel().isEmpty()) {
      serieNode.put(ChartConstants.NAME, getLabel());
    }

    // Add type of serie
    if (getType() != null) {
      serieNode.put(ChartConstants.TYPE, getType());
    }

    // Add color of serie
    if (getColor() != null) {
      serieNode.put(ChartConstants.COLOR, getColor());
    }

    // Add index xAxix
    if (getXAxis() != null) {
      serieNode.set(ChartConstants.X_AXIS, factory.numberNode(Integer.valueOf(getXAxis())));
    }

    // Add index yAxix
    if (getYAxis() != null) {
      serieNode.set(ChartConstants.Y_AXIS, factory.numberNode(Integer.valueOf(getYAxis())));
    }

    // Add xValue field name
    if (getXValue() != null && !getXValue().isEmpty()) {
      serieNode.put(ChartConstants.X_VALUE, getXValue());
    }
    // Add yValue field name
    if (getYValue() != null && !getYValue().isEmpty()) {
      serieNode.put(ChartConstants.Y_VALUE, getYValue());
    }
    // Add zValue field name
    if (getZValue() != null && !getZValue().isEmpty()) {
      serieNode.put(ChartConstants.Z_VALUE, getZValue());
    }

    // Add drilldown serie id
    if (isDrillDown() && getDrillDownSerie() != null) {
      serieNode.put(ChartConstants.DRILL_DOWN, getDrillDownSerie());
    }

    // Add extra parameters
    addParameters(serieNode);

    return serieNode;
  }
}
