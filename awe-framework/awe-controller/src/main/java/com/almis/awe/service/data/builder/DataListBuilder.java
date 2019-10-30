package com.almis.awe.service.data.builder;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.FilterColumn;
import com.almis.awe.model.dto.SortColumn;
import com.almis.awe.model.entities.Global;
import com.almis.awe.model.entities.queries.Field;
import com.almis.awe.model.entities.queries.SqlField;
import com.almis.awe.model.type.CellDataType;
import com.almis.awe.service.data.processor.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.QTuple;
import com.querydsl.core.types.dsl.SimpleOperation;

import java.util.*;

import static com.almis.awe.model.constant.AweConstants.*;

/*
 * File Imports
 */

/**
 * DataList Builder
 * <p>
 * Builder class to generate DataLists
 *
 * @author Pablo GARCIA - 20/MAR/2017
 */
public class DataListBuilder extends ServiceConfig {

  private DataList dataList = null;
  private List<DataList> dataListList = null;
  private Map<String, List<CellData>> columnList = null;
  private boolean sort = false;
  private boolean distinct = false;
  private boolean filter = false;
  private boolean compute = false;
  private boolean transform = false;
  private boolean translate = false;
  private boolean compound = false;
  private boolean identifier = false;
  private boolean totalize = false;
  private boolean noPrint = false;
  private boolean paginate = false;
  private List<Tuple> queryResult = null;
  private List<Global> enumQueryResult = null;
  private List<String> serviceQueryResult = null;
  private List<SqlField> fieldList = null;
  private Expression<?> projection = null;
  private List<SortColumn> sortList = null;
  private List<SortColumn> distinctList = null;
  private List<FilterColumn> filterList = null;
  private List<TotalizeColumnProcessor> totalizeList = null;
  private List<ComputedColumnProcessor> computedList = null;
  private List<TransformCellProcessor> transformList = null;
  private List<TranslateCellProcessor> translateList = null;
  private List<CompoundColumnProcessor> compoundList = null;
  private List<String> noPrintList = null;
  private long max = -1;
  private long page = -1;
  private long records = -1;

  /**
   * Adds a EnumQueryResult
   *
   * @param enumQueryResult Enumerated query output
   * @return DataListBuilder
   */
  public DataListBuilder setEnumQueryResult(List<Global> enumQueryResult) {
    this.enumQueryResult = enumQueryResult;
    return this;
  }

  /**
   * Adds a ServiceQueryResult
   *
   * @param serviceQueryResult Service query output
   * @return DataListBuilder
   */
  public DataListBuilder setServiceQueryResult(final String[] serviceQueryResult) {
    this.serviceQueryResult = serviceQueryResult == null ? null : Arrays.asList(serviceQueryResult);
    return this;
  }

  /**
   * Adds a the list of Field needed for the service query result
   *
   * @param fieldList Field list
   * @return DataListBuilder
   */
  public DataListBuilder setFieldList(List<SqlField> fieldList) {
    this.fieldList = fieldList;
    return this;
  }

  /**
   * Adds a QueryResult
   *
   * @param queryResult Query result
   * @return DataListBuilder
   */
  public DataListBuilder setQueryResult(List<Tuple> queryResult) {
    this.queryResult = queryResult;
    return this;
  }

  /**
   * Adds the query projection containing information about the columns returned
   *
   * @param projection Query projection
   * @return DataListBuilder
   */
  public DataListBuilder setQueryProjection(Expression<?> projection) {
    this.projection = projection;
    return this;
  }

  /**
   * Set the response as a datalist
   *
   * @param dataList DataList
   * @return DataListBuilder
   */
  public DataListBuilder setDataList(DataList dataList) {
    this.dataList = dataList;
    setPage(dataList.getPage());
    setRecords(dataList.getRecords());
    return this;
  }

  /**
   * Add a datalist
   *
   * @param dataList DataList
   * @return DataListBuilder
   */
  public DataListBuilder addDataList(DataList dataList) {
    if (dataListList == null) {
      dataListList = new ArrayList<>();
    }
    dataListList.add(dataList);
    return this;
  }

  /**
   * Add a computed processor
   *
   * @param computed Computed
   * @return DataListBuilder
   */
  public DataListBuilder addComputed(ComputedColumnProcessor computed) {
    if (computedList == null) {
      computedList = new ArrayList<>();
    }
    computedList.add(computed);
    this.compute = true;
    return this;
  }

  /**
   * Add a compound processor
   *
   * @param compound Compound processor
   * @return DataListBuilder
   */
  public DataListBuilder addCompound(CompoundColumnProcessor compound) {
    if (compoundList == null) {
      compoundList = new ArrayList<>();
    }
    compoundList.add(compound);
    this.compound = true;
    return this;
  }

  /**
   * Add a transform processor
   *
   * @param transform Transform processor
   * @return DataListBuilder
   */
  public DataListBuilder addTransform(TransformCellProcessor transform) {
    if (transformList == null) {
      transformList = new ArrayList<>();
    }
    transformList.add(transform);
    this.transform = true;
    return this;
  }

  /**
   * Add a translate processor
   *
   * @param translate Translate processor
   * @return DataListBuilder
   */
  public DataListBuilder addTranslate(TranslateCellProcessor translate) {
    if (translateList == null) {
      translateList = new ArrayList<>();
    }
    translateList.add(translate);
    this.translate = true;
    return this;
  }

  /**
   * Add a no print field
   *
   * @param noPrint No print field alias
   * @return DataListBuilder
   */
  public DataListBuilder addNoPrint(String noPrint) {
    if (noPrintList == null) {
      noPrintList = new ArrayList<>();
    }
    noPrintList.add(noPrint);
    this.noPrint = true;
    return this;
  }

  /**
   * Add a totalize processor
   *
   * @param totalize Totalize processor
   * @return DataListBuilder
   */
  public DataListBuilder addTotalize(TotalizeColumnProcessor totalize) {
    if (totalizeList == null) {
      totalizeList = new ArrayList<>();
    }
    totalizeList.add(totalize);
    this.totalize = true;
    return this;
  }

  /**
   * Add a column to datalist
   *
   * @param columnId Column id
   * @param data     Column data
   * @param type     Column data type
   * @return DataListBuilder
   */
  public DataListBuilder addColumn(String columnId, List<? extends Object> data, String type) {
    if (columnList == null) {
      columnList = new HashMap<>();
    }

    // Generate a celldata list
    List<CellData> columnData = new ArrayList<>();
    for (Object value : data) {
      CellData cellData = new CellData(value).setType(CellDataType.valueOf(type));
      columnData.add(cellData);
    }
    columnList.put(columnId, columnData);
    return this;
  }

  /**
   * Manage pagination or not
   *
   * @param paginate Paginate
   * @return DataListBuilder
   */
  public DataListBuilder paginate(boolean paginate) {
    this.paginate = paginate;
    return this;
  }

  /**
   * Set datalist page
   *
   * @param page Page number
   * @return DataListBuilder
   */
  public DataListBuilder setPage(Long page) {
    this.page = page;
    return this;
  }

  /**
   * Set datalist max records per page
   *
   * @param max Max elements per page
   * @return DataListBuilder
   */
  public DataListBuilder setMax(Long max) {
    this.max = max;
    return this;
  }

  /**
   * Set datalist records
   *
   * @param records Total records
   * @return DataListBuilder
   */
  public DataListBuilder setRecords(Long records) {
    this.records = records;
    return this;
  }

  /**
   * Get datalist page
   *
   * @return records
   */
  private long getPage() {
    return this.page < 1 ? 1 : this.page;
  }

  /**
   * Get datalist records
   *
   * @return records
   */
  private long getRecords() {
    return this.records < 0 ? dataList.getRows().size() : this.records;
  }

  /**
   * Get datalist records
   *
   * @return records
   */
  private long getTotalPages() {
    return Math.max(max <= 0 ? 1 : (long) Math.ceil((double) getRecords() / max), 1);
  }

  /**
   * Set datalist max records per page
   *
   * @param sortList Sort field list
   * @return DataListBuilder
   */
  public DataListBuilder sort(List<SortColumn> sortList) {
    this.sortList = sortList;
    this.sort = true;
    return this;
  }

  /**
   * Filter datalist
   *
   * @param column Column name
   * @param value  filter value
   * @return DataListBuilder
   */
  public DataListBuilder filter(String column, String value) {
    if (this.filterList == null) {
      this.filterList = new ArrayList<>();
    }
    this.filterList.add(new FilterColumn(column, value));
    this.filter = true;
    return this;
  }

  /**
   * Filter datalist
   *
   * @param filterList Filter list
   * @return DataListBuilder
   */
  public DataListBuilder filter(List<FilterColumn> filterList) {
    this.filterList = filterList;
    this.filter = true;
    return this;
  }

  /**
   * Set datalist max records per page
   *
   * @param distinctList Sort field list
   * @return DataListBuilder
   */
  public DataListBuilder distinct(List<SortColumn> distinctList) {
    this.distinctList = distinctList;
    this.distinct = true;
    return this;
  }

  /**
   * Generate identifiers
   *
   * @return DataListBuilder
   */
  public DataListBuilder generateIdentifiers() {
    this.identifier = true;
    return this;
  }

  /**
   * Totalize the datalist
   *
   * @param totalizeList Totalize list
   * @return DataListBuilder
   */
  public DataListBuilder totalize(List<TotalizeColumnProcessor> totalizeList) {
    this.totalizeList = totalizeList;
    this.totalize = true;
    return this;
  }

  /**
   * Build datalist
   *
   * @return DataListBuilder
   * @throws AWException Error building datalist
   */
  public DataList build() throws AWException {

    // Get build data
    extractData();

    // Transform data
    transformData();

    // Set page, records and total
    dataList.setPage(getPage());
    dataList.setRecords(getRecords());
    dataList.setTotal(getTotalPages());

    return dataList;
  }

  /**
   * Extract data
   */
  private void extractData() {
    dataList = dataList == null ? new DataList() : dataList;

    // Manage results from SQL
    if (queryResult != null) {
      generateFromQueryResult();
    }

    // Manage results from enumerated
    if (enumQueryResult != null) {
      generateFromEnumQueryResult();
    }

    // Manage results from services
    if (serviceQueryResult != null) {
      generateFromServiceQueryResult();
    }

    if (columnList != null) {
      generateFromColumnListResult();
    }

    // Generate the list
    if (dataListList != null) {
      doMerge();

      // Update records (new records on merge)
      setRecords((long) dataList.getRows().size());
    }
  }

  /**
   * Transform data
   *
   * @throws AWException Error building datalist
   */
  private void transformData() throws AWException {

    // Filter the list
    if (filter && filterList != null) {
      doFilter();
    }

    // Get distinct values
    if (distinct && distinctList != null) {
      doDistinct();
    }

    // Sort the final list
    if (sort && sortList != null) {
      doSort();
    }

    // Totalize
    if (totalize && totalizeList != null) {
      doTotalize();

      // Update records (new records on totalize)
      setRecords((long) dataList.getRows().size());
    }

    // Paginate if rows are more than max
    if (paginate) {
      doPaginate();
    }

    // Calculate computed, transform, compounds and identifiers
    if (compute || transform || translate || compound || identifier || noPrint) {
      doEvaluate();
    }
  }

  /**
   * Generate datalist data from query result
   */
  private void generateFromQueryResult() {
    if (this.projection == null) {
      throw new NullPointerException(getLocale("ERROR_TITLE_NOT_DEFINED", "projection"));
    }

    // Retrieve column names from projection
    List<String> columnNames = new ArrayList<>();
    List<Expression<?>> columns = ((QTuple) projection).getArgs();
    for (Expression<?> columnOperation : columns) {
      if (columnOperation instanceof SimpleOperation) {
        List<Expression<?>> columnData = ((SimpleOperation<?>) columnOperation).getArgs();
        columnNames.add(columnData.get(columnData.size() - 1).toString());
      } else {
        columnNames.add(columnOperation.toString());
      }
    }

    // For each row, create a map and add it to the datalist
    for (Tuple tuple : queryResult) {
      Map<String, CellData> row = new HashMap<>();
      for (int i = 0; i < columnNames.size(); i++) {
        row.put(columnNames.get(i), new CellData(tuple.get(i, Object.class)));
      }
      dataList.addRow(row);
    }
  }

  /**
   * Generate datalist data from enum query result
   */
  private void generateFromEnumQueryResult() {
    Integer rowNumber = 1;
    for (Global option : enumQueryResult) {

      Map<String, CellData> row = new HashMap<>();
      row.put(DATALIST_IDENTIFIER, new CellData(rowNumber));

      if (option.getLabel() != null) {
        row.put(JSON_LABEL_PARAMETER, new CellData(option.getLabel()));
      }

      if (option.getValue() != null) {
        row.put(JSON_VALUE_PARAMETER, new CellData(option.getValue()));
      }

      dataList.addRow(row);
      rowNumber++;
    }
    dataList.setRecords(enumQueryResult.size());
  }

  /**
   * Generate datalist data from a column list
   */
  private void generateFromColumnListResult() {
    List<Map<String, CellData>> rowList = new ArrayList<>();
    for (Map.Entry<String, List<CellData>> columnListEntry : columnList.entrySet()) {
      int rowIndex = 0;
      for (CellData cellData : columnListEntry.getValue()) {
        Map<String, CellData> row;

        // Add row if it doesn't exist, else retrieve it
        if (rowList.size() <= rowIndex) {
          row = new HashMap<>();
          rowList.add(row);
        } else {
          row = rowList.get(rowIndex);
        }

        // Add cell to row
        row.put(columnListEntry.getKey(), cellData);
        rowIndex++;
      }
    }
    dataList.setRows(rowList);
    dataList.setRecords(rowList.size());
  }

  /**
   * Generate datalist data from service query result
   */
  private void generateFromServiceQueryResult() {
    Integer totalRows = 0;
    Integer row;
    if (serviceQueryResult != null) {
      // Generate virtual field if is null (value)
      if (fieldList == null) {
        fieldList = new ArrayList<>();
        Field field = new Field();
        field.setId("value");
        fieldList.add(field);
      }

      // Calculate total records
      if (this.records < 0) {
        setRecords((long) (serviceQueryResult.size() / fieldList.size()));
      }

      // Calculate total rows
      totalRows = (int) (records < 0 ? records : serviceQueryResult.size() / fieldList.size());
    }

    // Fill partial list
    for (row = 1; row <= totalRows; row++) {
      // Add row
      dataList.addRow(generateFieldValues(row));
    }
    dataList.setRecords(getRecords());
  }

  /**
   * Generates the field values
   *
   * @param rowIndex Row number
   * @return Row data
   */
  private HashMap<String, CellData> generateFieldValues(Integer rowIndex) {
    // Variable definition */
    HashMap<String, CellData> row = new HashMap<>();
    Integer columnIndex = 0;
    Integer totalColumns = fieldList.size();

    // Generate row ID
    row.put(DATALIST_IDENTIFIER, new CellData(rowIndex));

    // Generate field values
    for (columnIndex = 0; columnIndex < totalColumns; columnIndex++) {

      // Store field value
      SqlField field = fieldList.get(columnIndex);
      String nom = field.getIdentifier();
      String value = serviceQueryResult.get(((rowIndex - 1) * totalColumns) + columnIndex);

      // Format output data
      CellData cell = new CellData(value);

      // Add cell
      row.put(nom, cell);
    }

    return row;
  }

  /**
   * Merge datalists
   */
  private void doMerge() {
    for (DataList list : dataListList) {
      // Get all rows from all lists
      dataList.getRows().addAll(list.getRows());
    }
  }

  /**
   * Sort datalist
   */
  private void doSort() {
    // Set rows and records
    dataList.setRows(new SortRowProcessor().setSortList(sortList).process(dataList.getRows()));
  }

  /**
   * Remove the rows whose column value is distinct to the value
   */
  private void doFilter() {
    dataList.setRows(new FilterRowProcessor().setFilterList(filterList).process(dataList.getRows()));
  }

  /**
   * Keeps only distinct values of given fields
   */
  private void doDistinct() {
    // Set rows and records
    dataList.setRows(new DistinctRowProcessor().setDistinctList(distinctList).process(dataList.getRows()));
  }

  /**
   * Keeps only distinct values of given fields
   */
  private void doTotalize() throws AWException {
    // Set rows and records
    dataList.setRows(new TotalizeRowProcessor().setTotalizeList(totalizeList).process(dataList.getRows()));
  }

  /**
   * Paginate results
   */
  private void doPaginate() {
    List<Map<String, CellData>> rows = dataList.getRows();
    if (max > 0 && rows.size() > max) {
      // Avoid off bounds
      page = Math.min(page, getTotalPages());
      page = page < 1 ? 1 : page;

      // Calculate start and end rows
      int startRow = (int) ((page - 1) * max);
      int endRow = (int) Math.min(page * max, rows.size());

      // Retrieve sublist
      dataList.setRows(rows.subList(startRow, endRow));
    }
  }

  /**
   * Generate computes, data, compounds and identifiers
   */
  private void doEvaluate() throws AWException {
    int rowIndex = 1;
    for (Map<String, CellData> row : dataList.getRows()) {
      // Transform data for this row
      if (translate) {
        doTranslates(row);
      }

      // Transform data for this row
      if (transform) {
        doTransforms(row);
      }

      // Calculate computes for this row
      if (compute) {
        doComputes(row);
      }

      // Calculate compounds for this row
      if (compound) {
        doCompounds(row);
      }

      // Remove noprint fields
      if (noPrint) {
        doNoPrint(row);
      }

      // Generate identifier only if not generated previously
      if (identifier && !row.containsKey(DATALIST_IDENTIFIER)) {
        row.put(DATALIST_IDENTIFIER, new CellData(rowIndex++));
      }
    }
  }

  /**
   * Generate compute columns
   *
   * @param row Row to process
   */
  private void doComputes(Map<String, CellData> row) throws AWException {
    for (ComputedColumnProcessor computed : computedList) {
      // Process computed
      CellData cell = computed.process(row);
      row.put(computed.getColumnIdentifier(), cell);
    }
  }

  /**
   * Generate transformations on columns
   *
   * @param row Row to process
   */
  private void doTransforms(Map<String, CellData> row) throws AWException {
    for (TransformCellProcessor processor : transformList) {
      // Process transform
      row.put(processor.getColumnIdentifier(), processor.process(row.get(processor.getColumnIdentifier())));
    }
  }

  /**
   * Generate translations on columns
   *
   * @param row Row to process
   */
  private void doTranslates(Map<String, CellData> row) throws AWException {
    for (TranslateCellProcessor processor : translateList) {
      // Process translate
      row.put(processor.getColumnIdentifier(), processor.process(row.get(processor.getColumnIdentifier())));
    }
  }

  /**
   * Generate compound columns
   *
   * @param row Row to process
   */
  private void doCompounds(Map<String, CellData> row) throws AWException {
    for (CompoundColumnProcessor processor : compoundList) {
      // Process compound
      CellData cell = processor.process(row);
      row.put(processor.getColumnIdentifier(), cell);
    }
  }

  /**
   * Remove noPrint fields
   *
   * @param row Row to process
   */
  private void doNoPrint(Map<String, CellData> row) {
    for (String columnId : noPrintList) {
      // Process compound
      row.remove(columnId);
    }
  }
}