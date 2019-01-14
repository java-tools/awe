package com.almis.awe.test;

import com.almis.awe.annotation.entities.session.FromSession;
import com.almis.awe.annotation.entities.session.ToSession;
import com.almis.awe.model.dto.MaintainResultDetails;
import com.almis.awe.model.type.MaintainType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(username = "mgr", password = "rai")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ScreenConfigurationTest extends TestUtil {

  // Logger
  private static Logger logger = LogManager.getLogger(ScreenConfigurationTest.class);
  private SimpleDateFormat sdfDate;
  private String textController;
  private String textModel;
  private String buttonController;
  private String criteriaTestLeft;
  private String screenParameters;

	/*
	 * Tables - Test schema Fields - Test functions with doubles (now is casted to Long always)
	 */

  /**
   * Initializes json mapper for tests
   * @throws Exception error updating user
   */
  @Before
  public void setup() throws Exception{
    super.setup();

    // Update user
    loginUser();

    // Initialize date and files
    sdfDate = new SimpleDateFormat("dd/MM/yyyy");

    // Read json files
    criteriaTestLeft = readFileAsText("screen-data/criteriaTestLeft.json");
    buttonController = readFileAsText("screen-data/buttonController.json");
    textController = readFileAsText("screen-data/textController.json");
    textModel = readFileAsText("screen-data/textModel.json");

    // Initialize parameters
    screenParameters = "{\"s\":\"e6144dad-6e67-499e-b74a-d1e600732e11\",\"option\":\"criteria-test-left\",\"view\":\"report\",\"TxtRea\":\"15:06:23\"}";

    // Clean up
    cleanUp("CleanUpScreenConfiguration");
  }

  /**
   * Close and logout
   * @throws Exception error updating user
   */
  @After
  public void clean() throws Exception{
    // Clean up
    cleanUp("CleanUpScreenConfiguration");

    // Update user
    logoutUser();
  }

  /**
   * Add a restriction
   *
   * @throws Exception Test error
   */
  private void addRestriction(String operation, String screen, String component, String attribute, String value) throws Exception {
    String maintainName = "updateScreenConfiguration";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"" + operation + "\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"updateScreenConfiguration\",\"module\":\"Test\",\"language\":\"en\",\"RefreshTime\":null,\"site\":\"Madrid\",\"database\":\"awemadora01\",\"theme\":\"sunset\",\"SetAutoload\":\"0\",\"GrdScrCnf\":1,\"GrdScrCnf.data\":{\"max\":30,\"page\":1,\"sort\":[]},\"IdeAweScrCnf\":[\"\"],\"IdeAweScrCnf.selected\":null,\"Scr\":[\"" + screen + "\"],\"Scr.selected\":null,\"IdeOpe\":[\"\"],\"IdeOpe.selected\":null,\"IdePro\":[\"\"],\"IdePro.selected\":null,\"Nam\":[\"" + component + "\"],\"Nam.selected\":null,\"Atr\":[\"" + attribute + "\"],\"Atr.selected\":null,\"Val\":[\"" + value + "\"],\"Val.selected\":null,\"Act\":[\"1\"],\"Act.selected\":null,\"GrdScrCnf-id\":[\"new-row-1\"],\"GrdScrCnf-RowTyp\":[\"" + operation + "\"],\"PrnScr\":\"ScrCnf\",\"CrtScr\":null,\"UsrPrn\":null,\"ActPrn\":\"2\",\"FmtPrn\":\"PDF\",\"CrtAct\":null,\"CrtUsr\":null,\"DblFmtPrn\":\"0\",\"TypPrn\":\"1\",\"CrtPro\":null,\"s\":\"410e0604-84d7-4cf1-9d28-4a6ddcf97d34\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.valueOf(operation), 1l)
    });
  }

  /**
   * Test of screen restriction
   *
   * @throws Exception Test error
   */
  private void testButtonRestriction(String attribute, JsonNode value) throws Exception {
    // Add restriction
    addRestriction("INSERT","CrtTstLeft", "ButBck", attribute, value.asText());

    // Define button controller
    ObjectNode buttonControllerObject = (ObjectNode) objectMapper.readTree(buttonController);
    buttonControllerObject.set(attribute, value);
    String buttonControllerUpdated = buttonControllerObject.toString();

    // Check screen
    Date date = new Date();
    String expected = addVariables(criteriaTestLeft, date, buttonControllerUpdated, textController, textModel);
    MvcResult mvcResult = mockMvc.perform(post("/action/screen-data")
            .param("p", screenParameters)
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    //logger.info(result);
    //logger.info(expected);
  }

  /**
   * Test of screen restriction
   *
   * @throws Exception Test error
   */
  private void testCriterionRestriction(String attribute, JsonNode value) throws Exception {
    // Add restriction
    addRestriction("INSERT","CrtTstLeft", "Txt", attribute, value.asText());

    // Define text controller
    ObjectNode textControllerObject = (ObjectNode) objectMapper.readTree(textController);
    textControllerObject.set(attribute, value);
    String textControllerUpdated = textControllerObject.toString();

    // Check screen
    Date date = new Date();
    String expected = addVariables(criteriaTestLeft, date, buttonController, textControllerUpdated, textModel);
    MvcResult mvcResult = mockMvc.perform(post("/action/screen-data")
            .param("p", screenParameters)
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    // logger.info(result);
    // logger.info(expected);
  }

  /**
   * Test of screen restriction - Hide criterion
   *
   * @throws Exception Test error
   */
  @Test
  public void testRestrictVisibility() throws Exception {
    testButtonRestriction("visible", JsonNodeFactory.instance.booleanNode(false));
  }

  /**
   * Test of screen restriction - Change label
   *
   * @throws Exception Test error
   */
  @Test
  public void testChangeLabel() throws Exception {
    testButtonRestriction("label", JsonNodeFactory.instance.textNode("Tira patraaaaas"));
  }

  /**
   * Test of screen restriction - Change label
   *
   * @throws Exception Test error
   */
  @Test
  public void testSetReadonly() throws Exception {
    testButtonRestriction("readonly", JsonNodeFactory.instance.booleanNode(true));
  }

  /**
   * Test of screen restriction - Optional
   *
   * @throws Exception Test error
   */
  @Test
  public void testOptional() throws Exception {
    testButtonRestriction("optional", JsonNodeFactory.instance.booleanNode(true));
  }

  /**
   * Test of screen restriction - Printable
   *
   * @throws Exception Test error
   */
  @Test
  public void testPrintable() throws Exception {
    testButtonRestriction("printable", JsonNodeFactory.instance.booleanNode(false));
  }

  /**
   * Test of screen restriction - Checked
   *
   * @throws Exception Test error
   */
  @Test
  public void testChecked() throws Exception {
    testButtonRestriction("checked", JsonNodeFactory.instance.booleanNode(true));
  }

  /**
   * Test of screen restriction - Help label
   *
   * @throws Exception Test error
   */
  @Test
  public void testHelpLabel() throws Exception {
    testButtonRestriction("help", JsonNodeFactory.instance.textNode("Ayudaaaaaaaaaaaaaaaa!"));
  }

  /**
   * Test of screen restriction - Style
   *
   * @throws Exception Test error
   */
  @Test
  public void testStyle() throws Exception {
    testButtonRestriction("style", JsonNodeFactory.instance.textNode("claseDeLaMuerte otraClaseChula"));
  }

  /**
   * Test of screen restriction - Initial load
   *
   * @throws Exception Test error
   */
  @Test
  public void testInitialLoad() throws Exception {
    // Add restrictions
    addRestriction("INSERT","CrtTstLeft", "Txt", "initialLoad", "value");
    addRestriction("INSERT","CrtTstLeft", "Txt", "targetAction", "TestComponentInitialLoadValue");

    // Text model
    String textModelUpdated = "{\"selected\":[1.0],\"defaultValues\":[1.0],\"values\":[{\"kk\":\"1\",\"value2\":1,\"label\":\"mgr\",\"id\":1,\"value\":1.0}],\"page\":1,\"total\":1,\"records\":1}";

    // Check screen
    Date date = new Date();
    String expected = addVariables(criteriaTestLeft, date, buttonController, textController, textModelUpdated);
    MvcResult mvcResult = mockMvc.perform(post("/action/screen-data")
            .param("p", screenParameters)
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
  }

  /**
   * Add variables to the json data
   * @param jsonData Json data
   * @return Json data with date and time
   */
  private String addVariables(String jsonData, Date date, String buttonControllerData, String textControllerData, String textModelData) {
    String currentDate = sdfDate.format(date);
    return jsonData
            .replace("{{currentDate}}", currentDate)
            .replace("{{currentTime}}", "15:06:23")
            .replace("{{buttonController}}", buttonControllerData)
            .replace("{{textController}}", textControllerData)
            .replace("{{textModel}}", textModelData);
  }

  // *****************************************************************************************************************//
  // SESSION TESTS
  // **************************************************************************************************************** //

  /**
   * Test that the setters to the session storage are working correctly.
   */
  @Test
  public void testSetSessionValues() throws Exception {
    setParameter("parameter1", "value1");
    setParameter("parameter2", "value2");
    setParameter("parameter3", "value3");
    setParameter("parameter4", "value4");

    Assert.assertTrue(getParameter("parameter1").equals("value1"));
    Assert.assertTrue(getParameter("parameter2").equals("value2"));
    Assert.assertTrue(getParameter("parameter3").equals("value3"));
    Assert.assertTrue(getParameter("parameter4").equals("value4"));
  }

  /**
   * Test that the parameters from session are correctly modified
   */
  @Test
  public void testModifySessionValues() throws Exception {
    setParameter("parameter1", "value4");
    setParameter("parameter2", "value3");
    setParameter("parameter3", "value2");
    setParameter("parameter4", "value1");

    Assert.assertTrue(getParameter("parameter1").equals("value4"));
    Assert.assertTrue(getParameter("parameter2").equals("value3"));
    Assert.assertTrue(getParameter("parameter3").equals("value2"));
    Assert.assertTrue(getParameter("parameter4").equals("value1"));
  }

  /**
   * Test that the parameters from session are correctly removed
   *
   */
  @Test
  public void testRemoveSessionValues() throws Exception {
    //Add values first
    testSetSessionValues();

    removeParameter("parameter1");
    removeParameter("parameter2");
    removeParameter("parameter3");
    removeParameter("parameter4");

    Assert.assertTrue(getParameter("parameter1").isEmpty());
    Assert.assertTrue(getParameter("parameter2").isEmpty());
    Assert.assertTrue(getParameter("parameter3").isEmpty());
    Assert.assertTrue(getParameter("parameter4").isEmpty());
  }

  /**
   * Test the setter and getter annotations for session parameters
   */
  //@Test
  public void testSessionAnnotations() throws Exception {
    toSessionMethodAnnotation();
    Assert.assertEquals(getParameter("param"), "toSessionMethod");

    toSessionParameterAnnotation("toSessionParameter");
    Assert.assertEquals(getParameter("param"), "toSessionParameter");

    setParameter("param", "fromSessionValue");

    Assert.assertEquals(fromSessionMethodAnnotation(), "fromSessionValue");
    Assert.assertEquals(fromSessionParameterAnnotation("session"), "fromSessionValue");
  }

  /**
   * Adds a value to the session with a custom annotation on the method.
   *
   * @return To session
   */
  @ToSession(name = "param")
  private String toSessionMethodAnnotation(){
    return "toSessionMethod";
  }

  /**
   * Adds a value to the session with a custom annotation on the parameter.
   *
   * @param toSession To session
   */
  private void toSessionParameterAnnotation(@ToSession(name = "param") String toSession){}

  /**
   * Retrieves a value from the session with a custom annotation on the method.
   *
   * @return From session
   */
  @FromSession(name = "param")
  private String fromSessionMethodAnnotation(){
    return new String();
  }

  /**
   * Retrieves a value from the session with a custom annotation on the parameter.
   *
   * @param fromSession From session
   * @return From session
   */
  private String fromSessionParameterAnnotation(@FromSession(name = "param") String fromSession){
    return fromSession;
  }

}