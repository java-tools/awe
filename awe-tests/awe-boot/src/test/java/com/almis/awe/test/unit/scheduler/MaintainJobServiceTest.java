package com.almis.awe.test.unit.scheduler;

import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.scheduler.bean.task.Task;
import com.almis.awe.scheduler.bean.task.TaskExecution;
import com.almis.awe.scheduler.bean.task.TaskParameter;
import com.almis.awe.scheduler.service.MaintainJobService;
import com.almis.awe.service.MaintainService;
import com.almis.awe.test.unit.TestUtil;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobDataMap;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.context.ApplicationContext;

import javax.naming.NamingException;
import java.util.Arrays;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
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
public class MaintainJobServiceTest extends TestUtil {

  @Mock
  QueryUtil queryUtil;
  @Mock
  AweElements aweElements;
  @Mock
  ApplicationContext context;
  @Mock
  MaintainService maintainService;
  @InjectMocks
  private MaintainJobService maintainJobService;

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void initBeans() throws Exception {
    maintainJobService.setApplicationContext(context);
    doReturn(aweElements).when(context).getBean(any(Class.class));
    given(aweElements.getProperty(anyString())).willReturn("ES");
  }

  /**
   * Test context loaded
   *
   * @throws NamingException Test error
   */
  @Test
  public void contextLoads() {
    // Check that controller are active
    assertThat(maintainJobService).isNotNull();
  }

  @Test
  public void testExecuteJob() throws Exception {
    Trigger trigger = mock(Trigger.class);
    when(queryUtil.getParameters(isNull(), any(), any())).thenReturn(JsonNodeFactory.instance.objectNode());
    when(trigger.getKey()).thenReturn(new TriggerKey("lalala"));
    Future<ServiceData> serviceData = maintainJobService.executeJob(new Task()
        .setTrigger(trigger)
        .setParameterList(Arrays.asList(
          new TaskParameter().setSource("1").setName("1").setValue("1").setType("STRING"),
          new TaskParameter().setSource("2").setName("2").setValue("2").setType("INTEGER")
        )),
      new TaskExecution(), new JobDataMap());

    assertThat(serviceData).isNotNull();
  }
}
