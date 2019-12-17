package com.almis.awe.test.unit.spring;

import com.almis.awe.controller.UploadController;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.actions.ClientAction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author pgarcia
 */
public class UploadControllerTest extends AweSpringBootTests {

  private final static String TOKEN = "16617f0d-97ee-4f6b-ad54-905d6ce3c328";

  // Upload identifier
  private String uploadIdentifierKey;
  private UploadController uploadController;

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void initBeans() throws Exception {
    uploadIdentifierKey = getProperty("file.upload.identifier");
    uploadController = getBean(UploadController.class);
  }

  /**
   * Test a UPLOAD POST
   * @param file File to upload
   * @throws Exception
   */
  private void doUploadTest(MockMultipartFile file, String address, String destination, ResultMatcher status) throws Exception {
    mockMvc.perform(fileUpload("/file/upload")
      .file(file)
      .param("address", address)
      .param("destination", destination)
      .param(uploadIdentifierKey, "uploader-test-1")
      .header("Authorization", TOKEN))
      .andExpect(status)
      .andReturn();
  }

  /**
   * Test upload file ok
   * @throws Exception Test error
   */
  @Test
  public void testUploadOK() throws Exception {
    String fileName = "orig";
    String content = "bar";
    MockMultipartFile file = new MockMultipartFile("file", fileName, MediaType.APPLICATION_OCTET_STREAM_VALUE, content.getBytes());
    doUploadTest(file, "{}", "", status().isOk());
    Mockito.verify(broadcastService, atLeastOnce()).broadcastMessageToUID(anyString(), any(ClientAction.class));
  }

  /**
   * Test upload file ko
   * @throws Exception Test error
   */
  @Test
  public void testUploadKO() throws Exception {
    String fileName = "orig";
    String content = "bar";
    MockMultipartFile file = new MockMultipartFile("kk", fileName, MediaType.APPLICATION_JSON_VALUE, content.getBytes());
    doUploadTest(file, "{}", "", status().is4xxClientError());
    Mockito.verify(broadcastService, atLeastOnce()).broadcastMessageToUID(isNull(), any(ClientAction.class));
  }

  /**
   * Test upload file ko
   * @throws Exception Test error
   */
  @Test
  public void testUploadKOAWException() throws Exception {
    uploadController.handleAWException(new AWException("tutu", "lala"));
    Mockito.verify(broadcastService, atLeastOnce()).broadcastMessageToUID(isNull(), any(ClientAction.class));
  }

  /**
   * Test upload file ko
   * @throws Exception Test error
   */
  @Test
  public void testUploadKOMaxSize() throws Exception {
    uploadController.handleMaxSizeException(new MaxUploadSizeExceededException(22342342323L));
    Mockito.verify(broadcastService, atLeastOnce()).broadcastMessageToUID(isNull(), any(ClientAction.class));
  }
}