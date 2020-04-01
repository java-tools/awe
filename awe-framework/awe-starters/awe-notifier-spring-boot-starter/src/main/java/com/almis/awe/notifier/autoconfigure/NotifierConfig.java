package com.almis.awe.notifier.autoconfigure;

import com.almis.awe.notifier.service.NotifierService;
import com.almis.awe.service.BroadcastService;
import com.almis.awe.service.MaintainService;
import com.almis.awe.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Notifier module configuration
 */
@Configuration
public class NotifierConfig {

  // Autowired services
  Environment environment;

  /**
   * Autowired constructor
   *
   * @param environment Environment
   */
  @Autowired
  public NotifierConfig(Environment environment) {
    this.environment = environment;
  }

  /**
   * Notifier service
   *
   * @param queryService     Query service
   * @param maintainService  Maintain service
   * @param broadcastService Broadcast service
   * @return Notifier service
   */
  @Bean
  public NotifierService notifierService(QueryService queryService, MaintainService maintainService, BroadcastService broadcastService) {
    return new NotifierService(queryService, maintainService, broadcastService);
  }
}
