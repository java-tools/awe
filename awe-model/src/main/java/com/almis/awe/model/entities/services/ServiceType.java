package com.almis.awe.model.entities.services;

import com.almis.awe.model.entities.Copyable;

import java.io.Serializable;
import java.util.List;

/**
 * Manages distinct service launchers
 *
 * @author Jorge BELLON
 */
public interface ServiceType extends Copyable, Serializable {

  /**
   * Returns the service parameter list
   *
   * @return Service parameter list
   */
  List<ServiceInputParameter> getParameterList();

  /**
   * Stores the service parameter list
   *
   * @param parameters Service parameter list
   */
  void setParameterList(List<ServiceInputParameter> parameters);

  /**
   * Return the service launcher class
   *
   * @return Launcher service launcher class
   */
  String getLauncherClass();
}
