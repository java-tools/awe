package com.almis.awe.test.unit.scheduler;

import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskDependency;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import com.almis.awe.scheduler.dao.TaskDAO;
import com.almis.awe.scheduler.enums.TaskLaunchType;
import com.almis.awe.scheduler.enums.TaskStatus;
import com.almis.awe.scheduler.filechecker.FileChecker;
import com.almis.awe.service.MaintainService;
import com.almis.awe.service.QueryService;
import com.almis.awe.test.unit.TestUtil;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.*;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.almis.awe.scheduler.constant.TaskConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Log4j2
@RunWith(MockitoJUnitRunner.class)
public class TaskDAOTest extends TestUtil {

  @InjectMocks
  private TaskDAO taskDAO;

  @Mock
  private MaintainService maintainService;

  @Mock
  private QueryService queryService;

  @Mock
  private QueryUtil queryUtil;

  @Mock
  private Scheduler scheduler;

  @Mock
  private ApplicationContext context;

  @Mock
  private AweElements aweElements;

  @Mock
  private FileChecker fileChecker;

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void initBeans() throws Exception {
    taskDAO.setApplicationContext(context);
    doReturn(aweElements).when(context).getBean(any(Class.class));
    given(aweElements.getLanguage()).willReturn("ES");
    given(aweElements.getLocaleWithLanguage(anyString(), anyString())).willReturn("LOCALE");
    given(queryUtil.getParameters()).willReturn(JsonNodeFactory.instance.objectNode());
    given(queryUtil.getParameters((String) isNull())).willReturn(JsonNodeFactory.instance.objectNode());
    given(queryUtil.getParameters(any(), any(), any())).willReturn(JsonNodeFactory.instance.objectNode());
  }

  /**
   * Test context loaded
   */
  @Test
  public void contextLoads() {
    // Check that controller are active
    assertThat(taskDAO).isNotNull();
  }

  /**
   * Change task test
   */
  @Test
  public void changeTask() throws Exception {
    // Mock and spy
    TaskExecution execution = new TaskExecution();
    execution.setStatus(TaskStatus.JOB_OK.getValue());
    execution.setDescription("Allright");

    // Run method
    taskDAO.changeStatus(new Task(), execution, TaskStatus.JOB_WARNING, "Because reasons");

    // Assert
    assertSame(TaskStatus.JOB_WARNING.getValue(), execution.getStatus());
    assertSame("Because reasons", execution.getDescription());
    verify(maintainService, times(1)).launchPrivateMaintain(anyString(), any(ObjectNode.class));
  }

  /**
   * Pause task test
   */
  @Test
  public void pauseTask() throws Exception {
    // Mock and spy
    Task task = new Task();
    task.setTaskId(1);
    task.setGroup("TASK_GROUP");
    given(scheduler.checkExists(any(TriggerKey.class))).willReturn(true);

    // Run method
    taskDAO.pauseTask(task);

    // Assert
    verify(scheduler, times(1)).pauseTrigger(any(TriggerKey.class));
  }

  /**
   * Pause task without trigger checked
   */
  @Test
  public void pauseTaskNoTrigger() throws Exception {
    // Mock and spy
    Task task = new Task();
    task.setTaskId(1);
    task.setGroup("TASK_GROUP");
    given(scheduler.checkExists(any(TriggerKey.class))).willReturn(false);

    // Run method
    taskDAO.pauseTask(task);

    // Assert
    verify(scheduler, times(0)).pauseTrigger(any(TriggerKey.class));
  }

  /**
   * Resume task test
   */
  @Test
  public void resumeTask() throws Exception {
    // Mock and spy
    Task task = new Task();
    task.setLaunchType(1);
    task.setTaskId(1);
    task.setGroup("TASK_GROUP");
    task.setJob(null);
    task.setTrigger(TriggerBuilder.newTrigger().build());
    given(scheduler.checkExists(any(TriggerKey.class))).willReturn(true);
    given(scheduler.getTrigger(any(TriggerKey.class))).willReturn(TriggerBuilder.newTrigger().build());

    // Run method
    taskDAO.resumeTask(task);

    // Assert
    verify(scheduler, times(1)).rescheduleJob(any(TriggerKey.class), any(Trigger.class));
  }

  /**
   * Resume new task test
   */
  @Test
  public void resumeNewTask() throws Exception {
    // Mock and spy
    Task task = new Task();
    task.setLaunchType(1);
    task.setTaskId(1);
    task.setGroup("TASK_GROUP");
    task.setJob(null);
    task.setTrigger(TriggerBuilder.newTrigger().build());
    given(scheduler.checkExists(any(JobKey.class))).willReturn(false);

    // Run method
    taskDAO.resumeTask(task);

    // Assert
    verify(scheduler, times(1)).scheduleJob(eq(null), any(Trigger.class));
  }

  /**
   * Resume manual task test
   */
  @Test
  public void resumeManualTask() throws Exception {
    // Mock and spy
    Task task = new Task();
    task.setLaunchType(0);
    task.setTaskId(1);
    task.setGroup("TASK_GROUP");
    task.setJob(null);
    task.setTrigger(TriggerBuilder.newTrigger().build());
    given(scheduler.checkExists(any(JobKey.class))).willReturn(false);

    // Run method
    taskDAO.resumeTask(task);

    // Assert
    verify(scheduler, times(0)).scheduleJob(eq(null), any(Trigger.class));
  }

  /**
   * Get task execution from trigger
   */
  @Test
  public void getTaskExecutionFromTrigger() {
    // Mock and spy
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("1-121", "TASK_GROUP").build();

    // Run method
    TaskExecution execution = taskDAO.getTaskExecution(trigger);

    // Assert
    assertSame(1, execution.getTaskId());
    assertSame(121, execution.getExecutionId());
    assertSame("TASK_GROUP", execution.getGroupId());
  }

  /**
   * Check task finish ok
   */
  @Test
  public void checkTaskFinishOk() throws Exception {
    Task task = mockTask();
    TaskExecution parentExecution = new TaskExecution().setTaskId(2).setExecutionId(11).setStatus(TaskStatus.JOB_OK.getValue());
    TaskExecution execution = new TaskExecution().setTaskId(1).setExecutionId(12).setStatus(TaskStatus.JOB_OK.getValue()).setParentExecution(parentExecution);

    // Finish task
    taskDAO.onFinishTask(task, execution);

    // Assert
    assertSame(TaskStatus.JOB_OK.getValue(), parentExecution.getStatus());
  }

  /**
   * Check task finish error
   */
  @Test
  public void checkTaskFinishError() throws Exception {
    Task task = mockTask();
    TaskExecution parentExecution = new TaskExecution().setTaskId(2).setExecutionId(11).setStatus(TaskStatus.JOB_OK.getValue());
    TaskExecution execution = new TaskExecution().setTaskId(1).setExecutionId(12).setStatus(TaskStatus.JOB_ERROR.getValue()).setParentExecution(parentExecution);

    // Finish task
    taskDAO.onFinishTask(task, execution);

    // Assert
    assertSame(TaskStatus.JOB_WARNING.getValue(), parentExecution.getStatus());
  }

  /**
   * Check task finish error
   */
  @Test
  public void checkTaskFinishErrorParentOk() throws Exception {
    Task task = mockTask(false);
    TaskExecution parentExecution = new TaskExecution().setTaskId(2).setExecutionId(11).setStatus(TaskStatus.JOB_OK.getValue());
    TaskExecution execution = new TaskExecution().setTaskId(1).setExecutionId(12).setStatus(TaskStatus.JOB_ERROR.getValue()).setParentExecution(parentExecution);

    // Finish task
    taskDAO.onFinishTask(task, execution);

    // Assert
    assertSame(TaskStatus.JOB_OK.getValue(), parentExecution.getStatus());
  }

  /**
   * Check task finish error
   */
  @Test
  public void checkTaskFinishWarning() throws Exception {
    Task task = mockTask();
    TaskExecution parentExecution = new TaskExecution().setTaskId(2).setExecutionId(11).setStatus(TaskStatus.JOB_OK.getValue());
    TaskExecution execution = new TaskExecution().setTaskId(1).setExecutionId(12).setStatus(TaskStatus.JOB_WARNING.getValue()).setParentExecution(parentExecution);

    // Finish task
    taskDAO.onFinishTask(task, execution);

    // Assert
    assertSame(TaskStatus.JOB_OK.getValue(), parentExecution.getStatus());
  }

  /**
   * Check task finish error
   */
  @Test
  public void checkTaskFinishInterrupted() throws Exception {
    Task task = mockTask();
    TaskExecution parentExecution = new TaskExecution().setTaskId(2).setExecutionId(11).setStatus(TaskStatus.JOB_OK.getValue());
    TaskExecution execution = new TaskExecution().setTaskId(1).setExecutionId(12).setStatus(TaskStatus.JOB_INTERRUPTED.getValue()).setParentExecution(parentExecution);

    // Finish task
    taskDAO.onFinishTask(task, execution);

    // Assert
    assertSame(TaskStatus.JOB_OK.getValue(), parentExecution.getStatus());
  }

  /**
   * Check if task execution is allowed
   */
  @Test
  public void isTaskExecutionAllowed() throws Exception {
    Task task = mockTask();
    task.setLaunchType(TaskLaunchType.FILE_TRACKING.getValue());
    given(fileChecker.checkFile(eq(task))).willReturn("File");

    // Finish task
    boolean allowed = taskDAO.isTaskExecutionAllowed(task);

    // Assert
    assertTrue(allowed);
  }

  /**
   * Test new task without execution
   */
  @Test
  public void startTask() throws Exception {
    given(queryService.launchPrivateQuery(anyString(), any(ObjectNode.class))).willReturn(new ServiceData().setDataList(new DataList()));
    Task task = new Task();
    task.setTrigger(TriggerBuilder.newTrigger().withIdentity("1", "TEST_GROUP").build());
    task.setParentExecution(new TaskExecution());

    // Finish task
    TaskExecution execution = taskDAO.startTask(task);

    // Assert
    assertNull(execution);
  }

  /**
   * Load execution screen
   */
  @Test
  public void loadExecutionScreen() throws Exception {
    ObjectNode address = JsonNodeFactory.instance.objectNode();
    address.put("row", "1" + TASK_SEPARATOR + "1");
    DataList dataList = new DataList();
    Map<String, CellData> row = new HashMap<>();
    row.put(TASK_IDE, new CellData(1));
    row.put("executionId", new CellData(1));
    row.put("initialDate", new CellData(new Date()));
    row.put("status", new CellData(TaskStatus.JOB_INTERRUPTED.getValue()));
    dataList.addRow(row);
    given(queryService.launchPrivateQuery(anyString(), any(ObjectNode.class))).willReturn(new ServiceData().setDataList(dataList));
    given(queryService.findLabel(anyString(), anyString())).willReturn("Label");


    // Finish task
    ServiceData serviceData = taskDAO.loadExecutionScreen("lala", address);

    // Assert
    assertEquals(16, serviceData.getClientActionList().size());
  }

  /**
   * Reload execution screen
   */
  @Test
  public void reloadExecutionScreen() throws Exception {
    ObjectNode address = JsonNodeFactory.instance.objectNode();
    address.put("row", "1" + TASK_SEPARATOR + "1");
    DataList dataList = new DataList();
    Map<String, CellData> row = new HashMap<>();
    row.put(TASK_IDE, new CellData(1));
    row.put("executionId", new CellData(1));
    row.put("initialDate", new CellData(new Date()));
    row.put("endDate", new CellData(new Date()));
    row.put("status", new CellData(TaskStatus.JOB_WARNING.getValue()));
    row.put("executionTime", new CellData(1231));
    dataList.addRow(row);
    given(queryService.launchPrivateQuery(anyString(), any(ObjectNode.class))).willReturn(new ServiceData().setDataList(dataList));
    given(queryService.findLabel(anyString(), anyString())).willReturn("Label");

    // Finish task
    ServiceData serviceData = taskDAO.reloadExecutionScreen(1, 1);

    // Assert
    assertEquals(13, serviceData.getClientActionList().size());
  }

  /**
   * Mock task
   *
   * @return Task task
   */
  private Task mockTask() throws Exception {
    return mockTask(true);
  }

  /**
   * Mock a task
   *
   * @return Task mocked
   * @throws Exception exception
   */
  private Task mockTask(boolean setTaskOnWarning) throws Exception {
    // Mock
    DataList taskDataList = new DataList();
    Map<String, CellData> row = new HashMap<>();
    row.put(TASK_LAUNCH_TYPE, new CellData(1));
    row.put(TASK_IDE, new CellData(2));
    row.put("repeatType", new CellData(2));
    row.put("repeatNumber", new CellData(2));
    row.put("executionType", new CellData(1));
    row.put("parentId", new CellData(1));
    row.put("launchDependenciesOnError", new CellData(true));
    row.put("launchDependenciesOnWarning", new CellData(true));
    row.put("setTaskOnWarningIfDependencyError", new CellData(setTaskOnWarning));
    row.put(FILE_PATH, new CellData("file/path"));
    row.put(UPDATE_DATE, new CellData(new Date()));
    taskDataList.addRow(row);
    given(queryService.launchPrivateQuery(anyString(), any(ObjectNode.class))).willReturn(new ServiceData().setDataList(taskDataList));

    return new Task().setTaskId(1).setDependencyList(Collections.singletonList(new TaskDependency().setTaskId(1).setParentId(2)));
  }
}
