package com.almis.awe.service.data.builder;

import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.maintain.Insert;
import com.almis.awe.model.entities.maintain.MaintainQuery;
import com.almis.awe.model.entities.queries.Field;
import com.almis.awe.model.entities.queries.Table;
import com.almis.awe.model.entities.queries.Variable;
import com.almis.awe.model.type.MaintainBuildOperation;
import com.almis.awe.model.type.MaintainType;
import com.almis.awe.model.type.ParameterType;
import com.almis.awe.model.util.data.QueryUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.AbstractSQLClause;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.almis.awe.model.type.ParameterType.MULTIPLE_SEQUENCE;
import static com.almis.awe.model.type.ParameterType.SEQUENCE;

/**
 * Generates SQL for maintain operations
 *
 * @author jbellon
 *
 */
public class SQLMaintainBuilder extends SQLBuilder {

  @Value("${awe.database.audit.date:HISdat}")
  private String auditDateField;

  @Value("${awe.database.audit.user:HISope}")
  private String auditUserField;

  @Value("${awe.database.audit.action:HISact}")
  private String auditTypeField;

  @Value("${awe.database.audit.lag:100}")
  private Integer auditLag;

  // Autowired services
  AweSession session;

  private AbstractSQLClause<?> previousQuery;
  private boolean audit = false;
  private MaintainBuildOperation operation;
  private static final String ERROR_TITLE_NOT_DEFINED = "ERROR_TITLE_NOT_DEFINED";
  private static final String ERROR_TITLE_LAUNCHING_MAINTAIN = "ERROR_TITLE_LAUNCHING_MAINTAIN";

  /**
   * Autowired constructor
   * @param session Session
   * @param queryUtil Query utilities
   */
  @Autowired
  public SQLMaintainBuilder(AweSession session, QueryUtil queryUtil) {
    super(queryUtil);
    this.session = session;
  }

  /**
   * Sets the Query created from XML
   *
   * @param maintain Maintain query
   * @return this
   */
  public SQLMaintainBuilder setMaintain(MaintainQuery maintain) {
    this.setQuery(maintain);

    return this;
  }

  /**
   * Sets whether if this maintain it's the audit operation or not
   *
   * @param audit Audit
   * @return this
   */
  public SQLMaintainBuilder setAudit(boolean audit) {
    this.audit = audit;

    return this;
  }

  /**
   * Sets whether if this maintain should be treated as batch
   *
   * @param operation Maintain operation
   * @return this
   */
  public SQLMaintainBuilder setOperation(MaintainBuildOperation operation) {
    this.operation = operation;

    return this;
  }

  /**
   * Sets already batched queries so this query can be added
   *
   * @param previousQuery Previous query
   * @return this
   */
  public SQLMaintainBuilder setPreviousQuery(AbstractSQLClause<?> previousQuery) {
    this.previousQuery = previousQuery;

    return this;
  }

  /**
   * Sets whether if this maintain it's the audit operation or not
   *
   * @param variableIndex Variable index
   * @return this
   */
  public SQLMaintainBuilder setVariableIndex(Integer variableIndex) {
    this.variableIndex = variableIndex;

    return this;
  }

  @Override
  public SQLMaintainBuilder setFactory(SQLQueryFactory factory) {
    super.setFactory(factory);
    return this;
  }

  @Override
  public SQLMaintainBuilder setVariables(Map<String, QueryParameter> parameterMap) {
    super.setVariables(parameterMap);
    return this;
  }

  @Override
  public SQLMaintainBuilder setParameters(ObjectNode parameterMap) {
    super.setParameters(parameterMap);
    return this;
  }

  private void validateBuilder() throws AWException {
    // Throws exception if elements are not assigned
    if (getElements() == null) {
      throw new AWException("Define elements before building the SQL query");
    }

    // Throws exception if query is not defined
    if (getQuery() == null) {
      throw new AWException(getElements().getLocale(ERROR_TITLE_NOT_DEFINED, "query"));
    }

    // Throws exceptions if factory is not defined
    if (getFactory() == null) {
      throw new AWException(getElements().getLocale(ERROR_TITLE_NOT_DEFINED, "factory"));
    }

    if (operation == null) {
      throw new AWException(getElements().getLocale(ERROR_TITLE_NOT_DEFINED, "operation"));
    }

    if (operation == MaintainBuildOperation.BATCH_INCREASING_ELEMENTS && previousQuery == null) {
      throw new AWException(getElements().getLocale(ERROR_TITLE_NOT_DEFINED, "previousQuery"));
    }
  }

  /**
   * Builds the SQLQuery
   *
   * @return SQLQuery prepared for fetch
   * @throws AWException Error building maintain
   */
  public AbstractSQLClause<?> build() throws AWException {
    // Validate builder is filled up
    validateBuilder();

    // Prepare query variables
    queryUtil.addToVariableMap(getVariables(), getQuery());
    AbstractSQLClause<?> finalQuery;

    if (this.audit) {
      switch (operation) {
      case BATCH_INITIAL_DEFINITION:
        return getFactory().insert(new RelationalPathBase<>(Object.class, "", "", ((MaintainQuery) getQuery()).getAuditTable()));
      case BATCH_INCREASING_ELEMENTS:
        finalQuery = previousQuery;
        break;
      default:
        finalQuery = getFactory().insert(new RelationalPathBase<>(Object.class, "", "", ((MaintainQuery) getQuery()).getAuditTable()));
        break;
      }
      SQLInsertClause auditInsertClause = (SQLInsertClause) finalQuery;
      auditInsertClause.columns(getAuditFieldPaths());
      List<Expression> auditFieldValues = getAuditFieldValues(variableIndex != null ? variableIndex : 0);
      for (Expression value : auditFieldValues) {
        auditInsertClause.values(value);
      }
    } else {
      // Builds the basic query
      RelationalPath<?> tablePath = getTable();

      switch (operation) {
      case BATCH_INITIAL_DEFINITION:
        return buildOperation(tablePath);
      case BATCH_INCREASING_ELEMENTS:
        finalQuery = previousQuery;
        break;
      default:
        finalQuery = buildOperation(tablePath);
        break;
      }

      MaintainQuery maintainQuery = (MaintainQuery) this.getQuery();
      switch (maintainQuery.getMaintainType()) {
        case INSERT:
          doInsert((SQLInsertClause) finalQuery);
          break;
        case UPDATE:
          doUpdate((SQLUpdateClause) finalQuery);
          break;
        case DELETE:
          doDelete((SQLDeleteClause) finalQuery);
          break;
        default:
      }
    }

    return finalQuery;
  }

  /**
   * Manage insert clause
   * @param insertClause Insert clause
   * @throws AWException
   */
  private void doInsert(SQLInsertClause insertClause) throws AWException {
    List<Path> fieldPaths = getFieldPaths();
    insertClause.columns(fieldPaths.toArray(new Path[0]));
    if (((Insert) this.getQuery()).getQuery() != null) {
      insertClause.select(getSubquery(((Insert) this.getQuery()).getQuery()));
    } else {
      List<Expression> fieldValues = getFieldValues();
      for (Expression value : fieldValues) {
        insertClause.values(value);
      }
    }
  }

  /**
   * Manage update clause
   * @param updateClause Update clause
   * @throws AWException
   */
  private void doUpdate(SQLUpdateClause updateClause) throws AWException {
    // If WHERE operations were defined, apply them
    if (getQuery().getFilterGroup() != null) {
      updateClause.where(getFilterExpression());
    }

    // Parse field definitions and values and apply them to be updated
    updateClause.set(getFieldPaths(), getFieldValues());
  }

  /**
   * Manage delete clause
   * @param deleteClause Delete clause
   * @throws AWException
   */
  private void doDelete(SQLDeleteClause deleteClause) throws AWException {
    // If WHERE operations were defined, apply them
    if (getQuery().getFilterGroup() != null) {
      deleteClause.where(getFilterExpression());
    }
  }

  /**
   * Generates the table path for the maintain operation
   *
   * @return table Path
   * @throws AWException Error retrieving table
   */
  private RelationalPath<?> getTable() throws AWException {
    if (this.getQuery().getTableList().isEmpty()) {
      throw new AWException(getElements().getLocale(ERROR_TITLE_LAUNCHING_MAINTAIN), getElements().getLocale("ERROR_MESSAGE_NOT_DEFINED_IN", "table", this.getQuery().getId() ));
    }
    Table table = this.getQuery().getTableList().get(0);
    return new RelationalPathBase<>(Object.class, "", table.getSchema(), table.getId());
  }

  /**
   * Retrieves and updates the value for the sequence
   *
   * @return value
   * @throws AWException Error retrieving sequence value
   */
  private String getSequence(String sequence) throws AWException {

    // SELECT KeyVal FROM AweKey WHERE KeyNam = ?

    SQLQuery<Long> getKey = getFactory().select(Expressions.numberPath(Long.class, "KeyVal")).from(buildPath("AweKey")).where(Expressions.stringPath("KeyNam").eq(sequence));
    List<Long> idsStored = getKey.fetch();
    if (idsStored.size() != 1) {
      throw new AWException(getElements().getLocale(ERROR_TITLE_LAUNCHING_MAINTAIN),
              getElements().getLocale("ERROR_MESSAGE_SEQUENCE_NOT_DEFINED", sequence));
    }
    Long id = idsStored.get(0);

    // UPDATE AweKey SET KeyVal = KeyVal + 1 WHERE KeyNam = ?

    SQLUpdateClause updateKey = getFactory()
            .update(new RelationalPathBase<>(Object.class, "", "", "AweKey"))
            .set(Expressions.numberPath(Long.class, "KeyVal"), id + 1)
            .where(Expressions.stringPath("KeyNam").eq(sequence));
    Long rowsAffected = updateKey.execute();
    if (rowsAffected != 1) {
      throw new AWException(getElements().getLocale(ERROR_TITLE_LAUNCHING_MAINTAIN),
              getElements().getLocale("ERROR_MESSAGE_SEQUENCE_NOT_UPDATED", sequence));
    }

    return String.valueOf(id + 1);
  }

  /**
   * Creates the basic operation depending on maintain's type
   *
   * @param tablePath Table path
   * @return sqlClause
   * @throws AWException Error building operation
   */
  private AbstractSQLClause<?> buildOperation(RelationalPath<?> tablePath) throws AWException {
    MaintainType type = ((MaintainQuery) this.getQuery()).getMaintainType();
    switch (type) {
    case DELETE:
      return getFactory().delete(tablePath);
    case INSERT:
      return getFactory().insert(tablePath);
    case UPDATE:
      return getFactory().update(tablePath);
    default:
      throw new AWException(MessageFormat.format("Operation not implemented yet: {0}", type));
    }
  }

  /**
   * Retrieves the list of paths defined by fields
   *
   * @return path list
   */
  private List getFieldPaths() {
    List paths = new ArrayList<>();

    for (Field field : this.getQuery().getFieldList()) {
      if (!"true".equals(field.getAudit())) {
        paths.add(buildPath(field.getTable(), field.getId(), field.getAlias()));
      }
    }

    return paths;
  }

  /**
   * Retrieves the list of paths defined by audit fields
   *
   * @return path list
   * @throws AWException Error retrieving audit field path
   */
  private Path[] getAuditFieldPaths() throws AWException {
    List<Path> paths = new ArrayList<>();

    // Check if there are field list
    if (this.getQuery().getFieldList() == null) {
      throw new AWException(getElements().getLocale("ERROR_TITLE_NO_AUDIT_FIELDS"),
              getElements().getLocale("ERROR_MESSAGE_NO_AUDIT_FIELDS", this.getQuery().getId()));
    }

    paths.add(buildPath(auditUserField));
    paths.add(buildPath(auditDateField));
    paths.add(buildPath(auditTypeField));

    for (Field field : this.getQuery().getFieldList()) {
      if (field.isAudit()) {
        paths.add((Path) buildPath(field.getTable(), field.getId(), field.getAlias()));
      }
    }

    return paths.toArray(new Path[paths.size()]);
  }

  /**
   * Retrieves the list of values defined by fields
   *
   * @return values list
   * @throws AWException Error retrieving field values
   */
  private List<Expression> getFieldValues() throws AWException {
    List<Expression> values = new ArrayList<>();

    for (Field field : getQuery().getFieldList()) {
      if (!field.isAudit()) {
        // Field as sequence
        if (field.getSequence() != null) {
          values.add(calculateSequence(field, this.getVariableIndex()));
          // Get field value
        } else {
          values.add(getFieldValue(field, this.getVariableIndex()));
        }
      }
    }

    return values;
  }



  /**
   * Retrieves the list of values defined by audit fields
   *
   * @return values list
   * @throws AWException Error retrieving audit field values
   */
  private List<Expression> getAuditFieldValues(int num) throws AWException {
    List<Expression> values = new ArrayList<>();

    // Create variable with Audit date (in milliseconds)
    long currentTime = new Date().getTime();

    // Add milliseconds to current time plus number
    currentTime = currentTime + (num * 1000) / auditLag;
    Timestamp dateAudit = new Timestamp(currentTime);

    // Audit variables
    values.add(getStringExpression(session.getUser()));
    values.add(Expressions.asDateTime(dateAudit));
    values.add(getStringExpression(((MaintainQuery) this.getQuery()).getMaintainType().toString()));

    for (Field field : this.getQuery().getFieldList()) {
      if ("true".equals(field.getAudit())) {
        values.add(getFieldValue(field, num));
      }
    }

    return values;
  }

  /**
   * Define sequence variable
   * @param field Field
   * @return sequence identifier
   * @throws AWException Error retrieving variable value
   */
  private String defineSequenceVariable(Field field) throws AWException {
    String sequenceIdentifier = field.getVariable() == null ? field.getId() : field.getVariable();
    if (getQuery().getVariableDefinition(sequenceIdentifier) == null) {
      Variable seqVariable = new Variable();
      QueryParameter parameter;
      seqVariable.setName(sequenceIdentifier);
      seqVariable.setId(sequenceIdentifier);
      // Type can only be MULTIPLE_SEQUENCE or SEQUENCE
      seqVariable.setType("true".equalsIgnoreCase(getQuery().getMultiple()) ? MULTIPLE_SEQUENCE.toString() : SEQUENCE.toString());
      // If query is not multiple, sequence value is not going to change, we can calculate it
      if (ParameterType.valueOf(seqVariable.getType()) == SEQUENCE) {
        parameter = new QueryParameter(JsonNodeFactory.instance.numberNode(Long.parseLong(getSequence(field.getSequence()))), false, SEQUENCE);
      } else {
        parameter = new QueryParameter(JsonNodeFactory.instance.arrayNode(), true, MULTIPLE_SEQUENCE);
      }
      // We add the variable to the query's variable list
      if (getQuery().getVariableDefinitionList() == null) {
        getQuery().setVariableDefinitionList(new ArrayList<Variable>());
      }
      getQuery().getVariableDefinitionList().add(seqVariable);
      variables.put(sequenceIdentifier, parameter);
    }
    return sequenceIdentifier;
  }

  /**
   * Calculate sequence
   * @param field Field
   * @param index Variable index
   * @return sequence expression
   * @throws AWException Error retrieving variable value
   */
  private Expression calculateSequence(Field field, Integer index) throws AWException {
    Expression fieldValue = null;

    // If variable is not defined in query, we create a new one
    String sequenceIdentifier = defineSequenceVariable(field);

    Variable seqVariable = getQuery().getVariableDefinition(sequenceIdentifier);
    ParameterType parameterType = ParameterType.valueOf(seqVariable.getType());
    String sequenceVariableValue = seqVariable.getValue();
    // If type is not MULTIPLE_SEQUENCE or SEQUENCE, we change it accordingly
    if (parameterType != MULTIPLE_SEQUENCE && parameterType != SEQUENCE) {
      seqVariable.setType("true".equalsIgnoreCase(getQuery().getMultiple()) ? MULTIPLE_SEQUENCE.toString() : SEQUENCE.toString());
      parameterType = ParameterType.valueOf(seqVariable.getType());
    }
    // If type is MULTIPLE_SEQUENCE, we calculate a new value; otherwise, we use the one assigned to the variable
    if (parameterType == MULTIPLE_SEQUENCE) {
      String value = getSequence(field.getSequence());
      fieldValue = getVariableAsExpression(value, ParameterType.LONG);

      // We add the value to the map of parsed variables if needed
      if (variables.get(sequenceIdentifier).getValue().size() == (index + 1)) {
        ((ArrayNode) (variables.get(sequenceIdentifier).getValue())).add(value);
      }
      // IF type is SEQUENCE but the value is empty, generate sequence
    } else if (parameterType == SEQUENCE && queryUtil.isEmptyString(sequenceVariableValue)) {
      String value = getSequence(field.getSequence());
      fieldValue = getVariableAsExpression(value, ParameterType.LONG);
      variables.put(sequenceIdentifier, new QueryParameter(JsonNodeFactory.instance.textNode(value), false, parameterType));
    } else {
      fieldValue = getStringExpression(sequenceVariableValue);
    }

    return fieldValue;
  }

  /**
   * Retrieve field value
   * @param field Field
   * @param index Variable index
   * @return field value expression
   * @throws AWException Error retrieving variable value
   */
  private Expression getFieldValue(Field field, Integer index) throws AWException {
    Expression fieldValue = null;
    if (field.getVariable() != null) {
      Variable variable = getQuery().getVariableDefinition(field.getVariable());
      if (variable != null) {
        // Get variable values from previously prepared map
        JsonNode variableValue = variables.get(variable.getId()).getValue();
        boolean isList = variables.get(variable.getId()).isList();
        if (variable.getValue() != null) {
          fieldValue = getVariableAsExpression(variable.getValue(), ParameterType.valueOf(variable.getType()));
        } else {
          if (isList) {
            fieldValue = getVariableAsExpression(getVariableAsString(variableValue.get(index)), ParameterType.valueOf(variable.getType()));
          } else {
            fieldValue = getVariableAsExpression(getVariableAsString(variableValue), ParameterType.valueOf(variable.getType()));
          }
        }
      }
      // Field as value
    } else if (field.getValue() != null) {
      fieldValue = getStringExpression(field.getValue());
      // Field as subquery
    } else if (field.getQuery() != null) {
      // TODO: Implement subqueries in maintain fields
      throw new AWException("Using subqueries in maintain fields is not supported yet");
    }

    return fieldValue;
  }

  /**
   * Retrieves the expression for the filters
   * 
   * @return booleanExpression
   * @throws AWException Error retrieving filter expression
   */
  private BooleanExpression getFilterExpression() throws AWException {
    // Obtain the result of applying the expressions contained in the group of filters
    return getFilterGroups(this.getQuery().getFilterGroup());
  }
}
