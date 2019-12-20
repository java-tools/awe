package com.almis.awe.scheduler.service;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.dao.TaskDAO;
import com.almis.awe.service.QueryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.almis.awe.scheduler.constant.QueryConstants.SCHEDULER_LOAD_TASK_DETAILS_QUERY;

@Log4j2
public class TaskService {

  // Autowired services
  private QueryService queryService;
  private TaskDAO taskDAO;

  /**
   * Autowired constructor
   *
   * @param queryService
   */
  @Autowired
  public TaskService(QueryService queryService, TaskDAO taskDAO) {
    this.queryService = queryService;
    this.taskDAO = taskDAO;
  }

  /**
   * Load tasks from the database to the scheduler
   *
   * @throws AWException
   */
  public void loadSchedulerTasks() throws AWException {
    // Load the list of task from database
    List<Future<Task>> taskList = new ArrayList<>();
    try {
      DataList dataList = queryService.launchPrivateQuery(SCHEDULER_LOAD_TASK_DETAILS_QUERY).getDataList();
      log.debug("[SCHEDULER][TASKS] Starting tasks load from current database");

      for (Map<String, CellData> row : dataList.getRows()) {
        taskList.add(taskDAO.getTask(row.get("taskId").getIntegerValue()));
      }

      Task lastLoadedTask = null;
      for (Future<Task> futureTask : taskList) {
        lastLoadedTask = loadTask(lastLoadedTask, futureTask);
      }
    } catch (Exception exc) {
      log.warn("No scheduler tables on the current database");
    }
  }

  /**
   * Load tasks from the database to the scheduler
   *
   * @throws AWException
   */
  public ServiceData updateInterruptedTasks() throws AWException {
    return taskDAO.updateInterruptedTasks();
  }

  /**
   * Load a single task
   *
   * @param lastLoadedTask
   * @param taskFuture
   * @return
   * @throws AWException
   */
  private Task loadTask(Task lastLoadedTask, Future<Task> taskFuture) throws AWException {
    try {
      lastLoadedTask = taskFuture.get();
      if (lastLoadedTask.getLaunchType() != 0) {
        taskDAO.addTaskToScheduler(lastLoadedTask);
      }
      return lastLoadedTask;
    } catch (InterruptedException | ExecutionException exc) {
      log.error("Error trying to load task. Last loaded task: #{}", lastLoadedTask == null ? null : lastLoadedTask.getTaskId(), exc);
      Thread.currentThread().interrupt();
    }
    return null;
  }
}
