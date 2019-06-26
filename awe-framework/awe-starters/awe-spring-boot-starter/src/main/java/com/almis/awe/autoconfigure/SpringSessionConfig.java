package com.almis.awe.autoconfigure;

import com.almis.awe.session.AweHttpSessionStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionStrategy;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dfuentes on 17/07/2017.
 */
@Configuration
@ConditionalOnClass(HttpSessionStrategy.class)
@EnableSpringHttpSession
public class SpringSessionConfig {

  @Value("${session.cookie.name:AWESESSIONID}")
  private String cookieName;

  @Value("${session.cookie.path:/}")
  private String cookiePath;

  @Value("${session.cookie.domain.name.pattern:^.+?\\.(\\w+\\.[a-z]+)$}")
  private String cookieDomainNamePattern;

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
   * Map session repository
   * @return
   */
  @Bean
  public SessionRepository<ExpiringSession> sessionRepository() {
    return new MapSessionRepository(new ConcurrentHashMap<>());
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
}
