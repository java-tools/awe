package com.almis.awe.annotation.processor.session;

import com.almis.awe.annotation.aspect.SessionAnnotation;
import com.almis.awe.annotation.entities.session.FromSession;
import com.almis.awe.annotation.entities.session.ToSession;
import com.almis.awe.config.ServiceConfig;

/**
 * Session annotation processor
 *
 * @author dfuentes
 * Created by dfuentes on 20/04/2017.
 * @see FromSession
 * @see ToSession
 * @see SessionAnnotation
 */
public class SessionProcessor extends ServiceConfig {

  /**
   * Get parameter from session
   *
   * @param fromSession current annotation
   * @param clazz       the type to be returned as
   * @param <T>
   * @return
   */
  public <T> T getFromSession(FromSession fromSession, Class<T> clazz) {
    return getSession().getParameter(clazz, fromSession.name());
  }

  /**
   * Add parameter to session
   *
   * @param toSession
   * @param o
   */
  public void setToSession(ToSession toSession, Object o) {
    if (getSession().hasParameter(toSession.name())) {
      if (toSession.overrideIfExist()) {
        getSession().setParameter(toSession.name(), o);
      }
    } else {
      getSession().setParameter(toSession.name(), o);
    }
  }
}
