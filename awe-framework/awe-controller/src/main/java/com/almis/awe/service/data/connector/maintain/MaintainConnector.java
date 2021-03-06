/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.almis.awe.service.data.connector.maintain;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.maintain.MaintainQuery;
import com.almis.awe.model.entities.queries.DatabaseConnection;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Maintain launcher interface
 *
 * @author jbellon
 */
public interface MaintainConnector {
  /**
   * Launches a maintain
   *
   * @param query      Query to launch
   * @param connection Connection
   * @return Service data with execution info
   * @throws AWException Error launching maintain
   */
  <T extends MaintainQuery> ServiceData launch(T query, DatabaseConnection connection, ObjectNode parameters) throws AWException;
}
