/*
 * Package definition
 */
package com.almis.awe.model.dto;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * File Imports
 */

/**
 * DataList Class
 *
 * Data list formatted as an standard data output
 *
 *
 * @author Pablo GARCIA - 24/JUN/2010
 */
public class DataList implements Serializable {

  private static final long serialVersionUID = 1L;

  // Total pages
  private long total;

  // Page number
  private long page;

  // Total records
  private long records;

  // Row list
  private List<Map<String, CellData>> rows;

  /**
   * Object initialization
   */
  public DataList() {
    // Datalist initialization
    total = 1;
    page = 1;
    records = 0;
    rows = new ArrayList<>();
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public DataList(DataList other) {
    this.total = other.total;
    this.page = other.page;
    this.records = other.records;
    if (other.rows != null) {
      this.rows = new ArrayList<>();
      for (Map<String, CellData> row : other.rows) {
        Map newRow = new HashMap<>();
        for (Map.Entry<String, CellData> cell : row.entrySet()) {
          newRow.put(cell.getKey(), new CellData(cell.getValue()));
        }
        this.rows.add(newRow);
      }
    }
  }

  /**
   * Returns the total pages number
   *
   * @return Total pages
   */
  public long getTotal() {
    return total;
  }

  /**
   * Stores the total pages number
   *
   * @param total Total pages
   */
  public void setTotal(long total) {
    this.total = total;
  }

  /**
   * Returns the page number
   *
   * @return Page number
   */
  public long getPage() {
    return page;
  }

  /**
   * Stores the page number
   *
   * @param page Page number
   */
  public void setPage(long page) {
    this.page = page;
  }

  /**
   * Returns the datalist total records
   *
   * @return Total records
   */
  public long getRecords() {
    return records;
  }

  /**
   * Stores the total records
   *
   * @param records Total records
   */
  public void setRecords(long records) {
    this.records = records;
  }

  /**
   * Returns the data row List
   *
   * @return Data Row List
   */
  public List<Map<String, CellData>> getRows() {
    return rows;
  }

  /**
   * Stores the data row list
   *
   * @param rows Data Row List
   */
  public void setRows(List<Map<String, CellData>> rows) {
    this.rows = rows;
  }

  /**
   * Add a row
   *
   * @param row Row to add
   */
  public void addRow(Map<String, CellData> row) {
    getRows().add(row);
    setRecords(getRows().size());
  }

  /**
   * Update a row
   *
   * @param row       Row to update
   * @param rowNumber Row index
   */
  public void updateRow(Map<String, CellData> row, int rowNumber) {
    if (rowNumber < getRows().size()) {
      getRows().set(rowNumber, row);
    }
  }

  /**
   * Delete a row
   *
   * @param rowNumber Row index
   */
  public void deleteRow(int rowNumber) {
    if (rowNumber < getRows().size()) {
      getRows().remove(rowNumber);
    }
  }

  private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {

    stream.writeLong(total);
    stream.writeLong(page);
    stream.writeLong(records);
    stream.writeObject(new ArrayList<>(rows));
  }

  private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {

    this.total = stream.readLong();
    this.page = stream.readLong();
    this.records = stream.readLong();
    this.rows = (List<Map<String, CellData>>)stream.readObject();
  }
}
