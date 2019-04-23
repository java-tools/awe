package com.almis.awe.model.dto;

import java.util.Map;

/**
 * Request wrapper interface
 *
 * @author pvidal
 */
public interface RequestWrapper {
  /**
   * Set the parameter map to the wrapper
   *
   * @param parameterMap Parameter map to be set
   */
  void setParameters(Map<String, Object> parameterMap);
}
