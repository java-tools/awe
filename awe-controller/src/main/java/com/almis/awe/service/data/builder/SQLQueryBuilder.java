package com.almis.awe.service.data.builder;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.model.dto.SortColumn;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.queries.Field;
import com.almis.awe.model.entities.queries.FilterGroup;
import com.almis.awe.model.entities.queries.GroupBy;
import com.almis.awe.model.entities.queries.Join;
import com.almis.awe.model.entities.queries.OrderBy;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.entities.queries.Table;
import com.almis.awe.model.entities.queries.Union;
import com.almis.awe.model.type.JoinType;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;

/**
 * Generates sql codes
 */
public class SQLQueryBuilder extends SQLBuilder {

  // Query is for count only
  boolean queryIsForCount = false;

  /**
   * Generate the sortlist from component sort
   *
   * @param sortList Component sort
   * @return this
   */
  public SQLQueryBuilder setComponentSort(ArrayNode sortList) {
    super.addComponentSort(sortList);
    return this;
  }

  @Override
  public SQLQueryBuilder setQuery(Query query) {
    super.setQuery(query);
    return this;
  }

  @Override
  public SQLQueryBuilder setFactory(SQLQueryFactory factory) {
    super.setFactory(factory);
    return this;
  }

  @Override
  public SQLQueryBuilder setVariables(Map<String, QueryParameter> variableMap) {
    super.setVariables(variableMap);
    return this;
  }

  @Override
  public SQLQueryBuilder setParameters(ObjectNode parameters) {
    super.setParameters(parameters);
    return this;
  }

  /**
   * Set query for count only
   * @return this
   */
  public SQLQueryBuilder queryForCount() {
    queryIsForCount = true;
    return this;
  }

  /**
   * Builds the SQLQuery
   *
   * @return SQLQuery prepared for fetch
   * @throws AWException Error building query
   */
  public SQLQuery<Tuple> build() throws AWException {
    // Throws exception if elements are not assigned
    if (getElements() == null) {
      throw new NullPointerException("Define elements before building the SQL query");
    }

    // Throws exception if query is not defined
    if (getQuery() == null) {
      throw new NullPointerException(getElements().getLocale("ERROR_TITLE_NOT_DEFINED", "query"));
    }

    // Throws exceptions if factory is not defined
    if (getFactory() == null) {
      throw new NullPointerException(getElements().getLocale("ERROR_TITLE_NOT_DEFINED", "factory"));
    }

    // Prepare query variables
    if (getVariables() == null) {
      setVariables(queryUtil.getVariableMap(getQuery(), getParameters()));
    }

    // Builds the basic query (SELECT FROM)
    SQLQuery<Tuple> finalQuery = getFactory().select(getFields()).from(getTables());

    // Do distinct query
    if (getQuery().isDistinct()) {
      finalQuery.distinct();
    }

    // If JOIN operations were defined, they are added
    if (getQuery().getJoinList() != null) {
      doJoin(finalQuery);
    }

    // If WHERE operations were defined, apply them
    if (getQuery().getFilterGroup() != null) {
      doWhere(finalQuery);
    }

    // If GROUP BY operations were defined, apply
    if (getQuery().getGroupList() != null) {
      doGroupBy(finalQuery);
    }

    // If HAVING operations were defined, apply
    if (getQuery().getHavingGroup() != null) {
      doHaving(finalQuery);
    }

    // If UNION operations were defined, apply them
    SQLQuery<Tuple> finalExpression = finalQuery;
    if (getQuery().getUnionList() != null) {
      finalExpression = doUnion(finalQuery);
    }

    // Do ORDER BY
    if (!queryIsForCount && (getQuery().getOrderByList() != null || componentSortList != null)) {
      doOrderBy(finalExpression);
    }

    return finalExpression;
  }

  /**
   * Forms the Expressions for the SELECT operation
   *
   * @return Expression[] expressions
   * @throws AWException Error retrieving fields
   */
  protected Expression[] getFields() throws AWException {
    List<Field> fieldList = this.getQuery().getFieldList();
    Expression[] fields = new Expression[fieldList.size()];

    // For each field, obtain its expression
    int i = 0;
    for (Field field : fieldList) {
      // Store the expression created
      fields[i] = getFieldExpression(field);
      i++;
    }

    return fields;
  }

  /**
   * Forms the Expressions for the SELECT operation
   *
   * @param table Field table
   * @return Expression[] expressions
   */
  protected Expression[] getFieldsAlias(String table) {
    List<Field> fieldList = this.getQuery().getFieldList();
    Expression[] fields = new Expression[fieldList.size()];

    // For each field, obtain its expression
    int i = 0;
    for (Field field : fieldList) {
      // Store the expression created
      fields[i] = getFieldAliasExpression(field, table);
      i++;
    }

    return fields;
  }

  /**
   * Forms the Expressions for the FROM operation
   *
   * @return Expression[] expressions
   * @throws AWException Error retrieving tables
   */
  protected Expression[] getTables() throws AWException {
    List<Table> tableList = getQuery().getTableList();
    Expression[] tables = new Expression[tableList.size()];

    // For each table, obtain its path
    int i = 0;
    for (Table table : tableList) {
      tables[i] = (Expression) getTableExpression(table, table.getAlias() != null);
      i++;
    }

    return tables;
  }

  /**
   * Adds WHERE operations to the SQLQuery
   *
   * @param finalQuery SQLQuery created in build method
   * @throws AWException Where statement generation error
   */
  protected void doWhere(SQLQuery<Tuple> finalQuery) throws AWException {
    // Obtain the result of applying the expressions contained in the group of filters
    BooleanExpression result = getFilterGroups(this.getQuery().getFilterGroup());

    // Apply the WHERE
    finalQuery.where(result);
  }

  /**
   * Adds JOIN operations to the SQLQuery
   *
   * @param finalQuery SQLQuery created in build method
   * @throws AWException Error generating join
   */
  private void doJoin(SQLQuery<Tuple> finalQuery) throws AWException {
    if (getQuery().getJoinList() != null) {
      // For each join defined, add an operation
      for (Join join : this.getQuery().getJoinList()) {
        addJoinOperation(join, finalQuery);
      }
    }
  }

  /**
   * Adds JOIN operation to the SQLQuery
   *
   * @param join Join
   * @param finalQuery SQLQuery created in build method
   * @throws AWException Error adding join operation
   */
  private void addJoinOperation(Join join, SQLQuery<Tuple> finalQuery) throws AWException {
    // For each table defined in join
    Table table = join.getTable();
    Method expressionCondition = null;
    String joinType = getJoinOperator(join.getJoinType());
    FilterGroup joinFilterGroup = join.getFilterGroup();

    try {
      // If subquery is defined, obtain it and alias if defined
      Object tableExpression = getTableExpression(table, false);

      // Generate the alias
      PathBuilder<Object> alias = buildPath(table.getAlias());

      // Do the join
      Class expressionClass = tableExpression instanceof PathBuilder ? EntityPath.class : SubQueryExpression.class;
      SQLQuery<Tuple> joinQuery;
      if (alias == null) {
        expressionCondition = finalQuery.getClass().getMethod(joinType, expressionClass);
        joinQuery = (SQLQuery<Tuple>) (expressionCondition.invoke(finalQuery, expressionClass.cast(tableExpression)));
      } else {
        expressionCondition = finalQuery.getClass().getMethod(joinType, expressionClass, Path.class);
        joinQuery = (SQLQuery<Tuple>) (expressionCondition.invoke(finalQuery, expressionClass.cast(tableExpression), alias));
      }

      // If filters are defined, add them
      if (joinFilterGroup != null) {
        joinQuery.on(getFilterGroups(joinFilterGroup));
      }
    } catch (Exception exc) {
      throw new AWException(getElements().getLocale("ERROR_TITLE_LAUNCHING_SQL_QUERY"),
              getElements().getLocale("ERROR_MESSAGE_QUERY_JOIN", join.toString(), getQuery().getId()), exc);
    }
  }

  /**
   * Retrieve the join type
   * 
   * @param joinType Join type
   * @return Join operator
   */
  private String getJoinOperator(JoinType joinType) {
    String stringType;
    switch (joinType) {
    case FULL:
      stringType = "fullJoin";
      break;
    case LEFT:
      stringType = "leftJoin";
      break;
    case RIGHT:
      stringType = "rightJoin";
      break;
    default:
      stringType = "innerJoin";
    }
    return stringType;
  }

  /**
   * Adds GROUP BY operations to the SQLQuery
   *
   * @param finalQuery SQLQuery created in build method
   */
  private void doGroupBy(SQLQuery<Tuple> finalQuery) {
    // List of partial Expressions from filters
    PathBuilder<Object>[] groupExpressions = new PathBuilder[this.getQuery().getGroupList().size()];

    int i = 0;
    for (GroupBy groupby : this.getQuery().getGroupList()) {
      // Store the path created
      groupExpressions[i] = buildPath(groupby.getTable(), groupby.getField());
      i++;
    }

    finalQuery.groupBy(groupExpressions);
  }

  /**
   * Adds HAVING operations to the SQLQuery
   *
   * @param finalQuery SQLQuery created in build method
   * @throws AWException Having statement generation error
   */
  private void doHaving(SQLQuery<Tuple> finalQuery) throws AWException {
    // Obtain the result of applying the expressions contained in the group of
    // filters
    BooleanExpression result = getFilterGroups(this.getQuery().getHavingGroup());

    // Apply the HAVING
    finalQuery.having(result);
  }

  /**
   * Adds UNION operations to the SQLQuery
   *
   * @param finalQuery SQLQuery created in build method
   * @return SQLQuery with UNIONs added
   * @throws AWException Union statement generation error
   */
  private SQLQuery<Tuple> doUnion(SQLQuery<Tuple> finalQuery) throws AWException {
    com.querydsl.sql.Union<Tuple> finalUnion = null;
    for (Union union : getQuery().getUnionList()) {
      SQLQuery<Tuple> subquery = getSubquery(union.getQuery());
      SQLQuery<Tuple> dummy = (SQLQuery<Tuple>) getFactory().query();

      if ("ALL".equals(union.getType())) {
        if (finalUnion == null) {
          finalUnion = dummy.unionAll(finalQuery, subquery);
        } else {
          finalUnion = dummy.unionAll(finalUnion, subquery);
        }
      } else {
        if (finalUnion == null) {
          finalUnion = dummy.union(finalQuery, subquery);
        } else {
          finalUnion = dummy.union(finalUnion, subquery);
        }
      }
    }
    String unionAlias = "T";
    return getFactory().select(getFieldsAlias(unionAlias)).from(finalUnion, buildPath(unionAlias));
  }

  /**
   * Adds ORDER BY operations to the SQLQuery
   *
   * @param finalQuery SQLQuery created in build method
   * @throws AWException Order by statement generation error
   */
  private void doOrderBy(SQLQuery<Tuple> finalQuery) throws AWException {
    // Add component sorts
    Set<String> addedSorts = addComponentSorts(finalQuery);

    // Add query sorts
    addQuerySorts(addedSorts, finalQuery);
  }

  /**
   * Add component sorts
   * @param finalQuery Query
   * @return Added sorts
   * @throws AWException Error adding component sorts
   */
  private Set<String> addComponentSorts(SQLQuery<Tuple> finalQuery) throws AWException {
    Set<String> addedSorts = new HashSet<>();

    // Add component sort first
    if (componentSortList != null) {
      for (SortColumn sort : componentSortList) {
        Order order = Order.valueOf(sort.getDirection());
        addedSorts.add(sort.getColumnId());

        // Store
        finalQuery.orderBy(new OrderSpecifier(order, buildPath(sort.getColumnId())));
      }
    }

    return addedSorts;
  }

  /**
   * Add query sorts
   * @param addedSorts Added sorts
   * @param finalQuery Query
   * @throws AWException Error sorting
   */
  private void addQuerySorts(Set<String> addedSorts, SQLQuery<Tuple> finalQuery) throws AWException {
    // Add query sort
    if (getQuery().getOrderByList() != null) {
      for (OrderBy orderBy : getQuery().getOrderByList()) {
        if (!addedSorts.contains(orderBy.getField())) {
          Order order = orderBy.getType() != null && orderBy.getType().equals(Order.DESC.toString()) ? Order.DESC : Order.ASC;

          // Add sort to added sorts
          addedSorts.add(orderBy.getField());
          finalQuery.orderBy(new OrderSpecifier(order, buildPath(orderBy.getTable(), orderBy.getField())));
        }
      }
    }
  }
}