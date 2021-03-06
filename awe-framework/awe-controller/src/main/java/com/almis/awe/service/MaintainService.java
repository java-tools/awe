package com.almis.awe.service;

import com.almis.awe.component.AweDatabaseContextHolder;
import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.details.MaintainResultDetails;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.maintain.*;
import com.almis.awe.model.entities.queries.*;
import com.almis.awe.model.type.ParameterType;
import com.almis.awe.model.type.RowType;
import com.almis.awe.model.util.data.ListUtil;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.service.data.builder.SQLMaintainBuilder;
import com.almis.awe.service.data.connector.maintain.MaintainLauncher;
import com.almis.awe.service.data.connector.query.QueryLauncher;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLQueryFactory;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Provides methods to insert/update/delete application data
 *
 * @author Pablo GARCIA
 */
public class MaintainService extends ServiceConfig {

  // Constants
  private static final String ERROR_TITLE_LAUNCHING_MAINTAIN = "ERROR_TITLE_LAUNCHING_MAINTAIN";
  private static final String ERROR_MESSAGE_MAINTAIN_NOT_FOUND = "ERROR_MESSAGE_MAINTAIN_NOT_FOUND";
  // Autowired services
  private final MaintainLauncher maintainLauncher;
  private final AccessService accessService;
  private final QueryUtil queryUtil;

  @Value("${awe.database.parameter.name:_database_}")
  private String databaseParameterName;

  /**
   * Autowired constructor
   *
   * @param maintainLauncher Maintain launcher
   * @param accessService    Access service
   * @param queryUtil        Query utilities
   */
  public MaintainService(MaintainLauncher maintainLauncher, AccessService accessService, QueryUtil queryUtil) {
    this.maintainLauncher = maintainLauncher;
    this.accessService = accessService;
    this.queryUtil = queryUtil;
  }

  /**
   * Launch maintain action (from services)
   *
   * @return Service data
   * @throws AWException AWE exception
   */
  public ServiceData launchMaintainAction() throws AWException {
    return launchMaintain(getRequest().getTargetAction());
  }

  /**
   * Retrieve maintain list
   *
   * @param search Search text
   * @return Maintain list
   */
  public ServiceData getMaintainList(String search) {
    DataList result = new DataList();
    Map<String, Target> maintainMap = getElements().getMaintainMap();

    // For each retrieved maintain, filter by suggest
    for (Map.Entry<String, Target> entry : maintainMap.entrySet()) {
      if (entry.getKey().toUpperCase().contains(search.toUpperCase())) {
        Map<String, CellData> row = new HashMap<>();
        row.put("Ide", new CellData(entry.getKey()));
        result.addRow(row);
      }
    }

    // Retrieve datalist
    return new ServiceData().setDataList(result);
  }

  /**
   * Launches a maintain
   *
   * @param maintainId Maintain identifier
   * @return Service output
   * @throws AWException Error launching maintain
   */
  public ServiceData launchMaintain(String maintainId) throws AWException {
    return launchMaintain(maintainId, queryUtil.getParameters());
  }

  /**
   * Launches a maintain with an alias
   *
   * @param maintainId Maintain identifier
   * @param alias      Connection alias
   * @return Service output
   * @throws AWException Error launching maintain
   */
  public ServiceData launchMaintain(String maintainId, String alias) throws AWException {
    return launchMaintain(maintainId, queryUtil.getParameters(alias, "1", "0"));
  }

  /**
   * Launches a maintain with parameters
   *
   * @param maintainId Maintain identifier
   * @param parameters Launch parameters
   * @return Service output
   * @throws AWException Error launching maintain
   */
  public ServiceData launchMaintain(String maintainId, ObjectNode parameters) throws AWException {
    return launchMaintain(maintainId, parameters, getDatabaseConnection(parameters), false);
  }

  /**
   * Launches a maintain with a connection
   *
   * @param maintainId          Maintain identifier
   * @param databaseConnection  Database connection
   * @param keepAliveConnection Keep alive connection (for treatments)
   * @return Service output
   * @throws AWException Error launching maintain
   */
  public ServiceData launchMaintain(String maintainId, DatabaseConnection databaseConnection, boolean keepAliveConnection) throws AWException {
    return launchMaintain(prepareMaintain(maintainId, true), queryUtil.getParameters(), databaseConnection, keepAliveConnection);
  }

  /**
   * Launches a maintain with a connection
   *
   * @param maintainId          Maintain identifier
   * @param databaseConnection  Database connection
   * @param keepAliveConnection Keep alive connection (for treatments)
   * @return Service output
   * @throws AWException Error launching maintain
   */
  public ServiceData launchMaintain(String maintainId, ObjectNode parameters, DatabaseConnection databaseConnection, boolean keepAliveConnection) throws AWException {
    // Check if database connection is defined
    if (databaseConnection == null || databaseConnection.getConnection() == null) {
      throw new AWException(getLocale(ERROR_TITLE_LAUNCHING_MAINTAIN), getLocale("ERROR_MESSAGE_INVALID_CONNECTION"));
    }

    return launchMaintain(prepareMaintain(maintainId, true), parameters, databaseConnection, keepAliveConnection);
  }

  /**
   * Launches a maintain
   *
   * @param maintainId Maintain identifier
   * @return Service output
   * @throws AWException Error launching maintain
   */
  public ServiceData launchPrivateMaintain(String maintainId) throws AWException {
    return launchPrivateMaintain(maintainId, queryUtil.getParameters());
  }

  /**
   * Launches a maintain with an alias
   *
   * @param maintainId Maintain identifier
   * @param alias      Connection alias
   * @return Service output
   * @throws AWException Error launching maintain
   */
  public ServiceData launchPrivateMaintain(String maintainId, String alias) throws AWException {
    return launchPrivateMaintain(maintainId, queryUtil.getParameters(alias, "1", "0"));
  }

  /**
   * Launches a maintain with parameters
   *
   * @param maintainId Maintain identifier
   * @param parameters Launch parameters
   * @return Service output
   * @throws AWException Error launching maintain
   */
  public ServiceData launchPrivateMaintain(String maintainId, ObjectNode parameters) throws AWException {
    return launchPrivateMaintain(maintainId, parameters, getDatabaseConnection(parameters), false);
  }

  /**
   * Launches a maintain with a connection without checking session
   *
   * @param maintainId          Maintain identifier
   * @param databaseConnection  Database connection
   * @param keepAliveConnection Keep alive connection (for treatments)
   * @return Service output
   * @throws AWException Error launching maintain
   */
  public ServiceData launchPrivateMaintain(String maintainId, DatabaseConnection databaseConnection, boolean keepAliveConnection) throws AWException {
    return launchMaintain(prepareMaintain(maintainId, false), queryUtil.getParameters(), databaseConnection, keepAliveConnection);
  }

  /**
   * Launches a maintain with a connection without checking session
   *
   * @param maintainId          Maintain identifier
   * @param parameters          Parameters
   * @param databaseConnection  Database connection
   * @param keepAliveConnection Keep alive connection (for treatments)
   * @return Service output
   * @throws AWException Error launching maintain
   */
  public ServiceData launchPrivateMaintain(String maintainId, ObjectNode parameters, DatabaseConnection databaseConnection, boolean keepAliveConnection) throws AWException {
    // Check if database connection is defined
    if (databaseConnection == null || databaseConnection.getConnection() == null) {
      throw new AWException(getLocale(ERROR_TITLE_LAUNCHING_MAINTAIN), getLocale("ERROR_MESSAGE_INVALID_CONNECTION"));
    }

    return launchMaintain(prepareMaintain(maintainId, false), parameters, databaseConnection, keepAliveConnection);
  }

  /**
   * Retrieve database connection from alias checking nulls
   *
   * @param alias Alias
   * @return Database connection
   * @throws AWException Error retrieving database connection
   */
  private DatabaseConnection getSafeDatabaseConnection(String alias) throws AWException {
    return alias == null ? getSessionDatabaseConnection() : getDatabaseConnection(alias);
  }

  /**
   * Retrieve database connection from parameters
   *
   * @param parameters Parameters
   * @return Database connection
   * @throws AWException Error retrieving database connection
   */
  public DatabaseConnection getDatabaseConnection(ObjectNode parameters) throws AWException {
    JsonNode database = queryUtil.getRequestParameter(databaseParameterName, parameters);
    return getSafeDatabaseConnection(Optional.ofNullable(database).orElse(JsonNodeFactory.instance.nullNode()).textValue());
  }

  /**
   * Retrieve database connection
   *
   * @return Database connection
   * @throws AWException Error retrieving database connection
   */
  public DatabaseConnection getDatabaseConnection() throws AWException {
    return getDatabaseConnection(queryUtil.getParameters());
  }

  /**
   * Get database connection
   *
   * @param alias Alias
   * @return Database connection
   * @throws AWException AWE exception
   */
  public DatabaseConnection getDatabaseConnection(String alias) throws AWException {
    return getBean(AweDatabaseContextHolder.class).getDatabaseConnection(alias);
  }

  /**
   * Get database connection
   *
   * @param dataSource Datasource
   * @return Database connection
   * @throws AWException AWE exception
   */
  public DatabaseConnection getDatabaseConnection(DataSource dataSource) throws AWException {
    return getBean(AweDatabaseContextHolder.class).getDatabaseConnection(dataSource);
  }

  /**
   * Get current database connection
   *
   * @return Database connection
   * @throws AWException AWE exception
   */
  private DatabaseConnection getSessionDatabaseConnection() throws AWException {
    return getDatabaseConnection(getSessionDataSource());
  }

  /**
   * Get datasource
   *
   * @return Datasource
   */
  private DataSource getSessionDataSource() {
    return getBean(DataSource.class);
  }

  /**
   * Launches a maintain
   *
   * @param maintainTarget      Maintain target
   * @param databaseConnection  Database connection
   * @param keepAliveConnection Keep alive connection (for treatments)
   * @return Service output
   * @throws AWException Error launching maintain
   */
  public ServiceData launchMaintain(Target maintainTarget, ObjectNode parameters, DatabaseConnection databaseConnection, boolean keepAliveConnection) throws AWException {
    String statementList = "";
    boolean manageConnection = !keepAliveConnection;

    try {
      // Remove autocommit
      if (databaseConnection != null && databaseConnection.getConnection() != null) {
        databaseConnection.getConnection().setAutoCommit(false);
      } else {
        // If connection is null, avoid connection management
        manageConnection = false;
      }

      // Manage maintain queries
      return manageMaintainQueries(maintainTarget, parameters, getMaintainQueryList(maintainTarget, new HashSet<>()), databaseConnection, manageConnection);
    } catch (AWException exc) {
      doRollback(databaseConnection, statementList, manageConnection);
      throw exc;
    } catch (Exception exc) {
      doRollback(databaseConnection, statementList, manageConnection);
      throw new AWException(getLocale(ERROR_TITLE_LAUNCHING_MAINTAIN), getLocale("ERROR_MESSAGE_DURING_MAINTAIN"), exc);
    } finally {
      if (manageConnection) {
        DataSourceUtils.releaseConnection(databaseConnection.getConnection(), databaseConnection.getDataSource());
      }
    }
  }

  /**
   * Retrieve next sequence value (and increase sequence)
   *
   * @param sequence Sequence identifier
   * @return Sequence value
   * @throws AWException Error retrieving next sequence value
   */
  public Integer getNextSequenceValue(String sequence) throws AWException {
    return getNextSequenceValue(sequence, getDatabaseConnection());
  }

  /**
   * Retrieve next sequence value (and increase sequence)
   *
   * @param sequence      Sequence identifier
   * @param databaseAlias Database alias
   * @return Sequence value
   * @throws AWException Error retrieving next sequence value
   */
  public Integer getNextSequenceValue(String sequence, String databaseAlias) throws AWException {
    return getNextSequenceValue(sequence, getDatabaseConnection(databaseAlias));
  }

  /**
   * Retrieve next sequence value (and increase sequence)
   *
   * @param sequence           Sequence identifier
   * @param databaseConnection Database alias
   * @return Sequence value
   * @throws AWException Error retrieving next sequence value
   */
  public Integer getNextSequenceValue(String sequence, DatabaseConnection databaseConnection) throws AWException {
    final Connection connection = databaseConnection.getConnection();
    Provider<Connection> connProvider = () -> connection;
    Configuration configuration = (Configuration) getBean(databaseConnection.getConfigurationBean());

    // Get maintain builder
    Integer result = Integer.valueOf(getBean(SQLMaintainBuilder.class)
      .setFactory(new SQLQueryFactory(configuration, connProvider))
      .getSequence(sequence));

    // Release connection
    DataSourceUtils.releaseConnection(databaseConnection.getConnection(), databaseConnection.getDataSource());

    // Retrieve result
    return result;
  }

  /**
   * Retrieve maintain target from maintain id
   *
   * @param maintainId   Maintain id
   * @param checkSession Check session
   * @return Maintain target
   * @throws AWException Error retrieving maintain target
   */
  private Target prepareMaintain(String maintainId, boolean checkSession) throws AWException {
    // Variable Definition
    Target maintain;
    try {
      if (getElements().getMaintain(maintainId) != null) {
        // Get maintain
        maintain = getElements().getMaintain(maintainId).copy();

        // If query is private, check security
        if (checkSession && !maintain.isPublic() && !accessService.isAuthenticated()) {
          getLogger().log(QueryService.class, Level.WARN, getLocale("ERROR_MESSAGE_OUT_OF_SESSION"));
          throw new AWException(getLocale(ERROR_TITLE_LAUNCHING_MAINTAIN), getLocale("ERROR_MESSAGE_OUT_OF_SESSION"));
        }
      } else {
        throw new AWException(getLocale(ERROR_TITLE_LAUNCHING_MAINTAIN), getLocale(ERROR_MESSAGE_MAINTAIN_NOT_FOUND, maintainId));
      }
      return maintain;
    } catch (AWException exc) {
      // Launch AWException
      throw exc;
    } catch (Exception exc) {
      throw new AWException(getLocale(ERROR_TITLE_LAUNCHING_MAINTAIN), exc.toString(), exc);
    }
  }

  /**
   * Manage maintain query
   *
   * @param maintainTarget     Target
   * @param queryList          Query list
   * @param databaseConnection Database connection
   * @param manageConnection   Manage connection
   * @return Service data
   * @throws AWException  Error in maintain query
   * @throws SQLException Error with database execution
   */
  private ServiceData manageMaintainQueries(Target maintainTarget, ObjectNode parameters, List<MaintainQuery> queryList, DatabaseConnection databaseConnection, boolean manageConnection) throws AWException, SQLException {
    ServiceData result = new ServiceData();
    List<MaintainResultDetails> resultDetails = new ArrayList<>();
    StringBuilder messageBuilder = new StringBuilder();
    int operationNumber = 1;

    for (MaintainQuery maintainQuery : queryList) {
      maintainQuery.setOperationId(maintainTarget.getName() + "-" + operationNumber++);
      if (maintainQuery instanceof Commit && manageConnection) {
        // If is a commit, launch it
        databaseConnection.getConnection().commit();
      } else if (maintainQuery instanceof RetrieveData) {
        QueryLauncher queryLauncher = getBean(QueryLauncher.class);
        result = queryLauncher.launchQuery(maintainQuery, parameters);
        queryUtil.addDataListToRequestParameters(result.getDataList(), parameters);
      } else {
        // Else launch the maintain or service action
        ServiceData resultingServiceData = maintainLauncher.launchMaintain(maintainQuery, databaseConnection, parameters);

        // Store result
        if (resultingServiceData != null) {
          // Set the service data
          result = resultingServiceData;
          resultDetails.addAll(resultingServiceData.getResultDetails());
        }
      }

      // Generate result message
      generateResultMessage(messageBuilder, result.getMessage(), maintainQuery.getLabel());
    }

    // Commit transaction if connection is not null
    if (manageConnection) {
      databaseConnection.getConnection().commit();
    }

    // Generate response and return data
    return generateResponse(result, maintainTarget, messageBuilder.toString(), resultDetails);
  }

  /**
   * Retrieve maintain query list
   *
   * @param maintainTarget  Maintain target
   * @param includedTargets Included target list
   * @return Query list
   */
  private List<MaintainQuery> getMaintainQueryList(Target maintainTarget, Set<String> includedTargets) throws AWException {
    List<MaintainQuery> finalQueryList = new ArrayList<>();

    // Get Extra Maintain Query Statements
    for (MaintainQuery maintainQuery : maintainTarget.getQueryList()) {
      if (maintainQuery instanceof Multiple) {
        // Add all individual statements to Query List
        finalQueryList.addAll(extractQueryList((Multiple) maintainQuery));
      } else if (maintainQuery instanceof IncludeTarget) {
        // Add current target before including a new one (to avoid cyclic dependencies)
        includedTargets.add(maintainTarget.getName());
        String targetToInclude = ((IncludeTarget) maintainQuery).getName();
        if (!includedTargets.contains(targetToInclude)) {
          finalQueryList.addAll(getMaintainQueryList(prepareMaintain(targetToInclude, false), includedTargets));
        }
      } else {
        // Launch the maintain or service action
        finalQueryList.add(maintainQuery);
      }
    }

    return finalQueryList;
  }

  /**
   * Generate result message
   *
   * @param messageBuilder Message builder
   * @param message        Message
   * @param label          Maintain query label
   */
  private void generateResultMessage(StringBuilder messageBuilder, String message, String label) {
    // Define new line string
    String newLine = messageBuilder.length() == 0 ? "" : "<br/><br/>";

    // Get message result
    if (message != null && !message.trim().isEmpty()) {
      messageBuilder.append(newLine).append(message);
    } else if (label != null && !label.trim().isEmpty()) {
      messageBuilder.append(newLine).append(getLocale(label));
    }
  }

  /**
   * Do maintain rollback
   *
   * @param databaseConnection Database connection
   * @param statementList      Statement list
   * @param manageConnection   Manage connection
   * @throws AWException Error doing rollback
   */
  public void doRollback(DatabaseConnection databaseConnection, String statementList, boolean manageConnection) throws AWException {
    if (manageConnection) {
      try (Connection connection = databaseConnection.getConnection()) {
        connection.rollback();
      } catch (SQLException exc) {
        throw new AWException(getLocale("ERROR_TITLE_DURING_ROLLBACK"), getLocale("ERROR_MESSAGE_DURING_ROLLBACK") + "\n[" + statementList + "]", exc);
      }
    }
  }

  /**
   * Do maintain rollback
   *
   * @param databaseConnection Database connection
   * @param statementList      Statement list
   * @param manageConnection   Manage connection
   * @throws AWException Error doing rollback
   */
  public void doCommit(DatabaseConnection databaseConnection, String statementList, boolean manageConnection) throws AWException {
    if (manageConnection) {
      try (Connection connection = databaseConnection.getConnection()) {
        connection.commit();
      } catch (SQLException exc) {
        doRollback(databaseConnection, statementList, true);
        throw new AWException(getLocale("ERROR_TITLE_DURING_COMMIT"),
          getLocale("ERROR_MESSAGE_DURING_COMMIT") + "\n[" + statementList + "]", exc);
      }
    }
  }

  /**
   * Generate response message
   *
   * @param serviceData    Service data
   * @param maintainTarget Maintain target
   * @param message        Message
   * @param resultDetails  Result details
   * @return Service data filled
   */
  private ServiceData generateResponse(ServiceData serviceData, Target maintainTarget, String message, List<MaintainResultDetails> resultDetails) {

    // Set title if it is not defined
    if (serviceData.getTitle().isEmpty()) {
      if (maintainTarget.getLabel() == null) {
        serviceData.setTitle(getLocale("OK_TITLE_MAINTAIN_OPERATION"));
      } else {
        serviceData.setTitle(getLocale(maintainTarget.getLabel()));
      }
    }

    // Set message if it is not defined
    if (message.isEmpty()) {
      serviceData.setMessage(getLocale("OK_MESSAGE_MAINTAIN_OPERATION"));
    } else {
      serviceData.setMessage(message);
    }

    // Generate result
    addOutputVariables(serviceData, resultDetails);

    return serviceData;
  }

  /**
   * Returns the Query List
   *
   * @param maintain Multiple maintain
   * @return List of maintain query
   * @throws AWException Error extracting query list
   */
  private List<MaintainQuery> extractQueryList(Multiple maintain) throws AWException {

    // Variable initialization
    List<MaintainQuery> queryList = new ArrayList<>();

    String typ;
    String rowTypIde = maintain.getGrid() + "-RowTyp";

    try {
      ArrayNode rowTypeList = (ArrayNode) getRequest().getParameter(rowTypIde);

      // Generate
      for (int i = 0; i < rowTypeList.size(); i++) {
        // Get Parameters
        typ = rowTypeList.get(i).asText();
        MaintainQuery query;

        switch (RowType.valueOf(typ)) {
          case UPDATE:
            // Add an update target
            query = new Update();
            addFields(maintain, query, false, true);
            addFilters(maintain, query);
            break;
          case DELETE:
            // Add a delete target
            query = new Delete();
            addFields(maintain, query, false, false);
            addFilters(maintain, query);
            break;
          case INSERT:
            // Add an insert target
            query = new Insert();
            addFields(maintain, query, true, true);
            break;
          default:
            continue;
        }

        // Add generic fields
        addTables(maintain, query);
        addVariables(maintain, query);

        // Add attributes to query
        query.setVariableIndex(i);
        query.setLabel(maintain.getLabel());
        query.setAuditTable(maintain.getAuditTable());

        // Add maintain query to query list
        queryList.add(query);
      }
    } catch (AWException exc) {
      getLogger().log(null, Level.ERROR, getLocale("ERROR_TITLE_MAINTAIN_MULTIPLE"), exc);
      throw exc;
    }

    return queryList;
  }

  /**
   * Add tables from multiple query to specific maintain query
   *
   * @param origin        Maintain multiple
   * @param maintainQuery Maintain query
   */
  private void addTables(Multiple origin, MaintainQuery maintainQuery) {

    // Table definition
    List<Table> tableList = new ArrayList<>();
    if (origin.getTableList() != null) {
      for (Table table : origin.getTableList()) {
        // Get table and table cloned
        Table clonedTable = table.copy();
        // Add variable to list
        tableList.add(clonedTable);
      }
    }

    // Set field list to query
    maintainQuery.setTableList(tableList);
  }

  /**
   * Add fields from multiple query to specific maintain query
   *
   * @param origin        Multiple maintain
   * @param maintainQuery Maintain query
   * @param addKeys       Add keys
   * @param addNonKeys    Add non keys
   */
  private void addFields(Multiple origin, MaintainQuery maintainQuery, boolean addKeys, boolean addNonKeys) throws AWException {

    // Field definition
    List<SqlField> fieldList = new ArrayList<>();
    if (origin.getFieldList() != null) {
      for (Field field : origin.getFieldList()) {
        Field clonedField = field.copy();

        // If field is key, and addKeys is false, or field is not key and addNonKeys is false, set audit to true to
        // use the field only for audit (else let it with its own value)
        if ((field.isKey() && !addKeys) || (!field.isKey() && !addNonKeys)) {
          clonedField.setAudit(true);
        }

        // Add field copy to list
        fieldList.add(clonedField);
      }
    }

    // Set field list to query
    maintainQuery.setSqlFieldList(fieldList);
  }

  /**
   * Add filters from multiple query to specific maintain query
   *
   * @param maintainQuery Maintain query
   */
  private void addFilters(Multiple origin, MaintainQuery maintainQuery) {

    // Filter definition
    List<Filter> filterList = new ArrayList<>();
    FilterAnd filterGroup = new FilterAnd();

    // For each key field generate a filter
    if (origin.getFieldList() != null) {
      for (Field field : origin.getFieldList()) {

        // If field is key generate a filter with it
        if (field.isKey()) {
          // Generate a new filter and fill it
          Filter filter = Filter.builder()
            .leftField(field.getId())
            .leftTable(field.getTable())
            .condition("eq")
            .rightVariable(field.getVariable())
            .build();

          // If variable is a string, ignorecase it
          Variable var = origin.getVariableDefinition(field.getVariable());
          switch (ParameterType.valueOf(var.getType())) {
            case STRING:
            case STRINGB:
            case STRINGL:
            case STRINGR:
            case STRING_HASH_RIPEMD160:
            case STRING_HASH_SHA:
            case STRING_HASH_PBKDF_2_W_HMAC_SHA_1:
            case STRING_ENCRYPT:
              filter.setIgnoreCase(true);
              break;
            default:
              filter.setIgnoreCase(false);
              break;
          }

          // Add new filter to filter list
          filterList.add(filter);
        }
      }
    }

    // Add filter list to filter group
    filterGroup.setFilterList(filterList);

    // Set filter group list to query
    maintainQuery.setFilterGroup(filterGroup);
  }

  /**
   * Add variables from multiple query to specific maintain query
   *
   * @param origin        Maintain multiple
   * @param maintainQuery Maintain query
   */
  private void addVariables(Multiple origin, MaintainQuery maintainQuery) throws AWException {
    // Set field list to query
    maintainQuery.setVariableDefinitionList(ListUtil.copyList(origin.getVariableDefinitionList()));
  }

  /**
   * Add output variables
   *
   * @param out Output
   */
  private void addOutputVariables(ServiceData out, List<MaintainResultDetails> resultDetails) {
    out
      .addVariable(AweConstants.ACTION_MESSAGE_TYPE, new CellData(out.getType().toString()))
      .addVariable(AweConstants.ACTION_MESSAGE_TITLE, new CellData(out.getTitle()))
      .addVariable(AweConstants.ACTION_MESSAGE_DESCRIPTION, new CellData(out.getMessage()))
      .addVariable(AweConstants.ACTION_MESSAGE_RESULT_DETAILS, new CellData(resultDetails));
  }
}
