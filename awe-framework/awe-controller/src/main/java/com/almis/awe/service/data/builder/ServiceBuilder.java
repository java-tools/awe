package com.almis.awe.service.data.builder;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.service.LauncherService;

/**
 * Generate service datalists
 */
public class ServiceBuilder extends AbstractQueryBuilder {

  // Autowired services
  private final LauncherService launcherService;

  @Override
  public ServiceBuilder setQuery(Query query) {
    super.setQuery(query);
    return this;
  }

  /**
   * Autowired constructor
   *
   * @param launcherService Launcher service
   * @param queryUtil       Query utilities
   */
  public ServiceBuilder(LauncherService launcherService, QueryUtil queryUtil) {
    super(queryUtil);
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
      return serviceData.copy();
    } else {
      return new ServiceData();
    }
  }
}
