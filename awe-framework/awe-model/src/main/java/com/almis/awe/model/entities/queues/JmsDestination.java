package com.almis.awe.model.entities.queues;

import com.almis.awe.model.type.JmsConnectionType;

/**
 * JmsMessage Class
 *
 * @author Pablo GARCIA - 31/OCT/2013
 */
public class JmsDestination {

  private String alias = null;
  private String broker = null;
  private boolean topic = false;
  private String destination = null;
  private JmsConnectionType connectionType = null;
  private String username = null;
  private String password = null;


  /**
   * JMS Alias
   *
   * @return Alias
   */
  public String getAlias() {
    return alias;
  }

  /**
   * JMS Alias
   *
   * @param alias Alias
   * @return this
   */
  public JmsDestination setAlias(String alias) {
    this.alias = alias;
    return this;
  }

  /**
   * JMS Broker
   *
   * @return Broker
   */
  public String getBroker() {
    return broker;
  }

  /**
   * JMS Broker
   *
   * @param broker Broker
   * @return this
   */
  public JmsDestination setBroker(String broker) {
    this.broker = broker;
    return this;
  }

  /**
   * Is topic
   *
   * @return Is topic
   */
  public boolean isTopic() {
    return topic;
  }

  /**
   * Set topic
   *
   * @param topic Topic
   * @return this
   */
  public JmsDestination setTopic(boolean topic) {
    this.topic = topic;
    return this;
  }

  /**
   * JMS Physical destination
   *
   * @return Destination
   */
  public String getDestination() {
    return destination;
  }

  /**
   * JMS Physical destination
   *
   * @param destination Destination
   * @return this
   */
  public JmsDestination setDestination(String destination) {
    this.destination = destination;
    return this;
  }

  /**
   * Connection type
   *
   * @return Connection type
   */
  public JmsConnectionType getConnectionType() {
    return connectionType;
  }

  /**
   * Set connection type
   *
   * @param connectionType Connection type
   * @return this
   */
  public JmsDestination setConnectionType(JmsConnectionType connectionType) {
    this.connectionType = connectionType;
    return this;
  }

  /**
   * JMS username
   *
   * @return Username
   */
  public String getUsername() {
    return username;
  }

  /**
   * JMS username
   *
   * @param username Username
   * @return this
   */
  public JmsDestination setUsername(String username) {
    this.username = username;
    return this;
  }

  /**
   * JMS password
   *
   * @return Password
   */
  public String getPassword() {
    return password;
  }

  /**
   * JMS password
   *
   * @param password Password
   * @return this
   */
  public JmsDestination setPassword(String password) {
    this.password = password;
    return this;
  }
}
