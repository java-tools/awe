package com.almis.awe.model.entities;

import java.io.Serializable;
import java.util.List;

/*
 * File Imports
 */

/**
 * XMLFile Interface
 * Thread safe read for XML files
 *
 * @author Pablo GARCIA - 03//2010
 */
public interface XMLFile extends Serializable {

  /**
   * Returns the element list iterator
   * @param <T> element type
   * @return element list iterator
   */
  <T extends XMLNode> List<T> getBaseElementList();
}
