/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.enumerates;

/**
 *
 * @author dfuentes
 */
public enum Aggregator {

  LOWER_BOUND_80("80% Lower Bound"),
  UPPER_BOUND_80("80% Upper Bound"),
  AVERAGE("Average"),
  COUNT("Count"),
  COUNT_UNIQUE_VALUES("Count Unique Values"),
  COUNT_AS_FRACTION_OF_COLUMNS("Count as Fraction of Columns"),
  COUNT_AS_FRACTION_OF_ROWS("Count as Fraction of Rows"),
  COUNT_AS_FRACTION_OF_TOTAL("Count as Fraction of Total"),
  CUSTOM_80_LOWER_BOUND("Custom 80% Lower Bound"),
  CUSTOM_80_UPPER_BOUND("Custom 80% Upper Bound"),
  CUSTOM_AVERAGE("Custom Average"),
  CUSTOM_MAXIMUM("Custom Maximum"),
  CUSTOM_MINIMUM("Custom Minimum"),
  CUSTOM_SUM("Custom Sum"),
  CUSTOM_SUM_OVER_SUM("Custom Sum Over Sum"),
  INTEGER_SUM("Integer Sum"),
  MAXIMUM("Maximum"),
  MINIMUM("Minimum"),
  SUM("Sum"),
  SUM_AS_FRACTION_OF_COLUMNS("Sum as Fraction of Columns"),
  SUM_AS_FRACTION_OF_ROWS("Sum as Fraction of Rows"),
  SUM_AS_FRACTION_OF_TOTAL("Sum as Fraction of Total"),
  SUM_OVER_SUM("Sum Over Sum");

  private final String aggregator;

  private Aggregator(String aggregator) {
    this.aggregator = aggregator;
  }

  /**
   * Equals method
   *
   * @param aggregator
   * @return
   */
  public boolean equalsStr(String aggregator) {
    return this.aggregator.equals(aggregator);
  }

  @Override
  public String toString() {
    return this.aggregator;
  }

}
