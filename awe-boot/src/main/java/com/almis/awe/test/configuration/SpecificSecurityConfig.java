package com.almis.awe.test.configuration;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.session.AweSessionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring security main configuration method
 * Created by dfuentes on 06/03/2017.
 */
@Configuration
@Profile({"dev", "gitlab-ci"})
public class SpecificSecurityConfig extends ServiceConfig {

  // Autowired services
  private AweSessionDetails aweSessionDetails;

  /**
   * Autowired constructor
   * @param sessionDetails Session details
   * @param logger Logger
   */
  @Autowired
  public SpecificSecurityConfig(AweSessionDetails sessionDetails, LogUtil logger) {
    this.aweSessionDetails = sessionDetails;
  }

  @Autowired
  private UsernamePasswordAuthenticationFilter authenticationFilter;

  @Value("${screen.parameter.username:cod_usr}")
  private String usernameParameter;

  @Value("${screen.parameter.password:pwd_usr}")
  private String passwordParameter;

  @Value ("${language.default}:en")
  private String defaultLocale;

  @Value ("${security.auth.mode:bbdd}")
  private String authenticationProviderSource;

  @Value ("${security.role.prefix:ROLE_}")
  private String rolePrefix;

  // Custom authentication
  @Value ("${security.auth.custom.providers:}")
  private String[] authenticationProviders;

  // BBDD authentication
  @Value ("${security.auth.jdbc.param.userPasswordQuery:}")
  private String userDetailsQuery;

  @Value ("${security.auth.jdbc.param.rolesQuery:}")
  private String userRolesQuery;

  // LDAP authentication
  @Value ("${security.auth.ldap.url:}")
  private String[] ldapUrl;

  @Value ("${security.auth.ldap.user:}")
  private String ldapUserFilter;

  @Value ("${security.auth.ldap.password.bind:}")
  private String ldapPassword;

  @Value ("${security.auth.ldap.user.bind:}")
  private String ldapUserDN;

  @Value ("${security.auth.ldap.basedn:}")
  private String ldapBaseDN;

  @Value ("${security.headers.frameOptions.sameOrigin:true}")
  private boolean sameOrigin;

  @Value ("${security.master.key:fdvsd4@sdsa08}")
  private String masterKey;

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
        .addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .formLogin().permitAll()
        .and()
        .logout().logoutUrl("/action/logout").clearAuthentication(true)
        .deleteCookies("SESSION").invalidateHttpSession(true)
        .addLogoutHandler((request, response, authentication) ->  {
          getRequest().init(request);
          aweSessionDetails.onBeforeLogout();
        })
        .logoutSuccessHandler((request, response, authentication) -> {
          getRequest().init(request);
          aweSessionDetails.onLogoutSuccess();
          request.getRequestDispatcher("/action/logoutRedirect").forward(request, response);
        })
        .and()
        .sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());

      if (sameOrigin) {
        http.headers().frameOptions().sameOrigin();
      }
    }
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
}