package com.almis.awe.developer.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.data.DataListUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Manage the development paths
 *
 * @author agomez
 */
public class PathService extends ServiceConfig {

  @Value("${developer.path}")
  private String developerPath;

  @Value("${developer.path.file:path.properties}")
  private String developerPathFile;

  @Value("${developer.path.property:path.project}")
  private String developerPathProperty;

  private static final String ERROR_TITLE_UPDATE_WRK_DIR = "ERROR_TITLE_UPDATE_WRK_DIR";
  private static final String ERROR_MESSAGE_UPDATE_WRK_DIR = "ERROR_MESSAGE_UPDATE_WRK_DIR";

  /**
   * Retrieves property's value for final path
   *
   * @return Path
   */
  private String getFinalPath() {
    return developerPath + developerPathFile;
  }

  /**
   * Check if path file properties exists
   *
   * @return Service data
   * @throws AWException Error checking path
   */
  public ServiceData checkPath() throws AWException {
    ServiceData serviceData = new ServiceData();
    Properties properties = getPropertiesFile();
    String path = properties.getProperty(developerPathProperty);

    // If path is not valid, set it to blank
    if (path == null) {
      updatePath("");
    }

    List<String> paths = Arrays.asList(path);
    DataList dataList = new DataList();
    DataListUtil.addColumn(dataList, "paths", paths);
    dataList.setRecords(dataList.getRows().size());

    serviceData.setDataList(dataList);
    return serviceData;
  }

  /**
   * Check if path file properties exists
   *
   * @return Path
   * @throws AWException Error retrieving path
   */
  public Properties getPropertiesFile() throws AWException {
    Properties properties = null;

    // Check if properties file exists
    checkIfFileExists();

    // Retrieve properties file
    try (FileInputStream in = new FileInputStream(getFinalPath())) {
      properties = new Properties();
      properties.load(in);
    } catch (IOException exc) {
      throw new AWException(getElements().getLocale(ERROR_TITLE_UPDATE_WRK_DIR),
              getElements().getLocale(ERROR_MESSAGE_UPDATE_WRK_DIR), exc);
    }
    return properties;
  }

  /**
   * Check if properties file exists, if not, create it
   */
  private void checkIfFileExists() throws AWException {
    File propertiesFile = new File(getFinalPath());
    if (!propertiesFile.exists()) {
      try {
        new File(developerPath).mkdirs();
        if (!propertiesFile.createNewFile()) {
          throw new AWException(getElements().getLocale(ERROR_TITLE_UPDATE_WRK_DIR),
                  getElements().getLocale(ERROR_MESSAGE_UPDATE_WRK_DIR));
        }
      } catch (IOException exc) {
        throw new AWException(getElements().getLocale(ERROR_TITLE_UPDATE_WRK_DIR),
                getElements().getLocale(ERROR_MESSAGE_UPDATE_WRK_DIR));
      }
    }
  }

  /**
   * Update path in properties file
   * @param path Path to update
   * @throws AWException Error updating path
   */
  private void updatePath(String path) throws AWException {
    Properties properties = getPropertiesFile();
    properties.setProperty(developerPathProperty, path);

    try (FileOutputStream out = new FileOutputStream(getFinalPath())) {
      properties.store(out, null);
    } catch (IOException exc) {
      throw new AWException(getElements().getLocale(ERROR_TITLE_UPDATE_WRK_DIR),
              getElements().getLocale("ERROR_MESSAGE_UPDATE_WRK_DIR2"), exc);
    }
  }

  /**
   * Check if path file properties exists
   *
   * @return Path
   * @throws AWException Error retrieving path
   */
  public String getPath() throws AWException {
    Properties props = getPropertiesFile();
    return props.getProperty(developerPathProperty);
  }

  /**
   * Updates user working directory
   *
   * @param path Path
   * @return Service data
   * @throws AWException Error setting path
   */
  public ServiceData setPath(String path) throws AWException {
    ServiceData serviceData = new ServiceData();
    // Update path
    updatePath(path);
    serviceData.setTitle(getElements().getLocale("CONFIRM_TITLE_UPDATE_WRK_DIR"))
            .setMessage(getElements().getLocale("CONFIRM_MESSAGE_UPDATE_WRK_DIR"));
    return serviceData;
  }
}
