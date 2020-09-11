package com.almis.awe.scheduler.job.report;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.scheduler.bean.task.TaskParameter;
import com.almis.awe.scheduler.enums.TaskStatus;
import com.almis.awe.service.MaintainService;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.almis.awe.scheduler.constant.ReportConstants.*;

@Log4j2
@Component
public class EmailReportJob extends ReportJob {

  // Constants
  private static final String LIST_START = "<li><b>";
  private static final String LIST_END = "</li>";
  private static final String BOLD_END = ":</b> ";
  private static final String COLON_SPACE = ": ";
  private static final String NEW_LINE = "\n";

  // Autowired services
  private QueryUtil queryUtil;
  private MaintainService maintainService;
  private QueryService queryService;
  private ObjectMapper mapper;

  /**
   * Autowired constructor
   *
   * @param queryUtil
   * @param maintainService
   */
  @Autowired
  public EmailReportJob(QueryUtil queryUtil, MaintainService maintainService, QueryService queryService) {
    this.queryUtil = queryUtil;
    this.maintainService = maintainService;
    this.queryService = queryService;
    this.mapper = new ObjectMapper();
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    // Store task and execution
    super.execute(context);

    // Store task and execution in parameters
    ObjectNode parameters = queryUtil.getParameters(getTask().getDatabase());
    parameters.set(REPORT_DESTINATION_EMAILS, mapper.valueToTree(getTask().getReport().getReportEmailDestination()));
    parameters.put(REPORT_TITLE, getTask().getReport().getReportTitle());
    parameters.put(REPORT_MESSAGE_HTML, constructHTMLMessage());
    parameters.put(REPORT_MESSAGE_TEXT, constructTextMessage());

    try {
      maintainService.launchPrivateMaintain(REPORT_MAINTAIN_TARGET, parameters);
    } catch (AWException exc) {
      // Log error
      log.error("Report generation error for task {}", getTask().getTaskId(), exc);
    }
  }


  /**
   * Get HTML string with report message with title
   *
   * @return
   * @throws AWException
   */
  private String constructHTMLMessage() {
    String msg = "";
    msg += "<html>";
    msg += constructHTMLHeader(getTask().getReport().getReportTitle() != null ? getTask().getReport().getReportTitle() : getLocale(PARAMETER_TASK_DETAILS));
    msg += constructHTMLBody();
    msg += "</html>";
    return msg;
  }

  /**
   * Get HTML message header
   *
   * @return String
   */
  private String constructHTMLHeader(String title) {
    StringBuilder builder = new StringBuilder();
    builder.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
    builder.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
    builder.append("<head>");
    builder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
    builder.append("<title>").append(title).append("</title>");
    builder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
    builder.append("</head>");
    return builder.toString();
  }

  /**
   * Get HTML message body
   *
   * @return String
   */
  private String constructHTMLBody() {
    String msg = "";
    // Construct HTML body
    msg += "<p>" + getTask().getReport().getReportMessage() + "</p>";
    msg += "<div style=\"border:1px solid black;\">";
    try {
      msg += "<br><b><u>" + getLocale(PARAMETER_TASK_DETAILS) + "</u></b>";
      msg += getTaskDetailsMessage();
    } catch (Exception exc) {
      // No details
      log.error("Error generating HTML task details for task #{} execution #{}", getExecution().getTaskId(), getExecution().getExecutionId(), exc);
    }
    msg += "</div>";
    return msg;
  }

  /**
   * Get HTML string with task details
   *
   * @return
   * @throws AWException
   */
  private String getTaskDetailsMessage() throws AWException {
    StringBuilder builder = new StringBuilder();

    String launchType = queryService.findLabel("LchTxtTyp", getExecution().getGroupId());
    String statusColor = queryService.findLabel("StaColor", getExecution().getStatus().toString());
    String statusText = queryService.findLabel("StaTyp", getExecution().getStatus().toString());

    // Construct HTML task details message
    builder.append("<div><ul>");
    builder.append(LIST_START).append(getLocale(PARAMETER_NAME)).append(BOLD_END).append(getTask().getName()).append(LIST_END);
    builder.append(LIST_START).append(getLocale(PARAMETER_IDE)).append(BOLD_END).append(getTask().getTrigger().getKey()).append(LIST_END);
    builder.append(LIST_START).append(getLocale(PARAMETER_LAUNCH_TYPE)).append(BOLD_END).append(getLocale(launchType)).append(LIST_END);
    builder.append(LIST_START).append(getLocale(PARAMETER_DESCRIPTION)).append(BOLD_END).append(getTask().getDescription()).append(LIST_END);
    builder.append("<li style=\"margin-top:12px;\"><b>")
      .append(getLocale(PARAMETER_STATUS)).append(BOLD_END)
      .append("<span style = \"padding:10px;color:white;-moz-border-radius: 20px; -webkit-border-radius: 20px; border-radius: 20px;background-color:")
      .append(statusColor).append("\">").append(getLocale(statusText)).append("</span></li>)");

    switch (TaskStatus.valueOf(getExecution().getStatus())) {
      case JOB_ERROR:
        builder.append(LIST_START).append(getLocale(ERROR_LOG)).append(BOLD_END).append(getExecution().getDescription()).append(LIST_END);
        break;
      case JOB_WARNING:
      case JOB_INFO:
        builder.append(LIST_START).append(queryService.findLabel("StaTit", getExecution().getStatus().toString())).append(BOLD_END).append(getExecution().getDescription()).append(LIST_END);
        break;
      default:
    }

    builder.append(LIST_START).append(getLocale(PARAMETER_EXECUTED_COMMAND)).append(BOLD_END).append(getTask().getAction()).append(LIST_END);
    builder.append(LIST_START).append(getLocale(PARAMETER_PARAMETERS)).append(BOLD_END).append(LIST_END);
    builder.append("<ul>");
    for (TaskParameter parameter : getTask().getParameterList()) {
      builder.append(LIST_START).append(parameter.getName()).append(BOLD_END).append(parameter.getValue()).append(LIST_END);
    }
    builder.append("</ul></li>");
    builder.append("</ul>");
    builder.append("</div>");

    return builder.toString();
  }

  /**
   * Get text string with report message with title
   *
   * @return
   * @throws AWException
   */
  private String constructTextMessage() {
    String msg = "";
    msg += getTask().getReport().getReportTitle() != null ? getTask().getReport().getReportTitle() : getLocale(PARAMETER_TASK_DETAILS) + NEW_LINE;
    msg += constructTextBody();
    return msg;
  }

  /**
   * Get Text message body
   *
   * @return String
   */
  private String constructTextBody() {
    String msg = "";
    // Construct text body
    msg += getTask().getReport().getReportMessage() + "\n\n";
    try {
      msg += getLocale(PARAMETER_TASK_DETAILS) + NEW_LINE;
      msg += getTextTaskDetailsMessage();
    } catch (Exception exc) {
      // No details
      log.error("Error generating text task details for task #{} execution #{}", getExecution().getTaskId(), getExecution().getExecutionId(), exc);
    }
    return msg;
  }

  /**
   * Get HTML string with task details
   *
   * @return
   * @throws AWException
   */
  private String getTextTaskDetailsMessage() throws AWException {
    StringBuilder builder = new StringBuilder();

    String launchType = queryService.findLabel("LchTxtTyp", getExecution().getGroupId());
    String statusText = queryService.findLabel("StaTyp", getExecution().getStatus().toString());

    // Construct HTML task details message
    builder.append(getLocale(PARAMETER_NAME)).append(COLON_SPACE).append(getTask().getName()).append(NEW_LINE);
    builder.append(getLocale(PARAMETER_IDE)).append(COLON_SPACE).append(getTask().getTrigger().getKey()).append(NEW_LINE);
    builder.append(getLocale(PARAMETER_LAUNCH_TYPE)).append(COLON_SPACE).append(getLocale(launchType)).append(NEW_LINE);
    builder.append(getLocale(PARAMETER_DESCRIPTION)).append(COLON_SPACE).append(getTask().getDescription()).append(NEW_LINE);
    builder.append(getLocale(PARAMETER_STATUS)).append(COLON_SPACE).append(getLocale(statusText)).append(NEW_LINE);

    switch (TaskStatus.valueOf(getExecution().getStatus())) {
      case JOB_ERROR:
        builder.append(getLocale(ERROR_LOG)).append(COLON_SPACE).append(getExecution().getDescription()).append(NEW_LINE);
        break;
      case JOB_WARNING:
      case JOB_INFO:
        builder.append(queryService.findLabel("StaTit", getExecution().getStatus().toString())).append(COLON_SPACE).append(getExecution().getDescription()).append(NEW_LINE);
        break;
      default:
        break;
    }

    builder.append(getLocale(PARAMETER_EXECUTED_COMMAND)).append(COLON_SPACE).append(getTask().getAction()).append(NEW_LINE);
    builder.append(getLocale(PARAMETER_PARAMETERS)).append(NEW_LINE);
    for (TaskParameter parameter : getTask().getParameterList()) {
      builder.append("  - " + parameter.getName()).append(COLON_SPACE).append(parameter.getValue()).append(NEW_LINE);
    }

    return builder.toString();
  }
}
