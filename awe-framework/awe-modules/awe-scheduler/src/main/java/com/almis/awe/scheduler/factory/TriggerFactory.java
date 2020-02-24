package com.almis.awe.scheduler.factory;

import com.almis.awe.exception.AWException;
import com.almis.awe.scheduler.bean.calendar.Schedule;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.builder.cron.PatternBuilder;
import com.almis.awe.scheduler.enums.TriggerType;
import com.almis.awe.scheduler.util.TaskUtil;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobDataMap;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.util.Calendar;

import static com.almis.awe.scheduler.constant.JobConstants.*;
import static com.almis.awe.scheduler.constant.TaskConstants.*;

@Log4j2
public class TriggerFactory {

  private static int instanceId = 1;

  // Private constructor
  private TriggerFactory() {}

  /**
   * Simple get instance
   *
   * @param type
   * @return
   * @throws AWException
   */
  public static Trigger getInstance(TriggerType type) throws AWException {
    return getInstance(type, new JobDataMap());
  }

  /**
   * Retrieve trigger instance
   *
   * @param type
   * @param dataMap
   * @return
   * @throws AWException
   */
  public static Trigger getInstance(TriggerType type, JobDataMap dataMap) throws AWException {
    Task task;
    // Depending on task data, use a builder
    switch (type) {

      // Progress task
      case PROGRESS:
        // Set task as not visible
        dataMap.put(TASK_VISIBLE, false);

        // Calculate progress refresh time
        Integer averageTime = (Integer) dataMap.get(TASK_JOB_AVERAGE_TIME);
        int progressTime = averageTime == 0 ? 1000 : Math.max(1, averageTime / 100);

        // Generate trigger
        return generateTriggerBuilder(dataMap, (String) dataMap.get("id"), "PROGRESS")
          .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(progressTime).repeatForever())
          .build();

      // Timeout task
      case TIMEOUT:
        // Set task as not visible
        dataMap.put(TASK_VISIBLE, false);

        // Define timeout date
        java.util.Calendar timeoutCalendar = java.util.Calendar.getInstance();
        timeoutCalendar.add(Calendar.SECOND, (Integer) dataMap.get(TASK_JOB_TIMEOUT));

        // Generate trigger
        return generateTriggerBuilder(dataMap, (String) dataMap.get("id"), "TIMEOUT")
          .startAt(timeoutCalendar.getTime()).build();

      // Manual launched task
      case MANUAL:
        return generateInmediateTrigger(dataMap, MANUAL_GROUP);

      // Dependency launched task
      case DEPENDENCY:
        return generateInmediateTrigger(dataMap, DEPENDENCY_GROUP);

      // Report job trigger
      case REPORT:
        return TriggerBuilder.newTrigger().build();

      // Scheduled task
      case TASK:
        // Get task
        task = (Task) dataMap.get(TASK);

        // Set task as visible
        dataMap.put(TASK_VISIBLE, true);

        // Generate scheduled task
        Schedule schedule = task.getSchedule();
        TriggerBuilder triggerBuilder = generateTriggerBuilder(dataMap, task.getTaskId().toString(), task.getGroup())
          .withDescription(task.getDescription());

        if (schedule.getInitialDateTime() != null) {
          triggerBuilder.startAt(schedule.getInitialDateTime());
        }

        if (schedule.getEndDateTime() != null) {
          triggerBuilder.endAt(schedule.getEndDateTime());
        }

        if (schedule.getCalendarId() != null && task.getCalendar().isActive()) {
          triggerBuilder.modifiedByCalendar(schedule.getCalendarId().toString());
        }

        if (task.getLaunchType() != 0) {
          triggerBuilder.withSchedule(new PatternBuilder(task.getSchedule()).build());
        }

        return triggerBuilder.build();
      default:
        log.error("The trigger type does not exist: {}", type);
        throw new AWException("The trigger type does not exist");
    }
  }

  /**
   * Generate inmmediate trigger for task
   * @param dataMap Data map
   * @param group Group
   * @return Trigger
   */
  private static Trigger generateInmediateTrigger(JobDataMap dataMap, String group) {
    // Get task
    Task task = (Task) dataMap.get(TASK);
    task.setGroup(group);
    task.setLaunchType(TaskUtil.getLaunchTypeForGroup(group).getValue());

    // Set task as visible
    dataMap.put(TASK_VISIBLE, true);

    // Generate trigger
    return generateTriggerBuilder(dataMap, task.getTaskId().toString() + TASK_SEPARATOR + instanceId++, group)
      .startNow()
      .build();
  }

  /**
   * Generate a trigger builder
   * @param dataMap Data map
   * @param id Identifier
   * @param group Group
   * @return Trigger builder
   */
  private static TriggerBuilder generateTriggerBuilder(JobDataMap dataMap, String id, String group) {
    return TriggerBuilder.newTrigger()
      .usingJobData(dataMap)
      .withIdentity(id, group);
  }
}
