package com.almis.awe.test.unit.pojo;

import com.almis.awe.model.details.ConnectionDetails;
import com.almis.awe.model.event.ScreenChangeEvent;
import com.almis.awe.model.tracker.AweConnectionTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;

/**
 * DataList, DataListUtil and DataListBuilder tests
 *
 * @author pgarcia
 */
public class AweConnectionTrackerTest {

  @InjectMocks
  private AweConnectionTracker connectionTracker;

  /**
   * Initializes json mapper for tests
   */
  @BeforeEach
  public void initBeans() throws Exception {
    MockitoAnnotations.initMocks(this);

    // Initialize
    connectionTracker.getAllConnections().clear();
    ConcurrentMap<String, ConnectionDetails> user1Connections = new ConcurrentHashMap<>();
    user1Connections.put("connection1", new ConnectionDetails().setConnection("connection1").setScreen("screen1").setSession("session1").setUser("user1"));
    user1Connections.put("connection2", new ConnectionDetails().setConnection("connection2").setScreen("screen2").setSession("session1").setUser("user1"));
    user1Connections.put("connection3", new ConnectionDetails().setConnection("connection3").setScreen("screen3").setSession("session3").setUser("user1"));
    ConcurrentMap<String, ConnectionDetails> user2Connections = new ConcurrentHashMap<>();
    user2Connections.put("connection4", new ConnectionDetails().setConnection("connection4").setScreen("screen2").setSession("session4").setUser("user2"));
    connectionTracker.getAllConnections().put("user1", user1Connections);
    connectionTracker.getAllConnections().put("user2", user2Connections);
  }

  @Test
  public void isUserConnected() {
    assertTrue(connectionTracker.isUserConnected("user2"));
    assertFalse(connectionTracker.isUserConnected("user3"));
  }

  @Test
  public void isConnectionActive() {
    assertTrue(connectionTracker.isConnectionActive("user1", "connection2"));
    assertTrue(connectionTracker.isConnectionActive("user2", "connection4"));
    assertFalse(connectionTracker.isConnectionActive("user2", "connection3"));
    assertFalse(connectionTracker.isConnectionActive("user4", "connection6"));
  }

  @Test
  public void getUserConnections() {
    assertEquals(3, connectionTracker.getUserConnections("user1").size());
    assertEquals(1, connectionTracker.getUserConnections("user2").size());
  }

  @Test
  public void getScreenConnections() {
    assertEquals(2, connectionTracker.getScreenConnections("screen2").size());
    assertTrue(connectionTracker.getScreenConnections("screen2").contains("connection2"));
    assertTrue(connectionTracker.getScreenConnections("screen2").contains("connection4"));
    assertEquals(1, connectionTracker.getScreenConnections("screen3").size());
    assertFalse(connectionTracker.getScreenConnections("screen3").contains("connection2"));
  }

  @Test
  public void getUserConnectionsFromSession() {
    assertEquals(2, connectionTracker.getUserConnectionsFromSession("user1", "session1").size());
    assertEquals(0, connectionTracker.getUserConnectionsFromSession("user1", "session2").size());
  }

  @Test
  public void initializeUserConnections() {
    connectionTracker.initializeUserConnections("user5", "connection5", "session5");
    assertEquals(1, connectionTracker.getUserConnections("user5").size());
  }

  @Test
  public void removeAllConnectionsFromUserSession() {
    connectionTracker.removeAllConnectionsFromUserSession("user1", "session1");
    assertEquals(0, connectionTracker.getUserConnectionsFromSession("user1", "session1").size());
  }

  @Test
  public void getAllConnections() {
    assertEquals(2, connectionTracker.getAllConnections().size());
  }

  @Test
  public void onScreenChange() {
    connectionTracker.onScreenChange(new ScreenChangeEvent(Mockito.mock(ApplicationEvent.class), "user1", "connection1", "screen10"));
    assertTrue(connectionTracker.getScreenConnections("screen10").contains("connection1"));
  }
}