package com.almis.awe.test.unit.spring;

import com.almis.awe.model.util.security.Crypto;
import com.almis.awe.test.service.EncryptService;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Class used for testing rest services through ActionController
 *
 * @author pgarcia
 *
 */
@Log4j2
public class EncryptTest extends AweSpringBootTests {

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

  /**
   * Check encrypt with AES a null value
   */
  @Test(expected = NullPointerException.class)
  public void checkEncryptNull() {
    Crypto.AES.encrypt(null, null, "UTF-8");
  }

  /**
   * Check decrypt with AES a null value
   */
  @Test(expected = NullPointerException.class)
  public void checkDecryptNull() {
    Crypto.AES.decrypt(null, null, "UTF-8");
  }
}