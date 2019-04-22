/*
 * Package definition
 */
package com.almis.awe.model.entities.queues;

import com.almis.awe.model.entities.actions.ComponentAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import java.util.Observable;
import java.util.Observer;

/**
 * JmsConnectionInfo Class
 *
 * Bean class with message queue connection info
 *
 *
 * @author Pablo GARCIA - 03/Aug/2017
 */
public class JmsConnectionInfo implements Observer {

  private DefaultMessageListenerContainer listenerContainer;
  private ComponentAddress address;
  private final Logger logger = LogManager.getLogger(this.getClass());

  /**
   * Get listener container
   *
   * @return Listener container
   */
  public DefaultMessageListenerContainer getListenerContainer() {
    return listenerContainer;
  }

  /**
   * Set listener container
   *
   * @param listenerContainer Listener container
   * @return this
   */
  public JmsConnectionInfo setListenerContainer(DefaultMessageListenerContainer listenerContainer) {
    this.listenerContainer = listenerContainer;
    return this;
  }

  /**
   * Get component address
   *
   * @return the address
   */
  public ComponentAddress getAddress() {
    return address;
  }

  /**
   * Set component address
   *
   * @param address the address to set
   * @return this
   */
  public JmsConnectionInfo setAddress(ComponentAddress address) {
    this.address = address;
    return this;
  }

  /**
   * Checks if connection must be closed
   *
   * @param observable Observable
   * @param value      Observable value
   */
  @Override
  public void update(Observable observable, Object value) {
    // Get last screen
    String lastScreen = (String) value;

    // Check observable
    if (!lastScreen.equalsIgnoreCase(getAddress().getScreen())) {
      observable.deleteObserver(this);
      destroy();
    }
  }

  /**
   * On destroy
   */
  public void destroy() {
    getListenerContainer().shutdown();
    logger.debug("JMS Connection finished for {0}", new Object[]{getAddress().toString()});
  }
}
