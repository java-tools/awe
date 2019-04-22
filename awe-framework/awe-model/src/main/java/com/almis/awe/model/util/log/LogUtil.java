package com.almis.awe.model.util.log;


import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.messages.AweMessage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

/**
 * LogUtil Class
 * Returns an instance of logger.
 *
 * @author Pablo VIDAL - 11/Sep/2018
 */
public class LogUtil {

  // Log management
  private LoggerContext loggerContext;
  private Configuration logConfiguration;
  private Logger loggerUtil;
  private ApplicationContext context;

  @Value("${application.log.users.level:info}")
  private String defaultLogLevel;

  @Value("${application.log.users.enabled:false}")
  private Boolean logUsersEnabled;

  @Value("${spring.datasource.jndi-name:}")
  private String defaultDatasource;

  /**
   * Autowired constructor
   *
   * @param context Application context
   */
  @Autowired
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
  public void log(Class logClass, Level level, String message) {
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
  public void log(Class logClass, Level level, String message, Throwable cause) {
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
  public void log(Class logClass, Level level, String message, EventObject parameter) {
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
  public void log(Class logClass, Level level, String message, Object parameter) {
    Logger logger = getLogger(logClass);
    if (!level.equals(Level.DEBUG) || logger.isDebugEnabled()) {
      logger.log(level, generateMessage(message, new Object[]{parameter}));
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
  public void log(Class logClass, Level level, String message, Throwable cause, Object parameter) {
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
  public void log(Class logClass, Level level, String message, Object... parameters) {
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
  public void log(Class logClass, Level level, String message, Throwable cause, Object... parameters) {
    Logger logger = getLogger(logClass);
    if (!level.equals(Level.DEBUG) || logger.isDebugEnabled()) {
      logger.log(level, generateMessage(message, parameters), cause);
    }
  }

  /**
   * Change level to logger
   *
   * @param logName  Log name
   * @param logLevel Log level
   */
  public void changeLogLevel(String logName, String logLevel) {
    String level = logLevel;
    LoggerConfig loggerConfig;
    try {
      if (logName != null && !logName.isEmpty()) {
        loggerConfig = ((XmlConfiguration) logConfiguration).getLogger(logName);
      } else {
        // Change root logger
        loggerConfig = logConfiguration.getRootLogger();
      }

      // Set level
      level = setLoggerConfig(logLevel, loggerConfig);

      // Update loggers
      loggerContext.updateLoggers();

    } catch (Exception exc) {
      loggerUtil.log(Level.ERROR, "Error changing log level in Logger {0} to {1} level", new Object[]{logName, level}, exc);
    }
  }

  /**
   * Set logger config
   *
   * @param level        Level to put
   * @param loggerConfig Logger configuration
   * @return Final level
   */
  private String setLoggerConfig(String level, LoggerConfig loggerConfig) {
    String finalLevel = level;
    switch (level) {
      case "ERROR":
      case "WARN":
      case "DEBUG":
      case "TRACE":
      case "FATAL":
      case "INFO":
        loggerConfig.setLevel(Level.toLevel(level));
        break;
      default:
        // Default level
        finalLevel = defaultLogLevel;
        loggerConfig.setLevel(Level.toLevel(finalLevel));
        break;
    }
    return finalLevel;
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
    AweMessage aweMessage = new AweMessage(message, parameters);
    try {
      String database = getSession().getParameter(String.class, AweConstants.SESSION_DATABASE);
      database = database == null ? defaultDatasource : database;
      aweMessage.setDatabase(database)
        .setScreen(getSession().getParameter(String.class, AweConstants.SESSION_CURRENT_SCREEN))
        .setUser(getUser());
    } catch (Exception exc) {
      // Do nothing
    }
    return aweMessage;
  }

  /**
   * Retrieve session user
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
  private Logger getLogger(Class loggerClass) {
    Logger loggerInstance = LogManager.getLogger(loggerClass);

    // User logger
    String userName = getUser();

    // User logger
    if (userName != null && !userName.isEmpty() && logUsersEnabled) {
      // Put key user for routing
      ThreadContext.put(AweConstants.SESSION_USER, userName);
    }

    // Return rootLogger if userLogger and classLogger not exist
    return (loggerInstance == null) ? LogManager.getRootLogger() : loggerInstance;
  }

  /**
   * Check if logger exists
   *
   * @param logName logger name
   * @return true if logger exist else false
   */
  public boolean loggerExists(String logName) {
    Map<String, LoggerConfig> mapLoggers = logConfiguration.getLoggers();
    // List loggers
    for (Map.Entry<String, LoggerConfig> entry : mapLoggers.entrySet()) {
      String loggerName = entry.getKey();
      if (loggerUtil.isDebugEnabled()) {
        loggerUtil.log(Level.DEBUG, loggerName);
      }
    }
    return mapLoggers.containsKey(logName);
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
