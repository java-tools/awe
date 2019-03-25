/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.test.unit;

import com.almis.awe.controller.UploadController;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.service.BroadcastService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author pgarcia
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(username = "test", password = "test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UploadControllerTest extends TestUtil {

  private final static String TOKEN = "16617f0d-97ee-4f6b-ad54-905d6ce3c328";

  // Upload identifier
  @Value("${file.upload.identifier:u}")
  private String uploadIdentifierKey;

  @MockBean
  private BroadcastService broadcastService;

  @MockBean
  private AweSession aweSession;

  @Autowired
  private UploadController uploadController;

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void setup() throws Exception {
    super.setup();
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
    Mockito.verify(broadcastService, atLeastOnce()).broadcastMessageToUID(Mockito.anyString(), Mockito.any(ClientAction.class));
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
    Mockito.verify(broadcastService, atLeastOnce()).broadcastMessageToUID(Mockito.anyString(), Mockito.any(ClientAction.class));
  }

  /**
   * Test upload file ko
   * @throws Exception Test error
   */
  @Test
  public void testUploadKOAWException() throws Exception {
    uploadController.handleAWException(new AWException("tutu", "lala"));
    Mockito.verify(broadcastService, atLeastOnce()).broadcastMessageToUID(Mockito.anyString(), Mockito.any(ClientAction.class));
  }

  /**
   * Test upload file ko
   * @throws Exception Test error
   */
  @Test
  public void testUploadKOMaxSize() throws Exception {
    uploadController.handleMaxSizeException(new MaxUploadSizeExceededException(22342342323L));
    Mockito.verify(broadcastService, atLeastOnce()).broadcastMessageToUID(Mockito.anyString(), Mockito.any(ClientAction.class));
  }
}