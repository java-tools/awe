package com.almis.awe.scheduler.bean.report;

import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author dfuentes
 */
@Getter
@Setter
public class EmailResultBean extends ResultBean implements Serializable {
  private List<String> destinationEmails;
  private String title;

  /**
   * Constructor
   *
   * @param task
   */
  public EmailResultBean(Task task, TaskExecution execution) {
    super(task, execution);
    this.title = task.getReport().getReportTitle();
    this.destinationEmails = task.getReport().getReportEmailDestination();
  }
}
