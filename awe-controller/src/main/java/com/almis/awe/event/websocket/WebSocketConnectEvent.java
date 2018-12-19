package com.almis.awe.event.websocket;

import com.almis.awe.model.util.log.LogUtil;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/**
 * Event when websocket connects
 */
@Component
public class WebSocketConnectEvent implements ApplicationListener<SessionConnectEvent> {

  // Autowired services
  private LogUtil logger;

  /**
   * Autowired constructor
   * @param logger
   */
  @Autowired
  public WebSocketConnectEvent(LogUtil logger) {
    this.logger = logger;
  }

  /**
   * On event launched
   * 
   * @param event
   */
  @Override
  public void onApplicationEvent(SessionConnectEvent event) {
    logger.log(WebSocketConnectEvent.class, Level.INFO, "[WebSocket Connect Event]");
    StompHeaderAccessor.wrap(event.getMessage());
  }

}
