package com.almis.awe.service.data.connector.query;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.entities.queues.Queue;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.service.data.builder.QueueBuilder;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Map;

/**
 * QueueQueryConnector Class
 * Connection class between QueryLauncher and EnumBuilder
 *
 * @author Pablo GARCIA 25-07-2017
 */
public class QueueQueryConnector extends AbstractQueryConnector {

  /**
   * Autowired constructor
   *
   * @param queryUtil Query utilities
   */
  public QueueQueryConnector(QueryUtil queryUtil) {
    super(queryUtil);
  }

  @Override
  public ServiceData launch(Query query, ObjectNode parameters) throws AWException {

    // Log start query prepare time
    List<Long> timeLapse = getLogger().prepareTimeLapse();

    // Get query builder
    QueueBuilder builder = getBean(QueueBuilder.class);
    Queue queue = getElements().getQueue(query.getQueue()).copy();

    // Get query preparation time
    getLogger().checkpoint(timeLapse);

    // Query launch
    builder.setQueue(queue);
    ServiceData result = buildResults(builder, query);

    // Get elapsed query time
    getLogger().checkpoint(timeLapse);

    // Generate results
    Map<String, QueryParameter> variableMap = getQueryUtil().getVariableMap(query, parameters);
    result = generateResults(result, query, variableMap);

    // // Get elapsed datalist time
    getLogger().checkpoint(timeLapse);

    // Log query
    getLogger().log(QueueQueryConnector.class, Level.INFO, "[{0}] =>  {1} records. Prepare queue time: {2}s - Queue time: {3}s - Datalist time: {4}s - Total time: {5}s",
      query.getId(),
      result.getDataList().getRecords(),
      getLogger().getElapsed(timeLapse, AweConstants.PREPARATION_TIME),
      getLogger().getElapsed(timeLapse, AweConstants.EXECUTION_TIME),
      getLogger().getElapsed(timeLapse, AweConstants.RESULTS_TIME),
      getLogger().getTotalTime(timeLapse));
    return result;
  }

  @Override
  public ServiceData subscribe(Query query, ComponentAddress address, ObjectNode parameters) throws AWException {

    // Log start query prepare time
    List<Long> timeLapse = getLogger().prepareTimeLapse();

    // Get query builder
    QueueBuilder builder = getBean(QueueBuilder.class);
    Queue queue = getElements().getQueue(query.getQueue()).copy();

    // Get query preparation time
    getLogger().checkpoint(timeLapse);

    // Query launch
    Map<String, QueryParameter> variableMap = getQueryUtil().getVariableMap(query, parameters);
    ServiceData result = builder.setQuery(query)
      .setQueue(queue)
      .setAddress(address)
      .setParameters(parameters)
      .setVariables(variableMap)
      .subscribe();

    // // Get elapsed datalist time
    getLogger().checkpoint(timeLapse);

    // Log query
    getLogger().log(QueueQueryConnector.class, Level.INFO, "[{0}] =>  Prepare queue time: {1}s - Queue time: {2}s - Total time: {3}s",
      query.getId(),
      getLogger().getElapsed(timeLapse, AweConstants.PREPARATION_TIME),
      getLogger().getElapsed(timeLapse, AweConstants.EXECUTION_TIME),
      getLogger().getTotalTime(timeLapse));
    return result;
  }

  /**
   * Manage subscription data
   *
   * @param query            Query
   * @param subscriptionData Subscription data
   * @param parameterMap     Parameter map
   * @return Service data with client actions
   * @throws AWException Error managing subscription data
   */
  public ServiceData onSubscriptionData(Query query, ServiceData subscriptionData, Map<String, QueryParameter> parameterMap) throws AWException {

    // Log start query prepare time
    List<Long> timeLapse = getLogger().prepareTimeLapse();

    // Generate results
    ServiceData result = generateResults(subscriptionData, query, parameterMap);

    // // Get elapsed datalist time
    getLogger().checkpoint(timeLapse);

    // Log query
    getLogger().log(QueueQueryConnector.class, Level.INFO, "[{0}] => Subscription data retrieved - Datalist time: {1}s - Total time: {2}s",
      query.getId(),
      getLogger().getElapsed(timeLapse, AweConstants.PREPARATION_TIME),
      getLogger().getTotalTime(timeLapse));
    return result;
  }
}
