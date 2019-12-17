package com.almis.awe.test.unit.scheduler;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.scheduler.dao.CalendarDAO;
import com.almis.awe.service.QueryService;
import com.almis.awe.test.unit.TestUtil;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.context.ApplicationContext;

import javax.naming.NamingException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Log4j2
public class CalendarDAOTest extends TestUtil {

  @InjectMocks
  private CalendarDAO calendarDAO;

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

  /**
   * Initializes json mapper for tests
   */
  @Before
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
    assertThat(calendarDAO).isNotNull();
  }


  /**
   * Check triggers contains calendars
   *
   * @throws NamingException Test error
   */
  @Test(expected = AWException.class)
  public void checkTriggersContainsCalendars() throws Exception {
    // Mock
    prepareCalendarForTests(1);

    // Check that controller are active
    calendarDAO.checkTriggersContainsCalendar(1, 2, 3);
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  @Test
  public void checkTriggersContainsCalendarsNoCalendar() throws Exception {
    // Mock
    prepareCalendarForTests(null);

    // Check that controller are active
    assertEquals(new ServiceData().setTitle("LOCALE").setMessage("LOCALE"), calendarDAO.checkTriggersContainsCalendar());
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  @Test
  public void checkTriggersContainsCalendarsOtherCalendar() throws Exception {
    // Mock
    prepareCalendarForTests(8);

    // Check that controller are active
    assertEquals(new ServiceData().setTitle("LOCALE").setMessage("LOCALE"), calendarDAO.checkTriggersContainsCalendar());
  }

  /**
   * Check triggers contains calendars without calendar list
   *
   * @throws NamingException Test error
   */
  @Test
  public void checkTriggersContainsCalendarsEmpty() throws Exception {
    // Mock
    prepareCalendarForTests(1);

    // Check that controller are active
    assertEquals(new ServiceData().setTitle("LOCALE").setMessage("LOCALE"), calendarDAO.checkTriggersContainsCalendar());
  }

  /**
   * Delete scheduler calendar
   *
   * @throws NamingException Test error
   */
  @Test
  public void deleteSchedulerCalendar() throws Exception {
    // Mock
    prepareCalendarForTests(1);

    // Delete calendars
    calendarDAO.deleteSchedulerCalendar(Arrays.asList(1,2,3));

    // Check that controller are active
    assertEquals(new ServiceData().setTitle("LOCALE").setMessage("LOCALE"), calendarDAO.checkTriggersContainsCalendar());
  }

  /**
   * Prepare calendar mocks
   *
   * @throws Exception
   */
  private void prepareCalendarForTests(Integer calendarId) throws Exception {
    // Mock
    DataList dataList = new DataList();
    Map<String, CellData> row = new HashMap<>();
    row.put("calendarId", new CellData(calendarId));
    row.put("name", new CellData("Calendario guachi"));
    row.put("active", new CellData(true));
    row.put("id", new CellData(1));
    row.put("date", new CellData(new Date()));
    row.put("description", new CellData("Fecha guachi"));
    row.put("taskId", new CellData(1));
    row.put("launchType", new CellData(1));
    dataList.addRow(row);

    Set<TriggerKey> triggerSet = new HashSet<>();
    triggerSet.add(new TriggerKey("1", "LALA"));
    triggerSet.add(new TriggerKey("2", "LALA"));
    given(scheduler.getTriggerKeys(any())).willReturn(triggerSet);
    given(scheduler.getTrigger(any())).willReturn(TriggerBuilder.newTrigger().modifiedByCalendar("1").build());
    given(queryService.launchPrivateQuery(any(), any(ObjectNode.class))).willReturn(new ServiceData().setDataList(dataList));
  }

}
