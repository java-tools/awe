package com.almis.awe.scheduler.bean.report;


import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import com.almis.awe.scheduler.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author dfuentes
 */
@Getter
@Setter
public class ResultBean implements Serializable {
  // Task general data
  private Integer taskId;
  private String taskName;
  private String taskDescription;
  private Integer taskType;
  private Integer taskExecutionType;
  private TaskStatus taskStatus;
  private String taskExecutedCommand;
  private Date lastFireTime;

  private String message;
  private List<String> sendInCaseOf;

  private TaskExecution execution;

  /**
   * Contructor
   *
   * @param task
   */
  public ResultBean(Task task, TaskExecution execution) {
    this.execution = execution;
    this.taskId = task.getTaskId();
    this.taskName = task.getName();
    this.taskType = task.getLaunchType();
    this.taskDescription = task.getDescription();
    this.taskExecutionType = task.getExecutionType();
    this.taskStatus = task.getStatus();
    this.taskExecutedCommand = task.getAction();
    this.lastFireTime = task.getTrigger().getPreviousFireTime();

    this.message = task.getReport().getReportMessage();
    this.sendInCaseOf = task.getReport().getReportSendStatus();
  }
}

