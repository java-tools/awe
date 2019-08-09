package com.almis.awe.service.data.builder;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.queries.Constant;
import com.almis.awe.model.entities.queries.Operation;
import com.almis.awe.model.entities.queries.*;
import com.almis.awe.model.type.ParameterType;
import com.almis.awe.model.type.UnionType;
import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;
import com.querydsl.sql.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Generates sql codes
 */
public abstract class SQLBuilder extends AbstractQueryBuilder {

  private SQLQueryFactory factory;
  private static final String ERROR_TITLE_GENERATING_FILTER = "ERROR_TITLE_GENERATING_FILTER";

  /**
   * Autowired constructor
   * @param queryUtil Query utilities
   */
  @Autowired
  public SQLBuilder(QueryUtil queryUtil) {
    super(queryUtil);
  }

  /**
   * Sets the SQLQueryFactory used by QueryDSL to create the SQLQuery
   *
   * @param factory Factory
   * @return this
   */
  public SQLBuilder setFactory(SQLQueryFactory factory) {
    this.factory = factory;
    return this;
  }

  /**
   * Retrieve sql factory
   *
   * @return SQL Factory
   */
  protected SQLQueryFactory getFactory() {
    return factory;
  }

  /**
   * Generates a SQLQuery as a subquery
   *
   * @param queryId Id of the query to generate
   * @return subquery
   * @throws AWException Error retrieving subquery
   */
  protected SQLQuery<Tuple> getSubquery(String queryId) throws AWException {
    SQLQueryBuilder builder = getBean(SQLQueryBuilder.class);
    return builder
      .setQuery(getElements().getQuery(queryId).copy())
      .setFactory(getFactory())
      .setParameters(getParameters())
      .build();
  }

  /**
   * Retrieve field expression
   *
   * @param field
   * @return Expression field expression
   * @throws AWException Error retrieving field expression
   */
  protected Expression getFieldExpression(Field field) throws AWException {
    Expression fieldExpression = null;

    if (field.getQuery() != null) {
      // Field as Subquery
      fieldExpression = getSubquery(field.getQuery());
    } else if (field.getVariable() != null) {
      // Field as variable
      fieldExpression = getVariableExpression(field.getVariable());
    } else {
      // Field from specifying its table
      fieldExpression = buildPath(field.getTable(), field.getId());
    }

    return fieldExpression;
  }

  /**
   * Apply function to field
   * @param field
   * @param fieldExpression
   * @return Field expression with function applied
   * @throws AWException
   */
  private Expression applyFunctionToField(SqlField field, Expression fieldExpression) throws AWException {
    // Apply function to field
    if (field.getFunction() != null) {
      try {
        fieldExpression = getExpressionFunction(fieldExpression, field.getFunction());
      } catch (Exception exc) {
        throw new AWException(getElements().getLocale("ERROR_TITLE_PARSING_FIELD"),
          getElements().getLocale("ERROR_MESSAGE_PARSING_FIELD", field.toString()), exc);
      }
    }

    return fieldExpression;
  }

  /**
   * Apply cast to field
   * @param field
   * @param fieldExpression
   * @return Field expression with function applied
   * @throws AWException
   */
  private Expression applyCastToField(SqlField field, Expression fieldExpression) throws AWException {
    // Apply function to field
    if (field.getCast() != null) {
      try {
        fieldExpression = getExpressionCast(fieldExpression, field.getCast());
      } catch (Exception exc) {
        throw new AWException(getElements().getLocale("ERROR_TITLE_PARSING_FIELD"),
          getElements().getLocale("ERROR_MESSAGE_PARSING_FIELD", field.toString()), exc);
      }
    }

    return fieldExpression;
  }

  /**
   * Retrieve field expression
   *
   * @param field
   * @return Expression field expression
   * @throws AWException Error retrieving field expression
   */
  protected Expression getConstantExpression(Constant field) throws AWException {
    ParameterType type = ParameterType.STRING;
    if (field.getType() != null) {
      type = ParameterType.valueOf(field.getType());
    }
    return getVariableAsExpression(field.getValue(), type);
  }

  /**
   * Retrieve a single variable as an Expression
   *
   * @param variableName
   * @return Expression
   * @throws AWException
   */
  protected Expression getVariableExpression(String variableName) throws AWException {
    Expression variableExpression = null;

    Variable variable = getQuery().getVariableDefinition(variableName);
    if (variable != null) {
      // Get variable values from previously prepared map
      JsonNode variableValue = variables.get(variable.getId()).getValue();
      boolean isList = variables.get(variable.getId()).isList();
      if (isList) {
        throw new AWException(getElements().getLocale("ERROR_TITLE_LAUNCHING_SQL_QUERY"),
          getElements().getLocale("ERROR_MESSAGE_MALFORMED_QUERY_LIST_FIELD", variableName));
      }
      String value = getVariableAsString(variableValue);
      variableExpression = getVariableAsExpression(value, ParameterType.valueOf(variable.getType()));
    }

    return variableExpression;
  }

  /**
   * Retrieve field expression
   *
   * @param field
   * @param table
   * @return Expression field expression
   */
  protected Expression getFieldAliasExpression(SqlField field, String table) {
    Expression fieldExpression = null;

    if (field.getIdentifier() != null) {
      fieldExpression = buildPath(table, field.getIdentifier(), field.getIdentifier());
    }

    return fieldExpression;
  }

  /**
   * Retrieve operation expression
   *
   * @param operation Operation to manage
   * @return Expression concat expression
   */
  protected Expression getOperationExpression(Operation operation) throws AWException {
    Expression operationExpression = null;
    List<Expression> operands = new ArrayList<>();

    if (operation.getOperandList() != null) {
      for (SqlField operand : operation.getOperandList()) {
        operands.add(getOperandExpression(operand));
      }
      operationExpression = generateOperationExpression(operation, operands.toArray(new Expression[0]));
    }

    return operationExpression;
  }

  /**
   * Apply operand into operation
   * @param operation Operation
   * @param operands Operand list
   * @return Operation expression
   */
  protected Expression generateOperationExpression(Operation operation, Expression... operands) {
    Expression substractExpression = Expressions.constant(-1);
    Expression result;
    switch (operation.getOperator()) {
      case "CONCAT":
        result = null;
        for (Expression operand : operands) {
          result = result == null ? operand : Expressions.stringOperation(Ops.CONCAT, result, operand);
        }
        return result;
      case "NULLIF":
        return Expressions.simpleOperation(Object.class, Ops.NULLIF, operands);
      case "COALESCE":
        return new Coalesce(operands);
      case "ADD":
      case "SUB":
      case "MULT":
      case "DIV":
        result = null;
        for (Expression operand : operands) {
          result = result == null ? operand : Expressions.numberOperation(Long.class, Ops.valueOf(operation.getOperator()), result, operand);
        }
        return result;
      case "ADD_SECONDS":
      case "ADD_MINUTES":
      case "ADD_HOURS":
      case "ADD_DAYS":
      case "ADD_WEEKS":
      case "ADD_MONTHS":
      case "ADD_YEARS":
        return Expressions.dateOperation(Date.class, Ops.DateTimeOps.valueOf(operation.getOperator()), operands);
      case "DIFF_SECONDS":
      case "DIFF_MINUTES":
      case "DIFF_HOURS":
      case "DIFF_DAYS":
      case "DIFF_WEEKS":
      case "DIFF_MONTHS":
      case "DIFF_YEARS":
        return Expressions.dateOperation(Integer.class, Ops.DateTimeOps.valueOf(operation.getOperator()), operands);
      case "SUB_SECONDS":
      case "SUB_MINUTES":
      case "SUB_HOURS":
      case "SUB_DAYS":
      case "SUB_WEEKS":
      case "SUB_MONTHS":
      case "SUB_YEARS":
        String operator = operation.getOperator().replace("SUB", "ADD");
        return Expressions.dateOperation(Date.class, Ops.DateTimeOps.valueOf(operator), operands[0], Expressions.numberOperation(Integer.class, Ops.MULT, operands[1], substractExpression));
      default:
        return null;
    }
  }

  /**
   * Retrieve operand template
   *
   * @param operand Operand to retrieve template
   * @return Expression concat expression
   */
  protected Expression getOperandExpression(SqlField operand) throws AWException {
    Expression expression;
    if (operand instanceof Constant) {
      expression = getConstantExpression((Constant) operand);
    } else if (operand instanceof Field) {
      expression = getFieldExpression((Field) operand);
    } else if (operand instanceof Operation) {
      expression = getOperationExpression((Operation) operand);
    } else if (operand instanceof Over) {
      expression = getOverExpression((Over) operand);
    } else if (operand instanceof Case) {
      expression = getCaseExpression((Case) operand);
    } else {
      return null;
    }

    // Apply cast
    Expression castedField = applyCastToField(operand, expression);

    // Apply function
    return applyFunctionToField(operand, castedField);
  }

  /**
   * Retrieve operand template
   *
   * @param field Operand to retrieve template
   * @return Expression concat expression
   */
  protected Expression getSqlFieldExpression(SqlField field) throws AWException {
    Expression expression = getOperandExpression(field);

    // Add an alias to the field if specified
    if (field.getIdentifier() != null) {
      expression = Expressions.as(expression, buildPath(field.getIdentifier()));
    }

    return expression;
  }

  /**
   * Retrieve case expression
   *
   * @param field Field to apply the case when condition
   * @return Expression caseWhen expression
   */
  protected Expression getCaseExpression(Case field) throws AWException {
    CaseBuilder initialCase = new CaseBuilder();
    CaseBuilder.Cases caseList = null;
    Expression caseElse = getOperandExpression(field.getCaseElse());

    if (field.getCaseWhenList() != null) {
      for (CaseWhen caseWhen : field.getCaseWhenList()) {
        BooleanExpression filter = getFilters(caseWhen);
        Expression caseThen = getOperandExpression(caseWhen.getThenOperand());
        if (caseList == null) {
          caseList = initialCase.when(filter).then(caseThen);
        } else {
          caseList.when(filter).then(caseThen);
        }
      }
    }

    return caseList == null ? caseElse : caseList.otherwise(caseElse);
  }

  /**
   * Retrieve over expression
   *
   * @param field Field to apply the case when condition
   * @return Expression caseWhen expression
   */
  protected Expression getOverExpression(Over field) throws AWException {
    WindowOver overField = (WindowOver) getOperandExpression(field.getFieldList().get(0));
    WindowFunction overFunction = overField.over();

    // Add partition by aggregators
    if (field.getPartitionByList() != null) {
      for (PartitionBy partitionBy : field.getPartitionByList()) {
        overFunction = overFunction.partitionBy(buildPath(partitionBy.getTable(), partitionBy.getField()));
      }
    }

    // Add order by sorters
    if (field.getOrderByList() != null) {
      for (OrderBy orderBy : field.getOrderByList()) {
        overFunction = overFunction.orderBy(getOrderByExpression(orderBy));
      }
    }

    return overFunction;
  }

  /**
   * Retrieve order by expression
   * @param orderBy Order by
   * @return Expression
   */
  protected OrderSpecifier getOrderByExpression(OrderBy orderBy) {
    Order order = orderBy.getType() != null ? Order.valueOf(orderBy.getType().toUpperCase()) : Order.ASC;
    OrderSpecifier orderSpecifier = new OrderSpecifier(order, buildPath(orderBy.getTable(), orderBy.getField()));
    if (orderBy.getNulls() != null) {
      if ("FIRST".equalsIgnoreCase(orderBy.getNulls())) {
        orderSpecifier = orderSpecifier.nullsFirst();
      } else {
        orderSpecifier = orderSpecifier.nullsLast();
      }
    }
    return orderSpecifier;
  }

  /**
   * Retrieve table expression
   *
   * @param table Table
   * @return Table expression
   * @throws AWException Error generating table expression
   */
  protected Object getTableExpression(Table table, boolean withAlias) throws AWException {
    // If subquery is defined, obtain it and alias if defined
    if (table.getQuery() != null) {
      SQLQuery<Tuple> subquery = getSubquery(table.getQuery());
      if (withAlias) {
        return subquery.as(buildPath(table.getAlias()));
      } else {
        return subquery;
      }

      // Otherwise, take table name (also schema and alias if defined)
    } else {
      // Check table value
      if (table.getId() == null || table.getId().isEmpty()) {
        throw new AWException("Malformed table expression", "Bad table definition: '" + table.toString() + "'");
      }

      // Generate table path
      return getTable(table, withAlias);
    }
  }

  /**
   * Get table expression with schema
   * @param table Table
   * @return Table expression
   */
  protected RelationalPath getTable(Table table, boolean withAlias) {
    String alias = withAlias && table.getAlias() != null ? table.getAlias() : table.getId();
    return new RelationalPathBase<>(Object.class, alias, table.getSchema(), table.getId());
  }

  /**
   * Retrieve filter operand
   *
   * @param filter Filter
   * @param operand Operand
   * @param fieldId Field id
   * @param table Table
   * @param variable Variable
   * @param query Query
   * @param function Function
   * @return Filter operand
   * @throws AWException Error generating filter
   */
  private Expression getFilterOperand(Filter filter, SqlField operand, String fieldId, String table, String variable, String query, String function) throws AWException {
    Expression operandExpression = null;

    if (operand == null) {
      Field field = (Field) new Field()
        .setVariable(variable)
        .setQuery(query)
        .setFunction(function)
        .setId(fieldId)
        .setTable(table);
      operand = field;
    }

    if (operand instanceof Field && ((Field) operand).getVariable() != null) {
      operandExpression = getOperandVariableExpression(filter, (Field) operand);
      if (operandExpression == null) {
        return null;
      }
    } else if (operand instanceof Constant) {
      operandExpression = getOperandConstantExpression(filter, (Constant) operand);
      if (operandExpression == null) {
        return null;
      }
    } else {
      operandExpression = getOperandExpression(operand);
    }


    // If filter is set to ignore cases or trim, trim operands
    if (filter.isTrim() || filter.isIgnoreCase()) {
      operandExpression = Expressions.stringTemplate("{0}", operandExpression).trim();
    }

    return operandExpression == null ? Expressions.nullExpression() : operandExpression;
  }

  /**
   * Generate variable for parameter
   *
   * @param value Variable value
   * @param type Variable type
   * @return Variable as expression
   * @throws AWException Variable replacement was wrong
   */
  protected Expression getVariableAsExpression(String value, ParameterType type) throws AWException {
    try {
      // Act selecting the variable type
      switch (type) {
        case STRINGL:
          return queryUtil.isEmptyString(value) ? getStringExpression("%") : getStringExpression("%" + value);
        case STRINGR:
          return queryUtil.isEmptyString(value) ? getStringExpression("%") : getStringExpression(value + "%");
        case STRINGB:
          return queryUtil.isEmptyString(value) ? getStringExpression("%%") : getStringExpression("%" + value + "%");
        case SYSTEM_DATE:
          return Expressions.currentDate();
        case SYSTEM_TIME:
          return getStringExpression(DateUtil.dat2WebTime(new Date()));
        case STRING:
        case STRING_HASH_RIPEMD160:
        case STRING_HASH_SHA:
        case STRING_ENCRYPT:
        case STRING_HASH_PBKDF_2_W_HMAC_SHA_1:
          return getVariableAsExpressionOrEmpty(value, type);
        case DATE:
        case TIMESTAMP:
        case FLOAT:
        case DOUBLE:
        case LONG:
        case INTEGER:
        case STRINGN:
        case MULTIPLE_SEQUENCE:
        case SEQUENCE:
        default:
          return getVariableAsExpressionOrNull(value, type);
      }
    } catch (Exception exc) {
      throw new AWException(getElements().getLocale("ERROR_TITLE_REPLACING_VALUES"), getElements().getLocale("ERROR_MESSAGE_REPLACING_VALUES", value), exc);
    }
  }

  /**
   * Generate variable for parameter
   *
   * @param value Variable value
   * @param type Variable type
   * @return Variable as expression
   * @throws AWException Variable replacement was wrong
   */
  protected Expression getVariableAsExpressionOrEmpty(String value, ParameterType type) throws AWException {
    if (queryUtil.isEmptyString(value)) {
      return getStringExpression("");
    }

    // Act selecting the variable type
    switch (type) {
      case STRING_HASH_RIPEMD160:
        return getStringExpression(EncodeUtil.encodeRipEmd160(value));
      case STRING_HASH_SHA:
        return getStringExpression(EncodeUtil.hash(EncodeUtil.HashingAlgorithms.SHA_256, value));
      case STRING_HASH_PBKDF_2_W_HMAC_SHA_1:
        return getStringExpression(EncodeUtil.encodePBKDF2WithHmacSHA1(value));
      case STRING_ENCRYPT:
        return getStringExpression(EncodeUtil.encryptRipEmd160(value));
      case STRING:
      default:
        return getStringExpression(value);
    }
  }

  /**
   * Generate variable for parameter
   *
   * @param value Variable value
   * @param type Variable type
   * @return Variable as expression
   */
  protected Expression getVariableAsExpressionOrNull(String value, ParameterType type) {
    if (queryUtil.isEmptyString(value)) {
      return Expressions.nullExpression();
    }

    // Act selecting the variable type
    switch (type) {
      case DATE:
        return Expressions.asDate(DateUtil.web2Date(value));
      case TIMESTAMP:
        return Expressions.asDateTime(DateUtil.web2Timestamp(value));
      case FLOAT:
        return Expressions.asNumber(Float.valueOf(value));
      case DOUBLE:
        return Expressions.asNumber(Double.valueOf(value));
      case LONG:
        return Expressions.asNumber(Long.valueOf(value));
      case INTEGER:
        return Expressions.asNumber(Integer.valueOf(value));
      case BOOLEAN:
        return Expressions.asBoolean(Boolean.valueOf(value));
      case NULL:
        return Expressions.nullExpression();
      case STRINGN:
      case MULTIPLE_SEQUENCE:
      case SEQUENCE:
      default:
        return getStringExpression(value);
    }
  }

  /**
   * Add function to expression
   *
   * @param fieldExpression Current field expression
   * @param function Function
   * @return Expression
   */
  protected Expression getExpressionFunction(Expression fieldExpression, String function) {
    switch (function.toUpperCase()) {
      case "ABS":
        return Expressions.asNumber(fieldExpression).abs();
      case "AVG":
        return new WindowOver(Double.class, Ops.AggOps.AVG_AGG, fieldExpression);
      case "CNT":
        return new WindowOver(Long.class, Ops.AggOps.COUNT_AGG, fieldExpression);
      case "CNT_DISTINCT":
        return new WindowOver(Long.class, Ops.AggOps.COUNT_DISTINCT_AGG, fieldExpression);
      case "LAG":
        return SQLExpressions.lag(fieldExpression);
      case "MAX":
        return new WindowOver(Long.class, Ops.AggOps.MAX_AGG, fieldExpression);
      case "MIN":
        return new WindowOver(Long.class, Ops.AggOps.MIN_AGG, fieldExpression);
      case "FIRST":
        return SQLExpressions.firstValue(fieldExpression);
      case "LAST":
        return SQLExpressions.lastValue(fieldExpression);
      case "TRUNCDATE":
        return Expressions.dateOperation(Date.class, Ops.DateTimeOps.DATE, fieldExpression);
      case "ROW_NUMBER":
        return SQLExpressions.rowNumber();
      case "SUM":
      default:
        return new WindowOver(Long.class, Ops.AggOps.SUM_AGG, fieldExpression);
    }
  }

  /**
   * Add cast to expression
   *
   * @param fieldExpression Current field expression
   * @param cast Function
   * @return Expression
   */
  protected Expression getExpressionCast(Expression fieldExpression, String cast) {
    switch (cast.toUpperCase()) {
      case "LONG":
        return Expressions.asString(fieldExpression).castToNum(Long.class);
      case "FLOAT":
        return Expressions.asString(fieldExpression).castToNum(Float.class);
      case "DOUBLE":
        return Expressions.asString(fieldExpression).castToNum(Double.class);
      case "INTEGER":
      default:
        return Expressions.asString(fieldExpression).castToNum(Integer.class);
    }
  }

  /**
   * Adds filter groups conditions
   *
   * @param group FilterGroup to add
   * @return BooleanExpression result of filter
   * @throws AWException Filter group statement generation error
   */
  protected BooleanExpression getFilterGroups(FilterGroup group) throws AWException {
    // List of partial Expressions from filters
    List<BooleanExpression> groupExpressions = new ArrayList<>();

    // If this group has groups nested, evaluate them
    if (group.getFilterGroupList() != null) {
      for (FilterGroup filterGroup : group.getFilterGroupList()) {
        groupExpressions.add(getFilterGroups(filterGroup));
      }
    }

    // If this group has filters, add their result to the list
    if (group.getFilterList() != null) {
      for (Filter filter : group.getFilterList()) {
        BooleanExpression filterExpression = getFilters(filter);
        if (filterExpression != null) {
          groupExpressions.add(filterExpression);
        }
      }
    }

    // Evaluate all expressions added to the list
    return getBooleanExpressionResults(groupExpressions, group);
  }

  private BooleanExpression getBooleanExpressionResults(List<BooleanExpression> list, FilterGroup group) {
    BooleanExpression result = null;
    for (BooleanExpression booleanExpression : list) {
      if (result == null) {
        result = booleanExpression;
      } else {
        // To join all expressions AND and OR commands can be used
        if (UnionType.AND.toString().equalsIgnoreCase(group.getUnion())) {
          result = result.and(booleanExpression);
        } else {
          result = result.or(booleanExpression);
        }
      }
    }
    return result;
  }

  /**
   * Evaluates a filter
   *
   * @param filter Filter to be evaluated
   * @return BooleanExpression result of evaluation
   * @throws AWException Filter statement generation error
   */
  private BooleanExpression getFilters(Filter filter) throws AWException {
    try {
      // The left side of the comparison has to be either a static value of a
      // column from a table (or concatenation of several)
      Expression firstOperand = getFilterOperand(filter, filter.getLeftOperand(), filter.getLeftField(), filter.getLeftTable(), filter.getLeftVariable(), null, filter.getLeftFunction());
      Expression secondOperand = getFilterOperand(filter, filter.getRightOperand(), filter.getRightField(), filter.getRightTable(), filter.getRightVariable(), filter.getQuery(), filter.getRightFunction());

      // If one of the operands return TRUE, it means optional filter with a null value
      if (firstOperand == null || secondOperand == null) {
        return null;
      }

      // Create the condition for the comparison and execute
      return applyFilterCondition(filter.getCondition().toLowerCase(), filter.isIgnoreCase(), firstOperand, secondOperand);
    } catch (AWException exc) {
      throw exc;
    } catch (Exception exc) {
      throw new AWException(getElements().getLocale(ERROR_TITLE_GENERATING_FILTER),
        getElements().getLocale("ERROR_MESSAGE_GENERATING_FILTER", filter.toString()), exc);
    }
  }

  /**
   * Get filter condition method
   *
   * @param filterCondition Filter condition
   * @param ignoreCase is ignoreCase
   * @param secondOperand Second operand (to check whether is null)
   * @return Filter condition
   */
  private BooleanOperation applyFilterCondition(String filterCondition, boolean ignoreCase, Expression firstOperand, Expression secondOperand) {
    boolean inCondition = secondOperand instanceof SimpleOperation || secondOperand instanceof SQLQuery;
    switch (filterCondition) {
      case "ge":
        return Expressions.booleanOperation(Ops.GOE, firstOperand, secondOperand);
      case "le":
        return Expressions.booleanOperation(Ops.LOE, firstOperand, secondOperand);
      case "in":
        return getFilterIn(inCondition, firstOperand, secondOperand);
      case "not in":
        return Expressions.booleanOperation(Ops.NOT, getFilterIn(inCondition, firstOperand, secondOperand));
      case "is null":
        return Expressions.booleanOperation(Ops.IS_NULL, firstOperand);
      case "is not null":
        return Expressions.booleanOperation(Ops.IS_NOT_NULL, firstOperand);
      case "like":
        return getFilterLike(ignoreCase, firstOperand, secondOperand);
      case "not like":
        return Expressions.booleanOperation(Ops.NOT, getFilterLike(ignoreCase, firstOperand, secondOperand));
      case "exists":
        return getFilterExists(firstOperand, secondOperand);
      case "not exists":
        return Expressions.booleanOperation(Ops.NOT, getFilterExists(firstOperand, secondOperand));
      case "eq":
        return getFilterEq(ignoreCase, firstOperand, secondOperand);
      case "ne":
        return Expressions.booleanOperation(Ops.NOT, getFilterEq(ignoreCase, firstOperand, secondOperand));
      default:
        return Expressions.booleanOperation(Ops.valueOf(filterCondition.toUpperCase()), firstOperand, secondOperand);
    }
  }

  /**
   * Get filter EQ
   * @param ignoreCase Ignore cases
   * @param firstOperand First operand
   * @param secondOperand Second operand
   * @return Boolean operation
   */
  private BooleanOperation getFilterEq(boolean ignoreCase, Expression firstOperand, Expression secondOperand) {
    if (secondOperand instanceof NullExpression) {
      return Expressions.booleanOperation(Ops.IS_NULL, firstOperand);
    } else if (ignoreCase) {
      return Expressions.booleanOperation(Ops.EQ_IGNORE_CASE, firstOperand, secondOperand);
    } else {
      return Expressions.booleanOperation(Ops.EQ, firstOperand, secondOperand);
    }
  }

  /**
   * Get filter IN
   * @param inCondition In condition
   * @param firstOperand First operand
   * @param secondOperand Second operand
   * @return Boolean operation
   */
  private BooleanOperation getFilterIn(boolean inCondition, Expression firstOperand, Expression secondOperand) {
    if (secondOperand instanceof NullExpression) {
      return Expressions.booleanOperation(Ops.IS_NULL, firstOperand);
    } else if (inCondition) {
      return Expressions.booleanOperation(Ops.IN, firstOperand, secondOperand);
    } else {
      return Expressions.booleanOperation(Ops.EQ, firstOperand, secondOperand);
    }
  }

  /**
   * Get filter LIKE
   * @param ignoreCase Ignore cases
   * @param firstOperand First operand
   * @param secondOperand Second operand
   * @return Boolean operation
   */
  private BooleanOperation getFilterLike(boolean ignoreCase, Expression firstOperand, Expression secondOperand) {
    if (ignoreCase) {
      return Expressions.booleanOperation(Ops.LIKE_IC, firstOperand, secondOperand);
    } else {
      return Expressions.booleanOperation(Ops.LIKE, firstOperand, secondOperand);
    }
  }

  /**
   * Get filter EXISTS
   * @param firstOperand First operand
   * @param secondOperand Second operand
   * @return Boolean operation
   */
  private BooleanOperation getFilterExists(Expression firstOperand, Expression secondOperand) {
    Expression operand = firstOperand instanceof SQLQuery ? firstOperand : secondOperand;
    return Expressions.booleanOperation(Ops.EXISTS, operand);
  }

  /**
   * Generate querydsl expression from variable
   *
   * @param filter Filter
   * @param field Field to
   * @return Filter variable as expression
   * @throws AWException Conversion was wrong
   */
  private Expression getOperandVariableExpression(Filter filter, Field field) throws AWException {
    Variable variable = getQuery().getVariableDefinition(field.getVariable());
    if (variable == null) {
      throw new AWException(getElements().getLocale("ERROR_TITLE_GENERATING_VARIABLE_VALUE"), getElements().getLocale("ERROR_MESSAGE_GENERATING_VARIABLE_VALUE", field.getVariable()));
    }

    // Get variable values from previously prepared map
    ParameterType parameterType = ParameterType.valueOf(variable.getType());
    JsonNode variableValue = variables.get(field.getVariable()).getValue();
    boolean isList = variables.get(field.getVariable()).isList();

    return getOperandFilterExpression(filter, field, isList, parameterType, variableValue, getVariableAsString(variableValue));
  }

  /**
   * Generate querydsl expression from variable
   *
   * @param filter Filter
   * @param field Field to
   * @return Filter variable as expression
   * @throws AWException Conversion was wrong
   */
  private Expression getOperandConstantExpression(Filter filter, Constant field) throws AWException {
    // Get variable values from previously prepared map
    String type = field.getType() == null ? "STRING" : field.getType();
    ParameterType parameterType = ParameterType.valueOf(type);
    JsonNode variableValue = queryUtil.getParameter(field.getValue(), null, parameterType, null, field.getValue().contains(","));

    return getOperandFilterExpression(filter, field, variableValue.isArray(), parameterType, variableValue, field.getValue());
  }

  /**
   * Generate querydsl expression from variable
   *
   * @param filter Filter
   * @param field Field to
   * @return Filter variable as expression
   * @throws AWException Conversion was wrong
   */
  private Expression getOperandFilterExpression(Filter filter, SqlField field, boolean isList, ParameterType type, JsonNode variableValue, String stringValue) throws AWException {
    List<Expression> expressionList = new ArrayList<>();
    Expression expression;

    if (isList && (getQuery().getMultiple() == null || "audit".equalsIgnoreCase(getQuery().getMultiple())) && getVariableIndex() == null) {
      // If variable is a list, generate a IN statement filtering out empty lists. Query should not be multiple
      expression = convertVariableListToExpression(expressionList, variableValue, type, stringValue, filter);
    } else if (isList) {
      // MULTIPLE query, getting index variable (each line a variable)
      return convertVariableListToExpressionWithIndex(variableValue, type, stringValue, filter);
    } else if (filter.isOptional() && queryUtil.isEmptyVariable(variableValue)) {
      // OPTIONAL filter and EMPTY variable, removing filter
      return null;
    } else if (!filter.isOptional() && queryUtil.isEmptyVariable(variableValue) && ParameterType.STRINGN.equals(type)) {
      // NON OPTIONAL filter and EMPTY variable and STRINGN Type, set IS NULL
      return Expressions.nullExpression();
    } else {
      // Retrieve field expression
      return getOperandExpression(field);
    }

    // If variable value is empty, don't generate the filter
    boolean variableOrParameterExpressionEmpty = queryUtil.isEmptyString(stringValue) || expressionList.isEmpty();
    if (filter.isOptional() && variableOrParameterExpressionEmpty) {
      return null;
    }

    return expression;
  }

  /**
   * Variable list as expression (IN)
   * @param expressionList Expression list
   * @param variableValue Variable value
   * @param type Variable type
   * @param value String value
   * @param filter Filter
   * @return Variable as expression
   * @throws AWException
   */
  private Expression convertVariableListToExpression(List<Expression> expressionList, JsonNode variableValue, ParameterType type, String value, Filter filter) throws AWException {
    int variableValuesLength = variableValue.size();
    // If variable has more than 1 element, generate a sequence of (?, ?, ...)
    if (variableValuesLength > 1) {
      // For each value, add a ? to the statement
      for (int i = 0; i < variableValuesLength; i++) {
        String valueAtIndex = getVariableAsString(variableValue.get(i));
        expressionList.add(getVariableAsExpression(valueAtIndex, type));
      }
      // One value NOT empty (in array)
    } else if (variableValuesLength == 1 && variableValue.isArray() && !(variableValue.get(0).asText().isEmpty())) {
      String valueAtIndex = getVariableAsString(variableValue.get(0));
      expressionList.add(getVariableAsExpression(valueAtIndex, type));
      // One value NOT empty
    } else if (variableValuesLength == 1 && !(variableValue.asText().isEmpty())) {
      expressionList.add(getVariableAsExpression(value, type));
      // OPTIONAL in, add nothing
    } else if (filter.isOptional()) {
      return Expressions.TRUE;
      // NON OPTIONAL in, add a check with NULL
    } else {
      throw new AWException(getElements().getLocale(ERROR_TITLE_GENERATING_FILTER),
        getElements().getLocale("ERROR_MESSAGE_EMPTY_LIST_OPTIONAL", filter.toString()));
    }
    return Expressions.list(expressionList.toArray(new SimpleExpression[expressionList.size()]));
  }

  /**
   * Variable list as expression for each index
   * @param variableValue Variable value
   * @param type Variable type
   * @param value String value
   * @param filter Filter
   * @return Variable as expression
   * @throws AWException
   */
  private Expression convertVariableListToExpressionWithIndex(JsonNode variableValue, ParameterType type, String value, Filter filter) throws AWException {
    // OPTIONAL filter and EMPTY variable, removing filter
    if (filter.isOptional() && queryUtil.isEmptyString(value)) {
      return null;
    } else if (!filter.isOptional() && queryUtil.isEmptyString(value)) {
      return Expressions.nullExpression();
    } else {
      String valueAtIndex = getVariableAsString(variableValue.get(getVariableIndex()));
      return getVariableAsExpression(valueAtIndex, type);
    }
  }

  /**
   * Retrieve string expression with quotes
   *
   * @param value String value
   * @return String expression with quotes
   */
  protected Expression getStringExpression(String value) {
    if (value != null) {
      return Expressions.stringTemplate("{0}" , value);
    } else {
      return Expressions.nullExpression();
    }
  }

  /**
   * Build a path with pathbuilder
   * @param parent Parent
   * @param node Node
   * @param alias Alias
   * @return Field path
   */
  protected SimpleExpression<Object> buildPath(String parent, String node, String alias) {
    SimpleExpression<Object> target = buildPath(parent, node);

    // Generate table alias
    if (alias != null) {
      target = target.as(buildPath(alias));
    }

    return target;
  }

  /**
   * Build a path with pathbuilder
   * @param parent Parent node
   * @param node Node
   * @return path
   */
  protected PathBuilder<Object> buildPath(String parent, String node) {
    PathBuilder<Object> target;
    if (parent != null) {
      target = buildPath(parent).get(node);
    } else {
      target = buildPath(node);
    }

    return target;
  }

  /**
   * Build a path with pathbuilder
   * @param node Node
   * @return path
   */
  protected PathBuilder<Object> buildPath(String node) {
    PathBuilder<Object> target = null;
    if (node != null) {
      target = new PathBuilder<>(Object.class, node);
    }
    return target;
  }
}