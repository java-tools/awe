/*
 * Package definition
 */
package com.almis.awe.model.type;

import java.io.Serializable;
import java.sql.Types;

/**
 * CellDataType Enumerated
 *
 * List of allowed cell data types in DataList
 *
 *
 * @author Pablo VIDAL - 16/Nov/2018
 */
public enum CellDataType implements Serializable {

  STRING("STRING"),
  DATE("DATE"),
  DOUBLE("DOUBLE"),
  FLOAT("FLOAT"),
  DECIMAL("DECIMAL"),
  INTEGER("INTEGER"),
  LONG("LONG"),
  OBJECT("OBJECT"),
  NULL("NULL"),
  CLOB("CLOB"),
  JSON("JSON"),
  BOOLEAN("BOOLEAN");

  private final String type;

  CellDataType(String type) {
    this.type = type;
  }

  /**
   * Get celldata type from value
   * @param value value
   * @return CellDataType
   */
  public static CellDataType fromValue(String value) {
    if (value != null) {
      for (CellDataType cellDataType : values()) {
        if (cellDataType.type.equals(value)) {
          return cellDataType;
        }
      }
    }

    // Return a default value
    return getDefault();
  }

  /**
   * Get string cellDataType
   * @return String
   */
  public String toValue() {
    return type;
  }

  private static CellDataType getDefault() {
    return NULL;
  }

  /**
   * Get enumerated property key
   *
   * @param type celldata type
   * @param precision numeric precision
   * @param scale numeric scale
   * @return Property key (if defined)
   */
  public static CellDataType convertSQLType(int type, int precision, int scale) {
    CellDataType convertedType;

    switch (type) {
    /* INTEGER */
      case Types.BIGINT:
        convertedType = LONG;
        break;

      case Types.INTEGER:
      case Types.SMALLINT:
      case Types.TINYINT:
        convertedType = INTEGER;
        break;

      case Types.DOUBLE:
      case Types.FLOAT:
        convertedType = DOUBLE;
        break;

      case Types.REAL:
        convertedType = FLOAT;
        break;

      case Types.NUMERIC:
      case Types.DECIMAL:
        convertedType = DECIMAL;
        break;

      case Types.DATE:
      case Types.TIME:
      case Types.TIMESTAMP:
        convertedType = DATE;
        break;

      case Types.CHAR:
      case Types.VARCHAR:
      case Types.LONGVARCHAR:
      case Types.NCHAR:
      case Types.NVARCHAR:
      case Types.LONGNVARCHAR:
      case Types.ROWID:
      case Types.CLOB:
      case Types.SQLXML:
        convertedType = STRING;
        break;

      case Types.NULL:
        convertedType = NULL;
        break;

      default:
        convertedType = OBJECT;
        break;
    }

    return convertedType;
  }
}
