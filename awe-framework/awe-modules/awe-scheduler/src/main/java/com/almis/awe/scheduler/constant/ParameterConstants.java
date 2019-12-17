package com.almis.awe.scheduler.constant;

/**
 *
 * @author dfuentes
 */
public class ParameterConstants {

  private ParameterConstants(){}

  // Parameters type
  public static final String STRING = "STRING";
  public static final String DATE = "DATE";
  public static final String TIME = "TIME";
  public static final String INTEGER = "INTEGER";
  public static final String FLOAT = "FLOAT";
  public static final String DOUBLE = "DOUBLE";
  public static final String LONG = "LONG";
  public static final String OBJECT = "OBJECT";

  // Parameter source
  public static final Integer VALUE = 0;
  public static final Integer VARIABLE = 1;
  public static final Integer PROPERTY = 2;
}
