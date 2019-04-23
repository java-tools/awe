/*
 * Package definition
 */
package com.almis.awe.model.entities.queries;

import com.almis.awe.model.dto.CellData;

import java.util.Map;

/**
 * DatabaseConnectionInfo Class
 *
 * Bean class with message queue connection info
 *
 *
 * @author Pablo GARCIA - 03/Aug/2017
 */
public class DatabaseConnectionInfo {
  private String alias;
  private String description;
  private String connectionType;
  private String databaseType;
  private String url;
  private String user;
  private String password;
  private String driver;
  private String jndi;
  private String environment;

  /**
   * Constructor
   */
  public DatabaseConnectionInfo() {
    super();
  }

  /**
   * Constructor from database row
   *
   * @param connectionInfo Database row
   */
  public DatabaseConnectionInfo(Map<String, CellData> connectionInfo) {
    super();
    alias = connectionInfo.get("alias").getStringValue();
    description = connectionInfo.get("description").getStringValue();
    connectionType = connectionInfo.get("connectionType").getStringValue();
    databaseType = connectionInfo.get("databaseType").getStringValue();
    environment = connectionInfo.get("environment").getStringValue();
    url = connectionInfo.get("url").getStringValue();
    user = connectionInfo.get("user").getStringValue();
    password = connectionInfo.get("password").getStringValue();
    driver = connectionInfo.get("driver").getStringValue();
    jndi = "D".equalsIgnoreCase(connectionType) ? "java:comp/env/" + url : null;
  }

  /**
   * Get alias
   *
   * @return alias
   */
  public String getAlias() {
    return alias;
  }

  /**
   * Set alias
   *
   * @param alias alias
   * @return this
   */
  public DatabaseConnectionInfo setAlias(String alias) {
    this.alias = alias;
    return this;
  }

  /**
   * Get description
   *
   * @return description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Set description
   *
   * @param description Description
   * @return this
   */
  public DatabaseConnectionInfo setDescription(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get connection type (Datasource, JDBC)
   *
   * @return connection type
   */
  public String getConnectionType() {
    return connectionType;
  }

  /**
   * Set connection type (Datasource, JDBC)
   *
   * @param connectionType connection type
   * @return this
   */
  public DatabaseConnectionInfo setConnectionType(String connectionType) {
    this.connectionType = connectionType;
    return this;
  }

  /**
   * Get database type (Oracle, SQS, Sybase...)
   *
   * @return database type
   */
  public String getDatabaseType() {
    return databaseType;
  }

  /**
   * Set database type (Oracle, SQS, Sybase...)
   *
   * @param databaseType Database type
   * @return this
   */
  public DatabaseConnectionInfo setDatabaseType(String databaseType) {
    this.databaseType = databaseType;
    return this;
  }

  /**
   * Get connection url
   *
   * @return URL
   */
  public String getUrl() {
    return url;
  }

  /**
   * Set connection URL
   *
   * @param url URL
   * @return this
   */
  public DatabaseConnectionInfo setUrl(String url) {
    this.url = url;
    return this;
  }

  /**
   * Get connection username
   *
   * @return Connection username
   */
  public String getUser() {
    return user;
  }

  /**
   * Set connection username
   *
   * @param user Username
   * @return this
   */
  public DatabaseConnectionInfo setUser(String user) {
    this.user = user;
    return this;
  }

  /**
   * Get connection password
   *
   * @return password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Set connection password
   *
   * @param password Password
   * @return this
   */
  public DatabaseConnectionInfo setPassword(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get connection driver
   *
   * @return Driver
   */
  public String getDriver() {
    return driver;
  }

  /**
   * Set connection driver
   *
   * @param driver driver
   * @return this
   */
  public DatabaseConnectionInfo setDriver(String driver) {
    this.driver = driver;
    return this;
  }

  /**
   * Get JNDI connection
   *
   * @return jndi
   */
  public String getJndi() {
    return jndi;
  }

  /**
   * Set JNDI connection
   *
   * @param jndi jndi
   * @return this
   */
  public DatabaseConnectionInfo setJndi(String jndi) {
    this.jndi = jndi;
    return this;
  }

  /**
   * Get database environment
   *
   * @return environment
   */
  public String getEnvironment() {
    return environment;
  }

  /**
   * Set database environment
   *
   * @param environment Environment
   * @return this
   */
  public DatabaseConnectionInfo setEnvironment(String environment) {
    this.environment = environment;
    return this;
  }
}
