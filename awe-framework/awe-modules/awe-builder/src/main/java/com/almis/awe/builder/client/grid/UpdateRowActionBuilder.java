package com.almis.awe.builder.client.grid;

import com.almis.awe.builder.client.ClientActionBuilder;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

/**
 * Update row action builder
 *
 * @author pgarcia
 */
public class UpdateRowActionBuilder extends ClientActionBuilder<UpdateRowActionBuilder> {

  private static final String TYPE = "update-row";
  private static final String ROW = "row";
  private static final String ROW_ID = "rowId";

  /**
   * Empty constructor
   */
  public UpdateRowActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with target and row data
   * @param target Target grid id
   * @param row Row data
   */
  public UpdateRowActionBuilder(String target, Map<String, Object> row) {
    setType(TYPE)
      .setTarget(target)
      .addParameter(ROW, row);
  }

  /**
   * Constructor with target, row id and row data
   * @param target Target grid id
   * @param rowId Row identifier
   * @param row Row data
   */
  public UpdateRowActionBuilder(String target, String rowId, Map<String, Object> row) {
    setType(TYPE)
      .setTarget(target)
      .addParameter(ROW_ID, rowId)
      .addParameter(ROW, row);
  }

  /**
   * Constructor with target and row data
   * @param target Target grid id
   * @param row Row data
   */
  public UpdateRowActionBuilder(String target, ObjectNode row) {
    setType(TYPE)
      .setTarget(target)
      .addParameter(ROW, row);
  }

  /**
   * Constructor with target, row id and row data
   * @param target Target grid id
   * @param rowId Row identifier
   * @param row Row data
   */
  public UpdateRowActionBuilder(String target, String rowId, ObjectNode row) {
    setType(TYPE)
      .setTarget(target)
      .addParameter(ROW_ID, rowId)
      .addParameter(ROW, row);
  }
}
