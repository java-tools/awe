package com.almis.awe.builder.client.css;

/**
 * Remove classes action builder
 *
 * @author pgarcia
 */
public class RemoveCssClassActionBuilder extends CssClassActionBuilder<RemoveCssClassActionBuilder> {

  private static final String TYPE = "remove-class";

  /**
   * Empty constructor
   */
  public RemoveCssClassActionBuilder() {
    super(TYPE);
  }

  /**
   * Constructor with an option
   *
   * @param cssSelector CSS Selector
   * @param classes     Classes to remove
   */
  public RemoveCssClassActionBuilder(String cssSelector, String... classes) {
    super(TYPE, cssSelector, classes);
  }
}
