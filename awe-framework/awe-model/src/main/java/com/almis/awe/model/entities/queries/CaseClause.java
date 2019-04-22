package com.almis.awe.model.entities.queries;

import com.almis.awe.model.entities.Copyable;

public interface CaseClause extends Copyable {

  /**
   * Get then field table name
   *
   * @return String
   */
  String getThenTable();

  /**
   * Get field if the condition is true
   *
   * @return String
   */
  String getThenField();

  /**
   * Get variable if the condition is true
   *
   * @return String
   */
  String getThenVariable();
}
