package com.almis.awe.service.data.connector.maintain;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.MaintainResultDetails;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.maintain.MaintainQuery;
import com.almis.awe.model.entities.queries.DatabaseConnection;
import com.almis.awe.model.entities.queries.Variable;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.type.MaintainBuildOperation;
import com.almis.awe.model.type.MaintainType;
import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.service.data.builder.SQLMaintainBuilder;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.AbstractSQLClause;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Provider;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maintain connector for SQL
 */
public class SQLMaintainConnector extends ServiceConfig implements MaintainConnector {

  @Value("${awe.database.audit:false}")
  private Boolean audit;

  @Value("${awe.database.batch.max:100}")
  private Integer batchMax;

  @Value("${awe.database.limit.log.size:0}")
  private Integer logLimit;

  @Override
  public <T extends MaintainQuery> ServiceData launch(T query, DatabaseConnection databaseConnection, Map<String, QueryParameter> parameterMap) throws AWException {
    // Store service data
    ServiceData mntOut;
    final Connection connection = databaseConnection.getConnection();
    Provider<Connection> connProvider = () -> connection;

    // Multiple (multiple maintain + multiple AUDIT)
    if ("true".equalsIgnoreCase(query.getMultiple())) {
      mntOut = launchMultipleMaintain(query, connProvider, databaseConnection.getConnectionType(), parameterMap);
      // Multiple for AUDIT (single maintain + multiple AUDIT)
    } else if ("audit".equalsIgnoreCase(query.getMultiple())) {
      mntOut = launchMultipleAudit(query, connProvider, databaseConnection.getConnectionType(), parameterMap);
      // Simple (single maintain + single AUDIT)
    } else {
      mntOut = launchSingleMaintain(query, connProvider, databaseConnection.getConnectionType(), parameterMap);
    }

    return mntOut;
  }

  /**
   * Launches a multiple SQL statement with multiple AUDIT statement
   *
   * @param query              Maintain Query
   * @param connectionProvider Connection provider
   * @param connectionType     Connection type
   * @param parameterMap       Parameter map
   * @return Maintain output
   * @throws AWException Error launching maintain
   */
  private ServiceData launchMultipleMaintain(MaintainQuery query, Provider<Connection> connectionProvider, String connectionType, Map<String, QueryParameter> parameterMap) throws AWException {

    // Variable definition
    long rowsUpdated;
    boolean isBatch = query.isBatch();

    AbstractSQLClause<?> queryBuilt = null;
    AbstractSQLClause<?> auditQueryBuilt = null;

    // Store service data
    ServiceData maintainOut = new ServiceData();
    maintainOut.setType(AnswerType.OK);
    maintainOut.getResultDetails();

    // Initialize SQL query factory
    SQLQueryFactory queryFactory = new SQLQueryFactory(getConfiguration(connectionType), connectionProvider);

    // Get maintain builder
    SQLMaintainBuilder builder = getBean(SQLMaintainBuilder.class)
      .setMaintain(query)
      .setOperation(MaintainBuildOperation.NO_BATCH)
      .setFactory(queryFactory)
      .setVariables(parameterMap);

    // If we have a variable which is a list, generate a audit query for each value
    Integer indexMaintain = 0;
    while (hasNext(query, indexMaintain, false, parameterMap)) {
      if (isBatch) {
        // If operation is batched
        queryBuilt = launchBatchOperation(indexMaintain, queryBuilt, builder, maintainOut, query, true, false);
      } else {
        // Operation is not batched
        queryBuilt = launchSingleOperation(indexMaintain, builder, maintainOut, query, true, false);
      }

      if (auditResults(query, maintainOut)) {
        if (isBatch) {
          // If operation is batched
          auditQueryBuilt = launchBatchOperation(indexMaintain, auditQueryBuilt, builder, maintainOut, query, true, true);
        } else {
          // Operation is not batched
          auditQueryBuilt = launchSingleOperation(indexMaintain, builder, maintainOut, query, true, true);
        }
      }

      // Increase index
      indexMaintain++;
    }

    // If operation is batched and we have queries left or a query without being launched yet, launch
    if (queryBuilt != null) {
      rowsUpdated = launchAsSingleOperation(queryBuilt, indexMaintain, false, query.getId());
      maintainOut.addResultDetails(new MaintainResultDetails(query.getMaintainType(), rowsUpdated, new HashMap<>(parameterMap)));
    }

    // Same with audit
    if (auditQueryBuilt != null) {
      rowsUpdated = launchAsSingleOperation(auditQueryBuilt, indexMaintain, true, query.getId());
      maintainOut.addResultDetails(new MaintainResultDetails(MaintainType.AUDIT, rowsUpdated, new HashMap<>(parameterMap)));
    }

    return maintainOut;
  }

  /**
   * Check audit results
   * @param query Query
   * @param maintainOut Service output
   * @return Launch audit results
   */
  private boolean auditResults(MaintainQuery query, ServiceData maintainOut) {
    boolean auditActive = audit && query.getAuditTable() != null;
    long rowsAffected = 0;
    for (MaintainResultDetails resultDetails : maintainOut.getResultDetails()) {
      rowsAffected = resultDetails.getRowsAffected();
    }
    return auditActive && (query.isBatch() || rowsAffected > 0);
  }

  /**
   * Launches a single SQL statement with multiple AUDIT statement
   *
   * @param query              Maintain query
   * @param connectionProvider Connection provider
   * @param connectionType     Connection type
   * @param parameterMap       Parameter map
   * @return Maintain output
   * @throws AWException Error launching multiple audit
   */
  private ServiceData launchMultipleAudit(MaintainQuery query, Provider<Connection> connectionProvider, String connectionType, Map<String, QueryParameter> parameterMap) throws AWException {

    // Variable definition
    Long rowsUpdated;
    boolean auditActive;
    boolean isBatch = query.isBatch();

    AbstractSQLClause<?> auditQueryBuilt = null;

    // Store service data
    ServiceData maintainOut = new ServiceData();
    maintainOut.setType(AnswerType.OK);
    maintainOut.getResultDetails();

    // Initialize SQL query factory
    SQLQueryFactory queryFactory = new SQLQueryFactory(getConfiguration(connectionType), connectionProvider);

    // Check if operation should be audited
    auditActive = audit && query.getAuditTable() != null;

    // Get maintain builder
    SQLMaintainBuilder builder = getBean(SQLMaintainBuilder.class)
      .setMaintain(query)
      .setOperation(MaintainBuildOperation.NO_BATCH)
      .setFactory(queryFactory)
      .setVariables(parameterMap);

    // Launch as single operation
    rowsUpdated = launchAsSingleOperation(builder.build(), null, false, query.getId());
    maintainOut.addResultDetails(new MaintainResultDetails(query.getMaintainType(), rowsUpdated, new HashMap<>(parameterMap)));

    // Audit the operation
    if (auditActive && rowsUpdated > 0) {
      // If we have a variable which is a list, generate a audit query for each value
      Integer indexAudit = 0;
      while (hasNext(query, indexAudit, true, parameterMap)) {
        if (isBatch) {
          // If operation is batched
          auditQueryBuilt = launchBatchOperation(indexAudit, auditQueryBuilt, builder, maintainOut, query, false, true);

        } else {
          // Operation is not batched
          auditQueryBuilt = launchSingleOperation(indexAudit, builder,  maintainOut, query, false, true);
        }

        // Increase index
        indexAudit++;
      }

      // If operation is batched and we have queries left or a query without being launched yet, launch
      if (auditQueryBuilt != null) {
        rowsUpdated = launchAsSingleOperation(auditQueryBuilt, indexAudit, true, query.getId());
        maintainOut.addResultDetails(new MaintainResultDetails(MaintainType.AUDIT, rowsUpdated, parameterMap));
      }
    }

    return maintainOut;
  }

  /**
   * Launches a single SQL statement with a single AUDIT statement
   *
   * @param query              Maintain query
   * @param connectionProvider Connection provider
   * @param connectionType     Connection type
   * @param parameterMap       Parameter map
   * @return Maintain output
   * @throws AWException Maintain error
   */
  private ServiceData launchSingleMaintain(MaintainQuery query, Provider<Connection> connectionProvider, String connectionType, Map<String, QueryParameter> parameterMap) throws AWException {
    // Variable definition
    Long rowsUpdated;
    boolean auditActive = false;

    // Store service data
    ServiceData maintainOut = new ServiceData();
    maintainOut.setType(AnswerType.OK);
    maintainOut.getResultDetails();

    // Initialize SQL query factory
    SQLQueryFactory queryFactory = new SQLQueryFactory(getConfiguration(connectionType), connectionProvider);

    // Check if operation should be audited
    auditActive = audit && query.getAuditTable() != null;

    // Get maintain builder
    SQLMaintainBuilder builder = getBean(SQLMaintainBuilder.class).setMaintain(query)
      .setVariableIndex(query.getVariableIndex())
      .setOperation(MaintainBuildOperation.NO_BATCH)
      .setFactory(queryFactory)
      .setVariables(parameterMap);

    // Launch as single operation
    rowsUpdated = launchAsSingleOperation(builder.build(), null, false, query.getId());
    maintainOut.addResultDetails(new MaintainResultDetails(query.getMaintainType(), rowsUpdated, new HashMap<>(parameterMap)));

    // If AUDIT table is defined and operation has updated any rows, AUDIT the operation
    if (auditActive && rowsUpdated > 0) {
      // Launch as single operation
      rowsUpdated = launchAsSingleOperation(builder.setAudit(true).setOperation(MaintainBuildOperation.NO_BATCH).build(), null, true, query.getId());
      maintainOut.addResultDetails(new MaintainResultDetails(MaintainType.AUDIT, rowsUpdated, new HashMap<>(parameterMap)));
    }

    return maintainOut;
  }

  /**
   * Launch batch operation
   * @param index Index audit
   * @param previousQuery Previous query
   * @param builder Maintain builder
   * @param maintainOut Maintain output
   * @param query Maintain query
   * @param addIndex Add index to builder
   * @throws AWException
   * @return Query built
   */
  private AbstractSQLClause<?> launchBatchOperation(int index, AbstractSQLClause<?> previousQuery, SQLMaintainBuilder builder, ServiceData maintainOut, MaintainQuery query, boolean addIndex, boolean isAudit) throws AWException {
    Long rowsUpdated;
    AbstractSQLClause<?> queryBuilt;

    // Batch block
    MaintainType maintainType = isAudit ? MaintainType.AUDIT : query.getMaintainType();

    // If this is the first operation of the batch, generate the initial definition
    if (index % batchMax == 0) {
      builder.setAudit(isAudit)
        .setOperation(MaintainBuildOperation.BATCH_INITIAL_DEFINITION);

      // Add index if defined
      if (addIndex) {
        builder.setVariableIndex(index);
      }

      previousQuery = builder.build();
    }

    // Build query given the initial definition
    builder.setAudit(isAudit)
      .setOperation(MaintainBuildOperation.BATCH_INCREASING_ELEMENTS)
      .setPreviousQuery(previousQuery);

    // Add index if defined
    if (addIndex) {
      builder.setVariableIndex(index);
    }

    queryBuilt = builder.build();

    // Add to batch
    addBatch(queryBuilt, maintainType);

    // If this is the last operation of the batch, launch it
    if ((index + 1) % batchMax == 0) {
      // Launch as single operation
      rowsUpdated = launchAsSingleOperation(queryBuilt, index, isAudit, query.getId());
      maintainOut.addResultDetails(new MaintainResultDetails(maintainType, rowsUpdated, new HashMap<>(builder.getVariables())));

      queryBuilt = null;
    }

    return queryBuilt;
  }

  /**
   * Launch single operation
   * @param index Index
   * @param builder Maintain builder
   * @param maintainOut Maintain output
   * @param query Query
   * @param addIndex Add index to builder
   * @param isAudit Query is an audit query
   * @return Operation builder
   * @throws AWException
   */
  private AbstractSQLClause<?> launchSingleOperation(Integer index, SQLMaintainBuilder builder, ServiceData maintainOut, MaintainQuery query, boolean addIndex, boolean isAudit) throws AWException {
    MaintainType maintainType = isAudit ? MaintainType.AUDIT : query.getMaintainType();

    // Build query
    builder
      .setAudit(isAudit)
      .setOperation(MaintainBuildOperation.NO_BATCH);

    if (addIndex) {
      builder.setVariableIndex(index);
    }

    // Launch as single operation
    Long rowsUpdated = launchAsSingleOperation(builder.build(), index, isAudit, query.getId());
    maintainOut.addResultDetails(new MaintainResultDetails(maintainType, rowsUpdated, new HashMap<>(builder.getVariables())));

    // Restore query's initial definition
    return null;
  }

  /**
   * Launch as single operation
   *
   * @param statement SQL Statement
   * @param index     Index
   * @param isAudit   Audit maintain
   * @param queryId   Maintain query identifier
   * @return Elements modified
   */
  private long launchAsSingleOperation(AbstractSQLClause<?> statement, Integer index, boolean isAudit, String queryId) {
    List<Long> timeLapse = getLogger().prepareTimeLapse();

    // Launch as single operation
    long updated = statement.execute();

    // Get final query time
    getLogger().checkpoint(timeLapse);

    // Audit message
    String auditMessage = isAudit ? "[AUDIT] " : "";
    String indexMessage = index == null ? "" : " (" + index.toString() + ")";
    String sql = StringUtil.toUnilineText(statement.getSQL().get(statement.getSQL().size() - 1).getSQL());

    // Shorten sql clause
    String sqlShortened = StringUtil.shortenText(sql, logLimit, "...");

    // Log operation
    getLogger().log(this.getClass(), Level.INFO, "{0}[{1}{2}] [{3}] => {4} rows affected - Elapsed time: {5}s",
            auditMessage, queryId, indexMessage, sqlShortened, updated, getLogger().getTotalTime(timeLapse));

    return updated;
  }

  /**
   * Returns if there is a next variable
   *
   * @param query        Maintain query
   * @param index        List index
   * @param audit        Check audit variables
   * @param parameterMap Parameter map
   * @return List has more elements
   */
  private boolean hasNext(MaintainQuery query, Integer index, boolean audit, Map<String, QueryParameter> parameterMap) {
    // Variable definition
    Integer total = 0;

    if (query.getVariableDefinitionList() != null) {
      for (Variable variable : query.getVariableDefinitionList()) {
        // If variable is not an AUDIT variable AND is a list
        if (parameterMap.get(variable.getId()).isList() && (!variable.isAudit() || audit)) {
          // Get total
          total = Math.max(parameterMap.get(variable.getId()).getValue().size(), total);
        }
      }
    }
    return (index < total);
  }

  /**
   * Adds batch operation casting the query according to its type
   *
   * @param query SQL query
   * @param type  Maintain type
   */
  private void addBatch(AbstractSQLClause<?> query, MaintainType type) {
    switch (type) {
      case DELETE:
        ((SQLDeleteClause) query).addBatch();
        break;
      case INSERT:
      case AUDIT:
        ((SQLInsertClause) query).addBatch().setBatchToBulk(true);
        break;
      case UPDATE:
        ((SQLUpdateClause) query).addBatch();
        break;
      default:
        break;
    }
  }

  /**
   * Get query configuration
   *
   * @param type Database type
   * @return Configuration
   */
  private Configuration getConfiguration(String type) {
    Configuration config = (Configuration) getBean(type + "DatabaseConfiguration");
    config.setUseLiterals(true);

    return config;
  }

}
