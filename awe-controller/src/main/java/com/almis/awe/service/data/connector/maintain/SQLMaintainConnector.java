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
  private String batchMax;

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
    long rowsUpdated = 0;
    boolean auditActive = false;
    boolean isBatch = query.isBatch();
    int batchBlock = Integer.parseInt(batchMax);

    AbstractSQLClause<?> queryBuilt = null;
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
    SQLMaintainBuilder builder = getBean(SQLMaintainBuilder.class);

    // Build query
    queryBuilt = builder.setMaintain(query)
            .setVariableIndex(0)
            .setOperation(MaintainBuildOperation.NO_BATCH)
            .setFactory(queryFactory)
            .setVariables(parameterMap)
            .build();

    if (auditActive) {
      // Build AUDIT query
      auditQueryBuilt = builder.setAudit(true)
              .build();
    }

    // If we have a variable which is a list, generate a audit query for each value
    Integer indexMaintain = 0;
    while (hasNext(query, indexMaintain, false, parameterMap)) {
      // If operation is batched
      if (isBatch) {
        // If this is the first operation of the batch, generate the initial definition
        if (indexMaintain % batchBlock == 0) {
          queryBuilt = builder.setAudit(false)
                  .setOperation(MaintainBuildOperation.BATCH_INITIAL_DEFINITION)
                  .setVariableIndex(indexMaintain)
                  .build();
        }
        // Build query given the initial definition
        queryBuilt = builder.setAudit(false)
                .setOperation(MaintainBuildOperation.BATCH_INCREASING_ELEMENTS)
                .setPreviousQuery(queryBuilt)
                .setVariableIndex(indexMaintain)
                .build();

        // Add to batch
        addBatch(queryBuilt, query.getMaintainType());

        // If this is the last operation of the batch, launch it
        if ((indexMaintain + 1) % batchBlock == 0) {
          // Launch as single operation
          rowsUpdated = launchAsSingleOperation(queryBuilt, indexMaintain, false, query.getId());
          maintainOut.addResultDetails(new MaintainResultDetails(query.getMaintainType(), rowsUpdated, new HashMap<>(parameterMap)));

          // Restore query's initial definition
          queryBuilt = null;
        }
        // Operation is not batched
      } else {
        // Build query
        queryBuilt = builder.setAudit(false)
                .setOperation(MaintainBuildOperation.NO_BATCH)
                .setVariableIndex(indexMaintain)
                .build();

        // Launch as single operation
        rowsUpdated = launchAsSingleOperation(queryBuilt, indexMaintain, false, query.getId());
        maintainOut.addResultDetails(new MaintainResultDetails(query.getMaintainType(), rowsUpdated, new HashMap<String, QueryParameter>(parameterMap)));

        // Restore query's initial definition
        queryBuilt = null;
      }

      if (auditActive && (isBatch || rowsUpdated > 0)) {
        // If operation is batched
        if (isBatch) {
          // If this is the first operation of the batch, generate the initial definition
          if (indexMaintain % batchBlock == 0) {
            auditQueryBuilt = builder.setAudit(true)
                    .setOperation(MaintainBuildOperation.BATCH_INITIAL_DEFINITION)
                    .setVariableIndex(indexMaintain)
                    .build();
          }
          // Build AUDIT query given the initial definition
          auditQueryBuilt = builder.setAudit(true)
                  .setOperation(MaintainBuildOperation.BATCH_INCREASING_ELEMENTS)
                  .setPreviousQuery(auditQueryBuilt)
                  .setVariableIndex(indexMaintain)
                  .build();

          // Add to batch
          ((SQLInsertClause) auditQueryBuilt).addBatch().setBatchToBulk(true);

          // If this is the last operation of the batch, launch it
          if ((indexMaintain + 1) % batchBlock == 0) {
            // Launch as single operation
            rowsUpdated = launchAsSingleOperation(auditQueryBuilt, indexMaintain, true, query.getId());
            maintainOut.addResultDetails(new MaintainResultDetails(MaintainType.AUDIT, rowsUpdated, new HashMap<>(parameterMap)));

            // Restore query's initial definition
            auditQueryBuilt = null;
          }
          // Operation is not batched
        } else {
          // Build AUDIT query
          auditQueryBuilt = builder.setAudit(true)
                  .setOperation(MaintainBuildOperation.NO_BATCH)
                  .setVariableIndex(indexMaintain)
                  .build();

          // Launch as single operation
          rowsUpdated = launchAsSingleOperation(auditQueryBuilt, indexMaintain, true, query.getId());
          maintainOut.addResultDetails(new MaintainResultDetails(MaintainType.AUDIT, rowsUpdated, new HashMap<>(parameterMap)));

          // Restore query's initial definition
          auditQueryBuilt = null;
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

    Integer batchBlock = Integer.valueOf(batchMax);

    AbstractSQLClause<?> queryBuilt;
    AbstractSQLClause<?> auditQueryBuilt;

    // Store service data
    ServiceData maintainOut = new ServiceData();
    maintainOut.setType(AnswerType.OK);
    maintainOut.getResultDetails();

    // Initialize SQL query factory
    SQLQueryFactory queryFactory = new SQLQueryFactory(getConfiguration(connectionType), connectionProvider);

    // Check if operation should be audited
    auditActive = audit && query.getAuditTable() != null;

    // Get maintain builder
    SQLMaintainBuilder builder = getBean(SQLMaintainBuilder.class);

    // Build query
    queryBuilt = builder.setMaintain(query)
            .setOperation(MaintainBuildOperation.NO_BATCH)
            .setFactory(queryFactory)
            .setVariables(parameterMap)
            .build();

    // Launch as single operation
    rowsUpdated = launchAsSingleOperation(queryBuilt, null, false, query.getId());
    maintainOut.addResultDetails(new MaintainResultDetails(query.getMaintainType(), rowsUpdated, new HashMap<String, QueryParameter>(parameterMap)));

    // Audit the operation
    if (auditActive && rowsUpdated > 0) {
      // Build AUDIT query
      auditQueryBuilt = builder.setAudit(true)
              .build();

      // If we have a variable which is a list, generate a audit query for each value
      Integer indexAudit = 0;
      while (hasNext(query, indexAudit, true, parameterMap)) {
        // If operation is batched
        if (isBatch) {
          // If this is the first operation of the batch, generate the initial definition
          if (indexAudit % batchBlock == 0) {
            auditQueryBuilt = builder.setAudit(true)
                    .setOperation(MaintainBuildOperation.BATCH_INITIAL_DEFINITION)
                    .build();
          }
          // Build AUDIT query given the initial definition
          auditQueryBuilt = builder.setAudit(true)
                  .setOperation(MaintainBuildOperation.BATCH_INCREASING_ELEMENTS)
                  .setPreviousQuery(auditQueryBuilt)
                  .build();

          // Add to batch
          ((SQLInsertClause) auditQueryBuilt).addBatch().setBatchToBulk(true);

          // If this is the last operation of the batch, launch it
          if ((indexAudit + 1) % batchBlock == 0) {
            // Launch as single operation
            rowsUpdated = launchAsSingleOperation(auditQueryBuilt, indexAudit, true, query.getId());
            maintainOut.addResultDetails(new MaintainResultDetails(MaintainType.AUDIT, rowsUpdated, new HashMap<>(parameterMap)));

            // Restore query's initial definition
            auditQueryBuilt = null;
          }
          // Operation is not batched
        } else {
          // Build AUDIT query
          auditQueryBuilt = builder.setAudit(true)
                  .setOperation(MaintainBuildOperation.NO_BATCH)
                  .build();

          // Launch as single operation
          rowsUpdated = launchAsSingleOperation(auditQueryBuilt, indexAudit, true, query.getId());
          maintainOut.addResultDetails(new MaintainResultDetails(MaintainType.AUDIT, rowsUpdated, parameterMap));

          // Restore query's initial definition
          auditQueryBuilt = null;
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
    SQLMaintainBuilder builder = getBean(SQLMaintainBuilder.class);

    // Generate query
    AbstractSQLClause<?> queryBuilt = builder.setMaintain(query)
            .setVariableIndex(query.getVariableIndex())
            .setOperation(MaintainBuildOperation.NO_BATCH)
            .setFactory(queryFactory)
            .setVariables(parameterMap)
            .build();

    // Launch as single operation
    rowsUpdated = launchAsSingleOperation(queryBuilt, null, false, query.getId());
    maintainOut.addResultDetails(new MaintainResultDetails(query.getMaintainType(), rowsUpdated, new HashMap<>(parameterMap)));

    // If AUDIT table is defined and operation has updated any rows, AUDIT the operation
    if (auditActive && rowsUpdated > 0) {
      // Generate AUDIT query
      queryBuilt = builder.setAudit(true)
              .setOperation(MaintainBuildOperation.NO_BATCH)
              .build();

      // Launch as single operation
      rowsUpdated = launchAsSingleOperation(queryBuilt, null, true, query.getId());
      maintainOut.addResultDetails(new MaintainResultDetails(MaintainType.AUDIT, rowsUpdated, new HashMap<>(parameterMap)));
    }

    return maintainOut;
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
    String indexMessage = index == null ? "" : index.toString();
    String sql = StringUtil.toUnilineText(statement.getSQL().get(statement.getSQL().size() - 1).getSQL());

    // Log operation
    getLogger().log(this.getClass(), Level.INFO, "{0}[{1} ({2})] [{3}] => {4} rows affected - Elapsed time: {5}s",
            auditMessage, queryId, indexMessage, sql, updated, getLogger().getTotalTime(timeLapse));

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
