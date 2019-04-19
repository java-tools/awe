/**
 *
 */
package com.almis.awe.tools.type;

/**
 * Align column types
 *
 * @author pvidal
 *
 */
public enum AlignType {

  CENTER("center"), RIGHT("right"), LEFT("left");

  // Parameter value
  private final String value;

  // Constructor
  private AlignType(String value) {
    this.value = value;
  }

  /**
   * Retrieve enumerated value
   *
   * @param value
   * @return
   */
  public static AlignType getEnum(String value) {
    for (AlignType enumerated : values()) {
      if (enumerated.value.equalsIgnoreCase(value)) {
        return enumerated;
      }
    }
    throw new IllegalArgumentException();
  }

  /**
   * Override toString method to return property value
   *
   * @return Property value (if defined)
   */
  @Override
  public String toString() {
    return value;
  }
}
