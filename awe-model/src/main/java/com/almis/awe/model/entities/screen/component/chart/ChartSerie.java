package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * ChartSerie Class
 *
 * Used to parse a chart Serie tag with XStream
 * Generates an Chart widget
 *
 * @author Pablo VIDAL - 21/OCT/2014
 */
@XStreamAlias("chart-serie")
public class ChartSerie extends ChartModel {

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
  private String drillDown;

  // Chart serie data
  @XStreamImplicit
  private transient List<ChartSeriePoint> data;

  /**
   * Default constructor
   */
  public ChartSerie() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ChartSerie(ChartSerie other) throws AWException {
    super(other);
    this.color = other.color;
    this.xAxis = other.xAxis;
    this.yAxis = other.yAxis;
    this.xValue = other.xValue;
    this.yValue = other.yValue;
    this.zValue = other.zValue;
    this.drillDownSerie = other.drillDownSerie;
    this.drillDown = other.drillDown;
    this.data = ListUtil.copyList(other.data);
  }

  @Override
  public ChartSerie copy() throws AWException {
    return new ChartSerie(this);
  }

  /**
   * Get data of serie
   *
   * @return serie data
   */
  public List<ChartSeriePoint> getData() {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    return data;
  }

  /**
   * Set serie data
   *
   * @param data Serie data
   */
  public void setData(List<ChartSeriePoint> data) {
    this.data = data;
  }

  /**
   * Get serie color
   *
   * @return color serie
   */
  public String getColor() {
    return color;
  }

  /**
   * Set color of serie
   *
   * @param color Serie color
   */
  public void setColor(String color) {
    this.color = color;
  }

  /**
   * Get xAxis index of serie
   *
   * @return xAxis
   */
  public String getxAxis() {
    return xAxis;
  }

  /**
   * Set xAxis index to serie
   *
   * @param xAxis Serie x axis
   */
  public void setxAxis(String xAxis) {
    this.xAxis = xAxis;
  }

  /**
   * Get yAxis index of serie
   *
   * @return yAxis
   */
  public String getyAxis() {
    return yAxis;
  }

  /**
   * Set yAxis index to serie
   *
   * @param yAxis Serie y axis
   */
  public void setyAxis(String yAxis) {
    this.yAxis = yAxis;
  }

  /**
   * Get query field name for xValue point of serie
   *
   * @return xValue
   */
  public String getxValue() {
    return xValue;
  }

  /**
   * Set query field name to xValue point of serie
   *
   * @param xValue Serie x value
   */
  public void setxValue(String xValue) {
    this.xValue = xValue;
  }

  /**
   * Get query field name for yValue point of serie
   *
   * @return yValue
   */
  public String getyValue() {
    return yValue;
  }

  /**
   * Set query field name to yValue point of serie
   *
   * @param yValue serie y value
   */
  public void setyValue(String yValue) {
    this.yValue = yValue;
  }

  /**
   * Get query field name for z point of serie
   *
   * @return zValue
   */
  public String getzValue() {
    return zValue;
  }

  /**
   * Set query field name to z point of serie
   *
   * @param zValue serie z value
   */
  public void setzValue(String zValue) {
    this.zValue = zValue;
  }

  /**
   * Serie id for drilldown
   *
   * @return drillDownSerie
   */
  public String getDrillDownSerie() {
    return drillDownSerie;
  }

  /**
   * Store serie id for drilldown
   *
   * @param drillDownSerie Drilldown serie
   */
  public void setDrillDownSerie(String drillDownSerie) {
    this.drillDownSerie = drillDownSerie;
  }

  /**
   * Retrieve flag if serie is drilldown type
   *
   * @return drillDown
   */
  public String getDrillDown() {
    return drillDown;
  }

  /**
   * Store drilldown serie
   *
   * @param drillDown Drilldown
   */
  public void setDrillDown(String drillDown) {
    this.drillDown = drillDown;
  }

  /**
   * Flat if it's drilldown serie
   *
   * @return drillDown
   */
  public boolean isDrillDownSerie() {
    return "true".equalsIgnoreCase(getDrillDown());
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
    if (getxAxis() != null) {
      serieNode.set(ChartConstants.X_AXIS, factory.numberNode(Integer.valueOf(getxAxis())));
    }

    // Add index yAxix
    if (getyAxis() != null) {
      serieNode.set(ChartConstants.Y_AXIS, factory.numberNode(Integer.valueOf(getyAxis())));
    }

    // Add xValue field name
    if (getxValue() != null && !getxValue().isEmpty()) {
      serieNode.put(ChartConstants.X_VALUE, getxValue());
    }
    // Add yValue field name
    if (getyValue() != null && !getyValue().isEmpty()) {
      serieNode.put(ChartConstants.Y_VALUE, getyValue());
    }
    // Add zValue field name
    if (getzValue() != null && !getzValue().isEmpty()) {
      serieNode.put(ChartConstants.Z_VALUE, getzValue());
    }

    // Add drilldown serie id
    if (getDrillDownSerie() != null) {
      serieNode.put(ChartConstants.DRILL_DOWN, getDrillDownSerie());
    }

    // Add extra parameters
    addParameters(serieNode);

    return serieNode;
  }
}
