package com.almis.awe.autoconfigure;

import com.almis.awe.model.settings.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Initialize properties
 *
 * @author pgarcia
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@PropertySource("classpath:config/security.properties")
@PropertySource("classpath:config/base.properties")
@PropertySource("classpath:config/library.properties")
@PropertySource("classpath:config/numeric.properties")
@PropertySource("classpath:config/email.properties")
@PropertySource("classpath:config/database.properties")
@PropertySource("classpath:config/microservices.properties")
@PropertySource("classpath:config/session.properties")
@PropertySource("classpath:config/web.properties")
@PropertySource("classpath:config/cache.properties")
public class PropertyConfig {

  /**
   * Web number options
   *
   * @return Web number options bean
   */
  @Bean
  @ConditionalOnMissingBean
  public WebNumberOptions webNumberOptions() {
    return new WebNumberOptions();
  }

  /**
   * Web chart options
   *
   * @return Web chart options bean
   */
  @Bean
  @ConditionalOnMissingBean
  public WebChartOptions webChartOptions() {
    return new WebChartOptions();
  }


  /**
   * Web pivot options
   *
   * @return Web pivot options bean
   */
  @Bean
  @ConditionalOnMissingBean
  public WebPivotOptions webPivotOptions() {
    return new WebPivotOptions();
  }

  /**
   * Web tooltip options
   *
   * @return Web tooltip options bean
   */
  @Bean
  @ConditionalOnMissingBean
  public WebTooltip webTooltip() {
    return new WebTooltip();
  }

  /**
   * Web settings
   *
   * @return Web settings bean
   */
  @Bean
  @ConditionalOnMissingBean
  public WebSettings webSettings(WebNumberOptions numberOptions, WebChartOptions chartOptions, WebTooltip tooltip, WebPivotOptions pivotOptions) {
    return WebSettings.builder()
      .numericOptions(numberOptions)
      .chartOptions(chartOptions)
      .pivotOptions(pivotOptions)
      .messageTimeout(tooltip)
      .build();
  }
}
