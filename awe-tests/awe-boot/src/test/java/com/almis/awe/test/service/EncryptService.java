package com.almis.awe.test.service;

import com.almis.awe.config.ServiceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Dummy Service class to test the queries that call services
 * 
 * @author jbellon
 *
 */
@Service
public class EncryptService extends ServiceConfig {

  @Value("${encrypted.property}")
  private String encryptedProperty;

  /**
   * Get decrypted property
   * @return
   */
  public String getProperty() {
    return encryptedProperty;
  }

  /**
   * Get decrypted property from environment
   * @return
   */
  public String getEnvironmentProperty() {
    return getProperty("encrypted.property");
  }
}
