package com.almis.awe.test.unit.scheduler;

import com.almis.awe.scheduler.bean.task.TaskExecution;
import com.almis.awe.scheduler.enums.TaskStatus;
import com.almis.awe.scheduler.service.ExecutionService;
import com.almis.awe.test.unit.TestUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.Scheduler;

import javax.naming.NamingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Log4j2
public class ExecutionServiceTest extends TestUtil {

  @InjectMocks
  private ExecutionService executionService;

  @Mock
  private Scheduler scheduler;

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void initBeans() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Test context loaded
   *
   * @throws NamingException Test error
   */
  @Test
  public void contextLoads() {
    // Check that controller are active
    assertThat(executionService).isNotNull();
  }

  /**
   * Start progress job 1 second
   */
  @Test
  public void startProgressJobOneSecond() throws Exception {
    // Mock and spy
    TaskExecution execution = new TaskExecution();
    execution.setStatus(TaskStatus.JOB_OK.getValue());
    execution.setDescription("Allright");

    // Run method
    executionService.startProgressJob(execution, 0);

    // Assert
    verify(scheduler, times(1)).scheduleJob(any(), any());
  }

  /**
   * Start progress job n seconds
   */
  @Test
  public void startProgressJobAverageTime() throws Exception {
    // Mock and spy
    TaskExecution execution = new TaskExecution();
    execution.setStatus(TaskStatus.JOB_OK.getValue());
    execution.setDescription("Allright");

    // Run method
    executionService.startProgressJob(execution, 12311);

    // Assert
    verify(scheduler, times(1)).scheduleJob(any(), any());
  }
}
