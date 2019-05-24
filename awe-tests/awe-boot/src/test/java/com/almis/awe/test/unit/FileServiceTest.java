package com.almis.awe.test.unit;

import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.service.FileService;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.NamingException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author pgarcia
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class FileServiceTest extends TestUtil {

  @MockBean
  private AweSession aweSession;

  @MockBean
  private AweRequest aweRequest;


  @Autowired
  private FileService fileService;

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void setup() throws Exception {
    super.setup();
  }

  /**
   * Test context loaded
   * @throws NamingException Test error
   */
  @Test
  public void contextLoads() {

    // Check that controller are active
    assertThat(fileService).isNotNull();
  }

  /**
   * Test of upload, download and delete file
   * @throws Exception Test error
   */
  @Test
  public void testUploadDownloadAndDeleteFile() throws Exception {
    MultipartFile file = new MockMultipartFile("test", "test", "text/plain", "lalala".getBytes());
    FileData fileData = fileService.uploadFile(file, null);
    assertEquals(HttpStatus.OK, fileService.downloadFile(fileData, 0).getStatusCode());
    assertEquals(AnswerType.OK, fileService.deleteFile(fileData).getType());
  }

  /**
   * Test of upload, download and delete file
   * @throws Exception Test error
   */
  @Test
  public void testDownloadMockedInputStream() throws Exception {
    String input = "some test data for my input stream";
    InputStream inputStream = IOUtils.toInputStream(input, "UTF-8");
    FileData fileData = new FileData("tutu", (long) input.getBytes().length, "application/pdf");
    fileData.setFileStream(inputStream);
    ResponseEntity fileDownloaded = fileService.downloadFile(fileData, 0);
    assertEquals(HttpStatus.OK, fileDownloaded.getStatusCode());
    assertEquals("tutu", fileDownloaded.getHeaders().get("Filename").get(0));
    assertEquals(MediaType.APPLICATION_PDF, fileDownloaded.getHeaders().getContentType());
  }
}