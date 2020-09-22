package com.almis.awe.scheduler.dao;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.scheduler.bean.calendar.Calendar;
import com.almis.awe.scheduler.bean.calendar.CalendarExcludedDate;
import com.almis.awe.scheduler.bean.calendar.Schedule;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.builder.cron.PatternBuilder;
import com.almis.awe.scheduler.util.TaskUtil;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.OperableTrigger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.almis.awe.scheduler.constant.CronConstants.*;
import static com.almis.awe.scheduler.constant.QueryConstants.*;
import static com.almis.awe.scheduler.constant.TaskConstants.YEAR_LABEL;
import static com.almis.awe.scheduler.constant.TaskConstants.YEAR_VALUE;

@Log4j2
public class CalendarDAO extends ServiceConfig {

  private static final String TITLE_SCHEDULER_DELETE_CALENDAR = "TITLE_SCHEDULER_DELETE_CALENDAR";
  private static final String MESSAGE_SCHEDULER_DELETE_CALENDAR = "MESSAGE_SCHEDULER_DELETE_CALENDAR";
  private static final String TITLE_SCHEDULER_DELETE_CALENDAR_WITH_ASSOCIATED_TASKS = "TITLE_SCHEDULER_DELETE_CALENDAR_WITH_ASSOCIATED_TASKS";
  private static final String MESSAGE_SCHEDULER_DELETE_CALENDAR_WITH_ASSOCIATED_TASKS = "MESSAGE_SCHEDULER_DELETE_CALENDAR_WITH_ASSOCIATED_TASKS";

  // Autowired services
  private final Scheduler scheduler;
  private final QueryService queryService;
  private final QueryUtil queryUtil;

  /**
   * Autowired constructor
   *
   * @param scheduler    Scheduler
   * @param queryService Query service
   * @param queryUtil    Query utilities
   */
  public CalendarDAO(Scheduler scheduler, QueryService queryService, QueryUtil queryUtil) {
    this.scheduler = scheduler;
    this.queryService = queryService;
    this.queryUtil = queryUtil;
  }

  /**
   * Load calendars from the database to the scheduler
   *
   * @throws AWException
   */
  public void loadSchedulerCalendar() throws AWException {
    DataList calendarIdeList = queryService.launchPrivateQuery(SCHEDULER_CALENDAR_LIST_QUERY).getDataList();
    log.debug("[CALENDARS] Starting calendar load from database");

    List<Calendar> calendarList = DataListUtil.asBeanList(calendarIdeList, Calendar.class);

    for (Calendar calendar : calendarList) {
      // load a calendar with the given ide
      insertSchedulerCalendar(null, calendar.getCalendarId(), true, false);
    }
  }

  /**
   * Get calendar for the given ID on the given database
   *
   * @param alias
   * @param calendarId
   * @return
   * @throws AWException
   */
  public Calendar getCalendar(String alias, Integer calendarId) throws AWException {
    // Set context parameters to execute queries
    ObjectNode parameters = queryUtil.getParameters(alias, "1", "0");
    parameters.put("calendarId", calendarId);

    // Execute queries
    DataList calendarDetails = queryService.launchPrivateQuery(SCHEDULER_TASK_CALENDAR_QUERY, parameters).getDataList();
    DataList calendarDays = queryService.launchPrivateQuery(SCHEDULER_TASK_CALENDAR_DATES_QUERY, parameters).getDataList();

    // Get calendar parameters
    List<Calendar> calendarList = DataListUtil.asBeanList(calendarDetails, Calendar.class);

    // Fill calendar parameters
    return calendarList
      .get(0)
      .addExcludedDateSet(DataListUtil.asBeanList(calendarDays, CalendarExcludedDate.class));
  }

  /**
   * Insert and schedule a new calendar
   *
   * @param calendarIde
   * @param replace
   * @param updateTriggers
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData insertSchedulerCalendar(Integer calendarIde, boolean replace, boolean updateTriggers) throws AWException {
    return insertSchedulerCalendar(null, calendarIde, replace, updateTriggers);
  }

  /**
   * Insert and schedule a new calendar
   *
   * @param calendarId
   * @param replace
   * @param updateTriggers
   * @param alias
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData insertSchedulerCalendar(String alias, Integer calendarId, boolean replace, boolean updateTriggers) throws AWException {
    try {
      Calendar calendar = getCalendar(alias, calendarId);

      // Add calendar to the scheduler if its active
      scheduler.addCalendar(calendarId.toString(), calendar, replace, updateTriggers);
      log.debug("[SCHEDULER][CALENDARS][CALENDAR {}] Calendar correctly " + (replace ? "updated" : "added") + " to the scheduler", calendar.getCalendarId());
    } catch (Exception exc) {
      throw new AWException("Error adding calendar", exc);
    }
    return new ServiceData();
  }

  /**
   * Update and schedule a new calendar
   *
   * @param calendarIde
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData updateSchedulerCalendar(Integer calendarIde) throws AWException {
    // Insert the scheduler calendar overwriting the old one.
    return insertSchedulerCalendar(calendarIde, true, true);
  }

  /**
   * Delete selected calendars
   *
   * @param calendarIdList
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData deleteSchedulerCalendar(List<Integer> calendarIdList) throws AWException {
    List<Task> taskList = retrieveTasksWithCalendars(calendarIdList);

    // Remove calendars from tasks
    for (Task task : taskList) {
      rescheduleTaskWithCalendar(task, null);
    }

    // Remove calendars from scheduler
    for (Integer calendarId : calendarIdList) {
      try {
        scheduler.deleteCalendar(calendarId.toString());
      } catch (SchedulerException exc) {
        throw new AWException("Error deleting the calendar " + calendarId, exc);
      }
    }

    return new ServiceData();
  }


  /**
   * Reschedule task with the new calendar id
   *
   * @param task
   * @param calendarId
   */
  public void rescheduleTaskWithCalendar(Task task, String calendarId) throws AWException {
    try {
      task.setGroup(TaskUtil.getGroupForLaunchType(task.getLaunchType()));
      TriggerKey triggerKey = new TriggerKey(task.getTaskId().toString(), task.getGroup());
      Trigger trigger = scheduler.getTrigger(triggerKey);
      TriggerBuilder triggerBuilder = trigger.getTriggerBuilder();

      // Create new trigger from old trigger builder
      Trigger newTrigger = triggerBuilder
        .modifiedByCalendar(calendarId)
        .build();

      // Reschedule Job with the new trigger and the old trigger key
      scheduler.rescheduleJob(triggerKey, newTrigger);
    } catch (SchedulerException exc) {
      throw new AWException("Error rescheduling calendar for task " + task.getTaskId(), exc);
    }
  }

  /**
   * Check if the scheduler contains the selected calendar
   *
   * @param calendarIdList
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData checkTriggersContainsCalendar(Integer... calendarIdList) throws AWException {
    ServiceData serviceData = new ServiceData();
    try {
      serviceData.setTitle(getLocale(TITLE_SCHEDULER_DELETE_CALENDAR));
      serviceData.setMessage(getLocale(MESSAGE_SCHEDULER_DELETE_CALENDAR));
      boolean triggerContainsCalendar = false;

      // Check if any trigger contains the selected calendar
      for (TriggerKey key : scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup())) {
        Trigger oldTrigger = scheduler.getTrigger(key);
        if (oldTrigger.getCalendarName() != null) {
          for (Integer calendarId : calendarIdList) {
            Calendar calendar = getCalendar(null, calendarId);
            if (calendar.getCalendarId().toString().equals(oldTrigger.getCalendarName())) {
              triggerContainsCalendar = true;
              break;
            }
          }
        }
      }

      // If any of the triggers contains a calendar, return a message telling
      // that the calendar will be removed from those triggers
      if (triggerContainsCalendar) {
        throw new AWException(getLocale(TITLE_SCHEDULER_DELETE_CALENDAR_WITH_ASSOCIATED_TASKS), getLocale(MESSAGE_SCHEDULER_DELETE_CALENDAR_WITH_ASSOCIATED_TASKS));
      }
    } catch (AWException exc) {
      throw exc;
    } catch (Exception exc) {
      log.debug("[CALENDAR {}] Error checking if trigger contains calendar", calendarIdList, exc);
    }
    return serviceData;
  }

  /**
   * Retrieves next 100 years
   *
   * @return ServiceData
   */
  public ServiceData yearSelectService() {
    ServiceData serviceData = new ServiceData();
    DataList dataList = new DataList();
    int maxNextYears = 100;
    Integer year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    // Fill add All option
    Map<String, CellData> row = new HashMap<>();
    row.put(YEAR_LABEL, new CellData("All"));
    row.put(YEAR_VALUE, new CellData("*"));
    dataList.getRows().add(row);

    for (int i = 0; i < maxNextYears; i++) {
      // Create new row
      Map<String, CellData> tempRow = new HashMap<>();
      // Fill rows of datalist
      tempRow.put(YEAR_LABEL, new CellData(String.valueOf(year)));
      tempRow.put(YEAR_VALUE, new CellData(String.valueOf(year)));
      // next year
      year++;
      // Add to row list
      dataList.getRows().add(tempRow);
    }
    dataList.setRecords(maxNextYears + 1L);
    serviceData.setDataList(dataList);
    return serviceData;
  }

  /**
   * Compute next fire times
   *
   * @param times
   * @return
   * @throws AWException
   */
  public ServiceData computeNextFiretimes(int times) throws AWException {
    ServiceData serviceData = new ServiceData();

    try {
      // Read schedule
      Schedule schedule = getSchedule();

      // Build trigger
      TriggerBuilder triggerBuilder = TriggerBuilder
        .newTrigger()
        .withIdentity("testTrigger", "testGroup");

      // Add initial date
      if (schedule.getInitialDateTime() != null) {
        triggerBuilder.startAt(schedule.getInitialDateTime());
      }

      // Add end date
      if (schedule.getEndDateTime() != null) {
        triggerBuilder.endAt(schedule.getEndDateTime());
      }

      // Add schedule
      triggerBuilder.withSchedule(buildSchedule(schedule));
      Trigger trigger = triggerBuilder.build();

      DataList nextExecutionDates = new DataList();

      List<Date> executionsDates = TriggerUtils.computeFireTimes((OperableTrigger) trigger, getQuartzCalendar(schedule.getCalendarId()), times);

      // Paint next execution dates
      for (Date date : executionsDates) {
        Map<String, CellData> row = new HashMap<>();
        row.put("nextDate", new CellData(DateUtil.dat2WebDate(date)));
        row.put("nextTime", new CellData(DateUtil.dat2WebTime(date)));
        nextExecutionDates.getRows().add(row);
      }
      nextExecutionDates.setRecords(nextExecutionDates.getRows().size());
      serviceData.setDataList(nextExecutionDates);
    } catch (Exception exc) {
      throw new AWException("Error computing next fire times", exc);
    }
    return serviceData;
  }

  /**
   * Retrieve all tasks with the calendar set and set the calendar to their triggers
   *
   * @param calendarIdList Calendar identifier list
   * @return Service data
   */
  public ServiceData activateCalendars(List<Integer> calendarIdList) throws AWException {
    List<Task> taskList = retrieveTasksWithCalendars(calendarIdList);
    for (Task task : taskList) {
      rescheduleTaskWithCalendar(task, task.getCalendarId().toString());
    }
    return new ServiceData();
  }

  /**
   * Retrieve all tasks with the calendar set and remove the calendar from their triggers
   *
   * @param calendarIdList Calendar identifier list
   * @return Service data
   */
  public ServiceData deactivateCalendars(List<Integer> calendarIdList) throws AWException {
    List<Task> taskList = retrieveTasksWithCalendars(calendarIdList);
    for (Task task : taskList) {
      rescheduleTaskWithCalendar(task, null);
    }
    return new ServiceData();
  }

  /**
   * Retrieve the task list which is using selected calendars
   *
   * @param calendarIdList Calendar list
   * @return Task list using calendars
   * @throws AWException
   */
  private List<Task> retrieveTasksWithCalendars(List<Integer> calendarIdList) throws AWException {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode parameters = queryUtil.getParameters();
    parameters.set("calendarId", mapper.valueToTree(calendarIdList));
    DataList dataList = queryService.launchPrivateQuery(SCHEDULER_GET_TASKS_WITH_CALENDARS, parameters).getDataList();
    return DataListUtil.asBeanList(dataList, Task.class);
  }

  /**
   * Retrieve calendar if it's active
   *
   * @param calendarId Calendar identifier
   * @return
   * @throws AWException
   */
  private org.quartz.Calendar getQuartzCalendar(Integer calendarId) throws AWException {
    org.quartz.Calendar quartzCalendar = null;

    if (calendarId != null) {
      try {
        Calendar calendar = getCalendar(null, calendarId);
        if (calendar.isActive()) {
          quartzCalendar = scheduler.getCalendar(calendarId.toString());
        }
      } catch (SchedulerException exc) {
        throw new AWException("Error retrieving calendar " + calendarId, exc);
      }
    }

    return quartzCalendar;
  }

  /**
   * Build schedule
   *
   * @return
   */
  private ScheduleBuilder buildSchedule(Schedule schedule) throws AWException {
    return new PatternBuilder(schedule).build();
  }

  /**
   * get parameters for generating cron from context
   *
   * @return
   * @throws AWException
   */
  private Schedule getSchedule() {
    return new Schedule()
      .setRepeatType(readJsonAsInteger(getRequest().getParameter(CRON_PARAMETER_REPEAT_TYPE)))
      .setRepeatNumber(readJsonAsInteger(getRequest().getParameter(CRON_PARAMETER_REPEAT_NUMBER)))
      .setCalendarId(readJsonAsInteger(getRequest().getParameter(CRON_PARAMETER_CALENDAR_IDE)))
      .setInitialDate(DateUtil.web2Date(getRequest().getParameterAsString(CRON_PARAMETER_INITIAL_DATE)))
      .setInitialTime(getRequest().getParameterAsString(CRON_PARAMETER_INITIAL_TIME))
      .setEndDate(DateUtil.web2Date(getRequest().getParameterAsString(CRON_PARAMETER_END_DATE)))
      .setEndTime(getRequest().getParameterAsString(CRON_PARAMETER_END_TIME))
      .setDate(DateUtil.web2Date(getRequest().getParameterAsString(CRON_PARAMETER_DATE)))
      .setTime(getRequest().getParameterAsString(CRON_PARAMETER_TIME))
      .setYearList(readJsonAsList(getRequest().getParameter(CRON_PARAMETER_YEARS)))
      .setMonthList(readJsonAsList(getRequest().getParameter(CRON_PARAMETER_MONTHS)))
      .setWeekList(readJsonAsList(getRequest().getParameter(CRON_PARAMETER_WEEKS)))
      .setDayList(readJsonAsList(getRequest().getParameter(CRON_PARAMETER_DAYS)))
      .setWeekDayList(readJsonAsList(getRequest().getParameter(CRON_PARAMETER_WEEKDAYS)))
      .setHourList(readJsonAsList(getRequest().getParameter(CRON_PARAMETER_HOURS)))
      .setMinuteList(readJsonAsList(getRequest().getParameter(CRON_PARAMETER_MINUTES)))
      .setSecondList(readJsonAsList(getRequest().getParameter(CRON_PARAMETER_SECONDS)));
  }

  /**
   * Read json node as integer
   *
   * @param jsonNode Json node
   * @return Integer value
   */
  private Integer readJsonAsInteger(JsonNode jsonNode) {
    return jsonNode.isNull() ? null : Integer.parseInt(jsonNode.asText());
  }

  /**
   * Read json node as integer list
   *
   * @param jsonNode Node
   * @return Integer list
   */
  private List<String> readJsonAsList(JsonNode jsonNode) {
    ObjectMapper mapper = new ObjectMapper();
    return jsonNode.isNull() ? null : mapper.convertValue(jsonNode, new TypeReference<List<String>>() {
    });
  }
}
