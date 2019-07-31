package com.almis.awe.builder.client.grid;

import com.almis.awe.builder.client.ClientActionBuilder;

/**
 * Delete row action builder
 *
 * @author pgarcia
 */
public class DeleteRowActionBuilder extends ClientActionBuilder<DeleteRowActionBuilder> {

  private static final String TYPE = "delete-row";
  private static final String ROW_ID = "rowId";

  /**
   * Empty constructor
   */
  public DeleteRowActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with target
   * @param target Target grid id
   */
  public DeleteRowActionBuilder(String target) {
    setType(TYPE)
      .setTarget(target);
  }

  /**
   * Constructor with target and row id
   * @param target Target grid id
   * @param rowId Row identifier
   */
  public DeleteRowActionBuilder(String target, String rowId) {
    setType(TYPE)
      .setTarget(target)
      .addParameter(ROW_ID, rowId);
  }
}
