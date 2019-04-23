/*
 * Package definition
 */
package com.almis.awe.exception;

/**
 * Specific AWE Session Exception class
 *
 * Formatted exception to show a title and a description of the thrown ERROR
 *
 *
 * @author Pablo GARCIA - 24/JUN/2010
 */
public class AWESessionException extends AWException {

  /**
   * Constructs an instance of <code>AWException</code> with the specified detail message.
   *
   * @param title Exception title
   * @param message Detail message
   */
  public AWESessionException(String title, String message) {
    super(message);
    setTitle(title);
  }
}
