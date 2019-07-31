package com.almis.awe.builder.client.grid;

import com.almis.awe.builder.client.ClientActionBuilder;

/**
 * Select all rows action builder
 *
 * @author pgarcia
 */
public class SelectAllRowsActionBuilder extends ClientActionBuilder<SelectAllRowsActionBuilder> {

  private static final String TYPE = "select-all-rows";

  /**
   * Empty constructor
   */
  public SelectAllRowsActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with target
   * @param target Target grid id
   */
  public SelectAllRowsActionBuilder(String target) {
    setType(TYPE)
      .setTarget(target);
  }
}
