package com.almis.awe.scheduler.dao;

import com.almis.awe.builder.client.FilterActionBuilder;
import com.almis.awe.builder.client.SelectActionBuilder;
import com.almis.awe.builder.client.UpdateControllerActionBuilder;
import com.almis.awe.builder.client.css.AddCssClassActionBuilder;
import com.almis.awe.builder.client.css.RemoveCssClassActionBuilder;
import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.maintain.MaintainQuery;
import com.almis.awe.model.entities.maintain.Target;
import com.almis.awe.model.entities.queries.Variable;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.model.util.data.TimeUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskDependency;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import com.almis.awe.scheduler.builder.task.TaskBuilder;
import com.almis.awe.scheduler.enums.TaskLaunchType;
import com.almis.awe.scheduler.enums.TaskStatus;
import com.almis.awe.scheduler.enums.TriggerType;
import com.almis.awe.scheduler.factory.TaskFactory;
import com.almis.awe.scheduler.factory.TriggerFactory;
import com.almis.awe.scheduler.filechecker.FileChecker;
import com.almis.awe.scheduler.util.TaskUtil;
import com.almis.awe.service.MaintainService;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.almis.awe.model.constant.AweConstants.*;
import static com.almis.awe.scheduler.constant.JobConstants.*;
import static com.almis.awe.scheduler.constant.MaintainConstants.*;
import static com.almis.awe.scheduler.constant.QueryConstants.*;
import static com.almis.awe.scheduler.constant.TaskConstants.*;

@Log4j2
public class TaskDAO extends ServiceConfig {

  @Value("${scheduler.execution.log.path}")
  private String executionLogPath;

  @Value("${scheduler.stored.executions:5}")
  private Integer defaultStoredExecutions;

  // locales
  private static final String TITLE_SCHEDULER_NEW_TASK = "TITLE_SCHEDULER_NEW_TASK";
  private static final String MESSAGE_SCHEDULER_NEW_TASK = "MESSAGE_SCHEDULER_NEW_TASK";
  private static final String WARNING_MESSAGE_TASK_DEPENDENCY_ERROR = "WARNING_MESSAGE_TASK_DEPENDENCY_ERROR";

  // Autowired services
  private final QueryService queryService;
  private final MaintainService maintainService;
  private final Scheduler scheduler;
  private final QueryUtil queryUtil;
  private final CalendarDAO calendarDAO;
  private final ServerDAO serverDAO;
  private final FileChecker fileChecker;

  /**
   * Autowired constructor
   *
   * @param queryService
   */
  public TaskDAO(Scheduler scheduler, QueryService queryService, MaintainService maintainService, QueryUtil queryUtil,
                 CalendarDAO calendarDAO, ServerDAO serverDAO, FileChecker fileChecker) {
    this.scheduler = scheduler;
    this.queryService = queryService;
    this.maintainService = maintainService;
    this.queryUtil = queryUtil;
    this.calendarDAO = calendarDAO;
    this.serverDAO = serverDAO;
    this.fileChecker = fileChecker;
  }

  /**
   * Load task from trigger
   *
   * @param trigger
   * @return Generated task
   */
  public Task getTask(Trigger trigger) {
    return (Task) trigger.getJobDataMap().get(TASK);
  }

  /**
   * Load task from job detail
   *
   * @param jobDetail
   * @return Generated task
   */
  public Task getTask(JobDetail jobDetail) {
    return (Task) jobDetail.getJobDataMap().get(TASK);
  }

  /**
   * Load task from task id
   *
   * @param taskId Task id
   * @return Generated task
   * @throws AWException
   */
  @Async("schedulerTaskPool")
  public Future<Task> getTask(Integer taskId) throws AWException {
    return getTask(taskId, null, null);
  }

  /**
   * Load task from task id
   *
   * @param taskId     Task id
   * @param launchType forced launch type
   * @return Generated task
   * @throws AWException
   */
  @Async("schedulerTaskPool")
  public Future<Task> getTask(Integer taskId, TaskLaunchType launchType) throws AWException {
    return getTask(taskId, null, launchType);
  }

  /**
   * Load task from task id
   *
   * @param taskId     Task id
   * @param database   Database
   * @param launchType forced launch type
   * @return Generated task
   * @throws AWException
   */
  @Async("schedulerTaskPool")
  public Future<Task> getTask(Integer taskId, String database, TaskLaunchType launchType) throws AWException {

    // Retrieve task data
    ObjectNode parameters = queryUtil.getParameters(database, "1", "0");
    parameters.put(TASK_ID, taskId);
    ServiceData taskParameters = queryService.launchPrivateQuery(SCHEDULER_TASK_QUERY, parameters);
    TaskBuilder taskBuilder = TaskFactory.getInstance(taskParameters.getDataList(), launchType, scheduler);

    // Set task parameters
    taskBuilder.setParameters(queryService.launchPrivateQuery(SCHEDULER_TASK_PARAMETERS_QUERY, parameters).getDataList());

    // Retrieve task dependencies
    taskBuilder.setDependencies(queryService.launchPrivateQuery(SCHEDULER_TASK_DEPENDENCIES_QUERY, parameters).getDataList());

    // Retrieve task calendar
    if (taskBuilder.getCalendarId() != null) {
      taskBuilder.setCalendar(calendarDAO.getCalendar(database, taskBuilder.getCalendarId()));
    }

    // Fill file if defined
    if (taskBuilder.getFile() != null) {
      // Generate server if file is defined
      taskBuilder.setFileServer(serverDAO.findServer(taskBuilder.getFile().getFileServerId(), taskBuilder.getTask().getDatabase()));

      // Generate file modifications if defined
      setFileModificationsFromDb(taskBuilder.getTask());
    }

    // Retrieve task
    return new AsyncResult<>(taskBuilder.build());
  }


  /**
   * load file modification hashmap from database
   *
   * @param task
   * @throws AWException
   */
  private void setFileModificationsFromDb(Task task) throws AWException {
    // Set server ip to the context
    ObjectNode parameters = queryUtil.getParameters(task.getDatabase(), "1", "0");
    parameters.put(TASK_IDE, task.getTaskId());
    DataList modificationsDataList = queryService.launchPrivateQuery(SCHEDULER_FILE_MODIFICATIONS_QUERY, parameters).getDataList();
    log.debug("[File] Last modification files loaded from database on task #{}", task.getTaskId());

    Map<String, Date> modifications = new HashMap<>();

    // Fill modifications hashmap
    for (Map<String, CellData> row : modificationsDataList.getRows()) {
      modifications.put(row.get(FILE_PATH).getStringValue(), row.get(UPDATE_DATE).getDateValue());
    }

    task.getFile().setFileModifications(modifications);
  }

  /**
   * Check if task is allowed to be launched
   *
   * @param task Task
   * @return Task is allowed
   */
  public boolean isTaskExecutionAllowed(Task task) {
    if (task != null && TaskLaunchType.FILE_TRACKING.getValue().equals(task.getLaunchType())) {
      // Check if file has been updated
      String file = fileChecker.checkFile(task);

      // Put launcher in job detail
      task.setLauncher(file);

      // Veto if file has not been updated
      return file != null;
    }
    return true;
  }

  /**
   * Start a task
   *
   * @param task Task
   * @return
   */
  public TaskExecution startTask(Task task) throws AWException {
    ObjectNode parameters = queryUtil.getParameters();
    parameters.put(TASK_ID, task.getTaskId());
    parameters.put(TASK_GROUP, task.getGroup());
    parameters.put(TASK_LAUNCHER, task.getLauncher());
    parameters.put("executions", task.getStoredExecutions() != null ? task.getStoredExecutions() : defaultStoredExecutions);
    maintainService.launchPrivateMaintain(TASK_START, parameters);

    // Retrieve task execution
    TaskExecution execution = getLastExecutionFromDB(task.getTaskId(), task.getTrigger().getKey().getGroup());

    // Set parent execution if defined
    if (execution != null) {
      execution.setParentExecution(task.getParentExecution());
    }

    return execution;
  }

  /**
   * End a task
   *
   * @param task      Task
   * @param execution Task execution
   * @return
   */
  public TaskExecution endTask(Task task, TaskExecution execution) throws AWException {
    ObjectNode parameters = queryUtil.getParameters();
    parameters.put(TASK_ID, task.getTaskId());
    parameters.put(TASK_JOB_EXECUTION, execution.getExecutionId());
    parameters.put("status", task.getStatus().getValue());
    parameters.put("taskDescription", execution.getDescription());
    maintainService.launchPrivateMaintain(TASK_END, parameters);

    // Retrieve updated task execution from database
    execution = getTaskExecution(task.getTaskId(), execution.getExecutionId());
    execution.setParentExecution(task.getParentExecution());

    // Retrieve task execution
    return execution;
  }

  /**
   * Retrieve executions to purge (sorted)
   *
   * @param taskId
   * @param executions
   * @return
   * @throws AWException
   */
  public ServiceData getExecutionsToPurge(Integer taskId, Integer executions) throws AWException {
    ObjectNode parameters = queryUtil.getParameters(null, "1", "0");
    parameters.put(TASK_ID, taskId);
    ServiceData serviceData = queryService.launchPrivateQuery(GET_ALL_EXECUTIONS_WITH_DATES, parameters);
    DataList dataList = serviceData.getDataList();

    // Filter rows
    dataList.setRows(serviceData.getDataList().getRows().stream().skip(executions).collect(Collectors.toList()));
    dataList.getRows().forEach(row -> row.remove("id"));

    // Set records
    dataList.setRecords(serviceData.getDataList().getRows().size());

    return serviceData;
  }

  /**
   * Purge execution log files
   *
   * @param taskId
   * @throws AWException
   */
  public ServiceData purgeExecutionLogFiles(Integer taskId, Integer executions) throws AWException {
    ArrayNode executionsToPurge = JsonNodeFactory.instance.arrayNode();
    String logPath = executionLogPath;
    DataList dataList = getExecutionsToPurge(taskId, executions).getDataList();
    List<TaskExecution> taskExecutionList = DataListUtil.asBeanList(dataList, TaskExecution.class);

    for (TaskExecution taskExecution : taskExecutionList) {
      // Delete each task execution file
      try {
        executionsToPurge.add(taskExecution.getExecutionId());
        Path logFilePath = getExecutionLogFilePath(logPath, taskId, taskExecution.getExecutionId());
        if (logFilePath.toFile().exists()) {
          Files.delete(logFilePath);
        }
      } catch (Exception exc) {
        log.warn("Could not delete log file for task {} and execution {}", taskId, taskExecution.getExecutionId(), exc);
      }
    }

    // Add executions id to parameters
    ObjectNode parameters = queryUtil.getParameters();
    parameters.set("executionId", executionsToPurge);
    parameters.put(TASK_ID, taskId);
    if (executionsToPurge.size() > 0) {
      return maintainService.launchPrivateMaintain(PURGE_EXECUTION_LOGS, parameters);
    } else {
      return new ServiceData();
    }
  }

  /**
   * Purge executions at start
   *
   * @throws AWException
   */
  public ServiceData purgeExecutionsAtStart() throws AWException {
    log.info("===== Deleting old execution logs =====");

    DataList dataList = queryService.launchPrivateQuery(GET_ALL_EXECUTIONS).getDataList();
    List<TaskExecution> taskExecutionList = DataListUtil.asBeanList(dataList, TaskExecution.class);
    Set<String> validLogFiles = taskExecutionList
      .stream()
      .map(e -> "execution_" + e.getTaskId() + TASK_SEPARATOR + e.getExecutionId() + ".log")
      .collect(Collectors.toSet());

    // For each file in log path, if it's not a valid file, delete it
    Path logPath = Paths.get(executionLogPath);
    if (logPath.toFile().exists()) {
      try (Stream<Path> pathStream = Files.list(logPath)) {
        pathStream.forEach(path -> {
          String name = path.toFile().getName();
          if (!validLogFiles.contains(name)) {
            try {
              Files.delete(path);
            } catch (Exception exc) {
              log.error("Error deleting log file: {}", name, exc);
            }
          }
        });
      } catch (IOException exc) {
        log.warn("Error reading execution log path: {}", executionLogPath, exc);
      }
    }

    return new ServiceData();
  }

  /**
   * Update running task status [4] to interrupted [6]
   *
   * @return
   */
  public ServiceData updateInterruptedTasks() throws AWException {
    return maintainService.launchPrivateMaintain(UPDATE_INTERRUPTED_TASKS);
  }

  /**
   * Change a task status
   *
   * @param execution Task execution
   * @param status    New status
   * @return
   */
  public ServiceData changeStatus(TaskExecution execution, TaskStatus status, String reason) throws AWException {
    // Set execution status
    execution.setStatus(status.getValue());
    execution.setDescription(reason);

    // Update execution status
    ObjectNode parameters = queryUtil.getParameters();
    parameters.put(TASK_ID, execution.getTaskId());
    parameters.put(TASK_JOB_EXECUTION, execution.getExecutionId());
    parameters.put("status", status.getValue());
    parameters.put("description", reason);
    return maintainService.launchPrivateMaintain(TASK_UPDATE_STATUS, parameters);
  }

  /**
   * Add task to the scheduler
   *
   * @param task Task to add
   * @throws AWException
   */
  public void addTaskToScheduler(Task task) throws AWException {
    // Add new scheduled job to the scheduler
    try {
      scheduleTask(task);
      log.debug("New task added to the scheduler: #{}", task.getTaskId());

      if (!task.isActive()) {
        scheduler.pauseTrigger(task.getTrigger().getKey());
      }
    } catch (Exception exc) {
      throw new AWException("Error adding scheduler task: #" + task.getTaskId(), exc);
    }
  }

  /**
   * Insert and schedule a new task
   *
   * @param taskId Task identifier
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData insertTask(Integer taskId) throws AWException {
    ServiceData serviceData = new ServiceData();
    Task task = null;
    try {
      task = getTask(taskId).get();

      // Clear file modification table from database
      if (TaskLaunchType.FILE_TRACKING.getValue().equals(task.getLaunchType())) {
        clearFileModificationTable(taskId);
        log.debug("[SCHEDULERS][TASK {}] The modification table has been cleared", taskId);
      }

      task.setGroup(TaskUtil.getGroupForLaunchType(task.getLaunchType()));

      // Add task to scheduler if not manual
      if (task.getLaunchType() != 0) {
        addTaskToScheduler(task);
      }

      log.info("[SCHEDULER][TASK {}] The task has been added", taskId);
      serviceData
        .setTitle(getLocale(TITLE_SCHEDULER_NEW_TASK))
        .setMessage(getLocale(MESSAGE_SCHEDULER_NEW_TASK));

    } catch (Exception exc) {
      throw new AWException("Error inserting new task: " + taskId, exc);
    }

    return serviceData;
  }

  /**
   * Update and reschedule a task
   *
   * @param taskId Task identifier list
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData updateTask(Integer taskId) throws AWException {
    // delete the task to update from the scheduler instance
    deleteTask(taskId);

    // insert the updated task to the scheduler
    ServiceData serviceData = insertTask(taskId);
    log.info("[SCHEDULER][TASK_QUERY {}] The task has been updated", taskId);

    return serviceData;
  }

  /**
   * Delete a task from scheduler
   *
   * @param ideTsk Task identifier list
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData deleteTask(List<Integer> ideTsk) throws AWException {
    ServiceData serviceData = new ServiceData();
    for (Integer id : ideTsk) {
      serviceData = deleteTask(id);
    }
    return serviceData;
  }

  /**
   * Delete a single task from scheduler
   *
   * @param ideTsk Task i dentifier list
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData deleteTask(Integer ideTsk) throws AWException {
    ServiceData serviceData = new ServiceData();
    String taskId = ideTsk.toString();
    try {
      // Purge log files
      purgeExecutionLogFiles(ideTsk, 0);

      // Check if the job exists
      for (String group : scheduler.getTriggerGroupNames()) {
        TriggerKey triggerKey = new TriggerKey(taskId, group);
        if (checkTrigger(triggerKey)) {

          // Delete job
          scheduler.deleteJob(scheduler.getTrigger(triggerKey).getJobKey());
          log.info("[SCHEDULER][TASK {}] The task has been deleted", triggerKey);

          // Clear file modification table from database
          clearFileModificationTable(ideTsk);
        }
      }

    } catch (Exception exc) {
      throw new AWException("[SCHEDULER] Error deleting task: " + taskId, exc);
    }
    return serviceData;
  }

  /**
   * Update execution time
   *
   * @param taskId Task id
   * @return
   * @throws AWException
   */
  public ServiceData updateExecutionTime(Integer taskId, Integer executionId) throws AWException {
    // Get task execution
    ObjectNode parameters = queryUtil.getParameters();
    parameters.put(TASK_ID, taskId);
    parameters.put(TASK_JOB_EXECUTION, executionId);
    ServiceData serviceData = queryService.launchPrivateQuery(SCHEDULER_TASK_EXECUTION, parameters);

    // Get dates
    DataList dataList = serviceData.getDataList();
    Date initialDate = DataListUtil.getCellData(dataList, 0, "initialDate").getDateValue();
    Date endDate = DataListUtil.getCellData(dataList, 0, "endDate").getDateValue();

    // Update time in execution
    parameters.put("executionTime", endDate.getTime() - initialDate.getTime());
    return maintainService.launchPrivateMaintain("updateExecutionTime", parameters);
  }

  /**
   * Retrieve task execution list fixed
   *
   * @param taskId Task id
   * @return Task execution list fixed
   * @throws AWException
   */
  public ServiceData getTaskExecutionList(Integer taskId) throws AWException {
    ObjectNode parameters = queryUtil.getParameters(null, "1", "0");
    ServiceData serviceData = queryService.launchQuery("getTaskExecutionList", parameters);
    long averageExecution = getAverageTime(taskId);
    String logPath = executionLogPath;

    DataList dataList = serviceData.getDataList();
    for (Map<String, CellData> row : dataList.getRows()) {
      CellData progress = new CellData();
      CellData averageTime = new CellData();
      Integer executionId = row.get(TASK_EXECUTION_ID).getIntegerValue();

      // Calculate progress
      CellData status = row.get("Sta");
      CellData executionTime = row.get(TASK_EXECUTION_TIME);
      CellData launchType = row.get(TASK_LAUNCH_TYPE_LIST);
      CellData launchedBy = new CellData(getLaunchedByText(launchType.getStringValue(), row.get(TASK_LAUNCHED_BY_LIST).getStringValue()));
      CellData executionLogPathCell = new CellData(getExecutionLogFileNode(logPath, taskId, executionId));
      Date initialDate = row.get("ExeLstTim").getDateValue();

      if (TaskStatus.JOB_RUNNING.equals(TaskStatus.valueOf(status.getIntegerValue()))) {
        progress = new CellData(getProgressNode(getProgress(initialDate, averageExecution)));
        averageTime = new CellData(TimeUtil.formatTime(getElapsedTime(initialDate), false));
      } else if (executionTime != null && executionTime.getIntegerValue() != null) {
        averageTime = new CellData(TimeUtil.formatTime(executionTime.getIntegerValue()));
      }

      row.put(TASK_EXECUTION_PROGRESS, progress);
      row.put(TASK_EXECUTION_TIME, averageTime);
      row.put(TASK_LAUNCHED_BY_LIST, launchedBy);
      row.put(TASK_EXECUTION_LOG_PATH, executionLogPathCell);
    }

    return serviceData;
  }

  /**
   * Get execution log file path
   *
   * @param logPath     Log path
   * @param taskId      Task id
   * @param executionId Execution id
   * @return Log file path
   */
  private Path getExecutionLogFilePath(String logPath, Integer taskId, Integer executionId) {
    return Paths.get(logPath, "execution_" + taskId + TASK_SEPARATOR + executionId + ".log");
  }

  /**
   * Retrieve execution log file node
   *
   * @param logPath
   * @param taskId
   * @param executionId
   * @return
   */
  private ObjectNode getExecutionLogFileNode(String logPath, Integer taskId, Integer executionId) throws AWException {
    Path executionLogFilePath = getExecutionLogFilePath(logPath, taskId, executionId);
    ObjectNode logFileNode = JsonNodeFactory.instance.objectNode();
    logFileNode.put(JSON_VALUE_PARAMETER, EncodeUtil.encodeSymmetric(executionLogFilePath.toString()));
    logFileNode.put(JSON_STYLE_PARAMETER, "no-btn");
    logFileNode.put(JSON_TITLE_PARAMETER, "SCHEDULER_SHOW_EXECUTION_LOG");
    logFileNode.put(JSON_ICON_PARAMETER, "fa-file-text-o text-info");
    logFileNode.put(JSON_LABEL_PARAMETER, "");
    return logFileNode;
  }

  /**
   * Retrieve progress node
   *
   * @param percentage Percentage
   * @return Progress node
   */
  public ObjectNode getProgressNode(long percentage) {
    ObjectNode progressBar = JsonNodeFactory.instance.objectNode();
    if (percentage > 100L) {
      progressBar.put(JSON_VALUE_PARAMETER, 100);
      progressBar.put(JSON_STYLE_PARAMETER, "progress-bar-danger");
      progressBar.put(JSON_LABEL_PARAMETER, "");
    } else {
      progressBar.put(JSON_VALUE_PARAMETER, percentage);
      progressBar.put(JSON_LABEL_PARAMETER, percentage + "%");
    }
    return progressBar;
  }

  /**
   * Generate status icon
   *
   * @param execution Execution
   * @return Status icon
   */
  public ObjectNode getStatusIcon(TaskExecution execution) {
    ObjectNode statusIcon = JsonNodeFactory.instance.objectNode();
    statusIcon.put(JSON_VALUE_PARAMETER, execution.getStatus());
    try {
      statusIcon.put(JSON_LABEL_PARAMETER, queryService.findLabel("StaTyp", execution.getStatus().toString()));
      statusIcon.put(JSON_ICON_PARAMETER, queryService.findLabel("StaIco", execution.getStatus().toString()));
      statusIcon.put(JSON_STYLE_PARAMETER, queryService.findLabel("StaSty", execution.getStatus().toString()));
    } catch (AWException exc) {
      log.error("Error finding icon labels", exc);
    }
    return statusIcon;
  }

  /**
   * Retrieve progress
   *
   * @param initialDate      Initial date
   * @param averageExecution Average execution
   * @return Progress
   */
  public long getProgress(Date initialDate, long averageExecution) {
    long currentExecution = new Date().getTime() - initialDate.getTime();
    return averageExecution == 0 ? 101L : (currentExecution * 100) / averageExecution;
  }

  /**
   * Retrieve elapsed time from a start date (without ms)
   *
   * @param startDate Start date
   * @return Elapsed time
   */
  public Integer getElapsedTime(Date startDate) {
    return (int) (new Date().getTime() - startDate.getTime());
  }

  /**
   * Execute the selected task now
   *
   * @param taskId
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData executeTaskNow(Integer taskId) throws AWException {
    // Creates a task that is executed at the moment it is added to the scheduler
    executeImmediateTask(taskId, TriggerType.MANUAL, getSession().getUser(), null);

    // Log launched task
    log.info("Task launched manually: {}", taskId);

    // Return ok service data
    return new ServiceData();
  }

  /**
   * Execute the task ask dependency
   *
   * @param taskId
   * @throws AWException
   */
  public void executeDependency(Integer taskId, TaskExecution parentExecution) throws AWException {
    // Creates a task that is executed at the moment it is added to the scheduler
    executeImmediateTask(taskId, TriggerType.DEPENDENCY, "#" + parentExecution.getTaskId(), parentExecution);

    // Log launched task
    log.info("Task #{} launched as dependency from task #{}", taskId, parentExecution.getTaskId());
  }

  /**
   * Execute immediately a task
   *
   * @param taskId      Task id
   * @param triggerType Trigger type
   * @param launcher    Task launcher
   * @throws AWException
   */
  private void executeImmediateTask(Integer taskId, TriggerType triggerType, String launcher, TaskExecution parent) throws AWException {
    try {
      Task task = getTask(taskId).get();
      task.setLauncher(launcher);
      task.setParentExecution(parent);

      JobDataMap dataMap = new JobDataMap();
      dataMap.put(TASK, task);

      // Add a new trigger to task
      task.setTrigger(TriggerFactory.getInstance(triggerType, dataMap));

      // Set launcher to job data
      task.setJob(task.getJob()
        .getJobBuilder()
        .withIdentity(task.getTrigger().getKey().getName(), task.getTrigger().getKey().getGroup())
        .build());

      // Schedule task
      addTaskToScheduler(task);
    } catch (Exception exc) {
      throw new AWException("Error launching immediate task: " + taskId, exc);
    }
  }

  /**
   * Pause the selected task
   *
   * @param task
   * @return ServiceData
   * @throws SchedulerException
   */
  public ServiceData pauseTask(Task task) throws SchedulerException {
    TriggerKey key = new TriggerKey(String.valueOf(task.getTaskId()), task.getGroup());
    // Pause trigger in scheduler
    if (checkTrigger(key)) {
      scheduler.pauseTrigger(key);
      log.info("[SCHEDULER][TASK_QUERY {}] Task paused", key);
    }
    return new ServiceData();
  }

  /**
   * Resume the selected task
   *
   * @param task
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData resumeTask(Task task) throws AWException {
    if (task.getLaunchType() != 0) {
      scheduleTask(task);
    }
    return new ServiceData();
  }

  /**
   * Schedule a task
   *
   * @param task
   * @throws AWException
   */
  private void scheduleTask(Task task) throws AWException {
    try {
      TriggerKey triggerKey = task.getTrigger().getKey();

      if (checkTrigger(triggerKey)) {
        Trigger oldTrigger = scheduler.getTrigger(triggerKey);
        TriggerBuilder triggerBuilder = oldTrigger.getTriggerBuilder();

        // Create new trigger from old trigger builder
        Trigger newTrigger = triggerBuilder.build();

        // Reschedule Job with the new trigger and the old trigger key
        scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);
        log.info("[SCHEDULER][TASK_QUERY {}] Task resumed", triggerKey);
      } else {
        scheduler.scheduleJob(task.getJob(), task.getTrigger());
      }
    } catch (Exception exc) {
      throw new AWException("Error scheduling task: " + task.getTaskId(), exc);
    }
  }

  /**
   * Retrieve the task list
   *
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData getTaskList() throws AWException {
    // Get task list details
    ObjectNode parameters = queryUtil.getParameters(null, "1", "0");
    ServiceData serviceData = queryService.launchQuery(SCHEDULER_LOAD_TASK_DETAILS_WITH_FILTER_QUERY, parameters);
    DataList filtered = new DataList();
    for (Map<String, CellData> row : serviceData.getDataList().getRows()) {
      // Add other parameters from the scheduler to the datalist
      filtered.addRow(addSchedulerParameters(row));
    }

    return serviceData.setDataList(filtered);
  }

  /**
   * Load needed variables from the selected maintain
   *
   * @param maintainStr
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData loadMaintainVariables(String maintainStr) throws AWException {
    ServiceData serviceData = new ServiceData();
    // Get the selected maintain
    Target maintain = getElements().getMaintain(maintainStr);
    // Get the maintain query list
    List<MaintainQuery> queryList = maintain.getQueryList();
    DataList dataList = new DataList();
    // Create a variables hashmap without duplicate variable names
    Map<String, Variable> variables = new HashMap<>();

    // Fill hashmap
    for (MaintainQuery query : queryList) {
      List<Variable> variableList = new ArrayList<>();
      List<Variable> tmpLst = query.getVariableDefinitionList();
      if (tmpLst != null) {
        variableList.addAll(tmpLst);
      }
      for (Variable variable : variableList) {
        // check if the variable already exist in the hasmap
        if (variable.getName() != null && !variables.containsKey(variable.getName())) {
          variables.put(variable.getName(), variable);
        }
      }
    }
    // Get hashmap keys
    Set<String> keys = variables.keySet();
    for (String key : keys) {
      // fill datalist rows with each variable data
      Map<String, CellData> row = new HashMap<>();
      Variable variable = variables.get(key);
      row.put(TASK_PARAMETER_NAME, new CellData(variable.getName()));
      row.put(TASK_PARAMETER_SOURCE, new CellData("1"));
      row.put(TASK_PARAMETER_TYPE, new CellData(variable.getType()));
      row.put(TASK_PARAMETER_VALUE, new CellData(""));
      dataList.getRows().add(row);
    }
    serviceData.setDataList(dataList);
    return serviceData;
  }

  /**
   * Load execution screen parameters
   *
   * @param path
   * @param address Button address
   * @return ServiceData
   */
  public ServiceData loadExecutionScreen(String path, ObjectNode address) throws AWException {
    ServiceData serviceData = new ServiceData();
    serviceData.addClientAction(new ClientAction("reset").setTarget("executionLogViewer").setSilent(true));
    serviceData.addClientAction(new SelectActionBuilder("path", path).setSilent(true).build());
    serviceData.addClientAction(new FilterActionBuilder("executionLogViewer").setSilent(true).build());

    // Refresh execution screen
    String[] executionAddress = address.get("row").asText().split(TASK_SEPARATOR);
    TaskExecution taskExecution = getTaskExecution(Integer.parseInt(executionAddress[0]), Integer.parseInt(executionAddress[1]));
    refreshExecutionScreen(taskExecution, serviceData);

    return serviceData;
  }

  /**
   * Reload execution task parameters
   *
   * @param taskId
   * @param executionId
   * @return
   * @throws AWException
   */
  public ServiceData reloadExecutionScreen(Integer taskId, Integer executionId) throws AWException {
    ServiceData serviceData = new ServiceData();

    // Refresh execution screen
    if (taskId != null && executionId != null) {
      TaskExecution taskExecution = getTaskExecution(taskId, executionId);
      if (taskExecution != null) {
        refreshExecutionScreen(taskExecution, serviceData);
      }
    }

    return serviceData;
  }

  /**
   * Refresh execution screen data
   *
   * @param taskExecution
   * @param serviceData
   */
  private void refreshExecutionScreen(TaskExecution taskExecution, ServiceData serviceData) throws AWException {
    serviceData.addClientAction(new SelectActionBuilder("task-id", taskExecution.getTaskId()).setAsync(true).setSilent(true).build());
    serviceData.addClientAction(new SelectActionBuilder("execution-id", taskExecution.getExecutionId()).setAsync(true).setSilent(true).build());
    serviceData.addClientAction(new SelectActionBuilder("execution-start-time", DateUtil.dat2WebTimestampMs(taskExecution.getInitialDate())).setAsync(true).setSilent(true).build());
    serviceData.addClientAction(new SelectActionBuilder("execution-end-time", taskExecution.getEndDate() == null ? "" : DateUtil.dat2WebTimestampMs(taskExecution.getEndDate())).setAsync(true).setSilent(true).build());
    serviceData.addClientAction(new SelectActionBuilder("execution-time", taskExecution.getExecutionTime() == null ? "" : TimeUtil.formatTime(taskExecution.getExecutionTime())).setAsync(true).setSilent(true).build());
    serviceData.addClientAction(new SelectActionBuilder("execution-information", taskExecution.getDescription()).setAsync(true).setSilent(true).build());
    serviceData.addClientAction(new SelectActionBuilder("execution-title", getLocale(queryService.findLabel("StaTit", taskExecution.getStatus().toString()))).setAsync(true).setSilent(true).build());
    serviceData.addClientAction(new SelectActionBuilder("execution-launched-by", getLaunchedByText(taskExecution.getGroupId(), taskExecution.getLaunchedBy())).setAsync(true).setSilent(true).build());
    serviceData.addClientAction(new UpdateControllerActionBuilder("execution-launched-by", "icon", queryService.findLabel("LchTxtIcoNam", taskExecution.getGroupId())).setAsync(true).setSilent(true).build());
    serviceData.addClientAction(new RemoveCssClassActionBuilder("#execution-header,#execution-icon,[criterion-id='execution-launched-by'] .text-icon,[criterion-id='execution-information'] .input",
      "bg-success", "bg-danger", "bg-warning", "bg-info",
      "fa-check", "fa-times", "fa-exclamation-triangle", "fa-chain-broken", "fa-refresh", "fa-spin",
      "text-danger", "text-info", "text-success", "text-light-gray", "input-sm").setAsync(true).setSilent(true).build());
    serviceData.addClientAction(new AddCssClassActionBuilder("#execution-header", queryService.findLabel("StaBg", taskExecution.getStatus().toString())).setAsync(true).setSilent(true).build());
    serviceData.addClientAction(new AddCssClassActionBuilder("#execution-icon", queryService.findLabel("StaIco", taskExecution.getStatus().toString())).setAsync(true).setSilent(true).build());
    serviceData.addClientAction(new AddCssClassActionBuilder("[criterion-id='execution-launched-by'] .text-icon", queryService.findLabel("LchTxtSty", taskExecution.getGroupId())).setAsync(true).setSilent(true).build());
  }

  /**
   * Retrieve launchedBy text
   *
   * @param launchType Launch type
   * @param launchedBy Launched by
   * @return
   */
  private String getLaunchedByText(String launchType, String launchedBy) {
    return DEPENDENCY_GROUP.equalsIgnoreCase(launchType) ? getLocale("PARAMETER_TASK") + " " + launchedBy : launchedBy;
  }

  /**
   * Add parameters from the scheduler to the task list
   *
   * @return Map<String, CellData>
   * @throws AWException
   * @parameter HashMap<String, CellData> row
   */
  private Map<String, CellData> addSchedulerParameters(Map<String, CellData> row) throws AWException {
    Integer ide;

    // Get trigger identifier
    ide = row.get(TASK_IDE_LIST).getIntegerValue();
    String group = TaskUtil.getGroupForLaunchType(Integer.valueOf(row.get(TASK_LAUNCH_TYPE_LIST).getStringValue()));

    try {
      // Check if the trigger exists
      TriggerKey triggerKey = new TriggerKey(ide.toString(), group);
      if (checkTrigger(triggerKey)) {

        Trigger trigger = scheduler.getTrigger(triggerKey);

        // get next execution time
        Date nextTime = null;

        // Get trigger current state from scheduler
        Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);

        // If the trigger state is normal the set previous and next execution
        // times.
        if (triggerState == Trigger.TriggerState.NORMAL) {
          nextTime = trigger.getNextFireTime();
        }
        // get the job associated to the trigger
        JobDetail job = scheduler.getJobDetail(trigger.getJobKey());
        row.put(TASK_STATUS, new CellData(isJobRunning(job) ? TaskStatus.JOB_RUNNING.getValue() : TaskStatus.JOB_OK.getValue()));

        // fill row data from scheduler parameters
        row.put(TASK_NEXT_EXECUTION, new CellData(nextTime != null ? DateUtil.dat2WebTimestamp(nextTime) : " - "));
      } else {
        // Initialize job status to OK if its not currently running on the
        // scheduler
        row.put(TASK_STATUS, new CellData(TaskStatus.JOB_OK.getValue()));

        // If the trigger does not exist, try to load last execution from the
        // database
        row.put(TASK_NEXT_EXECUTION, new CellData(" - "));
      }

      // Get the maximum number of stored executions
      row.put(TASK_AVERAGE_TIME, new CellData(TimeUtil.formatTime(row.get("averageTime").getIntegerValue())));
    } catch (Exception exc) {
      throw new AWException("Error adding scheduler parameters to the task datalist: {}", new TriggerKey(ide.toString(), group).toString(), exc);
    }

    return row;
  }

  /**
   * Get task execution from trigger
   *
   * @param trigger
   * @return String
   */
  public TaskExecution getTaskExecution(Trigger trigger) {
    String[] splittedTriggerId = trigger.getKey().getName().split(TASK_SEPARATOR);
    if (splittedTriggerId.length > 1) {
      return new TaskExecution()
        .setTaskId(Integer.valueOf(splittedTriggerId[0]))
        .setExecutionId(Integer.valueOf(splittedTriggerId[1]))
        .setGroupId(trigger.getKey().getGroup());
    } else {
      return null;
    }
  }

  /**
   * Get execution from Database
   *
   * @param taskId      Task id
   * @param executionId Execution id
   * @return
   * @throws AWException
   */
  public TaskExecution getTaskExecution(Integer taskId, Integer executionId) throws AWException {
    // Set context from the query
    ObjectNode parameters = queryUtil.getParameters(null, "1", "0");
    parameters.put(TASK_ID, taskId);
    parameters.put(TASK_JOB_EXECUTION, executionId);

    return getTaskExecution(SCHEDULER_TASK_EXECUTION, parameters);
  }

  /**
   * Get last execution from Database
   *
   * @param taskId    Task id
   * @param taskGroup Task group
   * @return
   * @throws AWException
   */
  private TaskExecution getLastExecutionFromDB(Integer taskId, String taskGroup) throws AWException {
    // Set context from the query
    ObjectNode parameters = queryUtil.getParameters(null, "1", "0");
    parameters.put(TASK_ID, taskId);
    parameters.put(TASK_GROUP, taskGroup);

    return getTaskExecution(SCHEDULER_LAST_TASK_EXECUTION, parameters);
  }

  /**
   * Launch a query and retrieve the first task execution from it
   *
   * @param query      Query
   * @param parameters Parameters
   * @return Task execution
   * @throws AWException
   */
  private TaskExecution getTaskExecution(String query, ObjectNode parameters) throws AWException {
    // Get task average time datalist
    ServiceData serviceData = queryService.launchPrivateQuery(query, parameters);
    List<TaskExecution> executionList = DataListUtil.asBeanList(serviceData.getDataList(), TaskExecution.class);
    return executionList.isEmpty() ? null : executionList.get(0);
  }

  /**
   * Get the average time as string
   *
   * @param taskId Task identifier
   * @return Average time as string
   * @throws AWException
   */
  public Integer getAverageTime(Integer taskId) throws AWException {
    // Get task average time datalist
    ObjectNode parameters = queryUtil.getParameters(null, "1", "0");
    parameters.put(TASK_ID, taskId);
    ServiceData serviceData = queryService.launchPrivateQuery(SCHEDULER_AVERAGE_TIME_QUERY, parameters);
    CellData cellData = DataListUtil.getCellData(serviceData.getDataList(), 0, "averageTime");
    return cellData == null || cellData.isEmpty() ? 0 : cellData.getIntegerValue();
  }

  /**
   * Check if the job is running
   *
   * @return boolean
   * @throws Exception
   * @parameter job
   */
  private boolean isJobRunning(JobDetail job) throws SchedulerException {
    // check if the job is in the scheduler currently executing job list
    for (JobExecutionContext jobExeContext : scheduler.getCurrentlyExecutingJobs()) {
      if (jobExeContext.getJobDetail().equals(job)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Clear the file modification table from database
   *
   * @param taskId
   */
  private void clearFileModificationTable(Integer taskId) throws AWException {
    getRequest().setParameter(TASK_IDE_TASK, JsonNodeFactory.instance.numberNode(taskId));
    maintainService.launchPrivateMaintain(FILE_DELETE_MODIFICATION_QUERY);
  }

  /**
   * Check if trigger exists
   *
   * @param triggerKey
   * @return
   * @throws SchedulerException
   */
  private boolean checkTrigger(TriggerKey triggerKey) throws SchedulerException {
    return scheduler.checkExists(triggerKey);
  }

  /**
   * On finish task event
   *
   * @param task
   * @param execution
   */
  public void onFinishTask(Task task, TaskExecution execution) throws AWException {
    // Update parent status if failed
    updateParentStatus(execution);

    // Check if the dependencies have to be executed
    if (checkDependencyExecution(task, execution)) {
      executeDependencies(task, execution);
    }
  }

  /**
   * Set task status checking sependencies status and task configuration
   *
   * @param execution
   */
  private void updateParentStatus(TaskExecution execution) throws AWException {
    if (execution.getParentExecution() != null) {
      try {
        Task parentTask = getTask(execution.getParentExecution().getTaskId()).get();
        if (TaskStatus.JOB_ERROR.equals(TaskStatus.valueOf(execution.getStatus())) && parentTask.isSetTaskOnWarningIfDependencyError()) {
          changeStatus(execution.getParentExecution(), TaskStatus.JOB_WARNING,
            getLocale(WARNING_MESSAGE_TASK_DEPENDENCY_ERROR, execution.getTaskId().toString(), execution.getExecutionId().toString()));
        }
      } catch (Exception exc) {
        throw new AWException("Error setting task status", exc);
      }
    }
  }

  /**
   * Check if the task has to execute dependencies depending on its
   * configuration
   *
   * @param task      Task
   * @param execution Task execution
   * @return boolean
   */
  private boolean checkDependencyExecution(Task task, TaskExecution execution) {
    // Check if the dependencies have to be launched
    switch (TaskStatus.valueOf(execution.getStatus())) {
      case JOB_OK:
        return true;
      case JOB_WARNING:
        return task.isLaunchDependenciesOnWarning();
      case JOB_ERROR:
        return task.isLaunchDependenciesOnError();
      default:
        return false;
    }
  }

  /**
   * Executes the dependencies for the given task
   *
   * @param task
   */
  private void executeDependencies(Task task, TaskExecution execution) throws AWException {
    // Iterate dependencies and create a new task for each.
    for (TaskDependency dependency : task.getDependencyList()) {
      // Execute task dependency
      executeDependency(dependency.getTaskId(), execution);
    }
  }
}
