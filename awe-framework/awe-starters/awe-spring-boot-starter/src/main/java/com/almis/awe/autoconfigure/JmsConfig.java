package com.almis.awe.autoconfigure;

import com.almis.awe.component.AweJmsDestination;
import com.almis.awe.listener.QueueListener;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.component.XStreamSerializer;
import com.almis.awe.model.entities.queues.MessageBuilder;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.service.BroadcastService;
import com.almis.awe.service.QueryService;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.service.data.builder.QueueBuilder;
import com.almis.awe.service.data.connector.maintain.QueueMaintainConnector;
import com.almis.awe.service.data.connector.query.QueueQueryConnector;
import com.almis.awe.service.data.processor.QueueProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.context.WebApplicationContext;

import javax.jms.ConnectionFactory;

/**
 * Class used to launch initial load treads
 */
@Configuration
@ConditionalOnProperty(name = "awe.jms.enabled", havingValue = "true")
@EnableJms
@Lazy
public class JmsConfig {

  /**
   * Listener container factory
   *
   * @param connectionFactory Connection factory
   * @param configurer        Configurer
   * @return Container factory
   */
  @Bean
  public JmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                 DefaultJmsListenerContainerFactoryConfigurer configurer) {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    // This provides all boot's default to this factory, including the message converter
    configurer.configure(factory, connectionFactory);
    return factory;
  }

  /**
   * AWE JMS Destination
   *
   * @param elements     Awe Elements
   * @param logUtil      Logger
   * @param queryService Query service
   * @return JMS Destination
   */
  @Bean
  @ConditionalOnMissingBean
  public AweJmsDestination aweJmsDestination(AweElements elements, LogUtil logUtil, QueryService queryService) {
    return new AweJmsDestination(elements, logUtil, queryService);
  }

  /////////////////////////////////////////////
  // CONNECTORS
  /////////////////////////////////////////////

  /**
   * Queue Query connector
   * @param queryUtil Query util
   * @return Queue Query connector bean
   */
  @Bean
  @ConditionalOnMissingBean
  public QueueQueryConnector queueQueryConnector(QueryUtil queryUtil) {
    return new QueueQueryConnector(queryUtil);
  }

  /**
   * Queue Maintain connector
   * @return Queue Query connector bean
   */
  @Bean
  @ConditionalOnMissingBean
  public QueueMaintainConnector queueMaintainConnector() {
    return new QueueMaintainConnector();
  }

  /////////////////////////////////////////////
  // PROCESSOR
  /////////////////////////////////////////////

  /**
   * Queue processor
   * @return Queue list builder bean
   */
  @Bean
  @ConditionalOnMissingBean
  @Scope("prototype")
  public QueueProcessor queueProcessor(XStreamSerializer serializer) {
    return new QueueProcessor(serializer);
  }

  /////////////////////////////////////////////
  // BUILDERS
  /////////////////////////////////////////////

  /**
   * Queue builder
   * @param jmsDestination Destination
   * @param connectionFactory Connection factory
   * @param transactionManager Transaction manager
   * @param queryUtil Query utilities
   * @return Queue builder bean
   */
  @Bean
  @ConditionalOnMissingBean
  @Scope("prototype")
  public QueueBuilder queueBuilder(AweJmsDestination jmsDestination, ConnectionFactory connectionFactory,
                                   PlatformTransactionManager transactionManager, QueryUtil queryUtil) {
    return new QueueBuilder(jmsDestination, connectionFactory, transactionManager, queryUtil);
  }

  /**
   * Message builder
   * @param context Context
   * @param serializer Serializer
   * @return Message builder bean
   */
  @Bean
  @ConditionalOnMissingBean
  @Scope("prototype")
  public MessageBuilder messageBuilder(WebApplicationContext context, XStreamSerializer serializer) {
    return new MessageBuilder(context, serializer);
  }

  /////////////////////////////////////////////
  // LISTENERS
  /////////////////////////////////////////////

  /**
   * Queue listener
   * @param queryService Query service
   * @param broadcastService Broadcasting service
   * @return Queue listener bean
   */
  @Bean
  @ConditionalOnMissingBean
  @Scope("prototype")
  public QueueListener queueListener(QueryService queryService, BroadcastService broadcastService) {
    return new QueueListener(queryService, broadcastService);
  }
}
