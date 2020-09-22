package com.almis.awe.scheduler.dao;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.service.MaintainService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.quartz.TriggerKey;

import java.util.Date;

import static com.almis.awe.scheduler.constant.MaintainConstants.*;
import static com.almis.awe.scheduler.constant.TaskConstants.*;

@Log4j2
public class FileDAO extends ServiceConfig {

  // Autowired services
  private final MaintainService maintainService;
  private final QueryUtil queryUtil;

  /**
   * Autowired constructor
   *
   * @param maintainService Maintain service
   */
  public FileDAO(MaintainService maintainService, QueryUtil queryUtil) {
    this.maintainService = maintainService;
    this.queryUtil = queryUtil;
  }

  /**
   * Add or replace a modification to the modifications hashmap
   *
   * @param filepath
   * @param lastModification
   * @param isUpdate
   */
  public void addModification(Task task, String filepath, Date lastModification, boolean isUpdate) {
    if (isUpdate) {
      updateModification(task, filepath, lastModification);
    } else {
      insertModification(task, filepath, lastModification);
    }
    task.getFile().getFileModifications().put(filepath, lastModification);
  }

  /**
   * remove a modification to the modifications hashmap
   *
   * @param filePath
   */
  public void removeModification(Task task, String filePath) {
    deleteModification(task, filePath);
    task.getFile().getFileModifications().remove(filePath);
  }

  /**
   * insert a modification to the database
   *
   * @param filepath
   * @param lastModification
   */
  public void insertModification(Task task, String filepath, Date lastModification) {
    try {
      ObjectNode parameters = queryUtil.getParameters(task.getDatabase());
      parameters.put(TASK_IDE, task.getTaskId());
      parameters.put(FILE_PATH, filepath);
      parameters.put(UPDATE_DATE, DateUtil.dat2WebTimestamp(lastModification));
      maintainService.launchPrivateMaintain(FILE_INSERT_MODIFICATION_QUERY, parameters);

    } catch (AWException exc) {
      log.error("[File] Error inserting new file {} to database: {}", filepath,
        new TriggerKey(String.valueOf(task.getTaskId()), task.getGroup()).toString(), exc);
    }
  }

  /**
   * update a modification on the database
   *
   * @param filepath
   * @param lastModification
   */
  public void updateModification(Task task, String filepath, Date lastModification) {
    try {
      ObjectNode parameters = queryUtil.getParameters(task.getDatabase());
      parameters.put(TASK_IDE, task.getTaskId());
      parameters.put(FILE_PATH, filepath);
      parameters.put(UPDATE_DATE, DateUtil.dat2WebTimestamp(lastModification));
      maintainService.launchPrivateMaintain(FILE_UPDATE_MODIFICATION_QUERY, parameters);

      log.debug("[File] The file {} has been updated: {}, trigger: {}", filepath, DateUtil.dat2WebTimestamp(lastModification),
        new TriggerKey(String.valueOf(task.getTaskId()), task.getGroup()).toString());

    } catch (AWException exc) {
      log.error("[File] Error on file {} update: {}", filepath, new TriggerKey(String.valueOf(task.getTaskId()), task.getGroup()).toString(), exc);
    }
  }

  /**
   * delete a modification from the database
   *
   * @param filepath
   */
  public void deleteModification(Task task, String filepath) {
    try {
      ObjectNode parameters = queryUtil.getParameters(task.getDatabase());
      parameters.put(TASK_IDE, task.getTaskId());
      parameters.put(FILE_PATH, filepath);
      maintainService.launchPrivateMaintain(FILE_DELETE_MODIFICATION_QUERY, parameters);

      log.debug("[File] File modification deleted for file {} - trigger: {}", filepath,
        new TriggerKey(String.valueOf(task.getTaskId()), task.getGroup()).toString());

    } catch (AWException exc) {
      log.error("[File] Error deleting file modification for file {} - trigger: {}", filepath,
        new TriggerKey(String.valueOf(task.getTaskId()), task.getGroup()).toString(), exc);
    }
  }
}
