package com.almis.awe.builder.client.grid;

import com.almis.awe.builder.client.ClientActionBuilder;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.screen.component.grid.Column;

import java.util.List;

/**
 * Replace columns action builder
 *
 * @author pgarcia
 */
public abstract class ColumnsActionBuilder<T> extends ClientActionBuilder<T> {

  /**
   * Empty constructor
   * @param type Type
   */
  public ColumnsActionBuilder(String type) {
    setType(type);
  }

  /**
   * Constructor with target and column list
   *
   * @param type Type
   * @param target Target
   * @param columns Column list
   */
  public ColumnsActionBuilder(String type, String target, List<Column> columns) {
    setType(type);
    setTarget(target);
    addParameter("columns", columns);
  }

  /**
   * Constructor with address and column list
   *
   * @param type Type
   * @param address Target
   * @param columns Column list
   */
  public ColumnsActionBuilder(String type, ComponentAddress address, List<Column> columns) {
    setType(type);
    setAddress(address);
    addParameter("columns", columns);
  }
}
