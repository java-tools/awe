package com.almis.awe.model.tracker;

import com.almis.awe.model.details.ConnectionDetails;
import com.almis.awe.model.event.ScreenChangeEvent;
import org.springframework.context.event.EventListener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class AweConnectionTracker {
  private ConcurrentMap<String, ConcurrentMap<String, ConnectionDetails>> connectedUsers = new ConcurrentHashMap<>();

  /**
   * Check if user is connected
   *
   * @param user User to check
   * @return User is connected
   */
  public boolean isUserConnected(String user) {
    return user != null && connectedUsers.containsKey(user);
  }

  /**
   * Check if connection is active
   *
   * @param user User to check
   * @return User is connected
   */
  public boolean isConnectionActive(String user, String connection) {
    return isUserConnected(user) && connection != null && connectedUsers.get(user).containsKey(connection);
  }

  /**
   * Retrieve user connections
   *
   * @param user User
   * @return Connection set
   */
  public Set<String> getUserConnections(String user) {
    if (isUserConnected(user)) {
      return connectedUsers.get(user).keySet();
    }
    return Collections.emptySet();
  }

  /**
   * Retrieve connections which are in a concrete screen
   *
   * @param screen Screen
   * @return Connection set
   */
  public Set<String> getScreenConnections(String screen) {
    Set<String> connections = new HashSet<>();
    connectedUsers
      .entrySet()
      .forEach(e1 -> connections.addAll(e1.getValue().entrySet().stream()
        .filter(e2 -> screen.equalsIgnoreCase(e2.getValue().getScreen()))
        .map(Entry::getKey)
        .collect(Collectors.toSet())));
    return connections;
  }

  /**
   * Retrieve user connections
   *
   * @param user User
   * @return Connection set
   */
  public Set<String> getUserConnectionsFromSession(String user, String session) {
    if (isUserConnected(user)) {
      return connectedUsers.get(user)
        .entrySet()
        .stream()
        .filter(e -> session.equalsIgnoreCase(e.getValue().getSession()))
        .collect(Collectors.toConcurrentMap(Entry::getKey, Entry::getValue))
        .keySet();
    }
    return Collections.emptySet();
  }

  /**
   * Initializes user connections
   *
   * @param user User
   */
  private void initializeUserConnections(String user) {
    if (user != null && !connectedUsers.containsKey(user)) {
      connectedUsers.put(user, new ConcurrentHashMap<>());
    }
  }

  /**
   * Initializes user connections
   *
   * @param user User
   */
  public void initializeUserConnections(String user, String connection, String session) {
    initializeUserConnections(user);
    addConnectionToUser(user, connection, session);
  }

  /**
   * Add connection to user
   *
   * @param user       User
   * @param connection Connection to add
   */
  private void addConnectionToUser(String user, String connection, String session) {
    if (!isConnectionActive(user, connection)) {
      connectedUsers.get(user).put(connection, new ConnectionDetails()
        .setUser(user)
        .setSession(session)
        .setConnection(connection));
    }
  }

  /**
   * Remove all connections from user session
   *
   * @param user User
   */
  public void removeAllConnectionsFromUserSession(String user, String session) {
    if (isUserConnected(user)) {
      connectedUsers.put(user, connectedUsers.get(user)
        .entrySet()
        .stream()
        .filter(e -> !session.equalsIgnoreCase(e.getValue().getSession()))
        .collect(Collectors.toConcurrentMap(Entry::getKey, Entry::getValue))
      );
    }
  }

  /**
   * Retrieve all connections
   *
   * @return Connection set
   */
  public ConcurrentMap<String, ConcurrentMap<String, ConnectionDetails>> getAllConnections() {
    return connectedUsers;
  }

  /**
   * On screen change event
   *
   * @param screenChangeEvent
   */
  @EventListener
  public void onScreenChange(ScreenChangeEvent screenChangeEvent) {
    if (isConnectionActive(screenChangeEvent.getUser(), screenChangeEvent.getConnection())) {
      connectedUsers.get(screenChangeEvent.getUser())
        .get(screenChangeEvent.getConnection())
        .setScreen(screenChangeEvent.getScreen());
    }
  }
}
