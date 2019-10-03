/*
 * Package definition
 */
package com.almis.awe.model.type;

/**
 * ParameterType Enumerated
 *
 * List of allowed parameter types in queries
 *
 *
 * @author Pablo GARCIA - 13/JUL/2010
 */
public enum ParameterType {

  // %value
  STRINGL,
  // value%
  STRINGR,
  // %value%
  STRINGB,
  // value
  STRING,
  // value | NULL
  STRINGN,
  // Hashes a string with the RipEmd160 algorithm
  STRING_HASH_RIPEMD160,
  // Hashes a string with the SHA-256 algorithm
  STRING_HASH_SHA,
  // Hashes a string with the PBKDF2WithHmacSHA1
  STRING_HASH_PBKDF_2_W_HMAC_SHA_1,
  // value symetrically encripted
  STRING_ENCRYPT,
  // date
  DATE,
  // time
  TIME,
  // timestamp
  TIMESTAMP,
  // date
  SYSTEM_DATE,
  // time
  SYSTEM_TIME,
  // Boolean
  BOOLEAN,
  // float
  FLOAT,
  // double
  DOUBLE,
  // integer
  INTEGER,
  // long
  LONG,
  // object
  OBJECT,
  // JsonNode
  JSON,
  // null
  NULL,
  // multiple sequence (integer)
  MULTIPLE_SEQUENCE,
  // sequence (integer)
  SEQUENCE
}
