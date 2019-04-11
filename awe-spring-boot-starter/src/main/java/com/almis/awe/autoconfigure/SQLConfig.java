package com.almis.awe.autoconfigure;

import com.almis.awe.component.AweDatabaseContextHolder;
import com.almis.awe.component.AweRoutingDataSource;
import com.almis.awe.listener.SpringSQLCloseListener;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.service.QueryService;
import com.almis.awe.service.SessionService;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.service.data.builder.SQLMaintainBuilder;
import com.almis.awe.service.data.builder.SQLQueryBuilder;
import com.almis.awe.service.data.connector.maintain.SQLMaintainConnector;
import com.almis.awe.service.data.connector.query.SQLQueryConnector;
import com.querydsl.sql.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

/**
 * Class used to launch initial load treads
 */
@org.springframework.context.annotation.Configuration
@ConditionalOnProperty(name = "awe.database.enabled", havingValue = "true")
public class SQLConfig {

  /**
   * Database context holder
   * @param context Web application context
   * @param elements Awe elements
   * @param queryService Query service
   * @param sessionService Session service
   * @param logger Awe logger
   * @return Database context holder bean
   */
  @Bean
  @ConditionalOnMissingBean
  public AweDatabaseContextHolder aweDatabaseContextHolder(WebApplicationContext context, AweElements elements,
                                                           QueryService queryService, SessionService sessionService,
                                                           LogUtil logger) {
    return new AweDatabaseContextHolder(context, elements, queryService, sessionService, logger);
  }

  /**
   * Datasource
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
   * @param logger Log utilities
   * @return Oracle database configuration bean
   */
  @Bean
  @Scope("prototype")
  public Configuration oracleDatabaseConfiguration (LogUtil logger) {
    return getConfiguration(new OracleTemplates(), logger);
  }

  /**
   * SQL Server database configuration
   * @param logger Log utilities
   * @return SQL Server database configuration bean
   */
  @Bean
  @Scope("prototype")
  public Configuration sqlserverDatabaseConfiguration (LogUtil logger) {
    return getConfiguration(new SQLServer2012Templates(), logger);
  }

  /**
   * Sybase database configuration
   * @param logger Log utilities
   * @return Sybase database configuration bean
   */
  @Bean
  @Scope("prototype")
  public Configuration sybaseDatabaseConfiguration (LogUtil logger) {return getConfiguration(SQLTemplates.DEFAULT, logger);
  }

  /**
   * HSQL database configuration
   * @param logger Log utilities
   * @return HSQL database configuration bean
   */
  @Bean
  @Scope("prototype")
  public Configuration hsqlDatabaseConfiguration (LogUtil logger) {
    return getConfiguration(new HSQLDBTemplates(), logger);
  }

  /**
   * H2 database configuration
   * @param logger Log utilities
   * @return HSQL database configuration bean
   */
  @Bean
  @Scope("prototype")
  public Configuration h2DatabaseConfiguration (LogUtil logger) {
    return getConfiguration(new H2Templates(), logger);
  }

  /**
   * MySQL database configuration
   * @param logger Log utilities
   * @return MySQL database configuration bean
   */
  @Bean
  @Scope("prototype")
  public Configuration mysqlDatabaseConfiguration (LogUtil logger) {
    return getConfiguration(new MySQLTemplates(), logger);
  }

  /**
   * Get configuration with listener
   * @param templates SQL Templates
   * @param logger Logger
   * @return Configuration bean
   */
  private Configuration getConfiguration(SQLTemplates templates, LogUtil logger) {
    Configuration configuration = new Configuration(templates);
    configuration.addListener(new SpringSQLCloseListener(logger));
    return configuration;
  }

  /////////////////////////////////////////////
  // CONNECTORS
  /////////////////////////////////////////////

  /**
   * SQL Query connector
   * @param contextHolder Context holder
   * @param queryUtil Query util
   * @param dataSource Datasource
   * @return SQL Query connector bean
   */
  @Bean
  @ConditionalOnMissingBean
  public SQLQueryConnector sqlQueryConnector(AweDatabaseContextHolder contextHolder, QueryUtil queryUtil, DataSource dataSource) {
    return new SQLQueryConnector(contextHolder, queryUtil, dataSource);
  }

  /**
   * SQL Maintain connector
   * @return SQL Query connector bean
   */
  @Bean
  @ConditionalOnMissingBean
  public SQLMaintainConnector sqlMaintainConnector() {
    return new SQLMaintainConnector();
  }

  /////////////////////////////////////////////
  // BUILDERS
  /////////////////////////////////////////////

  /**
   * SQL Query builder
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
   * @param session Awe session
   * @param queryUtil Query utilities
   * @return SQL Maintain builder bean
   */
  @Bean
  @ConditionalOnMissingBean
  @Scope("prototype")
  public SQLMaintainBuilder sqlMaintainBuilder(AweSession session, QueryUtil queryUtil) {
    return new SQLMaintainBuilder(session, queryUtil);
  }
}
