/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.model.entities.screen.data;

import com.almis.awe.model.dto.CellData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author pgarcia
 */
public class ComponentModel {

  // Selected values
  private List<CellData> selected = new ArrayList<CellData>();

  // Default values
  private List<CellData> defaultValues = new ArrayList<CellData>();

  // Selection values
  private List<Map<String, CellData>> values = new ArrayList<Map<String, CellData>>();

  // Page
  private Long page = 1L;

  // Total pages
  private Long total = 0l;

  // Records
  private Long records = 0l;

  /**
   * @return the selected
   */
  public List<CellData> getSelected() {
    return selected;
  }

  /**
   * @param selected the selected to set
   */
  public void setSelected(List<CellData> selected) {
    this.selected = selected;
  }

  /**
   * Add a value to the selected list
   *
   * @param value Selected value to add
   */
  public void addSelected(CellData value) {
    this.selected.add(value);
  }

  /**
   * @return the values
   */
  public List<Map<String, CellData>> getValues() {
    return values;
  }

  /**
   * @param values the values to set
   */
  public void setValues(List<Map<String, CellData>> values) {
    this.values = values;
  }

  /**
   * Add a value to the values list
   *
   * @param value Value to add
   */
  public void addValue(Map<String, CellData> value) {
    this.values.add(value);
  }

  /**
   * Get default values
   *
   * @return Default value list
   */
  public List<CellData> getDefaultValues() {
    return defaultValues;
  }

  /**
   * Set default values
   *
   * @param defaultValues Model default values
   * @return this
   */
  public ComponentModel setDefaultValues(List<CellData> defaultValues) {
    this.defaultValues = defaultValues;
    return this;
  }

  /**
   * Get model page number
   *
   * @return Page number
   */
  public Long getPage() {
    return page;
  }

  /**
   * Set model page number
   *
   * @param page Page number
   * @return this
   */
  public ComponentModel setPage(Long page) {
    this.page = page;
    return this;
  }

  /**
   * Get model total pages
   *
   * @return Total pages
   */
  public Long getTotal() {
    return total;
  }

  /**
   * Set model total pages
   *
   * @param total Total pages
   * @return this
   */
  public ComponentModel setTotal(Long total) {
    this.total = total;
    return this;
  }

  /**
   * Get model records
   *
   * @return Records
   */
  public Long getRecords() {
    return records;
  }

  /**
   * Set model records
   *
   * @param records Records
   * @return this
   */
  public ComponentModel setRecords(Long records) {
    this.records = records;
    return this;
  }
}
