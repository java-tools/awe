package com.almis.awe.model.entities.queues;

import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.tracker.AweTrackable;
import com.almis.awe.model.tracker.AweTracker;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 * JmsConnectionInfo Class
 * Bean class with message queue connection info
 *
 * @author Pablo GARCIA - 03/Aug/2017
 */
@Data
@Accessors(chain = true)
@Log4j2
public class JmsConnectionInfo implements AweTrackable {

  private DefaultMessageListenerContainer listenerContainer;
  private ComponentAddress address;

  /**
   * Checks if connection must be closed
   *
   * @param observable Observable
   * @param value      Observable value
   */
  @Override
  public void update(AweTracker observable, Object value) {
    // Get last screen
    String lastScreen = (String) value;

    // Check observable
    if (!lastScreen.equalsIgnoreCase(getAddress().getScreen())) {
      observable.untrack(this);
      destroy();
    }
  }

  /**
   * On destroy
   */
  public void destroy() {
    getListenerContainer().shutdown();
    log.debug("JMS Connection finished for {}", getAddress().toString());
  }
}
