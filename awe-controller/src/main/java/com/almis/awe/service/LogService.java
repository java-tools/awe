package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.*;
import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

/**
 * Manage logs
 *
 * @author pgarcia
 */
public class LogService extends ServiceConfig {

  // Autowired services
  private QueryUtil queryUtil;

  @Value("${application.log.base.path}")
  private String logBasePath;

  @Value("${application.log.user.home:false}")
  private String logUserHome;

  // System user home
  private static final String USER_HOME = System.getProperty("user.home");

  /**
   * Autowired constructor
   *
   * @param queryUtil Query utilities
   */
  @Autowired
  public LogService(QueryUtil queryUtil) {
    this.queryUtil = queryUtil;
  }

  /**
   * Get log files sorted
   *
   * @param fileName  Log file
   * @param date      Log date
   * @param startTime Start time
   * @param endTime   End time
   * @return Log list
   * @throws AWException Error retrieving log list
   */
  public ServiceData getLogList(String fileName, String date, String startTime, String endTime) throws AWException {
    ServiceData serviceData = new ServiceData();

    // Generate dates
    Date startDate = null;
    Date endDate = null;
    if (date != null) {
      // Start time
      if (startTime != null) {
        startDate = DateUtil.getDateWithTimeFromCriteria(date, startTime);
      } else {
        startDate = DateUtil.getDateWithTimeFromCriteria(date, "00:00:00");
      }

      // End time
      if (endTime != null) {
        endDate = DateUtil.getDateWithTimeFromCriteria(date, endTime);
      } else {
        endDate = DateUtil.getDateWithTimeFromCriteria(date, "23:59:59");
      }
    }

    // Generate a dataList with fields
    DataList logFiles = getFiles(fileName, startDate, endDate);

    // Generate and sort data list
    List<SortColumn> sortList = null;
    Map<String, QueryParameter> variableMap = queryUtil.getDefaultVariableMap(null);
    if (variableMap != null && variableMap.containsKey(AweConstants.QUERY_SORT)) {
      QueryParameter sortParameter = variableMap.get(AweConstants.QUERY_SORT);
      ArrayNode sortNode = (ArrayNode) sortParameter.getValue();
      sortList = queryUtil.getSortList(sortNode);
    }

    // Generate dataList builder
    DataListBuilder logListBuilder = getBean(DataListBuilder.class).addDataList(logFiles);

    // Sort
    if (sortList != null) {
      logListBuilder.sort(sortList);
    }

    // Generate datalist
    serviceData.setDataList(logListBuilder.build());
    return serviceData;
  }

  /**
   * Get files
   *
   * @param fileName  File name
   * @param startDate Start time
   * @param endDate   End time
   * @return Files
   */
  private DataList getFiles(String fileName, Date startDate, Date endDate) throws AWException {
    DataList fileList = new DataList();
    String logPath = "true".equalsIgnoreCase(logUserHome) ? USER_HOME + logBasePath : logBasePath;

    // Check flag user home path
    File baseLogDirectory = Paths.get(logPath).normalize().toFile();

    // Get base log directory
    if (baseLogDirectory.isDirectory()) {
      getFilesFromFolder(baseLogDirectory, fileName, startDate, endDate, fileList);
    }
    return fileList;
  }

  /**
   * Get files from a folder
   *
   * @param folder    Folder
   * @param fileName  File name
   * @param startDate Start date
   * @param endDate   End date
   * @param fileList  File list
   */
  private void getFilesFromFolder(File folder, String fileName, Date startDate, Date endDate, DataList fileList) throws AWException {
    if (folder.isDirectory() && folder.listFiles() != null) {
      for (File file : folder.listFiles()) {
        checkFolderFile(file, fileName, startDate, endDate, fileList);
      }
    }
  }

  /**
   * Check folder file
   *
   * @param file      File to be checked
   * @param fileName  File name
   * @param startDate Start date
   * @param endDate   End date
   * @param fileList  File list
   */
  private void checkFolderFile(File file, String fileName, Date startDate, Date endDate, DataList fileList) throws AWException {
    if (!file.isDirectory()) {
      if (checkFile(file, fileName, startDate, endDate)) {
        fileList.addRow(getFileRow(file));
      }
    } else {
      getFilesFromFolder(file, fileName, startDate, endDate, fileList);
    }
  }

  /**
   * Check if file is within range
   *
   * @param fileName  File name
   * @param startDate Start date
   * @param endDate   End date
   */
  private boolean checkFile(File file, String fileName, Date startDate, Date endDate) {
    // Get file date
    boolean check = true;
    Date fileDate = new Date(file.lastModified());

    // Check filename
    if (fileName != null && !file.getName().toLowerCase().contains(fileName.toLowerCase())) {
      check = false;
    }

    // Check start time
    if (startDate != null && startDate.after(fileDate)) {
      check = false;
    }

    // Check end time
    if (endDate != null && endDate.before(fileDate)) {
      check = false;
    }

    return check;
  }

  /**
   * Generate a new file row
   *
   * @param file File
   * @return File row
   */
  private Map<String, CellData> getFileRow(File file) throws AWException {
    Map<String, CellData> fileRow = new HashMap<>();
    fileRow.put("action", new CellData("getLogText"));
    String path = EncodeUtil.encodeSymmetric(file.getAbsolutePath());
    fileRow.put("path", new CellData(path));
    fileRow.put("name", new CellData(file.getName()));
    FastDateFormat formatter = FastDateFormat.getInstance("dd/MM/yyyy HH:mm:ss");
    fileRow.put("date", new CellData(formatter.format(file.lastModified())));
    return fileRow;
  }

  /**
   * Log a database change
   *
   * @param database New database
   */
  public void logChangeDatabase(String database) {
    getLogger().log(LogService.class, Level.INFO, "Database changed to ''{0}''", database);
  }
}