package com.almis.awe.model.dto;

import com.almis.awe.exception.AWException;

/**
 * Response Wrapper interface
 *
 * @author pvidal
 */
public interface ResponseWrapper {

  /**
   * Retrieve wrapper information as service data
   *
   * @return ServiceData
   * @throws AWException Error retrieving service data
   */
  ServiceData toServiceData() throws AWException;
}
