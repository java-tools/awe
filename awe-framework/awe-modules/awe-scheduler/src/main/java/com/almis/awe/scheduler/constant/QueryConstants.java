package com.almis.awe.scheduler.constant;

/**
 *
 * @author dfuentes
 */
public class QueryConstants {

  private QueryConstants(){}

  // Scheduler related queries
  public static final String SCHEDULER_TASK_QUERY = "taskData";
  public static final String SCHEDULER_DEPENDECIES_QUERY = "AweSchTskDpnLst";
  public static final String SCHEDULER_PARAMETER_QUERY = "AweSchTskParLst";
  public static final String SCHEDULER_TASK_PARAMETERS_QUERY = "taskParameters";
  public static final String SCHEDULER_TASK_DEPENDENCIES_QUERY = "taskDependencies";
  public static final String SCHEDULER_TASK_CALENDAR_QUERY = "taskCalendar";
  public static final String SCHEDULER_TASK_CALENDAR_DATES_QUERY = "taskCalendarDates";


  public static final String SCHEDULER_SERVER_QUERY = "SchSrv";
  public static final String SCHEDULER_SERVER_DATA = "serverData";
  public static final String SCHEDULER_TASK_LIST_QUERY = "getSchedulerTaskList";
  public static final String SCHEDULER_LOAD_TASK_DETAILS_QUERY = "javaSchedulerTaskList";
  public static final String SCHEDULER_LOAD_TASK_DETAILS_WITH_FILTER_QUERY = "javaSchedulerTaskListWfilter";
  public static final String SCHEDULER_AVERAGE_TIME_QUERY = "getTaskAverageTime";
  public static final String SCHEDULER_LAST_TASK_EXECUTION = "getLastExecution";
  public static final String SCHEDULER_TASK_EXECUTION = "getTaskExecution";
  public static final String SCHEDULER_CALENDAR_LIST_QUERY = "javaSchCalLst";
  public static final String SCHEDULER_FILE_MODIFICATIONS_QUERY = "SchFil";
  public static final String SCHEDULER_GET_TASKS_WITH_CALENDARS = "tasksUsingCalendars";

}
