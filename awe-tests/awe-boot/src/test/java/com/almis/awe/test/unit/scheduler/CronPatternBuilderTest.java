package com.almis.awe.test.unit.scheduler;

import com.almis.awe.exception.AWException;
import com.almis.awe.scheduler.bean.calendar.Schedule;
import com.almis.awe.scheduler.builder.cron.CronPatternBuilder;
import com.almis.awe.test.unit.TestUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.CronScheduleBuilder;
import org.quartz.Scheduler;

import javax.naming.NamingException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Log4j2
public class CronPatternBuilderTest extends TestUtil {

  @InjectMocks
  private CronPatternBuilder cronPatternBuilder;

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
    assertThat(cronPatternBuilder).isNotNull();
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  @Test(expected = AWException.class)
  public void buildPatternWithError() throws Exception {
    // Mock
    Schedule schedule = new Schedule();
    schedule.setRepeatType(11);
    cronPatternBuilder.setSchedule(schedule);

    // Call
    cronPatternBuilder.build();
  }

  /**
   * Week day pattern
   *
   * @throws NamingException Test error
   */
  @Test
  public void buildWeekDayPattern() throws Exception {
    // Mock
    Schedule schedule = new Schedule();
    schedule.setRepeatType(4);
    schedule.setRepeatNumber(2);
    schedule.setWeekDayList(Arrays.asList("1","2","3"));
    cronPatternBuilder.setSchedule(schedule);

    // Call
    CronScheduleBuilder scheduleBuilder = cronPatternBuilder.build();

    // Assert not null
    assertThat(scheduleBuilder).isNotNull();
  }

  /**
   * Week pattern
   *
   * @throws NamingException Test error
   */
  @Test
  public void buildWeekPattern() throws Exception {
    // Mock
    Schedule schedule = new Schedule();
    schedule.setRepeatType(4);
    schedule.setRepeatNumber(2);
    schedule.setWeekList(Arrays.asList("1","2","3"));
    cronPatternBuilder.setSchedule(schedule);

    // Call
    CronScheduleBuilder scheduleBuilder = cronPatternBuilder.build();

    // Assert not null
    assertThat(scheduleBuilder).isNotNull();
  }
}
