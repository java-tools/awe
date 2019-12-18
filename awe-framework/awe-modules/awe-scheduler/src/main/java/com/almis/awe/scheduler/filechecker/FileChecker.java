package com.almis.awe.scheduler.filechecker;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.scheduler.bean.file.File;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.enums.ServerConnectionType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author dfuentes
 */
@Log4j2
public class FileChecker extends ServiceConfig {

  // Autowired services
  private FTPFileChecker ftpFileChecker;
  private FolderFileChecker folderFileChecker;

  /**
   * Autowired constructor
   *
   * @param ftpFileChecker
   * @param folderFileChecker
   */
  @Autowired
  public FileChecker(FTPFileChecker ftpFileChecker, FolderFileChecker folderFileChecker) {
    this.ftpFileChecker = ftpFileChecker;
    this.folderFileChecker = folderFileChecker;
  }

  /**
   * Checks a file if needed and returns if it has changed or not
   *
   * @param task
   * @return boolean
   * @throws AWException
   */
  public String checkFile(Task task) {
    // true -> modifications | false -> no modifications
    File file = task.getFile();
    if (file == null || file.getFileServerId() == null) {
      return null;
    } else {
      String protocol = file.getServer().getTypeOfConnection();
      switch (ServerConnectionType.valueOf(protocol.toUpperCase())) {
        case FTP:
          return ftpFileChecker.checkForChanges(task);
        case FOLDER:
          return folderFileChecker.checkForChanges(task);
        case HTTP:
        case HTTPS:
        case SSH:
        default:
          return null;
      }
    }
  }
}