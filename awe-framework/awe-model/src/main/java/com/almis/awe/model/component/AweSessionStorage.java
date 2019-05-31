package com.almis.awe.model.component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by pgarcia
 */
public class AweSessionStorage {
  private Map<String, Object> sessionStorage;

  /**
   * Constructor
   */
  public AweSessionStorage() {
    sessionStorage = Collections.synchronizedMap(new HashMap<>());
  }

  /**
   * Set new parameter
   *
   * @param name  Parameter name
   * @param value Parameter value
   */
  public void store(String name, Object value) {
    sessionStorage.put(name, value);
  }

  /**
   * Get parameter
   *
   * @param name Parameter name
   * @return Parameter value
   */
  public Object retrieve(String name) {
    return sessionStorage.get(name);
  }

  /**
   * Returns parameter value casted to the given class
   *
   * @param <T>
   * @param clazz Parameter class
   * @param name  Parameter name
   * @return Parameter value
   */
  public <T> T retrieve(Class<T> clazz, String name) {
    Object o = retrieve(name);
    if (o != null) {
      return clazz.cast(o);
    }
    return null;
  }

  /**
   * Remove parameter from session
   *
   * @param name Parameter name
   */
  public void remove(String name) {
    sessionStorage.remove(name);
  }

  /**
   * Check if there is a parameter in the session
   *
   * @param name Parameter name
   * @return Session has parameter
   */
  public boolean has(String name) {
    return sessionStorage.containsKey(name);
  }

  /**
   * Get parameter names from current session
   *
   * @return Parameter names
   */
  public Set<String> sessionKeys() {
    return sessionStorage.keySet();
  }
}