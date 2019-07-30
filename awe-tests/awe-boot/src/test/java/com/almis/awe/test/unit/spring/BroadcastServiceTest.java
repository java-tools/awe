package com.almis.awe.test.unit.spring;

import com.almis.awe.builder.client.ScreenActionBuilder;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.service.BroadcastService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;

import static org.mockito.Mockito.atLeastOnce;

/**
 * Test broadcasting functions
 * @author pgarcia
 */
public class BroadcastServiceTest {

  // Broadcast service
  private BroadcastService broadcastService;
  private SimpMessagingTemplate brokerMessagingTemplate;
  private Map<String, Set<String>> connectedUsers = new HashMap<>();
  private LogUtil logger;

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void initBeans() {
    Set<String> sessions = new HashSet();
    sessions.add("session");
    connectedUsers.put("tutu", sessions);
    brokerMessagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
    logger = Mockito.mock(LogUtil.class);
    broadcastService = new BroadcastService(brokerMessagingTemplate, connectedUsers, logger);
  }

  /**
   * Test broadcasting a client action to some users
   */
  @Test
  public void testBroadcastClientActionToUsers() {
    broadcastService.broadcastMessageToUsers(new ScreenActionBuilder("newScreen").build(), "tutu", "lala");
    Mockito.verify(brokerMessagingTemplate, atLeastOnce()).convertAndSend(Matchers.eq("/topic/session"), Mockito.any(ClientAction.class));
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
    Mockito.verify(brokerMessagingTemplate, atLeastOnce()).convertAndSend(Matchers.eq("/topic/session"), Mockito.any(ClientAction.class));
  }

  /**
   * Test broadcasting a client action to some users
   */
  @Test
  public void testBroadcastClientActionToUsersWithoutUsers() {
    broadcastService.broadcastMessageToUsers(new ScreenActionBuilder("newScreen").build());
    Mockito.verify(brokerMessagingTemplate, Mockito.never()).convertAndSend(Matchers.eq("/topic/session"), Mockito.any(ClientAction.class));
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
    Mockito.verify(brokerMessagingTemplate,Mockito.never()).convertAndSend(Matchers.eq("/topic/session"), Mockito.any(ClientAction.class));
  }
}