package com.almis.awe.developer.autoconfigure;

/**
 * Created by pgarcia on 21/12/2017.
 */

import com.almis.awe.developer.service.LiteralsService;
import com.almis.awe.developer.service.PathService;
import com.almis.awe.developer.util.LocaleUtil;
import com.almis.awe.model.component.XStreamSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * Tool module configuration
 */
@Configuration
@PropertySource(value = "classpath:config/developer.properties")
public class DeveloperConfig {

  // Autowired services
  Environment environment;

  /**
   * Autowired constructor
   * @param environment Environment
   */
  @Autowired
  public DeveloperConfig(Environment environment) {
    this.environment = environment;
  }

  /**
   * Path management service
   * @return Path management  bean
   */
  @Bean
  @ConditionalOnMissingBean
  public PathService pathService() {
    return new PathService();
  }

  /**
   * Literals management service
   * @return Literals management bean
   */
  @Bean
  @ConditionalOnMissingBean
  public LiteralsService literalsService(PathService pathService, XStreamSerializer serializer) {
    return new LiteralsService(pathService, serializer);
  }

  /**
   * Initialize static utilities
   */
  @PostConstruct
  public void initializeStaticUtilities() {
    LocaleUtil.init(environment);
  }

}
