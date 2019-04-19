/*
 * Package definition
 */
package com.almis.awe.model.type;

import java.math.RoundingMode;

/**
 * RoundingType Enumerated
 *
 * List of allowed web numeric types
 *
 * @author Pablo GARCIA - 16/APR/2012
 */
public enum RoundingType {

  // Round-Half-Up Symmetric
  HALF_UP_SYMMETRIC(RoundingMode.HALF_UP, "S"),
  // Round-Half-Up Asymmetric
  HALF_UP_ASYMMETRIC(RoundingMode.HALF_UP, "A"),
  // Round-Half-Down Symmetric
  HALF_DOWN_SYMMETRIC(RoundingMode.HALF_DOWN, "s"),
  // Round-Half-Down Asymmetric
  HALF_DOWN_ASYMMETRIC(RoundingMode.HALF_DOWN, "a"),
  // Round-Half-Even
  HALF_EVEN(RoundingMode.HALF_EVEN, "B"),
  // Round Up
  UP(RoundingMode.UP, "U"),
  // Round Down
  DOWN(RoundingMode.DOWN, "D"),
  // Round to Ceiling
  CEILING(RoundingMode.CEILING, "C"),
  // Round to Floor
  FLOOR(RoundingMode.FLOOR, "F");

  private final RoundingMode value;
  private final String code;

  /* Constructor */
  RoundingType(RoundingMode value, String code) {
    this.value = value;
    this.code = code;
  }

  /**
   * Get rounding type from code
   * @param code Code
   * @return Rounding type
   */
  public static RoundingType fromCode(String code) {
    RoundingType found = null;
    for (RoundingType type : RoundingType.values()) {
      if (code.equals(type.getCode())) {
        found = type;
      }
    }
    return found;
  }

  /**
   * Override toString method to return property value
   *
   * @return Property value (if defined)
   */
  public RoundingMode getRoundingMode() {
    return value;
  }

  /**
   * Get rounding type code
   * @return code
   */
  public String getCode() {
    return code;
  }
}
