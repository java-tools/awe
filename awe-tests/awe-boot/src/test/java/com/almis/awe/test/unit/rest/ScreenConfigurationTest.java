package com.almis.awe.test.unit.rest;

import com.almis.awe.annotation.entities.session.FromSession;
import com.almis.awe.annotation.entities.session.ToSession;
import com.almis.awe.model.dto.MaintainResultDetails;
import com.almis.awe.model.type.MaintainType;
import com.almis.awe.service.screen.ScreenConfigurationGenerator;
import com.almis.awe.test.unit.database.DirectServiceCallTest;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@Log4j2
public class ScreenConfigurationTest extends AweSpringRestTests {

  private static final String AUTHORIZATION_HEADER = "410e0604-84d7-4cf1-9d28-4a6ddcf97d34";
  private String screenParameters;

  /**
   * Initializes json mapper for tests
   *
   * @throws Exception error updating user
   */
  @Before
  public void setup() throws Exception {
    super.setup();

    // Update user
    loginUser();

    // Initialize parameters
    screenParameters = "{\"s\":\"e6144dad-6e67-499e-b74a-d1e600732e11\",\"option\":\"criteria-test-left\",\"view\":\"report\",\"TxtRea\":\"15:06:23\"}";

    // Clean up
    cleanUp("CleanUpScreenConfiguration");
  }

  /**
   * Close and logout
   *
   * @throws Exception error updating user
   */
  @After
  public void clean() throws Exception {
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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"" + operation + "\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
      .header("Authorization", AUTHORIZATION_HEADER)
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"module\":\"Test\",\"language\":\"en\",\"RefreshTime\":null,\"site\":\"Madrid\",\"database\":\"awemadora01\",\"theme\":\"sunset\",\"SetAutoload\":\"0\",\"GrdScrCnf\":1,\"GrdScrCnf.data\":{\"max\":30,\"page\":1,\"sort\":[]},\"IdeAweScrCnf\":[\"\"],\"IdeAweScrCnf.selected\":null,\"Scr\":[\"" + screen + "\"],\"Scr.selected\":null,\"IdeOpe\":[\"\"],\"IdeOpe.selected\":null,\"IdePro\":[\"\"],\"IdePro.selected\":null,\"Nam\":[\"" + component + "\"],\"Nam.selected\":null,\"Atr\":[\"" + attribute + "\"],\"Atr.selected\":null,\"Val\":[\"" + value + "\"],\"Val.selected\":null,\"Act\":[\"1\"],\"Act.selected\":null,\"GrdScrCnf-id\":[\"new-row-1\"],\"GrdScrCnf-RowTyp\":[\"" + operation + "\"],\"PrnScr\":\"ScrCnf\",\"CrtScr\":null,\"UsrPrn\":null,\"ActPrn\":\"2\",\"FmtPrn\":\"PDF\",\"CrtAct\":null,\"CrtUsr\":null,\"DblFmtPrn\":\"0\",\"TypPrn\":\"1\",\"CrtPro\":null,\"max\":30}")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(expected))
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.debug(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.valueOf(operation), 1L)
    });
  }


  /**
   * Test of screen restriction - Hide criterion
   *
   * @throws Exception Test error
   */
  @Test
  public void testRestrictVisibility() throws Exception {
    // Add restriction
    addRestriction("INSERT", "CrtTstLeft", "ButBck", "visible", "false");
    // Check screen
    checkAttributeComponent("ButBck", "visible", false);
  }

  /**
   * Test of screen restriction - Restricted values of criterion
   *
   * @throws Exception Test error
   */
  @Test
  public void testRestrictedValues() throws Exception {

    addRestriction("INSERT", "CrtTstLeft", "SelRea", "restrictedValueList", "0");

    // Check screen
    MvcResult mvcResult = mockMvc.perform(post("/action/screen-data")
            .header("Authorization", AUTHORIZATION_HEADER)
            .contentType(MediaType.APPLICATION_JSON)
            .content(screenParameters)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].parameters.screenData.components[?(@.id == 'SelRea')].model.records", is(Collections.singletonList(1))))
            .andExpect(jsonPath("$[0].parameters.screenData.components[?(@.id == 'SelRea')].model.values[0].label", is(Collections.singletonList("ENUM_YES"))))
            .andExpect(jsonPath("$[0].parameters.screenData.components[?(@.id == 'SelRea')].model.values[0].value", is(Collections.singletonList("1"))))
            .andReturn();
  }

  /**
   * Test of screen restriction - Change label
   *
   * @throws Exception Test error
   */
  @Test
  public void testChangeLabel() throws Exception {
    // Add restriction
    addRestriction("INSERT", "CrtTstLeft", "Txt", "label", "Tira patraaaaas");
    // Check screen
    checkAttributeComponent("Txt", "label", "Tira patraaaaas");
  }

  /**
   * Test of screen restriction - Change label
   *
   * @throws Exception Test error
   */
  @Test
  public void testSetReadonly() throws Exception {
    // Add restriction
    addRestriction("INSERT", "CrtTstLeft", "Txt", "readonly", "true");
    // Check screen
    checkAttributeComponent("Txt", "readonly", true);
  }

  /**
   * Test of screen restriction - Optional
   *
   * @throws Exception Test error
   */
  @Test
  public void testOptional() throws Exception {
    // Add restriction
    addRestriction("INSERT", "CrtTstLeft", "Txt", "optional", "true");
    // Check screen
    checkAttributeComponent("Txt", "optional", true);
  }

  /**
   * Test of screen restriction - Printable
   *
   * @throws Exception Test error
   */
  @Test
  public void testPrintable() throws Exception {
    // Add restriction
    addRestriction("INSERT", "CrtTstLeft", "ButBck",  "printable", "false");

    // Check screen
    checkAttributeComponent("ButBck", "printable", false);
  }

  /**
   * Test of screen restriction - Checked
   *
   * @throws Exception Test error
   */
  @Test
  public void testChecked() throws Exception {
    // Add restriction
    addRestriction("INSERT", "CrtTstLeft", "ButBck",  "checked", "true");

    // Check screen
    checkAttributeComponent("ButBck", "checked", true);
  }

  /**
   * Test of screen restriction - Help label
   *
   * @throws Exception Test error
   */
  @Test
  public void testHelpLabel() throws Exception {
    // Add restriction
    addRestriction("INSERT", "CrtTstLeft", "ButBck",  "help", "Ayudaaaaaaaaaaaaaaaa");

    // Check screen
    checkAttributeComponent("ButBck", "help", "Ayudaaaaaaaaaaaaaaaa");
  }

  /**
   * Test of screen restriction - Style
   *
   * @throws Exception Test error
   */
  @Test
  public void testStyle() throws Exception {
    // Add restriction
    addRestriction("INSERT", "CrtTstLeft", "ButBck", "style", "claseDeLaMuerte otraClaseChula");

    // Check screen
    checkAttributeComponent("ButBck", "style", "claseDeLaMuerte otraClaseChula");
  }

  /**
   * Test of screen restriction - Initial load
   *
   * @throws Exception Test error
   */
  @Test
  public void testInitialLoad() throws Exception {
    // Add restrictions
    addRestriction("INSERT", "CrtTstLeft", "Txt", "initialLoad", "value");
    addRestriction("INSERT", "CrtTstLeft", "Txt", "targetAction", "TestComponentInitialLoadValue");

    MvcResult mvcResult = mockMvc.perform(post("/action/screen-data")
      .header("Authorization", AUTHORIZATION_HEADER)
      .contentType(MediaType.APPLICATION_JSON)
      .content(screenParameters)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].parameters.screenData.components[?(@.id == 'Txt')].controller.initialLoad", is(Collections.singletonList("value"))))
      .andExpect(jsonPath("$[0].parameters.screenData.components[?(@.id == 'Txt')].controller.targetAction", is(Collections.singletonList("TestComponentInitialLoadValue"))))
      .andExpect(jsonPath("$[0].parameters.screenData.components[?(@.id == 'Txt')].model.records", is(Collections.singletonList(1))))
      .andReturn();

    String result = mvcResult.getResponse().getContentAsString();
    logger.debug(result);
  }

  /**
   * Test of screen restriction - Style
   *
   * @throws Exception Test error
   */
  @Test
  public void testAttributeNotForComponent() throws Exception {
    // Add restriction
    addRestriction("INSERT", "CrtTstLeft", "Txt", "editable", "true");

    // Check screen
    checkAttributeComponentDoesntExist("Txt", "editable");
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

    Assert.assertEquals("value1", getParameter("parameter1"));
    Assert.assertEquals("value2", getParameter("parameter2"));
    Assert.assertEquals("value3", getParameter("parameter3"));
    Assert.assertEquals("value4", getParameter("parameter4"));
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

    Assert.assertEquals("value4", getParameter("parameter1"));
    Assert.assertEquals("value3", getParameter("parameter2"));
    Assert.assertEquals("value2", getParameter("parameter3"));
    Assert.assertEquals("value1", getParameter("parameter4"));
  }

  /**
   * Test that the parameters from session are correctly removed
   */
  @Test
  public void testRemoveSessionValues() throws Exception {
    //Add values first
    testSetSessionValues();

    removeParameter("parameter1");
    removeParameter("parameter2");
    removeParameter("parameter3");
    removeParameter("parameter4");

    assertTrue(getParameter("parameter1").isEmpty());
    assertTrue(getParameter("parameter2").isEmpty());
    assertTrue(getParameter("parameter3").isEmpty());
    assertTrue(getParameter("parameter4").isEmpty());
  }

  /**
   * Test the setter and getter annotations for session parameters
   */
  //@Test
  public void testSessionAnnotations() throws Exception {
    toSessionMethodAnnotation();
    Assert.assertEquals("toSessionMethod", getParameter("param"));

    toSessionParameterAnnotation("toSessionParameter");
    Assert.assertEquals("toSessionParameter", getParameter("param"));

    setParameter("param", "fromSessionValue");

    Assert.assertEquals("fromSessionValue", fromSessionMethodAnnotation());
    Assert.assertEquals("fromSessionValue", fromSessionParameterAnnotation("session"));
  }

  /**
   * Perform screen data action to check Json controller attributes
   *
   * @param componentId component ID
   * @param attribute component attribute
   * @param value attribute value
   * @throws Exception UnsupportedEncodingException exception
   */
  private void checkAttributeComponent(String componentId, String attribute, Object value) throws Exception {
    checkAttributeComponentMatcher(componentId, attribute, is(Collections.singletonList(value)));
  }

  /**
   * Perform screen data action to check Json controller attributes
   *
   * @param componentId component ID
   * @param attribute component attribute
   * @throws Exception UnsupportedEncodingException exception
   */
  private void checkAttributeComponentDoesntExist(String componentId, String attribute) throws Exception {
    checkAttributeComponentMatcher(componentId, attribute, is(Collections.EMPTY_LIST));
  }

  private void checkAttributeComponentMatcher(String componentId, String attribute, Matcher matcher) throws Exception {
    // Get screen action and check attribute
    MvcResult mvcResult = mockMvc.perform(post("/action/screen-data")
      .header("Authorization", AUTHORIZATION_HEADER)
      .contentType(MediaType.APPLICATION_JSON)
      .content(screenParameters)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath(String.format("$[0].parameters.screenData.components[?(@.id == '%s')].controller.%s", componentId, attribute), matcher))
      .andReturn();

    String result = mvcResult.getResponse().getContentAsString();
    logger.debug(result);
  }

  /**
   * Adds a value to the session with a custom annotation on the method.
   *
   * @return To session
   */
  @ToSession(name = "param")
  private String toSessionMethodAnnotation() {
    return "toSessionMethod";
  }

  /**
   * Adds a value to the session with a custom annotation on the parameter.
   *
   * @param toSession To session
   */
  private void toSessionParameterAnnotation(@ToSession(name = "param") String toSession) {
  }

  /**
   * Retrieves a value from the session with a custom annotation on the method.
   *
   * @return From session
   */
  @FromSession(name = "param")
  private String fromSessionMethodAnnotation() {
    return "";
  }

  /**
   * Retrieves a value from the session with a custom annotation on the parameter.
   *
   * @param fromSession From session
   * @return From session
   */
  private String fromSessionParameterAnnotation(@FromSession(name = "param") String fromSession) {
    return fromSession;
  }

}