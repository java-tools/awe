package com.almis.awe.test.unit.database;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.service.MaintainService;
import com.almis.awe.service.QueryService;
import com.almis.awe.test.unit.spring.AweSpringBootTests;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Class used for testing queries and maintain services
 *
 * @author pgarcia
 */
@Log4j2
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@WithMockUser(username = "test", password = "test")
public class DirectServiceCallTest extends AweSpringBootTests {

  @Autowired
  private MaintainService maintainService;

  @Autowired
  private QueryService queryService;

  @Before
  public void setSessionDatabase() {
    given(aweSession.getParameter(String.class, "database")).willReturn("testDatabase");
  }

  /**
   * Test of maintain not defined.
   *
   * @throws AWException Test error
   */
  @Test(expected = AWException.class)
  public void testMaintainNotDefined() throws Exception {
    maintainService.launchMaintain("MaintainNotDefined");
  }

  /**
   * Test of maintain over a null alias (should launch over default database)
   *
   * @throws AWException Test error
   */
  @Test
  public void testMaintain() throws Exception {
    given(aweSession.isAuthenticated()).willReturn(true);
    MaintainService mock = Mockito.spy(maintainService);
    mock.launchMaintain("SimpleSingleInsertFromVariableValue");
    verify(mock, times(1)).getDatabaseConnection(any(ObjectNode.class));
  }

  /**
   * Test of maintain over a null alias (should launch over default database)
   *
   * @throws AWException Test error
   */
  @Test
  public void testMaintainWithValidAlias() throws Exception {
    given(aweSession.isAuthenticated()).willReturn(true);
    MaintainService mock = Mockito.spy(maintainService);
    Mockito.doReturn(maintainService.getDatabaseConnection()).when(mock).getDatabaseConnection(anyString());
    mock.launchMaintain("SimpleSingleInsertFromVariableValue", "aweora1");
    verify(mock, times(1)).getDatabaseConnection("aweora1");
  }

  /**
   * Test of maintain over a null alias (should launch over default database)
   *
   * @throws AWException Test error
   */
  @Test
  public void testPrivateMaintain() throws Exception {
    MaintainService mock = Mockito.spy(maintainService);
    mock.launchPrivateMaintain("SimpleSingleInsertFromVariableValue");
    verify(mock, times(1)).getDatabaseConnection(any(ObjectNode.class));
  }

  /**
   * Test of maintain over a null alias (should launch over default database)
   *
   * @throws AWException Test error
   */
  @Test
  public void testPrivateMaintainWithValidAlias() throws Exception {
    MaintainService mock = Mockito.spy(maintainService);
    Mockito.doReturn(maintainService.getDatabaseConnection()).when(mock).getDatabaseConnection(anyString());
    mock.launchPrivateMaintain("SimpleSingleInsertFromVariableValue", "aweora1");
    verify(mock, times(1)).getDatabaseConnection("aweora1");
  }

  /**
   * Test of query not defined.
   *
   * @throws AWException Test error
   */
  @Test(expected = AWException.class)
  public void testDatabaseQueryNotDefined() throws Exception {
    queryService.launchQuery("QueryNotDefined");
  }

  /**
   * Test of log database results
   *
   * @throws AWException Test error
   */
  @Test
  public void testLogDatabase() {
    LogUtil spyLog = Mockito.spy(getLogger());
    Logger spyLogger = Mockito.spy(LogManager.getLogger(DirectServiceCallTest.class));
    spyLog.logWithDatabase(DirectServiceCallTest.class, Level.DEBUG, "testDatabase", "test log message");
    verify(spyLog, times(1)).logWithDatabase(any(), eq(Level.DEBUG), anyString(), anyString());
    verify(spyLogger, times(0)).log(eq(Level.DEBUG), anyString());

    Mockito.doReturn(true).when(spyLogger).isDebugEnabled();
    spyLog.logWithDatabase(DirectServiceCallTest.class, Level.INFO, "testDatabase", "test log message");
    verify(spyLog, times(1)).logWithDatabase(any(), eq(Level.INFO), anyString(), anyString());
    verify(spyLogger, times(0)).log(eq(Level.INFO), anyString());
  }
}
