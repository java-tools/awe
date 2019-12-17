package com.almis.awe.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;

/**
 * Routing datasource
 * @author pgarcia
 */
@Getter
@Setter
public class AweRoutingDataSource extends AbstractRoutingDataSource {

  // Autowired service
  private AweDatabaseContextHolder contextHolder;

  // Set datasources loaded as false
  private boolean loadedDatasources = false;

  /**
   * Autowired constructor
   * @param contextHolder Awe Database Context holder
   */
  public AweRoutingDataSource(AweDatabaseContextHolder contextHolder) {
    this.contextHolder = contextHolder;
  }

  @Override
  public void afterPropertiesSet() {
    // Set default target datasource
    setDefaultTargetDataSource(contextHolder.getDataSource());

    // Load datasources
    setTargetDataSources(new HashMap<>());

    // Call parent after properties set
    super.afterPropertiesSet();
  }

  /**
   * Load datasources from current connection
   */
  public void loadDataSources() {
    if (!loadedDatasources) {
      reloadDataSources();
    }
  }

  /**
   * Load datasources from current connection
   */
  public void reloadDataSources() {
    // Set loaded datasources to true
    loadedDatasources = true;

    // Redefine target datasources
    setTargetDataSources(contextHolder.getDataSources());

    // Call parent after properties set
    super.afterPropertiesSet();
  }

  /**
   * Get lookup key
   * @return Current database
   */
  @Override
  protected Object determineCurrentLookupKey() {
    return contextHolder.getCurrentDatabase();
  }
}
