package com.almis.awe.scheduler.constant;

/**
 *
 * @author dfuentes
 */
public class TaskConstants {

  private TaskConstants(){}

  // Task parameter constants
  public static final String TASK_IDE = "taskId";
  public static final String TASK_IDE_LIST = "Ide";
  public static final String TASK_IDE_TASK = "IdeTsk";
  public static final String TASK_IDE_SELECTED = "Ide.selected";
  public static final String TASK_IDE_TASK_SELECTED = "IdeTsk.selected";
  public static final String TASK_NAME = "Nam";
  public static final String TASK_ACTIVE = "Act";
  public static final String TASK_DESCRIPTION = "Des";
  public static final String NUM_STORED_EXECUTIONS = "NumStoExe";
  public static final String TASK_EXECUTION_TIMEOUT = "TimOutExe";
  public static final String TASK_EXECUTION_TYPE = "TypExe";
  public static final String TASK_EXECUTION_SERVER_IDE = "IdeSrvExe";
  public static final String TASK_EXECUTION_PATH = "CmdExePth";
  public static final String TASK_COMMAND = "CmdExe";
  public static final String TASK_LAUNCH_DEPENDENCIES_ON_ERROR = "LchDepErr";
  public static final String TASK_LAUNCH_DEPENDENCIES_ON_WARNING = "LchDepWrn";
  public static final String TASK_SET_WARNING_ON_ERROR = "LchSetWrn";
  public static final String TASK_LAUNCH_TYPE_LIST = "TypLch";
  public static final String TASK_LAUNCHED_BY_LIST = "LchBy";
  public static final String TASK_LAUNCH_TYPE = "launchType";
  public static final String TASK_LAST_EXECUTION = "LstTim";
  public static final String TASK_NEXT_EXECUTION = "NxtTim";
  public static final String TASK_AVERAGE_IDE = "Ide";
  public static final String TASK_AVERAGE_IDE_TASK = "IdeTsk";
  public static final String TASK_AVERAGE_IDE_LIST = "IdeLst";
  public static final String TASK_AVERAGE_TIME = "AvgTim";
  public static final String TASK_AVERAGE_DATE = "ExeDatTim";
  public static final String TASK_EXECUTION_DATE_TIME = "ExeDatTim";
  public static final String TASK_STATUS = "Sta";
  public static final String TASK_PROGRESS = "Prg";
  public static final String TASK_ACTIVE_IDE = "SchIde";
  public static final String TASK_ACTIVE_NEXT_EXECUTION = "NxtExe";
  public static final String TASK_PARAMETER_IDE = "IdePar";
  public static final String TASK_PARAMETER_NAME = "ParNam";
  public static final String TASK_PARAMETER_SOURCE = "ParSrc";
  public static final String TASK_PARAMETER_TYPE = "ParTyp";
  public static final String TASK_PARAMETER_VALUE = "ParVal";
  public static final String TASK_DATABASE = "db";
  public static final String TASK_SITE = "site";
  public static final String FILE_PATH = "filePath";
  public static final String UPDATE_DATE = "date";

  // year suggest parameters
  public static final String YEAR_LABEL = "yearStr";
  public static final String YEAR_VALUE = "yearVal";

  // Default group prefix for dependencies
  public static final String TASK_SEPARATOR = "-";
  public static final String GROUP_SEPARATOR = "_";

  // Default groups for tasks
  public static final String DEFAULT_GROUP = "TASK_QUERY";
  public static final String MANUAL_GROUP = "MANUAL_TASK";
  public static final String SCHEDULED_GROUP = "SCHEDULED_TASK";
  public static final String FILE_TRACKING_GROUP = "FILE_TRACKING_TASK";
  public static final String DEPENDENCY_GROUP = "DEPENDANT_TASK";
  public static final String REPORT_GROUP = "REPORT_TASK";

  // Execution fields
  public static final String TASK_EXECUTION_ID = "ExeTsk";
  public static final String TASK_EXECUTION_TIME = "ExeTim";
  public static final String TASK_EXECUTION_PROGRESS = "ExePrg";
  public static final String TASK_EXECUTION_ICON = "ExeStaIco";
  public static final String TASK_EXECUTION_LOG_PATH = "ExecutionLogPath";
}
