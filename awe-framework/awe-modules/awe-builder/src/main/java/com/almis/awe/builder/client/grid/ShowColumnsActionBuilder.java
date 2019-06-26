package com.almis.awe.builder.client.grid;

import com.almis.awe.model.entities.actions.ComponentAddress;

import java.util.List;

/**
 * Replace columns action builder
 *
 * @author pgarcia
 */
public class ShowColumnsActionBuilder extends ColumnsActionBuilder<ShowColumnsActionBuilder> {

  private static final String TYPE = "show-columns";

  /**
   * Empty constructor
   */
  public ShowColumnsActionBuilder() {
    super(TYPE);
  }

  /**
   * Constructor with target and column list
   *
   * @param target Target
   * @param columnIds Column id list
   */
  public ShowColumnsActionBuilder(String target, List<String> columnIds) {
    super(TYPE, target, columnIds);
  }

  /**
   * Constructor with target and column list
   *
   * @param target Target
   * @param columnIds Column id list
   */
  public ShowColumnsActionBuilder(String target, String... columnIds) {
    super(TYPE, target, columnIds);
  }

  /**
   * Constructor with address and column list
   *
   * @param address Target
   * @param columnIds Column id list
   */
  public ShowColumnsActionBuilder(ComponentAddress address, List<String> columnIds) {
    super(TYPE, address, columnIds);
  }

  /**
   * Constructor with address and column list
   *
   * @param address Target
   * @param columnIds Column id list
   */
  public ShowColumnsActionBuilder(ComponentAddress address, String... columnIds) {
    super(TYPE, address, columnIds);
  }
}
