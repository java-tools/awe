package com.almis.awe.builder.client.grid;

import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.screen.component.grid.Column;

import java.util.List;

/**
 * Replace columns action builder
 *
 * @author pgarcia
 */
public class ReplaceColumnsActionBuilder extends ColumnsActionBuilder<ReplaceColumnsActionBuilder> {

  private static final String TYPE = "replace-columns";

  /**
   * Empty constructor
   */
  public ReplaceColumnsActionBuilder() {
    super(TYPE);
  }

  /**
   * Constructor with target and column list
   *
   * @param target Target
   * @param columns Column list
   */
  public ReplaceColumnsActionBuilder(String target, List<Column> columns) {
    super(TYPE, target, columns);
  }

  /**
   * Constructor with address and column list
   *
   * @param address Target
   * @param columns Column list
   */
  public ReplaceColumnsActionBuilder(ComponentAddress address, List<Column> columns) {
    super(TYPE, address, columns);
  }
}
