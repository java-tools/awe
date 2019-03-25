/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.test.unit;

import com.almis.awe.exception.AWException;
import com.almis.awe.service.TemplateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author pgarcia
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithAnonymousUser
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TagListTest extends TestUtil {

  @MockBean
  TemplateService templateService;

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void setup() throws Exception {
    super.setup();
  }

  /**
   * Test a template call
   * @param endpoint Endpoint
   * @param accept Accept type
   * @param status Status
   * @return Result actions
   * @throws Exception
   */
  private ResultActions templateTestPost(String endpoint, String accept, String content, ResultMatcher status) throws Exception{
    return mockMvc.perform(post(endpoint)
      .header("Authorization", "b0d28a33-eea9-44c6-a142-a7fc6bfb7afa")
      .contentType(MediaType.APPLICATION_JSON)
      .content(content)
      .accept(accept)).andExpect(status);
  }

  /**
   * Test of getAngularSubTemplate method, of class TemplateController.
   * @throws Exception test error
   */
  @Test
  @WithMockUser(username = "test", password = "test")
  public void testTagList() throws Exception {
    String expected = "test";
    when(templateService.generateTaglistTemplate(anyString())).thenReturn(expected);
    MvcResult result = templateTestPost("/template/taglist/taglist1", "text/plain;charset=UTF-8", "{}", status().isOk())
      .andExpect(content().encoding("UTF-8"))
      .andReturn();

    assertEquals(expected, result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
  }

  /**
   * Test of getAngularSubTemplate method, of class TemplateController.
   * @throws Exception test error
   */
  @Test
  @WithMockUser(username = "test", password = "test")
  public void testTagListWithError() throws Exception {
    String expected = "test";
    when(templateService.generateTaglistTemplate(anyString())).thenThrow(new AWException("", ""));
    MvcResult result = templateTestPost("/template/taglist/taglist1", "text/plain;charset=UTF-8", "{}", status().isOk())
      .andExpect(content().encoding("UTF-8"))
      .andReturn();

    assertEquals("", result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
  }

  /**
   * Test of getAngularSubTemplate method, of class TemplateController.
   * @throws Exception test error
   */
  @Test
  @WithMockUser(username = "test", password = "test")
  public void testTagListOption() throws Exception {
    String expected = "test";
    when(templateService.generateTaglistTemplate(anyString(), anyString())).thenReturn(expected);
    MvcResult result = templateTestPost("/template/taglist/option1/taglist1", "text/plain;charset=UTF-8", "{}", status().isOk())
      .andExpect(content().encoding("UTF-8"))
      .andReturn();

    assertEquals(expected, result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
  }

  /**
   * Test of getAngularSubTemplate method, of class TemplateController.
   * @throws Exception test error
   */
  @Test
  @WithMockUser(username = "test", password = "test")
  public void testTagListOptionWithError() throws Exception {
    String expected = "test";
    when(templateService.generateTaglistTemplate(anyString(), anyString())).thenThrow(new AWException("", ""));
    MvcResult result = templateTestPost("/template/taglist/option1/taglist1", "text/plain;charset=UTF-8", "{}", status().isOk())
      .andExpect(content().encoding("UTF-8"))
      .andReturn();

    assertEquals("", result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
  }
}
