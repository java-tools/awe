package com.almis.awe.model.dto;

import java.util.Comparator;

/**
 * CellData comparator
 * Implements <code>{@link Comparator}</code>
 *
 * @author Pablo VIDAL - 21/Nov/2019
 */
public class CompareCellData implements Comparator<CellData> {

  private boolean nullsFirst;
  private String direction;

  /**
   * CompareCellData Constructor
   * Set nulls at last <code>nullsFirst = false</code>
   *
   * @param direction sort direction
   */
  public CompareCellData(String direction) {
    this.direction = direction;
    this.nullsFirst = false;
  }

  /**
   * Constructor with nullsFirst flag
   * @param direction sort direction
   * @param nullsFirst first the null values
   */
  public CompareCellData(String direction, boolean nullsFirst) {
    this.direction = direction;
    this.nullsFirst = nullsFirst;
  }

  @Override
  public int compare(CellData o1, CellData o2) {

    if (o1.getObjectValue() == null) {
      if (o2.getObjectValue() == null) return 0;
      else return getIndexNullCellData(1, -1);
    } else if (o2.getObjectValue() == null) {
      return getIndexNullCellData(-1, 1);
    } else {
      return compareCellData(o1, o2);
    }
  }

  /**
   * Get index position order when cellData value is null
   * Takes into account <code>nullsFirst</code> flag and <code>order</code>
   *
   * @param i null index position
   * @param j null index position
   * @return index position
   */
  private int getIndexNullCellData(int i, int j) {
    if ("desc".equalsIgnoreCase(direction)) {
      return nullsFirst ? i : j;
    } else {
      return nullsFirst ? j : i;
    }
  }

  /**
   * Compare CellData instances
   *
   * @param o1 CellData 1
   * @param o2 CellData 2
   * @return compare cellData
   */
  private int compareCellData(CellData o1, CellData o2) {
    switch (o1.getType()) {
      case DATE:
        // Compare as date value
        return o1.getDateValue().compareTo(o2.getDateValue());
      case DOUBLE:
      case FLOAT:
      case DECIMAL:
      case LONG:
        // Compare as Double value
        return o1.getDoubleValue().compareTo(o2.getDoubleValue());
      case INTEGER:
        return o1.getIntegerValue().compareTo(o2.getIntegerValue());
      case STRING:
      default:
        return o1.getStringValue().compareTo(o2.getStringValue());
    }
  }
}
