package com.almis.awe.test.service;

import com.almis.awe.annotation.entities.audit.Audit;
import com.almis.awe.annotation.entities.audit.AuditParams;
import com.almis.awe.annotation.entities.locale.Locale;
import com.almis.awe.annotation.entities.security.Crypto;
import com.almis.awe.annotation.entities.security.Hash;
import com.almis.awe.annotation.entities.session.FromSession;
import com.almis.awe.annotation.entities.session.ToSession;
import com.almis.awe.annotation.entities.util.Download;
import com.almis.awe.annotation.entities.util.GoTo;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Audit (value = @AuditParams (
  privateMethods = true,
  returnValues = true)
)
public class AnnotationTestService {

  /**
   * Check local annotations on the input parameters
   *
   * @param test
   *
   * @return
   */
  public String localeFromParameters(
    @Locale (language = "ES") String test) {
    String testValue = test;
    return testValue;
  }

  /**
   * Check locale annotation on the method, applies to the returned value
   *
   * @return
   */
  @Locale (language = "ES")
  public String localeFromReturnedValue() {
    return "ENUM_LAN_MO";
  }

  /**
   * Check locale annotaiton value from value method on annotaiton
   */
  public String localeFromAnnotationValue(@Locale (value = "ENUM_LAN_MO", language = "ES") String param) {
    return param;
  }

  /**
   * Check hash annotation on input parameters
   *
   * @param text
   *
   * @return
   */
  public String hashParameter(@Hash (algorithm = Hash.HashingAlgorithm.SHA_256, salt = "1234") String text) {
    return text;
  }

  /**
   * Check hash annotations on returned values
   *
   * @param text
   *
   * @return
   */
  @Hash (algorithm = Hash.HashingAlgorithm.SHA_256, salt = "1234")
  public String hashReturnedValue(String text) {
    return text;
  }

  /**
   * ENCRYPT input value
   *
   * @param text
   *
   * @return
   */
  public String encryptText(@Crypto (action = Crypto.ActionType.ENCRYPT, password = "1234") String text) {
    return text;
  }

  /**
   * DECRYPT input value
   *
   * @param text
   *
   * @return
   */
  public String decryptText(@Crypto (action = Crypto.ActionType.DECRYPT, password = "1234") String text) {
    return text;
  }

  /**
   * ENCRYPT returned value
   *
   * @param text
   *
   * @return
   */
  @Crypto (action = Crypto.ActionType.ENCRYPT, password = "1234")
  public String encryptReturnedText(String text) {
    return text;
  }

  /**
   * DECRYPT returned value
   *
   * @param text
   *
   * @return
   */
  @Crypto (action = Crypto.ActionType.DECRYPT, password = "1234")
  public String decryptReturnedText(String text) {
    return text;
  }

  /**
   * Test audit annotation to console
   *
   * @param test
   *
   * @return
   */
  @Audit (value = @AuditParams (
    privateMethods = true,
    returnValues = false)
  )
  public String testAuditParamToConsole(String test) {
    return test + " returned!";
  }

  /**
   * Add parameter value to session from input variable
   *
   * @param sample
   *
   * @return
   */
  public String addParameterToSessionFromInputVariable(@ToSession (name = "sample-variable") String sample) {
    return sample;
  }

  /**
   * Add parameter value to session from input variable
   *
   * @param sample
   *
   * @return
   */
  @ToSession (name = "sample-return")
  public String addParameterToSessionFromReturnValue(String sample) {
    return sample;
  }

  /**
   * Get variable value on input variable
   *
   * @param sample
   *
   * @return
   */
  public String getValueFromSessionOnInputVariable(@FromSession (name = "sample-variable") String sample) {
    return sample;
  }

  /**
   * Get variable value on return
   *
   * @return
   */
  @FromSession (name = "sample-return")
  public String getValueFromSessionOnReturnValue() {
    return "this text should be overwritten by the annotation";
  }


  /**
   * Test GoTo annotation
   *
   * @return
   */
  @GoTo (screenName = "index")
  public ServiceData testGoToAnnotation() {
    return new ServiceData();
  }

  /**
   * Test Download annotation
   *
   * @return
   */
  @Download (value = "new java.io.File(new org.springframework.core.io.ClassPathResource(\"application.properties\").getFile())", name = "customName")
  public ClientAction downloadFile() {
    return new ClientAction("screen");
  }

  /**
   * Test Download annotation
   *
   * @return
   */
  @Download (value = "new java.io.File(new org.springframework.core.io.ClassPathResource(\"application.properties\").getFile())", name = "customName")
  public ClientAction downloadFileNoParam() {
    return new ClientAction("screen");
  }

  /**
   * Test Download annotation
   *
   * @return
   */
  @Download (value = "#downloadFile", name = "customName")
  public ClientAction downloadFileFromVar(File downloadFile) {
    return new ClientAction("screen");
  }

  /**
   * Test Download annotation
   *
   * @return
   */
  @Download (value = "new java.io.File(#downloadFile)", name = "customName")
  public ClientAction downloadFileFromVarMixed(String downloadFile) {
    return new ClientAction("screen");
  }
}
