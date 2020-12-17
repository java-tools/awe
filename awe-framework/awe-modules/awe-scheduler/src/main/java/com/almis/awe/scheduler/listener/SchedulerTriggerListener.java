package com.almis.awe.scheduler.listener;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import com.almis.awe.scheduler.dao.TaskDAO;
import com.almis.awe.scheduler.enums.TaskStatus;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

import static com.almis.awe.scheduler.constant.ListenerConstants.TRIGGER_LISTENER_NAME;

/**
 * @author dfuentes
 */
@Log4j2
public class SchedulerTriggerListener extends ServiceConfig implements TriggerListener {

  // Autowired services
  private final TaskDAO taskDAO;

  // Locales
  private static final String SCHEDULER_ERROR_MESSAGE_TRIGGER_MISFIRED = "ERROR_MESSAGE_TRIGGER_MISFIRED";

  /**
   * Autowired constructor
   */
  public SchedulerTriggerListener(TaskDAO taskDAO) {
    this.taskDAO = taskDAO;
  }

  @Override
  public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
    // Do nothing
  }

  @Override
  public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
    Task task = taskDAO.getTask(trigger);
    return !taskDAO.isTaskExecutionAllowed(task);
  }

  @Override
  public void triggerMisfired(Trigger trigger) {
    // Get Task form jobs datamap
    Task task = taskDAO.getTask(trigger);
    TaskExecution execution = taskDAO.getTaskExecution(trigger);
    if (task != null && execution != null) {
      try {
        // Set job status as interrupted
        taskDAO.changeStatus(task, execution, TaskStatus.JOB_ERROR, getLocale(SCHEDULER_ERROR_MESSAGE_TRIGGER_MISFIRED));
      } catch (AWException exc) {
        log.error("Error trying to change task status: {}", task.getTaskId(), exc);
      }
    }
  }

  @Override
  public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction instruction) {
    // DO nothing
  }

  @Override
  public String getName() {
    return TRIGGER_LISTENER_NAME;
  }
}
