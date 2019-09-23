package com.almis.awe.test.unit;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.dto.MaintainResultDetails;
import com.almis.awe.model.type.MaintainType;
import com.almis.awe.session.AweSessionDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
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

@Log4j2
public class TestUtil extends ServiceConfig {

  @Autowired
  protected WebApplicationContext applicationContext;

  @MockBean
  private AweSessionDetails aweSessionDetails;

  protected MockMvc mockMvc;
  protected ObjectMapper objectMapper;
  protected MockHttpSession session;
  protected String SESSION_ID = "16617f0d-97ee-4f6b-ad54-905d6ce3c328";

  public void setup() throws Exception {
    objectMapper = new ObjectMapper();
    mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    MockServletContext sc = new MockServletContext("");
    ServletContextListener listener = new ContextLoaderListener(applicationContext);
    ServletContextEvent event = new ServletContextEvent(sc);
    listener.contextInitialized(event);
    session = new MockHttpSession(null, SESSION_ID);
  }

  /**
   * Set parameter in session
   * @param name Parameter name
   * @param value Parameter value
   * @return Parameter set
   */
  protected String setParameter(String name, String value) throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/session/set/" + name)
      .header("Authorization", SESSION_ID)
      .param("value", value)
      .session(session))
      .andReturn();
    return mvcResult.getResponse().getContentAsString();
  }

  /**
   * Get parameter from session
   * @param name Parameter name
   * @return Parameter value
   */
  protected String getParameter(String name) throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/session/get/" + name)
      .header("Authorization", SESSION_ID)
      .session(session))
      .andReturn();
    return mvcResult.getResponse().getContentAsString();
  }

  /**
   * Remove parameter from session
   * @param name Parameter name
   * @return Parameter value
   */
  protected String removeParameter(String name) throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/session/remove/" + name)
      .header("Authorization", SESSION_ID)
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
  protected String readFileAsText(String path) throws IOException {
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
  protected void assertResultJson(String maintainName, String result, int expectedOperationNumber, MaintainResultDetails[] expectedOperations) throws Exception {
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

    logger.debug("--------------------------------------------------------------------------------------");
    logger.debug("There are " + resultDetails.size() + " operations as a result of launching maintain " + maintainName);
    logger.debug("--------------------------------------------------------------------------------------");
  }


  /**
   * Test of launchAction method, of class ActionController.
   *
   * @param method Clean up method
   * @throws Exception Test error
   */
  protected void cleanUp(String method) throws Exception {

    logger.debug("--------------------------------------------------------------------------------------");
    logger.debug(" Cleaning up all the mess... ");
    logger.debug("--------------------------------------------------------------------------------------");

    String maintainName = method;
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
      .header("Authorization", SESSION_ID)
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"max\":30}")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.debug(result);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  protected void loginUser() throws Exception {

    logger.debug("--------------------------------------------------------------------------------------");
    logger.debug(" Login user... ");
    logger.debug("--------------------------------------------------------------------------------------");

    String maintainName = "loginUser";

    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
      .header("Authorization", SESSION_ID)
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"max\":30}")
      .session(session)
      .accept("application/json"))
      .andExpect(status().isOk())
      .andReturn();
    setParameter("user", "test");
    String result = mvcResult.getResponse().getContentAsString();
    logger.debug(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.UPDATE, 0l)
    });
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  protected void logoutUser() throws Exception {

    logger.debug("--------------------------------------------------------------------------------------");
    logger.debug(" Logout user... ");
    logger.debug("--------------------------------------------------------------------------------------");

    String maintainName = "logoutUser";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
      .header("Authorization", SESSION_ID)
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"max\":30}")
      .session(session)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.debug(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.UPDATE, 0l)
    });
  }

}
