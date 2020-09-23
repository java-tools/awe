package com.almis.awe.scheduler.filechecker;

import com.almis.awe.scheduler.bean.file.File;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.dao.FileDAO;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dfuentes
 */
@Log4j2
public class FTPFileChecker extends Connector {

  // Autowired services
  private final FTPClient ftpClient;

  /**
   * Autowired constructor
   *
   * @param fileDAO   File DAO
   * @param ftpClient FTP client
   */
  public FTPFileChecker(FileDAO fileDAO, FTPClient ftpClient) {
    super(fileDAO);
    this.ftpClient = ftpClient;
  }

  @Override
  protected List<FTPFile> connectAndGetFiles(Task task) {
    File file = task.getFile();
    try {
      // Connect to ftp server
      ftpClient.connect(file.getServer().getHost(), file.getServer().getPort());
      if (file.getFileServerUser() != null && !file.getFileServerUser().equals("")) {
        ftpClient.login(file.getFileServerUser(), file.getFileServerPassword());
      }

      // Notify correctly connected
      log.debug("[FTP Connection] Connected to the server by FTP: {}", file.getServer().getName() + "(" + file.getServer().getServerId() + ")");

      // lists files and directories in the current working directory
      return new ArrayList<>(Arrays.asList(ftpClient.listFiles(file.getFilePath())));
    } catch (IOException exc) {
      log.error("[FTP Connection] Error connecting to the server by FTP: {}", file.getServer().getName() + "(" + file.getServer().getServerId() + ")", exc);
    } finally {
      try {
        // Always logout and disconnect from the FTP server
        ftpClient.logout();
        ftpClient.disconnect();

        // Notify correctly logged out
        log.debug("[FTP Connection] Logged out from FTP server: {}", file.getServer().getName() + "(" + file.getServer().getServerId()
          + ")");
      } catch (IOException exc) {
        log.error("[FTP Connection] FTP Connection error: {}", file.getServer().getName() + "(" + file.getServer().getServerId() + ")", exc);
      }
    }
    return new ArrayList<>();
  }

  @Override
  public String checkForChanges(Task task) {
    String changedFile = null;
    File file = task.getFile();

    // Iterate on every file and check for changes
    List<FTPFile> serverFiles = connectAndGetFiles(task);
    for (FTPFile ftpFile : serverFiles) {
      if (checkFileModifications(task, ftpFile.getName(), file.getFilePath() + ftpFile.getName(), ftpFile.getTimestamp().getTime())) {
        changedFile = ftpFile.getName();
      }
    }

    // return if there are changes
    return changedFile;
  }
}
