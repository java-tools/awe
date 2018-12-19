package com.almis.awe.autoconfigure;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.security.accessbean.LoginAccessControl;
import com.almis.awe.security.authentication.encoder.Ripemd160PasswordEncoder;
import com.almis.awe.security.authentication.filter.JsonAuthenticationFilter;
import com.almis.awe.service.AccessService;
import com.almis.awe.service.MenuService;
import com.almis.awe.session.AweSessionDetails;
import org.apache.logging.log4j.Level;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;
import javax.sql.DataSource;

/**
 * Spring security main configuration method
 * Created by dfuentes on 06/03/2017.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends ServiceConfig {

  // Autowired services
  private AweSessionDetails aweSessionDetails;
  private LogUtil logger;

  /**
   * Autowired constructor
   * @param sessionDetails Session details
   * @param logger Logger
   */
  @Autowired
  public SecurityConfig(AweSessionDetails sessionDetails, LogUtil logger) {
    this.aweSessionDetails = sessionDetails;
    this.logger = logger;
  }

  private enum AUTHENTICATION_MODE {
    LDAP("ldap"),
    BBDD("bbdd"),
    IN_MEMORY("in_memory"),
    CUSTOM("custom");

    private String mode;

    AUTHENTICATION_MODE(String mode) {
      this.mode = mode;
    }

    /**
     * Get security value
     *
     * @return Mode
     */
    public String getValue() {
      return mode;
    }

    /**
     * Get authentication from value
     *
     * @param value Value
     *
     * @return Authentication mode
     */
    public static AUTHENTICATION_MODE fromValue(String value) {
      if (value.equalsIgnoreCase(LDAP.getValue())) {
        return LDAP;
      } else if (value.equalsIgnoreCase(BBDD.getValue())) {
        return BBDD;
      } else if (value.equalsIgnoreCase(IN_MEMORY.getValue())) {
        return IN_MEMORY;
      } else if (value.equalsIgnoreCase(CUSTOM.getValue())) {
        return CUSTOM;
      }
      return null;
    }
  }

  @Value("${screen.parameter.username:cod_usr}")
  private String usernameParameter;

  @Value("${screen.parameter.password:pwd_usr}")
  private String passwordParameter;

  @Value("${security.json.parameter:p}")
  private String securityParameter;

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
  public class AWEScreenSecurityAdapter extends WebSecurityConfigurerAdapter {

    /**
     * Spring security configuration
     *
     * @param http Http security object
     *
     * @throws Exception Configure error
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      Filter jsonAuthenticationFilter = new JsonAuthenticationFilter(usernameParameter, passwordParameter, securityParameter);
      http.authorizeRequests()
        //.anyRequest().access("@loginAccessControl.checkAccess(authentication, request)")
        //.anyRequest().authenticated().accessDecisionManager(accessDecisionManager())
//              .antMatchers("/beans").hasRole("ACTUATOR")
//              .anyRequest().permitAll()
        .and()
        .addFilterAt(jsonAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) //Add a filter to parse login parameters
        .formLogin()
        .usernameParameter(usernameParameter)
        .passwordParameter(passwordParameter)
        //.loginPage("/")
        .loginProcessingUrl("/action/login")
        .permitAll()
        .successHandler((request, response, authentication) -> {
          // Initialize parameters
          getRequest().init(request);
          aweSessionDetails.onLoginSuccess(authentication);
          request.getRequestDispatcher("/action/loginRedirect").forward(request, response);
        })
        .failureHandler((request, response, authenticationException) -> {
          // Initialize parameters
          getRequest().init(request);
          aweSessionDetails.onLoginFailure(authenticationException);
          request.getRequestDispatcher("/action/loginRedirect").forward(request, response);
        })
        .and()
        .logout().logoutUrl("/action/logout").clearAuthentication(true)
        .addLogoutHandler((request, response, authentication) -> {
          getRequest().init(request);
          aweSessionDetails.onBeforeLogout();
        })
        .logoutSuccessHandler((request, response, authentication) -> {
          aweSessionDetails.onLogoutSuccess();
          request.getRequestDispatcher("/action/logoutRedirect").forward(request, response);
        })
        .deleteCookies("SESSION").invalidateHttpSession(true)
        .and()
        .csrf().disable();

      if (sameOrigin) {
        http.headers().frameOptions().sameOrigin();
      }

    }

    /**
     * Configure current users datasource
     *
     * @param auth Authentication manager
     *
     * @throws Exception Global configuration error
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

      AUTHENTICATION_MODE mode = AUTHENTICATION_MODE.fromValue(authenticationProviderSource);
      mode = mode == null ? AUTHENTICATION_MODE.BBDD : mode;

      switch (mode) {
        case LDAP:
          auth.authenticationProvider(ldapAuthenticationProvider());
          break;

        case CUSTOM:
          // Custom authentication bean
          for (String provider : authenticationProviders) {
            try {
              Object beanObj = getBean(provider);
              if (beanObj instanceof AuthenticationProvider) {
                auth.authenticationProvider((AuthenticationProvider) beanObj);
              }
            } catch (Exception exc) {
              logger.log(this.getClass(), Level.ERROR, "Couldn't load authentication provider bean with name [{0}]", exc, provider);
            }
          }
          break;

        case BBDD:
        default:
          auth.jdbcAuthentication()
            .dataSource(getBean(DataSource.class))
            .usersByUsernameQuery(userDetailsQuery)
            .passwordEncoder(new Ripemd160PasswordEncoder())
            .authoritiesByUsernameQuery(userRolesQuery)
            .rolePrefix(rolePrefix);
          break;
      }
    }

    /**
     * Configure Ldap provider for bind auth
     *
     * @return LdapAuthenticationProvider
     */
    @Bean
    @ConditionalOnMissingBean
    public LdapAuthenticationProvider ldapAuthenticationProvider() {

      // Bind authenticator with search filter
      final BindAuthenticator bindAuthenticator = new BindAuthenticator(getBean(LdapContextSource.class));
      bindAuthenticator.setUserSearch(new FilterBasedLdapUserSearch("", "(" + ldapUserFilter + ")", contextSource()));

      // Ldap provider
      final LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider(bindAuthenticator);
      ldapAuthenticationProvider.setHideUserNotFoundExceptions(false);
      ldapAuthenticationProvider.setAuthoritiesMapper(new SimpleAuthorityMapper());

      return ldapAuthenticationProvider;
    }

    /**
     * Spring context source for ldap connection
     *
     * @return Ldap context
     */
    @Bean
    @ConditionalOnMissingBean
    public LdapContextSource contextSource() {
      LdapContextSource ldapContextSource = new LdapContextSource();
      ldapContextSource.setUrls(ldapUrl);
      ldapContextSource.setBase(ldapBaseDN);
      ldapContextSource.setUserDn(ldapUserDN);
      ldapContextSource.setPassword(ldapPassword);
      ldapContextSource.setPooled(true);

      return ldapContextSource;
    }

    /**
     * Get access control bean to use in the configuration method
     *
     * @return Login access control
     */
    @Bean
    @ConditionalOnMissingBean
    public LoginAccessControl loginAccessControl() {
      return new LoginAccessControl();
    }
  }

  /**
   * Jasypt string encryptor to encrypt/decrypt properties
   * @param encryptorConfig Encryptor configuration
   * @return String encryptor bean
   */
  @Bean
  @ConditionalOnMissingBean
  public StringEncryptor jasyptStringEncryptor(SimpleStringPBEConfig encryptorConfig) {
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    SimpleStringPBEConfig config = encryptorConfig;
    config.setPassword(masterKey);
    encryptor.setConfig(config);
    return encryptor;
  }

  /**
   * Jasypt string encriptor configuration
   *
   * @return Encryptor configuration
   */
  @Bean
  @Scope("prototype")
  @ConditionalOnMissingBean
  public SimpleStringPBEConfig encryptorConfig() {
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setAlgorithm("PBEWithMD5AndDES");
    config.setKeyObtentionIterations("1000");
    config.setPoolSize("1");
    config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setStringOutputType("base64");
    return config;
  }

  /////////////////////////////////////////////
  // SERVICES
  /////////////////////////////////////////////

  /**
   * Access service
   * @param menuService Menu service
   * @return Access service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public AccessService accessService(MenuService menuService) {
    return new AccessService(menuService);
  }
}