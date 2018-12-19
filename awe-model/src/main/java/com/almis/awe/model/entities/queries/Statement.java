/*
 * Package definition
 */
package com.almis.awe.model.entities.queries;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Statement Class
 *
 * Used to manage a sql statement
 *
 *
 * @author Pablo GARCIA - 27/APR/2012
 */
public class Statement extends XMLWrapper implements Serializable {

  private static final long serialVersionUID = 4668607967222574481L;
  // Sql statement
  private String sql = "";
  // Statement variable list
  private List<Variable> variableList = new ArrayList<>();

  // Variable index (for multiple statements)
  private Integer index = 0;

  /**
   * Default constructor
   */
  public Statement() {
    this.variableList = new ArrayList<>();
    this.sql = "";
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Statement(Statement other) throws AWException {
    this.sql = other.sql;
    this.index = other.index;
    this.variableList = ListUtil.copyList(other.variableList);
  }

  /**
   * Returns the sql statement
   *
   * @return SQL statement
   */
  public String getSql() {
    return sql;
  }

  /**
   * Adds some sql code to the final sql statement
   *
   * @param sql SQL code
   */
  public void addSql(String sql) {
    this.sql += sql;
  }

  /**
   * Return the variable list
   *
   * @return the variable list
   */
  public List<Variable> getVariableList() {
    return variableList;
  }

  /**
   * Stores the variable list
   *
   * @param variableList the variable list to set
   */
  public void setVariableList(List<Variable> variableList) {
    this.variableList = variableList;
  }

  /**
   * Returns the query index
   *
   * @return Query index
   */
  public Integer getIndex() {
    return index;
  }

  /**
   * Stores the query index
   *
   * @param index Query index
   */
  public void setIndex(Integer index) {
    this.index = index;
  }
}
