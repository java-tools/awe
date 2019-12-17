package com.almis.awe.scheduler.bean.report;

import com.almis.awe.exception.AWException;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dfuentes
 */
@Getter
@Setter
public class MaintainResultBean extends ResultBean implements Serializable {
  private String maintain;

  /**
   * Constructor
   *
   * @param task
   * @throws AWException
   */
  public MaintainResultBean(Task task, TaskExecution execution) {
    super(task, execution);
    this.maintain = task.getReport().getReportMaintainId();
  }
}
