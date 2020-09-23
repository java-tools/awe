package com.almis.awe.test.unit.scheduler;

import com.almis.awe.exception.AWException;
import com.almis.awe.scheduler.bean.calendar.Schedule;
import com.almis.awe.scheduler.builder.cron.CronPatternBuilder;
import com.almis.awe.test.unit.TestUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.CronScheduleBuilder;

import javax.naming.NamingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@TestMethodOrder(Alphanumeric.class)
@Log4j2
public class CronPatternBuilderTest extends TestUtil {

  @InjectMocks
  private CronPatternBuilder cronPatternBuilder;

  /**
   * Initializes json mapper for tests
   */
  @BeforeEach
  public void initBeans() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Test context loaded
   */
  @Test
  public void contextLoads() {
    // Check that controller are active
    assertThat(cronPatternBuilder).isNotNull();
  }

  /**
   * Check triggers contains calendars without calendar list
   */
  @Test
  public void buildPatternWithError() {
    // Mock
    Schedule schedule = new Schedule();
    schedule.setRepeatType(11);
    cronPatternBuilder.setSchedule(schedule);

    Assertions.assertThrows(AWException.class, () -> cronPatternBuilder.build());
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
    schedule.setWeekDayList(Arrays.asList("1", "2", "3"));
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
    schedule.setWeekList(Arrays.asList("1", "2", "3"));
    cronPatternBuilder.setSchedule(schedule);

    // Call
    CronScheduleBuilder scheduleBuilder = cronPatternBuilder.build();

    // Assert not null
    assertThat(scheduleBuilder).isNotNull();
  }

  /**
   * Day pattern
   *
   * @throws NamingException Test error
   */
  @Test
  public void buildDayPattern() throws Exception {
    // Mock
    Schedule schedule = new Schedule();
    schedule.setRepeatType(3);
    schedule.setRepeatNumber(1);
    schedule.setHourList(Arrays.asList("9", "15"));
    schedule.setWeekDayList(Collections.singletonList(""));
    schedule.setWeekList(Collections.singletonList(""));
    cronPatternBuilder.setSchedule(schedule);

    // Call
    CronScheduleBuilder scheduleBuilder = cronPatternBuilder.build();

    // Assert not null
    assertThat(scheduleBuilder.build()).isNotNull();
  }

  /**
   * Day pattern
   *
   * @throws NamingException Test error
   */
  @Test
  public void buildDayPatternEmptyWeek() throws Exception {
    // Mock
    Schedule schedule = new Schedule();
    schedule.setRepeatType(3);
    schedule.setRepeatNumber(1);
    schedule.setHourList(Arrays.asList("9", "15"));
    schedule.setWeekDayList(Collections.emptyList());
    schedule.setWeekList(Collections.emptyList());
    cronPatternBuilder.setSchedule(schedule);

    // Call
    CronScheduleBuilder scheduleBuilder = cronPatternBuilder.build();

    // Assert not null
    assertThat(scheduleBuilder.build()).isNotNull();
  }

  /**
   * Day pattern
   *
   * @throws NamingException Test error
   */
  @Test
  public void buildYearPattern() throws Exception {
    // Mock
    Schedule schedule = new Schedule();
    schedule.setRepeatType(5);
    schedule.setRepeatNumber(1);
    schedule.setDate(new Date());
    schedule.setTime("00:00:01");
    schedule.setHourList(Arrays.asList("9", "15"));
    schedule.setWeekDayList(Collections.emptyList());
    schedule.setWeekList(Collections.emptyList());
    cronPatternBuilder.setSchedule(schedule);

    // Call
    CronScheduleBuilder scheduleBuilder = cronPatternBuilder.build();

    // Assert not null
    assertThat(scheduleBuilder.build()).isNotNull();
  }
}
