package com.almis.awe.scheduler.factory;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.scheduler.builder.task.*;
import com.almis.awe.scheduler.enums.TaskLaunchType;
import org.quartz.Scheduler;

import static com.almis.awe.scheduler.constant.TaskConstants.TASK_LAUNCH_TYPE;

public class TaskFactory {
  // Private constructor
  private TaskFactory() {
  }

  /**
   * Generate a task using a task builder depending on task data
   *
   * @param taskData Task data
   * @return Task
   */
  public static TaskBuilder getInstance(DataList taskData, Scheduler scheduler) throws AWException {
    return getInstance(taskData, null, scheduler);
  }

  /**
   * Generate a task using a task builder depending on task data
   *
   * @param taskData Task data
   * @return Task
   */
  public static TaskBuilder getInstance(DataList taskData, TaskLaunchType launchType, Scheduler scheduler) throws AWException {
    // Depending on task data, use a builder
    TaskLaunchType taskLaunchType = launchType != null ? launchType : TaskLaunchType.valueOf(DataListUtil.getCellData(taskData, 0, TASK_LAUNCH_TYPE).getIntegerValue());
    switch (taskLaunchType) {
      // Scheduled task
      case SCHEDULED:
        return new ScheduledTaskBuilder(taskData).setScheduler(scheduler);
      case FILE_TRACKING:
        return new FileTaskBuilder(taskData).setScheduler(scheduler);
      case DEPENDENCY:
        return new DependantTaskBuilder(taskData).setScheduler(scheduler);
      case MANUAL:
      default:
        return new ManualTaskBuilder(taskData).setScheduler(scheduler);
    }
  }
}
