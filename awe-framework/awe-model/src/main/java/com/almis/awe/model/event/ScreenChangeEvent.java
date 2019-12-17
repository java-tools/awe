package com.almis.awe.model.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * Screen change event
 */
@Getter
@Setter
public class ScreenChangeEvent extends ApplicationEvent {

  private String connection;
  private String user;
  private String screen;

  /**
   * Constructor
   *
   * @param caller
   * @param connection
   * @param screen
   */
  public ScreenChangeEvent(Object caller, String user, String connection, String screen) {
    super(caller);
    this.user = user;
    this.connection = connection;
    this.screen = screen;
  }
}
