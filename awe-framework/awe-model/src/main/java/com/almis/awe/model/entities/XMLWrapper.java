package com.almis.awe.model.entities;

import com.almis.awe.model.constant.AweConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/*
 * File Imports
 */

/**
 * XMLWrapper Class
 * Thread safe read for XML files
 *
 * @author Pablo GARCIA - 03//2010
 */
public abstract class XMLWrapper implements Serializable {

  private static final long serialVersionUID = -7801980809414641021L;

  // Element list
  @XStreamOmitField
  private XMLWrapper parent = null;

  /**
   * Default constructor
   */
  public XMLWrapper() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public XMLWrapper(XMLWrapper other) {
    this.parent = other.parent;
  }

  /**
   * Returns the element list iterator
   *
   * @return element list iterator
   */
  @JsonIgnore
  public <T extends XMLWrapper> List<T> getBaseElementList() {
    return Collections.emptyList();
  }

  /**
   * Returns if identifier belongs to the element
   *
   * @param ide Element identifier
   * @return Identifier is the current element
   */
  @JsonIgnore
  public boolean isElement(String ide) {
    return true;
  }

  /**
   * Stores element parent
   *
   * @param parent Parent element
   */
  public void setParent(XMLWrapper parent) {
    this.parent = parent;
  }

  /**
   * Returns element parent
   *
   * @return element parent
   */
  @JsonIgnore
  public XMLWrapper getParent() {
    return this.parent;
  }

  /**
   * @return the elementKey
   */
  @JsonIgnore
  public String getElementKey() {
    return AweConstants.NO_KEY;
  }
}
