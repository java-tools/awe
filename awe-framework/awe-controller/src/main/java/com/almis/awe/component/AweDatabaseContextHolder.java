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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.util.StringValueResolver;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pgarcia
 */
public class AweDatabaseContextHolder implements EmbeddedValueResolverAware {

  // Autowired services
  private AweElements elements;
  private QueryService queryService;
  private SessionService sessionService;
  private WebApplicationContext context;
  private LogUtil logger;
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

  /**
   * Autowired constructor
   *
   * @param context        Web app context
   * @param elements       Awe elements
   * @param queryService   Query service
   * @param sessionService Session Service
   * @param logger         Logger
   */
  @Autowired
  public AweDatabaseContextHolder(WebApplicationContext context, AweElements elements, QueryService queryService, SessionService sessionService,
                                  LogUtil logger) {
    this.elements = elements;
    this.queryService = queryService;
    this.sessionService = sessionService;
    this.context = context;
    this.logger = logger;
  }

  // Connection type
  @Value("${awe.database.connection.type}")
  private String databaseType;

  // Store datasources list
  private Map<String, DatabaseConnectionInfo> connectionInfoMap = new HashMap<>();
  private Map<Object, Object> dataSourceMap = new HashMap<>();
  private static final String ERROR_TITLE_INVALID_CONNECTION = "ERROR_TITLE_INVALID_CONNECTION";

  /**
   * Load datasources from current connection
   *
   * @return datasource map
   */
  public Map<Object, Object> getDataSources() {
    dataSourceMap = new HashMap<>();
    connectionInfoMap = loadDataSources();

    // Retrieve datasources
    for (DatabaseConnectionInfo connectionInfo : connectionInfoMap.values()) {
      try {
        dataSourceMap.put(connectionInfo.getAlias(), getDataSource(connectionInfo.getJndi(), connectionInfo.getUrl(),
          connectionInfo.getUser(), connectionInfo.getPassword(), connectionInfo.getDriver(), validationQuery));
      } catch (Exception exc) {
        // Log datasource failure
        logger.log(AweDatabaseContextHolder.class, Level.ERROR, "Error retrieving datasource ''{0}''", exc, connectionInfo.getAlias());
      }
    }

    // Redefine target datasources
    return dataSourceMap;
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
      tomcatDataSource.setLogAbandoned(false);
      tomcatDataSource.setTestOnBorrow(true);
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
      throw new AWException(elements.getLocale(ERROR_TITLE_INVALID_CONNECTION),
        elements.getLocale("ERROR_MESSAGE_UNDEFINED_DATASOURCE", alias));
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
    connectionInfoMap = new HashMap<>();
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
          connectionInfoMap.put(connectionInfo.getAlias(), connectionInfo);
        } catch (Exception exc) {
          // Log datasource failure
          logger.log(AweDatabaseContextHolder.class, Level.ERROR, "Error retrieving datasource ''{0}''", exc, connectionInfo.getAlias());
        }
      }
    }

    // Redefine target datasources
    return connectionInfoMap;
  }

  /**
   * Get current connection type
   *
   * @return Database type
   * @throws AWException Error retrieving database type
   */
  public String getDatabaseType() throws AWException {
    return getDatabaseType(getCurrentDatabase());
  }

  /**
   * Get current connection type
   *
   * @param alias Database alias
   * @return Database type
   * @throws AWException Error retrieving database type
   */
  private String getDatabaseType(String alias) throws AWException {
    String currentDatabaseType = databaseType;
    if (currentDatabaseType != null && connectionInfoMap != null && connectionInfoMap.containsKey(alias)) {
      String databaseKey = connectionInfoMap.get(alias).getDatabaseType();
      currentDatabaseType = queryService.findLabel(AweConstants.DATABASE_BEAN_TRANSLATION, databaseKey);
    }

    return currentDatabaseType;
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
    return new DatabaseConnection(getDatabaseType(), dataSource, getCurrentDatabase());
  }

  /**
   * Get current database connection
   *
   * @param alias Datasource alias
   * @return Database connection
   * @throws AWException error retrieving connection or database type
   */
  public DatabaseConnection getDatabaseConnection(String alias) throws AWException {
    return new DatabaseConnection(getDatabaseType(alias), getDataSource(alias), alias);
  }

  @Override
  public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
    resolver = stringValueResolver;
  }
}
