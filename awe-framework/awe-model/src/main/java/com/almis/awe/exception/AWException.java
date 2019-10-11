/*
 * Package definition
 */
package com.almis.awe.exception;

import com.almis.awe.model.type.AnswerType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Specific AWE Exception class
 * Formatted exception to show a title and a description of the thrown ERROR
 *
 * @author Pablo GARCIA - 24/JUN/2010
 */
@JsonIgnoreProperties({"cause", "localizedMessage", "stackTrace", "suppressed", "variables"})
public class AWException extends Exception {

  private static final long serialVersionUID = 7702204335666608962L;
  private static final Logger logger = LogManager.getLogger(AWException.class);

  // Exception title
  private String title = "";

  // Exception type
  private AnswerType type = AnswerType.ERROR;

  /**
   * Creates a new instance of <code>AWException</code> without detail message.
   */
  public AWException() {
  }

  /**
   * Constructs an instance of <code>AWException</code> with the specified detail message.
   *
   * @param msg Detail message.
   */
  public AWException(String msg) {
    super(msg);
  }

  /**
   * Constructs an instance of <code>AWException</code> with the specified detail message.
   *
   * @param msg   Detail message.
   * @param cause Cause of exception
   */
  public AWException(String msg, Throwable cause) {
    super(msg, cause);
  }

  /**
   * Constructs an instance of <code>AWException</code> with the specified detail message.
   *
   * @param title   Exception title
   * @param message Detail message
   */
  public AWException(String title, String message) {
    super(message);
    setTitle(title);
  }

  /**
   * Constructs an instance of <code>AWException</code> with the specified detail message.
   *
   * @param tit Exception title
   * @param msg Detail message
   * @param typ Message type (ERROR, warning)
   */
  public AWException(String tit, String msg, AnswerType typ) {
    super(msg);
    setTitle(tit);
    setType(typ);
  }

  /**
   * Constructs an instance of <code>AWException</code> with the specified detail message and cause exception.
   *
   * @param tit   Exception title
   * @param msg   Detail message
   * @param cause Cause exception
   */
  public AWException(String tit, String msg, Throwable cause) {
    super(msg, cause);
    setTitle(tit);
  }

  /**
   * Returns the exception title
   *
   * @return Exception title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Stores the exception title
   *
   * @param title Exception title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the exception type
   *
   * @return Exception type
   */
  public AnswerType getType() {
    return type;
  }

  /**
   * Stores the exception type
   *
   * @param type Exception type
   */
  public void setType(AnswerType type) {
    this.type = type;
  }

  /**
   * Log exception error
   *
   * @return this
   */
  public AWException log() {
    // Log ERROR
    String errorType = this.getType() == AnswerType.ERROR ? "ERROR" : "WARNING";
    Level errorLevel = this.getType() == AnswerType.ERROR ? Level.ERROR : Level.WARN;

    // Start log
    StringBuilder exceptionBuilder = new StringBuilder();
    exceptionBuilder.append("\n================================================================================")
      .append("\n[")
      .append(errorType)
      .append("] [Message] ")
      .append(getTitle())
      .append(" (")
      .append(getMessage())
      .append(")\n");

    // Log details
    exceptionBuilder.append("[")
      .append(errorType)
      .append("] [StackTrace]");
    logger.log(errorLevel, exceptionBuilder, getCause());
    return this;
  }
}
