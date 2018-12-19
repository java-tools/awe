package com.almis.awe.service.data.builder;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.service.LauncherService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Generate service datalists
 */
public class ServiceBuilder extends AbstractQueryBuilder {

  // Autowired services
  private LauncherService launcherService;

  @Override
  public ServiceBuilder setQuery(Query query) {
    super.setQuery(query);
    return this;
  }

  /**
   * Autowired constructor
   * @param launcherService Launcher service
   */
  @Autowired
  public ServiceBuilder(LauncherService launcherService) {
    this.launcherService = launcherService;
  }

  /**
   * Call the service and returns the ServiceData
   * 
   * @return serviceData
   * @throws AWException Error calling service
   */
  public ServiceData build() throws AWException {
    // Call service
    if (getVariables() == null) {
      setVariables(queryUtil.getVariableMap(getQuery(), getParameters()));
    }
    ServiceData serviceData = launcherService.callService(getQuery().getService(), extractValuesFromParameters(getVariables()));
    if (serviceData != null) {
      return new ServiceData(serviceData);
    } else {
      return new ServiceData();
    }
  }
}
