package com.almis.awe.autoconfigure;

import com.almis.awe.model.component.AweClientTracker;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.service.QueryService;
import com.almis.awe.service.SessionService;
import com.almis.awe.session.AweHttpSessionStrategy;
import com.almis.awe.session.AweSessionDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Map;
import java.util.Set;

/**
 * Created by dfuentes on 17/07/2017.
 */
@Configuration
@EnableSpringHttpSession
public class SessionConfig {

  @Value("${session.cookie.name:AWESESSIONID}")
  private String cookieName;

  @Value("${session.cookie.path:/}")
  private String cookiePath;

  @Value("${session.cookie.domain.name.pattern:^.+?\\.(\\w+\\.[a-z]+)$}")
  private String cookieDomainNamePattern;

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
   * Cookie serializer
   *
   * @return
   */
  @Bean
  @ConditionalOnMissingBean
  public CookieSerializer cookieSerializer() {
    DefaultCookieSerializer serializer = new DefaultCookieSerializer();
    serializer.setCookieName(cookieName);
    serializer.setCookiePath(cookiePath);
    serializer.setDomainNamePattern(cookieDomainNamePattern);
    return serializer;
  }

  /**
   * Session strategy
   *
   * @return Session strategy
   */
  @Bean
  @ConditionalOnMissingBean
  HttpSessionStrategy httpSessionStrategy(CookieSerializer cookieSerializer) {
    return new AweHttpSessionStrategy(cookieSerializer);
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
   * @param aweClientTracker Awe Client tracker
   * @param queryService     Query service
   * @param connectedUsers   Connected user list
   * @return Session details bean
   */
  @Bean
  @ConditionalOnMissingBean
  public AweSessionDetails aweSessionDetails(AweClientTracker aweClientTracker,
                                             QueryService queryService, Map<String, Set<String>> connectedUsers) {
    return new AweSessionDetails(aweClientTracker, queryService, connectedUsers);
  }
}
