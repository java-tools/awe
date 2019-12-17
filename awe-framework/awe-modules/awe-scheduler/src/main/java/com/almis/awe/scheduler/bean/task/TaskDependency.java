package com.almis.awe.scheduler.bean.task;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Task dependency bean
 */
@Data
@Accessors(chain = true)
public class TaskDependency implements Serializable {
  private Integer taskId;
  private Integer parentId;
}
