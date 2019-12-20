package com.almis.awe.scheduler.bean.task;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Task parameter bean
 */
@Data
@Accessors(chain = true)
public class TaskParameter implements Serializable {
  private Integer id;
  private String name;
  private String source;
  private String type;
  private String value;
}
