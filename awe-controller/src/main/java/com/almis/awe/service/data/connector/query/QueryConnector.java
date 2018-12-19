package com.almis.awe.service.data.connector.query;

import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.queries.Query;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Interface to launch query processes
 * 
 * @author pvidal
 */
public interface QueryConnector {

  /**
   * Launch a full query
   * 
   * @param query Query to be launched
   * @param parameters Parameters
   * @return ServiceData with complete output
   * @throws AWException Error launching query
   */
  ServiceData launch(Query query, ObjectNode parameters) throws AWException;

  /**
   * Subscribe to a queue
   * 
   * @param query Query to be subscribed to
   * @param address Address of the subscribed component
   * @param parameters Parameters
   * @return Service data with execution info
   * @throws AWException Error on subscription
   */
  ServiceData subscribe(Query query, ComponentAddress address, ObjectNode parameters) throws AWException;
}
