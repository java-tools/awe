package com.almis.awe.scheduler.dao;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.scheduler.listener.SchedulerJobListener;
import com.almis.awe.scheduler.listener.SchedulerTriggerListener;
import com.almis.awe.scheduler.service.TaskService;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static com.almis.awe.scheduler.constant.JobConstants.TASK_VISIBLE;

@Repository
@Log4j2
public class SchedulerDAO extends ServiceConfig {

  private static final String SCHEDULER_STATUS = "schedulerStatus";

  @Value("${scheduler.tasks.load.on.start:true}")
  private boolean loadSchedulerTasks;

  @Value("${scheduler.tasks.wait.on.stop:true}")
  private boolean waitForTasksOnStop;

  // Autowired services
  private Scheduler scheduler;
  private CalendarDAO calendarDAO;
  private TaskService taskService;
  private SchedulerTriggerListener triggerListener;
  private SchedulerJobListener jobListener;

  /**
   * Constructor
   */
  @Autowired
  public SchedulerDAO(Scheduler scheduler, CalendarDAO calendarDAO, TaskService taskService,
                      SchedulerTriggerListener triggerListener, SchedulerJobListener jobListener) {
    this.scheduler = scheduler;
    this.calendarDAO = calendarDAO;
    this.taskService = taskService;
    this.triggerListener = triggerListener;
    this.jobListener = jobListener;
  }

  /**
   * Start scheduler
   *
   * @throws SchedulerException
   */
  private String startScheduler() throws SchedulerException {
    log.info("===== Starting scheduler tasks ========");

    // Start scheduler (Load last execution time tasks and calendars)
    scheduler.start();

    log.info("[SCHEDULER] The scheduler has been started");

    // Add listeners to scheduler
    scheduler.getListenerManager().addTriggerListener(triggerListener);
    scheduler.getListenerManager().addJobListener(jobListener);

    log.debug("[SCHEDULER] Trigger and Job listeners added to the scheduler");
    return "The scheduler has been started";
  }

  /**
   * Start the scheduler service
   *
   * @throws AWException
   */
  public ServiceData start() throws AWException {
    return startNoQuartz();
  }

  /**
   * Start the scheduler service
   *
   * @throws AWException
   */
  public ServiceData startNoQuartz() throws AWException {
    ServiceData serviceData = new ServiceData();
    try {
      // Start scheduler
      serviceData.setTitle(startScheduler());

      // Load calendars from DB to the scheduler
      calendarDAO.loadSchedulerCalendar();

      log.debug("[SCHEDULER] Calendars loaded from database and added to the scheduler");

      // Update interrupted task status
      taskService.updateInterruptedTasks();

      // Check if tasks have to be loaded
      if (loadSchedulerTasks) {
        // Load Tasks from DB to scheduler
        taskService.loadSchedulerTasks();

        log.debug("[SCHEDULER] Tasks loaded from database and added to the scheduler");
      }
    } catch (SchedulerException exc) {
      throw new AWException("Error trying to start the scheduler", exc);
    }

    return serviceData;
  }

  /**
   * Stop the scheduler service
   *
   * @throws AWException
   */
  public ServiceData stop() throws AWException {
    ServiceData serviceData = new ServiceData();
    try {
      log.debug("[SCHEDULER] The scheduler has started shutting down");

      // Stop jobs that are currently been executed
      interruptCurrentlyExecutingJobs();

      // Shutdown the scheduler
      scheduler.standby();
      log.info("[SCHEDULER] The scheduler has been shutdown correctly");
      serviceData.setTitle("The scheduler has been shutdown correctly");
    } catch (Exception exc) {
      throw new AWException("Error trying to shutdown the scheduler", exc);
    }

    return serviceData;
  }

  /**
   * Scheduler's emergency reboot method
   *
   * @throws AWException
   */
  public ServiceData restart() throws AWException {
    try {
      // Stop jobs that are currently been executed
      stop();
      return startNoQuartz();
    } catch (Exception exc) {
      throw new AWException("Error trying to restart the scheduler", exc);
    }
  }

  /**
   * Clear all scheduled tasks and stop the scheduler.
   *
   * @return
   */
  public ServiceData clearAndStop() throws AWException {
    ServiceData serviceData = new ServiceData();
    try {
      scheduler.clear();
      stop();
      return serviceData.setTitle("Scheduler stopped correctly");
    } catch (Exception exc) {
      throw new AWException("Error trying to shutdown the scheduler", exc);
    }
  }

  /*
   * *******************************************
   * SCHEDULER MANAGEMENT METHODS
   * *******************************************
   */

  /**
   * Get currently executing job instances from the scheduler isntance
   *
   * @return
   * @throws AWException
   */
  public ServiceData getCurrentlyExecutingJobs() throws AWException {
    ServiceData serviceData = new ServiceData();
    DataList dataList = new DataList();
    serviceData.setDataList(dataList);

    try {
      if (!isSchedulerUp()) {
        return serviceData;
      }

      List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
      for (JobExecutionContext executingJob : executingJobs) {
        Map<String, CellData> row = new HashMap<>();
        row.put("taskKey", new CellData(executingJob.getTrigger().getKey().getName()));
        row.put("triggerKey", new CellData(executingJob.getTrigger().getKey().toString()));
        row.put("calendar", new CellData(executingJob.getTrigger().getCalendarName() != null ? scheduler.getCalendar(executingJob.getTrigger().getCalendarName()) : ""));
        row.put("executionTime", new CellData(executingJob.getFireTime() != null ? DateUtil.dat2WebTimestamp(executingJob.getFireTime()) : ""));
        row.put("scheduledExecutionTime", new CellData(executingJob.getScheduledFireTime() != null ? DateUtil.dat2WebTimestamp(executingJob.getScheduledFireTime()) : ""));
        row.put("nextExecutionTime", new CellData(executingJob.getNextFireTime() != null ? DateUtil.dat2WebTimestamp(executingJob.getNextFireTime()) : ""));
        row.put("previousExecutionTime", new CellData(executingJob.getPreviousFireTime() != null ? DateUtil.dat2WebTimestamp(executingJob.getPreviousFireTime()) : ""));
        row.put("jobRuntime", new CellData(executingJob.getJobRunTime()));
        row.put("jobStatus", new CellData(getTriggerState(executingJob.getTrigger().getKey().getGroup(), executingJob.getTrigger().getKey().getName())));
        dataList.addRow(row);
      }
      return serviceData;
    } catch (Exception e) {
      throw new AWException("[SCHEDULER-MANAGER] Error loading currently executing tasks", e);
    }
  }

  /**
   * Get configured
   *
   * @return
   * @throws AWException
   */
  public ServiceData getConfiguredJobs() throws AWException {
    ServiceData serviceData = new ServiceData();
    DataList dataList = new DataList();
    serviceData.setDataList(dataList);

    try {
      if (!isSchedulerUp()) {
        return serviceData;
      }

      // Retrieve all jobs but TIMEOUT ones
      List<Trigger> schedulerJobs = getAllJobs()
        .stream()
        .filter(t -> (boolean) t.getJobDataMap().get(TASK_VISIBLE))
        .collect(Collectors.toList());

      for (Trigger currentTrigger : schedulerJobs) {
        Map<String, CellData> row = new HashMap<>();
        row.put("taskKey", new CellData(currentTrigger.getKey().getName()));
        row.put("triggerKey", new CellData(currentTrigger.getKey().toString()));
        row.put("taskCalendar", new CellData(currentTrigger.getCalendarName() != null ? scheduler.getCalendar(currentTrigger.getCalendarName()) : ""));
        row.put("taskStartTime", new CellData(currentTrigger.getStartTime() == null ? "-" : DateUtil.dat2WebTimestamp(currentTrigger.getStartTime())));
        row.put("taskEndTime", new CellData(currentTrigger.getEndTime() == null ? "-" : DateUtil.dat2WebTimestamp(currentTrigger.getEndTime())));
        row.put("taskFinalExecutionTime", new CellData(currentTrigger.getFinalFireTime() == null ? "-" : DateUtil.dat2WebTimestamp(currentTrigger.getFinalFireTime())));
        row.put("taskNextExecutionTime", new CellData(currentTrigger.getNextFireTime() == null ? "-" : DateUtil.dat2WebTimestamp(currentTrigger.getNextFireTime())));
        row.put("taskPreviousExecutionTime", new CellData(currentTrigger.getPreviousFireTime() == null ? "-" : DateUtil.dat2WebTimestamp(currentTrigger.getPreviousFireTime())));
        dataList.addRow(row);
      }

      return serviceData;
    } catch (Exception exc) {
      throw new AWException("[SCHEDULER-MANAGER] Error loading currently executing tasks", exc);
    }
  }

  /**
   * Returns the job group names
   *
   * @return
   * @throws AWException
   */
  private List<Trigger> getAllJobs() throws AWException {

    List<Trigger> triggerDetails = new ArrayList<>();
    try {
      List<String> triggerGroupNames = this.scheduler.getTriggerGroupNames();

      for (String triggerGroupName : triggerGroupNames) {
        Set<TriggerKey> triggerKeys = this.scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroupName));
        for (TriggerKey triggerKey : triggerKeys) {
          triggerDetails.add(this.scheduler.getTrigger(triggerKey));
        }
      }
    } catch (Exception e) {
      throw new AWException(e.getLocalizedMessage(), e);
    }

    return triggerDetails;
  }

  /**
   * Returns information about the configured scheduler
   *
   * @return
   * @throws AWException
   */
  public ServiceData getSchedulerMetadata() throws AWException {
    ServiceData serviceData = new ServiceData();

    try {
      DataList dataList = new DataList();
      Map<String, CellData> row = new HashMap<>();
      SchedulerMetaData metadata = this.scheduler.getMetaData();
      row.put("schedulerInstanceId", new CellData(metadata.getSchedulerInstanceId()));
      row.put("schedulerName", new CellData(metadata.getSchedulerName()));
      row.put("schedulerVersion", new CellData(metadata.getVersion()));
      row.put("schedulerRemote", new CellData(String.valueOf(metadata.isSchedulerRemote())));
      row.put("schedulerJobstoreClass", new CellData(metadata.getJobStoreClass().getName()));
      row.put("schedulerJobstoreCluster", new CellData(String.valueOf(metadata.isJobStoreClustered())));
      row.put("schedulerJobstorePersistence", new CellData(String.valueOf(metadata.isJobStoreSupportsPersistence())));
      row.put("schedulerExecutedJobs", new CellData(metadata.getNumberOfJobsExecuted()));
      row.put("schedulerRunningSince", new CellData(metadata.getRunningSince() == null ? "Stopped" : DateUtil.dat2WebTimestamp(metadata.getRunningSince())));
      row.put("schedulerThreadPoolClass", new CellData(metadata.getThreadPoolClass().getName()));
      row.put("schedulerThreadPoolSize", new CellData(metadata.getThreadPoolSize()));

      String schedulerStatus = "Stopped";
      if (metadata.isInStandbyMode()) {
        schedulerStatus = "Standby";
      } else if (metadata.isStarted()) {
        schedulerStatus = "Started and running";
      }
      row.put(SCHEDULER_STATUS, new CellData(schedulerStatus));

      dataList.addRow(row);
      serviceData.setDataList(dataList);

      metadata.getSummary();
    } catch (Exception e) {
      throw new AWException(e.getLocalizedMessage(), e);
    }
    return serviceData;
  }

  private boolean isSchedulerUp() throws SchedulerException {
    return !scheduler.isInStandbyMode() && scheduler.isStarted();
  }

  /**
   * Get the trigger state for the given group and trigger key
   *
   * @param group
   * @param key
   * @return
   * @throws Exception
   */
  public String getTriggerState(String group, String key) throws AWException {
    try {
      return scheduler.getTriggerState(new TriggerKey(key, group)).name();
    } catch (SchedulerException exc) {
      throw new AWException(getLocale("[SCHEDULER] Error trying to retrieve trigger state from group: {} and key {}", group, key), exc);
    }
  }

  /**
   * Interrupt currently executing jobs
   */
  private void interruptCurrentlyExecutingJobs() throws SchedulerException {
    // Iterate through and stop all the executing jobs
    for (JobExecutionContext job : scheduler.getCurrentlyExecutingJobs()) {
      scheduler.interrupt(job.getJobDetail().getKey());
      log.debug("[SCHEDULER][TASK_QUERY {}] The task has been interrupted", job.getTrigger().getKey());
    }
  }

}
