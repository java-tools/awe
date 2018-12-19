/*
 * Package definition
 */
package com.almis.awe.model.type;

/**
 * EmailMessageType Enumerated
 *
 * List of allowed email message types
 *
 *
 * @author Pablo GARCIA - 28/JUL/2011
 */
public enum EmailMessageType {

  HTML("html"), /* HTML message */
  TEXT("text"); /* TEXT plain message */

  /* Parameter value */
  private String value;

  /* Constructor */
  private EmailMessageType(String value) {
    this.value = value;
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
