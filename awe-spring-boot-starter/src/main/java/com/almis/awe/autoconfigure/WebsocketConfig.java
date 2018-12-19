package com.almis.awe.autoconfigure;

import com.almis.awe.event.websocket.WebSocketConnectEvent;
import com.almis.awe.event.websocket.WebSocketConnectedEvent;
import com.almis.awe.event.websocket.WebSocketDisconnectEvent;
import com.almis.awe.model.component.AweClientTracker;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.service.BroadcastService;
import com.almis.awe.service.InitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Awe Web Socket configuration.
 *
 * @author mvelez
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

  @Value("application.acronym")
  private String applicationAcronym;
  private Map<String, List<String>> connectedUsers = Collections.synchronizedMap(new HashMap<>());

  /**
   * Configures the message broker.
   * @param config
   */
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic", "/queue");
    config.setApplicationDestinationPrefixes("/" + applicationAcronym);
  }

  /**
   * Registers the "/chat" end point.
   * @param registry
   */
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/websocket").withSockJS();
  }

  /**
   * Awe Client Tracker
   * @return
   */
  @Bean
  @ConditionalOnMissingBean
  @SessionScope
  public AweClientTracker aweClientTracker() {
    return new AweClientTracker();
  }

  /**
   * Retrieve connected users
   * @return Connected users
   */
  @Bean
  public Map<String, List<String>> connectedUsers() {
    return connectedUsers;
  }

  /////////////////////////////////////////////
  // SERVICES
  /////////////////////////////////////////////

  /**
   * Broadcast service
   * @param brokerMessagingTemplate Messaging template
   * @param connectedUsers Connected users
   * @param logger Logger
   * @return Broadcasting service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public BroadcastService broadcastService(SimpMessagingTemplate brokerMessagingTemplate, Map<String, List<String>> connectedUsers,
                                      LogUtil logger) {
    return new BroadcastService(brokerMessagingTemplate, connectedUsers, logger);
  }

  /////////////////////////////////////////////
  // EVENTS
  /////////////////////////////////////////////

  /**
   * Websocket connect event
   * @param logUtil Logger
   * @return Websocket connect event bean
   */
  @Bean
  @ConditionalOnMissingBean
  public WebSocketConnectEvent webSocketConnectEvent(LogUtil logUtil) {
    return new WebSocketConnectEvent(logUtil);
  }

  /**
   * Websocket connected event
   * @param logUtil Logger
   * @return Websocket connected event bean
   */
  @Bean
  @ConditionalOnMissingBean
  public WebSocketConnectedEvent webSocketConnectedEvent(LogUtil logUtil) {
    return new WebSocketConnectedEvent(logUtil);
  }

  /**
   * Websocket disconnect event
   * @param initService Init Service
   * @param logUtil Logger
   * @return Websocket disconnect event bean
   */
  @Bean
  @ConditionalOnMissingBean
  public WebSocketDisconnectEvent webSocketDisconnectEvent(InitService initService, LogUtil logUtil) {
    return new WebSocketDisconnectEvent(initService, logUtil);
  }
}
