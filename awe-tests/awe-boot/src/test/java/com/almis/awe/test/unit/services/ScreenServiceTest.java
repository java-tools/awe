package com.almis.awe.test.unit.services;

import com.almis.awe.service.ScreenService;
import com.almis.awe.test.unit.TestUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@TestMethodOrder(Alphanumeric.class)
@Log4j2
public class ScreenServiceTest extends TestUtil {

  @InjectMocks
  private ScreenService screenService;

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
    assertThat(screenService).isNotNull();
  }

  /**
   * Check null screen
   */
  @Test
  public void getScreenElementListWithNull() {
    Assertions.assertThrows(NullPointerException.class, () -> screenService.getScreenElementList(null, null));
  }
}
