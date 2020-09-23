package com.almis.awe.annotation.processor.session;

import com.almis.awe.annotation.aspect.SessionAnnotation;
import com.almis.awe.annotation.entities.session.FromSession;
import com.almis.awe.annotation.entities.session.ToSession;
import com.almis.awe.model.component.AweSession;
import org.springframework.beans.factory.ObjectFactory;

/**
 * Session annotation processor
 *
 * @author dfuentes
 * Created by dfuentes on 20/04/2017.
 * @see FromSession
 * @see ToSession
 * @see SessionAnnotation
 */
public class SessionProcessor {

  // Autowired objects
  private final ObjectFactory<AweSession> aweSessionObjectFactory;

  /**
   * Autowired constructor
   *
   * @param aweSessionObjectFactory Session object factory
   */
  public SessionProcessor(ObjectFactory<AweSession> aweSessionObjectFactory) {
    this.aweSessionObjectFactory = aweSessionObjectFactory;
  }

  /**
   * Get parameter from session
   *
   * @param fromSession current annotation
   * @param clazz       the type to be returned as
   * @param <T>
   * @return
   */
  public <T> T getFromSession(FromSession fromSession, Class<T> clazz) {
    return aweSessionObjectFactory.getObject().getParameter(clazz, fromSession.name());
  }

  /**
   * Add parameter to session
   *
   * @param toSession
   * @param o
   */
  public void setToSession(ToSession toSession, Object o) {
    if (!aweSessionObjectFactory.getObject().hasParameter(toSession.name()) || toSession.overrideIfExist()) {
      aweSessionObjectFactory.getObject().setParameter(toSession.name(), o);
    }
  }
}
