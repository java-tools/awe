package com.almis.awe.builder.client;

import com.almis.awe.model.entities.actions.ComponentAddress;

/**
 * Fill action builder
 *
 * @author pgarcia
 */
public class FilterActionBuilder extends ClientActionBuilder<FilterActionBuilder> {

  private static final String TYPE = "filter";

  /**
   * Empty constructor
   */
  public FilterActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with target and datalist
   *
   * @param target Target
   */
  public FilterActionBuilder(String target) {
    setType(TYPE)
      .setTarget(target);
  }

  /**
   * Constructor with address and datalist
   *
   * @param address Target
   */
  public FilterActionBuilder(ComponentAddress address) {
    setType(TYPE)
      .setAddress(address);
  }
}
