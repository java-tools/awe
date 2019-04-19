package com.almis.awe.event.websocket;

import com.almis.awe.service.InitService;
import com.almis.awe.model.type.LaunchPhaseType;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.almis.awe.model.util.log.LogUtil;

/**
 * Event when websocket disconnects
 */
@Component
public class WebSocketDisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {

  // Autowired services
  private LogUtil logger;
  private InitService initService;

  /**
   * Autowired constructor
   * @param logger
   */
  @Autowired
  public WebSocketDisconnectEvent(InitService initService, LogUtil logger) {
    this.initService = initService;
    this.logger = logger;
  }

  /**
   * On event launched
   * 
   * @param event
   */
  @Override
  public void onApplicationEvent(SessionDisconnectEvent event) {
    logger.log(WebSocketConnectEvent.class, Level.INFO, "[WebSocket Disconnect Event]");
    StompHeaderAccessor.wrap(event.getMessage());
    initService.launchPhaseServices(LaunchPhaseType.CLIENT_END);
  }
}
