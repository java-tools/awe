package com.almis.awe.scheduler.bean.task;

import com.almis.awe.scheduler.bean.calendar.Calendar;
import com.almis.awe.scheduler.bean.calendar.Schedule;
import com.almis.awe.scheduler.bean.file.File;
import com.almis.awe.scheduler.bean.report.Report;
import com.almis.awe.scheduler.bean.trigger.CronPattern;
import com.almis.awe.scheduler.enums.TaskStatus;
import lombok.Data;
import lombok.experimental.Accessors;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.io.Serializable;
import java.util.List;

/**
 * Task bean
 *
 * @author dfuentes
 * @see CronPattern
 */
@Data
@Accessors(chain = true)
public class Task implements Serializable {

  // Task
  private Integer taskId;
  private String name;
  private boolean active;
  private String description;

  // Execution
  private Integer storedExecutions;
  private Integer executionTimeout;
  private Integer executionType;
  private Integer serverId;
  private String commandPath;
  private String action;
  private boolean launchDependenciesOnError;
  private boolean launchDependenciesOnWarning;
  private boolean setTaskOnWarningIfDependencyError;

  // Trigger
  private Integer launchType;
  private Schedule schedule;
  private Integer calendarId;

  // File
  private File file;

  // Reporting
  private Integer reportType;
  private Report report;

  private String database;

  // Dependencies list and configuration values
  private List<TaskDependency> dependencyList;

  // Parameters to add to the context for the current task execution
  private List<TaskParameter> parameterList;

  // The trigger that the task is going to have, null if the task is manual.
  private Trigger trigger = null;

  // The job that the task is going to execute or check
  private JobDetail job = null;

  // The calendar associated to the task
  private Calendar calendar = null;

  // Task status, by default OK
  private TaskStatus status = TaskStatus.JOB_OK;

  // Value to know if the current task is been executed as a dependency.
  boolean dependency = false;

  // Value to know if the task has been executed, used for the File type tasks.
  boolean executed = false;

  // Task group, by default is TASK_QUERY
  private String group = null;

  // Task launcher text
  private String launcher = null;

  // Parent execution
  private TaskExecution parentExecution = null;
}
