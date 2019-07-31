package com.almis.awe.builder.client.grid;

import com.almis.awe.builder.enumerates.RowPosition;

/**
 * Copy rows action builder
 *
 * @author pgarcia
 */
public class CopyRowActionBuilder extends RowsActionBuilder<CopyRowActionBuilder> {

  private static final String TYPE = "copy-row";

  /**
   * Empty constructor
   */
  public CopyRowActionBuilder() {
    super(TYPE, RowPosition.BOTTOM);
  }


  /**
   * Add row with position builder
   */
  public CopyRowActionBuilder(RowPosition position) {
    super(TYPE, position);
  }

  /**
   * Constructor with target
   *
   * @param position Row position
   * @param target Target
   */
  public CopyRowActionBuilder(RowPosition position, String target) {
    super(TYPE, position, target);
  }

  /**
   * Constructor with target and selected row
   *
   * @param position Row position
   * @param target Target
   * @param rowId Selected row to copy
   */
  public CopyRowActionBuilder(RowPosition position, String target, String rowId) {
    super(TYPE, position, target);
    addParameter("selectedRow", rowId);
  }
}
