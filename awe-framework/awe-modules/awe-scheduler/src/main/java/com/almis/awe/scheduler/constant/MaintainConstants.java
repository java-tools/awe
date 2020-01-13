package com.almis.awe.scheduler.constant;

/**
 *
 * @author dfuentes
 */
public class MaintainConstants {

  private MaintainConstants(){}

  // Scheduler related maintains
  public static final String UPDATE_TASKS_CALENDAR_MAINTAIN = "UpdSchTskWthCal";
  public static final String REMOVE_SELECTED_CALENDAR_FROM_TASKS = "UpdSchTskWthCal";
  public static final String FILE_INSERT_MODIFICATION_QUERY = "insertFile";
  public static final String FILE_DELETE_MODIFICATION_QUERY = "deleteFile";
  public static final String FILE_UPDATE_MODIFICATION_QUERY = "updateFile";
  public static final String TASK_START = "startTask";
  public static final String TASK_END = "endTask";
  public static final String GET_ALL_EXECUTIONS_WITH_DATES = "getSortedExecutions";
  public static final String PURGE_EXECUTION_LOGS = "purgeExecutionLogs";
  public static final String GET_ALL_EXECUTIONS = "getAllExecutions";
  public static final String UPDATE_INTERRUPTED_TASKS = "updateInterruptedTasks";
  public static final String TASK_UPDATE_STATUS = "updateTaskStatus";
}
