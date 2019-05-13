package com.almis.awe.model.entities.queues;

import com.almis.awe.model.type.JmsConnectionType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * JmsMessage Class
 *
 * @author Pablo GARCIA - 31/OCT/2013
 */
@Data
@Accessors(chain = true)
public class JmsDestination {

  private String alias = null;
  private String broker = null;
  private boolean topic = false;
  private String destination = null;
  private JmsConnectionType connectionType = null;
  private String username = null;
  private String password = null;
}
