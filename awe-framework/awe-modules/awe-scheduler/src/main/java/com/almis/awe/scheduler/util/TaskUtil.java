package com.almis.awe.scheduler.util;

import com.almis.awe.scheduler.enums.TaskLaunchType;

import static com.almis.awe.scheduler.constant.TaskConstants.*;
import static com.almis.awe.scheduler.enums.TaskLaunchType.*;

public class TaskUtil {
  private TaskUtil() {}

  /**
   * Returns the group for the given task
   *
   * @param taskLaunchType
   */
  public static String getGroupForLaunchType(Integer taskLaunchType) {
    switch (TaskLaunchType.valueOf(taskLaunchType)) {
      case SCHEDULED:
        return SCHEDULED_GROUP;
      case FILE_TRACKING:
        return FILE_TRACKING_GROUP;
      default:
        return MANUAL_GROUP;
    }
  }

  /**
   * Returns the group for the given task
   *
   * @param group
   */
  public static TaskLaunchType getLaunchTypeForGroup(String group) {
    switch (group) {
      case SCHEDULED_GROUP:
        return SCHEDULED;
      case FILE_TRACKING_GROUP:
        return FILE_TRACKING;
      default:
        return MANUAL;
    }
  }
}
