package com.almis.awe.autoconfigure;

import com.almis.awe.component.AweDatabaseContextHolder;
import com.almis.awe.component.AweRoutingDataSource;
import com.almis.awe.listener.SpringSQLCloseListener;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.service.QueryService;
import com.almis.awe.service.SessionService;
import com.almis.awe.service.data.builder.SQLMaintainBuilder;
import com.almis.awe.service.data.builder.SQLQueryBuilder;
import com.almis.awe.service.data.connector.maintain.SQLMaintainConnector;
import com.almis.awe.service.data.connector.query.SQLQueryConnector;
import com.almis.awe.template.FixedOracleTemplates;
import com.almis.awe.template.FixedSQLServerTemplates;
import com.querydsl.sql.*;
import com.querydsl.sql.types.ClobType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;

/**
 * Class used to launch initial load treads
 */
@org.springframework.context.annotation.Configuration
@ConditionalOnProperty(name = "awe.database.enabled", havingValue = "true")
public class SQLConfig {

  /**
   * Database context holder
   *
   * @param elements       Awe elements
   * @param queryService   Query service
   * @param sessionService Session service
   * @param logger         Awe logger
   * @return Database context holder bean
   */
  @Bean
  @ConditionalOnMissingBean
  public AweDatabaseContextHolder aweDatabaseContextHolder(AweElements elements, QueryService queryService,
                                                           SessionService sessionService, LogUtil logger) {
    return new AweDatabaseContextHolder(elements, queryService, sessionService, logger);
  }

  /**
   * Datasource
   *
   * @param databaseContextHolder Database context holder
   * @return Datasource bean
   */
  @Bean
  @ConditionalOnMissingBean
  @Lazy
  public AweRoutingDataSource aweRoutingDataSource(AweDatabaseContextHolder databaseContextHolder) {
    return new AweRoutingDataSource(databaseContextHolder);
  }

  /**
   * Oracle database configuration
   *
   * @return Oracle database configuration bean
   */
  @Bean
  @Scope("prototype")
  public Configuration oracleDatabaseConfiguration() {
    return getConfiguration(new FixedOracleTemplates());
  }

  /**
   * SQL Server database configuration
   *
   * @return SQL Server database configuration bean
   */
  @Bean
  @Scope("prototype")
  public Configuration sqlserverDatabaseConfiguration() {
    return getConfiguration(new FixedSQLServerTemplates());
  }

  /**
   * Sybase database configuration
   *
   * @return Sybase database configuration bean
   */
  @Bean
  @Scope("prototype")
  public Configuration sybaseDatabaseConfiguration() {
    return getConfiguration(SQLTemplates.DEFAULT);
  }

  /**
   * HSQL database configuration
   *
   * @return HSQL database configuration bean
   */
  @Bean
  @Scope("prototype")
  public Configuration hsqldbDatabaseConfiguration() {
    return getConfiguration(HSQLDBTemplates.builder().build());
  }

  /**
   * H2 database configuration
   *
   * @return HSQL database configuration bean
   */
  @Bean
  @Scope("prototype")
  public Configuration h2DatabaseConfiguration() {
    return getConfiguration(H2Templates.builder().build());
  }

  /**
   * MySQL database configuration
   *
   * @return MySQL database configuration bean
   */
  @Bean
  @Scope("prototype")
  public Configuration mysqlDatabaseConfiguration() {
    return getConfiguration(MySQLTemplates.builder().build());
  }

  /**
   * Get configuration with listener
   *
   * @param templates SQL Templates
   * @return Configuration bean
   */
  private Configuration getConfiguration(SQLTemplates templates) {
    Configuration configuration = new Configuration(templates);
    configuration.addListener(new SpringSQLCloseListener());
    configuration.register(new ClobType());
    return configuration;
  }

  /////////////////////////////////////////////
  // CONNECTORS
  /////////////////////////////////////////////

  /**
   * SQL Query connector
   *
   * @param contextHolder Context holder
   * @param queryUtil     Query util
   * @param dataSource    Datasource
   * @return SQL Query connector bean
   */
  @Bean
  @ConditionalOnMissingBean
  public SQLQueryConnector sqlQueryConnector(AweDatabaseContextHolder contextHolder, QueryUtil queryUtil, DataSource dataSource) {
    return new SQLQueryConnector(contextHolder, queryUtil, dataSource);
  }

  /**
   * SQL Maintain connector
   *
   * @return SQL Query connector bean
   */
  @Bean
  @ConditionalOnMissingBean
  public SQLMaintainConnector sqlMaintainConnector(QueryUtil queryUtil) {
    return new SQLMaintainConnector(queryUtil);
  }

  /////////////////////////////////////////////
  // BUILDERS
  /////////////////////////////////////////////

  /**
   * SQL Query builder
   *
   * @param queryUtil Query utilities
   * @return SQL Query builder bean
   */
  @Bean
  @ConditionalOnMissingBean
  @Scope("prototype")
  public SQLQueryBuilder sqlQueryBuilder(QueryUtil queryUtil) {
    return new SQLQueryBuilder(queryUtil);
  }

  /**
   * SQL Maintain builder
   *
   * @param queryUtil Query utilities
   * @return SQL Maintain builder bean
   */
  @Bean
  @ConditionalOnMissingBean
  @Scope("prototype")
  public SQLMaintainBuilder sqlMaintainBuilder(QueryUtil queryUtil) {
    return new SQLMaintainBuilder(queryUtil);
  }
}
