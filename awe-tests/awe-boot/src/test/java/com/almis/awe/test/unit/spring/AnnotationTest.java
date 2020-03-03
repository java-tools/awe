package com.almis.awe.test.unit.spring;

import com.almis.awe.annotation.entities.security.Hash;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.util.file.FileUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import com.almis.awe.test.service.AnnotationTestService;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 * Class used for testing rest services through ActionController
 *
 * @author pgarcia
 */
@Log4j2
public class AnnotationTest extends AweSpringBootTests {

  private AnnotationTestService annotationTestService;

  @Before
  public void loadBeans() {
    annotationTestService = getBean(AnnotationTestService.class);
  }

  @Test
  public void checkLocaleAnnotations() {

    String valueFromInput = annotationTestService.localeFromParameters("ENUM_LAN_ES");
    String valueFromReturn = annotationTestService.localeFromReturnedValue();

    Assert.assertEquals("Español", valueFromInput);
    Assert.assertEquals("Español", valueFromReturn);
    Assert.assertEquals("Español", annotationTestService.localeFromAnnotationValue("This value should be overwritten"));
  }

  @Test
  public void checkHashAnnotations() throws Exception {
    //Hashing
    logger.warn("Check hash annotations");
    Assert.assertEquals(EncodeUtil.hash(Hash.HashingAlgorithm.SHA_256.getAlgorithm(), "Moderdonio", "1234"), annotationTestService.hashParameter("Moderdonio"));
    Assert.assertEquals(EncodeUtil.hash(Hash.HashingAlgorithm.SHA_256.getAlgorithm(), "Moderdonio", "1234"), annotationTestService.hashReturnedValue("Moderdonio"));
  }

  @Test
  public void checkCryptoAnnotations() throws Exception {
    // Crypto annotation on input parameters
    logger.warn("Check crypto annotations");
    String encriptedTextUtil = EncodeUtil.encryptAes("Moderdonio", "1234");
    logger.warn("EncodeUtil => " + encriptedTextUtil);
    String encryptedText = annotationTestService.encryptText("Moderdonio");
    logger.warn("Annotation => " + encryptedText);

    Assert.assertEquals("Moderdonio", EncodeUtil.decryptAes(encryptedText, "1234"));
    Assert.assertEquals("Moderdonio", annotationTestService.decryptText(encriptedTextUtil));

    // Crypto annotation on return values
    logger.warn("Check crypto annotations on return values");
    Assert.assertEquals("Moderdonio", EncodeUtil.decryptAes(annotationTestService.encryptReturnedText("Moderdonio"), "1234"));
    Assert.assertEquals("Moderdonio", annotationTestService.decryptReturnedText(encriptedTextUtil));
  }

  @Test
  public void checkAuditAnnotation() {
    // Test message audit | Symbolic, some Audit messages should appear on the log files
    annotationTestService.testAuditParamToConsole("Test message");
  }

  @Test
  public void checkSessionAnnotation() throws Exception {
    // Test variable input
    String inputValue = "This is the variable input value";
    String returnValue = "This is the return value";
    given(aweSession.getParameter(any(), anyString())).willReturn(inputValue, returnValue);
    String inputVariableText = annotationTestService.addParameterToSessionFromInputVariable(inputValue);
    Assert.assertEquals(inputVariableText, annotationTestService.getValueFromSessionOnInputVariable("This text should be overwritten"));

    String returnValueText = annotationTestService.addParameterToSessionFromReturnValue(returnValue);
    Assert.assertEquals(returnValueText, annotationTestService.getValueFromSessionOnReturnValue());

    given(aweSession.hasParameter(anyString())).willReturn(true);
    annotationTestService.addParameterToSessionFromInputVariable("test");
  }

  @Test
  public void checkGoToAnnotation() {
    // Test message audit | Symbolic, some Audit messages should appear on the log files
    Assert.assertEquals("index", annotationTestService.testGoToAnnotation().getClientActionList().get(0).getTarget());

    // Test message audit | Symbolic, some Audit messages should appear on the log files
    Assert.assertEquals("index", annotationTestService.testGoToAnnotationClientAction().getTarget());

    // Test message audit | Symbolic, some Audit messages should appear on the log files
    Assert.assertEquals("default", annotationTestService.testGoToAnnotationWithoutScreen().getTarget());

    // Test message audit | Symbolic, some Audit messages should appear on the log files
    Assert.assertEquals("default", annotationTestService.testGoToAnnotationReturningString());
  }

  @Test
  public void checkDownloadAnnotation() throws Exception {
    String file = this.getClass().getClassLoader().getResource("application.properties").getFile();

    FileData fileData = new FileData(new java.io.File(file).getName(), new java.io.File(file).length(), "application/octet-stream");
    fileData.setBasePath(new File(file).getParent());
    fileData.setFileName("customName");
    String fileDataString = FileUtil.fileDataToString(fileData);

    ClientAction clientAction = annotationTestService.downloadFile();
    Assert.assertEquals(fileDataString, clientAction.getParameterMap().get("filename"));

    ClientAction clientAction2 = annotationTestService.downloadFileNoParam();
    Assert.assertEquals(fileDataString, clientAction2.getParameterMap().get("filename"));

    ClientAction clientAction3 = annotationTestService.downloadFileFromVar(new File(file));
    Assert.assertEquals(fileDataString, clientAction3.getParameterMap().get("filename"));

    ClientAction clientAction4 = annotationTestService.downloadFileFromVarMixed(file);
    Assert.assertEquals(fileDataString, clientAction4.getParameterMap().get("filename"));
  }
}