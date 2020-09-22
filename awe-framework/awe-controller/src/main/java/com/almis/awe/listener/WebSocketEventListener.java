package com.almis.awe.listener;

import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.tracker.AweConnectionTracker;
import com.almis.awe.model.type.LaunchPhaseType;
import com.almis.awe.service.InitService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Objects;

/**
 * Event when websocket is connected
 */
@Log4j2
public class WebSocketEventListener {

  // Autowired services
  private final InitService initService;
  private final AweConnectionTracker connectionTracker;

  /**
   * Autowired constructor
   *
   * @param initService Init service
   */
  public WebSocketEventListener(InitService initService, AweConnectionTracker connectionTracker) {
    this.initService = initService;
    this.connectionTracker = connectionTracker;
  }

  /**
   * On connect event
   *
   * @param event Session connect event
   */
  @EventListener
  public void onConnectEvent(SessionConnectEvent event) {
    log.info("[WebSocket Connect Event]");
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    Principal user = event.getUser();

    if (user != null) {
      String token = Objects.requireNonNull(accessor.getNativeHeader(AweConstants.SESSION_CONNECTION_HEADER)).get(0);
      connectionTracker.initializeUserConnections(user.getName(), token, (String) Objects.requireNonNull(accessor.getSessionAttributes()).get("HTTP.SESSION.ID"));
    }
  }

  /**
   * On connected event
   *
   * @param event websocket event
   */
  @EventListener
  public void onConnectedEvent(SessionConnectedEvent event) {
    log.info("[WebSocket Connected Event]");
    StompHeaderAccessor.wrap(event.getMessage());
  }

  /**
   * On disconnect event
   *
   * @param event websocket disconnect event
   */
  @EventListener
  public void onDisconnectEvent(SessionDisconnectEvent event) {
    log.info("[WebSocket Disconnect Event]");
    StompHeaderAccessor.wrap(event.getMessage());

    // Launch client end services
    initService.launchPhaseServices(LaunchPhaseType.CLIENT_END);
  }
}
