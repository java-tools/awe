package com.almis.awe.service.data.connector.query;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.queries.Query;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;
import org.springframework.cache.annotation.Cacheable;

/**
 * QueryLauncher Class
 * Factory class of interface QueryConnector
 *
 * @author Jorge BELLON 24-02-2017
 */
public class QueryLauncher extends ServiceConfig {

  /**
   * Launches a query
   * 
   * @param query Query
   * @param parameters Parameters
   * @return Query data
   * @throws AWException Query has failed
   */
  @Cacheable(value = "queryData", condition = "#query.isCacheable()", key = "{ #query.getId(), #parameters.toString() }")
  public ServiceData launchQuery(Query query, ObjectNode parameters) throws AWException {
    QueryConnector queryLauncher;
    try {
      queryLauncher = getQueryConnector(query);
    } catch (AWException exc) {
      getLogger().log(QueryLauncher.class, Level.ERROR, exc.getMessage(), exc);
      return new ServiceData().setDataList(new DataList());
    }
    return queryLauncher.launch(query, parameters);
  }

  /**
   * Subscribe to a query
   *
   * @param query Query
   * @param address Component address
   * @param parameters Parameters
   * @return Query data
   * @throws AWException Query has failed
   */
  public ServiceData subscribe(Query query, ComponentAddress address, ObjectNode parameters) throws AWException {
    QueryConnector queryLauncher;
    try {
      queryLauncher = getQueryConnector(query);
    } catch (AWException exc) {
      getLogger().log(QueryLauncher.class, Level.ERROR, "Query connector not found. Perhaps database is not activated?", exc);
      return new ServiceData().setDataList(new DataList());
    }
    return queryLauncher.subscribe(query, address, parameters);
  }

  /**
   * Get connector to query
   *
   * @param query Query
   * @return QueryConnector
   */
  private QueryConnector getQueryConnector(Query query) throws AWException {

      QueryConnector connector = null;

      if (query.getService() != null) {
        connector = getBean(ServiceQueryConnector.class);
      } else if (query.getEnumerated() != null) {
        connector = getBean(EnumQueryConnector.class);
      } else if (query.getQueue() != null) {
        try {
          connector = getBean(QueueQueryConnector.class);
        } catch (Exception exc) {
          throw new AWException("Queue query connector not found. Perhaps JMS is not activated?", exc);
        }
      } else {
        try {
          connector = getBean(SQLQueryConnector.class);
        } catch (Exception exc) {
          throw new AWException("SQL query connector not found. Perhaps SQL DATABASE is not activated?", exc);
        }
      }

      return connector;
  }

}
