/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.interfaces;

/**
 *
 * @author dfuentes
 */
public interface IBuilderInitializer<T>{

  /**
   * Initializes any needed elements
   */
  void initializeElements();

  /**
   * Sets current objects parent
   *
   * @return Parent
   */
  T setParent();
}
