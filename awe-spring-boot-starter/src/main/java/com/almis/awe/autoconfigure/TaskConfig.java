package com.almis.awe.autoconfigure;

import com.almis.awe.executor.ContextAwarePoolExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Class used to launch initial load treads
 */
@Configuration
@ConfigurationProperties(prefix = "awe.task-pool")
@EnableAsync
public class TaskConfig {

  private Integer size;
  private Integer maxSize;
  private Integer queueSize;
  private Integer terminationSeconds;
  private String threadPrefix;

  /**
   * Returns the asynchronous executor task
   * @return Thread pool executor bean
   */
  @Bean("threadPoolTaskExecutor")
  @ConditionalOnMissingBean
  public ContextAwarePoolExecutor getAsyncExecutor() {
    ContextAwarePoolExecutor executor = new ContextAwarePoolExecutor();
    executor.setCorePoolSize(getSize());
    executor.setMaxPoolSize(getMaxSize());
    executor.setQueueCapacity(getQueueSize());
    executor.setAwaitTerminationSeconds(getTerminationSeconds());
    executor.setThreadNamePrefix(getThreadPrefix());
    return executor;
  }

  /**
   * Core pool size
   * @return Size
   */
  public Integer getSize() {
    return size;
  }

  /**
   * Core pool size
   * @param size Size
   */
  public void setSize(Integer size) {
    this.size = size;
  }

  /**
   * Max pool size
   * @return Size
   */
  public Integer getMaxSize() {
    return maxSize;
  }

  /**
   * Max pool size
   * @param maxSize Size
   */
  public void setMaxSize(Integer maxSize) {
    this.maxSize = maxSize;
  }

  /**
   * Queue size
   * @return Size
   */
  public Integer getQueueSize() {
    return queueSize;
  }

  /**
   * Queue size
   * @param queueSize Size
   */
  public void setQueueSize(Integer queueSize) {
    this.queueSize = queueSize;
  }

  /**
   * Termination seconds
   * @return Seconds
   */
  public Integer getTerminationSeconds() {
    return terminationSeconds;
  }

  /**
   * Termination seconds
   * @param terminationSeconds Seconds
   */
  public void setTerminationSeconds(Integer terminationSeconds) {
    this.terminationSeconds = terminationSeconds;
  }

  /**
   * Thread prefix
   * @return Thread prefix
   */
  public String getThreadPrefix() {
    return threadPrefix;
  }

  /**
   * Thread prefix
   * @param threadPrefix Prefix
   */
  public void setThreadPrefix(String threadPrefix) {
    this.threadPrefix = threadPrefix;
  }
}
