package com.almis.awe.test.unit.rest;

import com.almis.awe.model.details.MaintainResultDetails;
import com.almis.awe.model.type.MaintainType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@Log4j2
public class ScreenRestrictionTest extends AweSpringRestTests {

  private String sessionToken = "e6144dad-6e67-499e-b74a-d1e600732e11";

  /*
   * Tables - Test schema Fields - Test functions with doubles (now is casted to Long always)
   */

  /**
   * Initializes json mapper for tests
   *
   * @throws Exception error updating user
   */
  @Before
  public void setup() throws Exception {
    super.setup();

    // Clean up
    cleanUp("CleanUpScreenRestriction");
  }

  /**
   * Close and logout
   *
   * @throws Exception error updating user
   */
  @After
  public void clean() throws Exception {
    // Clean up
    cleanUp("CleanUpScreenRestriction");
  }

  /**
   * Restrict an option and its children
   *
   * @param option Option node
   */
  private void restrictOption(ObjectNode option, String optionName, boolean restrict) {
    String searchFor = optionName;
    if (option.has("name") && optionName.equalsIgnoreCase(option.get("name").textValue()) ||
      "*".equalsIgnoreCase(optionName)) {
      option.put("restricted", restrict);
      searchFor = "*";
    }

    if (option.has("options")) {
      ArrayNode options = (ArrayNode) option.get("options");
      for (JsonNode childNode : options) {
        ObjectNode childOption = (ObjectNode) childNode;
        restrictOption(childOption, searchFor, restrict);
      }
    }
  }

  /**
   * Add a restriction
   *
   * @throws Exception Test error
   */
  private void addRestriction(String operation, String user, String profile, String option, String value) throws Exception {
    String maintainName = "updateScreenRestriction";
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1}]}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
      .header("Authorization", sessionToken)
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"module\":\"Test\",\"language\":\"en\",\"RefreshTime\":null,\"site\":\"Madrid\",\"database\":\"awemadora01\",\"theme\":\"sunset\",\"SetAutoload\":\"0\",\"GrdScrAccLst\":1,\"GrdScrAccLst.data\":{\"max\":30,\"page\":1,\"sort\":[]},\"IdeAweScrRes\":[\"\"],\"IdeAweScrRes.selected\":null,\"Opt\":[\"" + option + "\"],\"Opt.selected\":null,\"IdeOpe\":[" + user + "],\"IdeOpe.selected\":null,\"IdePro\":[" + profile + "],\"IdePro.selected\":null,\"AccMod\":[\"" + value + "\"],\"AccMod.selected\":null,\"Act\":[\"1\"],\"Act.selected\":null,\"GrdScrAccLst-id\":[\"new-row-1\"],\"GrdScrAccLst-RowTyp\":[\"" + operation + "\"],\"PrnScr\":\"ScrCnf\",\"CrtScr\":null,\"UsrPrn\":null,\"ActPrn\":\"2\",\"FmtPrn\":\"PDF\",\"CrtAct\":null,\"CrtUsr\":null,\"DblFmtPrn\":\"0\",\"TypPrn\":\"1\",\"CrtPro\":null,\"max\":30}")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(expected))
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    //logger.debug(result);
    assertResultJson(maintainName, result, 2, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.valueOf(operation), 1l),
      new MaintainResultDetails(MaintainType.AUDIT, 1l)
    });
  }

  /**
   * Test of option restriction
   *
   * @return parameters
   * @throws Exception Test error
   */
  private String prepareOptions(String optionName, String value, String previousOptions) throws Exception {
    // Define button controller
    String options = previousOptions != null ? previousOptions : readFileAsText("menu/menu.json");

    // Filter options
    if (optionName != null) {
      ObjectNode optionsObject = (ObjectNode) objectMapper.readTree(options);
      restrictOption(optionsObject, optionName, "R".equalsIgnoreCase(value));
      options = optionsObject.toString();
    }
    return options;
  }

  /**
   * Test of option restriction
   *
   * @throws Exception Test error
   */
  private void testOptionRestriction(String user, String profile, String optionName, String value, String parameters, String userSession, String restrictionSession, String profileSession) throws Exception {

    // Add restriction
    addRestriction("INSERT", user, profile, optionName, value);
    logger.debug(setParameter("user", userSession));
    logger.debug(setParameter("restriction", restrictionSession));
    logger.debug(setParameter("profile", profileSession));

    // Check screen
    String expected = "[{\"type\":\"change-menu\",\"parameters\":" + parameters + "},{\"type\":\"end-load\"}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/refresh-menu")
      .header("Authorization", sessionToken)
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"view\":\"report\"}")
      .accept(MediaType.APPLICATION_JSON)
      .session(session))
      .andExpect(status().isOk())
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.debug(result);
    logger.debug(expected);
  }

  /**
   * Test of option restriction
   *
   * @throws Exception Test error
   */
  private void testOptionRestriction(String user, String profile, String optionName, String value, String parameters) throws Exception {
    testOptionRestriction(user, profile, optionName, value, parameters, "test", "administrator", "ADM");
  }

  /**
   * Test of screen restriction
   *
   * @throws Exception Test error
   */
  @Test
  public void testRestrictHelpAll() throws Exception {
    String parameters = prepareOptions("images/flags/help", "R", null);
    testOptionRestriction(null, null, "images/flags/help", "R", parameters);
  }

  /**
   * Test of screen restriction
   *
   * @throws Exception Test error
   */
  @Test
  public void testRestrictToolsAll() throws Exception {
    String parameters = prepareOptions("tools", "R", null);
    testOptionRestriction(null, null, "tools", "R", parameters);
  }

  /**
   * Test of screen restriction - Change label
   *
   * @throws Exception Test error
   */
  @Test
  public void testSomeRestrictions() throws Exception {
    // Add restriction to settings
    String parameters = prepareOptions("settings", "R", null);
    testOptionRestriction(null, null, "settings", "R", parameters);

    // Remove restriction from security
    parameters = prepareOptions("security", "A", parameters);
    testOptionRestriction(null, null, "security", "A", parameters);
  }

  /**
   * Test of screen restriction
   *
   * @throws Exception Test error
   */
  @Test
  public void testEncryptToolUser() throws Exception {
    // Add restriction to user 'pgarcia' and check with 'test'
    String parameters = prepareOptions(null, null, null);
    testOptionRestriction("3", null, "encrypt-tools", "R", parameters);

    // Add restriction to user 'pgarcia' and check with 'pgarcia'
    parameters = prepareOptions("encrypt-tools", "R", null);
    testOptionRestriction("3", null, "encrypt-tools", "R", parameters, "pgarcia", "administrator", "ADM");
  }

  /**
   * Test of screen restriction
   *
   * @throws Exception Test error
   */
  @Test
  public void testEncryptToolProfile() throws Exception {
    // Add restriction to profile 'administrator' and check with 'test'
    String parameters = prepareOptions("sites", "R", null);
    testOptionRestriction(null, "1", "sites", "R", parameters);

    // Add restriction to profile 'administrator' and check with 'dfuentes'
    testOptionRestriction(null, "1", "sites", "R", parameters, "dfuentes", "administrator", "ADM");
  }
}