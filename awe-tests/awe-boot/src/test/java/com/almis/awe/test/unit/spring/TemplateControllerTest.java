package com.almis.awe.test.unit.spring;

import com.almis.awe.controller.ActionController;
import com.almis.awe.controller.SettingsController;
import com.almis.awe.controller.TemplateController;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.naming.NamingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author pgarcia
 */
@Log4j2
@WithAnonymousUser
public class TemplateControllerTest extends AweSpringBootTests {

  private ActionController actionController;
  private SettingsController settingsController;
  private TemplateController templateController;

  @BeforeEach
  public void initBeans() {
    actionController = getBean(ActionController.class);
    settingsController = getBean(SettingsController.class);
    templateController = getBean(TemplateController.class);
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
      .content(content)
      .accept(accept)).andExpect(status);
  }

  /**
   * Test a template call
   * @param endpoint Endpoint
   * @param accept Accept type
   * @param status Status
   * @return Result actions
   * @throws Exception
   */
  private ResultActions templateTest(String endpoint, String accept, ResultMatcher status) throws Exception{
    return mockMvc.perform(get(endpoint).accept(accept)).andExpect(status);
  }

  /**
   * Test context loaded
   * @throws NamingException Test error
   */
  @Test
  public void contextLoads() throws NamingException {
    // Check that controller are active
    assertThat(actionController).isNotNull();
    assertThat(settingsController).isNotNull();
    assertThat(templateController).isNotNull();
  }

  /**
   * Test of getAngularTemplate method, of class TemplateController.
   * @throws Exception Test error
   */
  @Test
  public void testGetAngularTemplate() throws Exception {
    String expected = readFileAsText("templates/Modal.txt");
    templateTest("/template/angular/confirm", "text/html;charset=UTF-8", status().isOk())
      .andExpect(content().string(expected));
  }

  /**
   * Test of getAngularSubTemplate method, of class TemplateController.
   * @throws Exception test error
   */
  @Test
  public void testGetAngularSubTemplate() throws Exception {
    String expected = readFileAsText("templates/Criterion.txt");
    templateTest("/template/angular/input/text", "text/html;charset=UTF-8", status().isOk())
      .andExpect(content().string(expected));
  }

  /**
   * Test of getScreenTemplate method, of class TemplateController.
   * @throws Exception test error
   */
  @Test
  public void testGetScreenTemplate() throws Exception {
    String expected = readFileAsText("templates/Screen.txt").replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));
    MvcResult result = templateTest("/template/screen/base/information", "text/html;charset=UTF-8", status().isOk())
      .andExpect(content().encoding("UTF-8"))
      .andReturn();

    assertEquals(result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")), expected);
  }

  /**
   * Test of launchAction method, of class ActionController.
   * @throws Exception Test error
   */
  @Test
  public void testGetErrorScreenTemplate() throws Exception {
    String expected = readFileAsText("templates/ScreenError.txt").replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));
    MvcResult result = templateTest("/template/screen/base/pantalla-inexistente", "text/html;charset=UTF-8", status().isUnauthorized())
      .andExpect(content().encoding("UTF-8"))
      .andReturn();

    assertEquals(result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")), expected);
  }

  /**
   * Test of getDefaultScreenTemplate method, of class TemplateController.
   * @throws Exception test error
   */
  @Test
  public void testGetDefaultScreenTemplate() throws Exception {
    String expected = readFileAsText("templates/DefaultScreen.txt").replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));
    MvcResult result = templateTest("/template/screen/", "text/html;charset=UTF-8", status().isOk())
      .andExpect(content().encoding("UTF-8"))
      .andReturn();

    assertEquals(result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")), expected);
  }

  /**
   * Test of public screen
   * @throws Exception test error
   */
  @Test
  public void testGetPublicScreenTemplate() throws Exception {
    String expected = readFileAsText("templates/DefaultScreen.txt").replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));
    MvcResult result = templateTest("/template/screen/base/signin", "text/html;charset=UTF-8", status().isOk())
      .andExpect(content().encoding("UTF-8"))
      .andReturn();

    assertEquals(result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")), expected);
  }

  /**
   * Test of getAngularTemplate method, of class TemplateController.
   * @throws Exception Test error
   */
  @Test
  @WithMockUser(username = "test", password = "test")
  public void testGetSitesHelpTemplate() throws Exception {
    given(aweSession.isAuthenticated()).willReturn(true);
    String expected = readFileAsText("context-help/ScreenHelp.txt").replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));
    MvcResult result = templateTest("/template/help/sites", "text/html;charset=UTF-8", status().isOk())
      .andExpect(content().encoding("UTF-8"))
      .andReturn();

    assertEquals(result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")), expected);
  }

  /**
   * Test of getAngularSubTemplate method, of class TemplateController.
   * @throws Exception test error
   */
  @Test
  @WithMockUser(username = "test", password = "test")
  public void testGetApplicationHelpTemplate() throws Exception {
    given(aweSession.isAuthenticated()).willReturn(true);
    String expected = readFileAsText("context-help/ApplicationHelp.txt").replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));
    MvcResult result = templateTest("/template/help", "text/html;charset=UTF-8", status().isOk())
      .andExpect(content().encoding("UTF-8"))
      .andReturn();

    assertEquals(expected, result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
  }

  /**
   * Test of getSettings method, of class SettingsController.
   * @throws Exception Test error
   */
  @Test
  public void testGetSettings() throws Exception {
    SettingsController settingsController = getBean(SettingsController.class);

    String expected = "{\"pathServer\":\"\",\"initialURL\":\"\",\"language\":\"en\",\"theme\":\"sky\",\"charset\":\"UTF-8\",\"applicationName\":\"awe-boot\",\"dataSuffix\":\".data\",\"homeScreen\":\"screen/home\",\"recordsPerPage\":30,\"pixelsPerCharacter\":8,\"defaultComponentSize\":\"sm\",\"reloadCurrentScreen\":false,\"suggestTimeout\":300,\"connectionProtocol\":\"COMET\",\"connectionTransport\":\"websocket\",\"connectionBackup\":\"streaming\",\"connectionTimeout\":60000000,\"uploadIdentifier\":\"u\",\"downloadIdentifier\":\"d\",\"uploadMaxSize\":500,\"addressIdentifier\":\"address\",\"passwordPattern\":\".*\",\"minlengthPassword\":4,\"encodeTransmission\":false,\"encodeKey\":\"p\",\"tokenKey\":\"t\",\"actionsStack\":0,\"debug\":\"INFO\",\"loadingTimeout\":10000,\"helpTimeout\":1000,\"messageTimeout\":{\"info\":0,\"error\":0,\"validate\":2000,\"help\":5000,\"warning\":4000,\"ok\":2000,\"wrong\":0,\"chat\":0},\"numericOptions\":{\"pSign\":\"s\",\"aDec\":\",\",\"vMin\":-1.0E10,\"dGroup\":3,\"vMax\":1.0E10,\"mDec\":5,\"mRound\":\"S\",\"aPad\":false,\"wEmpty\":\"empty\",\"aSep\":\".\",\"aSign\":\"\"},\"pivotOptions\":{\"numGroup\":5000},\"chartOptions\":{\"limitPointsSerie\":1000000}}";
    ObjectNode expectedJson = (ObjectNode) objectMapper.readTree(expected);
    MvcResult mvcResult = templateTestPost("/settings", "application/json", "{\"view\":\"base\"}", status().isOk())
      .andExpect(content().json(expected))
      .andReturn();

    String result = mvcResult.getResponse().getContentAsString();
    //logger.warn(result);
    //logger.warn(expected);
    ObjectNode retrievedJson = (ObjectNode) objectMapper.readTree(result);

    // Check objects
    assertTrue(expectedJson.equals(retrievedJson));
  }
}
