package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * CaseWhen Class
 *
 * Used to parse the file Queries.xml with XStream
 * Generates a CASE WHEN condition in a query
 *
 * @author Isaac Serna - 13/JUN/2018
 */
@XStreamAlias("case-when")
public class CaseWhen extends Filter implements CaseClause {

  private static final long serialVersionUID = 6575179747084476685L;

  // Optional filter
  @XStreamAlias("then-field")
  @XStreamAsAttribute
  private String thenField;

  // Optional filter
  @XStreamAlias("then-table")
  @XStreamAsAttribute
  private String thenTable;


  // Optional filter
  @XStreamAlias("then-variable")
  @XStreamAsAttribute
  private String thenVariable;

  /**
   * Copy constructor
   *
   * @param other
   */
  public CaseWhen(CaseWhen other) throws AWException {
    super(other);
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
  public CaseWhen setThenTable(String thenTable) {
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
  public CaseWhen setThenField(String thenField) {
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
  public CaseWhen setThenVariable(String thenVariable) {
    this.thenVariable = thenVariable;
    return this;
  }

  @Override
  public CaseWhen copy() throws AWException {
    return new CaseWhen(this);
  }
}
