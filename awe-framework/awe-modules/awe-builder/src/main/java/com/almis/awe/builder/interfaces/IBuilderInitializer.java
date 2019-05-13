package com.almis.awe.builder.interfaces;

import com.almis.awe.model.entities.Element;

/**
 *
 * @author dfuentes
 */
public interface IBuilderInitializer<T, I extends Element> {

  /**
   * Sets current objects parent
   *
   * @return Parent
   */
  T setParent();

  /**
   * Build instance without parameters
   * @return New instance of object
   */
  Element build();


  /**
   * Build instance already initialized
   * @param instance Instance to update
   * @return Instance of object updated
   */
  I build(I instance);
}
