package com.almis.awe.builder.client;

/**
 * Redirect action builder
 *
 * @author pgarcia
 */
public class RedirectScreenActionBuilder extends ClientActionBuilder<RedirectScreenActionBuilder> {

  private static final String TYPE = "redirect-screen";

  /**
   * Empty constructor
   */
  public RedirectScreenActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with an option
   *
   * @param screen Screen to redirect
   * @param url    Redirect URL
   */
  public RedirectScreenActionBuilder(String screen, String url) {
    setType(TYPE)
      .addParameter("screen", screen)
      .setTarget(url);
  }
}
