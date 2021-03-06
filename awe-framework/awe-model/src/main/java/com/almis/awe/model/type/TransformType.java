/*
 * Package definition
 */
package com.almis.awe.model.type;

/**
 * TransformType Enumerated
 *
 * List of allowed transform types in queries
 *
 * 
 * @author Pablo GARCIA - 13/JUL/2010
 */
public enum TransformType {
  DATE,
  DATE_MS,
  TIME,
  TIMESTAMP,
  TIMESTAMP_MS,
  JS_DATE,
  JS_TIMESTAMP,
  ELAPSED_TIME,
  DATE_SINCE,
  NUMBER,
  NUMBER_PLAIN,
  BOOLEAN,
  TEXT_HTML,
  TEXT_UNILINE,
  TEXT_PLAIN,
  MARKDOWN_HTML,
  DECRYPT,
  GENERIC_DATE,
  DATE_RDB,
  ARRAY,
  LIST
}
