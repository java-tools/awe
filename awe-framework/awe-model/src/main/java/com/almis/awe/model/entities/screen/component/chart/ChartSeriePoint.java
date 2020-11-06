package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Bean to define a chart serie point
 *
 * @author pvidal
 */
public class ChartSeriePoint implements Copyable {

  private transient List<JsonNode> positionPoint;

  /**
   * Copy constructor
   *
   * @param seriePoint series point
   */
  public ChartSeriePoint(ChartSeriePoint seriePoint) {
    if (seriePoint.positionPoint != null) {
      this.positionPoint = new ArrayList<>();
      for (JsonNode point : seriePoint.positionPoint) {
        this.positionPoint.add(point.deepCopy());
      }
    }
  }

  /**
   * Constructor range point
   *
   * @param points Point list
   */
  public ChartSeriePoint(Object... points) {
    ObjectMapper mapper = new ObjectMapper();
    this.positionPoint = new ArrayList<>();
    Arrays.stream(points).forEach(point -> positionPoint.add(mapper.valueToTree(point)));
  }

  /**
   * Get position point
   *
   * @return the positionPoint
   */
  @JsonValue
  public List<JsonNode> getPositionPoint() {
    return positionPoint;
  }

  /**
   * Set position point
   *
   * @param positionPoint the positionPoint to set
   */
  public void setPositionPoint(List<JsonNode> positionPoint) {
    this.positionPoint = positionPoint;
  }

  @Override
  public ChartSeriePoint copy() throws AWException {
    return new ChartSeriePoint(this);
  }
}
