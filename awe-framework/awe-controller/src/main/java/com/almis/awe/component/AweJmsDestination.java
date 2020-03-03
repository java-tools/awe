/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.queues.JmsDestination;
import com.almis.awe.model.type.JmsConnectionType;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import com.almis.awe.service.QueryService;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * JMS Destination list
 * @author pgarcia
 */
public class AweJmsDestination {

  // Autowired services
  private AweElements elements;
  private LogUtil logger;
  private QueryService queryService;
  private Map<String, JmsDestination> destinationMap;

  /**
   * Autowired constructor
   * @param elements Awe Elements
   * @param logger Logger
   * @param queryService Query service
   */
  @Autowired
  public AweJmsDestination(AweElements elements, LogUtil logger, QueryService queryService) {
    this.elements = elements;
    this.logger  = logger;
    this.queryService = queryService;

    // Load sources
    loadJmsSources();
  }

  /**
   * Load initially all jms sources
   */
  private void loadJmsSources() {
    destinationMap = new HashMap<>();
    try {
      ServiceData jmsConnections = queryService.launchQuery(AweConstants.JMS_CONNECTIONS_QUERY);

      if (jmsConnections.getDataList() != null) {
        // Retrieve queue info
        for (Map<String, CellData> connection : jmsConnections.getDataList().getRows()) {

          String alias = connection.get("Als").getStringValue();
          JmsConnectionType connectionType = JmsConnectionType.valueOf(connection.get("ConTyp").getStringValue());
          boolean isTopic = connection.get("QueTyp").getStringValue().equalsIgnoreCase("TOPIC");

          JmsDestination destination = new JmsDestination()
                  .setAlias(alias)
                  .setConnectionType(connectionType)
                  .setBroker(connection.get("JmsBrk").getStringValue())
                  .setUsername(connection.get("JmsUsr").getStringValue())
                  .setPassword(EncodeUtil.decryptRipEmd160(connection.get("JmsPwd").getStringValue()))
                  .setTopic(isTopic)
                  .setDestination(connection.get("DstNam").getStringValue());

          destinationMap.put(alias, destination);
        }
      }
    } catch (AWException exc) {
      logger.log(AweJmsDestination.class, Level.ERROR, "Error getting queue connections info.", exc);
    }
  }

  /**
   * Retrieve topic list
   * @return Topic list
   */
  public List<String> getTopicList() {
    List<String> topicList = new ArrayList<>();
    for (Entry<String, JmsDestination> connection : destinationMap.entrySet()) {
      if (connection.getValue().isTopic()) {
        topicList.add(connection.getKey());
      }
    }
    return topicList;
  }

  /**
   * Retrieve JMS destination for a queue or a topic
   * @param alias JMS destination alias
   * @return Destination
   * @throws AWException Error retrieving destination
   */
  public Destination getDestination(String alias) throws AWException {
    return getDestination(destinationMap.get(alias));
  }

  /**
   * Retrieve JMS destination for a queue or a topic
   * @param destinationInfo JMS Destination information
   * @return Destination
   */
  private Destination getDestination(JmsDestination destinationInfo) throws AWException {
    Destination destination = null;

    try {
      // Find and retrieve connection
      switch (destinationInfo.getConnectionType()) {
        case J: // JNDI
        default:
          destination = getDestinationWithJNDI(destinationInfo);
          break;
      }
    } catch (AWException exc) {
      throw exc;
    } catch (Exception exc) {
      throw new AWException(elements.getLocaleWithLanguage("ERROR_TITLE_DESTINATION_FAILED", elements.getLanguage()),
              elements.getLocaleWithLanguage("ERROR_MESSAGE_DESTINATION_FAILED", elements.getLanguage(), destinationInfo.getDestination()), exc);
    }

    return destination;
  }

  /**
   * Create Jms destination with server resources
   *
   * @param destinationInfo Queue
   * @return Connection
   * @throws AWException Error retrieving destination
   */
  private Destination getDestinationWithJNDI(JmsDestination destinationInfo) throws AWException {

    // Variable definition
    Destination destination = null;
    try {
      // Define initial context
      Context ctx = new InitialContext();
      Context envContext = (Context) ctx.lookup("java:/comp/env");

      // Get destination
      destination = (Destination) envContext.lookup(destinationInfo.getDestination());
    } catch (Exception exc) {
      throw new AWException(elements.getLocaleWithLanguage("ERROR_TITLE_QUEUE_DESTINATION_NOT_FOUND", elements.getLanguage()),
              elements.getLocaleWithLanguage("ERROR_MESSAGE_QUEUE_DESTINATION_NOT_FOUND", elements.getLanguage(), destinationInfo.getDestination()), exc);
    }

    return destination;
  }
}
