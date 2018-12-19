package com.almis.awe.autoconfigure;

import com.almis.awe.model.component.AweClientTracker;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.service.QueryService;
import com.almis.awe.service.SessionService;
import com.almis.awe.session.AweHttpSessionStrategy;
import com.almis.awe.session.AweSessionDetails;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by dfuentes on 17/07/2017.
 */
@Configuration
@EnableSpringHttpSession
public class SessionConfig {

  /**
   * Session strategy
   *
   * @return Session strategy
   */
  @Bean
  @ConditionalOnMissingBean
  @SessionScope
  AweSession aweSession(HttpSession httpSession) {
    return new AweSession(httpSession);
  }

  /**
   * Session strategy
   *
   * @return Session strategy
   */
  @Bean
  @ConditionalOnMissingBean
  HttpSessionStrategy httpSessionStrategy() {
    return new AweHttpSessionStrategy();
  }

  /**
   * Session service
   * @return Session service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public SessionService sessionService() {
    return new SessionService();
  }

  /**
   * Session details
   * @param aweClientTracker Awe Client tracker
   * @param queryService Query service
   * @param connectedUsers Connected user list
   * @return Session details bean
   */
  @Bean
  @ConditionalOnMissingBean
  public AweSessionDetails aweSessionDetails(AweClientTracker aweClientTracker,
                                             QueryService queryService, Map<String, List<String>> connectedUsers) {
    return new AweSessionDetails(aweClientTracker, queryService, connectedUsers);
  }
}
