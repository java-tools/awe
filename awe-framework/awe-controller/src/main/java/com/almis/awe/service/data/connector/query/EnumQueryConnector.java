package com.almis.awe.service.data.connector.query;

import com.almis.awe.exception.AWEQueryException;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.Global;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.almis.awe.service.data.builder.EnumBuilder;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * EnumQueryConnector Class
 * Connection class between QueryLauncher and EnumBuilder
 *
 * @author Jorge BELLON 27-03-2017
 */
public class EnumQueryConnector extends AbstractQueryConnector {

  /**
   * Autowired constructor
   * @param queryUtil Query utilities
   */
  @Autowired
  public EnumQueryConnector(QueryUtil queryUtil) {
    super(queryUtil);
  }

  /**
   * Launch query
   *
   * @param query      Query to be launched
   * @param parameters Parameters
   * @return Query output as service data
   * @throws AWException Query launch error
   */
  @Override
  public ServiceData launch(Query query, ObjectNode parameters) throws AWException {
    return launch(query, query.getEnumerated(), parameters);
  }

  /**
   * Launch enumerated
   *
   * @param enumId     Enumerated identifier
   * @param parameters Parameters
   * @return Query output as servicedata
   * @throws AWException Error launching query
   */
  public ServiceData launchEnum(String enumId, ObjectNode parameters) throws AWException {
    return launch(null, enumId, parameters);
  }

  /**
   * Launch query
   *
   * @param query      Query to be launched
   * @param enumId     Enumerated id
   * @param parameters Parameters
   * @return Query output as servicedata
   * @throws AWException Error launching query
   */
  private ServiceData launch(Query query, String enumId, ObjectNode parameters) throws AWException {

    // Log start query prepare time
    List<Long> timeLapse = getLogger().prepareTimeLapse();
    ServiceData result = new ServiceData();

    // Get query builder
    EnumBuilder builder = getBean(EnumBuilder.class);

    // Get query preparation time
    getLogger().checkpoint(timeLapse);

    // Query launch
    List<Global> resultList;
    try {
      // Launch query
      resultList = builder.setEnumerated(enumId).build();
    } catch (AWException exc) {
      throw exc;
    } catch (Exception exc) {
      throw new AWEQueryException(getLocale("ERROR_TITLE_LAUNCHING_ENUM_QUERY"), exc.getMessage(), enumId, exc);
    }

    // Get elapsed query time
    getLogger().checkpoint(timeLapse);

    // Generate results
    try {
      // Generate datalist
      Map<String, QueryParameter> variableMap = query == null ? getQueryUtil().getDefaultVariableMap(parameters) : getQueryUtil().getVariableMap(query, parameters);
      result.setDataList(fillDataList(resultList, resultList.size(), query, variableMap));
    } catch (AWException exc) {
      throw new AWEQueryException(exc.getTitle(), exc.getMessage(), enumId, exc);
    }

    // Get elapsed datalist time
    getLogger().checkpoint(timeLapse);

    // Log query
    getLogger().log(EnumQueryConnector.class, Level.INFO, "[{0}] => {1} records. Create enumerated time: {2}s - Enumerated time: {3}s - Datalist time: {4}s - Total time: {5}s",
            enumId,
            result.getDataList().getRecords(),
            getLogger().getElapsed(timeLapse, AweConstants.PREPARATION_TIME),
            getLogger().getElapsed(timeLapse, AweConstants.EXECUTION_TIME),
            getLogger().getElapsed(timeLapse, AweConstants.RESULTS_TIME),
            getLogger().getTotalTime(timeLapse));

    return result;
  }

  /**
   * Fill output datalist
   *
   * @param results      Query results
   * @param records      Query records
   * @param query        Query with the information
   * @param parameterMap Parameter map
   * @return Query output as datalist
   * @throws AWException Datalist generation error
   */
  private DataList fillDataList(List<Global> results, long records, Query query, Map<String, QueryParameter> parameterMap) throws AWException {
    DataListBuilder builder = getBean(DataListBuilder.class);
    boolean paginate = query == null || !query.isPaginationManaged();
    builder.setEnumQueryResult(results)
            .setRecords(records)
            .setPage(parameterMap.get(AweConstants.QUERY_PAGE).getValue().asLong())
            .setMax(parameterMap.get(AweConstants.QUERY_MAX).getValue().asLong())
            .paginate(paginate)
            .generateIdentifiers();

    // If query is defined, fill with query data
    if (query != null) {
      // Add transformations & translations
      builder = processDataList(builder, query, parameterMap);
    }

    // Sort datalist
    builder = sortDataList(builder, parameterMap);

    return builder.build();
  }

  @Override
  public ServiceData subscribe(Query query, ComponentAddress address, ObjectNode parameters) throws AWException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
