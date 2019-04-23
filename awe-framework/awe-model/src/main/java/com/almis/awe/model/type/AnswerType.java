/*
 * Package definition
 */
package com.almis.awe.model.type;

/**
 * AnswerType Enumerated
 *
 * List of allowed web service types
 *
 *
 * @author Pablo GARCIA - 23/NOV/2010
 */
public enum AnswerType {

  ERROR("error"), /* Error output */
  OK("ok"), /* Valid output */
  WARNING("warning"), /* Valid output with WARNING */
  INFO("info"); /* Valid output with INFO */

  /* Parameter value */
  private String value;

  /* Constructor */
  private AnswerType(String value) {
    this.value = value;
  }

  /**
   * Retrieve enumerated value
   *
   * @param value
   * @return
   */
  public static AnswerType getEnum(String value) {
    for (AnswerType enumerated : values()) {
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
