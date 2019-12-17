package com.almis.awe.scheduler.bean.event;

import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class SchedulerTaskFinishedEvent extends ApplicationEvent {

  private Task task;
  private TaskExecution taskExecution;
  private Integer elapsedTime;

  public SchedulerTaskFinishedEvent(Object source, Task task, TaskExecution taskExecution, Integer elapsedTime) {
    super(source);
    this.task = task;
    this.taskExecution = taskExecution;
    this.elapsedTime = elapsedTime;
  }
}
