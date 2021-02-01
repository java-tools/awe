package com.almis.awe.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.queries.DatabaseConnection;
import com.almis.awe.model.entities.queries.DatabaseConnectionInfo;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.service.QueryService;
import com.almis.awe.service.SessionService;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.StringValueResolver;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pgarcia
 */
public class AweDatabaseContextHolder implements EmbeddedValueResolverAware {

  private static final String ERROR_TITLE_INVALID_CONNECTION = "ERROR_TITLE_INVALID_CONNECTION";
  // Autowired services
  private final AweElements elements;
  private final QueryService queryService;
  private final SessionService sessionService;
  private final LogUtil logger;
  private StringValueResolver resolver;
  // Database jndi-url
  @Value("${spring.datasource.jndi-name}")
  private String databaseJndi;
  // Database url
  @Value("${spring.datasource.url:}")
  private String databaseUrl;
  // Database user
  @Value("${spring.datasource.username:}")
  private String databaseUser;
  // Database password
  @Value("${spring.datasource.password:}")
  private String databasePassword;
  // Database driver
  @Value("${spring.datasource.driver-class-name:}")
  private String databaseDriver;
  // Validation query
  @Value("${spring.datasource.validation-query:}")
  private String validationQuery;
  // Store datasources list
  private Map<Object, Object> dataSourceMap;
  /**
   * Autowired constructor
   *
   * @param elements       Awe elements
   * @param queryService   Query service
   * @param sessionService Session Service
   * @param logger         Logger
   */
  public AweDatabaseContextHolder(AweElements elements, QueryService queryService, SessionService sessionService, LogUtil logger) {
    this.elements = elements;
    this.queryService = queryService;
    this.sessionService = sessionService;
    this.logger = logger;
  }

  /**
   * Load datasources from current connection
   *
   * @return datasource map
   */
  public Map<Object, Object> getDataSources() {
    Map<Object, Object> dataSources = new HashMap<>();
    Map<String, DatabaseConnectionInfo> connectionInfoMap = loadDataSources();

    // Retrieve datasources
    for (DatabaseConnectionInfo connectionInfo : connectionInfoMap.values()) {
      try {
        dataSources.put(connectionInfo.getAlias(), getDataSource(connectionInfo.getJndi(), connectionInfo.getUrl(),
          connectionInfo.getUser(), connectionInfo.getPassword(), connectionInfo.getDriver(), validationQuery));
      } catch (Exception exc) {
        // Log datasource failure
        logger.log(AweDatabaseContextHolder.class, Level.ERROR, "Error retrieving datasource ''{0}''", exc, connectionInfo.getAlias());
      }
    }

    // Redefine target datasources
    dataSourceMap = dataSources;
    return dataSources;
  }

  /**
   * Retrieve datasource definition
   *
   * @param jndi   JNDI
   * @param url    URL
   * @param user   User
   * @param pass   Password
   * @param driver Driver
   * @return Datasource
   */
  DataSource getDataSource(String jndi, String url, String user, String pass, String driver, String validationQuery) {
    DataSource dataSource = null;
    if (jndi != null && !jndi.isEmpty()) {
      final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
      dsLookup.setResourceRef(true);
      dataSource = dsLookup.getDataSource(resolver.resolveStringValue(jndi));
    } else if (url != null && !url.isEmpty()) {
      org.apache.tomcat.jdbc.pool.DataSource tomcatDataSource = new org.apache.tomcat.jdbc.pool.DataSource();
      tomcatDataSource.setUrl(resolver.resolveStringValue(url));
      if (user != null) {
        tomcatDataSource.setUsername(resolver.resolveStringValue(user));
      }
      if (pass != null) {
        tomcatDataSource.setPassword(resolver.resolveStringValue(pass));
      }
      tomcatDataSource.setDriverClassName(resolver.resolveStringValue(driver));
      tomcatDataSource.setValidationQuery(validationQuery);
      dataSource = tomcatDataSource;
    }
    return dataSource;
  }

  /**
   * Get a datasource connection from an alias
   *
   * @param alias Datasource alias
   * @return Datasource connection
   */
  DataSource getDataSource(String alias) throws AWException {
    if (dataSourceMap.containsKey(alias)) {
      return (DataSource) dataSourceMap.get(alias);
    } else {
      throw new AWException(elements.getLocaleWithLanguage(ERROR_TITLE_INVALID_CONNECTION, elements.getLanguage()),
        elements.getLocaleWithLanguage("ERROR_MESSAGE_UNDEFINED_DATASOURCE", elements.getLanguage(), alias));
    }
  }

  /**
   * Get the default datasource
   *
   * @return Datasource connection
   */
  public DataSource getDataSource() {
    return getDataSource(databaseJndi, databaseUrl, databaseUser, databasePassword, databaseDriver, validationQuery);
  }

  /**
   * Load datasources from current connection
   *
   * @return datasource map
   */
  private Map<String, DatabaseConnectionInfo> loadDataSources() {
    Map<String, DatabaseConnectionInfo> connectionMap = new HashMap<>();
    ServiceData serviceData = null;
    try {
      serviceData = queryService.launchPrivateQuery(AweConstants.DATABASE_CONNECTIONS_QUERY, "1", "0");
    } catch (AWException exc) {
      logger.log(AweDatabaseContextHolder.class, Level.ERROR, "Error retrieving datasources from default connection", exc);
    }

    // Retrieve datasources
    if (serviceData != null && serviceData.getDataList() != null) {
      for (Map<String, CellData> row : serviceData.getDataList().getRows()) {
        DatabaseConnectionInfo connectionInfo = new DatabaseConnectionInfo(row);
        try {
          connectionMap.put(connectionInfo.getAlias(), connectionInfo);
        } catch (Exception exc) {
          // Log datasource failure
          logger.log(AweDatabaseContextHolder.class, Level.ERROR, "Error retrieving datasource ''{0}''", exc, connectionInfo.getAlias());
        }
      }
    }

    // Redefine target datasources
    return connectionMap;
  }

  /**
   * Get current connection type
   *
   * @return Database type
   * @throws AWException Error retrieving database type
   */
  public String getDatabaseType(DataSource dataSource) throws AWException {
    try {
      String url = JdbcUtils.extractDatabaseMetaData(dataSource, DatabaseMetaData::getURL);
      return DatabaseDriver.fromJdbcUrl(url).getId();
    } catch (Exception exc) {
      throw new AWException("Error retrieving database type from datasource", dataSource.toString(), exc);
    }
  }

  /**
   * Get current database
   *
   * @return Current database
   */
  public String getCurrentDatabase() {
    try {
      return (String) sessionService.getSessionParameter(AweConstants.SESSION_DATABASE);
    } catch (Exception exc) {
      return null;
    }
  }

  /**
   * Get current database connection
   *
   * @param dataSource datasource
   * @return Database connection
   * @throws AWException error retrieving connection or database type
   */
  public DatabaseConnection getDatabaseConnection(DataSource dataSource) throws AWException {
    return new DatabaseConnection(getDatabaseType(dataSource), dataSource, getCurrentDatabase());
  }

  /**
   * Get current database connection
   *
   * @param alias Datasource alias
   * @return Database connection
   * @throws AWException error retrieving connection or database type
   */
  public DatabaseConnection getDatabaseConnection(String alias) throws AWException {
    DataSource dataSource = getDataSource(alias);
    return new DatabaseConnection(getDatabaseType(dataSource), dataSource, alias);
  }

  @Override
  public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
    resolver = stringValueResolver;
  }
}
