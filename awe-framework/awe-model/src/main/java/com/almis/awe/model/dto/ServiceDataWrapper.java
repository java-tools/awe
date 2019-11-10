package com.almis.awe.model.dto;

import com.almis.awe.exception.AWException;

/**
 * Generic class that implements ResponseWrapper and is used to parse the response from a microservice/rest service when no specific class is provided
 *
 * @author jbellon
 */
public class ServiceDataWrapper extends ServiceData implements ResponseWrapper {
  @Override
  public ServiceData toServiceData() throws AWException {
    return this;
  }
}
