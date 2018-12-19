package com.almis.awe.exception;

import com.almis.awe.model.type.AnswerType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * File Imports
 */

/**
 * Specific AWE Exception class
 * Formatted exception to show a title and a description of the thrown ERROR
 *
 * @author Pablo GARCIA - 24/JUN/2010
 */
public class AWEQueryException extends AWException {

  private static final long serialVersionUID = -764683322805477265L;
  // Exception query
  private final String query;
  private static final Logger logger = LogManager.getLogger(AWEQueryException.class);

  /**
   * Constructs an instance of <code>AWException</code> with the specified detail message and cause exception.
   *
   * @param title   Exception title
   * @param message Detail message
   * @param query   Query launched
   * @param cause   Cause exception
   */
  public AWEQueryException(String title, String message, String query, Throwable cause) {
    super(title, message, cause);
    this.query = query;
  }

  @Override
  public AWException log() {
    // Log ERROR
    String errorType = getType() == AnswerType.ERROR ? "ERROR" : "WARNING";
    Level errorLevel = getType() == AnswerType.ERROR ? Level.ERROR : Level.WARN;

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


    // Log query
    if (query != null) {
      exceptionBuilder.append("[")
        .append(errorType)
        .append("] [Query]: ")
        .append("[")
        .append(query)
        .append("]\n");
    }

    // Log details
    exceptionBuilder.append("[")
      .append(errorType)
      .append("] [StackTrace]");
    logger.log(errorLevel, exceptionBuilder.toString(), getCause());
    return this;
  }
}
