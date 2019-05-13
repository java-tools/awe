package com.almis.awe.model.entities.screen.component.chart;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean to define a chart serie point
 *
 * @author pvidal
 */
public class ChartSeriePoint {

  private List<JsonNode> positionPoint;

  /**
   * Copy constructor
   *
   * @param other
   */
  public ChartSeriePoint(ChartSeriePoint other) {
    if (other.positionPoint != null) {
      this.positionPoint = new ArrayList<>();
      for (JsonNode point : other.positionPoint) {
        this.positionPoint.add(point.deepCopy());
      }
    }
  }

  /**
   * Constructor two dimension point
   *
   * @param xPoint
   * @param yPoint
   */
  public ChartSeriePoint(JsonNode xPoint, JsonNode yPoint) {
    this.positionPoint = new ArrayList<>();
    this.positionPoint.add(xPoint);
    this.positionPoint.add(yPoint);
  }

  /**
   * Constructor range point
   *
   * @param xPoint
   * @param yPoint
   * @param zPoint
   */
  public ChartSeriePoint(JsonNode xPoint, JsonNode yPoint, JsonNode zPoint) {
    this.positionPoint = new ArrayList<>();
    this.positionPoint.add(xPoint);
    this.positionPoint.add(yPoint);
    this.positionPoint.add(zPoint);
  }

  /**
   * @return the positionPoint
   */
  @JsonValue
  public List<JsonNode> getPositionPoint() {
    return positionPoint;
  }

  /**
   * @param positionPoint the positionPoint to set
   */
  public void setPositionPoint(List<JsonNode> positionPoint) {
    this.positionPoint = positionPoint;
  }
}
