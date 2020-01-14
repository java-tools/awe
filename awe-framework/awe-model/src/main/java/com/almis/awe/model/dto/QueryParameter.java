package com.almis.awe.model.dto;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.type.ParameterType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class that holds information of Query Variable obtained from AWERequest parameters
 *
 * @author jbellon
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class QueryParameter implements Copyable {

  private JsonNode value;
  private boolean isList;
  private ParameterType type;

  /**
   * Copy constructor
   *
   * @param other QueryParameter object
   */
  public QueryParameter(QueryParameter other) {
    if (other != null) {
      this.value = other.value == null ? null : other.value.deepCopy();
      this.isList = other.isList;
      this.type = other.type;
    } else {
      this.value = null;
      this.type = ParameterType.NULL;
    }
  }

  /**
   * Constructor
   *
   * @param value Parameter value
   */
  @JsonCreator
  public QueryParameter(JsonNode value) {
    this.value = value;
    this.isList = value.isArray();
    if (value.isInt()) {
      this.type = ParameterType.INTEGER;
    } else if (value.isLong()) {
      this.type = ParameterType.LONG;
    } else if (value.isFloat()) {
      this.type = ParameterType.FLOAT;
    } else if (value.isDouble()) {
      this.type = ParameterType.DOUBLE;
    } else if (value.isBoolean()) {
      this.type = ParameterType.BOOLEAN;
    } else if (value.isNull()) {
      this.type = ParameterType.NULL;
    } else {
      this.type = ParameterType.STRING;
    }
  }

  /**
   * Constructor
   *
   * @param value  Parameter value
   * @param isList Is list
   * @param type   Parameter type
   */
  public QueryParameter(JsonNode value, boolean isList, ParameterType type) {
    this.value = value;
    this.isList = isList;
    this.type = type;
  }

  /**
   * Retrieves the value
   *
   * @return value
   */
  @JsonValue
  public JsonNode getValue() {
    return value;
  }

  /**
   * Sets the value
   *
   * @param value Parameter value
   */
  @JsonIgnore
  public void setValue(JsonNode value) {
    this.value = value;
  }

  /**
   * Retrieves whether if it's a list or not
   *
   * @return isList
   */
  @JsonIgnore
  public boolean isList() {
    return isList;
  }

  /**
   * Sets whether if it's a list or not
   *
   * @param isList Parameter is list
   */
  @JsonIgnore
  public void setList(boolean isList) {
    this.isList = isList;
  }

  /**
   * Get parameter type
   *
   * @return Parameter type
   */
  @JsonIgnore
  public ParameterType getType() {
    return type;
  }

  /**
   * Set parameter type
   *
   * @param type Parameter type
   */
  @JsonIgnore
  public void setType(ParameterType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public boolean equals(Object anotherObject) {
    if (anotherObject == null) {
      return false;
    }
    if (anotherObject instanceof QueryParameter) {
      QueryParameter anotherParameter = (QueryParameter) anotherObject;
      if (getType().equals(anotherParameter.getType())) {
        if (anotherParameter.getValue() == null || this.getValue() == null) {
          return this.getValue() == anotherParameter.getValue();
        }
        return getValue().equals(anotherParameter.getValue());
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    return value != null ? value.hashCode() : 0;
  }

  @Override
  public QueryParameter copy() throws AWException {
    return new QueryParameter(this);
  }

  private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    ObjectMapper objectMapper = new ObjectMapper();
    byte[] jsonBytes = objectMapper.writeValueAsBytes(value);
    stream.write(jsonBytes.length);
    stream.writeObject(jsonBytes);
  }

  private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    byte[] jsonBytes = new byte[stream.readInt()];
    stream.readFully(jsonBytes);
    ObjectMapper objectMapper = new ObjectMapper();
    this.value = objectMapper.readTree(jsonBytes);
  }
}
