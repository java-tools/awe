package com.almis.awe.service.data.builder;

import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.queries.Query;

import java.util.Map;

/**
 * Query builder interface
 */
public interface QueryBuilder {

  /**
   * Set query
   * @param query Query
   * @return query builder
   */
  QueryBuilder setQuery(Query query);

  /**
   * Retrieve query generated variables
   *
   * @return Query parameters
   * @throws AWException Error retrieving query parameters
   */
  Map<String, QueryParameter> getVariables() throws AWException;

  /**
   * Set variables
   *
   * @param parameterMap Parameter map
   * @return this
   */
  QueryBuilder setVariables(Map<String, QueryParameter> parameterMap);

  /**
   * Build
   * @return Build output
   * @throws AWException error building
   */
  Object build() throws AWException;
}
