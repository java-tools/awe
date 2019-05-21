package com.almis.awe.test.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Dummy Service class to test call services from <code>qualifier</code> attribute
 *
 * @author pvidal
 *
 */
@Service
@Qualifier("dummyQualifierService")
public class DummyQualifierService extends ServiceConfig {

  /**
   * Constructor
   */
  public DummyQualifierService() {
    // Constructor
  }

  /**
   * Check if service has been instantiated
   * @return check flag
   */
  public ServiceData getInstance() throws AWException {
    ServiceData serviceData = new ServiceData();
    serviceData.setValid(true);
    return serviceData;
  }
}
