/*
 * Package definition
 */
package com.almis.awe.model.dto;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.queries.Variable;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonInclude;

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
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ServiceData implements Serializable {

  private static final long serialVersionUID = 8252045856785186581L;

  // Service is valid
  private boolean valid;

  // Service response message type
  private AnswerType type;

  // Service response message title
  private String title;

  // Service response message description
  private String message;

  // Service response data
  private Serializable data;

  // Service response data (as datalist)
  private DataList dataList;

  // Service response file list
  private List<ClientAction> clientActionList;

  // Service variable list
  private Map<String, CellData> variableMap;

  // Service details if maintain
  private transient List<MaintainResultDetails> resultDetails;

  /**
   * Object initialization
   */
  public ServiceData() {
    // Datalist initialization
    valid = true;
    type = AnswerType.OK;
    title = "";
    message = "";
    data = null;
    dataList = null;
    clientActionList = null;
    variableMap = null;
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public ServiceData(ServiceData other) throws AWException {
    this.valid = other.valid;
    this.type = other.type;
    this.title = other.title;
    this.message = other.message;
    this.data = other.data;
    this.dataList = other.dataList == null ? null : new DataList(other.dataList);
    this.clientActionList = ListUtil.copyList(other.clientActionList);
    this.variableMap = ListUtil.copyMap(other.variableMap);
    this.resultDetails = ListUtil.copyList(other.resultDetails);
  }

  /**
   * Returns true if service is valid
   *
   * @return Service is valid
   */
  public boolean isValid() {
    return valid;
  }

  /**
   * Stores if service is valid
   *
   * @param valid Service is valid
   * @return this
   */
  public ServiceData setValid(boolean valid) {
    this.valid = valid;
    return this;
  }

  /**
   * Returns the service response title
   *
   * @return Response title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Stores the service response title
   *
   * @param title Response title
   * @return this
   */
  public ServiceData setTitle(String title) {
    this.title = title;
    return this;
  }

  /**
   * Returns the service response description
   *
   * @return Service response description
   */
  public String getMessage() {
    return message;
  }

  /**
   * Stores the service response description
   *
   * @param message Service response description
   * @return this
   */
  public ServiceData setMessage(String message) {
    this.message = message;
    return this;
  }

  /**
   * Returns the service response data
   *
   * @return Service response data
   */
  public Object getData() {
    return data;
  }

  /**
   * Stores the service response data
   *
   * @param data Service response data
   * @return this
   */
  public ServiceData setData(Serializable data) {
    this.data = data;
    return this;
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
   * Returns the service response message type (ERROR, OK, WARNING, INFO)
   *
   * @return Service output type
   */
  public AnswerType getType() {
    return type;
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

    // Set validity by answer types
    switch (type) {
      case ERROR:
        this.setValid(false);
        break;

      case OK:
      case WARNING:
      case INFO:
      default:
        this.setValid(true);
        break;
    }
    return this;
  }

  /**
   * Returns the output file list
   *
   * @return Output file list
   */
  public List<ClientAction> getClientActionList() {
    if (clientActionList == null) {
      clientActionList = new ArrayList<>();
    }
    return clientActionList;
  }

  /**
   * Returns the output client action list
   *
   * @param clientActionList Output client action list
   * @return this
   */
  public ServiceData setClientActionList(List<ClientAction> clientActionList) {
    this.clientActionList = clientActionList;
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
   * Returns the output file list
   *
   * @return Output file list
   */
  public Map<String, CellData> getVariableMap() {
    if (variableMap == null) {
      variableMap = new HashMap<>();
    }
    return variableMap;
  }

  /**
   * Sets the variable map
   *
   * @param map Variable map
   * @return this
   */
  public ServiceData setVariableMap(Map<String, CellData> map) {
    this.variableMap = map;
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
   * @return the dataList
   */
  public DataList getDataList() {
    return dataList;
  }

  /**
   * Set the datalist
   *
   * @param dataList Datalist
   * @return this
   */
  public ServiceData setDataList(DataList dataList) {
    this.dataList = dataList;
    return this;
  }

  /**
   * Retrieves result details when the operation is a maintain
   *
   * @return resultDetails
   */
  public List<MaintainResultDetails> getResultDetails() {
    if (resultDetails == null) {
      resultDetails = new ArrayList<>();
    }
    return resultDetails;
  }

  /**
   * Sets result details when the operation is a maintain
   *
   * @param resultDetails Result details
   * @return this
   */
  public ServiceData setResultDetails(List<MaintainResultDetails> resultDetails) {
    this.resultDetails = resultDetails;
    return this;
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
