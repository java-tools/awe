package com.almis.awe.test;

import com.almis.awe.test.service.EncryptService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

/**
 * Class used for testing rest services through ActionController
 *
 * @author pgarcia
 *
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@WithMockUser(username = "test", password = "test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
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


  //@Test
  public void checkEncryptedProperty() {
    EncryptService service = applicationContext.getBean(EncryptService.class);
    logger.info("With @Value: " +service.getProperty());
    logger.info("With Environment.getProperty: " +service.getEnvironmentProperty());
    assertEquals("prueba2", service.getProperty());
    assertEquals("prueba2", service.getEnvironmentProperty());
  }
}