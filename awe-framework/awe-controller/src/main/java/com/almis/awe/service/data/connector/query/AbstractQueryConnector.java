package com.almis.awe.service.data.connector.query;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWEQueryException;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.queries.*;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.almis.awe.service.data.builder.QueryBuilder;
import com.almis.awe.service.data.processor.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * AbstractQueryConnector Class
 *
 * Abstract class that all database-related query launcher should extend
 *
 * @author Jorge BELLON 24-02-2017
 */
public abstract class AbstractQueryConnector extends ServiceConfig implements QueryConnector {

  // Autowired services
  private QueryUtil queryUtil;

  /**
   * Autowired constructor
   * @param queryUtil Query utilities
   */
  @Autowired
  public AbstractQueryConnector(QueryUtil queryUtil) {
    this.queryUtil = queryUtil;
  }

  /**
   * Build results
   * @param builder Builder
   * @param query Query launched
   * @return Final output
   * @throws AWException error generating results
   */
  protected ServiceData buildResults(QueryBuilder builder, Query query) throws AWException {
    ServiceData result = null;
    try {
      // Launch query
      result = (ServiceData) builder.setQuery(query).build();
    } catch (Exception exc) {
      throw new AWEQueryException(getLocale("ERROR_TITLE_LAUNCHING_SQL_QUERY"), exc.getMessage(), query.getId(), exc);
    }
    return result;
  }

  /**
   * Generate datalist result
   * @param result Output
   * @param query Query launched
   * @param parameterMap Parameters
   * @return Final output
   * @throws AWException error generating results
   */
  protected ServiceData generateResults(ServiceData result, Query query, Map<String, QueryParameter> parameterMap) throws AWException {
    DataList datalist = null;
    try {
      // Generate datalist
      datalist = fillDataList(result, query, parameterMap);
    } catch (AWException exc) {
      throw exc;
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_ERROR_EXECUTING_SERVICE_QUERY"),
              getLocale("ERROR_MESSAGE_EXECUTING_SERVICE_QUERY", query.getId()), exc);
    }
    result.setDataList(datalist);
    return result;
  }

  /**
   * Retrieves complete data list with totals
   *
   * @param serviceData ServiceData
   * @param query Query
   * @param parameterMap Parameters
   * @return Complete data list with totals
   * @throws AWException Complete list generation error
   */
  protected DataList fillDataList(ServiceData serviceData, Query query, Map<String, QueryParameter> parameterMap) throws AWException {

    // If there's no result, return an empty DataList
    if (serviceData == null) {
      return new DataList();
    }

    // Recover builder's instance
    DataListBuilder builder = getBean(DataListBuilder.class);

    // Define pagination
    builder.paginate(!query.isPaginationManaged());

    // Check output type
    builder.generateIdentifiers();
    DataList serviceDataList = serviceData.getDataList();
    if (serviceDataList != null) {
      if (query.getSqlFieldList() != null) {
        // As datalist
        for (Field field : query.getFieldList()) {
          if (field.getAlias() != null && !field.getId().equalsIgnoreCase(field.getAlias())) {
            DataListUtil.copyColumn(serviceDataList, field.getAlias(), serviceDataList, field.getId());
          }
        }
        builder.setDataList(serviceDataList)
                .setFieldList(query.getSqlFieldList())
                .setMax(parameterMap.get(AweConstants.QUERY_MAX).getValue().asLong())
                .setPage(parameterMap.get(AweConstants.QUERY_PAGE).getValue().asLong())
                .setRecords(serviceDataList.getRecords());
      } else {
        builder.setDataList(serviceDataList)
                .setMax(parameterMap.get(AweConstants.QUERY_MAX).getValue().asLong())
                .setPage(parameterMap.get(AweConstants.QUERY_PAGE).getValue().asLong())
                .setRecords(serviceDataList.getRecords());
      }


    } else {
      // As string array
      builder.setServiceQueryResult((String[]) serviceData.getData())
              .setFieldList(query.getSqlFieldList())
              .setMax(parameterMap.get(AweConstants.QUERY_MAX).getValue().asLong())
              .setPage(parameterMap.get(AweConstants.QUERY_PAGE).getValue().asLong());
    }

    // Add transformations & translations
    builder = processDataList(builder, query, parameterMap);

    // Sort datalist
    builder = sortDataList(builder, parameterMap);

    // Return complete resultset
    return builder.build();
  }

  /**
   * Sort data list
   * 
   * @param builder DataListBuilder
   * @param variables Query variables
   * @return Builder
   * @throws AWException Processing failed
   */
  protected DataListBuilder sortDataList(DataListBuilder builder, Map<String, QueryParameter> variables) throws AWException {
    // Mount sort list if not null
    if (variables.containsKey(AweConstants.QUERY_SORT)) {
      ArrayNode sortList = (ArrayNode) variables.get(AweConstants.QUERY_SORT).getValue();

      // Sort builder
      builder.sort(getQueryUtil().getSortList(sortList));
    }

    return builder;
  }

  /**
   * Process dataList
   * 
   * @param builder DataListBuilder
   * @param query Query
   * @param variables Query variables
   * @return Builder
   * @throws AWException Processing failed
   */
  protected DataListBuilder processDataList(DataListBuilder builder, Query query, Map<String, QueryParameter> variables) throws AWException {
    // Add transformations & translations
    if (query.getSqlFieldList() != null) {
      for (SqlField field : query.getSqlFieldList()) {
        addFieldTransformations(field, builder);
      }
    }

    // Add computed fields
    if (query.getComputedList() != null) {
      for (Computed computed : query.getComputedList()) {
        addComputedTransformations(computed, builder, variables);
      }
    }

    // Add compound fields
    if (query.getCompoundList() != null) {
      for (Compound compound : query.getCompoundList()) {
        CompoundColumnProcessor compoundProcessor = new CompoundColumnProcessor();
        compoundProcessor.setElements(getElements()).setCompound(compound).setVariableMap(variables);
        builder.addCompound(compoundProcessor);
      }
    }

    // Add totalizators
    if (query.getTotalizeList() != null) {
      for (Totalize totalize : query.getTotalizeList()) {
        TotalizeColumnProcessor totalizeProcessor = new TotalizeColumnProcessor();
        totalizeProcessor.setElements(getElements()).setFieldList(query.getSqlFieldList()).setTotalize(totalize);
        builder.addTotalize(totalizeProcessor);
      }
    }

    return builder;
  }

  /**
   * Add field transformations to the builder
   * @param field Field
   * @param builder Builder
   * @throws AWException
   */
  private void addFieldTransformations(OutputField field, DataListBuilder builder) throws AWException {
    // Check transformations
    if (field.isTransform()) {
      TransformCellProcessor transformProcessor = new TransformCellProcessor();
      transformProcessor.setElements(getElements()).setField(field);
      builder.addTransform(transformProcessor);
    }

    // Check translations
    if (field.isTranslate()) {
      TranslateCellProcessor translateProcessor = new TranslateCellProcessor();
      translateProcessor.setElements(getElements()).setField(field);
      builder.addTranslate(translateProcessor);
    }

    // Check no print
    if (field.isNoprint()) {
      builder.addNoPrint(field.getIdentifier());
    }
  }

  /**
   * Add computed transformations to the builder
   * @param computed Computed
   * @param builder Builder
   * @param variables Variables
   * @throws AWException
   */
  private void addComputedTransformations(Computed computed, DataListBuilder builder, Map<String, QueryParameter> variables) throws AWException {
    // Add computed
    ComputedColumnProcessor computedProcessor = new ComputedColumnProcessor();
    computedProcessor.setElements(getElements()).setComputed(computed).setVariableMap(variables);
    builder.addComputed(computedProcessor);

    // Add no print
    if (computed.isNoprint()) {
      builder.addNoPrint(computed.getIdentifier());
    }
  }

  /**
   * Get query util
   * @return Query util
   */
  public QueryUtil getQueryUtil() {
    return queryUtil;
  }
}
