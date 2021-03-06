package com.almis.awe.service.data.connector.maintain;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWEQueryException;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.maintain.MaintainQuery;
import com.almis.awe.model.entities.queries.DatabaseConnection;
import com.almis.awe.service.data.builder.ServiceBuilder;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;

import java.util.List;

/**
 * Maintain connector for services
 */
public class ServiceMaintainConnector extends ServiceConfig implements MaintainConnector {

  @Override
  public <T extends MaintainQuery> ServiceData launch(T query, DatabaseConnection connection, ObjectNode parameters) throws AWException {

    // Log start query prepare time
    List<Long> timeLapse = getLogger().prepareTimeLapse();

    // Get query builder
    ServiceBuilder builder = getBean(ServiceBuilder.class);

    // Prepare variables
    builder
      .setQuery(query)
      .setParameters(parameters)
      .getVariables();

    // Get query preparation time
    getLogger().checkpoint(timeLapse);

    ServiceData result;
    try {
      // Launch service
      result = builder.build();
      getLogger().checkpoint(timeLapse);

      // Log query
      getLogger().log(ServiceMaintainConnector.class, Level.INFO, "[{0}] => Prepare service time: {1}s - Service time: {2}s - Total time: {3}s",
        query.getService(),
        getLogger().getElapsed(timeLapse, AweConstants.PREPARATION_TIME),
        getLogger().getElapsed(timeLapse, AweConstants.EXECUTION_TIME),
        getLogger().getTotalTime(timeLapse));

    } catch (AWException exc) {
      throw exc;
    } catch (Exception exc) {
      throw new AWEQueryException(getLocale("ERROR_TITLE_LAUNCHING_MAINTAIN"), exc.getMessage(), query.getId(), exc);
    }
    return result;
  }
}
