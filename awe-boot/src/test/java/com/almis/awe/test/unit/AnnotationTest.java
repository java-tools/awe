package com.almis.awe.test.unit;

import com.almis.awe.annotation.entities.security.Hash;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.util.file.FileUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import com.almis.awe.test.service.AnnotationTestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;

/**
 * Class used for testing rest services through ActionController
 *
 * @author pgarcia
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@WithMockUser(username = "test", password = "test")
public class AnnotationTest {

  @Value ("${application.base.path:/}")
  private String applicationBasePath;

  @Autowired
  private AnnotationTestService annotationTestService;

  @Autowired
  private FileUtil fileUtil;

  @MockBean
  private AweSession aweSession;

  // Logger
  private static Logger logger = LogManager.getLogger(AnnotationTest.class);

  @Test
  public void checkLocaleAnnotations() {

    String valueFromInput = annotationTestService.localeFromParameters("ENUM_LAN_ES");
    String valueFromReturn = annotationTestService.localeFromReturnedValue();

    Assert.assertEquals("Spanish", valueFromInput);
    Assert.assertEquals("Spanish", valueFromReturn);
    Assert.assertEquals("Spanish", annotationTestService.localeFromAnnotationValue("This value should be overwritten"));
  }

  @Test
  public void checkHashAnnotations() throws AWException {
    //Hashing
    logger.warn("Check hash annotations");
    Assert.assertEquals(EncodeUtil.hash(Hash.HashingAlgorithm.SHA_256.getAlgorithm(), "Moderdonio", "1234"), annotationTestService.hashParameter("Moderdonio"));
    Assert.assertEquals(EncodeUtil.hash(Hash.HashingAlgorithm.SHA_256.getAlgorithm(), "Moderdonio", "1234"), annotationTestService.hashReturnedValue("Moderdonio"));
  }

  @Test
  public void checkCryptoAnnotations() throws AWException {
    // Crypto annotation on input parameters
    logger.warn("Check crypto annotations");
    String encriptedTextUtil = EncodeUtil.encryptAes("Moderdonio",  "1234");
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
    given(aweSession.getParameter(anyObject(), anyString())).willReturn(inputValue, returnValue);
    String inputVariableText = annotationTestService.addParameterToSessionFromInputVariable(inputValue);
    Assert.assertEquals(inputVariableText, annotationTestService.getValueFromSessionOnInputVariable("This text should be overwritten"));

    String returnValueText = annotationTestService.addParameterToSessionFromReturnValue(returnValue);
    Assert.assertEquals(returnValueText, annotationTestService.getValueFromSessionOnReturnValue());

    given(aweSession.hasParameter(anyString())).willReturn(true);
    annotationTestService.addParameterToSessionFromInputVariable("test");
  }

  @Test
  public void checkGoToAnnotation() {
    //Test message audit | Symbolic, some Audit messages should appear on the log files
    Assert.assertEquals("index", annotationTestService.testGoToAnnotation().getClientActionList().get(0).getTarget());
  }

  @Test
  public void checkDownloadAnnotation() throws AWException {
    String file = this.getClass().getClassLoader().getResource("application.properties").getFile();

    //Create sample file
    //Files.createFile(new java.io.File(applicationBasePath + File.separator + file).toPath());

    FileData fileData = new FileData(new java.io.File(file).getName(), new java.io.File(file).length(), "application/octet-stream");
    fileData.setBasePath(new File(file).getParent());
    fileData.setFileName("customName");
    CellData fileDataCell = new CellData(fileUtil.fileDataToString(fileData));

    ClientAction clientAction = annotationTestService.downloadFile();

    Assert.assertEquals(fileDataCell, clientAction.getParameterMap().get("filename"));

    ClientAction clientAction2 = annotationTestService.downloadFileNoParam();

    Assert.assertEquals(fileDataCell, clientAction2.getParameterMap().get("filename"));

    ClientAction clientAction3 = annotationTestService.downloadFileFromVar(new File(file));

    Assert.assertEquals(fileDataCell, clientAction3.getParameterMap().get("filename"));

    ClientAction clientAction4 = annotationTestService.downloadFileFromVarMixed(file);

    Assert.assertEquals(fileDataCell, clientAction4.getParameterMap().get("filename"));
  }
}