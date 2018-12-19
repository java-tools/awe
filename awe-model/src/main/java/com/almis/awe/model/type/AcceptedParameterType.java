/*
 * Package definition
 */
package com.almis.awe.model.type;

/**
 * AcceptedParameterType Enumerated
 *
 * List of allowed input parameters
 *
 *
 * @author Pablo GARCIA - 13/JUL/2010
 */
public enum AcceptedParameterType {

  // Accepted parameters
  // TODO: Move Log name and log level to a rest controller
  SCREEN("application.parameter.screen"),
  FRAME("application.parameter.frame"),
  TOKEN("application.parameter.token"),
  COMET_UID("application.parameter.comet.id"),
  PARAMETERS("security.json.parameter"),
  DOWNLOAD_IDENTIFIER("file.download.identifier");

  // Parameter key
  private String key;

  // Constructor
  AcceptedParameterType(String property) {
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
