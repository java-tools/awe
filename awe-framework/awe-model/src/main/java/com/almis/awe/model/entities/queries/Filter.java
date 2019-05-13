package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Filters from queries and maintain
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("filter")
public class Filter implements Copyable {

  private static final long serialVersionUID = 4901754004253786690L;

  // Field to compare (left side)
  @XStreamAlias("left-field")
  @XStreamAsAttribute
  private String leftField;

  // Table to compare (left side)
  @XStreamAlias("left-table")
  @XStreamAsAttribute
  private String leftTable;

  // Concat to compare (left side)
  @XStreamAlias("left-concat")
  @XStreamAsAttribute
  private String leftConcat;

  // Value to filter (string at left side) (to be deprecated)
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  // Variable to compare (left side)
  @XStreamAlias("left-variable")
  @XStreamAsAttribute
  private String leftVariable;

  // Function to field or variable (left side)
  @XStreamAlias("left-function")
  @XStreamAsAttribute
  private String leftFunction;

  // Filter condition
  @XStreamAlias("condition")
  @XStreamAsAttribute
  private String condition;

  // Variable to compare (right side)
  @XStreamAlias("right-variable")
  @XStreamAsAttribute
  private String rightVariable;

  // Field to compare (right side)
  @XStreamAlias("right-field")
  @XStreamAsAttribute
  private String rightField;

  // Table to compare (right side)
  @XStreamAlias("right-table")
  @XStreamAsAttribute
  private String rightTable;

  // Concat to compare (right side)
  @XStreamAlias("right-concat")
  @XStreamAsAttribute
  private String rightConcat;

  // Function to field or variable (right side)
  @XStreamAlias("right-function")
  @XStreamAsAttribute
  private String rightFunction;

  // Query identificator to compare (right side)
  @XStreamAlias("query")
  @XStreamAsAttribute
  private String query;

  // Type of values to compare
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  // Ignore case in both sides (Check with uppercase function)
  @XStreamAlias("ignorecase")
  @XStreamAsAttribute
  private Boolean ignoreCase;

  // Trim in both sides (Check with trim function)
  @XStreamAlias("trim")
  @XStreamAsAttribute
  private Boolean trim;

  // Optional filter
  @XStreamAlias("optional")
  @XStreamAsAttribute
  private Boolean optional;

  /**
   * Returns if optional
   * @return Is optional
   */
  public boolean isOptional() {
    return optional != null && optional;
  }

  /**
   * Returns if trim
   * @return Is trim
   */
  public boolean isTrim() {
    return trim != null && trim;
  }

  /**
   * Returns if ignoreCase
   * @return Is ignoreCase
   */
  public boolean isIgnoreCase() {
    return ignoreCase != null && ignoreCase;
  }

  @Override
  public String toString() {

    // Generate filter expressions
    StringBuilder builder = new StringBuilder();
    builder.append(getSideExpression(getLeftField(), getLeftTable(), getLeftVariable(), null, getLeftFunction()));
    builder.append(" ").append(getCondition()).append(" ");
    String rightExpression = getSideExpression(getRightField(), getRightTable(), getRightVariable(), getQuery(), getRightFunction());
    builder.append(rightExpression == null ? "" : rightExpression);

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
    builder.append(modifiers.isEmpty() ? "" : " <= (" + modifiers + ")");

    // Generate full filter
    return builder.toString();
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
    return this.toBuilder().build();
  }
}
