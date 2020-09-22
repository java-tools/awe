package com.almis.awe.scheduler.listener;

import com.almis.awe.builder.client.FilterActionBuilder;
import com.almis.awe.builder.client.SelectActionBuilder;
import com.almis.awe.builder.client.grid.UpdateCellActionBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.tracker.AweConnectionTracker;
import com.almis.awe.model.util.data.TimeUtil;
import com.almis.awe.scheduler.bean.event.SchedulerTaskFinishedEvent;
import com.almis.awe.scheduler.bean.event.SchedulerTaskProgressEvent;
import com.almis.awe.scheduler.bean.event.SchedulerTaskStartedEvent;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import com.almis.awe.scheduler.dao.TaskDAO;
import com.almis.awe.service.BroadcastService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.almis.awe.scheduler.constant.TaskConstants.*;

@Log4j2
public class SchedulerEventListener {

  private final BroadcastService broadcastService;
  private final AweConnectionTracker connectionTracker;
  private final TaskDAO taskDAO;
  private static final String TASK_SCREEN = "scheduler-tasks";

  /**
   * Autowired constructor
   *
   * @param broadcastService  Broadcast service
   * @param connectionTracker Connection tracker
   * @param taskDAO           Task DAO
   */
  public SchedulerEventListener(BroadcastService broadcastService, AweConnectionTracker connectionTracker, TaskDAO taskDAO) {
    this.broadcastService = broadcastService;
    this.connectionTracker = connectionTracker;
    this.taskDAO = taskDAO;
  }

  /**
   * On trigger launched event
   *
   * @param event
   */
  @EventListener
  public void onTaskStarted(SchedulerTaskStartedEvent event) {
    ClientAction clearStartingTask = new SelectActionBuilder(getAddress("starting-task"), "").setSilent(true).build();
    ClientAction setStartingTask = new SelectActionBuilder(getAddress("starting-task"), event.getTask().getTaskId()).setSilent(true).build();

    // Filter connections from users which are in TASK SCREEN and broadcast them the filter grid action
    getConnectedUsersToTaskScreen().forEach(cometID -> broadcastService.broadcastMessageToUID(cometID, clearStartingTask, setStartingTask));
  }

  /**
   * On job progress event
   *
   * @param event
   */
  @EventListener
  public void onTaskProgress(SchedulerTaskProgressEvent event) {
    // Set progress on progress bar
    ClientAction progressBar = new UpdateCellActionBuilder(getAddress(event.getTaskExecution(), TASK_EXECUTION_PROGRESS), taskDAO.getProgressNode(taskDAO.getProgress(event.getTaskExecution().getInitialDate(), event.getAverageTime())))
      .setSilent(true)
      .setAsync(true)
      .build();

    // Set execution time
    ClientAction executionTime = new UpdateCellActionBuilder(getAddress(event.getTaskExecution(), TASK_EXECUTION_TIME), new CellData(TimeUtil.formatTime(taskDAO.getElapsedTime(event.getTaskExecution().getInitialDate()), false)))
      .setSilent(true)
      .setAsync(true)
      .build();

    // Filter connections from users which are in TASK SCREEN and broadcast them the filter grid action
    getConnectedUsersToTaskScreen().forEach(cometID -> broadcastService.broadcastMessageToUID(cometID, progressBar, executionTime));
  }

  /**
   * On job progress event
   *
   * @param event
   */
  @EventListener
  public void onTaskFinished(SchedulerTaskFinishedEvent event) throws AWException {
    // Set task status icon
    ClientAction updateIcon = new UpdateCellActionBuilder(getAddress(event.getTaskExecution(), TASK_EXECUTION_ICON), taskDAO.getStatusIcon(event.getTaskExecution()))
      .setSilent(true)
      .setAsync(true)
      .build();

    // Set progress on progress bar
    ClientAction progressBar = new UpdateCellActionBuilder(getAddress(event.getTaskExecution(), TASK_EXECUTION_PROGRESS), taskDAO.getProgressNode(0))
      .setSilent(true)
      .setAsync(true)
      .build();

    // Set final execution time
    ClientAction executionTime = new UpdateCellActionBuilder(getAddress(event.getTaskExecution(), TASK_EXECUTION_TIME), new CellData(TimeUtil.formatTime(event.getTaskExecution().getExecutionTime())))
      .setSilent(true)
      .setAsync(true)
      .build();

    // Set final execution time
    ClientAction executionLog = new FilterActionBuilder(getAddress("reload-execution-data"))
      .setSilent(true)
      .setAsync(true)
      .build();

    // Filter connections from users which are in TASK SCREEN and broadcast them the filter grid action
    getConnectedUsersToTaskScreen().forEach(cometID -> broadcastService.broadcastMessageToUID(cometID, updateIcon, progressBar, executionTime, executionLog));

    // Launch end task dependencies
    taskDAO.onFinishTask(event.getTask(), event.getTaskExecution());
  }

  /**
   * Retrieve execution list grid address
   *
   * @return
   */
  private ComponentAddress getAddress() {
    return getAddress("GrdExeLst");
  }

  /**
   * Retrieve execution list grid address
   *
   * @return
   */
  private ComponentAddress getAddress(String component) {
    return new ComponentAddress().setView("report").setComponent(component);
  }

  /**
   * Retrieve execution list grid cell address
   *
   * @param execution
   * @param column
   * @return
   */
  private ComponentAddress getAddress(TaskExecution execution, String column) {
    return getAddress().setColumn(column).setRow(execution.getTaskId() + TASK_SEPARATOR + execution.getExecutionId());
  }

  /**
   * Retrieve connected user sessions
   *
   * @return Set of connections
   */
  private Set<String> getConnectedUsersToTaskScreen() {
    return connectionTracker
      .getAllConnections().entrySet().stream()
      .flatMap(u -> u.getValue().entrySet().stream().filter(c -> TASK_SCREEN.equalsIgnoreCase(c.getValue().getScreen())).map(Map.Entry::getKey))
      .collect(Collectors.toSet());
  }
}
