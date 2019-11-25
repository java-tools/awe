package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.services.Service;
import com.almis.awe.model.type.LaunchPhaseType;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manage application initialization
 */
public class InitService extends ServiceConfig {

  // Autowired services
  private LauncherService launcherService;
  private PropertyService propertyService;
  private QueryService queryService;

  /**
   * Autowired constructor
   * @param launcherService Launcher service
   * @param propertyService Property service
   * @param queryService Query service
   */
  @Autowired
  public InitService(LauncherService launcherService, PropertyService propertyService, QueryService queryService) {
    this.launcherService = launcherService;
    this.propertyService = propertyService;
    this.queryService = queryService;
  }

  /**
   * Initialize AWE Elements
   */
  public void initAweElements() {
    // Init awe elements
    getElements().init();

    // Init datasource map
    queryService.initDatabaseConnector();

    // Initialize properties
    propertyService.refreshDatabaseProperties();
  }

  /**
   * Launch initial services
   * @param phase Service phase
   */
  public void launchPhaseServices(LaunchPhaseType phase) {
    List<Service> serviceList;
    try {
      serviceList = getElements().getPhaseServices(phase);
    } catch (AWException exc) {
      getLogger().log(InitService.class, Level.ERROR, exc.getMessage(), exc);
      serviceList = new ArrayList<>();
    }

    // Launch service list
    launchServiceList(serviceList);
  }

  /**
   * Launch a service list
   */
  private void launchServiceList(List<Service> serviceList) {
    Map<String, Object> parameters = new HashMap<>();
    for (Service service : serviceList) {
      try {
        launcherService.callService(service.getId(), parameters);
      } catch (AWException exc) {
        getLogger().log(InitService.class, Level.ERROR, exc.getMessage(), exc);
      }
    }
  }
}
