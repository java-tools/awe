package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.tracker.AweConnectionTracker;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.util.log.LogUtil;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Set;

/**
 * BroadcastService Class
 * <p>
 * AWE Broadcast Engine
 * Provides generate function to send information to client
 *
 * @author Pablo GARCIA
 */
public class BroadcastService extends ServiceConfig {

  // Autowired services
  private SimpMessagingTemplate brokerMessagingTemplate;
  private AweConnectionTracker connectionTracker;
  private LogUtil logger;

  /**
   * Autowired constructor
   *
   * @param brokerMessagingTemplate Broker messaging template
   * @param connectionTracker       Connection tracker
   * @param logger                  Logger
   */
  @Autowired
  public BroadcastService(SimpMessagingTemplate brokerMessagingTemplate, AweConnectionTracker connectionTracker,
                          LogUtil logger) {
    this.brokerMessagingTemplate = brokerMessagingTemplate;
    this.connectionTracker = connectionTracker;
    this.logger = logger;
  }

  /**
   * Broadcast an action list
   *
   * @param actionList Action list to broadcast
   */
  public void broadcastMessage(ClientAction... actionList) {
    logger.log(BroadcastService.class, Level.DEBUG, "Broadcasting message to all connected customers: {0} actions", actionList.length);
    brokerMessagingTemplate.convertAndSend("/topic/broadcast", actionList);
  }

  /**
   * Broadcast an action list
   *
   * @param actionList Action list to broadcast
   */
  public void broadcastMessage(List<ClientAction> actionList) {
    broadcastMessage(actionList.toArray(new ClientAction[0]));
  }

  /**
   * Broadcast an action list to a user
   *
   * @param user       User
   * @param actionList Action list to broadcast
   */
  public void broadcastMessageToUser(String user, ClientAction... actionList) {
    if (connectionTracker.isUserConnected(user)) {
      Set<String> sessions = connectionTracker.getUserConnections(user);
      for (String cometUID : sessions) {
        broadcastMessageToUID(cometUID, actionList);
      }
    }
  }

  /**
   * Broadcast an action list to a user
   *
   * @param user       User
   * @param actionList Action list to broadcast
   */
  public void broadcastMessageToUser(String user, List<ClientAction> actionList) {
    broadcastMessageToUser(user, actionList.toArray(new ClientAction[0]));
  }

  /**
   * Broadcast an action to some users
   *
   * @param action Action to broadcast
   * @param users  User list
   */
  public void broadcastMessageToUsers(ClientAction action, String... users) {
    for (String user : users) {
      broadcastMessageToUser(user, action);
    }
  }

  /**
   * Broadcast an action list to some users
   *
   * @param actions Action list to broadcast
   * @param users   User list
   */
  public void broadcastMessageToUsers(List<ClientAction> actions, String... users) {
    for (String user : users) {
      broadcastMessageToUser(user, actions);
    }
  }

  /**
   * Broadcast an action list to a connection
   *
   * @param cometUID   Connection ID
   * @param actionList Action list to broadcast
   */
  public void broadcastMessageToUID(String cometUID, ClientAction... actionList) {
    logger.log(BroadcastService.class, Level.DEBUG, "Broadcasting message to {0}: {1} actions", "/topic/" + cometUID, actionList.length);
    brokerMessagingTemplate.convertAndSend("/topic/" + cometUID, actionList);
  }

  /**
   * Broadcast an action list to a connection
   *
   * @param cometUID   Connection ID
   * @param actionList Action list to broadcast
   */
  public void broadcastMessageToUID(String cometUID, List<ClientAction> actionList) {
    broadcastMessageToUID(cometUID, actionList.toArray(new ClientAction[0]));
  }

  /**
   * Send a message to a user
   *
   * @param user    User
   * @param message Message to send
   * @return Service data
   */
  public ServiceData sendMessageToUser(String user, String message) {
    ServiceData serviceData = new ServiceData();

    // Generate message action
    String title = getLocale("SCREEN_TEXT_MESSAGE_FROM", getSession().getUser());
    ClientAction messageAction = generateMessageAction(AnswerType.INFO.toString(), title, message);

    // Send message
    if (user != null) {
      broadcastMessageToUser(user, messageAction);
    } else {
      broadcastMessage(messageAction);
    }

    // Return ok message
    return serviceData.setTitle(getLocale("OK_TITLE_MESSAGE_SENT")).setMessage(getLocale("OK_MESSAGE_MESSAGE_SENT"));
  }

  /**
   * Send an error message
   *
   * @param title   Title
   * @param message Message to send
   */
  public void sendError(String title, String message) {
    // Generate message action
    ClientAction messageAction = generateMessageAction(AnswerType.ERROR.toString(), title, message);

    // Send message
    broadcastMessage(messageAction);
  }

  /**
   * Send an error message to a user
   *
   * @param user    Connection id
   * @param title   Title
   * @param message Message to send
   */
  public void sendErrorToUser(String user, String title, String message) {
    // Generate message action
    ClientAction messageAction = generateMessageAction(AnswerType.ERROR.toString(), title, message);

    // Send message
    broadcastMessageToUser(user, messageAction);
  }

  /**
   * Send an error message to a connection
   *
   * @param cometUID Connection id
   * @param title    Title
   * @param message  Message to send
   */
  public void sendErrorToUID(String cometUID, String title, String message) {
    // Generate message action
    ClientAction messageAction = generateMessageAction(AnswerType.ERROR.toString(), title, message);

    // Send message
    broadcastMessageToUID(cometUID, messageAction);
  }

  /**
   * Generate message action
   *
   * @param type    Message type
   * @param title   Message title
   * @param content Message contents
   * @return Message client action
   */
  private ClientAction generateMessageAction(String type, String title, String content) {
    // Generate message action
    ClientAction messageAction = new ClientAction("message")
      .setAsync(true)
      .addParameter("type", type)
      .addParameter("message", content);

    if (title != null) {
      messageAction.addParameter("title", title);
    }

    return messageAction;
  }
}
