package com.almis.awe.scheduler.bean.event;

import com.almis.awe.scheduler.bean.task.TaskExecution;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class SchedulerTaskProgressEvent extends ApplicationEvent {

  private TaskExecution taskExecution;
  private Integer averageTime;

  public SchedulerTaskProgressEvent(Object source, TaskExecution taskExecution, Integer averageTime) {
    super(source);
    this.taskExecution = taskExecution;
    this.averageTime = averageTime;
  }
}
