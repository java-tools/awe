package com.almis.awe.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * XMLNode Interface
 * Thread safe read for XML nodes
 *
 * @author Pablo GARCIA - 03//2010
 */
public interface XMLNode extends Serializable {
  /**
   * @return the elementKey
   */
  @JsonIgnore
  String getElementKey();
}
