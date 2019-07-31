package com.almis.awe.rest.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * REST security configuration
 */
@Configuration
public class RestSecurityConfiguration {

  /**
   * Rest security configuration adapter
   */
  @Configuration
  @Order (1)
  public class RestSecurityConfigurationImpl extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable()
        .antMatcher("/api/**").authorizeRequests()
        .anyRequest().authenticated().and()
        // With an special header 'Authentication: Basic <Base64(user:password)>'
        .httpBasic();
    }
  }
}