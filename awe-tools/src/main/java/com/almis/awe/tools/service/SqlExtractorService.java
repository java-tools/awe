package com.almis.awe.tools.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.screen.component.grid.Column;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.almis.awe.tools.type.SqlExtractorQueriesType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sql Extractor Engine service
 */
@Service
public class SqlExtractorService extends ServiceConfig {

  // Autowired services
  private DataListBuilder builder;
  private DataSource dataSource;

  private static final String[] WRITE_STATEMENTS = {"insert", "delete", "update", "drop", "create"};
  private static final String SELECTED_GRID = "selectGrid";
  private static final Integer MIN_COLUMN_SIZE = 10;
  private static final Integer MAX_COLUMN_SIZE = 30;
  private static final String USER_HOME = "user.home";
  private static final String SELECTS_PATH = AweConstants.FILE_SEPARATOR + "aweFiles" + AweConstants.FILE_SEPARATOR;
  private static final String DATABASE_MESSAGE_TITLE = "DATABASE_MESSAGE_TITLE";
  private static final String STRING = "STRING";

  /**
   * Autowired constructor
   * @param builder
   * @param dataSource
   */
  @Autowired
  public SqlExtractorService(DataListBuilder builder, DataSource dataSource) {
    this.builder = builder;
    this.dataSource = dataSource;
  }

  /**
   * Extract data
   * @param select Select
   * @param type Data type
   * @return Extracted data
   * @throws AWException Error in extraction
   */
  public ServiceData extractData(String select, String type) throws AWException {

    ServiceData serviceData = new ServiceData();
    DataList dataList = new DataList();
    boolean isWriteQuery = checkWriteQuery(select);
    boolean isWriteQueryPermission = type.equalsIgnoreCase(SqlExtractorQueriesType.W.name());

    if (!isWriteQuery || isWriteQueryPermission) {

      // Try with resources
      try (Connection conn = dataSource.getConnection();
           Statement stmt =  conn.createStatement();
           ResultSet resultSet = stmt.executeQuery(select)) {

        if (!isWriteQuery) {
          // Extract metadata of the query
          ResultSetMetaData metaData = resultSet.getMetaData();

          // Get number of columns
          int columnTotal = metaData.getColumnCount();

          List<Map<String, CellData>> rows = new ArrayList<>();
          Integer rowIndex = 0;

          while (resultSet.next()) {
            rows.add(getDataRow(rowIndex, columnTotal, metaData, resultSet));
            rowIndex++;
          }
          // Add generated rows to datalist
          dataList.setRows(rows);
          dataList.setRecords(rows.size());

          serviceData = fillGrid(metaData, dataList);
        } else {
          // Create message action
          ClientAction messageAction = new ClientAction("message");
          messageAction.addParameter("type", new CellData(AnswerType.OK.toString()));
          messageAction.addParameter("title", new CellData(getLocale(DATABASE_MESSAGE_TITLE)));
          messageAction.addParameter("message", new CellData(getLocale("DATABASE_MESSAGE_RESPONSE_OK")));

          serviceData.addClientAction(messageAction);
        }
      } catch (AWException exc) {
        throw new AWException(getLocale(DATABASE_MESSAGE_TITLE), getLocale("ERROR_MESSAGE_SELECT_RUN") + ": " + exc.getMessage(), exc);
      } catch (SQLException exc) {
        throw new AWException(getLocale(DATABASE_MESSAGE_TITLE), getLocale("ERROR_MESSAGE_SQL") + ": " + exc.getMessage(), exc);
      }
    } else {
      throw new AWException(getLocale(DATABASE_MESSAGE_TITLE), getLocale("ERROR_MESSAGE_PERMISION_DENIED"));
    }

    return serviceData;
  }

  /**
   * Check query statement
   * @param query Query
   * @return Is a write statement
   */
  private boolean checkWriteQuery(String query) {
    for (String statement : WRITE_STATEMENTS) {
      if (StringUtil.containsIgnoreCase(query, statement)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Retrieve data row from resultset
   * @param rowIndex Row index
   * @param columnTotal Column total
   * @param metaData Metadata
   * @param resultSet Resultset
   * @return Data row
   * @throws SQLException Error retrieving data row
   */
  private Map<String, CellData> getDataRow(Integer rowIndex, Integer columnTotal, ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
    Map<String, CellData> row = new HashMap<>();
    row.put("id", new CellData(rowIndex));
    for (int i = 1; i <= columnTotal; i++) {
      String cell = resultSet.getString(metaData.getColumnName(i));
      row.put(metaData.getColumnName(i), new CellData(cell));
    }
    return row;
  }

  /**
   * Get existing select files
   *
   * @return Selected file
   * @throws AWException Error retrieving file
   */
  public ServiceData getSelectFile() throws AWException {

    ServiceData serviceData = new ServiceData();

    String userHome = System.getProperty(USER_HOME);
    String selectsPath = userHome + SELECTS_PATH;
    List<String> keys = new ArrayList<>();

    File folder = new File(selectsPath);

    if (!folder.exists()) {
      folder.mkdir();
    }

    File[] listOfFiles = folder.listFiles();

    for (File listOfFile : listOfFiles) {
      if (listOfFile.isFile()) {
        keys.add(listOfFile.getName());
      }
    }

    builder.addColumn("label", keys, STRING);
    builder.addColumn("value", keys, STRING);

    serviceData.setDataList(builder.build());

    return serviceData;

  }

  /**
   * Save select file
   *
   * @param fileName File name
   * @return Selected sentence
   * @throws AWException Error loading sentence
   * @throws IOException Error retrieving file
   */
  public ServiceData loadSelectSentence(String fileName) throws AWException, IOException {

    ServiceData serviceData = new ServiceData();
    List<String> keys = new ArrayList<>();

    String userHome = System.getProperty(USER_HOME);
    String selectsPath = userHome + SELECTS_PATH;
    String filePath = selectsPath + AweConstants.FILE_SEPARATOR + fileName;

    try (FileInputStream file = new FileInputStream(filePath);
         BufferedReader reader = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8))) {

      StringBuilder stringBuilder = new StringBuilder();
      String aux = "";

      while ((aux = reader.readLine()) != null) {
        stringBuilder.append(aux).append('\n');
      }

      keys.add(stringBuilder.toString());
    } catch (IOException exc) {
      throw new AWException(getLocale("ERROR_TITLE_WRITING_SELECTS"), getLocale("ERROR_MESSAGE_WRITING_SELECTS"), exc);
    }

    builder.addColumn("label", keys, STRING);
    builder.addColumn("value", keys, STRING);

    serviceData.setDataList(builder.build());

    return serviceData;
  }

  /**
   * Save select file
   *
   * @param fileName File name
   * @param selects Select
   * @return Selected file
   * @throws AWException Error storing file
   */
  public ServiceData saveSelectFile(String fileName, String selects) throws AWException {

    ServiceData serviceData = new ServiceData();

    String userHome = System.getProperty(USER_HOME);
    String selectsPath = userHome + SELECTS_PATH;
    String filePath = selectsPath + AweConstants.FILE_SEPARATOR + fileName + ".txt";

    // Instantiate selects files path
    File folder = new File(selectsPath);
    // Check if path exists and create it
    if (!folder.exists()) {
      folder.mkdir();
    }

    // Whatever the file path is.
    File statText = new File(filePath);

    try (FileOutputStream is = new FileOutputStream(statText);
         OutputStreamWriter osw = new OutputStreamWriter(is)) {

      Writer w = new BufferedWriter(osw);
      w.write(selects);
    } catch (IOException exc) {
      throw new AWException(getLocale("ERROR_TITLE_WRITING_SELECTS"), getLocale("ERROR_MESSAGE_WRITING_SELECTS"), exc);
    }

    return serviceData;
  }

  /**
   * Check file exists
   *
   * @param fileName File name
   * @return Check
   */
  public ServiceData checkFile(String fileName) {

    ServiceData serviceData = new ServiceData();

    String userHome = System.getProperty(USER_HOME);
    String selectsPath = userHome + SELECTS_PATH;
    String filePath = selectsPath + AweConstants.FILE_SEPARATOR + fileName + ".txt";

    // Instantiate selects files path
    File folder = new File(selectsPath);
    // Check if path exists and create it
    if (!folder.exists()) {
      folder.mkdir();
    }

    File file = new File(filePath);
    // Check if file already exists
    if (file.exists()) {
      serviceData.addVariable("MESSAGE_TYPE", AnswerType.ERROR.toString());
      serviceData.addVariable("MESSAGE_TITLE", getLocale("WARNING_TITLE_DUPLICATED_FILE"));
      serviceData.addVariable("MESSAGE_DESCRIPTION", getLocale("WARNING_MESSAGE_DUPLICATED_FILE"));
      return serviceData;
    }

    serviceData.setType(AnswerType.OK);
    return serviceData;
  }

  /**
   * Delete columns from grid
   *
   * @return clientActionList
   */
  public ServiceData deleteColumns() {

    // Variable initialization
    ServiceData serviceData = new ServiceData();
    // Columns
    List<Column> columns = new ArrayList<>();
    Column column = new Column();
    column.setId("NONE");
    columns.add(column);

    // Add columns to the multiselect grid
    ClientAction addColumnsGrid = new ClientAction("replace-columns");
    addColumnsGrid.setTarget(SELECTED_GRID);
    addColumnsGrid.addParameter("columns", new CellData(columns));
    serviceData.addClientAction(addColumnsGrid);
    return serviceData;

  }

  /**
   * Fill grid with data
   *
   * @param rsMetaData Metadata
   * @param datalist Datalist
   * @return Client actions
   * @throws AWException Error filling grid
   */
  private ServiceData fillGrid(ResultSetMetaData rsMetaData, DataList datalist) throws AWException {
    // Variable initialization
    ServiceData serviceData = new ServiceData();

    try {

      // Build column structure
      List<Column> columns = getColumnList(rsMetaData);
      // Add columns to the multiselect grid
      ClientAction addColumnsGrid = new ClientAction("replace-columns");
      addColumnsGrid.setTarget(SELECTED_GRID);
      addColumnsGrid.addParameter("columns", new CellData(columns));

      // Fill Grid
      ClientAction fillGrid = new ClientAction("fill");
      fillGrid.setTarget(SELECTED_GRID);
      fillGrid.addParameter("datalist", datalist);

      // Set variables
      serviceData.addClientAction(addColumnsGrid);
      serviceData.addClientAction(fillGrid);
    } catch (AWException exc) {
      throw new AWException(getLocale("ERROR_TITLE_SESSION_EXPIRED"), getLocale("ERROR_MESSAGE_SESSION_EXPIRED"), exc);
    }
    return serviceData;
  }

  /**
   * Generate columns structure
   *
   * @param rsMetaData Metadata
   * @param index Index
   * @return Column
   * @throws SQLException Error retrieving column
   */
  private Column getColumn(ResultSetMetaData rsMetaData, Integer index) throws SQLException {

    // New column
    Column column = new Column();
    Integer size;
    String columnName = rsMetaData.getColumnName(index);

    if (rsMetaData.getPrecision(index) < MIN_COLUMN_SIZE) {
      size = MIN_COLUMN_SIZE;
    } else if (rsMetaData.getPrecision(index) > MAX_COLUMN_SIZE) {
      size = MAX_COLUMN_SIZE;
    } else {
      size = rsMetaData.getPrecision(index);
    }

    column.setCharLength(size.toString());
    column.setField(columnName);
    column.setName(columnName);
    column.setLabel(columnName);

    switch (rsMetaData.getColumnType(index)) {
    case java.sql.Types.FLOAT:
    case java.sql.Types.DECIMAL:
    case java.sql.Types.NUMERIC:
      column.setAlign("right");
      break;
    case java.sql.Types.CHAR:
    case java.sql.Types.VARCHAR:
      column.setAlign("left");
      break;
    default:
      column.setAlign("center");
    }

    return column;
  }

  /**
   * Build columns structure list
   *
   * @param rsMetaData Metadata
   * @return Column list
   * @throws AWException Error retrieving column list
   */
  private List<Column> getColumnList(ResultSetMetaData rsMetaData) throws AWException {

    List<Column> columns = new ArrayList<>();

    try {
      for (int i = 1, t = rsMetaData.getColumnCount(); i <= t; i++) {
        columns.add(getColumn(rsMetaData, i));
      }

    } catch (SQLException exc) {
      throw new AWException(getLocale("ERROR_TITLE_BUILD_COLUMNS"), getLocale("ERROR_MESSAGE_BUILD_COLUMNS"), exc);
    }
    return columns;
  }
}
