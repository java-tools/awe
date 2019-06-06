package com.almis.awe.builder.client.css;

/**
 * Add classes action builder
 *
 * @author pgarcia
 */
public class AddCssClassActionBuilder extends CssClassActionBuilder<AddCssClassActionBuilder> {

  private static final String TYPE = "add-class";

  /**
   * Empty constructor
   */
  public AddCssClassActionBuilder() {
    super(TYPE);
  }

  /**
   * Constructor with an option
   *
   * @param cssSelector CSS selector
   * @param classes     Classes to add
   */
  public AddCssClassActionBuilder(String cssSelector, String... classes) {
    super(TYPE, cssSelector, classes);
  }
}
