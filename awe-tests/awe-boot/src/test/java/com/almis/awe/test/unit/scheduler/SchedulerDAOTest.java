package com.almis.awe.test.unit.scheduler;

import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.scheduler.dao.SchedulerDAO;
import com.almis.awe.test.unit.TestUtil;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.*;
import org.springframework.context.ApplicationContext;

import javax.naming.NamingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.almis.awe.scheduler.constant.JobConstants.TASK_VISIBLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@TestMethodOrder(Alphanumeric.class)
@Log4j2
public class SchedulerDAOTest extends TestUtil {

  @InjectMocks
  private SchedulerDAO schedulerDAO;

  @Mock
  private QueryUtil queryUtil;

  @Mock
  private Scheduler scheduler;

  @Mock
  private ApplicationContext context;

  @Mock
  private AweElements aweElements;

  /**
   * Initializes json mapper for tests
   */
  @BeforeEach
  public void initBeans() throws Exception {
    MockitoAnnotations.initMocks(this);
    doReturn(aweElements).when(context).getBean(any(Class.class));
    given(aweElements.getLanguage()).willReturn("ES");
    given(aweElements.getLocaleWithLanguage(anyString(), anyString())).willReturn("LOCALE");
    given(queryUtil.getParameters()).willReturn(JsonNodeFactory.instance.objectNode());
    given(queryUtil.getParameters(any(), any(), any())).willReturn(JsonNodeFactory.instance.objectNode());
  }

  /**
   * Test context loaded
   *
   * @throws NamingException Test error
   */
  @Test
  public void contextLoads() {
    // Check that controller are active
    assertThat(schedulerDAO).isNotNull();
  }

  /**
   * Get executing jobs without scheduler configured
   */
  @Test
  public void getCurrentlyExecutingJobsNoScheduler() throws Exception {
    // Mock and spy
    JobExecutionContext jobExecutionContext = Mockito.mock(JobExecutionContext.class);
    List<JobExecutionContext> jobExecutionContextList = Arrays.asList(jobExecutionContext);
    given(scheduler.getCurrentlyExecutingJobs()).willReturn(jobExecutionContextList);

    // Run method
    ServiceData serviceData = schedulerDAO.getCurrentlyExecutingJobs();

    // Assert
    assertThat(serviceData).isNotNull();
  }

  /**
   * Get executing jobs without scheduler configured
   */
  @Test
  public void getCurrentlyExecutingJobsStandby() throws Exception {
    // Mock and spy
    JobExecutionContext jobExecutionContext = Mockito.mock(JobExecutionContext.class);
    List<JobExecutionContext> jobExecutionContextList = Arrays.asList(jobExecutionContext);
    given(scheduler.getCurrentlyExecutingJobs()).willReturn(jobExecutionContextList);
    given(scheduler.isInStandbyMode()).willReturn(true);
    given(scheduler.isStarted()).willReturn(false);

    // Run method
    ServiceData serviceData = schedulerDAO.getCurrentlyExecutingJobs();

    // Assert
    assertThat(serviceData).isNotNull();
  }

  /**
   * Get executing jobs without scheduler configured
   */
  @Test
  public void getCurrentlyExecutingJobsNoStarted() throws Exception {
    // Mock and spy
    JobExecutionContext jobExecutionContext = Mockito.mock(JobExecutionContext.class);
    List<JobExecutionContext> jobExecutionContextList = Arrays.asList(jobExecutionContext);
    given(scheduler.getCurrentlyExecutingJobs()).willReturn(jobExecutionContextList);
    given(scheduler.isInStandbyMode()).willReturn(false);
    given(scheduler.isStarted()).willReturn(false);

    // Run method
    ServiceData serviceData = schedulerDAO.getCurrentlyExecutingJobs();

    // Assert
    assertThat(serviceData).isNotNull();
  }

  /**
   * Get executing jobs with scheduler configured
   */
  @Test
  public void getCurrentlyExecutingJobsWithScheduler() throws Exception {
    // Mock and spy
    JobExecutionContext jobExecutionContext = Mockito.mock(JobExecutionContext.class);
    given(jobExecutionContext.getTrigger()).willReturn(TriggerBuilder.newTrigger().withIdentity("1", "TEST_GROUP").build());
    List<JobExecutionContext> jobExecutionContextList = Arrays.asList(jobExecutionContext);
    given(scheduler.getCurrentlyExecutingJobs()).willReturn(jobExecutionContextList);
    given(scheduler.isInStandbyMode()).willReturn(false);
    given(scheduler.isStarted()).willReturn(true);
    given(scheduler.getTriggerState(any())).willReturn(Trigger.TriggerState.NORMAL);

    // Run method
    ServiceData serviceData = schedulerDAO.getCurrentlyExecutingJobs();

    // Assert
    assertThat(serviceData).isNotNull();
  }

  /**
   * Get configured jobs with scheduler configured
   */
  @Test
  public void getConfiguredJobsWithScheduler() throws Exception {
    // Mock and spy
    JobExecutionContext jobExecutionContext = Mockito.mock(JobExecutionContext.class);
    given(jobExecutionContext.getTrigger()).willReturn(TriggerBuilder.newTrigger().withIdentity("1", "TEST_GROUP").build());
    List<JobExecutionContext> jobExecutionContextList = Arrays.asList(jobExecutionContext);
    given(scheduler.getCurrentlyExecutingJobs()).willReturn(jobExecutionContextList);
    given(scheduler.isInStandbyMode()).willReturn(false);
    given(scheduler.isStarted()).willReturn(true);
    given(scheduler.getTriggerState(any())).willReturn(Trigger.TriggerState.NORMAL);
    given(scheduler.getTriggerGroupNames()).willReturn(Arrays.asList("1", "2", "3", "TEST_GROUP"));
    Set<TriggerKey> triggerKeySet = new HashSet<>();
    triggerKeySet.add(new TriggerKey("1", "TEST_GROUP"));
    given(scheduler.getTriggerKeys(any())).willReturn(triggerKeySet);
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("1", "TEST_GROUP").build();
    trigger.getJobDataMap().put(TASK_VISIBLE, true);
    given(scheduler.getTrigger(any())).willReturn(trigger);

    // Run method
    ServiceData serviceData = schedulerDAO.getConfiguredJobs();

    // Assert
    assertThat(serviceData).isNotNull();
  }
}
