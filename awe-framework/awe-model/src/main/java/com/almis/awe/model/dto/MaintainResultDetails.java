package com.almis.awe.model.dto;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.type.MaintainType;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Stores details about a maintain operation
 *
 * @author jbellon
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class MaintainResultDetails implements Copyable {

  private MaintainType operationType;
  private Long rowsAffected;
  private Map<String, QueryParameter> parameterMap = null;

  /**
   * Copy constructor
   *
   * @param other
   */
  public MaintainResultDetails(MaintainResultDetails other) throws AWException {
    this.operationType = other.operationType;
    this.rowsAffected = other.rowsAffected;
    this.parameterMap = ListUtil.copyMap(other.parameterMap);
  }

  /**
   * Constructor
   *
   * @param operationType Operation type
   * @param rowsAffected  Rows affected
   * @param parameterMap  Parameter map
   */
  @JsonCreator
  public MaintainResultDetails(@JsonProperty("operationType") MaintainType operationType, @JsonProperty("rowsAffected") Long rowsAffected, @JsonProperty("parameterMap") Map<String, QueryParameter> parameterMap) {
    this.operationType = operationType;
    this.rowsAffected = rowsAffected;
    this.parameterMap = parameterMap;
  }

  /**
   * Constructor
   *
   * @param operationType Operation type
   * @param rowsAffected  Rows affected
   */
  public MaintainResultDetails(MaintainType operationType, Long rowsAffected) {
    this.operationType = operationType;
    this.rowsAffected = rowsAffected;
  }

  /**
   * Retrieves operation type
   *
   * @return operationType
   */
  @JsonGetter("operationType")
  public MaintainType getOperationType() {
    return operationType;
  }

  /**
   * Sets operation type
   *
   * @param operationType Operation type
   */
  @JsonSetter("operationType")
  public void setOperationType(MaintainType operationType) {
    this.operationType = operationType;
  }

  /**
   * Retrieves rows affected
   *
   * @return rowsAffected
   */
  @JsonGetter("rowsAffected")
  public Long getRowsAffected() {
    return rowsAffected;
  }

  /**
   * Sets number of rows affected
   *
   * @param rowsAffected Rows affected
   */
  @JsonSetter("rowsAffected")
  public void setRowsAffected(Long rowsAffected) {
    this.rowsAffected = rowsAffected;
  }

  /**
   * Get parameter map
   *
   * @return parameter map
   */
  @JsonGetter("parameterMap")
  public Map<String, QueryParameter> getParameterMap() {
    return parameterMap;
  }

  /**
   * Set parameter map
   *
   * @param parameterMap Parameter map
   * @return Parameter map
   */
  @JsonSetter("parameterMap")
  public MaintainResultDetails setParameterMap(Map<String, QueryParameter> parameterMap) {
    this.parameterMap = parameterMap;
    return this;
  }

  @Override
  public MaintainResultDetails copy() throws AWException {
    return new MaintainResultDetails(this);
  }

  private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    ListUtil.writeMap(stream, this.parameterMap);
  }

  private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.parameterMap = ListUtil.readMap(stream, QueryParameter.class);
  }
}
