package com.almis.awe.autoconfigure;

import com.almis.awe.listener.WebSocketEventListener;
import com.almis.awe.model.tracker.AweClientTracker;
import com.almis.awe.model.tracker.AweConnectionTracker;
import com.almis.awe.service.BroadcastService;
import com.almis.awe.service.InitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * Awe Web Socket configuration.
 *
 * @author mvelez
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

  @Value("${application.acronym}")
  private String applicationAcronym;

  @Value("${security.headers.allowedOrigins:*}")
  private String allowedOrigins;

  @Value("${server.port:8080}")
  private Integer serverPort;

  /**
   * Configures the message broker.
   *
   * @param config Message broker registry
   */
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic", "/queue");
    config.setApplicationDestinationPrefixes("/" + applicationAcronym);
  }

  /**
   * Registers the end points.
   *
   * @param registry Stomp end point registry
   */
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/websocket")
      .setAllowedOrigins(allowedOrigins)
      .addInterceptors(new HttpSessionHandshakeInterceptor())
      .withSockJS();
  }

  /**
   * Awe Client Tracker
   *
   * @return Awe client tracker
   */
  @Bean
  @ConditionalOnMissingBean
  @SessionScope
  public AweClientTracker aweClientTracker() {
    return new AweClientTracker();
  }

  /**
   * Retrieve connection tracker
   *
   * @return Connection tracker
   */
  @Bean
  public AweConnectionTracker aweConnectionTracker() {
    return new AweConnectionTracker();
  }

  /////////////////////////////////////////////
  // SERVICES
  /////////////////////////////////////////////

  /**
   * Broadcast service
   *
   * @param brokerMessagingTemplate Messaging template
   * @param connectionTracker       Connection tracker
   * @return Broadcasting service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public BroadcastService broadcastService(SimpMessagingTemplate brokerMessagingTemplate, AweConnectionTracker connectionTracker) {
    return new BroadcastService(brokerMessagingTemplate, connectionTracker);
  }

  /////////////////////////////////////////////
  // EVENTS
  /////////////////////////////////////////////

  /**
   * Websocket events
   *
   * @return Websocket connected event bean
   */
  @Bean
  @ConditionalOnMissingBean
  public WebSocketEventListener webSocketEvent(InitService initService, AweConnectionTracker connectionTracker) {
    return new WebSocketEventListener(initService, connectionTracker);
  }
}
