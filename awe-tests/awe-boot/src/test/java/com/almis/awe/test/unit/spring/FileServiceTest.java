package com.almis.awe.test.unit.spring;

import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.service.FileService;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.NamingException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author pgarcia
 */
public class FileServiceTest extends AweSpringBootTests {

  /**
   * Test context loaded
   * @throws NamingException Test error
   */
  @Test
  public void contextLoads() {
    // Check that controller is active
    assertThat(getBean(FileService.class)).isNotNull();
  }

  /**
   * Test of upload, download and delete file
   * @throws Exception Test error
   */
  @Test
  public void testUploadDownloadAndDeleteFile() throws Exception {
    FileService fileService = getBean(FileService.class);
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
    FileService fileService = getBean(FileService.class);
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