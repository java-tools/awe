package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.component.AweSession;

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
    AweSession session = getSession();
    if (isSessionValid(session)) {
      return session.getParameter(name);
    } else {
      return null;
    }
  }

  /**
   * Store session parameter (as string)
   * @param name Parameter name
   * @param value Parameter value
   */
  public void setSessionParameter(String name, String value) {
    AweSession session = getSession();
    if (isSessionValid(session)) {
      session.setParameter(name, value);
    }
  }

  private boolean isSessionValid(AweSession session) {
    return session != null;
  }
}
