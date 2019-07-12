package com.almis.awe.builder.client.grid;

import com.almis.awe.builder.enumerates.RowPosition;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

/**
 * Add rows action builder
 *
 * @author pgarcia
 */
public class AddRowActionBuilder extends RowsActionBuilder<AddRowActionBuilder> {

  private static final String TYPE = "add-row";

  /**
   * Empty constructor
   */
  public AddRowActionBuilder() {
    super(TYPE, RowPosition.BOTTOM);
  }


  /**
   * Add row with position builder
   */
  public AddRowActionBuilder(RowPosition position) {
    super(TYPE, position);
  }

  /**
   * Constructor with target and column list
   *
   * @param position Row position
   * @param target Target
   */
  public AddRowActionBuilder(RowPosition position, String target) {
    super(TYPE, position, target);
  }

  /**
   * Constructor with target and column list
   *
   * @param position Row position
   * @param target Target
   * @param row Row values
   */
  public AddRowActionBuilder(RowPosition position, String target, Map<String, Object> row) {
    super(TYPE, position, target, row);
  }

  /**
   * Constructor with target and column list
   *
   * @param position Row position
   * @param target Target
   * @param row Row values
   */
  public AddRowActionBuilder(RowPosition position, String target, ObjectNode row) {
    super(TYPE, position, target, row);
  }
}
