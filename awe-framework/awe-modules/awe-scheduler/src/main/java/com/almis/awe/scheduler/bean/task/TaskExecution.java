package com.almis.awe.scheduler.bean.task;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

import static com.almis.awe.scheduler.constant.TaskConstants.TASK_SEPARATOR;

@Data
@Accessors(chain = true)
public class TaskExecution implements Serializable {
  private Integer taskId;
  private String name;
  private String groupId;
  private Integer executionId;
  private Date initialDate;
  private Date endDate;
  private Integer executionTime;
  private Integer status;
  private String launchedBy;
  private String description;
  private TaskExecution parentExecution;

  public String getKey() {
    return taskId + TASK_SEPARATOR + executionId;
  }
}
