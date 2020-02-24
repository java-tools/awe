package com.almis.awe.template;

import com.querydsl.core.types.Ops;
import com.querydsl.sql.SQLServer2012Templates;

/**
 * Fix to QueryDSL Oracle Templates in diff management
 */
public class FixedSQLServerTemplates extends SQLServer2012Templates {

  /**
   * Constructor
   */
  public FixedSQLServerTemplates() {
    super();
    add(Ops.CONCAT, "concat({0}, {1})");
  }
}
