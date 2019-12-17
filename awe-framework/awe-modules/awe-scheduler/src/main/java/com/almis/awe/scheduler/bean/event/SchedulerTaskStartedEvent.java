package com.almis.awe.scheduler.bean.event;

import com.almis.awe.scheduler.bean.task.Task;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SchedulerTaskStartedEvent extends ApplicationEvent {
  private Task task;
  public SchedulerTaskStartedEvent(Object source, Task task) {
    super(source);
    this.task = task;
  }
}
