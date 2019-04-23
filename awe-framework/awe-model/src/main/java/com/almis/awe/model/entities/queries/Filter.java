/*
 * Package definition
 */
package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * File Imports
 */

/**
 * Filter Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Filters from queries and maintain
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("filter")
public class Filter extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = 4901754004253786690L;

  // Field to compare (left side)
  @XStreamAlias("left-field")
  @XStreamAsAttribute
  private String leftField = null;

  // Table to compare (left side)
  @XStreamAlias("left-table")
  @XStreamAsAttribute
  private String leftTable = null;

  // Concat to compare (left side)
  @XStreamAlias("left-concat")
  @XStreamAsAttribute
  private String leftConcat = null;

  // Value to filter (string at left side) (to be deprecated)
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value = null;

  // Variable to compare (left side)
  @XStreamAlias("left-variable")
  @XStreamAsAttribute
  private String leftVariable = null;

  // Function to field or variable (left side)
  @XStreamAlias("left-function")
  @XStreamAsAttribute
  private String leftFunction = null;

  // Filter condition
  @XStreamAlias("condition")
  @XStreamAsAttribute
  private String condition = null;

  // Variable to compare (right side)
  @XStreamAlias("right-variable")
  @XStreamAsAttribute
  private String rightVariable = null;

  // Field to compare (right side)
  @XStreamAlias("right-field")
  @XStreamAsAttribute
  private String rightField = null;

  // Table to compare (right side)
  @XStreamAlias("right-table")
  @XStreamAsAttribute
  private String rightTable = null;

  // Concat to compare (right side)
  @XStreamAlias("right-concat")
  @XStreamAsAttribute
  private String rightConcat = null;

  // Function to field or variable (right side)
  @XStreamAlias("right-function")
  @XStreamAsAttribute
  private String rightFunction = null;

  // Query identificator to compare (right side)
  @XStreamAlias("query")
  @XStreamAsAttribute
  private String query = null;

  // Type of values to compare
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type = null;

  // Ignore case in both sides (Check with uppercase function)
  @XStreamAlias("ignorecase")
  @XStreamAsAttribute
  private String ignoreCase = null;

  // Trim in both sides (Check with trim function)
  @XStreamAlias("trim")
  @XStreamAsAttribute
  private String trim = null;

  // Optional filter
  @XStreamAlias("optional")
  @XStreamAsAttribute
  private String optional = null;

  /**
   * Default constructor
   */
  public Filter() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Filter(Filter other) throws AWException {
    super(other);
    this.leftField = other.leftField;
    this.leftTable = other.leftTable;
    this.leftConcat = other.leftConcat;
    this.value = other.value;
    this.leftVariable = other.leftVariable;
    this.leftFunction = other.leftFunction;
    this.condition = other.condition;
    this.rightVariable = other.rightVariable;
    this.rightField = other.rightField;
    this.rightTable = other.rightTable;
    this.rightConcat = other.rightConcat;
    this.rightFunction = other.rightFunction;
    this.query = other.query;
    this.type = other.type;
    this.ignoreCase = other.ignoreCase;
    this.trim = other.trim;
    this.optional = other.optional;
  }

  /**
   * Get left field of the filter
   *
   * @return Left field of the filter
   */
  public String getLeftField() {
    return leftField;
  }

  /**
   * Set left field of the filter
   *
   * @param leftField Left field of the filter
   * @return Filter
   */
  public Filter setLeftField(String leftField) {
    this.leftField = leftField;
    return this;
  }

  /**
   * Get left table of the filter
   *
   * @return Left table of the filter
   */
  public String getLeftTable() {
    return leftTable;
  }

  /**
   * Set left table of the filter
   *
   * @param leftTable Left table of the filter
   * @return Filter
   */
  public Filter setLeftTable(String leftTable) {
    this.leftTable = leftTable;
    return this;
  }

  /**
   * Get left concat of the filter
   *
   * @return Left concat of the filter
   */
  public String getLeftConcat() {
    return leftConcat;
  }

  /**
   * Set left concat of the filter
   *
   * @param leftConcat Left concat of the filter
   * @return Filter
   */
  public Filter setLeftConcat(String leftConcat) {
    this.leftConcat = leftConcat;
    return this;
  }

  /**
   * Get left variable of the filter
   *
   * @return Left variable of the filter
   */
  public String getLeftVariable() {
    return leftVariable;
  }

  /**
   * Set left variable of the filter
   *
   * @param leftVariable Left variable of the filter
   * @return Filter
   */
  public Filter setLeftVariable(String leftVariable) {
    this.leftVariable = leftVariable;
    return this;
  }

  /**
   * Get left function of the filter
   *
   * @return Left function of the filter
   */
  public String getLeftFunction() {
    return leftFunction;
  }

  /**
   * Set left function of the filter
   *
   * @param leftFunction Left function of the filter
   * @return Filter
   */
  public Filter setLeftFunction(String leftFunction) {
    this.leftFunction = leftFunction;
    return this;
  }

  /**
   * Get right variable of the filter
   *
   * @return Right variable of the filter
   */
  public String getRightVariable() {
    return rightVariable;
  }

  /**
   * Set right variable of the filter
   *
   * @param rightVariable Right variable of the filter
   * @return Filter
   */
  public Filter setRightVariable(String rightVariable) {
    this.rightVariable = rightVariable;
    return this;
  }

  /**
   * Set right field of the filter
   *
   * @return Right field of the filter
   */
  public String getRightField() {
    return rightField;
  }

  /**
   * Get right field of the filter
   *
   * @param rightField Right field of the filter
   * @return Filter
   */
  public Filter setRightField(String rightField) {
    this.rightField = rightField;
    return this;
  }

  /**
   * Get right table of the filter
   *
   * @return Right table of the filter
   */
  public String getRightTable() {
    return rightTable;
  }

  /**
   * Set right table of the filter
   *
   * @param rightTable Right table of the filter
   * @return Filter
   */
  public Filter setRightTable(String rightTable) {
    this.rightTable = rightTable;
    return this;
  }

  /**
   * Get right concat of the filter
   *
   * @return Right concat of the filter
   */
  public String getRightConcat() {
    return rightConcat;
  }

  /**
   * Set right concat of the filter
   *
   * @param rightConcat Right concat of the filter
   * @return Filter
   */
  public Filter setRightConcat(String rightConcat) {
    this.rightConcat = rightConcat;
    return this;
  }

  /**
   * Get right function of the filter
   *
   * @return Right function of the filter
   */
  public String getRightFunction() {
    return rightFunction;
  }

  /**
   * Set right function of the filter
   *
   * @param rightFunction Right function of the filter
   * @return Filter
   */
  public Filter setRightFunction(String rightFunction) {
    this.rightFunction = rightFunction;
    return this;
  }

  /**
   * Returns the field to filter (left side)
   *
   * @return Field to filter
   * @deprecated Use getLeftField instead
   */
  @Deprecated
  public String getField() {
    return getLeftField();
  }

  /**
   * Stores the field to filter (left side)
   *
   * @param field Field to filter
   * @deprecated Use setLeftField instead
   */
  @Deprecated
  public void setField(String field) {
    setLeftField(field);
  }

  /**
   * Returns the table of the field to filter (left side)
   *
   * @return Table of the field to filter
   * @deprecated Use getLeftTable instead
   */
  @Deprecated
  public String getTable() {
    return getLeftTable();
  }

  /**
   * Stores the table of the field to filter (left side)
   *
   * @param table Table of the field to filter
   * @deprecated Use setLeftTable instead
   */
  @Deprecated
  public void setTable(String table) {
    setLeftTable(table);
  }

  /**
   * Returns the filter value to compare (left side)
   *
   * @return Value to compare
   * @deprecated Use getLeftVariable instead
   */
  @Deprecated
  public String getValue() {
    return value;
  }

  /**
   * Stores the filter value to compare (left side)
   *
   * @param value Value to compare
   * @deprecated Use setLeftVariable instead
   */
  @Deprecated
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Returns the filter condition
   *
   * @return Filter condition
   */
  public String getCondition() {
    return condition;
  }

  /**
   * Stores the filter condition
   *
   * @param condition Filter condition
   * @return this
   */
  public Filter setCondition(String condition) {
    this.condition = condition;
    return this;
  }

  /**
   * Returns the variable identificator to filter (right side)
   *
   * @return Variable identificator
   * @deprecated Use getRightVariable instead
   */
  @Deprecated
  public String getVariable() {
    return getRightVariable();
  }

  /**
   * Stores the variable identificator to filter (right side)
   *
   * @param variable Variable identificator
   * @deprecated Use setRightVariable instead
   */
  @Deprecated
  public void setVariable(String variable) {
    setRightVariable(variable);
  }

  /**
   * Returns the field to filter (right side)
   *
   * @return Field to filter
   * @deprecated Use getRightField instead
   */
  @Deprecated
  public String getCounterfield() {
    return getRightField();
  }

  /**
   * Stores the field to filter (right side)
   *
   * @param counterfield Field to filter
   * @deprecated Use setRightField instead
   */
  @Deprecated
  public void setCounterfield(String counterfield) {
    setRightField(counterfield);
  }

  /**
   * Returns the table of the field to filter (right side)
   *
   * @return Table of the field to filter
   * @deprecated Use getRightTable instead
   */
  @Deprecated
  public String getCountertable() {
    return getRightTable();
  }

  /**
   * Stores the table of the field to filter (right side)
   *
   * @param countertable Table of the field to filter
   * @deprecated Use setRightTable instead
   */
  @Deprecated
  public void setCountertable(String countertable) {
    setRightTable(countertable);
  }

  /**
   * Returns the type of the values to compare
   *
   * @return Type to compare
   */
  public String getType() {
    return type;
  }

  /**
   * Stores the type of the values to compare
   *
   * @param type Type to compare
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Returns if the filter ignores case
   *
   * @return Filter ignores case
   */
  public Boolean isIgnoreCase() {
    return Boolean.parseBoolean(ignoreCase);
  }

  /**
   * Stores if the filter ignores case
   *
   * @param ignoreCase Filter ignores case
   */
  public void setIgnoreCase(String ignoreCase) {
    this.ignoreCase = ignoreCase;
  }

  /**
   * Returns if the filter is trimmed
   *
   * @return Filter is trimmed
   */
  public Boolean isTrim() {
    return Boolean.parseBoolean(trim);
  }

  /**
   * Stores if the filter is trimmed
   *
   * @param trim Filter is trimmed
   */
  public void setTrim(String trim) {
    this.trim = trim;
  }

  /**
   * Returns if filter is optional
   *
   * @return Filter is optional
   */
  public Boolean isOptional() {
    return Boolean.parseBoolean(optional);
  }

  /**
   * Stores if filter is optional
   *
   * @param optional Filter is optional
   */
  public void setOptional(String optional) {
    this.optional = optional;
  }

  /**
   * Returns the filter query identificator to compare (right side)
   *
   * @return Query identificator
   */
  public String getQuery() {
    return query;
  }

  /**
   * Stores the filter query identificator to compare (right side)
   *
   * @param query Query identificator
   */
  public void setQuery(String query) {
    this.query = query;
  }

  @Override
  public String toString() {

    // Generate filter expressions
    String leftExpression = getSideExpression(getLeftField(), getLeftTable(), getLeftVariable(), null, getLeftFunction());
    String conditionExpression = " " + this.getCondition() + " ";
    String rightExpression = getSideExpression(getRightField(), getRightTable(), getRightVariable(), getQuery(), getRightFunction());

    // Add modifiers
    List<String> modifierList = new ArrayList<>();
    if (this.isOptional()) {
      modifierList.add("optional");
    }
    if (this.isIgnoreCase()) {
      modifierList.add("ignorecase");
    }
    if (this.isTrim()) {
      modifierList.add("trim");
    }
    String modifiers = StringUtils.join(modifierList, ", ");
    modifiers = modifiers.isEmpty() ? "" : " <= (" + modifiers + ")";

    // Generate full filter
    return leftExpression + conditionExpression + (rightExpression == null ? "" : rightExpression) + modifiers;
  }

  /**
   * Retrieve a side expression
   *
   * @param field    Expression field
   * @param table    Expression table
   * @param variable Expression variable
   * @param query    Expression query
   * @param function Expression function
   * @return Side expression
   */
  private String getSideExpression(String field, String table, String variable, String query, String function) {
    String fieldTable = (table != null) ? (table + "." + field) : field;
    String expression = (field != null) ? fieldTable : null;
    expression = variable != null ? "variable(" + variable + ")" : expression;
    expression = query != null ? "query(" + query + ")" : expression;
    return function != null ? function.toLowerCase() + "(" + expression + ")" : expression;
  }

  @Override
  public Filter copy() throws AWException {
    return new Filter(this);
  }
}
