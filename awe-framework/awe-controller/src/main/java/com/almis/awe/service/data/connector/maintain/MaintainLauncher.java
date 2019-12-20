package com.almis.awe.service.data.connector.maintain;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.maintain.Email;
import com.almis.awe.model.entities.maintain.MaintainQuery;
import com.almis.awe.model.entities.maintain.Queue;
import com.almis.awe.model.entities.maintain.Serve;
import com.almis.awe.model.entities.queries.DatabaseConnection;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;

import javax.cache.annotation.CacheRemoveAll;

/**
 * MaintainLauncher Class
 * Factory class of interface IMaintain
 *
 * @author Jorge BELLON 24-02-2017
 */
public class MaintainLauncher extends ServiceConfig {

  /**
   * Launches a maintain with a connection
   *
   * @param maintain   Maintain query
   * @param connection Connection
   * @return serviceData Maintain output
   * @throws AWException Error launching maintain
   */
  @CacheRemoveAll(cacheName = "queryData")
  public ServiceData launchMaintain(MaintainQuery maintain, DatabaseConnection connection, ObjectNode parameters) throws AWException {
    MaintainConnector maintainLauncher;
    try {
      maintainLauncher = getMaintainConnector(maintain);
    } catch (AWException exc) {
      getLogger().log(MaintainLauncher.class, Level.ERROR, exc.getMessage(), exc);
      return new ServiceData();
    }
    return maintainLauncher.launch(maintain, connection, parameters);
  }

  /**
   * Get connector to maintain
   *
   * @param maintain Maintain query
   * @return IMaintain
   */

  private MaintainConnector getMaintainConnector(MaintainQuery maintain) throws AWException {


    MaintainConnector connector;

    if (maintain instanceof Serve) {
      connector = getBean(ServiceMaintainConnector.class);
    } else if (maintain instanceof Queue) {
      try {
        connector = getBean(QueueMaintainConnector.class);
      } catch (Exception exc) {
        throw new AWException("Queue maintain connector not found. Perhaps JMS is not activated?", exc);
      }
    } else if (maintain instanceof Email) {
      try {
        connector = getBean(EmailMaintainConnector.class);
      } catch (Exception exc) {
        throw new AWException("Email maintain connector not found. Perhaps MAIL is not activated?", exc);
      }
    } else {
      try {
        connector = getBean(SQLMaintainConnector.class);
      } catch (Exception exc) {
        throw new AWException("SQL maintain connector not found. Perhaps SQL DATABASE is not activated?", exc);
      }
    }

    // Set maintain query
    return connector;
  }

}
