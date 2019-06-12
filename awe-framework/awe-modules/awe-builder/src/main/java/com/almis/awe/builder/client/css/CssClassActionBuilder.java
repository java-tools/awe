package com.almis.awe.builder.client.css;

import com.almis.awe.builder.client.ClientActionBuilder;

/**
 * Css classes action builder
 *
 * @author pgarcia
 */
public abstract class CssClassActionBuilder<T> extends ClientActionBuilder<T> {

  /**
   * Empty constructor
   *
   * @param type Action type
   */
  public CssClassActionBuilder(String type) {
    setType(type);
  }

  /**
   * Constructor with an option
   *
   * @param type        Action type
   * @param cssSelector CSS selector
   * @param classes     Classes to add
   */
  public CssClassActionBuilder(String type, String cssSelector, String... classes) {
    setType(type);
    setTarget(cssSelector);
    setAsync(true);
    setSilent(true);
    addParameter("targetAction", String.join(" ", classes));
  }
}
