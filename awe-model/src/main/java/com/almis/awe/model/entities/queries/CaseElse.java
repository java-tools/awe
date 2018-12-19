package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * CaseElse Class
 *
 * Used to parse the file Queries.xml with XStream
 *
 *
 * Generates a CASE ELSE condition in a query
 *
 *
 * @author Isaac Serna - 13/JUN/2018
 */
@XStreamAlias("case-else")
public class CaseElse extends XMLWrapper implements CaseClause {


  // Optional filter
  @XStreamAlias("then-field")
  @XStreamAsAttribute
  private String thenField = null;

  // Optional filter
  @XStreamAlias("then-table")
  @XStreamAsAttribute
  private String thenTable = null;


  // Optional filter
  @XStreamAlias("then-variable")
  @XStreamAsAttribute
  private String thenVariable = null;

  /**
   * Default constructor
   */
  public CaseElse() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public CaseElse(CaseElse other) {
    this.thenField = other.thenField;
    this.thenTable = other.thenTable;
    this.thenVariable = other.thenVariable;
  }

  /**
   * Get then field table name
   *
   * @return String
   */
  public String getThenTable() {
    return thenTable;
  }

  /**
   * Set then field table name
   *
   * @param thenTable then field table name
   * @return CaseWhen
   */
  public CaseElse setThenTable(String thenTable) {
    this.thenTable = thenTable;
    return this;
  }


  /**
   * Get field if the condition is true
   *
   * @return String
   */
  public String getThenField() {
    return thenField;
  }

  /**
   * Set then field value
   *
   * @param thenField then field value of the condition
   * @return CaseWhen
   */
  public CaseElse setThenField(String thenField) {
    this.thenField = thenField;
    return this;
  }

  /**
   * Get variable if the condition is true
   *
   * @return String
   */
  public String getThenVariable() {
    return thenVariable;
  }

  /**
   * Set then variable value
   *
   * @param thenVariable then variable value of the condition
   * @return CaseWhen
   */
  public CaseElse setThenVariable(String thenVariable) {
    this.thenVariable = thenVariable;
    return this;
  }

  @Override
  public CaseElse copy() throws AWException {
    return new CaseElse(this);
  }
}
