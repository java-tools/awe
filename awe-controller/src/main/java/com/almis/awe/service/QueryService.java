package com.almis.awe.service;

import com.almis.awe.component.AweRoutingDataSource;
import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.service.data.connector.query.EnumQueryConnector;
import com.almis.awe.service.data.connector.query.QueryLauncher;
import com.almis.awe.service.data.connector.query.QueueQueryConnector;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

/**
 * Provides methods to retrieve application data
 *
 * @author Pablo GARCIA
 */
public class QueryService extends ServiceConfig {

  // Autowired services
  private QueryLauncher queryLauncher;
  private QueryUtil queryUtil;

  /**
   * Autowired constructor
   * @param queryLauncher Query launcher
   * @param queryUtil Query utilities
   */
  @Autowired
  public QueryService(QueryLauncher queryLauncher, QueryUtil queryUtil) {
    this.queryLauncher = queryLauncher;
    this.queryUtil = queryUtil;
  }

  /**
   * Launches a query (must be defined in APP or AWE Queries.xml file) and generates the output Query comes defined in target-action variable
   * 
   * @return Query output
   * @throws AWException Query failed
   */
  public ServiceData launchQueryAction() throws AWException {
    return launchQuery(getRequest().getTargetAction());
  }

  /**
   * Update some criteria at once
   * 
   * @return Service data
   * @throws AWException Query failed
   */
  public ServiceData updateModelAction() throws AWException {
    return updateModel(getRequest().getTargetAction());
  }

  /**
   * Check if a query returns data or not
   * 
   * @return Service data
   * @throws AWException Query failed
   */
  public ServiceData checkUniqueAction() throws AWException {
    return checkUnique(getRequest().getTargetAction());
  }

  /**
   * Subscribe to a query
   * 
   * @return Service data
   * @throws AWException Subscription failed
   */
  public ServiceData subscribeAction() throws AWException {
    // Variable initialization
    String query = getRequest().getTargetAction();
    ObjectNode address = (ObjectNode) getRequest().getParameter(AweConstants.PARAMETER_ADDRESS);

    // Generate Component Address
    ComponentAddress componentAddress = new ComponentAddress(address);

    // Store session token into the address
    componentAddress.setSession(getRequest().getToken());

    // Subscribe to query
    return subscribe(query, componentAddress);
  }

  /**
   * Subscribe to a query
   * 
   * @param queryId Query identifier
   * @param address Component address
   * @return Service data
   * @throws AWException Error in subscription
   */
  public ServiceData subscribe(String queryId, ComponentAddress address) throws AWException {
    Query query = getQuery(queryId, true);

    // Subscribe to query
    return queryLauncher.subscribe(query, address, queryUtil.getParameters());
  }

  /**
   * Manage subscription results
   * 
   * @param query Query
   * @param address Component address
   * @param subscriptionData Subscription data
   * @param parameterMap Parameters
   * @return Service data
   * @throws AWException Error in subscription
   */
  public ServiceData onSubscribeData(Query query, ComponentAddress address, ServiceData subscriptionData, Map<String, QueryParameter> parameterMap) throws AWException {
    // Subscribe to query
    ServiceData serviceData = getBean(QueueQueryConnector.class).onSubscriptionData(query, subscriptionData, parameterMap);

    // Add a client action to 'fill' a criterion or a grid
    serviceData.addClientAction(new ClientAction("fill").setAddress(address).addParameter("data", new CellData(serviceData.getDataList())).setAsync(Boolean.TRUE.toString()));

    return serviceData;
  }

  /**
   * Launches a query (must be defined in APP or AWE Queries.xml file) and generates the output Query comes defined in target-action variable
   *
   * @param queryId Query identifier
   * @return Query output
   * @throws AWException Query failed
   */
  public ServiceData launchQuery(String queryId) throws AWException {
    return launchQuery(queryId, null, null, null, true);
  }

  /**
   * Launches a query (must be defined in APP or AWE Queries.xml file) and generates the output Query comes defined in target-action variable
   *
   * @param queryId Query identifier
   * @param alias   Database alias
   * @return Query output
   * @throws AWException Query failed
   */
  public ServiceData launchQuery(String queryId, String alias) throws AWException {
    return launchQuery(queryId, alias, null, null, true);
  }

  /**
   * Launches a query (must be defined in APP or AWE Queries.xml file) and generates the output Query comes defined in target-action variable
   *
   * @param queryId Query identifier
   * @param parameters Parameters
   * @return Query output
   * @throws AWException Query failed
   */
  public ServiceData launchQuery(String queryId, ObjectNode parameters) throws AWException {
    return launchQuery(queryId, queryUtil.getParameters(parameters), true);
  }

  /**
   * Launches a query (must be defined in APP or AWE Queries.xml file) and generates the output Query comes defined in target-action variable
   *
   * @param queryId Query identifier
   * @return Query output
   * @throws AWException Query failed
   */
  public ServiceData launchPrivateQuery(String queryId) throws AWException {
    return launchQuery(queryId, null, null, null, false);
  }

  /**
   * Launches a query (must be defined in APP or AWE Queries.xml file) and generates the output Query comes defined in target-action variable
   *
   * @param queryId Query identifier
   * @param alias Database alias
   * @return Query output
   * @throws AWException Query failed
   */
  public ServiceData launchPrivateQuery(String queryId, String alias) throws AWException {
    return launchQuery(queryId, alias, null, null, false);
  }

  /**
   * Launches a query
   *
   * @param queryId Query identifier
   * @param alias Query alias
   * @param forcedPage Page
   * @param forcedMax Elements per page
   * @param checkSession Check if session is available
   * @return Query output
   * @throws AWException Query failed
   */
  public ServiceData launchQuery(String queryId, String alias,  String forcedPage, String forcedMax, boolean checkSession) throws AWException {
    Query query = getQuery(queryId, checkSession);
    return launchQuery(query, alias, forcedPage, forcedMax);
  }

  /**
   * Launches a query
   *
   * @param query Query
   * @param alias Query alias
   * @param forcedPage Page
   * @param forcedMax Elements per page
   * @return Query output
   * @throws AWException Query failed
   */
  public ServiceData launchQuery(Query query, String alias, String forcedPage, String forcedMax) throws AWException {
    ServiceData out = queryLauncher.launchQuery(query, queryUtil.getParameters(alias, forcedPage, forcedMax));

    // Add variables
    addVariables(out);
    return out;
  }

  /**
   * Launches a query
   *
   * @param queryId Query identifier
   * @param parameters Parameters
   * @param checkSession Check if session is available
   * @return Query output
   * @throws AWException Query failed
   */
  public ServiceData launchQuery(String queryId, ObjectNode parameters, boolean checkSession) throws AWException {
    Query query = getQuery(queryId, checkSession);
    ServiceData out = queryLauncher.launchQuery(query, queryUtil.getParameters(parameters));

    // Add variables
    addVariables(out);
    return out;
  }

  /**
   * Launches a query
   *
   * @param queryId Query identifier
   * @param forcedPage Page
   * @param forcedMax Elements per page
   * @return Query output
   * @throws AWException Query failed
   */
  public ServiceData launchQuery(String queryId, String forcedPage, String forcedMax) throws AWException {
    return launchQuery(queryId, null, forcedPage, forcedMax, true);
  }

  /**
   * Launches an enumerated query
   * @param enumId Enumerated identifier
   * @return Query output
   * @throws AWException Query failed
   */
  @Cacheable(value = "queryEnum")
  public ServiceData launchEnumQuery(String enumId) throws AWException {
    return launchEnumQuery(enumId, null, null);
  }

  /**
   * Launches a private query
   * 
   * @param queryId Enumerated identifier
   * @param forcedPage Page
   * @param forcedMax Elements per page
   * @return Query output
   * @throws AWException Query failed
   */
  public ServiceData launchPrivateQuery(String queryId, String forcedPage, String forcedMax) throws AWException {
    return launchQuery(queryId, null, forcedPage, forcedMax, false);
  }

  /**
   * Launches a private query
   *
   * @param queryId Enumerated identifier
   * @param parameters Parameters
   * @return Query output
   * @throws AWException Query failed
   */
  public ServiceData launchPrivateQuery(String queryId, ObjectNode parameters) throws AWException {
    return launchQuery(queryId, queryUtil.getParameters(parameters), false);
  }

  /**
   * Launches an enumerated query
   * 
   * @param enumId Enumerated identifier
   * @param forcedPage Page
   * @param forcedMax Elements per page
   * @return Query output
   * @throws AWException Query failed
   */
  @Cacheable(value = "queryEnum")
  public ServiceData launchEnumQuery(String enumId, String forcedPage, String forcedMax) throws AWException {
    ServiceData out = getBean(EnumQueryConnector.class).launchEnum(enumId, queryUtil.getParameters(JsonNodeFactory.instance.objectNode(), null, forcedPage, forcedMax));

    // Add variables
    addVariables(out);
    return out;
  }

  /**
   * Update some criteria at once
   * 
   * @param queryId Query identifier
   * @return Service data
   * @throws AWException Query failed
   */
  public ServiceData updateModel(String queryId) throws AWException {
    ServiceData serviceData = launchQuery(queryId);

    // Get column list
    List<String> columnList = DataListUtil.getColumnList(serviceData.getDataList());

    // For each column, generate a client action
    for (String column : columnList) {
      if (!AweConstants.JSON_ID_PARAMETER.equals(column)) {
        ClientAction clientAction = new ClientAction("select");
        clientAction.addParameter("values", new CellData(DataListUtil.getColumn(serviceData.getDataList(), column)));
        clientAction.setTarget(column);
        clientAction.setAsync(Boolean.TRUE.toString());
        serviceData.addClientAction(clientAction);
      }
    }

    return serviceData;
  }

  /**
   * Check if a query returns data or not
   * 
   * @param queryId Query identifier
   * @return Service data
   * @throws AWException Query failed
   */
  public ServiceData checkUnique(String queryId) throws AWException {
    ServiceData serviceData = launchQuery(queryId);

    // If query has data, retrieve a validation error
    if (serviceData.getDataList().getRecords() > 0) {
      serviceData.setType(AnswerType.ERROR);
      serviceData.setTitle(getLocale("ERROR_TITLE_CRITERIA_VALIDATION"));
      serviceData.setMessage(getLocale("ERROR_MESSAGE_UNIQUE_CRITERIA"));
    }
    return serviceData;
  }

  /**
   * Add output variables
   * 
   * @param out Output
   */
  private void addVariables(ServiceData out) {
    DataList data = out.getDataList();
    out
      .addVariable(AweConstants.ACTION_DATA, new CellData(data))
      .addVariable(AweConstants.ACTION_ROWS, new CellData(data.getRows()))
      .addVariable(AweConstants.ACTION_MESSAGE_TYPE, new CellData(out.getType().toString()))
      .addVariable(AweConstants.ACTION_MESSAGE_TITLE, new CellData(out.getTitle()))
      .addVariable(AweConstants.ACTION_MESSAGE_DESCRIPTION, new CellData(out.getMessage()));
  }

  /**
   * Prepare a query object
   *
   * @param queryName query name
   * @param checkAvailable Check query security
   * @return Query object
   * @throws AWException Query retrieval failure
   */
  private Query getQuery(String queryName, boolean checkAvailable) throws AWException {
    // Variable definition
    Query query;
    try {
      // Get the query
      query = new Query(getElements().getQuery(queryName));

      // If query is private, check security
      if (checkAvailable && !query.isPublic() && !getSession().isAuthenticated()) {
        getLogger().log(QueryService.class, Level.WARN, "ERROR_MESSAGE_OUT_OF_SESSION");
        throw new AWException(getLocale("ERROR_TITLE_LAUNCHING_SQL_QUERY"), getLocale("ERROR_MESSAGE_OUT_OF_SESSION"));
      }
    } catch (AWException exc) {
      throw exc;
    } catch (Exception exc) {
      String datMsg = exc.getMessage() == null ? queryName : exc.toString() + " (" + queryName + ")";
      throw new AWException(getLocale("ERROR_TITLE_RETRIEVING_DATA"), datMsg, exc);
    }

    return query;
  }

  /**
   * Initialize datasource connections
   */
  public void initDatasourceConnections() {
    try {
      AweRoutingDataSource dataSource = getBean(AweRoutingDataSource.class);
      if (dataSource != null) {
        dataSource.loadDataSources();
      }
    } catch (Exception exc) {
      getLogger().log(QueryService.class, Level.INFO, "AweRoutingDataSource not found. Using default datasource");
    }
  }

  /**
   * Reload datasource connections
   */
  public void reloadDatasourceConnections() {
    try {
      AweRoutingDataSource dataSource = getBean(AweRoutingDataSource.class);
      if (dataSource != null) {
        dataSource.reloadDataSources();
      }
    } catch (Exception exc) {
      getLogger().log(QueryService.class, Level.INFO, "AweRoutingDataSource not found. Using default datasource");
    }
  }

  /**
   * Retrieve a label from an enumerated value
   * @param enumeratedId Enumerated id
   * @param value Enumerated value
   * @return Enumerated label
   */
  public String findLabel(String enumeratedId, String value) throws AWException {
    return getElements().getEnumerated(enumeratedId).findLabel(value);
  }
}
