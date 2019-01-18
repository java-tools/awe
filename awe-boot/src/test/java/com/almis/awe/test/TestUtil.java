package com.almis.awe.test;

import com.almis.awe.model.dto.MaintainResultDetails;
import com.almis.awe.model.type.MaintainType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUtil {

  @Autowired
  protected WebApplicationContext applicationContext;
  protected MockMvc mockMvc;
  ObjectMapper objectMapper;
  protected MockHttpSession session;

  // Logger
  private static Logger logger = LogManager.getLogger(TestUtil.class);

  public void setup() throws Exception {
    objectMapper = new ObjectMapper();
    mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    MockServletContext sc = new MockServletContext("");
    ServletContextListener listener = new ContextLoaderListener(applicationContext);
    ServletContextEvent event = new ServletContextEvent(sc);
    listener.contextInitialized(event);
    session = new MockHttpSession();
  }
  /**
   * Set parameter in session
   * @param name Parameter name
   * @param value Parameter value
   * @return Parameter set
   */
  String setParameter(String name, String value) throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/session/set/" + name + "/" + value)
            .session(session))
            .andReturn();
    return mvcResult.getResponse().getContentAsString();
  }

  /**
   * Get parameter from session
   * @param name Parameter name
   * @return Parameter value
   */
  String getParameter(String name) throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/session/get/" + name)
            .session(session))
            .andReturn();
    return mvcResult.getResponse().getContentAsString();
  }

  /**
   * Remove parameter from session
   * @param name Parameter name
   * @return Parameter value
   */
  String removeParameter(String name) throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/session/remove/" + name)
            .session(session))
            .andReturn();
    return mvcResult.getResponse().getContentAsString();
  }

  /**
   * Read a test file as Text
   * @param path
   * @return
   * @throws IOException
   */
  String readFileAsText(String path) throws IOException {
    Resource resource = new ClassPathResource(path);
    return FileUtils.readFileToString(resource.getFile(), StandardCharsets.UTF_8.name());
  }

  /**
   * Asserts the JSON in the response
   *
   * @param maintainName Maintain name
   * @param result Result
   * @param expectedOperationNumber Expected operations number
   * @param expectedOperations Expected operations
   * @throws Exception Error in asser
   */
  void assertResultJson(String maintainName, String result, int expectedOperationNumber, MaintainResultDetails[] expectedOperations) throws Exception {
    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);
    ObjectNode messageAction = (ObjectNode) resultList.get(1);
    assertEquals("message", messageAction.get("type").textValue());
    ObjectNode messageParameters = (ObjectNode) messageAction.get("parameters");
    assertEquals(4, messageParameters.size());
    ArrayNode resultDetails = (ArrayNode) messageParameters.get("result_details");
    assertTrue(expectedOperationNumber <= resultDetails.size());

    if (expectedOperationNumber > 0 && expectedOperations != null) {
      for (int i = 0; i < resultDetails.size(); i++) {
        ObjectNode operationDetails = (ObjectNode) resultDetails.get(i);
        MaintainResultDetails expected = expectedOperations[i];

        assertEquals(expected.getOperationType().name(), operationDetails.get("operationType").asText());
        assertTrue(expected.getRowsAffected() <= new Long(operationDetails.get("rowsAffected").asLong()));
      }
    }

    logger.info("--------------------------------------------------------------------------------------");
    logger.info("There are " + resultDetails.size() + " operations as a result of launching maintain " + maintainName);
    logger.info("--------------------------------------------------------------------------------------");
  }


  /**
   * Test of launchAction method, of class ActionController.
   *
   * @param method Clean up method
   * @throws Exception Test error
   */
  void cleanUp(String method) throws Exception {

    logger.info("--------------------------------------------------------------------------------------");
    logger.info(" Cleaning up all the mess... ");
    logger.info("--------------------------------------------------------------------------------------");

    String maintainName = method;
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
      .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\",\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
      .accept("application/json"))
      .andExpect(status().isOk())
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[] {
      new MaintainResultDetails(MaintainType.DELETE, 0l)
    });
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  void loginUser() throws Exception {

    logger.info("--------------------------------------------------------------------------------------");
    logger.info(" Login user... ");
    logger.info("--------------------------------------------------------------------------------------");

    String maintainName = "loginUser";

    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
      .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\",\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
      .session(session)
      .accept("application/json"))
      .andExpect(status().isOk())
      .andReturn();
    setParameter("user", "test");
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.UPDATE, 0l)
    });
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  void logoutUser() throws Exception {

    logger.info("--------------------------------------------------------------------------------------");
    logger.info(" Logout user... ");
    logger.info("--------------------------------------------------------------------------------------");

    String maintainName = "logoutUser";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
      .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\",\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
      .session(session)
      .accept("application/json"))
      .andExpect(status().isOk())
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.UPDATE, 0l)
    });
  }

}
