package com.almis.awe.model.dto;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ServiceData Class
 *
 * This class is used to generate objects as return of a service call
 *
 *
 * @author Pablo VIDAL - 20/MAR/2015
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ServiceData implements Serializable, Copyable {

  // Service is valid
  @Builder.Default private boolean valid = true;

  // Service response message type
  @Builder.Default private AnswerType type = AnswerType.OK;

  // Service response message title
  @Builder.Default private String title = "";

  // Service response message description
  @Builder.Default private String message = "";

  // Service response data
  @Builder.Default private Serializable data = null;

  // Service response data (as datalist)
  @Builder.Default private DataList dataList = null;

  // Service response file list
  @Builder.Default private List<ClientAction> clientActionList = new ArrayList<>();

  // Service variable list
  @Builder.Default private Map<String, CellData> variableMap = new HashMap<>();

  // Service details if maintain
  @Builder.Default private transient List<MaintainResultDetails> resultDetails = new ArrayList<>();

  /**
   * Copy constructor
   * @param other Other service data
   * @throws AWException
   */
  public ServiceData(ServiceData other) throws AWException {
    this.valid = other.valid;
    this.type = other.type;
    this.title = other.title;
    this.message = other.message;
    this.data = other.data;
    this.dataList = other.dataList == null ? null : other.getDataList().copy();
    this.clientActionList = ListUtil.copyList(other.getClientActionList());
    this.variableMap = ListUtil.copyMap(other.getVariableMap());
    this.resultDetails = ListUtil.copyList(other.getResultDetails());
  }

  @Override
  public ServiceData copy() throws AWException {
    return this.toBuilder()
      .dataList(dataList == null ? null : dataList.copy())
      .clientActionList(ListUtil.copyList(clientActionList))
      .variableMap(ListUtil.copyMap(variableMap))
      .resultDetails(ListUtil.copyList(resultDetails))
      .build();
  }

  /**
   * Store the service response from one row of Datalist
   *
   * @param dataList DataList to store
   * @param rowNum   Row index of DataList to store
   * @return this
   */
  public ServiceData setData(DataList dataList, Integer rowNum) {
    if (rowNum != null) {
      List<String> result = new ArrayList<>(DataListUtil.getColumnList(dataList).size());
      // Get row data
      Map<String, CellData> rowData = DataListUtil.getRow(dataList, rowNum);
      // Build list with data of row
      for (Map.Entry<String, CellData> entry : rowData.entrySet()) {
        result.add(entry.getValue().getStringValue());
      }
      this.data = result.toArray();
    }
    return this;
  }

  /**
   * Store the service response from column list of Datalist
   *
   * @param dataList   DataList to store
   * @param columnList Column name of DataList to store
   * @return this
   */
  public ServiceData setData(DataList dataList, List<String> columnList) {

    if (columnList != null && !columnList.isEmpty()) {
      // Get columns data
      this.data = DataListUtil.getDataAsArray(dataList, columnList);
    }
    return this;
  }

  /**
   * Stores the service response message type (ERROR, OK, WARNING, INFO)
   *
   * @param type Service output type
   * @return this
   */
  public ServiceData setType(AnswerType type) {
    // Store type
    this.type = type;
    this.setValid(!AnswerType.ERROR.equals(type));
    return this;
  }

  /**
   * Adds a client action to the list
   *
   * @param clientAction Client action to be added
   * @return this
   */
  public ServiceData addClientAction(ClientAction clientAction) {
    this.getClientActionList().add(clientAction);
    return this;
  }

  /**
   * Adds a variable to the map
   *
   * @param name     Variable name
   * @param variable Variable value (as CellData)
   * @return this
   */
  public ServiceData addVariable(String name, CellData variable) {
    this.getVariableMap().put(name, variable);
    return this;
  }

  /**
   * Adds a variable to the map
   *
   * @param name  Variable name
   * @param value Variable value
   * @return this
   */
  public ServiceData addVariable(String name, String value) {
    this.getVariableMap().put(name, new CellData(value));
    return this;
  }

  /**
   * Get variable from the map
   *
   * @param name Variable name
   * @return this
   */
  public CellData getVariable(String name) {
    return getVariableMap().get(name);
  }

  /**
   * Adds details about an operation
   *
   * @param details Details
   * @return this
   */
  public ServiceData addResultDetails(MaintainResultDetails details) {
    this.resultDetails.add(details);
    return this;
  }
}
