package com.almis.awe.test.unit.spring;

import com.almis.awe.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NamingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * Menu service tests
 * @author pgarcia
 */
public class MenuServiceTest extends AweSpringBootTests {

  private MenuService menuService;

  @Before
  public void loadBeans() {
    menuService = getBean(MenuService.class);
  }

  /**
   * Test context loaded
   * @throws NamingException Test error
   */
  @Test
  public void contextLoads() {
    // Check that controller are active
    assertThat(menuService).isNotNull();
  }

  /**
   * Test of check public addresses
   * @throws Exception Test error
   */
  @Test
  public void testCheckPublicAddresses() throws Exception {
    given(aweSession.isAuthenticated()).willReturn(false);
    assertFalse(menuService.checkOptionAddress(""));
    assertTrue(menuService.checkOptionAddress("screen/signin"));
    assertFalse(menuService.checkOptionAddress("screen/patata"));
    assertFalse(menuService.checkOptionAddress("screen/private/home/information"));
  }

  /**
   * Test of check private addresses
   * @throws Exception Test error
   */
  @Test
  public void testCheckPrivateAddresses() throws Exception {
    given(aweSession.isAuthenticated()).willReturn(true);
    assertFalse(menuService.checkOptionAddress(""));
    assertTrue(menuService.checkOptionAddress("screen/signin"));
    assertFalse(menuService.checkOptionAddress("screen/patata"));
    assertTrue(menuService.checkOptionAddress("screen/private/home/information"));
  }

  /**
   * Check available public screen list
   * @throws Exception
   */
  @Test
  public void getAvailablePublicScreenList() throws Exception {
    given(aweSession.isAuthenticated()).willReturn(false);
    assertEquals(6, menuService.getAvailableScreenList("").getDataList().getRecords());
    assertEquals(1, menuService.getAvailableScreenList("si").getDataList().getRecords());
  }

  /**
   * Check available private screen list
   * @throws Exception
   */
  @Test
  public void getAvailablePrivateScreenList() throws Exception {
    given(aweSession.isAuthenticated()).willReturn(true);
    assertEquals(11, menuService.getAvailableScreenList("").getDataList().getRecords());
    ObjectMapper mapper = new ObjectMapper();
  }
}