package com.almis.awe.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import com.almis.awe.model.dto.MaintainResultDetails;
import com.almis.awe.model.type.MaintainType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Joiner;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(username = "test", password = "test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Ignore("Generic class for database testing")
public class MaintainTest extends TestUtil {

  // Logger
  private static Logger logger = LogManager.getLogger(MaintainTest.class);

	/*
	 * Tables - Test schema Fields - Test functions with doubles (now is casted to Long always)
	 */

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void setup() throws Exception {
    super.setup();
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testSimpleSingleInsert() throws Exception {
    String maintainName = "SimpleSingleInsert";
    String variables = "";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.INSERT, 1l)
    });

    // Clean the mess
    cleanUp("CleanUp");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testSimpleSingleInsertFromVariableValue() throws Exception {
    for (int i = 0; i < 5; i++) {
      String maintainName = "SimpleSingleInsertFromVariableValue";
      String variables = "\"variable\":\"AWEBOOT-TEST-" + i + "\",";
      String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
      MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
              .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
              .accept("application/json"))
              .andExpect(status().isOk())
              .andExpect(content().json(expected))
              .andReturn();
      String result = mvcResult.getResponse().getContentAsString();
      logger.info(result);
      assertResultJson(maintainName, result, 1, new MaintainResultDetails[] {
              new MaintainResultDetails(MaintainType.INSERT, 1l)
      });

      // Clean the mess
      cleanUp("CleanUp");
    }
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testSingleInsertWithSequence() throws Exception {
    String maintainName = "SingleInsertWithSequence";
    String variables = "";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.INSERT, 1l)
    });

    // Clean the mess
    cleanUp("CleanUpSequence");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testMultipleInsertWithSequence() throws Exception {
    String maintainName = "MultipleInsertWithSequence";
    String variables = "\"variable\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\"],";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 2, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.INSERT, 1l),
            new MaintainResultDetails(MaintainType.INSERT, 1l)
    });

    // Clean the mess
    cleanUp("CleanUpSequence");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testSimpleSingleUpdate() throws Exception {
    String maintainName = "SimpleSingleInsert";
    String variables = "";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.INSERT, 1l)
    });

    maintainName = "SimpleSingleUpdate";
    variables = "";
    expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.UPDATE, 1l)
    });

    // Clean the mess
    cleanUp("CleanUp");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testSimpleSingleDelete() throws Exception {
    String maintainName = "CleanUp";
    String variables = "";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"DELETE\",\"rowsAffected\":0}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
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
  @Test
  @WithMockUser(username = "test", password = "test")
  public void testSimpleSingleInsertAudit() throws Exception {
    String maintainName = "SimpleSingleInsertAudit";
    String variables = "";
    setParameter("user", "test");
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .session(session)
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 2, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.INSERT, 1l),
            new MaintainResultDetails(MaintainType.AUDIT, 1l)
    });

    // Clean the mess
    cleanUp("CleanUp");
  }

  /**
   * Launch a simple single insert from variable
   * @throws Exception
   */
  private void launchSimpleSingleInsertFromVariable() throws Exception {
    for (int i = 0; i < 5; i++) {
      String maintainName = "SimpleSingleInsertFromVariable";
      String variables = "\"variable\":\"AWEBOOT-TEST-" + i + "\",";
      String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
      MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
        .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
        .accept("application/json"))
        .andExpect(status().isOk())
        .andExpect(content().json(expected))
        .andReturn();
      String result = mvcResult.getResponse().getContentAsString();
      logger.info(result);
      assertResultJson(maintainName, result, 1, new MaintainResultDetails[] {
        new MaintainResultDetails(MaintainType.INSERT, 1l)
      });
    }
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  @WithMockUser(username = "test", password = "test")
  public void testSingleUpdateWithVariableListAudit() throws Exception {
    launchSimpleSingleInsertFromVariable();

    String maintainName = "SingleUpdateWithVariableListAudit";
    String variables = "\"variable\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\", \"AWEBOOT-TEST-2\", \"AWEBOOT-TEST-3\", \"AWEBOOT-TEST-4\"],";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"UPDATE\",\"rowsAffected\":5},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 6, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.UPDATE, 5l),
            new MaintainResultDetails(MaintainType.AUDIT, 1l),
            new MaintainResultDetails(MaintainType.AUDIT, 1l),
            new MaintainResultDetails(MaintainType.AUDIT, 1l),
            new MaintainResultDetails(MaintainType.AUDIT, 1l),
            new MaintainResultDetails(MaintainType.AUDIT, 1l)
    });

    // Clean the mess
    cleanUp("CleanUp");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  @WithMockUser(username = "test", password = "test")
  public void testSingleUpdateWithVariableListAuditBatched() throws Exception {
    launchSimpleSingleInsertFromVariable();

    String maintainName = "SingleUpdateWithVariableListAuditBatched";
    String variables = "\"variable\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\", \"AWEBOOT-TEST-2\", \"AWEBOOT-TEST-3\", \"AWEBOOT-TEST-4\"],";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"UPDATE\",\"rowsAffected\":5},{\"operationType\":\"AUDIT\",\"rowsAffected\":5}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 2, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.UPDATE, 5l),
            new MaintainResultDetails(MaintainType.AUDIT, 5l)
    });

    // Clean the mess
    cleanUp("CleanUp");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testMultipleUpdate() throws Exception {
    launchSimpleSingleInsertFromVariable();

    String maintainName = "MultipleUpdate";
    String variables = "\"variable\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\", \"AWEBOOT-TEST-2\", \"AWEBOOT-TEST-3\", \"AWEBOOT-TEST-4\"],";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 5, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.UPDATE, 1l),
            new MaintainResultDetails(MaintainType.UPDATE, 1l),
            new MaintainResultDetails(MaintainType.UPDATE, 1l),
            new MaintainResultDetails(MaintainType.UPDATE, 1l),
            new MaintainResultDetails(MaintainType.UPDATE, 1l)
    });

    // Clean the mess
    cleanUp("CleanUp");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  @WithMockUser(username = "test", password = "test")
  public void testMultipleUpdateAudit() throws Exception {
    launchSimpleSingleInsertFromVariable();

    String maintainName = "MultipleUpdateAudit";
    String variables = "\"variable\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\", \"AWEBOOT-TEST-2\", \"AWEBOOT-TEST-3\", \"AWEBOOT-TEST-4\"],";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 10, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.UPDATE, 1l),
            new MaintainResultDetails(MaintainType.AUDIT, 1l),
            new MaintainResultDetails(MaintainType.UPDATE, 1l),
            new MaintainResultDetails(MaintainType.AUDIT, 1l),
            new MaintainResultDetails(MaintainType.UPDATE, 1l),
            new MaintainResultDetails(MaintainType.AUDIT, 1l),
            new MaintainResultDetails(MaintainType.UPDATE, 1l),
            new MaintainResultDetails(MaintainType.AUDIT, 1l),
            new MaintainResultDetails(MaintainType.UPDATE, 1l),
            new MaintainResultDetails(MaintainType.AUDIT, 1l)
    });

    // Clean the mess
    cleanUp("CleanUp");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  @WithMockUser(username = "test", password = "test")
  public void testMultipleUpdateAuditBatched() throws Exception {
    launchSimpleSingleInsertFromVariable();

    String maintainName = "MultipleUpdateAuditBatched";
    String variables = "\"variable\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\", \"AWEBOOT-TEST-2\", \"AWEBOOT-TEST-3\", \"AWEBOOT-TEST-4\"],";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"UPDATE\",\"rowsAffected\":5},{\"operationType\":\"AUDIT\",\"rowsAffected\":5}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 2, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.UPDATE, 5l),
            new MaintainResultDetails(MaintainType.AUDIT, 5l)
    });

    // Clean the mess
    cleanUp("CleanUp");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testInsertUpdateDelete() throws Exception {
    String maintainName = "InsertUpdateDelete";
    String variables = "";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"DELETE\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 3, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.INSERT, 1l),
            new MaintainResultDetails(MaintainType.UPDATE, 1l),
            new MaintainResultDetails(MaintainType.DELETE, 1l)
    });

    // Clean the mess
    cleanUp("CleanUp");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testInsertUpdateDeleteWithVariables() throws Exception {
    String maintainName = "InsertUpdateDeleteWithVariables";
    String variables = "\"var1\": \"AWEBOOT-TEST-0\", \"var2\": \"AWEBOOT-TEST-1\",";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1},{\"operationType\":\"INSERT\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"DELETE\",\"rowsAffected\":2}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 5, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.INSERT, 1l),
            new MaintainResultDetails(MaintainType.INSERT, 1l),
            new MaintainResultDetails(MaintainType.UPDATE, 1l),
            new MaintainResultDetails(MaintainType.UPDATE, 1l),
            new MaintainResultDetails(MaintainType.DELETE, 2l)
    });

    // Clean the mess
    cleanUp("CleanUp");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testRollback() throws Exception {
    String maintainName = "TestRollback";
    String variables = "";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"There was a problem calling a maintain process\",\"title\":\"Error in maintain operation\",\"type\":\"error\"}},{\"type\":\"cancel\",\"parameters\":{}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables	+ "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);

    String queryName = "CheckRollback";
    expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":0,\"rows\":[]}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    mvcResult = mockMvc.perform(post("/action/data/" + queryName)
            .param("p", "{\"serverAction\":\"data\",\"targetAction\":\"" + queryName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    result = mvcResult.getResponse().getContentAsString();
    logger.info(result);

    // Clean the mess
    cleanUp("CleanUp");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testCommit() throws Exception {
    String maintainName = "TestCommit";
    String variables = "";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"There was a problem calling a maintain process\",\"title\":\"Error in maintain operation\",\"type\":\"error\"}},{\"type\":\"cancel\",\"parameters\":{}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables	+ "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);

    String queryName = "CheckRollback";
    expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"HISope\":\"test\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    mvcResult = mockMvc.perform(post("/action/data/" + queryName)
            .param("p", "{\"serverAction\":\"data\",\"targetAction\":\"" + queryName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    result = mvcResult.getResponse().getContentAsString();
    logger.info(result);

    // Clean the mess
    cleanUp("CleanUp");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testGridMultiple() throws Exception {
    String maintainName = "GridMultiple";
    String variables = "\"grid-RowTyp\": [\"INSERT\", \"INSERT\"], \"nam\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\"], \"act\":[0, 0],";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 2, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.INSERT, 1l),
            new MaintainResultDetails(MaintainType.INSERT, 1l)
    });

    // Clean the mess
    cleanUp("CleanUpSequence");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testGridMultipleInsertSingle() throws Exception {
    String maintainName = "GridMultiple";
    String variables = "\"grid-RowTyp\": [\"INSERT\"], \"nam\": [\"AWEBOOT-TEST-0\"], \"act\":[0],";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.INSERT, 1l)
    });

    // Clean the mess
    cleanUp("CleanUpSequence");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testIncludeTarget() throws Exception {
    String maintainName = "testInclude";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\",\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 3, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.INSERT, 1l),
            new MaintainResultDetails(MaintainType.UPDATE, 1l),
            new MaintainResultDetails(MaintainType.DELETE, 1l)
    });

    // Clean the mess
    cleanUp("CleanUpSequence");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testGridMultipleDeleteMultiple() throws Exception {
    // Clean the mess
    cleanUp("CleanUpSequence");

    String maintainName = "GridMultiple";
    String variables = "\"grid-RowTyp\": [\"INSERT\", \"INSERT\"], \"nam\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\"], \"act\":[0, 0],";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 2, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.INSERT, 1l),
            new MaintainResultDetails(MaintainType.INSERT, 1l)
    });

    List<String> keys = new ArrayList<String>();
    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);
    ObjectNode messageAction = (ObjectNode) resultList.get(1);
    ObjectNode messageParameters = (ObjectNode) messageAction.get("parameters");
    ArrayNode resultDetails = (ArrayNode) messageParameters.get("result_details");
    for (JsonNode resultDetail: resultDetails) {
      ObjectNode resultNode = (ObjectNode) resultDetail;
      ObjectNode parameterMap = (ObjectNode) resultNode.get("parameterMap");
      keys.add(parameterMap.get("varKey").asText());
    }

    maintainName = "GridMultiple";
    String key = "\"key\":[" + Joiner.on(", ").join(keys) + "],";
    variables = "\"grid-RowTyp\": [\"DELETE\", \"DELETE\"], \"nam\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\"], \"act\":[0, 0]," + key;
    expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
    assertResultJson(maintainName, result, 2, new MaintainResultDetails[] {
            new MaintainResultDetails(MaintainType.DELETE, 1l),
            new MaintainResultDetails(MaintainType.DELETE, 1l)
    });
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testServiceNoParams() throws Exception {
    String maintainName = "ServeNoParams";
    String variables = "";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been succesfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testServiceMessageParams() throws Exception {
    String maintainName = "ServeMessageParams";
    String variables = "";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"message\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testServiceTitleMessageParams() throws Exception {
    String maintainName = "ServeTitleMessageParams";
    String variables = "";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"message\",\"title\":\"title\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
            .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().json(expected))
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);
  }

  // *****************************************************************************************************************//
  // EMAIL TESTS
  // **************************************************************************************************************** //

  /**
   * Sends an email created with the AWE XML format
   *
   */
  @Test
  public void testXMLEmail() {
    String maintainName = "SchTskEmaRep";
    String variables = "";
    Exception ex = null;

    try {
      mockMvc.perform(post("/action/maintain/" + maintainName)
              .param("p", "{\"serverAction\":\"maintain\",\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
              .accept("application/json"))
              .andReturn();
    } catch (Exception e) {
      ex = e;
    }

    Assert.assertTrue(ex == null);
  }
}