package com.almis.awe.builder.screen.base;

/**
 * @author dfuentes
 */
public abstract class AbstractAttributes<B extends AweBuilder> {

  protected B parent;

  /**
   * Constructor
   */
  public AbstractAttributes(B builder) {
    this.parent = builder;
  }

  /**
   * Retrieve builder
   * @return Builder
   */
  public B builder() {
    return parent;
  }
}
