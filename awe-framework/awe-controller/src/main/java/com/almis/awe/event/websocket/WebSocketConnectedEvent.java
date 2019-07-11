package com.almis.awe.event.websocket;

import com.almis.awe.model.util.log.LogUtil;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

/**
 * Event when websocket is connected
 */
@Component
public class WebSocketConnectedEvent implements ApplicationListener<SessionConnectedEvent> {

  // Autowired services
  private LogUtil logger;

  /**
   * Autowired constructor
   * @param logger LogUtil service
   */
  @Autowired
  public WebSocketConnectedEvent(LogUtil logger) {
    this.logger = logger;
  }

  /**
   * On event launched
   * 
   * @param event websocket event
   */
  @Override
  public void onApplicationEvent(SessionConnectedEvent event) {
    logger.log(WebSocketConnectedEvent.class, Level.INFO, "[WebSocket Connected Event]");
    StompHeaderAccessor.wrap(event.getMessage());
  }
}
