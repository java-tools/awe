package com.almis.awe.scheduler.constant;

/**
 *
 * @author dfuentes
 */
public class ReportConstants {

  private ReportConstants(){}

  // Scheduler report constants
  public static final String REPORT_TYPE = "RepTyp";
  public static final String REPORT_EMAIL_SERVER = "RepEmaSrv";
  public static final String REPORT_MAINTAIN_TARGET = "SchTskEmaRep";
  public static final String REPORT_TITLE = "RepTit";
  public static final String REPORT_MESSAGE_HTML = "RepMsgHtml";
  public static final String REPORT_MESSAGE_TEXT = "RepMsgText";
  public static final String REPORT_DESTINATION_EMAILS = "ToValue";
  public static final String REPORT_DESTINATION_USERS = "RepUsrDst";
  public static final String REPORT_SEND_STATUS = "RepSndSta";
  public static final String REPORT_MAINTAIN = "RepMntId";

  // Report message parameter labels
  public static final String SPLIT_PATTERN = ",";
  public static final String PARAMETER_TASK = "PARAMETER_TASK";
  public static final String PARAMETER_TASK_DETAILS = "PARAMETER_TASK_DETAILS";
  public static final String PARAMETER_TASK_DEPENDENCIES = "PARAMETER_TASK_DEPENDENCIES";
  public static final String PARAMETER_NAME = "PARAMETER_NAME";
  public static final String PARAMETER_DESCRIPTION = "PARAMETER_DESCRIPTION";
  public static final String PARAMETER_LAST_EXECUTION = "PARAMETER_LAST_EXECUTION";
  public static final String PARAMETER_AVERAGE_TIME = "PARAMETER_AVERAGE_TIME";
  public static final String PARAMETER_STATUS = "PARAMETER_STATUS";
  public static final String PARAMETER_IDE = "PARAMETER_IDE";
  public static final String PARAMETER_EXECUTE = "PARAMETER_EXECUTE";
  public static final String PARAMETER_LAUNCH_TYPE = "PARAMETER_LAUNCH_TYPE";
  public static final String PARAMETER_EXECUTED_COMMAND="PARAMETER_EXECUTED_COMMAND";
  public static final String PARAMETER_PARAMETERS="PARAMETER_PARAMETERS";
  public static final String ERROR_LOG="ERROR_LOG";

  // Launch types labels
  public static final String ENUM_LAUNCH_MANUAL = "ENUM_LAUNCH_MANUAL";
  public static final String ENUM_LAUNCH_SCHEDULED = "ENUM_LAUNCH_SCHEDULED";
  public static final String ENUM_LAUNCH_FILE = "ENUM_LAUNCH_FILE";
  public static final String ENUM_LAUNCH_DEPENDENCY="ENUM_LAUNCH_DEPENDENCY";
}
