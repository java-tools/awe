package com.almis.awe.service.data.connector.query;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.service.data.builder.ServiceBuilder;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * EnumQueryConnector Class
 * Connection class between QueryLauncher and EnumBuilder
 *
 * @author Jorge BELLON 27-03-2017
 */
public class ServiceQueryConnector extends AbstractQueryConnector {

  /**
   * Autowired constructor
   *
   * @param queryUtil Query utilities
   */
  @Autowired
  public ServiceQueryConnector(QueryUtil queryUtil) {
    super(queryUtil);
  }

  @Override
  public ServiceData launch(Query query, ObjectNode parameters) throws AWException {

    // Log start query prepare time
    List<Long> timeLapse = getLogger().prepareTimeLapse();

    // Get query builder
    ServiceBuilder builder = getBean(ServiceBuilder.class);
    builder.setParameters(parameters);

    // Get query preparation time
    getLogger().checkpoint(timeLapse);

    // Query launch
    ServiceData result = buildResults(builder, query);

    // Get elapsed query time
    getLogger().checkpoint(timeLapse);

    // Process and generate results
    if (query.isPostProcessed()) {
      Map<String, QueryParameter> variableMap = getQueryUtil().getVariableMap(query, parameters);
      result = generateResults(result, query, variableMap);
    }

    // Get elapsed datalist time
    getLogger().checkpoint(timeLapse);

    // Log query
    getLogger().log(ServiceQueryConnector.class, Level.INFO, "[{0}] =>  {1} records. Prepare service time: {2}s - Service time: {3}s - Datalist time: {4}s - Total time: {5}s",
      query.getService(),
      result.getDataList().getRecords(),
      getLogger().getElapsed(timeLapse, AweConstants.PREPARATION_TIME),
      getLogger().getElapsed(timeLapse, AweConstants.EXECUTION_TIME),
      getLogger().getElapsed(timeLapse, AweConstants.RESULTS_TIME),
      getLogger().getTotalTime(timeLapse));
    return result;
  }

  @Override
  public ServiceData subscribe(Query query, ComponentAddress address, ObjectNode parameters) throws AWException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
