/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.almis.awe.controller.ActionController;
import com.almis.awe.controller.InitController;
import com.almis.awe.controller.SettingsController;
import com.almis.awe.controller.TemplateController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author pgarcia
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithAnonymousUser
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TemplateControllerTest extends TestUtil {

  @Autowired
  private WebApplicationContext applicationContext;

  @Autowired
  private ActionController actionController;

  @Autowired
  private InitController initController;

  @Autowired
  private SettingsController settingsController;

  @Autowired
  private TemplateController templateController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .build();
    objectMapper = new ObjectMapper();
    MockServletContext sc = new MockServletContext("");
    ServletContextListener listener = new ContextLoaderListener(applicationContext);
    ServletContextEvent event = new ServletContextEvent(sc);
    listener.contextInitialized(event);
  }

  /**
   * Test context loaded
   * @throws NamingException Test error
   */
  @Test
  public void contextLoads() throws NamingException {

    // Check that controller are active
    assertThat(actionController).isNotNull();
    assertThat(initController).isNotNull();
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
    mockMvc.perform(get("/template/angular/confirm")
            .accept("text/html;charset=UTF-8"))
            .andExpect(status().isOk())
            .andExpect(content().xml(expected))
            .andReturn();
  }

  /**
   * Test of getAngularSubTemplate method, of class TemplateController.
   * @throws Exception test error
   */
  @Test
  public void testGetAngularSubTemplate() throws Exception {
    String expected = readFileAsText("templates/Criterion.txt");
    mockMvc.perform(get("/template/angular/input/text")
            .accept("text/html;charset=UTF-8"))
            .andExpect(status().isOk())
            .andExpect(content().xml(expected))
            .andReturn();
  }

  /**
   * Test of getScreenTemplate method, of class TemplateController.
   * @throws Exception test error
   */
  @Test
  public void testGetScreenTemplate() throws Exception {
    String expected = readFileAsText("templates/Screen.txt").replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));
    MvcResult result = mockMvc.perform(get("/template/screen/base/information")
            .accept("text/html;charset=UTF-8"))
            .andExpect(status().isOk())
            .andExpect(content().encoding("UTF-8"))
            .andDo(print())
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
    MvcResult result = mockMvc.perform(get("/template/screen/base/pantalla-inexistente")
            .accept("text/html;charset=UTF-8"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().encoding("UTF-8"))
            .andDo(print())
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
    MvcResult result = mockMvc.perform(get("/template/screen/")
            .accept("text/html;charset=UTF-8"))
            .andExpect(status().isOk())
            .andExpect(content().encoding("UTF-8"))
            .andDo(print())
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
    String expected = readFileAsText("context-help/ScreenHelp.txt").replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));
    MvcResult result = mockMvc.perform(get("/template/help/sites")
            .accept("text/html;charset=UTF-8"))
            .andExpect(status().isOk())
            .andExpect(content().encoding("UTF-8"))
            .andDo(print())
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
    String expected = readFileAsText("context-help/ApplicationHelp.txt").replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));
    MvcResult result = mockMvc.perform(get("/template/help")
            .accept("text/html;charset=UTF-8"))
            .andExpect(status().isOk())
            .andExpect(content().encoding("UTF-8"))
            .andDo(print())
            .andReturn();

    assertEquals(result.getResponse().getContentAsString().replaceAll("\\n|\\r\\n", System.getProperty("line.separator")), expected);
  }

  /**
   * Test of getSettings method, of class SettingsController.
   * @throws Exception Test error
   */
  @Test
  public void testGetSettings() throws Exception {
    String expected = "{\"pathServer\":\"\",\"initialURL\":\"\",\"language\":\"en\",\"theme\":\"sky\",\"charset\":\"UTF-8\",\"applicationName\":\"AWE (Almis Web Engine)\",\"dataSuffix\":\".data\",\"homeScreen\":\"screen/home\",\"recordsPerPage\":30,\"pixelsPerCharacter\":8,\"defaultComponentSize\":\"sm\",\"shareSessionInTabs\":false,\"reloadCurrentScreen\":false,\"suggestTimeout\":300,\"connectionProtocol\":\"COMET\",\"connectionTransport\":\"websocket\",\"connectionBackup\":\"streaming\",\"connectionTimeout\":60000000,\"connectionId\":\"s\",\"uploadIdentifier\":\"u\",\"downloadIdentifier\":\"d\",\"uploadMaxSize\":524288000,\"addressIdentifier\":\"address\",\"passwordPattern\":\".*\",\"minlengthPassword\":4,\"encodeTransmission\":false,\"encodeKey\":\"p\",\"tokenKey\":\"t\",\"actionsStack\":0,\"debug\":\"INFO\",\"loadingTimeout\":10000,\"helpTimeout\":1000,\"messageTimeout\":{\"info\":0,\"error\":0,\"validate\":2000,\"help\":5000,\"warning\":4000,\"ok\":2000,\"wrong\":0,\"chat\":0},\"numericOptions\":{\"aSep\":\".\",\"dGroup\":3,\"aDec\":\",\",\"aSign\":\"\",\"pSign\":\"s\",\"vMin\":-1.0E10,\"vMax\":1.0E10,\"mDec\":5,\"mRound\":\"S\",\"aPad\":false,\"wEmpty\":\"empty\"},\"pivotOptions\":{\"numGroup\":5000},\"chartOptions\":{\"limitPointsSerie\":1000000}}";
    ObjectNode expectedJson = (ObjectNode) objectMapper.readTree(expected);
    MvcResult mvcResult = mockMvc.perform(post("/settings")
            .param("p", "{\"s\":\"b0d28a33-eea9-44c6-a142-a7fc6bfb7afa\",\"view\":\"base\"}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andDo(print())
            .andReturn();

    String result = mvcResult.getResponse().getContentAsString();
    ObjectNode retrievedJson = (ObjectNode) objectMapper.readTree(result);
    // retrievedJson.put("s", "b0d28a33-eea9-44c6-a142-a7fc6bfb7afa");

    // Check objects
    assertTrue(expectedJson.equals(retrievedJson));
  }
}
