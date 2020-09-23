package com.almis.awe.scheduler.job.report;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static com.almis.awe.scheduler.constant.JobConstants.TASK;
import static com.almis.awe.scheduler.constant.JobConstants.TASK_JOB_EXECUTION;

@Log4j2
@Getter
@Setter
public abstract class ReportJob extends ServiceConfig implements Job {

  private Task task;
  private TaskExecution execution;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    this.task = (Task) context.getJobDetail().getJobDataMap().get(TASK);
    this.execution = (TaskExecution) context.getJobDetail().getJobDataMap().get(TASK_JOB_EXECUTION);
  }

  /**
   * Check if the report send status contains the current task status
   *
   * @return boolean
   */
  protected boolean checkSendStatus(Task task) {
    // check status
    return task.getReport().getReportSendStatus().contains(String.valueOf(task.getStatus().getValue()));
  }
}
