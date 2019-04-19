package com.almis.awe.model.dto;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.type.CellDataType;
import com.almis.awe.model.util.data.DateUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import static com.almis.awe.model.type.CellDataType.*;

/**
 * CellData Class
 * CellData as an standard data output
 *
 * @author Pablo GARCIA - 24/JUN/2010
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class CellData implements Serializable, Comparable<CellData>, Copyable {

  // Printable
  private boolean printable = true;

  // Send String value (formatted)
  private boolean sendStringValue = false;

  // String value
  private String stringValue;

  // Object value
  private Object objectValue = null;

  // Object value
  private CellDataType type = NULL;

  // Hash values
  private static final Integer HASH_NUMBER = 7;
  private static final Integer HASH_MULTIPLIER = 72;
  private static final Logger logger = LogManager.getLogger(CellData.class);

  /**
   * Constructor: fast initialization
   */
  public CellData() {
    setNull();
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public CellData(CellData other) {
    if (other != null) {
      this.printable = other.printable;
      this.sendStringValue = other.sendStringValue;
      this.stringValue = other.stringValue;
      this.objectValue = other.objectValue;
      this.type = other.type;
    } else {
      setNull();
    }
  }

  /**
   * Constructor: fast initialization
   *
   * @param <T>
   * @param value Date value
   */
  @JsonCreator
  public <T> CellData(T value) {
    setValue(value);
  }

  /**
   * Constructor: for C JNI calls
   *
   * @param value Integer value
   */
  public CellData(Integer value) {
    setValue(value);
  }

  /**
   * Constructor: for C JNI calls
   *
   * @param value Float value
   */
  public CellData(Float value) {
    setValue(value);
  }

  /**
   * Constructor: for C JNI calls
   *
   * @param value Date value
   */
  public CellData(Date value) {
    setValue(value);
  }

  /**
   * Constructor: for C JNI calls
   *
   * @param value String value
   */
  public CellData(String value) {
    setValue(value);
  }

  /**
   * Stores the string value
   *
   * @param value String value
   * @return this
   */
  @JsonIgnore
  public CellData setStringValue(String value) {
    this.stringValue = value;
    return this;
  }

  /**
   * Retrieve the string value
   *
   * @return CellData value as String
   */
  @JsonIgnore
  public String getStringValue() {
    return this.stringValue.trim();
  }

  /**
   * Returns the object value
   *
   * @return Total pages
   */
  public Object toObject() {
    return getObjectValue();
  }

  /**
   * Stores the object value
   *
   * @param value String value
   * @return this
   */
  @JsonIgnore
  public CellData setObjectValue(Object value) {
    this.objectValue = value;
    return this;
  }

  /**
   * Returns the object value
   *
   * @return CellData value as Object
   */
  @JsonIgnore
  public Object getObjectValue() {
    return objectValue;
  }

  /**
   * Returns the value casted as double
   *
   * @return CellData value as Object
   */
  @JsonIgnore
  public Double getDoubleValue() {
    Double doubleValue;
    switch (this.getType()) {
      // Get value as double
      case DOUBLE:
        doubleValue = (Double) this.getObjectValue();
        break;
      // Get float value as double
      case FLOAT:
        doubleValue = ((Float) this.getObjectValue()).doubleValue();
        break;
      // Get integer value as double
      case INTEGER:
        doubleValue = ((Integer) this.getObjectValue()).doubleValue();
        break;
      // Get long value as double
      case LONG:
        doubleValue = ((Long) this.getObjectValue()).doubleValue();
        break;
      // Get long value as double
      case DECIMAL:
        doubleValue = ((BigDecimal) this.getObjectValue()).doubleValue();
        break;
      // If default, set to null
      default:
        doubleValue = null;
        break;
    }
    return doubleValue;
  }

  /**
   * Returns the value casted as integer
   *
   * @return CellData value as Integer
   */
  @JsonIgnore
  public Integer getIntegerValue() {
    Integer integerValue;
    switch (this.getType()) {
      // Get value as double
      case DOUBLE:
        integerValue = ((Double) this.getObjectValue()).intValue();
        break;
      // Get float value as double
      case FLOAT:
        integerValue = ((Float) this.getObjectValue()).intValue();
        break;
      // Get integer value as double
      case INTEGER:
        integerValue = (Integer) this.getObjectValue();
        break;
      // Get long value as double
      case LONG:
        integerValue = ((Long) this.getObjectValue()).intValue();
        break;
      // Get long value as double
      case DECIMAL:
        integerValue = ((BigDecimal) this.getObjectValue()).intValue();
        break;
      // If default, set to null
      default:
        integerValue = null;
        break;
    }
    return integerValue;
  }

  /**
   * Returns the value casted as date
   *
   * @return CellData value as Date
   */
  @JsonIgnore
  public Date getDateValue() {
    Date dateValue = null;
    switch (this.getType()) {
      // Get value as date
      case DATE:
        dateValue = (Date) this.getObjectValue();
        break;
      // Get value as date
      case STRING:
      default:
        dateValue = stringToDate(getStringValue());
        break;
    }
    return dateValue;
  }

  /**
   * Returns the value casted as date
   *
   * @return CellData value as Date
   */
  private Date stringToDate(String dateString) {
    Date dateValue = null;
    try {
      if (DateUtil.isSqlDate(dateString)) {
        dateValue = DateUtil.sql2JavaDate(dateString);
      } else if (DateUtil.isWbsDate(dateString)) {
        dateValue = DateUtil.wbs2JavaDate(dateString);
      } else if (DateUtil.isWebTimestamp(dateString)) {
        dateValue = DateUtil.web2TimestampWithMs(dateString);
      }
    } catch (ParseException exc) {
      logger.error("Parsing date " + dateString, exc);
    }
    return dateValue;
  }

  /**
   * Request cell type
   *
   * @return the type
   */
  @JsonIgnore
  public CellDataType getType() {
    return type;
  }

  /**
   * Store cell type
   *
   * @param type the type to set
   * @return this;
   */
  @JsonIgnore
  public CellData setType(CellDataType type) {
    this.type = type;
    return this;
  }

  /**
   * Stores a string value
   *
   * @param value String value
   * @return this
   */
  @JsonIgnore
  public CellData setValue(String value) {
    setValue(value, STRING);
    return this;
  }

  /**
   * Stores a date value
   *
   * @param value Date value
   * @return this
   */
  @JsonIgnore
  public CellData setValue(Date value) {
    String dateString = "";
    if (value != null) {
      // Parse value as web timestamp
      dateString = DateUtil.dat2WebTimestamp(value);
    }
    setValue(dateString, value, DATE);
    return this;
  }

  /**
   * Stores a double value
   *
   * @param value Double value
   * @return this
   */
  @JsonIgnore
  public CellData setValue(Double value) {
    setValue(value, DOUBLE);
    return this;
  }

  /**
   * Stores a float value
   *
   * @param value Double value
   * @return this
   */
  @JsonIgnore
  public CellData setValue(Float value) {
    setValue(value, FLOAT);
    return this;
  }

  /**
   * Stores an integer value
   *
   * @param value Integer value
   * @return this
   */
  @JsonIgnore
  public CellData setValue(Integer value) {
    setValue(value, INTEGER);
    return this;
  }

  /**
   * Stores a long value
   *
   * @param value Long value
   * @return this
   */
  @JsonIgnore
  public CellData setValue(Long value) {
    setValue(value, LONG);
    return this;
  }

  /**
   * Stores a long value
   *
   * @param value Long value
   * @return this
   */
  @JsonIgnore
  public CellData setValue(BigDecimal value) {
    setValue(value, DECIMAL);
    return this;
  }

  /**
   * Set celldata value
   *
   * @param value Long value
   * @return this
   */
  @JsonIgnore
  public CellData setValue(CellData value) {
    setValue(value.getStringValue(), value.getObjectValue(), value.getType());
    return this;
  }

  private void setValue(Object value, CellDataType type) {
    setValue(value != null ? value.toString() : "", value, type);
  }

  private void setValue(String stringValue, Object value, CellDataType type) {
    setStringValue(stringValue);
    setObjectValue(value);
    setType(type);
  }

  /**
   * Stores an object value
   *
   * @param value Object value
   * @return this
   */
  @JsonIgnore
  public CellData setValue(Object value) {
    if (value == null) {
      setNull();
    } else if (value instanceof String) {
      setValue((String) value);
    } else if (value instanceof Integer) {
      setValue((Integer) value);
    } else if (value instanceof Long) {
      setValue((Long) value);
    } else if (value instanceof BigDecimal) {
      setValue((BigDecimal) value);
    } else if (value instanceof Float) {
      setValue((Float) value);
    } else if (value instanceof Double) {
      setValue((Double) value);
    } else if (value instanceof Boolean) {
      setValue((Boolean) value);
    } else if (value instanceof Date) {
      setValue((Date) value);
    } else if (value instanceof JsonNode) {
      setValue((JsonNode) value);
    } else if (value instanceof CellData) {
      setValue((CellData) value);
    } else {
      logger.debug("Clase de ''{}'' - ''{}''", value, value.getClass().getName());
      setValue(value.toString(), value, OBJECT);
    }
    return this;
  }

  /**
   * Stores a boolean value
   *
   * @param value Boolean value
   * @return this
   */
  @JsonIgnore
  public CellData setValue(Boolean value) {
    setValue(value == null ? Boolean.toString(false) : value.toString(), value, BOOLEAN);
    return this;
  }

  /**
   * Stores a Json value
   *
   * @param value Json value
   * @return this
   */
  @JsonIgnore
  public CellData setValue(JsonNode value) {
    setValue(value == null ? "" : value.toString(), value == null ? JsonNodeFactory.instance.nullNode() : value, JSON);
    return this;
  }

  /**
   * Stores a null value
   *
   * @return this
   */
  @JsonIgnore
  public CellData setNull() {
    setValue("", null, NULL);
    return this;
  }

  /**
   * Request if cell is printable
   *
   * @return the printable
   */
  @JsonIgnore
  public boolean isPrintable() {
    return printable;
  }

  /**
   * Request if cell is printable
   *
   * @return the printable
   */
  @JsonIgnore
  public boolean isEmpty() {
    return this.stringValue.isEmpty();
  }

  /**
   * Check if value must be sent as string
   *
   * @return Send value as string
   */
  @JsonIgnore
  public boolean isSendStringValue() {
    return sendStringValue;
  }

  /**
   * Set value as string
   *
   * @param sendStringValue Send value as string
   * @return this
   */
  @JsonIgnore
  public CellData setSendStringValue(boolean sendStringValue) {
    this.sendStringValue = sendStringValue;
    return this;
  }

  /**
   * Store if cell is printable
   *
   * @param printable the printable to set
   * @return this
   */
  @JsonIgnore
  public CellData setPrintable(boolean printable) {
    this.printable = printable;
    return this;
  }

  /**
   * Compare to another cell to see which one is higher
   *
   * @param cell2 Cell to compare to
   * @return Comparison
   */
  @Override
  public int compareTo(CellData cell2) {
    int comparison;
    switch (this.getType()) {
      // Compare as date
      case DATE:
        comparison = compareObjects(toObject(), cell2.toObject(), Date.class);
        break;
      // Compare as double
      case DOUBLE:
        comparison = compareObjects(toObject(), cell2.toObject(), Double.class);
        break;
      // Compare as float
      case FLOAT:
        comparison = compareObjects(toObject(), cell2.toObject(), Float.class);
        break;
      // Compare as integer
      case INTEGER:
        comparison = compareObjects(toObject(), cell2.toObject(), Integer.class);
        break;
      // Compare as long
      case LONG:
        comparison = compareObjects(toObject(), cell2.toObject(), Long.class);
        break;
      // Compare as string
      case STRING:
      default:
        String string1 = this.getStringValue();
        String string2 = cell2.getStringValue();
        comparison = string1.compareTo(string2);
        break;
    }
    return comparison;
  }

  /**
   * Compare two cells
   *
   * @param object1     Object 1 to compare
   * @param object2     Object 2 to compare
   * @param objectClass Object classes
   * @return Comparison result
   */
  private int compareObjects(Object object1, Object object2, Class<?> objectClass) {
    int comparison;
    if (object1 == null && object2 == null) {
      comparison = 0;
    } else if (object1 == null) {
      comparison = -1;
    } else if (object2 == null) {
      comparison = 1;
    } else {
      try {
        Method compareTo = object1.getClass().getMethod("compareTo", objectClass);
        comparison = (int) compareTo.invoke(object1, object2);
      } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException exc) {
        logger.error("Can't compare classes {0} - {1}", new Object[]{object1, object2}, exc);
        comparison = 0;
      }
    }
    return comparison;
  }

  /**
   * Check if object is equals to other object
   *
   * @param obj2 Object to compare to
   * @return Objects are equal or not
   */
  @Override
  public boolean equals(Object obj2) {
    boolean equals = false;
    if (obj2 instanceof CellData) {
      CellData cell2 = (CellData) obj2;
      switch (this.getType()) {
        // Compare as date
        case DATE:
          Date date1 = (Date) this.toObject();
          Date date2 = (Date) cell2.toObject();
          equals = date1.equals(date2);
          break;
        // Compare as double
        case DOUBLE:
          Double double1 = (Double) this.toObject();
          Double double2 = (Double) cell2.toObject();
          equals = double1.equals(double2);
          break;
        // Compare as float
        case FLOAT:
          Float float1 = (Float) this.toObject();
          Float float2 = (Float) cell2.toObject();
          equals = float1.equals(float2);
          break;
        // Compare as integer
        case INTEGER:
          Integer integer1 = (Integer) this.toObject();
          Integer integer2 = (Integer) cell2.toObject();
          equals = integer1.equals(integer2);
          break;
        // Compare as long
        case LONG:
          Long long1 = (Long) this.toObject();
          Long long2 = (Long) cell2.toObject();
          equals = long1.equals(long2);
          break;
        // Compare as object
        case NULL:
          equals = cell2.getType() == CellDataType.NULL;
          break;
        // Compare as string
        case STRING:
        default:
          String string1 = this.getStringValue();
          String string2 = cell2.getStringValue();
          equals = string1.equals(string2);
          break;
      }
    }
    return equals;
  }

  /**
   * Generate class hashcode
   *
   * @return Class hashcode
   */
  @Override
  public int hashCode() {
    // Get celldata hashcode (for comparison purposes)
    int hash = HASH_NUMBER;
    hash = HASH_MULTIPLIER * hash + (this.printable ? 1 : 0);
    hash = HASH_MULTIPLIER * hash + (this.stringValue != null ? this.stringValue.hashCode() : 0);
    hash = HASH_MULTIPLIER * hash + (this.objectValue != null ? this.objectValue.hashCode() : 0);
    hash = HASH_MULTIPLIER * hash + (this.type != null ? this.type.hashCode() : 0);
    return hash;
  }

  /**
   * Returns the object value
   *
   * @return Total pages
   */
  @JsonValue
  public Object getValue() {
    if (sendStringValue) {
      return this.getStringValue();
    } else {
      switch (this.getType()) {
        // Get object value
        case DOUBLE:
        case FLOAT:
        case INTEGER:
        case LONG:
        case DECIMAL:
        case JSON:
        case OBJECT:
          return this.getObjectValue();
        // Get json value as null
        case NULL:
          return null;
        // Get json value as string
        case DATE:
        case STRING:
        default:
          return this.getStringValue();
      }
    }
  }

  @Override
  public CellData copy() throws AWException {
    return new CellData(this);
  }

  private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {

    stream.writeBoolean(printable);
    stream.writeBoolean(sendStringValue);
    stream.writeObject(stringValue);
    stream.writeObject(type);

    switch (this.getType()) {
      // Get object value
      case STRING:
        stream.writeObject(objectValue);
        break;
      case DOUBLE:
        stream.writeDouble((Double) objectValue);
        break;
      case FLOAT:
        stream.writeFloat((Float) objectValue);
        break;
      case INTEGER:
        stream.writeInt((Integer) objectValue);
        break;
      case LONG:
        stream.writeLong((Long) objectValue);
        break;
      case DECIMAL:
        stream.writeDouble(((BigDecimal) objectValue).doubleValue());
        break;
      case JSON:
        ObjectMapper objectJsonMapper = new ObjectMapper();
        stream.writeUTF(objectJsonMapper.writeValueAsString(objectValue));
        break;
      default:
        stream.writeObject(objectValue);
    }
  }

  private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {

    this.printable = stream.readBoolean();
    this.sendStringValue = stream.readBoolean();
    this.stringValue = (String) stream.readObject();
    this.type = (CellDataType)stream.readObject();

    switch (this.getType()) {
      // Get object value
      case STRING:
        this.objectValue = stream.readObject();
        break;
      case DOUBLE:
        this.objectValue = stream.readDouble();
        break;
      case FLOAT:
        this.objectValue = stream.readFloat();
        break;
      case INTEGER:
        this.objectValue = stream.readInt();
        break;
      case LONG:
        this.objectValue = stream.readLong();
        break;
      case DECIMAL:
        this.objectValue = BigDecimal.valueOf(stream.readDouble());
        break;
      case JSON:
        ObjectMapper objectJsonMapper = new ObjectMapper();
        this.objectValue = objectJsonMapper.readTree(stream.readUTF());
        break;
      default:
        this.objectValue = stream.readObject();
    }
  }

  /**
   * Returns the string value
   *
   * @return Total pages
   */
  @Override
  public String toString() {
    return "CellData{" +
            "type=" + type +
            ", stringValue='" + stringValue + '\'' +
            ", objectValue=" + objectValue +
            ", printable=" + printable +
            ", sendStringValue=" + sendStringValue +
            '}';
  }
}
