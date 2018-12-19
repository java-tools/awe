package com.almis.awe.model.messages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Util class for format message in logs management
 *
 * @author pvidal - 11/SEP/2014
 */
public class AweMessage implements Message {

	private static final long serialVersionUID = -1281689202246503929L;
	private static Logger logger = LogManager.getLogger(AweMessage.class);
  private final transient List<Object> parameters;
  private final String message;
  private String database;
  private String user;
  private String screen;

  /**
   * Constructor with message and parameters
   *
   * @param message Message text
   *  @param parameters Message parameters
   */
  public AweMessage(String message, final Object... parameters) {
    this.message = message;
    this.parameters = parameters == null ? null : Arrays.asList(parameters);
  }

  @Override
  public String getFormat() {
    return message;
  }

  @Override
  public Object[] getParameters() {
    return parameters != null ? parameters.toArray(new Object[parameters.size()]) : null;
  }

  @Override
  public Throwable getThrowable() {
    return null;
  }

  @Override
  public String getFormattedMessage() {
    String messageFormatted = message;
    try {
      if (message != null && parameters != null) {
        MessageFormat messageFormat = new MessageFormat(message);
        messageFormatted = messageFormat.format(getParameters());
      }
    } catch (IllegalArgumentException ex) {
      logger.error("Error message formating: " + message + "\n" + "\t\t\t\t" + "Parameters: " + formatParameters(), ex);
    }
    // Complete message with other info
    return completeLogMessage(messageFormatted);
  }

  /**
   * Format parameters to log
   *
   * @return Message formatted
   */
  private String formatParameters() {
    StringBuilder sb = new StringBuilder();
    boolean first = true;

    if (parameters != null) {
      for (Object parameter : parameters) {
        if (!first) {
          sb.append(", ");
        }
        sb.append(parameter.toString());
        first = false;
      }
    }
    return sb.toString();
  }

  /**
   * Completes a message with needed information
   * @return the complete message with params
   */
  public String toString() {
    return getFormattedMessage();
  }

  /**
   * Completes a message with needed information
   *
   * @param message log message
   * @return the complete message with params
   */
  private String completeLogMessage(String message) {
    StringBuilder sb = new StringBuilder();

    // Add user
    if (getUser() != null && !getUser().isEmpty()) {
      sb.append("[Usr: ");
      sb.append(getUser());
      sb.append("] ");
    }

    // Add screen
    if (getScreen() != null && !getScreen().isEmpty()) {
      sb.append("[Screen: ");
      sb.append(getScreen());
      sb.append("] ");
    }

    // Add database
    if (getDatabase() != null && !getDatabase().isEmpty()) {
      sb.append("[DB: ");
      sb.append(getDatabase());
      sb.append("] ");
    }

    sb.append(message);

    // Return message with logged extra parameters
    return sb.toString();
  }

  /**
   * Retrieve current database
   * @return database
   */
  public String getDatabase() {
    return database;
  }

  /**
   * Set current database
   * @param database Database
   * @return this
   */
  public AweMessage setDatabase(String database) {
    this.database = database;
    return this;
  }

  /**
   * Retrieve current user
   * @return User
   */
  public String getUser() {
    return user;
  }

  /**
   * Store current user
   * @param user User
   * @return this
   */
  public AweMessage setUser(String user) {
    this.user = user;
    return this;
  }

  /**
   * Retrieve last screen
   * @return Screen
   */
  public String getScreen() {
    return screen;
  }

  /**
   * Store last screen
   * @param screen Screen
   * @return this
   */
  public AweMessage setScreen(String screen) {
    this.screen = screen;
    return this;
  }
}
