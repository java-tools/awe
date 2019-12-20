package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.services.Service;
import com.almis.awe.model.type.LaunchPhaseType;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.almis.awe.model.constant.AweConstants.DOUBLE_LOG_LINE;
import static com.almis.awe.model.constant.AweConstants.LOG_LINE;

/**
 * Manage application initialization
 */
@Log4j2
public class InitService extends ServiceConfig implements DisposableBean {

  // Autowired services
  private LauncherService launcherService;

  /**
   * Autowired constructor
   *
   * @param launcherService Launcher service
   */
  @Autowired
  public InitService(LauncherService launcherService) {
    this.launcherService = launcherService;
  }

  /**
   * Launch services when application have been started (before spring context)
   *
   * @param event Context refresh event
   */
  @EventListener
  public void onApplicationStart(ContextRefreshedEvent event) {
    log.info(DOUBLE_LOG_LINE);
    log.info("=======  Launching application start services  =======");
    log.info(DOUBLE_LOG_LINE);
    launchPhaseServices(LaunchPhaseType.APPLICATION_START);
    log.info(LOG_LINE);
    log.info("-----------------------------  AWE STARTED  --------------------------------------");
    log.info(LOG_LINE);
  }

  /**
   * Launch services when application have been started (before spring context)
   */
  public void destroy() throws Exception {
    log.info(DOUBLE_LOG_LINE);
    log.info("=======  Launching application end services  =========");
    log.info(DOUBLE_LOG_LINE);
    launchPhaseServices(LaunchPhaseType.APPLICATION_END);
    log.info(LOG_LINE);
    log.info("-----------------------------  AWE STOPPING  -------------------------------------");
    log.info(LOG_LINE);
  }

  /**
   * Launch a client initialization
   */
  public void onClientStart() {
    // Launch client start services
    log.info(DOUBLE_LOG_LINE);
    log.info("=======  Initializing client start services  =========");
    log.info(DOUBLE_LOG_LINE);
    launchPhaseServices(LaunchPhaseType.CLIENT_START);
  }

  /**
   * Launch initial services
   *
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
