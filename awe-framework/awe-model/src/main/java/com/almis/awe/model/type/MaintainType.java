package com.almis.awe.model.type;

/**
 * MaintainType Enumerated
 *
 * List of allowed maintain operations
 *
 *
 * @author jbellon
 */

public enum MaintainType {
  AUDIT("A"),
  COMMIT("C"),
  DELETE("D"),
  EMAIL("E"),
  INSERT("I"),
  MULTIPLE("M"),
  NONE("N"),
  SERVE("S"),
  QUEUE("Q"),
  UPDATE("U"),
  INCLUDE("T"),
  RETRIEVE_DATA("G");

  private String key;

  // Constructor
  private MaintainType(String property) {
    this.key = property;
  }

  /**
   * Override toString method to return property value
   *
   * @return Property value (if defined)
   */
  @Override
  public String toString() {
    return this.key;
  }

}