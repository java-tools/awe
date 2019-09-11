package com.almis.awe.autoconfigure;

import com.almis.awe.executor.ContextAwarePoolExecutor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Class used to launch initial load treads
 */
@Configuration
@ConfigurationProperties(prefix = "awe.task-pool")
@EnableAsync
@Data
public class TaskConfig {

  private Integer size;
  private Integer maxSize;
  private Integer queueSize;
  private Integer terminationSeconds;
  private String threadPrefix;
  private String contextlessThreadPrefix;

  /**
   * Returns the asynchronous executor task
   * @return Thread pool executor bean
   */
  @Bean("threadPoolTaskExecutor")
  public ContextAwarePoolExecutor getContextAwareTaskExecutor() {
    ContextAwarePoolExecutor executor = new ContextAwarePoolExecutor();
    executor.setCorePoolSize(getSize());
    executor.setMaxPoolSize(getMaxSize());
    executor.setQueueCapacity(getQueueSize());
    executor.setAwaitTerminationSeconds(getTerminationSeconds());
    executor.setThreadNamePrefix(getThreadPrefix());
    return executor;
  }

  /**
   * Returns the asynchronous executor task
   * @return Thread pool executor bean
   */
  @Bean("contextlessTaskExecutor")
  public ThreadPoolTaskExecutor getContextlessTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(getSize());
    executor.setMaxPoolSize(getMaxSize());
    executor.setQueueCapacity(getQueueSize());
    executor.setAwaitTerminationSeconds(getTerminationSeconds());
    executor.setThreadNamePrefix(getContextlessThreadPrefix());
    return executor;
  }
}
