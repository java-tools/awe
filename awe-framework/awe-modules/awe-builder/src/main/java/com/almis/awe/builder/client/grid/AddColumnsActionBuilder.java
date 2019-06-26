package com.almis.awe.builder.client.grid;

import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.screen.component.grid.Column;

import java.util.List;

/**
 * Replace columns action builder
 *
 * @author pgarcia
 */
public class AddColumnsActionBuilder extends ColumnsActionBuilder<AddColumnsActionBuilder> {

  private static final String TYPE = "add-columns";

  /**
   * Empty constructor
   */
  public AddColumnsActionBuilder() {
    super(TYPE);
  }

  /**
   * Constructor with target and column list
   *
   * @param target Target
   * @param columns Column list
   */
  public AddColumnsActionBuilder(String target, List<Column> columns) {
    super(TYPE, target, columns);
  }

  /**
   * Constructor with target and column list
   *
   * @param target Target
   * @param columns Column list
   */
  public AddColumnsActionBuilder(String target, Column... columns) {
    super(TYPE, target, columns);
  }


  /**
   * Constructor with address and column list
   *
   * @param address Target
   * @param columns Column list
   */
  public AddColumnsActionBuilder(ComponentAddress address, List<Column> columns) {
    super(TYPE, address, columns);
  }

  /**
   * Constructor with address and column list
   *
   * @param address Target
   * @param columns Column list
   */
  public AddColumnsActionBuilder(ComponentAddress address, Column... columns) {
    super(TYPE, address, columns);
  }
}
