package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
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
@XStreamAlias("chart-legend")
public class ChartLegend extends ChartModel {

  private static final long serialVersionUID = 3276211655233499386L;

  // Flag enable chart legend
  @XStreamAlias("enabled")
  @XStreamAsAttribute
  private String enabled;

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
  private String floating;

  // The width of the drawn border around the legend
  @XStreamAlias("border-width")
  @XStreamAsAttribute
  private String borderWidth;

  // Chart legend parameter list
  @XStreamImplicit
  private List<ChartParameter> legendParameterList;

  /**
   * Default constructor
   */
  public ChartLegend() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ChartLegend(ChartLegend other) throws AWException {
    super(other);
    this.enabled = other.enabled;
    this.layout = other.layout;
    this.align = other.align;
    this.verticalAlign = other.verticalAlign;
    this.floating = other.floating;
    this.borderWidth = other.borderWidth;

    if (other.legendParameterList != null) {
      this.legendParameterList = new ArrayList<>();
      for (ChartParameter parameter : other.legendParameterList) {
        this.legendParameterList.add(new ChartParameter(parameter));
      }
    }
  }

  @Override
  public ChartLegend copy() throws AWException {
    return new ChartLegend(this);
  }

  /**
   * Get flag to enabled legend of char
   *
   * @return Legend enabled
   */
  public String getEnabled() {
    return enabled;
  }

  /**
   * Set flat to enable chart legend
   *
   * @param enabled flag legend enabled
   * @return ChartLegend
   */
  public ChartLegend setEnabled(String enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * If legend chart is enabled
   *
   * @return Legend is enabled
   */
  public boolean isEnabled() {
    return !"false".equalsIgnoreCase(getEnabled());
  }

  /**
   * Retrieve align
   *
   * @return align
   */
  public String getAlign() {
    return align;
  }

  /**
   * Store align legend
   *
   * @param align Legend align
   * @return ChartLegend
   */
  public ChartLegend setAlign(String align) {
    this.align = align;
    return this;
  }

  /**
   * Retrieve vertical align
   *
   * @return vertical align
   */
  public String getVerticalAlign() {
    return verticalAlign;
  }

  /**
   * Store vertical align
   *
   * @param verticalAlign Vertical align
   * @return ChartLegend
   */
  public ChartLegend setVerticalAlign(String verticalAlign) {
    this.verticalAlign = verticalAlign;
    return this;
  }

  /**
   * Retrieve floating legend
   *
   * @return floating legend
   */
  public String getFloating() {
    return floating;
  }

  /**
   * Store floating legend flag
   *
   * @param floating Legend is floating
   * @return ChartLegend
   */
  public ChartLegend setFloating(String floating) {
    this.floating = floating;
    return this;
  }

  /**
   * Check if legend is floating
   *
   * @return flag floating
   */
  public boolean isFloating() {
    return "true".equalsIgnoreCase(getFloating());
  }

  /**
   * Retrieve border width of legend
   *
   * @return border width
   */
  public String getBorderWidth() {
    return borderWidth;
  }

  /**
   * Store border width to legend
   *
   * @param borderWidth Legend border width
   * @return ChartLegend
   */
  public ChartLegend setBorderWidth(String borderWidth) {
    this.borderWidth = borderWidth;
    return this;
  }

  /**
   * Retrieve layout of legend items
   *
   * @return layout
   */
  public String getLayout() {
    return layout;
  }

  /**
   * Store layout of legend items
   *
   * @param layout Legend layout
   * @return ChartLegend
   */
  public ChartLegend setLayout(String layout) {
    this.layout = layout;
    return this;
  }

  /**
   * Retrieve parameter list of chart legend element
   *
   * @return legend parameter list
   */
  public List<ChartParameter> getLegendParameterList() {
    return legendParameterList;
  }

  /**
   * Store the parameter list to chart legend element
   *
   * @param legendParameterList Legend parameter list
   * @return ChartLegend
   */
  public ChartLegend setLegendParameterList(List<ChartParameter> legendParameterList) {
    this.legendParameterList = legendParameterList;
    return this;
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
    if (getEnabled() != null) {
      legendNode.put(ChartConstants.ENABLED, isEnabled());
    }

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
    if (getFloating() != null) {
      legendNode.put(ChartConstants.FLOATING, isFloating());
    }

    // Add border width
    if (getBorderWidth() != null) {
      legendNode.set(ChartConstants.BORDER_WIDTH, factory.numberNode(Integer.valueOf(getBorderWidth())));
    }

    // Update model with chart parameters
    addParameters(legendNode);

    return legendNode;
  }
}
