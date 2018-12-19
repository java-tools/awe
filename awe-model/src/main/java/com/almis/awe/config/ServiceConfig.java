package com.almis.awe.config;

import com.almis.awe.model.util.log.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.component.AweSession;
import org.springframework.core.env.Environment;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

/**
 * Base class for all custom service
 *
 * @author Jorge BELLON
 */
@Configuration
public abstract class ServiceConfig {

  @Autowired
  private WebApplicationContext context;

  // Injected services
  private AweElements elements;
  private Environment environment;
  private LogUtil logger;

  @PostConstruct
  public void onInitialize() {
    this.elements = getBean(AweElements.class);
    this.environment = context.getEnvironment();
    this.logger = getBean(LogUtil.class);
  }

  /**
   * Returns instantiated elements
   *
   * @return Awe Elements
   */
  public AweElements getElements() {
    return elements;
  }

  /**
   * Returns request object
   *
   * @return Awe request
   */
  public AweRequest getRequest() {
    try {
      AweRequest request = getBean(AweRequest.class);
      request.getParameterList();
      return request;
    } catch (Exception exc) {
      return null;
    }
  }

  /**
   * Check if bean is defined
   * @param bean Bean class
   * @return Bean
   */
  public boolean containsBean(String bean) {
    return context.containsBean(bean);
  }

  /**
   * Retrieve a bean
   * @param clazz Bean class
   * @param <T> Bean
   * @return Bean
   */
  public <T> T getBean(Class<T> clazz) {
    return context.getBean(clazz);
  }

  /**
   * Retrieve a bean
   * @param beanId Bean identifier
   * @return Bean
   */
  public Object getBean(String beanId) {
    return context.getBean(beanId);
  }

  /**
   * Get current users session
   *
   * @return Awe Session
   */
  public AweSession getSession() {
    return getBean(AweSession.class);
  }

  /**
   * Retrieve locale
   * @param locale Locale identifier
   * @return Locale text
   */
  public String getLocale(String locale) {
    return getElements().getLocale(locale);
  }

  /**
   * Retrieve locale with parameter
   * @param locale Locale identifier
   * @param parameters Parameter
   * @return Locale text
   */
  public String getLocale(String locale, String... parameters) {
    return getElements().getLocale(locale, parameters);
  }

  /**
   * Retrieve propertu
   * @param property Property identifier
   * @return Property value
   */
  public String getProperty(String property) {
    return environment.getProperty(property);
  }

  /**
   * Retrieve propertu
   * @param <T> Return value class
   * @param property Property identifier
   * @param clazz Property class
   * @return Property value
   */
  public <T> T getProperty(String property, Class<T> clazz) {
    return environment.getProperty(property, clazz);
  }

  /**
   * Retrieve logger
   * @return Logger
   */
  public LogUtil getLogger() {
    return logger;
  }
}
