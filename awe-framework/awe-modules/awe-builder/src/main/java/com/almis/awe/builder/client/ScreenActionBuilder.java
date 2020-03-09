package com.almis.awe.builder.client;

/**
 * Screen action builder
 *
 * @author pgarcia
 */
public class ScreenActionBuilder extends ClientActionBuilder<ScreenActionBuilder> {

  private static final String TYPE = "screen";

  /**
   * Empty constructor
   */
  public ScreenActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with an option
   *
   * @param option Screen option
   */
  public ScreenActionBuilder(String option) {
    setType(TYPE)
      .setTarget(option);
  }

  /**
   * Constructor with an option
   *
   * @param option Screen option
   */
  public ScreenActionBuilder(String option, Boolean reload) {
    setType(TYPE)
      .setTarget(option)
      .addParameter("reload", reload);
  }
}
