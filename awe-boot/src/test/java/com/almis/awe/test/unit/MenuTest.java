/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.test.unit;

import com.almis.awe.model.component.AweSession;
import com.almis.awe.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.naming.NamingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

/**
 *
 * @author pgarcia
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MenuTest extends TestUtil {

  @MockBean
  private AweSession aweSession;

  @Autowired
  private MenuService menuService;

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void setup() throws Exception {
    super.setup();
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