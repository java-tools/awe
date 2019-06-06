package com.almis.awe.builder.client;

/**
 * Confirm action builder
 *
 * @author pgarcia
 */
public class ConfirmActionBuilder extends ClientActionBuilder<ConfirmActionBuilder> {

  private static final String TYPE = "confirm";

  /**
   * Empty constructor
   */
  public ConfirmActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with a confirm target
   *
   * @param confirm confirm to open
   */
  public ConfirmActionBuilder(String confirm) {
    setType(TYPE)
      .setTarget(confirm);
  }

  /**
   * Constructor with a title and description
   *
   * @param title Confirm title
   * @param description Confirm description
   */
  public ConfirmActionBuilder(String title, String description) {
    setType(TYPE)
      .addParameter("title", title)
      .addParameter("message", description);
  }
}
