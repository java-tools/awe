package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pgarcia on 12/06/2017.
 */
public class PropertyService extends ServiceConfig {

  @Value("${awe.database.enabled:false}")
  private boolean databaseEnabled;

  // Autowired services
  private QueryService queryService;
  private ConfigurableEnvironment environment;

  /**
   * Autowired constructor
   * @param queryService Query service
   * @param configurableEnvironment Configurable environment
   */
  @Autowired
  public PropertyService(QueryService queryService, ConfigurableEnvironment configurableEnvironment) {
    this.queryService = queryService;
    this.environment = configurableEnvironment;
  }

  /**
   * Generate application properties
   * @return Service data
   */
  ServiceData refreshDatabaseProperties() {
    ServiceData serviceData = new ServiceData();

    // Retrieve properties from database if database is enabled
    if (databaseEnabled) {
      Map aweDatabaseProperties = new HashMap();
      String propertyName = "";
      try {
        getLogger().log(PropertyService.class, Level.INFO, "===== Loading database properties =====");

        // Retrieve application properties
        serviceData = queryService.launchPrivateQuery(AweConstants.APPLICATION_PARAMETERS_QUERY);
        DataList dataList = serviceData.getDataList();
        List<Map<String, CellData>> applicationPropertyList = dataList.getRows();

        // Store them into a properties object
        for (Map<String, CellData> property : applicationPropertyList) {
          // Store application property into environment
          propertyName = property.get(AweConstants.PARAMETER_NAME).getStringValue();
          String propertyValue = property.get(AweConstants.PARAMETER_VALUE).getStringValue();
          aweDatabaseProperties.put(propertyName, propertyValue);
        }
        // Add properties to system
      } catch (AWException exc) {
        getLogger().log(AweElements.class, Level.ERROR, "Error generating database property: {0}", exc, propertyName);
      }

      // Update environment sources
      MutablePropertySources propertySources = environment.getPropertySources();
      MapPropertySource propertySource = new MapPropertySource(AweConstants.AWE_DATABASE_PROPERTIES, aweDatabaseProperties);

      // Store property source
      if (propertySources.contains(AweConstants.AWE_DATABASE_PROPERTIES)) {
        propertySources.replace(AweConstants.AWE_DATABASE_PROPERTIES, propertySource);
      } else {
        propertySources.addFirst(propertySource);
      }
    }

    // Return service data for service calls
    return serviceData;
  }
}
