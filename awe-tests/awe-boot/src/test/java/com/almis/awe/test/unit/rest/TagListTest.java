package com.almis.awe.test.unit.rest;

import com.almis.awe.exception.AWException;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author pgarcia
 */
public class TagListTest extends AweSpringRestTests {

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
  public void testTagList() throws Exception {
    String expected = "test";
    when(templateService.generateTaglistTemplate(anyString())).thenReturn(expected);
    MvcResult result = templateTestPost("/taglist/taglist1", "text/plain;charset=UTF-8", "{}", status().isOk())
      .andExpect(content().encoding("UTF-8"))
      .andReturn();

    assertEquals(expected, result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
  }

  /**
   * Test of getAngularSubTemplate method, of class TemplateController.
   * @throws Exception test error
   */
  @Test
  public void testTagListWithError() throws Exception {
    String expected = "test";
    when(templateService.generateTaglistTemplate(anyString())).thenThrow(new AWException("", ""));
    MvcResult result = templateTestPost("/taglist/taglist1", "text/plain;charset=UTF-8", "{}", status().isOk())
      .andExpect(content().encoding("UTF-8"))
      .andReturn();

    assertEquals("", result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
  }

  /**
   * Test of getAngularSubTemplate method, of class TemplateController.
   * @throws Exception test error
   */
  @Test
  public void testTagListOption() throws Exception {
    String expected = "test";
    when(templateService.generateTaglistTemplate(anyString(), anyString())).thenReturn(expected);
    MvcResult result = templateTestPost("/taglist/option1/taglist1", "text/plain;charset=UTF-8", "{}", status().isOk())
      .andExpect(content().encoding("UTF-8"))
      .andReturn();

    assertEquals(expected, result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
  }

  /**
   * Test of getAngularSubTemplate method, of class TemplateController.
   * @throws Exception test error
   */
  @Test
  public void testTagListOptionWithError() throws Exception {
    String expected = "test";
    when(templateService.generateTaglistTemplate(anyString(), anyString())).thenThrow(new AWException("", ""));
    MvcResult result = templateTestPost("/taglist/option1/taglist1", "text/plain;charset=UTF-8", "{}", status().isOk())
      .andExpect(content().encoding("UTF-8"))
      .andReturn();

    assertEquals("", result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
  }
}
