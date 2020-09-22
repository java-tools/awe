package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.services.ServiceType;
import com.almis.awe.service.connector.ServiceConnector;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * LauncherService Class
 * AWE Launcher service
 * Provides functions to call service
 *
 * @author Jorge BELLON - 30/ENE/2017
 */
public class LauncherService extends ServiceConfig {

  // Application context
  private final ApplicationContext context;

  /**
   * Initialize service with aweElements and application context
   *
   * @param context Application context
   */
  public LauncherService(ApplicationContext context) {
    this.context = context;
  }

  /**
   * Launches a service (must be defined in APP or AWE Services.xml file) and returns the service data
   *
   * @param serviceName Service name
   * @param serviceArgs Service parameters
   * @return Service Data
   * @throws AWException Error calling service
   */
  public ServiceData callService(String serviceName, Map<String, Object> serviceArgs) throws AWException {
    // Variable definition
    ServiceType service = getElements().getService(serviceName).getType();
    ServiceData serviceData;

    // Check if service exists
    if (service == null) {
      throw new AWException(getLocale("ERROR_TITLE_LAUNCHING_SERVICE"), getLocale("ERROR_MESSAGE_SERVICE_NOT_FOUND") + " (" + serviceName + ")");
    }

    // Get connector
    ServiceConnector connector = (ServiceConnector) context.getBean(service.getLauncherClass());

    // Launch service
    serviceData = connector.launch(service, serviceArgs);

    // Return service output
    return serviceData;
  }

}