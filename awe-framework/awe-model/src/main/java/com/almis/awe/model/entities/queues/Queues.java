package com.almis.awe.model.entities.queues;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.Collections;
import java.util.List;

/*
 * File Imports
 */

/**
 * Queues Class
 *
 * Used to parse the tag 'queues' in file Queues.xml with XStream
 * This file contains the list of application queues
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@XStreamAlias("queues")
public class Queues extends XMLWrapper {

  private static final long serialVersionUID = -1137206257271832250L;
  // Queue list
  @XStreamImplicit(itemFieldName = "queue")
  private List<Queue> queueList;

  /**
   * Default constructor
   */
  public Queues() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Queues(Queues other) throws AWException {
    super(other);
    this.queueList = ListUtil.copyList(other.queueList);
  }

  /**
   * Returns a queue
   *
   * @param ide queue identifier
   * @return Selected queue
   */
  public Queue getQueue(String ide) {
    // Variable definition
    for (Queue queue : this.getBaseElementList()) {
      if (ide.equals(queue.getId())) {
        return queue;
      }
    }
    return null;
  }

  /**
   * Returns the service list
   *
   * @return Queue list
   */
  public List<Queue> getQueueList() {
    return queueList;
  }

  /**
   * Stores the service list
   *
   * @param queues Queue list
   */
  public void setQueueList(List<Queue> queues) {
    this.queueList = queues;
  }

  @Override
  public List<Queue> getBaseElementList() {
    return queueList == null ? Collections.emptyList() : queueList;
  }
}
