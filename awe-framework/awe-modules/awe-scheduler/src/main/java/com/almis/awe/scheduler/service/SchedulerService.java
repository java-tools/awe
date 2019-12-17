package com.almis.awe.scheduler.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.dao.CalendarDAO;
import com.almis.awe.scheduler.dao.SchedulerDAO;
import com.almis.awe.scheduler.dao.TaskDAO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author pgarcia
 */
@Service
@Log4j2
public class SchedulerService extends ServiceConfig {

  // Autowired services
  private TaskDAO taskDAO;
  private SchedulerDAO schedulerDAO;
  private CalendarDAO calendarDAO;

  @Value("${scheduler.max.stored.executions:5}")
  private Integer maxStoredExecutions;

  // Locales
  private static final String ERROR_MESSAGE_SCHEDULER_PAUSE_TASK = "ERROR_MESSAGE_SCHEDULER_PAUSE_TASK";
  private static final String ERROR_TITLE_SCHEDULER_PAUSE_TASK = "ERROR_TITLE_SCHEDULER_PAUSE_TASK";
  private static final String ERROR_MESSAGE_SCHEDULER_RESUME_TASK = "ERROR_MESSAGE_SCHEDULER_RESUME_TASK";
  private static final String ERROR_TITLE_SCHEDULER_RESUME_TASK = "ERROR_TITLE_SCHEDULER_RESUME_TASK";

  /**
   * Constructor
   */
  @Autowired
  public SchedulerService(TaskDAO taskDAO, SchedulerDAO schedulerDAO, CalendarDAO calendarDAO) {
    this.taskDAO = taskDAO;
    this.schedulerDAO = schedulerDAO;
    this.calendarDAO = calendarDAO;
  }

  /**
   * Start the scheduler service
   *
   * @throws AWException
   */
  public ServiceData start() throws AWException {
    return schedulerDAO.start();
  }

  /**
   * Start the scheduler service
   *
   * @throws AWException
   */
  public ServiceData startNoQuartz() throws AWException {
    return schedulerDAO.startNoQuartz();
  }

  /**
   * Stop the scheduler service
   *
   * @throws AWException
   */
  public ServiceData stop() throws AWException {
    return schedulerDAO.stop();
  }

  /**
   * Scheduler's emergency reboot method
   *
   * @throws AWException
   */
  public ServiceData restart() throws AWException {
    return schedulerDAO.restart();
  }

  /**
   * Clear all scheduled tasks and stop the scheduler.
   *
   * @return
   */
  public ServiceData clearAndStop() throws AWException {
    return schedulerDAO.clearAndStop();
  }

  /**
   * Get currently executing jobs from the scheduler instance
   *
   * @return
   * @throws AWException
   */
  public ServiceData currentlyExecutingJobs() throws AWException {
    return schedulerDAO.getCurrentlyExecutingJobs();
  }


  /**
   * Get configured
   *
   * @return
   * @throws AWException
   */
  public ServiceData getConfiguredJobs() throws AWException {
    return schedulerDAO.getConfiguredJobs();
  }

  /**
   * Returns information about the configured scheduler
   *
   * @return
   * @throws AWException
   */
  public ServiceData getSchedulerMetadata() throws AWException {
    return schedulerDAO.getSchedulerMetadata();
  }

  /**
   * Retrieve the task list
   *
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData getTaskList() throws AWException {
    return taskDAO.getTaskList();
  }

  /**
   * Retrieve the task progress status
   *
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData getTaskExecutionList(Integer taskId) throws AWException {
    return taskDAO.getTaskExecutionList(taskId);
  }

  /**
   * Execute the selected task now
   *
   * @param taskId
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData executeTaskNow(Integer taskId) throws AWException {
    return taskDAO.executeTaskNow(taskId);
  }

  /**
   * Pause the selected task
   *
   * @param taskId
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData pauseTask(Integer taskId) throws AWException {
    try {
      Task task = taskDAO.getTask(taskId).get();
      return taskDAO.pauseTask(task);
    } catch (Exception exc) {
      throw new AWException(getLocale(ERROR_TITLE_SCHEDULER_PAUSE_TASK), getLocale(ERROR_MESSAGE_SCHEDULER_PAUSE_TASK), exc);
    }
  }

  /**
   * Resume the selected task
   *
   * @param taskId
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData resumeTask(Integer taskId) throws AWException {
    try {
      Task task = taskDAO.getTask(taskId).get();
      return taskDAO.resumeTask(task);
    } catch (Exception exc) {
      throw new AWException(getLocale(ERROR_TITLE_SCHEDULER_RESUME_TASK), getLocale(ERROR_MESSAGE_SCHEDULER_RESUME_TASK), exc);
    }
  }

  /**
   * OBJECT
   * Insert and schedule a new task
   *
   * @param taskId          Task identifier
   * @param sendStatus      Status to send list
   * @param sendDestination Destination target list
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData insertSchedulerTask(Integer taskId, List<Integer> sendStatus, List<Integer> sendDestination) throws AWException {
    return taskDAO.insertTask(taskId);
  }

  /**
   * Update and reschedule a task
   *
   * @param taskId          Task identifier
   * @param sendStatus      Status to send list
   * @param sendDestination Destination target list
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData updateSchedulerTask(Integer taskId, List<Integer> sendStatus, List<Integer> sendDestination) throws AWException {
    return taskDAO.updateTask(taskId);
  }


  /**
   * Update and reschedule a task
   *
   * @param taskId Task identifier list
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData updateSchedulerTask(Integer taskId) throws AWException {
    return taskDAO.updateTask(taskId);
  }

  /**
   * Delete a task from scheduler
   *
   * @param ideTsk Task identifier list
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData deleteSchedulerTask(List<Integer> ideTsk) throws AWException {
    return taskDAO.deleteTask(ideTsk);
  }

  /**
   * Delete a single task from scheduler
   *
   * @param ideTsk Task identifier list
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData deleteSchedulerTask(Integer ideTsk) throws AWException {
    return taskDAO.deleteTask(ideTsk);
  }

  /**
   * Update execution time
   *
   * @param taskId Task identifier list
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData updateExecutionTime(Integer taskId, Integer taskExecution) throws AWException {
    return taskDAO.updateExecutionTime(taskId, taskExecution);
  }

  /**
   * Load needed variables from the selected maintain
   *
   * @param maintainStr
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData loadMaintainVariables(String maintainStr) throws AWException {
    return taskDAO.loadMaintainVariables(maintainStr);
  }

  /**
   * Load execution screen
   *
   * @param path
   * @return
   * @throws AWException
   */
  public ServiceData loadExecutionScreen(String path, JsonNode address) throws AWException {
    return taskDAO.loadExecutionScreen(path, (ObjectNode) address);
  }

  /**
   * Reload execution screen
   *
   * @param taskId
   * @param executionId
   * @return
   * @throws AWException
   */
  public ServiceData reloadExecutionScreen(Integer taskId, Integer executionId) throws AWException {
    return taskDAO.reloadExecutionScreen(taskId, executionId);
  }

  /**
   * Purge execution logs
   *
   * @param taskId
   * @param executions
   * @return
   * @throws AWException
   */
  public ServiceData purgeExecutionLogs(Integer taskId, Integer executions) throws AWException {
    return taskDAO.purgeExecutionLogFiles(taskId, executions);
  }

  /**
   * Purge execution logs on application start
   *
   * @return
   * @throws AWException
   */
  public ServiceData purgeExecutionsAtStart() throws AWException {
    return taskDAO.purgeExecutionsAtStart();
  }

  /**
   * Pause the selected calendar
   *
   * @param calendarIdList
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData deactivateCalendar(List<Integer> calendarIdList) throws AWException {
    return calendarDAO.deactivateCalendars(calendarIdList);
  }

  /**
   * Resume the selected calendar
   *
   * @param calendarIdList
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData activateCalendar(List<Integer> calendarIdList) throws AWException {
    return calendarDAO.activateCalendars(calendarIdList);
  }

  /**
   * Check if the scheduler contains the selected calendar
   *
   * @param calendarIdList
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData checkCalendarExist(List<Integer> calendarIdList) throws AWException {
    return calendarDAO.checkTriggersContainsCalendar(calendarIdList.toArray(new Integer[0]));
  }

  /**
   * Insert and schedule a new calendar
   *
   * @param calendarIde
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData insertSchedulerCalendar(Integer calendarIde) throws AWException {
    return calendarDAO.insertSchedulerCalendar(calendarIde, false, false);
  }

  /**
   * Insert and schedule a new calendar
   *
   * @param calendarId
   * @param replace
   * @param updateTriggers
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData insertSchedulerCalendar(Integer calendarId, boolean replace, boolean updateTriggers) throws AWException {
    return calendarDAO.insertSchedulerCalendar(null, calendarId, replace, updateTriggers);
  }

  /**
   * Insert and schedule a new calendar
   *
   * @param calendarId
   * @param replace
   * @param updateTriggers
   * @param alias
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData insertSchedulerCalendar(String alias, Integer calendarId, boolean replace, boolean updateTriggers) throws AWException {
    return calendarDAO.insertSchedulerCalendar(alias, calendarId, replace, updateTriggers);
  }

  /**
   * Update and schedule a new calendar
   *
   * @param calendarId
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData updateSchedulerCalendar(Integer calendarId) throws AWException {
    return calendarDAO.updateSchedulerCalendar(calendarId);
  }

  /**
   * Delete selected calendars
   *
   * @param calendarIdList
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData deleteSchedulerCalendar(List<Integer> calendarIdList) throws AWException {
    return calendarDAO.deleteSchedulerCalendar(calendarIdList);
  }

  /**
   * Delete a calendar from scheduler
   *
   * @param calendarId
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData deleteSchedulerCalendar(Integer calendarId) throws AWException {
    return calendarDAO.deleteSchedulerCalendar(Arrays.asList(calendarId));
  }

  /**
   * Retrieves next 100 years
   *
   * @return ServiceData
   */
  public ServiceData yearSelectService() {
    return calendarDAO.yearSelectService();
  }

  /**
   * Compute next fire times
   *
   * @param times
   * @return
   * @throws AWException
   */
  public ServiceData computeNextFiretimes(Integer times) throws AWException {
    return calendarDAO.computeNextFiretimes(times);
  }
}
