package com.almis.awe.test.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestSessionListener implements HttpSessionListener {
  private static Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

  public TestSessionListener() {
    super();
  }

  public static Map<String, HttpSession> getAllSessions() {
    return sessionMap;
  }

  public void sessionCreated(final HttpSessionEvent event) {
    sessionMap.put(event.getSession().getId(), event.getSession());
  }

  public void sessionDestroyed(final HttpSessionEvent event) {
    sessionMap.remove(event.getSession().getId());
  }
}
