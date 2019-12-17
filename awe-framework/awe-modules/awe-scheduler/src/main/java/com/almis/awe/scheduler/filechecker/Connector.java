package com.almis.awe.scheduler.filechecker;

import com.almis.awe.exception.AWException;
import com.almis.awe.scheduler.bean.file.File;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.dao.FileDAO;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.List;

/**
 * @author dfuentes
 */
@Log4j2
public abstract class Connector {

  // Autowired services
  private FileDAO fileDAO;

  public Connector(FileDAO fileDAO) {
    this.fileDAO = fileDAO;
  }

  /**
   * Abstract class for connecting to some server and getting an array with all
   * files back
   *
   * @param task
   * @return
   * @throws AWException
   */
  protected abstract <T> List<T> connectAndGetFiles(Task task) throws AWException;

  /**
   * Abstract class for checking on all saved files if there was any change
   *
   * @param task
   * @return Changed file name
   * @throws AWException
   */
  public abstract String checkForChanges(Task task) throws AWException;

  /**
   * Check for changes on a single file, (usually called from the function
   * checkForChanges to check the files individually)
   *
   * @param task
   * @param fileName
   * @param path
   * @param lastModification
   * @return
   */
  protected boolean checkFileModifications(Task task, String fileName, String path, Date lastModification) {
    boolean hasChanged = false;
    File file = task.getFile();

    // Check if the file matches the given pattern
    if (fileName.matches(file.getFilePattern())) {

      // Check if the file is in the correct path on the server
      if (file.getFileModifications().containsKey(path)) {

        // If the file exists check for changes
        if (file.hasChanged(path, lastModification)) {

          // If the file has changed, update the last modification date on the
          // database
          hasChanged = true;
          fileDAO.addModification(task, path, lastModification, true);

          // Notify when detecting some modification
          log.debug("[File check] File modified: {}", path);
        }
      } else {

        // If the file is new, add it to the database with the modification date
        hasChanged = true;
        fileDAO.addModification(task, path, lastModification, false);

        // Notify when detecting new file
        log.debug("[File check] New file detected: {}", path);
      }
    }

    // returns if the file is new or it has changed
    return hasChanged;
  }
}
