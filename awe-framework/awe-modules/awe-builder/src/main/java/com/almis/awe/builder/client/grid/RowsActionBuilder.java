package com.almis.awe.builder.client.grid;

import com.almis.awe.builder.client.ClientActionBuilder;
import com.almis.awe.builder.enumerates.RowPosition;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

/**
 * Rows action builder
 *
 * @author pgarcia
 */
abstract class RowsActionBuilder<T> extends ClientActionBuilder<T> {

  private static final String ROW = "row";

  /**
   * Set type depending on row position
   * @param action Row action
   * @param position Row position
   */
  private T setType(String action, RowPosition position) {
    String fullAction = action;
    if (position != null) {
      switch (position) {
        case UP:
          fullAction = action + "-up";
          break;
        case DOWN:
          fullAction = action + "-down";
          break;
        case TOP:
          fullAction = action + "-top";
          break;
        case BOTTOM:
        default:
          break;
      }
    }
    setType(fullAction);
    return (T) this;
  }

  /**
   * Base constructor
   *
   * @param type Type
   * @param position Row position
   */
  public RowsActionBuilder(String type, RowPosition position) {
    setType(type, position);
  }

  /**
   * Constructor with target
   *
   * @param type    Type
   * @param position Row position
   * @param target  Target
   */
  public RowsActionBuilder(String type, RowPosition position, String target) {
    setType(type, position);
    setTarget(target);
  }

  /**
   * Constructor with target and column list
   *
   * @param type    Type
   * @param position Row position
   * @param target  Target
   * @param row Row values
   */
  public RowsActionBuilder(String type, RowPosition position, String target, Map row) {
    setType(type, position);
    setTarget(target);
    addParameter(ROW, row);
  }

  /**
   * Constructor with target and column list
   *
   * @param type    Type
   * @param position Row position
   * @param target  Target
   * @param row Row values
   */
  public RowsActionBuilder(String type, RowPosition position, String target, ObjectNode row) {
    setType(type, position);
    setTarget(target);
    addParameter(ROW, row);
  }
}
