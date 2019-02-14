package com.almis.awe.test.unit;

import com.almis.awe.test.service.EncryptService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * Class used for testing rest services through ActionController
 *
 * @author pgarcia
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@WithMockUser(username = "test", password = "test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class EncryptTest extends TestUtil {

  // Logger
  private static Logger logger = LogManager.getLogger(EncryptTest.class);

	/*
	 * Tables - Test schema Fields - Test functions with doubles (now is casted to Long always)
	 */

  /**
   * Initializes json mapper for tests
   * @throws Exception error updating user
   */
  @Before
  public void setup() throws Exception{
    super.setup();
  }


  @Test
  public void checkEncryptedProperty() {
    EncryptService service = applicationContext.getBean(EncryptService.class);
    logger.debug("With @Value: " +service.getProperty());
    logger.debug("With Environment.getProperty: " +service.getEnvironmentProperty());
    assertEquals("prueba2", service.getProperty());
    assertEquals("prueba2", service.getEnvironmentProperty());
  }
}