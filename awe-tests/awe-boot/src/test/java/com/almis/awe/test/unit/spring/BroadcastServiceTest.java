package com.almis.awe.test.unit.spring;

import com.almis.awe.builder.client.ScreenActionBuilder;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.tracker.AweConnectionTracker;
import com.almis.awe.service.BroadcastService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test broadcasting functions
 *
 * @author pgarcia
 */
public class BroadcastServiceTest {

  @InjectMocks
  private BroadcastService broadcastService;

  @Mock
  private SimpMessagingTemplate brokerMessagingTemplate;

  @Mock
  private AweConnectionTracker connectionTracker;

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void initBeans() {
    MockitoAnnotations.initMocks(this);

    Set<String> connections = new HashSet<>();
    connections.add("connection");
    when(connectionTracker.getUserConnections(anyString())).thenReturn(connections);
    when(connectionTracker.getScreenConnections(anyString())).thenReturn(connections);
    when(connectionTracker.isUserConnected(anyString())).thenReturn(true);
  }

  /**
   * Test broadcasting a client action to all
   */
  @Test
  public void testBroadcastClientAction() {
    broadcastService.broadcastMessage(new ScreenActionBuilder("newScreen").build());
    verify(brokerMessagingTemplate, atLeastOnce()).convertAndSend(eq("/topic/broadcast"), any(ClientAction[].class));
  }

  /**
   * Test broadcasting a client action to some users
   */
  @Test
  public void testBroadcastClientActionToUsers() {
    broadcastService.broadcastMessageToUsers(new ScreenActionBuilder("newScreen").build(), "tutu", "lala");
    verify(brokerMessagingTemplate, atLeastOnce()).convertAndSend(eq("/topic/connection"), any(ClientAction[].class));
  }

  /**
   * Test broadcasting a client action list to some users
   */
  @Test
  public void testBroadcastClientActionsToUsers() {
    List<ClientAction> clientActionList = new ArrayList<>();
    clientActionList.add(new ScreenActionBuilder("newScreen").build());
    clientActionList.add(new ScreenActionBuilder("otherScreen").build());
    broadcastService.broadcastMessageToUsers(clientActionList, "tutu", "lala");
    verify(brokerMessagingTemplate, atLeastOnce()).convertAndSend(eq("/topic/connection"), any(ClientAction[].class));
  }


  /**
   * Test broadcasting a client action to a concrete screen
   */
  @Test
  public void testBroadcastClientActionToScreen() {
    broadcastService.broadcastMessageToScreen("tutu", new ScreenActionBuilder("newScreen").build());
    verify(brokerMessagingTemplate, atLeastOnce()).convertAndSend(eq("/topic/connection"), any(ClientAction[].class));
  }

  /**
   * Test broadcasting a client action to some users
   */
  @Test
  public void testBroadcastClientActionToUsersWithoutUsers() {
    broadcastService.broadcastMessageToUsers(new ScreenActionBuilder("newScreen").build());
    verify(brokerMessagingTemplate, Mockito.never()).convertAndSend(eq("/topic/connection"), any(ClientAction.class));
  }

  /**
   * Test broadcasting a client action list to some users
   */
  @Test
  public void testBroadcastClientActionsToUsersWithoutUsers() {
    List<ClientAction> clientActionList = new ArrayList<>();
    clientActionList.add(new ScreenActionBuilder("newScreen").build());
    clientActionList.add(new ScreenActionBuilder("otherScreen").build());
    broadcastService.broadcastMessageToUsers(clientActionList);
    verify(brokerMessagingTemplate, Mockito.never()).convertAndSend(eq("/topic/connection"), any(ClientAction.class));
  }
}