/*
 * Package definition
 */
package com.almis.awe.model.type;

/**
 * NumericType Enumerated
 *
 * List of allowed web numeric types
 *
 *
 * @author Pablo GARCIA - 16/APR/2012
 */
public enum NumericType {

  EUR("eur"), /* EUR formatting */
  AME("ame"), /* AME formatting */
  EUR_NO("eur_no"), /* EUR no thousands */
  AME_NO("ame_no"); /* AME no thousands */

  /* Parameter value */
  private String value;

  /* Constructor */
  NumericType(String value) {
    this.value = value;
  }

  /**
   * Retrieve enumerated value
   *
   * @param value numeric value
   * @return NumericType enum
   */
  public static NumericType getEnum(String value) {
    for (NumericType enumerated : values()) {
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
