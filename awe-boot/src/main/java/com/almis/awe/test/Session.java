package com.almis.awe.test;

import com.almis.awe.model.dto.ServiceData;
import org.springframework.stereotype.Service;

/**
 * File test class
 *
 * @author pgarcia
 */
@Service
public class Session {

  /**
   * Fill a modal dialog
   *
   * @return Service data
   */
  public ServiceData checkCurrentSession() {
    ServiceData serviceData = new ServiceData();
    return serviceData;
  }
}
