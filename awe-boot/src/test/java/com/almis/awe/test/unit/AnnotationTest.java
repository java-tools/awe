package com.almis.awe.test.unit;

import com.almis.awe.annotation.entities.security.Hash;
import com.almis.awe.exception.AWException;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

/**
 * Class used for testing rest services through ActionController
 *
 * @author pgarcia
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@WithMockUser(username = "test", password = "test")
public class AnnotationTest extends TestUtil {
  @Autowired
  private AnnotationTestService annotationTestService;
  @Value ("${application.base.path:/}")
  private String applicationBasePath;
  @Autowired
  private FileUtil fileUtil;

  // Logger
  private static Logger logger = LogManager.getLogger(AnnotationTest.class);

  /**
   * Initializes json mapper for tests
   *
   * @throws Exception error updating user
   */
  @Before
  public void setup() throws Exception {
    super.setup();
  }

  @Test
  public void checkLocaleAnnotations() {

    String valueFromInput = annotationTestService.localeFromParameters("ENUM_LAN_ES");
    String valueFromReturn = annotationTestService.localeFromReturnedValue();

    Assert.assertEquals("Spanish", valueFromInput);
    Assert.assertEquals("Spanish", valueFromReturn);
    Assert.assertEquals("Spanish", annotationTestService.localeFromAnnotationValue("This value should be overwritten"));
  }

  //@Test
  public void checkHashAnnotations() throws AWException {
    //Hashing
    Assert.assertEquals(EncodeUtil.hash(Hash.HashingAlgorithm.SHA_256.getAlgorithm(), "Moderdonio", "1234"), annotationTestService.hashParameter("Moderdonio"));
    Assert.assertEquals(EncodeUtil.hash(Hash.HashingAlgorithm.SHA_256.getAlgorithm(), "Moderdonio", "1234"), annotationTestService.hashReturnedValue("Moderdonio"));
  }

  //@Test
  public void checkCryptoAnnotations() throws AWException {
    // Crypto annotation on input parameters
    logger.debug("EncodeUtil => " + EncodeUtil.encryptAes("Moderdonio",  "1234"));
    logger.debug("Annotation => " + annotationTestService.encryptText("Moderdonio"));
    Assert.assertEquals("Moderdonio", EncodeUtil.decryptAes(annotationTestService.encryptText("Moderdonio"), "1234"));
    Assert.assertEquals("Moderdonio", annotationTestService.decryptText(EncodeUtil.encryptAes("Moderdonio", "1234")));

    //Crypto annotation on return values
    Assert.assertEquals("Moderdonio", EncodeUtil.decryptAes(annotationTestService.encryptReturnedText("Moderdonio"), "1234"));
    Assert.assertEquals("Moderdonio", annotationTestService.decryptReturnedText(EncodeUtil.encryptAes("Moderdonio", "1234")));
  }

  @Test
  public void checkAuditAnnotation() {
    // Test message audit | Symbolic, some Audit messages should appear on the log files
    annotationTestService.testAuditParamToConsole("Test message");
  }

  //@Test
  //TODO create a session before executing
  public void checkSessionAnnotation() throws Exception {
    //Test variable input
    String inputVariableText = annotationTestService.addParameterToSessionFromInputVariable("This is the variable input value");
    Assert.assertEquals(inputVariableText, annotationTestService.getValueFromSessionOnInputVariable("This text should be overwritten"));

    String returnValueText = annotationTestService.addParameterToSessionFromReturnValue("This is the return value");
    Assert.assertEquals(returnValueText, annotationTestService.getValueFromSessionOnReturnValue());
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