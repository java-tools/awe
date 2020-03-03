package com.almis.awe.service.data.processor;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweContextAware;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.entities.queries.SqlField;
import com.almis.awe.model.entities.queries.Totalize;
import com.almis.awe.model.entities.queries.TotalizeBy;
import com.almis.awe.model.entities.queries.TotalizeField;
import com.almis.awe.model.util.data.NumericUtil;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TransformCellProcessor class
 */
public class TotalizeColumnProcessor implements ColumnProcessor, AweContextAware {
  private Totalize totalize;
  Map<String, CellData> totalizeValues = null;
  Map<String, String> totalizeKeys = null;
  List<SqlField> fieldList = null;
  private AweElements elements;

  /**
   * Set Awe Elements
   *
   * @return Totalize Column Processor
   */
  public TotalizeColumnProcessor setElements(AweElements elements) {
    this.elements = elements;
    return this;
  }

  /**
   * Retrieve Awe Elements
   *
   * @return Awe Elements
   */
  private AweElements getElements() throws AWException {
    if (elements == null) {
      throw new AWException("No elements defined", "Define elements before building the totalize processor");
    }
    return elements;
  }

  /**
   * Set transform field
   *
   * @param totalize Totalize field
   * @return TotalizeColumnProcessor
   */
  public TotalizeColumnProcessor setTotalize(Totalize totalize) {
    this.totalize = totalize;
    return this;
  }

  /**
   * Set field list
   *
   * @param fieldList Field list
   * @return TotalizeColumnProcessor
   */
  public TotalizeColumnProcessor setFieldList(List<SqlField> fieldList) {
    this.fieldList = fieldList;
    return this;
  }

  /**
   * Check if add a new line
   *
   * @param row Row to check
   * @return Add a new line
   */
  public boolean checkNewLine(Map<String, CellData> row) {
    boolean addLine = row == null;
    // If totalizeKeys is null, generate it
    if (totalizeKeys == null) {
      totalizeKeys = new HashMap<>();
    }

    // Check totalizeBy fields
    List<TotalizeBy> totalizeByList = totalize.getTotalizeByList();

    // Check if one of totalize by fields is different
    if (totalizeByList != null && row != null) {
      // Check totalize by
      for (TotalizeBy totalizeBy : totalizeByList) {
        addLine = checkTotalizedLine(row, totalizeBy, addLine);
      }
    }
    return addLine;
  }

  /**
   * Check totalized line
   *
   * @param row        Row to check
   * @param totalizeBy Totalize by
   * @param addLine    Add line value
   * @return Add line or not
   */
  private boolean checkTotalizedLine(Map<String, CellData> row, TotalizeBy totalizeBy, boolean addLine) {
    String lastValue = row.get(totalizeBy.getField()) != null ? row.get(totalizeBy.getField()).getStringValue() : null;
    if (totalizeKeys.containsKey(totalizeBy.getField())) {
      String nextValue = totalizeKeys.get(totalizeBy.getField());
      if (nextValue != null && !nextValue.equals(lastValue)) {
        addLine = true;
        totalizeKeys.put(totalizeBy.getField(), lastValue);
      }
    } else if (lastValue != null) {
      totalizeKeys.put(totalizeBy.getField(), lastValue);
    }
    return addLine;
  }

  /**
   * Retrieve a new totalize line
   *
   * @return New totalize line
   */
  private Map<String, CellData> getNewLine() {
    // Create new row
    Map<String, CellData> totalizeRow = new HashMap<>();

    for (SqlField field : fieldList) {
      String columnIdentifier = "";
      String totalizeIdentifier = "-" + totalize.getFunction();
      CellData cell = null;

      TransformCellProcessor transformProcessor = new TransformCellProcessor()
        .setElements(elements)
        .setField(field);

      columnIdentifier = transformProcessor.getColumnIdentifier();
      totalizeIdentifier = columnIdentifier + totalizeIdentifier;

      // Get totalize values
      if (totalizeValues.containsKey(totalizeIdentifier)) {
        // Format output data
        cell = totalizeValues.get(totalizeIdentifier);

      } else {
        cell = new CellData();
      }

      totalizeRow.put(columnIdentifier, cell);
    }
    totalizeValues.clear();
    return totalizeRow;
  }

  /**
   * Add a new line with values
   *
   * @param row  Row data
   * @param list Row list
   * @throws AWException Error adding a new line
   */
  public void addNewLine(Map<String, CellData> row, List<Map<String, CellData>> list) throws AWException {
    // Create new row
    Map<String, CellData> newRow = getNewLine();

    // Add label
    newRow.put(totalize.getField(), new CellData(getElements().getLocaleWithLanguage(totalize.getLabel(), getElements().getLanguage())));

    // Add style value
    if (totalize.getStyle() != null) {
      newRow.put(AweConstants.DATALIST_STYLE_FIELD, new CellData(totalize.getStyle()));
    }

    // Add row ID
    newRow.put("id", new CellData(String.valueOf("TOT-" + list.size())));

    // Add row list
    list.add(newRow);
  }

  /**
   * Retrieve column identifier
   *
   * @return Column identifier
   */
  public String getColumnIdentifier() {
    return totalize.getField();
  }

  /**
   * Process row
   *
   * @param row Row to process
   * @return Null (Interface requirements)
   * @throws AWException Error processing row
   */
  public CellData process(Map<String, CellData> row) throws AWException {

    if (totalize == null) {
      throw new NullPointerException("No totalize defined");
    }

    if (fieldList == null) {
      throw new NullPointerException("No field list defined");
    }

    // Calculate values
    if (totalize.getTotalizeFieldList() != null && row != null) {
      if (totalizeValues == null) {
        totalizeValues = new HashMap<>();
      }

      // For each field to totalize, calculate values
      for (TotalizeField totalizeField : totalize.getTotalizeFieldList()) {
        calculateTotalizedRow(row, totalizeField);
      }
    }

    // Return null
    return null;
  }

  /**
   * Calculate totalized row
   *
   * @param row           Row to be calculated
   * @param totalizeField Totalize field
   * @throws AWException Error generating values
   */
  private void calculateTotalizedRow(Map<String, CellData> row, TotalizeField totalizeField) throws AWException {
    // Big decimal treatment. Choose number type and cast to BigDecimal
    Object value = row.get(totalizeField.getField()).getObjectValue();
    Double doubleValue = fixDoubleValue(row.get(totalizeField.getField()).getDoubleValue(),
      row.get(totalizeField.getField()).getStringValue());

    if (totalizeValues.containsKey(totalizeField.getField() + "-CNT")) {
      Double totVal = totalizeValues.get(totalizeField.getField() + "-SUM").getDoubleValue();
      Double maxVal = totalizeValues.get(totalizeField.getField() + "-MAX").getDoubleValue();
      Double minVal = totalizeValues.get(totalizeField.getField() + "-MIN").getDoubleValue();
      Integer cntVal = totalizeValues.get(totalizeField.getField() + "-CNT").getIntegerValue();

      totVal = totVal == null ? doubleValue : totVal + doubleValue;
      cntVal++;
      maxVal = maxVal == null ? doubleValue : Math.max(maxVal, doubleValue);
      minVal = minVal == null ? doubleValue : Math.min(minVal, doubleValue);
      Double avgVal = Math.ceil(totVal / cntVal.longValue());

      // Put value on list
      totalizeValues.put(totalizeField.getField() + "-SUM", new CellData(totVal));
      totalizeValues.put(totalizeField.getField() + "-AVG", new CellData(avgVal));
      totalizeValues.put(totalizeField.getField() + "-MAX", new CellData(maxVal));
      totalizeValues.put(totalizeField.getField() + "-MIN", new CellData(minVal));
      totalizeValues.put(totalizeField.getField() + "-CNT", new CellData(cntVal));
    } else if (!totalizeValues.containsKey(totalizeField.getField() + "-CNT")) {
      // Put value on list
      totalizeValues.put(totalizeField.getField() + "-SUM", new CellData(doubleValue));
      totalizeValues.put(totalizeField.getField() + "-AVG", new CellData(doubleValue));
      totalizeValues.put(totalizeField.getField() + "-MAX", new CellData(doubleValue));
      totalizeValues.put(totalizeField.getField() + "-MIN", new CellData(doubleValue));
      totalizeValues.put(totalizeField.getField() + "-CNT", new CellData(value == null ? 0 : 1));
    }
  }

  /**
   * Fix double value with string value
   *
   * @param doubleValue Double value
   * @param stringValue String value
   * @return Double value fixed
   * @throws AWException
   */
  private Double fixDoubleValue(Double doubleValue, String stringValue) throws AWException {
    if (doubleValue == null) {
      if (stringValue.isEmpty()) {
        return 0.0;
      } else {
        try {
          return NumericUtil.parseNumericString(stringValue).doubleValue();
        } catch (ParseException exc) {
          throw new AWException(getElements().getLocaleWithLanguage("ERROR_TITLE_PARSING_TEXT", getElements().getLanguage()),
            getElements().getLocaleWithLanguage("ERROR_MESSAGE_PARSING_TEXT", getElements().getLanguage(),stringValue, "double"), exc);
        }
      }
    } else {
      return doubleValue;
    }
  }
}
