package com.almis.awe.test.unit.services;

import com.almis.awe.service.ScreenService;
import com.almis.awe.test.unit.TestUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.naming.NamingException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Log4j2
public class ScreenServiceTest extends TestUtil {

  @InjectMocks
  private ScreenService screenService;

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
    assertThat(screenService).isNotNull();
  }

  /**
   * Check null screen
   */
  @Test(expected = NullPointerException.class)
  public void getScreenElementListWithNull() throws Exception {
    screenService.getScreenElementList(null, null);
  }
}
