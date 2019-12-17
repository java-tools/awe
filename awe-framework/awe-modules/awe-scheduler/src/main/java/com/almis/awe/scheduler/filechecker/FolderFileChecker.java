package com.almis.awe.scheduler.filechecker;

import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.scheduler.bean.file.File;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.dao.FileDAO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author dfuentes
 */
@Component
@Log4j2
public class FolderFileChecker extends Connector {

  // Autowired services
  private FileClient fileClient;

  /**
   * Autowired constructor
   *
   * @param fileDAO
   */
  @Autowired
  public FolderFileChecker(FileDAO fileDAO, FileClient fileClient) {
    super(fileDAO);
    this.fileClient = fileClient;
  }

  @Override
  protected List<java.io.File> connectAndGetFiles(Task task) {
    File file = task.getFile();
    return fileClient.listFiles(file.getServer().getHost(), file.getFilePath());
  }

  @Override
  public String checkForChanges(Task task) {
    String changedFile = null;

    List<java.io.File> files = connectAndGetFiles(task);
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    for (java.io.File currentFile : files) {
      try {
        if (checkFileModifications(task, currentFile.getName(), StringUtil.fixPath(currentFile.getAbsolutePath()), formatter.parse(formatter.format(new Date(currentFile.lastModified())))))
          changedFile = currentFile.getName();
      } catch (ParseException exc) {
        log.error("[Folder checker] Error parsing last modification date for file {}", currentFile.getAbsolutePath(), exc);
      }
    }


    return changedFile;
  }
}
