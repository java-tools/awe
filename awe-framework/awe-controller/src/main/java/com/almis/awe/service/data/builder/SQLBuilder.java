package com.almis.awe.service.data.builder;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.queries.*;
import com.almis.awe.model.type.FilterValueType;
import com.almis.awe.model.type.ParameterType;
import com.almis.awe.model.type.UnionType;
import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.*;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Generates sql codes
 */
public abstract class SQLBuilder extends AbstractQueryBuilder {

  private SQLQueryFactory factory;

  private static final String IS_NULL = "isNull";
  private static final String IS_NOT_NULL = "isNotNull";
  private static final String IN = "in";
  private static final String NOT_IN = "notIn";
  private static final String ERROR_TITLE_GENERATING_FILTER = "ERROR_TITLE_GENERATING_FILTER";
  private static final List<String> NULL_CONDITIONS = Arrays.asList((String[]) new String[] { IS_NULL, IS_NOT_NULL });
  private static final List<String> IN_CONDITIONS = Arrays.asList((String[]) new String[] { IN, NOT_IN });

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

    // Field as Subquery
    if (field.getQuery() != null) {
      fieldExpression = getSubquery(field.getQuery());
      // Field as variable
    } else if (field.getVariable() != null) {
      fieldExpression = getVariableExpression(field.getVariable());
      // Field as caseWhen
    } else if (field.getCaseElse() != null) {
      fieldExpression = getCaseExpression(field);
      // Field as concat
    } else if (field.getConcatList() != null) {
      fieldExpression = getConcatExpression(field);
      // Field as value
    } else if (field.getValue() != null) {
      fieldExpression = getStringExpression(field.getValue());
      // Field from function (numeric)
    } else if (field.getFunction() != null) {
      fieldExpression = convertFieldToExpression(field.getTable(), field.getId(), FilterValueType.NUMBER);
      // Field from specifying its table
    } else {
      fieldExpression = buildPath(field.getTable(), field.getId());
    }

    // Add a function to the field if specified
    if (field.getFunction() != null) {
      try {
        fieldExpression = getExpressionFunction(fieldExpression, field.getFunction());
      } catch (Exception exc) {
        throw new AWException(getElements().getLocale("ERROR_TITLE_PARSING_FIELD"),
          getElements().getLocale("ERROR_MESSAGE_PARSING_FIELD", field.toString()), exc);
      }
    }

    // Add an alias to the field if specified
    if (field.getAlias() != null) {
      fieldExpression = Expressions.as(fieldExpression, buildPath(field.getAlias()));
    }

    return fieldExpression;
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
  protected Expression getFieldAliasExpression(Field field, String table) {
    Expression fieldExpression = null;

    if (field.getAlias() != null) {
      fieldExpression = buildPath(table, field.getAlias(), field.getAlias());
    } else {
      fieldExpression = buildPath(table, field.getId(), field.getId());
    }

    return fieldExpression;
  }

  /**
   * Retrieve concat expression
   *
   * @param field Field to concat
   * @return Expression concat expression
   */
  protected Expression getConcatExpression(Field field) {
    StringExpression concatExpression = null;

    if (field.getConcatList() != null) {
      for (Concat concat : field.getConcatList()) {
        StringExpression template = getConcatTemplate(concat);

        if (concatExpression == null) {
          concatExpression = template;
        } else {
          concatExpression = concatExpression.concat(template);
        }
      }
    }

    return concatExpression;
  }

  /**
   * Retrieve concat template
   *
   * @param concat Field to concat
   * @return Expression concat expression
   */
  private StringExpression getConcatTemplate(Concat concat) {
    StringExpression concatExpression = null;

    if (concat.getId() != null) {
      if (concat.getTable() != null) {
        concatExpression = buildPath(concat.getTable()).getString(concat.getId());
      } else {
        concatExpression = Expressions.stringTemplate(concat.getId());
      }
    } else if (concat.getValue() != null) {
      concatExpression = Expressions.stringTemplate("{0}", concat.getValue());
    }

    return concatExpression;
  }

  /**
   * Retrieve case expression
   *
   * @param field Field to apply the case when condition
   * @return Expression caseWhen expression
   */
  protected Expression getCaseExpression(Field field) throws AWException {
    CaseBuilder initialCase = new CaseBuilder();
    CaseBuilder.Cases caseList = null;
    Expression caseElse = getCaseThenExpression(field.getCaseElse());

    if (field.getCaseWhenList() != null) {
      for (CaseWhen caseWhen : field.getCaseWhenList()) {
        BooleanExpression filter = getFilters(caseWhen);
        Expression caseThen = getCaseThenExpression(caseWhen);
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
   * Retrieve caseWhen expression
   *
   * @param caseClause Case clause
   * @return Expression caseWhen expression
   */
  protected Expression getCaseThenExpression(CaseClause caseClause) throws AWException {

    Expression caseThen;

    if (caseClause.getThenField() != null) {
      caseThen = buildPath(caseClause.getThenTable(), caseClause.getThenField());
    } else {
      caseThen = getVariableExpression(caseClause.getThenVariable());
    }

    return caseThen;
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
   * @param field Field
   * @param concat Concat
   * @param table Table
   * @param variable Variable
   * @param query Query
   * @param function Function
   * @return Filter operand
   * @throws AWException Error generating filter
   */
  private Expression getFilterOperand(Filter filter, String field, String table, String concat, String variable, String query, String function) throws AWException {
    Expression operand = null;

    if (variable != null) {
      operand = convertVariableToExpression(filter, variable);
      // If operand is Expressions.TRUE means we don't want to continue with the evaluation of the filter
      if (Expressions.TRUE.equals(operand)) {
        return operand;
      }
    } else if (field != null) {
      FilterValueType type = filter.getType() != null ? FilterValueType.valueOf(filter.getType()) : FilterValueType.STRING;
      operand = convertFieldToExpression(table, field, type);
    } else if (concat != null) {
      operand = convertConcatToExpression(concat);
    } else if (query != null) {
      operand = getSubquery(query);
    }

    // Apply function if defined
    operand = applyFilterFunction(operand, filter, function);

    // If filter is set to ignore cases or trim, trim operands
    if ((filter.isTrim() || filter.isIgnoreCase()) && field != null) {
      operand = operand != null ? ((StringExpression) operand).trim() : null;
    }

    return operand == null ? Expressions.nullExpression() : operand;
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
    Expression functionExpression;

    switch (function.toUpperCase()) {
      case "AVG":
        functionExpression = Expressions.numberOperation(Double.class, Ops.AggOps.AVG_AGG, fieldExpression);
        break;
      case "CNT":
        functionExpression = Expressions.numberOperation(Long.class, Ops.AggOps.COUNT_AGG, fieldExpression);
        break;
      case "MAX":
        functionExpression = Expressions.numberOperation(Long.class, Ops.AggOps.MAX_AGG, fieldExpression);
        break;
      case "MIN":
        functionExpression = Expressions.numberOperation(Long.class, Ops.AggOps.MIN_AGG, fieldExpression);
        break;
      case "TRUNCDATE":
        functionExpression = Expressions.dateOperation(Date.class, Ops.DateTimeOps.DATE, fieldExpression);
        break;
      case "SUM":
      default:
        functionExpression = Expressions.numberOperation(Long.class, Ops.AggOps.SUM_AGG, fieldExpression);
    }
    return functionExpression;
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
    // Final result
    BooleanExpression result = null;

    try {
      // The left side of the comparison has to be either a static value of a
      // column from a table (or concatenation of several)
      Expression firstOperand = getFilterOperand(filter, filter.getLeftField(), filter.getLeftTable(), filter.getLeftConcat(), filter.getLeftVariable(), null, filter.getLeftFunction());
      Expression secondOperand = getFilterOperand(filter, filter.getRightField(), filter.getRightTable(), filter.getRightConcat(), filter.getRightVariable(), filter.getQuery(),
        filter.getRightFunction());

      // If one of the operands return TRUE, it means optional filter with a null value
      if (Expressions.TRUE.equals(firstOperand) || Expressions.TRUE.equals(secondOperand)) {
        return null;
      }

      // Create the condition for the comparison and execute
      String conditionMethod = getFilterConditionMethod(filter.getCondition().toLowerCase(), filter.isIgnoreCase(), secondOperand);

      // Execute the method
      if (NULL_CONDITIONS.contains(conditionMethod)) {
        Method expressionCondition = firstOperand.getClass().getMethod(conditionMethod);
        result = (BooleanExpression) (expressionCondition.invoke(firstOperand));
      } else if (IN_CONDITIONS.contains(conditionMethod) && secondOperand instanceof SQLQuery) {
        Method expressionCondition = firstOperand.getClass().getMethod(conditionMethod, SubQueryExpression.class);
        result = (BooleanExpression) (expressionCondition.invoke(firstOperand, secondOperand));
      } else if (IN_CONDITIONS.contains(conditionMethod)) {
        Method expressionCondition = firstOperand.getClass().getMethod(conditionMethod, Collection.class);
        result = (BooleanExpression) (expressionCondition.invoke(firstOperand, ((SimpleOperation) secondOperand).getArgs()));
      } else {
        Method expressionCondition = firstOperand.getClass().getMethod(conditionMethod, Expression.class);
        result = (BooleanExpression) (expressionCondition.invoke(firstOperand, secondOperand));
      }
      return result;
    } catch (AWException exc) {
      throw exc;
    } catch (Exception exc) {
      throw new AWException(getElements().getLocale(ERROR_TITLE_GENERATING_FILTER),
        getElements().getLocale("ERROR_MESSAGE_GENERATING_FILTER", filter.toString()), exc);
    }
  }

  /**
   * Apply filter function to operand
   *
   * @param operand Operand
   * @param filter Filter
   * @param function Function to apply
   * @return Operand with function
   * @throws AWException Filter function is wrong
   */
  private Expression applyFilterFunction(Expression operand, Filter filter, String function) throws AWException {
    Expression expression = operand;
    if (function != null) {
      try {
        expression = getExpressionFunction(operand, function);
      } catch (Exception exc) {
        throw new AWException(getElements().getLocale(ERROR_TITLE_GENERATING_FILTER),
          getElements().getLocale("ERROR_MESSAGE_GENERATING_FILTER", filter.toString()), exc);
      }
    }
    return expression;
  }

  /**
   * Get filter condition method
   *
   * @param filterCondition Filter condition
   * @param ignoreCase is ignoreCase
   * @param secondOperand Second operand (to check whether is null)
   * @return Filter condition
   */
  private String getFilterConditionMethod(String filterCondition, boolean ignoreCase, Expression secondOperand) {
    String conditionMethod = null;
    boolean inCondition = secondOperand instanceof SimpleOperation || secondOperand instanceof SQLQuery;
    switch (filterCondition) {
      case "ge":
        conditionMethod = "goe";
        break;
      case "le":
        conditionMethod = "loe";
        break;
      case "in":
        if (inCondition) {
          conditionMethod = "in";
        } else {
          conditionMethod = getFilterConditionMethod("eq", ignoreCase, secondOperand);
        }
        break;
      case "not in":
        if (inCondition) {
          conditionMethod = NOT_IN;
        } else {
          conditionMethod = getFilterConditionMethod("ne", ignoreCase, secondOperand);
        }
        break;
      case "is null":
        conditionMethod = IS_NULL;
        break;
      case "is not null":
        conditionMethod = IS_NOT_NULL;
        break;
      case "like":
        if (ignoreCase) {
          conditionMethod = "likeIgnoreCase";
        } else {
          conditionMethod = "like";
        }
        break;
      case "not like":
        conditionMethod = "notLike";
        break;
      case "eq":
        conditionMethod = getVariableConditionMethod(secondOperand, ignoreCase, IS_NULL, "equalsIgnoreCase", "eq");
        break;
      case "ne":
        conditionMethod = getVariableConditionMethod(secondOperand, ignoreCase, IS_NOT_NULL, "notEqualsIgnoreCase", "ne");
        break;
      default:
        conditionMethod = filterCondition;
    }
    return conditionMethod;
  }

  /**
   * Check operand and retrieve condition method
   * @param operand Operand
   * @param nullValue Value if null
   * @param ignoreCaseValue Value if ignorecase
   * @param value Value
   * @return Condition
   */
  private String getVariableConditionMethod(Expression operand, boolean ignoreCase, String nullValue, String ignoreCaseValue, String value) {
    if (operand instanceof NullExpression) {
      return nullValue;
    } else if (ignoreCase) {
      return ignoreCaseValue;
    } else {
      return value;
    }
  }

  /**
   * Generate queryDSL expression from field
   *
   * @param table Field table
   * @param field Field name
   * @param type Filter value type
   * @return Field as expression
   */
  private Expression convertFieldToExpression(String table, String field, FilterValueType type) {
    if (table != null) {
      PathBuilder<Object> path = buildPath(table);
      switch (type) {
        case NUMBER:
          return path.getNumber(field, Long.class);
        case DECIMAL_NUMBER:
          return path.getNumber(field, Double.class);
        case DATE:
          return path.getDate(field, String.class);
        case TIME:
          return path.getTime(field, String.class);
        default:
          return path.getString(field);
      }
    } else {
      switch (type) {
        case NUMBER:
          return Expressions.numberPath(Long.class, field);
        case DECIMAL_NUMBER:
          return Expressions.numberPath(Double.class, field);
        case DATE:
          return Expressions.datePath(Date.class, field);
        case TIME:
          return Expressions.timePath(Date.class, field);
        default:
          return Expressions.stringPath(field);
      }
    }
  }

  /**
   * Generate queryDSL expression from concat
   *
   * @param fieldConcatId Field concat id
   * @return Expression
   */
  private Expression convertConcatToExpression(String fieldConcatId) {
    for (Field fieldConcat : this.getQuery().getFieldList()) {
      if (fieldConcatId.equals(fieldConcat.getId())) {
        return getConcatExpression(fieldConcat);
      }
    }
    return Expressions.ONE;
  }

  /**
   * Generate querydsl expression from variable
   *
   * @param filter Filter
   * @param variableName Variable name
   * @return Filter variable as expression
   * @throws AWException Conversion was wrong
   */
  private Expression convertVariableToExpression(Filter filter, String variableName) throws AWException {
    List<Expression> expressionList = new ArrayList<>();
    Expression expression;
    Variable variable = getQuery().getVariableDefinition(variableName);
    if (variable == null) {
      throw new AWException(getElements().getLocale("ERROR_TITLE_GENERATING_VARIABLE_VALUE"), getElements().getLocale("ERROR_MESSAGE_GENERATING_VARIABLE_VALUE", variableName));
    }

    // Get variable values from previously prepared map
    JsonNode variableValue = variables.get(variableName).getValue();
    boolean isList = variables.get(variableName).isList();
    String value = getVariableAsString(variableValue);

    if (isList && (getQuery().getMultiple() == null || "audit".equalsIgnoreCase(getQuery().getMultiple())) && getVariableIndex() == null) {
      // If variable is a list, generate a IN statement filtering out empty lists. Query should not be multiple
      expression = convertVariableListToExpression(expressionList, variableValue, variable, value, filter);
    } else if (isList) {
      // MULTIPLE query, getting index variable (each line a variable)
      return convertVariableListToExpressionWithIndex(variableValue, variable, value, filter);
    } else if (filter.isOptional() && queryUtil.isEmptyVariable(variableValue)) {
      // OPTIONAL filter and EMPTY variable, removing filter
      return Expressions.TRUE;
    } else if (!filter.isOptional() && queryUtil.isEmptyVariable(variableValue) && ParameterType.STRINGN.toString().equalsIgnoreCase(variable.getType())) {
      // NON OPTIONAL filter and EMPTY variable and STRINGN Type, set IS NULL
      return Expressions.nullExpression();
    } else {
      // EMPTY variable but not optional filter, filling variable as NULL
      return getVariableAsExpression(value, ParameterType.valueOf(variable.getType()));
    }

    // If variable value is empty, don't generate the filter
    boolean variableOrParameterExpressionEmpty = queryUtil.isEmptyString(value) || expressionList.isEmpty();
    if (filter.isOptional() && variableOrParameterExpressionEmpty) {
      return Expressions.TRUE;
    }

    return expression;
  }

  /**
   * Variable list as expression (IN)
   * @param expressionList Expression list
   * @param variableValue Variable value
   * @param variable Variable
   * @param value String value
   * @param filter Filter
   * @return Variable as expression
   * @throws AWException
   */
  private Expression convertVariableListToExpression(List<Expression> expressionList, JsonNode variableValue, Variable variable, String value, Filter filter) throws AWException {
    int variableValuesLength = variableValue.size();
    // If variable has more than 1 element, generate a sequence of (?, ?, ...)
    if (variableValuesLength > 1) {
      // For each value, add a ? to the statement
      for (int i = 0; i < variableValuesLength; i++) {
        String valueAtIndex = getVariableAsString(variableValue.get(i));
        expressionList.add(getVariableAsExpression(valueAtIndex, ParameterType.valueOf(variable.getType())));
      }
      // One value NOT empty (in array)
    } else if (variableValuesLength == 1 && variableValue.isArray() && !(variableValue.get(0).asText().isEmpty())) {
      String valueAtIndex = getVariableAsString(variableValue.get(0));
      expressionList.add(getVariableAsExpression(valueAtIndex, ParameterType.valueOf(variable.getType())));
      // One value NOT empty
    } else if (variableValuesLength == 1 && !(variableValue.asText().isEmpty())) {
      expressionList.add(getVariableAsExpression(value, ParameterType.valueOf(variable.getType())));
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
   * @param variable Variable
   * @param value String value
   * @param filter Filter
   * @return Variable as expression
   * @throws AWException
   */
  private Expression convertVariableListToExpressionWithIndex(JsonNode variableValue, Variable variable, String value, Filter filter) throws AWException {
    // OPTIONAL filter and EMPTY variable, removing filter
    if (filter.isOptional() && queryUtil.isEmptyString(value)) {
      return null;
    } else if (!filter.isOptional() && queryUtil.isEmptyString(value)) {
      return Expressions.nullExpression();
    } else {
      String valueAtIndex = getVariableAsString(variableValue.get(getVariableIndex()));
      return getVariableAsExpression(valueAtIndex, ParameterType.valueOf(variable.getType()));
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
      String sanitizedValue = StringUtil.fixQueryValue(value);
      return Expressions.stringTemplate("'" + sanitizedValue + "'");
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