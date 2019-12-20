package com.almis.awe.autoconfigure;

import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.tracker.AweClientTracker;
import com.almis.awe.model.tracker.AweConnectionTracker;
import com.almis.awe.service.BroadcastService;
import com.almis.awe.service.QueryService;
import com.almis.awe.service.SessionService;
import com.almis.awe.session.AweSessionDetails;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Created by dfuentes on 17/07/2017.
 */
@Configuration
public class SessionConfig {

  /**
   * Session strategy
   *
   * @return Session strategy
   */
  @Bean
  @ConditionalOnMissingBean
  @SessionScope
  AweSession aweSession() {
    return new AweSession();
  }

  /**
   * Session service
   *
   * @return Session service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public SessionService sessionService() {
    return new SessionService();
  }

  /**
   * Session details
   *
   * @param aweClientTracker  Awe Client tracker
   * @param queryService      Query service
   * @param connectionTracker connection tracker
   * @return Session details bean
   */
  @Bean
  @ConditionalOnMissingBean
  public AweSessionDetails aweSessionDetails(AweClientTracker aweClientTracker, QueryService queryService,
                                             AweConnectionTracker connectionTracker, BroadcastService broadcastService) {
    return new AweSessionDetails(aweClientTracker, queryService, connectionTracker, broadcastService);
  }
}
