package com.almis.awe.builder.client.grid;

import com.almis.awe.builder.client.ClientActionBuilder;

/**
 * Unselect all rows action builder
 *
 * @author pgarcia
 */
public class UnselectAllRowsActionBuilder extends ClientActionBuilder<UnselectAllRowsActionBuilder> {

  private static final String TYPE = "unselect-all-rows";

  /**
   * Empty constructor
   */
  public UnselectAllRowsActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with target
   * @param target Target grid id
   */
  public UnselectAllRowsActionBuilder(String target) {
    setType(TYPE)
      .setTarget(target);
  }
}
