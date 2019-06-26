package com.almis.awe.test.configuration;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.security.authentication.filter.JsonAuthenticationFilter;
import com.almis.awe.security.handler.AweLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring security main configuration method
 * Created by dfuentes on 06/03/2017.
 */
@Configuration
@Profile({"dev", "gitlab-ci"})
public class SpecificSecurityConfig extends ServiceConfig {

  // Autowired services
  private SessionRegistry sessionRegistry;

  @Value("${security.headers.frameOptions.sameOrigin:true}")
  private boolean sameOrigin;

  @Value("${session.cookie.name:AWESESSIONID}")
  private String cookieName;

  /**
   * Autowired constructor
   * @param sessionRegistry Session registry
   */
  @Autowired
  public SpecificSecurityConfig(SessionRegistry sessionRegistry) {
    this.sessionRegistry = sessionRegistry;
  }

  /**
   * Second configuration class for spring security
   */
  @Configuration
  @Order(99)
  public class AWEScreenSecurityAdapter extends WebSecurityConfigurerAdapter {

    /**
     * Spring security configuration
     *
     * @param http Http security object
     * @throws Exception Configure error
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
        .csrf().disable()
        .authorizeRequests()
        .and()
        // Add a filter to parse login parameters
        .addFilterAt(getBean(JsonAuthenticationFilter.class), UsernamePasswordAuthenticationFilter.class)
        .formLogin().permitAll()
        .and()
        .logout().logoutUrl("/action/logout")
        .deleteCookies(cookieName)
        .addLogoutHandler(getBean(AweLogoutHandler.class))
        .and()
        .sessionManagement()
        .maximumSessions(1).sessionRegistry(sessionRegistry);

      if (sameOrigin) {
        http.headers().frameOptions().sameOrigin();
      }
    }
  }
}