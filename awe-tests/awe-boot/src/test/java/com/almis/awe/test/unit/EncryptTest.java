package com.almis.awe.test.unit;

import com.almis.awe.model.util.security.Crypto;
import com.almis.awe.test.service.EncryptService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Class used for testing rest services through ActionController
 *
 * @author pgarcia
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
@WithMockUser(username = "test", password = "test")
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

  /**
   * Check encrypted properties
   */
  @Test
  public void checkEncryptedProperty() {
    EncryptService service = applicationContext.getBean(EncryptService.class);
    logger.warn("With @Value: " +service.getProperty());
    logger.warn("With Environment.getProperty: " +service.getEnvironmentProperty());
    assertEquals("prueba2", service.getProperty());
    assertEquals("prueba2", service.getEnvironmentProperty());
  }

  /**
   * Check pbkdf2 static method
   * @throws Exception
   */
  @Test
  public void checkPbkdf2() throws Exception {
    String goodValues = Crypto.Utils.pbkdf2("tutu", "qsadasdas", 0, 16);
    String badValues = Crypto.Utils.pbkdf2("tutu", "qsadasdas", -1, 44);
    assertEquals(goodValues, badValues);
  }

  /**
   * Check random bytes retrieval
   */
  @Test
  public void checkGetRandomBytes() {
    byte[] random = Crypto.Utils.getRandomBytes(-1);
    byte[] random2 = Crypto.Utils.getRandomBytes(8);
    assertNotEquals(random, random2);
  }

  /**
   * Check encrypt empty string with AES
   */
  @Test
  public void checkEncryptEmptyString() {
    String encrypted = Crypto.AES.encrypt("", "asdad", "UTF-8");
    assertNull(encrypted);
  }

  /**
   * Check encrypt with null key with AES
   */
  @Test
  public void checkEncryptNullPassphrase() {
    String encrypted = Crypto.AES.encrypt("asdads", null, "UTF-8");
    assertNull(encrypted);
  }

  /**
   * Check decrypt empty string with AES
   */
  @Test
  public void checkDecryptEmptyString() {
    String decrypted = Crypto.AES.decrypt("", "asdad", "UTF-8");
    assertNull(decrypted);
  }

  /**
   * Check decrypt with null key with AES
   */
  @Test
  public void checkDecryptNullPassphrase() {
    String decrypted = Crypto.AES.decrypt("asdads", null, "UTF-8");
    assertNull(decrypted);
  }
}