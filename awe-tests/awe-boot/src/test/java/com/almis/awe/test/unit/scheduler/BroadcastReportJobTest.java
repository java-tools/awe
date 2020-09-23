package com.almis.awe.test.unit.scheduler;

import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.scheduler.bean.report.Report;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import com.almis.awe.scheduler.enums.TaskStatus;
import com.almis.awe.scheduler.job.report.BroadcastReportJob;
import com.almis.awe.service.BroadcastService;
import com.almis.awe.test.unit.TestUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

import javax.naming.NamingException;
import java.util.ArrayList;

import static com.almis.awe.scheduler.constant.JobConstants.TASK;
import static com.almis.awe.scheduler.constant.JobConstants.TASK_JOB_EXECUTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@TestMethodOrder(Alphanumeric.class)
@Log4j2
public class BroadcastReportJobTest extends TestUtil {

  @InjectMocks
  private BroadcastReportJob broadcastReportJob;

  @Mock
  private BroadcastService broadcastService;

  /**
   * Initializes json mapper for tests
   */
  @BeforeEach
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
    assertThat(broadcastReportJob).isNotNull();
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  @Test
  public void executeOk() throws Exception {
    executeBroadcastJob(TaskStatus.JOB_OK);
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  @Test
  public void executeError() throws Exception {
    executeBroadcastJob(TaskStatus.JOB_ERROR);
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  @Test
  public void executeInfo() throws Exception {
    executeBroadcastJob(TaskStatus.JOB_RUNNING);
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  @Test
  public void executeWarning() throws Exception {
    executeBroadcastJob(TaskStatus.JOB_INTERRUPTED);
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  public void executeBroadcastJob(TaskStatus status) throws Exception {
    JobExecutionContext executionContext = Mockito.mock(JobExecutionContext.class);
    JobDetail jobDetail = Mockito.mock(JobDetail.class);
    JobDataMap dataMap = new JobDataMap();
    dataMap.put(TASK, new Task().setReport(new Report().setReportUserDestination(new ArrayList<>())));
    dataMap.put(TASK_JOB_EXECUTION, new TaskExecution().setStatus(status.getValue()));
    given(executionContext.getJobDetail()).willReturn(jobDetail);
    given(jobDetail.getJobDataMap()).willReturn(dataMap);
    broadcastReportJob.execute(executionContext);
    verify(broadcastService, Mockito.times(1)).broadcastMessageToUsers(any(ClientAction.class), any());
  }
}
