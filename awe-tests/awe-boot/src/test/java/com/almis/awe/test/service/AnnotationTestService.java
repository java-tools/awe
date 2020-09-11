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
import com.almis.awe.builder.client.ScreenActionBuilder;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

import static com.almis.awe.annotation.entities.security.Crypto.ActionType.DECRYPT;
import static com.almis.awe.annotation.entities.security.Crypto.ActionType.ENCRYPT;

/**
 * The type Annotation test service.
 */
@Service
@Audit (value = @AuditParams (
        privateMethods = true,
        returnValues = true)
)
public class AnnotationTestService {

  /**
   * Check local annotations on the input parameters
   *
   * @param test the test
   * @return string
   */
  public String localeFromParameters(
          @Locale (language = "es") String test) {
    return test;
  }

  /**
   * Check locale annotation on the method, applies to the returned value
   *
   * @return string
   */
  @Locale (language = "ES")
  public String localeFromReturnedValue() {
    return "ENUM_LAN_ES";
  }

  /**
   * Check locale annotaiton value from value method on annotaiton
   *
   * @param param the param
   * @return the string
   */
  public String localeFromAnnotationValue(@Locale (value = "ENUM_LAN_ES", language = "ES") String param) {
    return param;
  }

  /**
   * Check hash annotation on input parameters
   *
   * @param text the text
   * @return string
   */
  public String hashParameter(@Hash (algorithm = Hash.HashingAlgorithm.SHA_256, salt = "1234") String text) {
    return text;
  }

  /**
   * Check hash annotations on returned values
   *
   * @param text the text
   * @return string
   */
  @Hash (algorithm = Hash.HashingAlgorithm.SHA_256, salt = "1234")
  public String hashReturnedValue(String text) {
    return text;
  }

  /**
   * ENCRYPT input value
   *
   * @param text the text
   * @return string
   */
  public String encryptText(@Crypto (action = ENCRYPT, password = "1234") String text) {
    return text;
  }

  /**
   * DECRYPT input value
   *
   * @param text the text
   * @return string
   */
  public String decryptText(@Crypto (action = DECRYPT, password = "1234") String text) {
    return text;
  }

  /**
   * ENCRYPT returned value
   *
   * @param text the text
   * @return string
   */
  @Crypto (action = ENCRYPT, password = "1234")
  public String encryptReturnedText(String text) {
    return text;
  }

  /**
   * DECRYPT returned value
   *
   * @param text the text
   * @return string
   */
  @Crypto (action = DECRYPT, password = "1234")
  public String decryptReturnedText(String text) {
    return text;
  }

  /**
   * Test audit annotation to console
   *
   * @param test the test
   * @return string
   */
  @Audit(value = @AuditParams(
          privateMethods = true,
          returnValues = false)
  )
  public String testAuditParamToConsole(String test) {
    return test + " returned!";
  }

  /**
   * Test audit annotations private method
   *
   * @param test input text
   * @return text to show in console
   */
  @Audit(value = @AuditParams(
          privateMethods = true,
          returnValues = false)
  )
  public String testAuditPrivateMethodToConsole(String test) {
    return callPrivateMethod(test);
  }

  /**
   * Test audit annotations private method
   *
   * @param test input text
   * @return text to show in console
   */
  @Audit(value = @AuditParams(
          privateMethods = false,
          returnValues = false)
  )
  public String testAuditPrivateMethodAsFalseToConsole(String test) {
    return callPrivateMethod(test);
  }

  /**
   * Test audit annotation return values
   *
   * @param test input text
   * @return text to show in console
   */
  @Audit(value = @AuditParams(
          privateMethods = true,
          returnValues = true)
  )
  public String testAuditMethodReturnValuesToConsole(String test) {
    return callPrivateMethod(test);
  }


  /**
   * Test audit annotation return value list
   *
   * @return list to show in console
   */
  @Audit(value = @AuditParams(privateMethods = true, returnValues = true))
  public Collection<String> testAuditMethodReturnList() {
    return getDummyList();
  }

  /**
   * Test audit annotation return values of map
   *
   * @return list to show in console
   */
  @Audit(value = @AuditParams(privateMethods = true, returnValues = true))
  public Map<String, String> testAuditMethodReturnMap() {
    return getHashMap();
  }

  @NotNull
  private HashMap<String, String> getHashMap() {
    return new HashMap<String, String>() {{
      put("key1", "value1");
      put("key2", "value2");
      put("key3", "value3");
    }};
  }

  @NotNull
  private List<String> getDummyList() {
    return Arrays.asList("Elem1", "Elem2", "Elem3");
  }

  @NotNull
  private String callPrivateMethod(String test) {
    return test + " returned!";
  }

  /**
   * Add parameter value to session from input variable
   *
   * @param sample the sample
   * @return string
   */
  public String addParameterToSessionFromInputVariable(@ToSession(name = "sample-variable") String sample) {
    return sample;
  }

  /**
   * Add parameter value to session from input variable
   *
   * @param sample the sample
   * @return string
   */
  @ToSession (name = "sample-return")
  public String addParameterToSessionFromReturnValue(String sample) {
    return sample;
  }

  /**
   * Get variable value on input variable
   *
   * @param sample the sample
   * @return value from session on input variable
   */
  public String getValueFromSessionOnInputVariable(@FromSession (name = "sample-variable") String sample) {
    return sample;
  }

  /**
   * Get variable value on return
   *
   * @return value from session on return value
   */
  @FromSession (name = "sample-return")
  public String getValueFromSessionOnReturnValue() {
    return "this text should be overwritten by the annotation";
  }


  /**
   * Test GoTo annotation
   *
   * @return service data
   */
  @GoTo (screenName = "index")
  public ServiceData testGoToAnnotation() {
    return new ServiceData();
  }

  /**
   * Test GoTo annotation
   *
   * @return client action
   */
  @GoTo (screenName = "index")
  public ClientAction testGoToAnnotationClientAction() {
    return new ClientAction();
  }

  /**
   * Test GoTo annotation
   *
   * @return client action
   */
  @GoTo
  public ClientAction testGoToAnnotationWithoutScreen() {
    return new ClientAction("screen").setTarget("default");
  }

  /**
   * Test GoTo annotation
   *
   * @return string
   */
  @GoTo (screenName = "index")
  public String testGoToAnnotationReturningString() {
    return "default";
  }

  /**
   * Test Download annotation
   *
   * @return client action
   */
  @Download (value = "new java.io.File(new org.springframework.core.io.ClassPathResource(\"application.properties\").getFile())", name = "customName")
  public ClientAction downloadFile() {
    return new ScreenActionBuilder().build();
  }

  /**
   * Test Download annotation
   *
   * @return client action
   */
  @Download (value = "new java.io.File(new org.springframework.core.io.ClassPathResource(\"application.properties\").getFile())", name = "customName")
  public ClientAction downloadFileNoParam() {
    return new ScreenActionBuilder().build();
  }

  /**
   * Test Download annotation
   *
   * @param downloadFile the download file
   * @return client action
   */
  @Download (value = "#downloadFile", name = "customName")
  public ClientAction downloadFileFromVar(File downloadFile) {
    return new ScreenActionBuilder().build();
  }

  /**
   * Test Download annotation
   *
   * @param downloadFile the download file
   * @return client action
   */
  @Download (value = "new java.io.File(#downloadFile)", name = "customName")
  public ClientAction downloadFileFromVarMixed(String downloadFile) {
    return new ScreenActionBuilder().build();
  }
}
