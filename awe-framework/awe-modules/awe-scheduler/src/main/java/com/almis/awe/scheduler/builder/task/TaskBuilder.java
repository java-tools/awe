package com.almis.awe.scheduler.builder.task;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.scheduler.bean.calendar.Calendar;
import com.almis.awe.scheduler.bean.calendar.Schedule;
import com.almis.awe.scheduler.bean.file.File;
import com.almis.awe.scheduler.bean.file.Server;
import com.almis.awe.scheduler.bean.report.Report;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskDependency;
import com.almis.awe.scheduler.bean.task.TaskParameter;
import com.almis.awe.scheduler.enums.JobType;
import com.almis.awe.scheduler.enums.TriggerType;
import com.almis.awe.scheduler.factory.JobFactory;
import com.almis.awe.scheduler.factory.TriggerFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;

import static com.almis.awe.scheduler.constant.JobConstants.TASK;

@Getter
@Setter
@Log4j2
@Accessors(chain = true)
public abstract class TaskBuilder {

  private Integer index;
  private DataList configurationData;
  private String site;
  private Task task;
  private Scheduler scheduler;

  /**
   * Set configuration data
   *
   * @param data
   * @return
   * @throws AWException
   */
  public TaskBuilder setData(DataList data) throws AWException {
    // Store data
    setConfigurationData(data);

    // Generate task data
    generateTask(data);

    // Retrieve task builder
    return this;
  }

  /**
   * Build the task
   *
   * @return Task built
   */
  public Task build() throws AWException {
    // Generate trigger
    generateTrigger();

    // Generate job
    generateJob();

    // Retrieve task
    return getTask();
  }

  /**
   * Fill task from datalist
   *
   * @param data
   * @return
   * @throws AWException
   */
  private void generateTask(DataList data) throws AWException {
    if (data == null || data.getRows().isEmpty()) {
      String dataListStatus = data == null ? "null" : "empty";
      log.error("[SCHEDULER] The task could not be created, the datalist is {}", dataListStatus);
      throw new AWException("[SCHEDULER] The datalist is " + dataListStatus);
    }

    // Initialize data
    setTask(DataListUtil.asBeanList(data, Task.class).get(0));
    getTask().setSchedule(DataListUtil.asBeanList(data, Schedule.class).get(0));
    getTask().setReport(DataListUtil.asBeanList(data, Report.class).get(0));
  }

  /**
   * Fill parameters from datalist
   *
   * @param data
   * @return
   * @throws AWException
   */
  public TaskBuilder setParameters(DataList data) throws AWException {
    getTask().setParameterList(DataListUtil.asBeanList(data, TaskParameter.class));
    return this;
  }

  /**
   * Fill dependencies from datalist
   *
   * @param data
   * @return
   * @throws AWException
   */
  public TaskBuilder setDependencies(DataList data) throws AWException {
    getTask().setDependencyList(DataListUtil.asBeanList(data, TaskDependency.class));
    return this;
  }

  /**
   * Retrieve calendar id
   *
   * @return Calendar ID
   */
  public Integer getCalendarId() {
    return getTask().getCalendarId();
  }

  /**
   * Retrieve file
   *
   * @return File
   */
  public File getFile() {
    return getTask().getFile();
  }

  /**
   * Set calendar to task
   *
   * @param calendar
   * @return
   */
  public TaskBuilder setCalendar(Calendar calendar) {
    getTask().setCalendar(calendar);
    return this;
  }

  /**
   * Set server to file
   *
   * @param server
   * @return
   */
  public TaskBuilder setFileServer(Server server) {
    getTask().getFile().setServer(server);
    return this;
  }

  /**
   * Generate scheduled trigger
   *
   * @return Trigger
   * @throws AWException
   */
  private void generateTrigger() throws AWException {
    // Create trigger object
    getTask().setTrigger(TriggerFactory.getInstance(TriggerType.TASK, defineJobData()));
    log.debug("[SCHEDULER][TASK_QUERY {}][TRIGGER] Trigger generated", getTask().getTrigger().getKey().toString());
  }

  /**
   * Creates a new Job and retrieves it. It also saves the Job into the task
   *
   * @return JobDetail
   * @throws AWException
   */
  private void generateJob() throws AWException {
    // Create and set the job to the Task
    task.setJob(JobFactory.getInstance(JobType.valueOf(task.getExecutionType()), defineJobData()));
    log.debug("[SCHEDULER][TASK_QUERY][JOB {}] Job generated", task.getJob() != null ? task.getJob().getKey().toString() : "");
  }

  /**
   * Define job datamap
   *
   * @return job datamap
   */
  private JobDataMap defineJobData() {
    // Define job data
    JobDataMap data = new JobDataMap();
    data.put("id", task.getTaskId().toString());
    data.put(TASK, task);

    return data;
  }
}
