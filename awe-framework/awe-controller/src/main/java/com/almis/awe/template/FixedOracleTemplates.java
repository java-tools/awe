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
    // https://github.com/querydsl/querydsl/pull/1590/commits/f9f54bb2bd4213dc9bb43bb8b5c2f00958fb77b0
    add(Ops.DateTimeOps.DIFF_WEEKS, "round((cast({1} as date) - cast({0} as date)) / 7)");
    add(Ops.DateTimeOps.DIFF_DAYS, "round(cast({1} as date) - cast({0} as date))");
    add(Ops.DateTimeOps.DIFF_HOURS, "round((cast({1} as date) - cast({0} as date)) * 24)");
    add(Ops.DateTimeOps.DIFF_MINUTES, "round((cast({1} as date) - cast({0} as date)) * 1440)");
    add(Ops.DateTimeOps.DIFF_SECONDS, "round((cast({1} as date) - cast({0} as date)) * 86400)");
  }
}
