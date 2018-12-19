package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;

/**
 * Session service
 * @author pgarcia
 */
public class SessionService extends ServiceConfig {

  /**
   * Retrieve session parameter
   * @param name Parameter name
   * @return Session parameter
   */
  public Object getSessionParameter(String name) {
    return getSession().getParameter(name);
  }

  /**
   * Store session parameter
   * @param name Parameter name
   * @param value Parameter value
   */
  public void setSessionParameter(String name, Object value) {
    getSession().setParameter(name, value);
  }

  /**
   * Store session parameter (as string)
   * @param name Parameter name
   * @param value Parameter value
   */
  public void setSessionParameter(String name, String value) {
    getSession().setParameter(name, value);
  }
}
