package com.almis.awe.model.util.log;


import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.messages.AweMessage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * LogUtil Class
 * Returns an instance of logger.
 *
 * @author Pablo VIDAL - 11/Sep/2018
 */
public class LogUtil {

  // Log management
  private final ApplicationContext context;

  @Value("${application.log.users.level:info}")
  private String defaultLogLevel;

  @Value("${application.log.users.enabled:false}")
  private boolean logUsersEnabled;

  @Value("${spring.datasource.jndi-name:}")
  private String defaultDatasource;

  /**
   * Autowired constructor
   *
   * @param context Application context
   */
  public LogUtil(ApplicationContext context) {
    this.context = context;
  }

  /**
   * Log a simple message
   *
   * @param logClass Name of the logger that generates the message.
   *                 Note: Default is the class name
   * @param level    Log level
   * @param message  Log message
   */
  public <T> void log(Class<T> logClass, Level level, String message) {
    Logger logger = getLogger(logClass);
    if (!level.equals(Level.DEBUG) || logger.isDebugEnabled()) {
      logger.log(level, generateMessage(message));
    }
  }

  /**
   * Log a message with a throwable
   *
   * @param logClass Name of the logger that generates the message.
   *                 Note: Default is the class name
   * @param level    Log level
   * @param message  Log message
   * @param cause    Log throwable
   */
  public <T> void log(Class<T> logClass, Level level, String message, Throwable cause) {
    Logger logger = getLogger(logClass);
    if (!level.equals(Level.DEBUG) || logger.isDebugEnabled()) {
      logger.log(level, generateMessage(message), cause);
    }
  }

  /**
   * Log a message with a parameter
   *
   * @param logClass  Name of the class that generates the message
   * @param level     Log level
   * @param message   Log message
   * @param parameter Log parameters
   */
  public <T> void log(Class<T> logClass, Level level, String message, EventObject parameter) {
    Logger logger = getLogger(logClass);
    if (!level.equals(Level.DEBUG) || logger.isDebugEnabled()) {
      logger.log(level, generateMessage(message).toString(), parameter);
    }
  }

  /**
   * Log a message with a parameter
   *
   * @param logClass  Name of the class that generates the message
   * @param level     Log level
   * @param message   Log message
   * @param parameter Log parameters
   */
  public <T> void log(Class<T> logClass, Level level, String message, Object parameter) {
    Logger logger = getLogger(logClass);
    if (!level.equals(Level.DEBUG) || logger.isDebugEnabled()) {
      logger.log(level, generateMessage(message, new Object[]{parameter}));
    }
  }

  /**
   * Log a message with a parameter
   *
   * @param logClass   Name of the class that generates the message
   * @param level      Log level
   * @param database   Database ALIAS
   * @param message    Log message
   * @param parameters Log parameters
   */
  public <T> void logWithDatabase(Class<T> logClass, Level level, String database, String message, Object... parameters) {
    Logger logger = getLogger(logClass);
    if (!level.equals(Level.DEBUG) || logger.isDebugEnabled()) {
      logger.log(level, generateMessageWithDatabase(message, parameters, database));
    }
  }

  /**
   * Log a message with a parameter and throwable
   *
   * @param logClass  Name of the class that generates the message
   * @param level     Log level
   * @param message   Log message
   * @param parameter Log parameters
   * @param cause     Log throwable
   */
  public <T> void log(Class<T> logClass, Level level, String message, Throwable cause, Object parameter) {
    Logger logger = getLogger(logClass);
    if (!level.equals(Level.DEBUG) || logger.isDebugEnabled()) {
      logger.log(level, generateMessage(message, new Object[]{parameter}), cause);
    }
  }

  /**
   * Log a message with a parameter list
   *
   * @param logClass   Name of the class that generates the message
   * @param level      Log level
   * @param message    Log message
   * @param parameters Log parameter list
   */
  public <T> void log(Class<T> logClass, Level level, String message, Object... parameters) {
    Logger logger = getLogger(logClass);
    if (!level.equals(Level.DEBUG) || logger.isDebugEnabled()) {
      logger.log(level, generateMessage(message, parameters));
    }
  }

  /**
   * Log a message with a parameter list and throwable
   *
   * @param logClass   Name of the class that generates the message
   * @param level      Log level
   * @param message    Log message
   * @param parameters Log parameter list
   * @param cause      Throwable Cause exception
   */
  public <T> void log(Class<T> logClass, Level level, String message, Throwable cause, Object... parameters) {
    Logger logger = getLogger(logClass);
    if (!level.equals(Level.DEBUG) || logger.isDebugEnabled()) {
      logger.log(level, generateMessage(message, parameters), cause);
    }
  }

  /**
   * Generate message
   *
   * @param message Message string
   * @return Awe Message
   */
  private AweMessage generateMessage(String message) {
    return generateMessage(message, null);
  }

  /**
   * Generate message
   *
   * @param message    Message string
   * @param parameters Message parameters
   * @return Awe Message
   */
  private AweMessage generateMessage(String message, final Object[] parameters) {
    return generateMessageWithDatabase(message, parameters, null);
  }

  /**
   * Generate message with database
   *
   * @param message    Message string
   * @param parameters Message parameters
   * @return Awe Message
   */
  private AweMessage generateMessageWithDatabase(String message, final Object[] parameters, String database) {
    return new AweMessage(message, parameters).setDatabase(getDatabase(database))
      .setScreen(getScreen())
      .setUser(getUser());
  }

  /**
   * Retrieve session user
   *
   * @return Session user
   */
  private String getUser() {
    try {
      return getSession().isAuthenticated() ? getSession().getUser() : null;
    } catch (Exception exc) {
      return null;
    }
  }

  /**
   * Retrieve session screen
   *
   * @return Session screen
   */
  private String getScreen() {
    try {
      return getSession().isAuthenticated() ? getSession().getParameter(String.class, AweConstants.SESSION_CURRENT_SCREEN) : null;
    } catch (Exception exc) {
      return null;
    }
  }

  /**
   * Retrieve session database
   *
   * @param definedDatabase Specific defined database
   * @return Session database
   */
  private String getDatabase(String definedDatabase) {
    if (definedDatabase != null) {
      return definedDatabase;
    }

    try {
      return getSession().isAuthenticated() ? getSession().getParameter(String.class, AweConstants.SESSION_DATABASE) : defaultDatasource;
    } catch (Exception exc) {
      return defaultDatasource;
    }
  }

  /**
   * Retrieve current session
   *
   * @return Session
   */
  private AweSession getSession() {
    return context.getBean(AweSession.class);
  }

  /**
   * Get logger Object to log If user logger is active return his logger else
   * return logger of className
   *
   * @return Logger
   */
  private <T> Logger getLogger(Class<T> loggerClass) {
    Logger loggerInstance = LogManager.getLogger(loggerClass);

    // User logger
    String userName = getUser();

    // User logger
    if (userName != null && !userName.isEmpty() && logUsersEnabled) {
      // Put key user for routing
      ThreadContext.put(AweConstants.SESSION_USER, userName);
    } else {
      ThreadContext.remove(AweConstants.SESSION_USER);
    }

    // Return rootLogger if userLogger and classLogger not exist
    return (loggerInstance == null) ? LogManager.getRootLogger() : loggerInstance;
  }

  /**
   * Generate a time lapse
   *
   * @return Time lapse
   */
  public List<Long> prepareTimeLapse() {
    List<Long> timeLapse = new ArrayList<>();
    checkpoint(timeLapse);
    return timeLapse;
  }

  /**
   * Store a checkpoint in the time lapse
   *
   * @param timeLapse Time lapse
   */
  public void checkpoint(List<Long> timeLapse) {
    timeLapse.add(System.currentTimeMillis());
  }

  /**
   * Return the elapsed time between two code points
   *
   * @param timeLapse  Time lapse
   * @param startPoint start point
   * @param endPoint   end point
   * @return Elapsed time
   */
  public double getElapsed(List<Long> timeLapse, int startPoint, int endPoint) {
    long startTime = timeLapse.get(startPoint);
    long endTime = timeLapse.get(endPoint);
    return ((double) (endTime - startTime)) / 1000;
  }

  /**
   * Return the elapsed time in a point
   *
   * @param timeLapse Time lapse
   * @param point     point to retrieve (starting from 1)
   * @return Elapsed time
   */
  public double getElapsed(List<Long> timeLapse, int point) {
    return getElapsed(timeLapse, point - 1, point);
  }


  /**
   * Return the total elapsed time
   *
   * @param timeLapse Time lapse
   * @return Elapsed time
   */
  public double getTotalTime(List<Long> timeLapse) {
    return getElapsed(timeLapse, 0, timeLapse.size() - 1);
  }
}
