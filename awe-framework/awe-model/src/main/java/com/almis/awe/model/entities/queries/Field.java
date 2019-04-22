/*
 * Package definition
 */
package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Field Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 *
 *
 * Table Fields from queries and maintain
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("field")
public class Field extends OutputField {

  private static final long serialVersionUID = 7587109759292448862L;

  // Field id (database id)
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id = null;

  // Field table
  @XStreamAlias("table")
  @XStreamAsAttribute
  private String table = null;

  // Variable value to set into the id
  @XStreamAlias("variable")
  @XStreamAsAttribute
  private String variable = null;

  // Function to apply to the field
  @XStreamAlias("function")
  @XStreamAsAttribute
  private String function = null;

  // Value to set into the id (for system variables like SYSDATE)
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value = null;

  // Query to be used as field
  @XStreamAlias("query")
  @XStreamAsAttribute
  private String query = null;

  // Defined if field is for audit only
  @XStreamAlias("audit")
  @XStreamAsAttribute
  private String audit = null;
  // Defined if field is a key field
  @XStreamAlias("key")
  @XStreamAsAttribute
  private String key = null;
  // Sequence
  @XStreamAlias("sequence")
  @XStreamAsAttribute
  private String sequence = null;

  // Field concat list
  @XStreamImplicit
  private List<Concat> concatList;

  // Field caseWhen list
  @XStreamImplicit
  private List<CaseWhen> caseWhenList;

  // Field caseElse
  @XStreamAlias("case-else")
  private CaseElse caseElse;

  /**
   * Default constructor
   */
  public Field() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Field(Field other) throws AWException {
    super(other);
    this.id = other.id;
    this.table = other.table;
    this.variable = other.variable;
    this.function = other.function;
    this.value = other.value;
    this.query = other.query;
    this.audit = other.audit;
    this.key = other.key;
    this.sequence = other.sequence;
    this.concatList = ListUtil.copyList(other.concatList);
    this.caseWhenList = ListUtil.copyList(other.caseWhenList);
    this.caseElse = other.caseElse == null ? null : new CaseElse(other.caseElse);
  }

  @Override
  public Field copy() throws AWException {
    return new Field(this);
  }

  /**
   * Returns the field identifier (database field identifier)
   *
   * @return Field identifier
   */
  public String getId() {
    return id;
  }

  /**
   * Stores the field identifier (database field identifier)
   *
   * @param id Field identifier
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns the field table
   *
   * @return Field table
   */
  public String getTable() {
    return table;
  }

  /**
   * Stores the field table
   *
   * @param table Field table
   */
  public void setTable(String table) {
    this.table = table;
  }

  /**
   * Returns the field variable assignation
   *
   * @return Field variable assignation
   */
  public String getVariable() {
    return variable;
  }

  /**
   * Stores the field variable assignation
   *
   * @param variable Field variable assignation
   */
  public void setVariable(String variable) {
    this.variable = variable;
  }

  /**
   * Returns the field function (MAX, MIN, AVG, SUM, COUNT)
   *
   * @return Field function
   */
  public String getFunction() {
    return function;
  }

  /**
   * Stores the field function (MAX, MIN, AVG, SUM, COUNT)
   *
   * @param function Field function
   */
  public void setFunction(String function) {
    this.function = function;
  }

  /**
   * Returns the field value
   *
   * @return Field value
   */
  public String getValue() {
    return value;
  }

  /**
   * Stores the field value
   *
   * @param value Field value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Returns the field query id
   *
   * @return Field query id
   */
  public String getQuery() {
    return query;
  }

  /**
   * Stores the field query id
   *
   * @param query Field query id
   */
  public void setQuery(String query) {
    this.query = query;
  }

  /**
   * Returns if field is for audit only
   *
   * @return Field is for audit only
   */
  public String getAudit() {
    return audit;
  }

  /**
   * Returns if field is for audit only
   *
   * @return Field is for audit only
   */
  public boolean isAudit() {
    return "true".equalsIgnoreCase(audit);
  }

  /**
   * Stores if field is for audit only
   *
   * @param audit Field is for audit only
   */
  public void setAudit(String audit) {
    this.audit = audit;
  }

  /**
   * Returns if field is a key field
   *
   * @return Field is a key field
   */
  public String getKey() {
    return key;
  }

  /**
   * Stores if field is a key field
   *
   * @param key Field is a key field
   */
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * Returns if field is a sequence
   *
   * @return Field is a sequence field
   */
  public String getSequence() {
    return sequence;
  }

  /**
   * Stores if field is a sequence field
   *
   * @param sequence Field is a sequence field
   */
  public void setSequence(String sequence) {
    this.sequence = sequence;
  }

  /**
   * Returns the field concat list
   *
   * @return Concat list
   */
  public List<Concat> getConcatList() {
    return concatList;
  }

  /**
   * Stores the field concat list
   *
   * @param concatList Concat list
   */
  public void setConcatList(List<Concat> concatList) {
    this.concatList = concatList;
  }

  /**
   * Returns the field caseWhen list
   *
   * @return CaseWhen list
   */
  public List<CaseWhen> getCaseWhenList() {
    return caseWhenList;
  }

  /**
   * Stores the field caseWhen list
   *
   * @param caseWhenList CaseWhen list
   */
  public void setCaseWhenList(List<CaseWhen> caseWhenList) {
    this.caseWhenList = caseWhenList;
  }

  /**
   * Returns a CaseElse object
   *
   * @return CaseElse object
   */
  public CaseElse getCaseElse() {
    return caseElse;
  }

  /**
   * Stores the caseElse object
   *
   * @param caseElse CaseElse object
   */
  public void setCaseElse(CaseElse caseElse) {
    this.caseElse = caseElse;
  }

  @Override
  public String toString() {

    String fieldDefinition = "";
    // Field as Subquery
    if (this.getQuery() != null) {

      fieldDefinition = "query(" + this.getQuery() + ")";

    } else if (this.getVariable() != null) {
      // Field as variable
      fieldDefinition = "variable(" + this.getVariable() + ")";
    } else if (this.getValue() != null) {
      // Field as value
      fieldDefinition = this.getValue();
    } else {
      // Standard field
      fieldDefinition = this.getTable() != null ? this.getTable() + "." + this.getId() : this.getId();
    }

    // Generate string representation
    String fieldFunction = this.getFunction() != null ? this.getFunction() + "(" + fieldDefinition + ")" : fieldDefinition;
    String alias = this.getAlias() != null ? " as " + this.getAlias() : "";
    return fieldFunction + alias;
  }
}
