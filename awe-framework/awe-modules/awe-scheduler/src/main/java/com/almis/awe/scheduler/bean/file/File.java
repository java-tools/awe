package com.almis.awe.scheduler.bean.file;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dfuentes
 */
@Data
@Accessors(chain = true)
@Log4j2
public class File implements Serializable {

  // File modifications array, composed by String filepath+filename and the date
  // of the modification
  private transient Map<String, Date> fileModifications = null;
  // Server to look in
  private Integer fileServerId = null;
  // Path of the files
  private String filePath = null;
  // Pattern that the files have to match with
  private String filePattern = null;
  // User for the server connection
  private String fileServerUser = null;
  // Password for the server connection
  private String fileServerPassword = null;


  // Other attributes
  private Server server;

  /**
   * Constructor
   */
  public File() {
    fileModifications = new HashMap<>();
  }

  /**
   * check if the file has changed
   *
   * @param fileName
   * @param newFileModification
   * @return boolean
   */
  public boolean hasChanged(String fileName, Date newFileModification) {
    return fileModifications.get(fileName).compareTo(newFileModification) != 0;
  }
}
