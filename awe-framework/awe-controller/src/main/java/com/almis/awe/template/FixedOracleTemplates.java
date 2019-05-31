package com.almis.awe.template;

import com.querydsl.core.types.Ops;
import com.querydsl.sql.OracleTemplates;

/**
 * Fix to QueryDSL Oracle Templates in diff management
 */
public class FixedOracleTemplates extends OracleTemplates {

  /**
   * Constructor
   */
  public FixedOracleTemplates() {
    super();
    // querydsl-sql library (v3.6.2) has a wrong datediff template for oracle, so we redefine it:
    add(Ops.DateTimeOps.DIFF_WEEKS, "round(extract(day from ({1} - {0}) / 7))");
    add(Ops.DateTimeOps.DIFF_DAYS, "extract(day from {1} - {0})");
    add(Ops.DateTimeOps.DIFF_HOURS, "extract(day from ({1} - {0}) * 24)");
    add(Ops.DateTimeOps.DIFF_MINUTES, "extract(day from ({1} - {0}) * 1440)");
    add(Ops.DateTimeOps.DIFF_SECONDS, "extract(day from ({1} - {0}) * 86400)");
  }
}
