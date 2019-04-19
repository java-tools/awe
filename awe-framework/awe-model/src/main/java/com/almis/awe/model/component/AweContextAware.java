package com.almis.awe.model.component;


/**
 * AweContextAware
 * For new objects which need AweElements
 */
public interface AweContextAware {
  /**
   * Set the awe elements
   * @param elements Elements
   * @return this
   */
  AweContextAware setElements(AweElements elements);
}
