package com.almis.awe.test.unit.scheduler;

import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskParameter;
import com.almis.awe.scheduler.dao.CommandDAO;
import com.almis.awe.test.unit.TestUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.TriggerBuilder;

import javax.naming.NamingException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@TestMethodOrder(Alphanumeric.class)
@Log4j2
public class CommandDAOTest extends TestUtil {

  @InjectMocks
  private CommandDAO commandDAO;

  @Mock
  private Runtime runtime;

  private Process process;
  private InputStream errorStream;
  private InputStream outputStream;

  /**
   * Initializes json mapper for tests
   */
  @BeforeEach
  public void initBeans() throws Exception {
    MockitoAnnotations.initMocks(this);
    process = Mockito.mock(Process.class);
    errorStream = IOUtils.toInputStream("error stream data", "UTF-8");
    outputStream = IOUtils.toInputStream("output stream data", "UTF-8");

    given(runtime.exec(anyString(), any())).willReturn(process);
    given(process.getErrorStream()).willReturn(errorStream);
    given(process.getInputStream()).willReturn(outputStream);
  }

  /**
   * Test context loaded
   *
   * @throws NamingException Test error
   */
  @Test
  public void contextLoads() {
    // Check that controller are active
    assertThat(commandDAO).isNotNull();
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  @Test
  public void runExeCommand() throws Exception {
    // Mock
    Task task = generateTask();
    task.setAction("test.exe");

    // Run action
    commandDAO.runCommand(task, new String[0], 1000);

    // Check that controller are active
    verify(runtime, times(1)).exec(anyString(), any());
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  @Test
  public void runCmdCommand() throws Exception {
    // Mock
    Task task = generateTask();
    task.setAction("test.cmd");
    task.getParameterList().add(new TaskParameter().setValue("tutu"));

    // Run action
    commandDAO.runCommand(task, new String[]{}, 1000);

    // Check mock called
    verify(runtime, times(1)).exec(anyString(), any());
  }

  private Task generateTask() {
    Task task = new Task();
    task.setCommandPath("/test/command/");
    task.setParameterList(new ArrayList<>());
    task.setTrigger(TriggerBuilder.newTrigger().build());
    return task;
  }
}
