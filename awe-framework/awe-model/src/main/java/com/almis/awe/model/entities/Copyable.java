package com.almis.awe.model.entities;

import com.almis.awe.exception.AWException;

import java.io.Serializable;

public interface Copyable extends Serializable {

  /**
   *  Get a copy of this element
   * @param <T> element type
   * @return element copy
   * @throws AWException AWE exception
   */
  <T> T copy() throws AWException;
}
