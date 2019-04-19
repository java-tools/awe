package com.almis.awe.model.entities;

import com.almis.awe.exception.AWException;

public interface Copyable {
  /**
   * Get a copy of this element
   *
   * @return
   */
  <T> T copy() throws AWException;
}
