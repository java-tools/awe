package com.almis.awe.builder.client;

import com.almis.awe.model.entities.actions.ComponentAddress;

import java.util.List;

/**
 * Select action builder
 *
 * @author pgarcia
 */
public class SelectActionBuilder extends ClientActionBuilder<SelectActionBuilder> {

  private static final String TYPE = "select";

  /**
   * Empty constructor
   */
  public SelectActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with target and value list
   *
   * @param target Target
   * @param values Value list
   */
  public SelectActionBuilder(String target, List values) {
    setType(TYPE)
      .setTarget(target)
      .addParameter("values", values);
  }

  /**
   * Constructor with address and value list
   *
   * @param address Target
   * @param values  Value list
   */
  public SelectActionBuilder(ComponentAddress address, List values) {
    setType(TYPE)
      .setAddress(address)
      .addParameter("values", values);
  }
}
