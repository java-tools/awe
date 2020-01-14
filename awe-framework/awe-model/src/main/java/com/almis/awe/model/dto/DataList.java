package com.almis.awe.model.dto;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.util.data.ListUtil;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DataList Class
 *
 * Data list formatted as an standard data output
 *
 *
 * @author Pablo GARCIA - 24/JUN/2010
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder(toBuilder = true)
public class DataList implements Serializable, Copyable {
  // Total pages
  @Builder.Default private long total = 1;

  // Page number
  @Builder.Default private long page = 1;

  // Total records
  @Builder.Default private long records = 0;

  // Row list
  @Builder.Default private List<Map<String, CellData>> rows = new ArrayList<>();

  /**
   * Copy constructor
   * @param other Other datalist
   */
  public DataList(DataList other) {
    this.total = other.getTotal();
    this.page = other.getPage();
    this.records = other.getRecords();
    this.rows = ListUtil.copyDataListRows(other.getRows());
  }

  @Override
  public DataList copy() throws AWException {
    return this.toBuilder()
      .rows(ListUtil.copyDataListRows(rows))
      .build();
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
      setRecords(getRows().size());
    }
  }

  private void writeObject(@NonNull ObjectOutputStream stream) throws IOException {
    stream.writeLong(total);
    stream.writeLong(page);
    stream.writeLong(records);
    stream.writeObject(new ArrayList<>(rows));
  }

  private void readObject(@NonNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
    this.total = stream.readLong();
    this.page = stream.readLong();
    this.records = stream.readLong();
    this.rows = (List<Map<String, CellData>>)stream.readObject();
  }
}
