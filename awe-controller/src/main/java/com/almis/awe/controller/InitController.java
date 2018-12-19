package com.almis.awe.controller;

import com.almis.awe.model.type.LaunchPhaseType;
import com.almis.awe.service.InitService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Manage all base screen requests
 */
@Controller
public class InitController {

  // Autowired services
  private InitService initService;

  /**
   * Autowired constructor
   * @param initService Init service
   */
  @Autowired
  public InitController(InitService initService) {
    this.initService = initService;
  }

  // Logger
  private static final Logger logger = LogManager.getLogger(InitController.class);

  private static final String LOG_LINE = "----------------------------------------------------------------------------------";

  /**
   * Handler for index page
   * @return Index page
   */
  @RequestMapping (value = {"/", "/screen/**"})
  public String index() {
    return "index";
  }

  /**
   * Launch startup services
   */
  @PostConstruct
  public void onStartup() {
    logger.info(LOG_LINE);
    logger.info("----------------------------- AWE starting ... -----------------------------------");
    logger.info(LOG_LINE);

    // Initialize Awe Elements (Read all XML sources)
    logger.info("=============================");
    logger.info("===== Reading XML Files =====");
    logger.info("=============================");
    initService.initAweElements();

    // Launch initial service
    logger.info("===== Launching initial services =====");
    initService.launchPhaseServices(LaunchPhaseType.APPLICATION_START);

    logger.info(LOG_LINE);
    logger.info("----------------------------- AWE started ----------------------------------------");
    logger.info(LOG_LINE);
  }

  /**
   * Launch end services
   */
  @PreDestroy
  public void onDestroy() {
    logger.info(LOG_LINE);
    logger.info("----------------------------- AWE stopping ... -----------------------------------");
    logger.info(LOG_LINE);

    // Launch final service
    logger.info("===== Launching final services =====");
    initService.launchPhaseServices(LaunchPhaseType.APPLICATION_END);

    logger.info(LOG_LINE);
    logger.info("------------------------------ AWE stopped ---------------------------------------");
    logger.info(LOG_LINE);
  }
}
