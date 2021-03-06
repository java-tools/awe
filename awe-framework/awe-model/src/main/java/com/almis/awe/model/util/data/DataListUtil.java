package com.almis.awe.model.util.data;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.NonNull;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiPredicate;

/**
 * DataList Class
 * Data list formatted as an standard data output
 *
 * @author Pablo GARCIA - 24/JUN/2010
 */
public final class DataListUtil {

  /**
   * Private constructor to hide the implicit one
   */
  private DataListUtil() {
  }

  /**
   * Returns the value data by (rowNumber, hashKey)
   *
   * @param list      DataList
   * @param rowNumber Row number
   * @param key       Column name
   * @return Single Value Data
   */
  public static String getData(DataList list, Integer rowNumber, String key) {
    // Variable definition
    Map<String, CellData> row;
    String cellString = "";

    row = getRow(list, rowNumber);
    CellData cell = row.get(key);
    if (cell != null) {
      cellString = cell.getStringValue();
    }

    return cellString;
  }

  /**
   * Returns the cellData value by (rowNumber, columnName)
   *
   * @param list       DataList
   * @param rowNumber  Row number
   * @param columnName Column name
   * @return Single cellData
   */
  public static CellData getCellData(DataList list, Integer rowNumber, String columnName) {
    return getRow(list, rowNumber).get(columnName);
  }

  /**
   * Returns the HasMap Row by (rowNumber)
   *
   * @param list      DataList
   * @param rowNumber Row number
   * @return Single Hash Map
   */
  public static Map<String, CellData> getRow(DataList list, int rowNumber) {
    return list.getRows().get(rowNumber);
  }

  /**
   * Retrieve the row index for an identifier
   *
   * @param list             DataList
   * @param columnIdentifier Column identifier
   * @param rowIdentifier    Row identifier
   * @return Row index
   */
  public static int getRowIndex(DataList list, String columnIdentifier, Object rowIdentifier) {
    // Init variables
    int index = 0;
    for (Map<String, CellData> row : list.getRows()) {
      CellData cell = row.get(columnIdentifier);
      if (cell != null && cell.getObjectValue().equals(rowIdentifier)) {
        return index;
      }
      index++;
    }

    return -1;
  }

  /**
   * Set column Name and it's value per line
   *
   * @param list         DataList
   * @param columnName   Column name (alias)
   * @param defaultValue Default value
   */
  public static void addColumn(DataList list, String columnName, String defaultValue) {
    CellData cell;

    // Add alias row by row
    for (Map<String, CellData> row : list.getRows()) {
      // Define cell
      cell = new CellData();

      // Add default value
      if (defaultValue != null) {
        cell.setValue(defaultValue);
      }

      // Store cell
      row.put(columnName, cell);
    }
  }

  /**
   * Copy column from other datalist
   *
   * @param target           Target datalist
   * @param targetColumnName Target column
   * @param source           Source datalist
   * @param sourceColumnName Source column
   */
  public static void copyColumn(DataList target, String targetColumnName, DataList source, String sourceColumnName) {
    addColumn(target, targetColumnName, getColumn(source, sourceColumnName));
  }

  /**
   * Rename column in datalist
   *
   * @param dataList         DataList
   * @param sourceColumnName Source column name
   * @param targetColumnName Target column name
   */
  public static void renameColumn(DataList dataList, String sourceColumnName, String targetColumnName) {
    if (dataList.getRows() != null) {
      for (Map<String, CellData> row : dataList.getRows()) {
        row.put(targetColumnName, row.get(sourceColumnName));
        row.remove(sourceColumnName);
      }
    }
  }

  /**
   * Set column Name and it's value per line
   *
   * @param list         Datalist
   * @param columnName   Column name (alias)
   * @param columnValues List with column values
   */
  public static void addColumn(DataList list, String columnName, List<?> columnValues) {
    Integer rowIdentifier = 0;

    // Add alias row by row
    for (Object columnData : columnValues) {
      CellData cell;
      if (columnData instanceof CellData) {
        cell = (CellData) columnData;
      } else {
        cell = new CellData(columnData);
      }

      // Add cell to row
      addCellToRow(list, columnName, rowIdentifier, cell);

      // Increase row identifier
      rowIdentifier++;
    }
  }

  /**
   * Set column Name and it's value per line
   *
   * @param list      DataList
   * @param column    Column name
   * @param rowNumber Row number
   * @param cell      Cell data
   */
  private static void addCellToRow(DataList list, String column, Integer rowNumber, CellData cell) {
    // If size is lower or equal than current rowId, add one row
    if (list.getRows().size() <= rowNumber) {
      list.getRows().add(new HashMap<>());
    }

    // Store cell
    list.getRows().get(rowNumber).put(column, cell);

    // Set records
    list.setRecords(list.getRows().size());
  }

  /**
   * Returns the datalist as a string array
   *
   * @param dataList   Datalist
   * @param columnList Column order
   * @return DataList as a string array
   */
  public static String[] getDataAsArray(DataList dataList, List<String> columnList) {
    List<String> list = new ArrayList<>();

    for (Map<String, CellData> row : dataList.getRows()) {
      for (String val : columnList) {
        CellData cell = row.get(val);
        list.add(cell != null ? cell.getStringValue() : "");
      }
    }
    String[] dataArray = new String[list.size()];
    return list.toArray(dataArray);
  }

  /**
   * Return the datalist as bean list
   *
   * @param dataList  datalist
   * @param beanClass bean class
   * @param <T>       class type
   * @return bean list
   * @throws AWException AWE exception
   */
  public static <T> List<T> asBeanList(@NonNull DataList dataList, Class<T> beanClass) throws AWException {
    List<T> list = new ArrayList<>();
    T rowBean;

    for (Map<String, CellData> row : dataList.getRows()) {
      try {
        // Generate row bean
        rowBean = beanClass.getDeclaredConstructor().newInstance();
      } catch (Exception exc) {
        throw new AWException("Error converting datalist into a bean list", "Cannot create instance of " + beanClass.getSimpleName(), exc);
      }

      // Set field value if found in row
      for (Field field : getAllFields(beanClass)) {
        if (row.containsKey(field.getName())) {
          PropertyAccessor rowBeanAccesor = PropertyAccessorFactory.forDirectFieldAccess(rowBean);
          rowBeanAccesor.setPropertyValue(field.getName(), row.get(field.getName()).getObjectValue());
        }
      }

      // Store row bean
      list.add(rowBean);
    }
    return list;
  }

  /**
   * Return the datalist as bean list
   *
   * @param beanList bean class
   * @param <T>       class type
   * @return bean list
   */
  public static <T> DataList fromBeanList(List<T> beanList) {
    DataList dataList = DataList.builder().build();

    for (T bean : beanList) {
      Map<String, CellData> row = new HashMap<>();
      PropertyAccessor rowBeanAccesor = PropertyAccessorFactory.forDirectFieldAccess(bean);

      // Set field value if found in row
      for (Field field : getAllFields(bean.getClass())) {
        row.put(field.getName(), new CellData(rowBeanAccesor.getPropertyValue(field.getName())));
      }

      dataList.addRow(row);
    }
    return dataList;
  }

  private static List<Field> getAllFields(Class<?> type) {
    List<Field> fields = new ArrayList<>(Arrays.asList(type.getDeclaredFields()));

    if (type.getSuperclass() != null) {
      fields.addAll(getAllFields(type.getSuperclass()));
    }

    return fields;
  }

  /**
   * Add a column with one row value
   *
   * @param list       DataList
   * @param columnName Column name (alias)
   * @param cellValue  Cell value
   */
  public static void addColumnWithOneRow(DataList list, String columnName, Object cellValue) {
    // Add cell to row
    addCellToRow(list, columnName, 0, new CellData(cellValue));
  }

  /**
   * Retrieve a column data
   *
   * @param list       DataList
   * @param columnName Column name (alias)
   * @return Column object list
   */
  public static List<CellData> getColumn(DataList list, String columnName) {
    List<CellData> columnData = new ArrayList<>();

    // Add alias row by row
    for (Map<String, CellData> row : list.getRows()) {
      columnData.add(new CellData(row.get(columnName)));
    }

    return columnData;
  }

  /**
   * Retrieve a column data as QueryParameter
   *
   * @param list       DataList
   * @param columnName Column name (alias)
   * @return Column object list
   */
  public static ArrayNode getColumnAsArrayNode(DataList list, String columnName) {
    ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
    ObjectMapper mapper = new ObjectMapper();

    // Add alias row by row
    for (Map<String, CellData> row : list.getRows()) {
      arrayNode.add(mapper.valueToTree(row.get(columnName)));
    }

    return arrayNode;
  }

  /**
   * Sort datalist
   *
   * @param list     DataList to sort
   * @param sortList Sort by field list
   */
  public static void sort(DataList list, List<SortColumn> sortList) {
    list.getRows().sort(new CompareRow(sortList));
  }

  /**
   * Sort datalist
   *
   * @param list      DataList to sort
   * @param columnId  Sort by field list
   * @param direction Sort direction
   */
  public static void sort(DataList list, String columnId, String direction) {
    List<SortColumn> sortColumnList = new ArrayList<>();
    SortColumn sortColumn = new SortColumn(columnId, direction);
    sortColumnList.add(sortColumn);
    list.getRows().sort(new CompareRow(sortColumnList));
  }

  /**
   * Sort datalist set nulls values position
   *
   * @param list DataList to sort
   * @param sortColumnList List with sort columns
   * @param nullsFirst Null values at first
   */
  public static void sort(DataList list, List<SortColumn> sortColumnList, boolean nullsFirst) {
      list.getRows().sort(new CompareRow(sortColumnList, nullsFirst));
  }

  /**
   * Remove the rows whose column value is distinct to the value
   *
   * @param list   DataList to filter
   * @param column column to check
   * @param value  value to check
   */
  public static void filter(DataList list, String column, String value) {
    filter(list, new FilterColumn(column, value));
  }

  /**
   * Remove the rows whose column value is distinct to the value
   *
   * @param list          DataList to filter
   * @param filterColumns columns to check
   */
  public static void filter(DataList list, FilterColumn... filterColumns) {
    filterDataList(list, String::equalsIgnoreCase, filterColumns);
  }

  /**
   * Remove the rows whose column value doesn't contains the defined values
   *
   * @param list          DataList to filter
   * @param filterColumns columns to check
   */
  public static void filterContains(DataList list, FilterColumn... filterColumns) {
    filterDataList(list, (String a, String b) -> a.toUpperCase().contains(b.toUpperCase()), filterColumns);
  }

  /**
   * Filter a DataList
   *
   * @param list          DataList to filter
   * @param comparator    Comparator
   * @param filterColumns Columns to filter
   */
  private static void filterDataList(DataList list, BiPredicate<String, String> comparator, FilterColumn... filterColumns) {
    List<Map<String, CellData>> newRows = new ArrayList<>();
    for (Map<String, CellData> row : list.getRows()) {
      boolean add = false;
      for (FilterColumn filterColumn : filterColumns) {
        if (row.containsKey(filterColumn.getColumnId()) && comparator.test(row.get(filterColumn.getColumnId()).getStringValue().toUpperCase(), filterColumn.getValue().toUpperCase())) {
          add = true;
        }
      }

      if (add) {
        newRows.add(row);
      }
    }
    // Set the new list
    list.setRows(newRows);
    list.setRecords(newRows.size());
  }

  /**
   * Retrieve dataList column names
   *
   * @param list Datalist
   * @return Column list
   */
  public static List<String> getColumnList(DataList list) {
    // Variable Definition
    List<String> columnList = new ArrayList<>();

    // Get first row
    if (!list.getRows().isEmpty()) {
      Map<String, CellData> row = getRow(list, 0);

      // Store keyset as columnList
      columnList.addAll(row.keySet());
    }
    return columnList;
  }

  /**
   * Keeps only distinct values of given fields
   *
   * @param list        DataList
   * @param sortColumns Sort by field list
   */
  public static void distinct(DataList list, List<SortColumn> sortColumns) {
    CompareRow comparator = new CompareRow(sortColumns);
    List<Map<String, CellData>> newRows = new ArrayList<>();
    for (Map<String, CellData> row : list.getRows()) {
      if (!in(newRows, row, comparator)) {
        newRows.add((Map<String, CellData>) ((HashMap<String, CellData>) row).clone());
      }
    }

    // Set rows and records
    list.setRows(newRows);
    list.setRecords(list.getRows().size());
  }

  /**
   * Check if a row is inside another list
   *
   * @param list       Row list
   * @param rowToCheck row to check
   * @param comparator row comparator
   */
  private static boolean in(List<Map<String, CellData>> list, Map<String, CellData> rowToCheck, CompareRow comparator) {
    for (Map<String, CellData> row : list) {
      if (comparator.compare(row, rowToCheck) == 0) {
        return true;
      }
    }
    return false;
  }
}
