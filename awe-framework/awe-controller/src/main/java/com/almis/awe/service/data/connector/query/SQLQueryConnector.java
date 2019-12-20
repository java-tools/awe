package com.almis.awe.service.data.connector.query;

import com.almis.awe.component.AweDatabaseContextHolder;
import com.almis.awe.exception.AWEQueryException;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.queries.DatabaseConnection;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.almis.awe.service.data.builder.SQLQueryBuilder;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * SQLQueryConnector Class
 * Connection class between QueryLauncher and SQLQueryBuilder
 *
 * @author Jorge BELLON 24-02-2017
 */
public class SQLQueryConnector extends AbstractQueryConnector {

  // Autowired services
  private AweDatabaseContextHolder contextHolder;
  private DataSource dataSource;

  /**
   * Autowired constructor
   *
   * @param contextHolder Database context holder
   * @param queryUtil     Query utilities
   * @param dataSource    Datasource
   */
  @Autowired
  public SQLQueryConnector(AweDatabaseContextHolder contextHolder, QueryUtil queryUtil, DataSource dataSource) {
    super(queryUtil);
    this.contextHolder = contextHolder;
    this.dataSource = dataSource;
  }

  /**
   * Launch query
   *
   * @param query      Query to be launched
   * @param parameters Parameters
   * @return Query output as service data
   * @throws AWException Error launching query
   */
  @Override
  public ServiceData launch(Query query, ObjectNode parameters) throws AWException {

    // Log start query prepare time
    List<Long> timeLapse = getLogger().prepareTimeLapse();

    // Generate the corresponding query factory
    SQLQueryFactory queryFactory = getQueryFactory(parameters);

    // Get query builder
    SQLQueryBuilder builder = getBean(SQLQueryBuilder.class);

    // Get query variables for further usage
    Map<String, QueryParameter> variableMap = builder.setQuery(query)
      .setFactory(queryFactory)
      .setParameters(parameters)
      .getVariables();

    // Set sort and order
    if (variableMap.containsKey(AweConstants.QUERY_SORT)) {
      ArrayNode sortList = (ArrayNode) variableMap.get(AweConstants.QUERY_SORT).getValue();
      builder.setComponentSort(sortList);
    }

    // Get pagination
    long elementsPerPage = variableMap.get(AweConstants.QUERY_MAX).getValue().asLong();
    boolean paginate = query.isPaginationManaged() && (elementsPerPage > 0);

    // Generate query
    SQLQuery<Tuple> queryBuilt = builder.build();
    SQLQuery<Tuple> queryCount = null;
    if (paginate) {
      queryCount = builder.queryForCount().build();
    }

    // Get query preparation time
    getLogger().checkpoint(timeLapse);

    List<Tuple> results;
    long records;
    try {
      if (paginate) {
        long page = variableMap.get(AweConstants.QUERY_PAGE).getValue().asLong();
        queryBuilt.limit(elementsPerPage).offset(elementsPerPage * (page - 1));
      }

      // Launch query
      List<Tuple> allResults = queryBuilt.fetch();
      if (paginate) {
        records = queryFactory.select(SQLExpressions.all).from(queryCount, new PathBuilder<>(Object.class, "R")).fetchCount();
      } else {
        records = allResults.size();
      }
      results = allResults;
    } catch (Exception exc) {
      throw new AWEQueryException(getElements().getLocale("ERROR_TITLE_RETRIEVING_DATA"), getElements().getLocale("ERROR_MESSAGE_EXECUTING_SERVICE_QUERY", query.getId()),
        StringUtil.toUnilineText(queryBuilt.toString()), exc);
    }

    // Get query preparation time
    getLogger().checkpoint(timeLapse);

    DataList datalist;
    try {
      // Generate datalist
      datalist = fillDataList(results, records, query, queryBuilt.getMetadata().getProjection(), variableMap);

      // Get query preparation time
      getLogger().checkpoint(timeLapse);

      // Log query
      getLogger().logWithDatabase(SQLQueryConnector.class, Level.INFO, getQueryUtil().getDatabaseAlias(variableMap), "[{0}] [{1}] => {2} records. Create query time: {3}s - Sql time: {4}s - Datalist time: {5}s - Total time: {6}s",
        query.getId(), StringUtil.toUnilineText(queryBuilt.toString()), records,
        getLogger().getElapsed(timeLapse, AweConstants.PREPARATION_TIME),
        getLogger().getElapsed(timeLapse, AweConstants.EXECUTION_TIME),
        getLogger().getElapsed(timeLapse, AweConstants.RESULTS_TIME),
        getLogger().getTotalTime(timeLapse));
    } catch (AWException exc) {
      throw new AWEQueryException(exc.getTitle(), exc.getMessage(), StringUtil.toUnilineText(queryBuilt.toString()), exc);
    } catch (Exception exc) {
      throw new AWEQueryException(getElements().getLocale("ERROR_TITLE_RETRIEVING_DATA"), getElements().getLocale("ERROR_MESSAGE_EXECUTING_SERVICE_QUERY", query.getId()),
        StringUtil.toUnilineText(queryBuilt.toString()), exc);
    }

    ServiceData out = new ServiceData();
    out.setDataList(datalist);
    return out;
  }

  /**
   * Retrieve corresponding query factory
   *
   * @param parameters Parameters
   * @return Query factory
   * @throws AWException
   */
  private SQLQueryFactory getQueryFactory(ObjectNode parameters) throws AWException {
    // Retrieve current datasource
    DataSource currentDataSource = dataSource;
    DatabaseConnection databaseConnection = contextHolder.getDatabaseConnection(dataSource);

    // Check if call refers to a specific database
    if (parameters.get(AweConstants.COMPONENT_DATABASE) != null) {
      databaseConnection = contextHolder.getDatabaseConnection(parameters.get(AweConstants.COMPONENT_DATABASE).asText());
      currentDataSource = databaseConnection.getDataSource();
    }

    // Retrieve query factory
    return new SQLQueryFactory(getConfiguration(databaseConnection.getConnectionType()), currentDataSource);
  }

  /**
   * Get query configuration
   *
   * @param type connection type
   * @return SQL query configuration
   */
  private Configuration getConfiguration(String type) {
    Configuration config = (Configuration) getBean(type + "DatabaseConfiguration");
    config.setUseLiterals(true);

    return config;
  }

  /**
   * Fill output datalist
   *
   * @param results    Query result
   * @param records    Query records
   * @param query      Query with the information
   * @param projection Query projection
   * @return Query output as datalist
   * @throws AWException Error generating datalist
   */
  private DataList fillDataList(List<Tuple> results, long records, Query query, Expression<?> projection, Map<String, QueryParameter> variableMap) throws AWException {
    DataListBuilder builder = getBean(DataListBuilder.class);
    builder.setQueryProjection(projection)
      .setQueryResult(results)
      .setRecords(records)
      .setMax(variableMap.get(AweConstants.QUERY_MAX).getValue().asLong())
      .setPage(variableMap.get(AweConstants.QUERY_PAGE).getValue().asLong())
      .paginate(!query.isPaginationManaged())
      .generateIdentifiers();

    // Add transformations & translations
    builder = processDataList(builder, query, variableMap);

    return builder.build();
  }

  @Override
  public ServiceData subscribe(Query query, ComponentAddress address, ObjectNode parameters) throws AWException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
