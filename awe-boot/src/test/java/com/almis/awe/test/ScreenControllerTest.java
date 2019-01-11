/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.almis.awe.controller.ActionController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author jbellon
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WithAnonymousUser
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ScreenControllerTest {

  @Autowired
  private WebApplicationContext applicationContext;

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  // Logger
  private static Logger logger = LogManager.getLogger(ActionController.class);

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void setup() {
    objectMapper = new ObjectMapper();
    mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    MockServletContext sc = new MockServletContext("");
    ServletContextListener listener = new ContextLoaderListener(applicationContext);
    ServletContextEvent event = new ServletContextEvent(sc);
    listener.contextInitialized(event);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  //@Test
  public void testLaunchScreenDataAction() throws Exception {
    String expected = "[{\"type\":\"screen-data\",\"parameters\":{\"view\":\"base\",\"screenData\":{\"components\":[{\"id\":\"ButLogIn\",\"controller\":{\"actions\":[{\"type\":\"validate\",\"parameters\":{}},{\"type\":\"server\",\"parameters\":{\"serverAction\":\"login\"}}],\"buttonType\":\"submit\",\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"icon\":\"sign-in\",\"id\":\"ButLogIn\",\"label\":\"BUTTON_LOGIN\",\"optional\":false,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"no-class btn btn-primary signin-btn bg-primary\",\"visible\":true},\"model\":{\"selected\":[],\"defaultValues\":[],\"values\":[]}},{\"id\":\"pwd_usr\",\"controller\":{\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"icon\":\"key signin-form-icon\",\"id\":\"pwd_usr\",\"optional\":false,\"placeholder\":\"SCREEN_TEXT_PASS\",\"printable\":true,\"readonly\":false,\"required\":true,\"size\":\"lg\",\"strict\":true,\"style\":\"no-label\",\"validation\":\"required\",\"visible\":true},\"model\":{\"selected\":[],\"defaultValues\":[],\"values\":[]}},{\"id\":\"cod_usr\",\"controller\":{\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"icon\":\"user signin-form-icon\",\"id\":\"cod_usr\",\"optional\":false,\"placeholder\":\"SCREEN_TEXT_USER\",\"printable\":true,\"readonly\":false,\"required\":true,\"size\":\"lg\",\"strict\":true,\"style\":\"no-label\",\"validation\":\"required\",\"visible\":true},\"model\":{\"selected\":[],\"defaultValues\":[],\"values\":[]}}],\"messages\":{},\"errors\":[],\"screen\":{\"name\":\"signin\",\"title\":\"SCREEN_TITLE_LOGIN\",\"option\":null}}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/screen-data")
            .param("p", "{\"s\":\"e6144dad-6e67-499e-b74a-d1e600732e11\",\"view\":\"base\"}")
            .accept("application/json"))
            .andExpect(status().isOk())
            //.andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    logger.info(expected);
    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);

    ObjectNode screenDataAction = (ObjectNode) resultList.get(0);
    assertEquals("screen-data", screenDataAction.get("type").textValue());
    ObjectNode screenDataParameters = (ObjectNode) screenDataAction.get("parameters");
    assertEquals(2, screenDataParameters.size());
    assertEquals("base", screenDataParameters.get("view").textValue());
    ObjectNode screenData = (ObjectNode) screenDataParameters.get("screenData");
    assertEquals(0, screenData.get("actions").size());
    assertEquals(0, screenData.get("messages").size());
    ArrayNode screenDataComponents = (ArrayNode) screenData.get("components");
    assertEquals(3, screenDataComponents.size());
    assertEquals("signin", screenData.get("screen").get("name").textValue());

    ObjectNode endLoad = (ObjectNode) resultList.get(1);
    assertEquals("end-load", endLoad.get("type").textValue());
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    // Test all keys
    for (JsonNode element: screenDataComponents) {
      ObjectNode component = (ObjectNode) element;
      String key = component.get("id").asText();
      logger.info(key + ": " + component.get("model").get("selected").toString());
    }

    logger.info("-------------------------------------------");
    logger.info("There are " + screenDataComponents.size() + " component in the screen " + screenData.get("screen").get("name"));
    logger.info("-------------------------------------------");
  }

  /**
   * Test of launchAction method, of class ActionController.
   * @throws Exception Test error
   */
  //@Test
  public void testLaunchScreenDataActionError() throws Exception {
    String expected = "[{\"type\":\"screen-data\",\"parameters\":{\"view\":\"base\",\"screenData\":{\"components\":[],\"messages\":{},\"actions\":[],\"screen\":{\"name\":\"error\",\"title\":\"Option not defined\",\"option\":\"error\"}}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/screen-data")
            .param("p", "{\"s\":\"e6144dad-6e67-499e-b74a-d1e600732e11\",\"option\":\"pantalla-inexistente\",\"view\":\"base\"}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
  }

  /**
   * Test of launchAction method, of class ActionController.
   * @throws Exception Test error
   */
  //@Test
  public void testLaunchGetLocalsAction() throws Exception {

    MvcResult mvcResult = mockMvc.perform(post("/action/get-locals")
            .param("p", "{\"s\":\"e6144dad-6e67-499e-b74a-d1e600732e11\",\"language\":\"es\"}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    //logger.info(result);
    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);

    ObjectNode endLoad = (ObjectNode) resultList.get(0);
    assertEquals("end-load", endLoad.get("type").textValue());
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    ObjectNode localsRetrievedActionES = (ObjectNode) resultList.get(1);
    assertEquals("locals-retrieved", localsRetrievedActionES.get("type").textValue());
    ObjectNode localsRetrievedParametersES = (ObjectNode) localsRetrievedActionES.get("parameters");
    assertEquals(2, localsRetrievedParametersES.size());
    ObjectNode translationsES = (ObjectNode) localsRetrievedParametersES.get("translations");
    int translationEsSize = translationsES.size();

    mvcResult = mockMvc.perform(post("/action/get-locals")
            .param("p", "{\"s\":\"e6144dad-6e67-499e-b74a-d1e600732e11\",\"language\":\"en\"}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andReturn();
    result = mvcResult.getResponse().getContentAsString();
    //logger.info(result);

    ObjectNode localsRetrievedActionEN = (ObjectNode) resultList.get(1);
    assertEquals("locals-retrieved", localsRetrievedActionEN.get("type").textValue());
    ObjectNode localsRetrievedParametersEN = (ObjectNode) localsRetrievedActionEN.get("parameters");
    assertEquals(2, localsRetrievedParametersEN.size());
    ObjectNode translationsEN = (ObjectNode) localsRetrievedParametersEN.get("translations");
    assertEquals(translationEsSize, translationsEN.size());

    mvcResult = mockMvc.perform(post("/action/get-locals")
            .param("p", "{\"s\":\"e6144dad-6e67-499e-b74a-d1e600732e11\",\"language\":\"fr\"}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andReturn();
    result = mvcResult.getResponse().getContentAsString();
    //logger.info(result);

    ObjectNode localsRetrievedActionFR = (ObjectNode) resultList.get(1);
    assertEquals("locals-retrieved", localsRetrievedActionFR.get("type").textValue());
    ObjectNode localsRetrievedParametersFR = (ObjectNode) localsRetrievedActionFR.get("parameters");
    assertEquals(2, localsRetrievedParametersFR.size());
    ObjectNode translationsFR = (ObjectNode) localsRetrievedParametersFR.get("translations");
    assertEquals(translationEsSize, translationsFR.size());

    // Test all keys
    ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> translationsMap = mapper.convertValue(translationsES, Map.class);
    List<String> keys = new ArrayList<>();
    keys.addAll(translationsMap.keySet());
    Collections.sort(keys);

    for (String key : keys) {
      assertTrue(translationsEN.has(key));
      assertTrue(translationsFR.has(key));
    }

    logger.info("-------------------------------------------");
    logger.info("There are " + translationEsSize + " locales");
    logger.info("-------------------------------------------");
  }
}
