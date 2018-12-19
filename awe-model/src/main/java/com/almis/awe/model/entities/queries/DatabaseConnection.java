package com.almis.awe.model.entities.queries;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * DatabaseConnection Class
 * Bean class with database connection
 *
 * @author Pablo GARCIA - 03/Aug/2017
 */
public class DatabaseConnection {
  private String connectionType;
  private String databaseAlias;
  private DataSource dataSource;
  private Connection connection;

  /**
   * Constructor
   * @param connection Connection
   * @param connectionType Connection type
   * @param dataSource DataSource
   * @param alias Connection alias
   */
  public DatabaseConnection(Connection connection, String connectionType, DataSource dataSource, String alias) {
    this.connection = connection;
    this.connectionType = connectionType;
    this.dataSource = dataSource;
    this.databaseAlias = alias;
  }

  /**
   * Constructor
   * @param connectionType Connection type
   * @param dataSource DataSource
   * @param alias DatabaseAlias
   */
  public DatabaseConnection(String connectionType, DataSource dataSource, String alias) {
    this.connectionType = connectionType;
    this.dataSource = dataSource;
    this.databaseAlias = alias;
  }

  /**
   * Get Connection
   * @return connection
   */
  public Connection getConnection() {
    if (connection == null) {
      try {
        connection = dataSource.getConnection();
      } catch (SQLException exc) {
        connection = null;
      }
    }
    return connection;
  }

  /**
   * Get connection type
   * @return Connection type
   */
  public String getConnectionType() {
    return connectionType;
  }

  /**
   * Set connection type
   * @param connectionType Connection type
   * @return this
   */
  public DatabaseConnection setConnectionType(String connectionType) {
    this.connectionType = connectionType;
    return this;
  }

  /**
   * Get datasource
   * @return Datasource
   */
  public DataSource getDataSource() {
    return dataSource;
  }

  /**
   * Set datasource
   * @param dataSource Datasource
   */
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Get datasource
   * @return Datasource
   */
  public String getDatabaseAlias() {
    return databaseAlias;
  }

  /**
   * Set database alias
   * @param alias Database alias
   */
  public void setDatabaseAlias(String alias) {
    this.databaseAlias = alias;
  }
}
