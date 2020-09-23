package com.almis.awe.listener;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.entities.queues.ResponseMessage;
import com.almis.awe.service.BroadcastService;
import com.almis.awe.service.QueryService;
import com.almis.awe.service.data.processor.QueueProcessor;
import org.apache.logging.log4j.Level;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Map;

/**
 * Queue Listener (application listener)
 * Class which manages receiving messages from topic queue
 *
 * @author Pablos - 20/NOV/2013
 */
public class QueueListener extends ServiceConfig implements MessageListener {

  // Autowired services
  private final QueryService queryService;
  private final BroadcastService broadcastService;

  private Query query;
  private ComponentAddress address;
  private ResponseMessage response;
  private Map<String, QueryParameter> parameterMap;

  /**
   * Autowired constructor
   *
   * @param queryService     query service
   * @param broadcastService broadcast service
   */
  public QueueListener(QueryService queryService, BroadcastService broadcastService) {
    this.queryService = queryService;
    this.broadcastService = broadcastService;
  }

  /**
   * Broadcast a message each time is called
   *
   * @param message Message
   */
  @Override
  public void onMessage(Message message) {
    try {
      // Generate data
      QueueProcessor processor = getBean(QueueProcessor.class);
      ServiceData serviceData = processor.parseResponseMessage(response, message);

      // If return is a DataList or Data, parse it with the query
      if (serviceData.getClientActionList() == null || serviceData.getClientActionList().isEmpty()) {
        serviceData = queryService.onSubscribeData(query, address, serviceData, parameterMap);
      } else {
        // Add address to client action list
        for (ClientAction action : serviceData.getClientActionList()) {
          action.setAddress(getAddress());
        }
      }

      // Broadcast data
      broadcastService.broadcastMessageToUID(address.getSession(), serviceData.getClientActionList());

      // Log sent message
      getLogger().log(QueueListener.class, Level.DEBUG,
        "New message received from subscription to address {0}. Content: {1}",
        getAddress().toString(), message.toString());
    } catch (AWException exc) {
      broadcastService.sendErrorToUID(address.getSession(), exc.getTitle(), exc.getMessage());

      // Log error
      getLogger().log(QueueListener.class, Level.ERROR, "Error in message from subscription to address {0}. Content: {1}", exc,
        getAddress().toString(), message.toString());
    } catch (Exception exc) {
      broadcastService.sendErrorToUID(address.getSession(), null, exc.getMessage());

      // Log error
      getLogger().log(QueueListener.class, Level.ERROR, "Error in message from subscription to address {0}. Content: {1}", exc,
        getAddress().toString(), message.toString());
    }
  }

  /**
   * @return the response
   */
  public ResponseMessage getResponse() {
    return response;
  }

  /**
   * @param response the response to set
   * @return this
   */
  public QueueListener setResponse(ResponseMessage response) {
    this.response = response;
    return this;
  }

  /**
   * Get component address
   *
   * @return Component address
   */
  public ComponentAddress getAddress() {
    return address;
  }

  /**
   * Set component address
   *
   * @param address Component address
   * @return this
   */
  public QueueListener setAddress(ComponentAddress address) {
    this.address = address;
    return this;
  }

  /**
   * Get query
   *
   * @return query
   */
  public Query getQuery() {
    return query;
  }

  /**
   * Set query
   *
   * @param query Query
   * @return this
   */
  public QueueListener setQuery(Query query) {
    this.query = query;
    return this;
  }

  /**
   * Get parameter map
   *
   * @return parameter map
   */
  public Map<String, QueryParameter> getParameterMap() {
    return parameterMap;
  }

  /**
   * Set parameter map
   *
   * @param parameterMap Parameter map
   * @return this
   */
  public QueueListener setParameterMap(Map<String, QueryParameter> parameterMap) {
    this.parameterMap = parameterMap;
    return this;
  }
}
