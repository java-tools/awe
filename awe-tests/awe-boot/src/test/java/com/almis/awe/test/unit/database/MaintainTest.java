package com.almis.awe.test.unit.database;

import com.almis.awe.model.dto.MaintainResultDetails;
import com.almis.awe.model.type.MaintainType;
import com.almis.awe.service.MaintainService;
import com.almis.awe.test.unit.categories.NotHSQLDatabaseTest;
import com.almis.awe.test.unit.categories.NotMySQLDatabaseTest;
import com.almis.awe.test.unit.categories.NotOracleDatabaseTest;
import com.almis.awe.test.unit.categories.NotSQLServerDatabaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Joiner;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertSame;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@Log4j2
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MaintainTest extends AweSpringDatabaseTests {

  /**
   * Launch a maintain test
   * @param maintainName Maintain name
   * @param variables Variables
   * @param expected Expected value
   * @return Maintain result
   * @throws Exception
   */
  private String launchMaintain(String maintainName, String variables, String expected) throws Exception {
    return launchPostRequest("maintain", maintainName, variables, expected);
  }

  /**
   * Launch a query test
   * @param queryName Maintain name
   * @param variables Variables
   * @param expected Expected value
   * @return Maintain result
   * @throws Exception
   */
  private String launchQuery(String queryName, String variables, String expected) throws Exception {
    return launchPostRequest("data", queryName, variables, expected);
  }

  /**
   * Launch a request test
   * @param type Request type
   * @param name Name
   * @param variables Variables
   * @param expected Expected value
   * @return Maintain result
   * @throws Exception
   */
  private String launchPostRequest(String type, String name, String variables, String expected) throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/action/" + type + "/" + name)
      .header("Authorization", SESSION_ID)
      .session(session)
      .contentType(MediaType.APPLICATION_JSON)
      .content("{" + variables + "\"max\":30}")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(expected))
      .andReturn();
    return mvcResult.getResponse().getContentAsString();
  }

  /**
   * Launch to update hsql database
   *
   * @throws Exception Error in initialization
   */
  @Test
  public void initializeDatabase() throws Exception {
    String maintainName = "testDatabaseInitialization";

    String dt = "10/10/2008";  // Start date
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Calendar c = Calendar.getInstance();
    c.setTime(sdf.parse(dt));
    ArrayNode dates = JsonNodeFactory.instance.arrayNode();
    ArrayNode datesApp = JsonNodeFactory.instance.arrayNode();
    for (int i = 0; i < 2000; i++) {
      c.add(Calendar.DATE, 2);
      String date = sdf.format(c.getTime());
      dates.add(date);
      if (i < 89) {
        datesApp.add(date);
      }
    }
    String variables = "\"dat\": " + dates.toString() + ",\"datApp\":" + datesApp.toString() + ",";
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/maintain/" + maintainName)
      .header("Authorization", "16617f0d-97ee-4f6b-ad54-905d6ce3c328")
      .content("{\"targetAction\":\"" + maintainName + "\"," + variables + "\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"max\":30}")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(expected))
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.debug(result);
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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[]{
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
      String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
      String result = launchMaintain(maintainName, variables, expected);
      logger.debug(result);
      assertResultJson(maintainName, result, 1, new MaintainResultDetails[]{
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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[]{
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
  public void testGetNextSequence() throws Exception {
    // Clean the mess
    cleanUp("CleanUpSequence");
    MaintainService maintainService = getBean(MaintainService.class);

    Integer sequenceValue = maintainService.getNextSequenceValue("ThmKey");
    assertSame(20, sequenceValue);

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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 2, new MaintainResultDetails[]{
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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.INSERT, 1l)
    });

    maintainName = "SimpleSingleUpdate";
    variables = "";
    expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[]{
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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"DELETE\",\"rowsAffected\":0}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 1, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.DELETE, 0l)
    });
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testSimpleSingleInsertAudit() throws Exception {
    loginUser();

    String maintainName = "SimpleSingleInsertAudit";
    String variables = "";
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 2, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.INSERT, 1l),
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
  public void testSimpleSingleInsertAuditWithSequenceWithoutVariable() throws Exception {
    loginUser();

    String maintainName = "InsertAuditSequenceWithoutVariable";
    String variables = "\"nam\": \"AWEBOOT-TEST-0\", \"act\":0,";
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.info(result);
    assertResultJson(maintainName, result, 2, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.INSERT, 1l),
      new MaintainResultDetails(MaintainType.AUDIT, 1l)
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
  public void testSimpleSingleInsertMultipleAuditWithSequenceWithoutVariable() throws Exception {
    loginUser();

    String maintainName = "InsertAuditSequenceWithoutVariableMultiple";
    String variables = "\"nam\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\", \"AWEBOOT-TEST-2\"], \"act\":[0, 0, 1],";
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.info(result);
    assertResultJson(maintainName, result, 6, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.INSERT, 1l),
      new MaintainResultDetails(MaintainType.AUDIT, 1l),
      new MaintainResultDetails(MaintainType.INSERT, 1l),
      new MaintainResultDetails(MaintainType.AUDIT, 1l),
      new MaintainResultDetails(MaintainType.INSERT, 1l),
      new MaintainResultDetails(MaintainType.AUDIT, 1l)

    });

    // Clean the mess
    cleanUp("CleanUpSequence");
  }

  /**
   * Launch a simple single insert from variable
   *
   * @throws Exception
   */
  private void launchSimpleSingleInsertFromVariable() throws Exception {
    for (int i = 0; i < 5; i++) {
      String maintainName = "SimpleSingleInsertFromVariable";
      String variables = "\"variable\":\"AWEBOOT-TEST-" + i + "\",";
      String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
      String result = launchMaintain(maintainName, variables, expected);
      logger.debug(result);
      assertResultJson(maintainName, result, 1, new MaintainResultDetails[]{
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
  public void testSingleUpdateWithVariableListAudit() throws Exception {
    launchSimpleSingleInsertFromVariable();

    String maintainName = "SingleUpdateWithVariableListAudit";
    String variables = "\"variable\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\", \"AWEBOOT-TEST-2\", \"AWEBOOT-TEST-3\", \"AWEBOOT-TEST-4\"],";
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"UPDATE\",\"rowsAffected\":5},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 6, new MaintainResultDetails[]{
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
  public void testSingleUpdateWithVariableListAuditBatched() throws Exception {
    launchSimpleSingleInsertFromVariable();

    String maintainName = "SingleUpdateWithVariableListAuditBatched";
    String variables = "\"variable\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\", \"AWEBOOT-TEST-2\", \"AWEBOOT-TEST-3\", \"AWEBOOT-TEST-4\"],";
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"UPDATE\",\"rowsAffected\":5},{\"operationType\":\"AUDIT\",\"rowsAffected\":5}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 2, new MaintainResultDetails[]{
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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 5, new MaintainResultDetails[]{
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
  public void testMultipleUpdateAudit() throws Exception {
    launchSimpleSingleInsertFromVariable();

    String maintainName = "MultipleUpdateAudit";
    String variables = "\"variable\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\", \"AWEBOOT-TEST-2\", \"AWEBOOT-TEST-3\", \"AWEBOOT-TEST-4\"],";
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"AUDIT\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 10, new MaintainResultDetails[]{
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
  public void testMultipleUpdateAuditBatched() throws Exception {
    launchSimpleSingleInsertFromVariable();

    String maintainName = "MultipleUpdateAuditBatched";
    String variables = "\"variable\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\", \"AWEBOOT-TEST-2\", \"AWEBOOT-TEST-3\", \"AWEBOOT-TEST-4\"],";
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"UPDATE\",\"rowsAffected\":5},{\"operationType\":\"AUDIT\",\"rowsAffected\":5}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 2, new MaintainResultDetails[]{
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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"DELETE\",\"rowsAffected\":1}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 3, new MaintainResultDetails[]{
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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1},{\"operationType\":\"INSERT\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"UPDATE\",\"rowsAffected\":1},{\"operationType\":\"DELETE\",\"rowsAffected\":2}],\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 5, new MaintainResultDetails[]{
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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"There was a problem calling a maintain process\",\"title\":\"Error in maintain operation\",\"type\":\"error\"}},{\"type\":\"cancel\"}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);

    expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":0,\"rows\":[]}}},{\"type\":\"end-load\"}]";
    result = launchQuery("CheckRollback", variables, expected);
    logger.debug(result);

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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"There was a problem calling a maintain process\",\"title\":\"Error in maintain operation\",\"type\":\"error\"}},{\"type\":\"cancel\"}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);

    expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"HISope\":\"test\"}]}}},{\"type\":\"end-load\"}]";
    result = launchQuery("CheckRollback", variables, expected);
    logger.debug(result);

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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 4, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.INSERT, 1l),
      new MaintainResultDetails(MaintainType.AUDIT, 1l),
      new MaintainResultDetails(MaintainType.INSERT, 1l),
      new MaintainResultDetails(MaintainType.AUDIT, 1l)
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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 2, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.INSERT, 1l),
      new MaintainResultDetails(MaintainType.AUDIT, 1l)
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
  @Category(NotOracleDatabaseTest.class)
  public void testGridMultipleAutoIncrement() throws Exception {
    String maintainName = "GridMultipleAutoIncrement";
    String variables = "\"grid-RowTyp\": [\"INSERT\", \"INSERT\", \"UPDATE\", \"DELETE\"], \"id\": [null, null, 101, 100], \"name\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\", \"AWEBOOT-TEST-2\", \"AWEBOOT-TEST-3\"], \"email\":[\"test@test.es\", \"test2@test.es\", \"test3@test.es\", \"test4@test.es\"],";
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 4, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.INSERT, 1l),
      new MaintainResultDetails(MaintainType.INSERT, 1l),
      new MaintainResultDetails(MaintainType.UPDATE, 1l),
      new MaintainResultDetails(MaintainType.DELETE, 1l),
    });
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testIncludeTarget() throws Exception {
    String maintainName = "testInclude";
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, "", expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 3, new MaintainResultDetails[]{
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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 4, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.INSERT, 1l),
      new MaintainResultDetails(MaintainType.AUDIT, 1l),
      new MaintainResultDetails(MaintainType.INSERT, 1l),
      new MaintainResultDetails(MaintainType.AUDIT, 1l)
    });

    Set<String> keys = new HashSet<>();
    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);
    ObjectNode messageAction = (ObjectNode) resultList.get(1);
    ObjectNode messageParameters = (ObjectNode) messageAction.get("parameters");
    ArrayNode resultDetails = (ArrayNode) messageParameters.get("result_details");
    for (JsonNode resultDetail : resultDetails) {
      ObjectNode resultNode = (ObjectNode) resultDetail;
      ObjectNode parameterMap = (ObjectNode) resultNode.get("parameterMap");
      keys.add(parameterMap.get("varKey").asText());
    }

    maintainName = "GridMultiple";
    String key = "\"key\":[" + Joiner.on(", ").join(keys) + "],";
    variables = "\"grid-RowTyp\": [\"DELETE\", \"DELETE\"], \"nam\": [\"AWEBOOT-TEST-0\", \"AWEBOOT-TEST-1\"], \"act\":[0, 0]," + key;
    expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
    assertResultJson(maintainName, result, 4, new MaintainResultDetails[]{
      new MaintainResultDetails(MaintainType.DELETE, 1l),
      new MaintainResultDetails(MaintainType.AUDIT, 1l),
      new MaintainResultDetails(MaintainType.DELETE, 1l),
      new MaintainResultDetails(MaintainType.AUDIT, 1l)
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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"The selected maintain operation has been successfully performed\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"message\",\"title\":\"Operation successful\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
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
    String expected = "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"message\":\"message\",\"title\":\"title\",\"type\":\"ok\"}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testRetrieveDataAndInsertAfter() throws Exception {
    loginUser();

    String maintainName = "testRetrieveDataAndInsertAfter";
    String variables = "";
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"INSERT\",\"rowsAffected\":1},{\"operationType\":\"INSERT\",\"rowsAffected\":1},{\"operationType\":\"INSERT\",\"rowsAffected\":1},{\"operationType\":\"INSERT\",\"rowsAffected\":1},{\"operationType\":\"INSERT\",\"rowsAffected\":1},{\"operationType\":\"INSERT\",\"rowsAffected\":1}]}}]";
    String result = launchMaintain(maintainName, variables, expected);
    logger.debug(result);

    // Clean the mess
    cleanUp("CleanUp");
  }

  // *****************************************************************************************************************//
  // EMAIL TESTS
  // **************************************************************************************************************** //

  /**
   * Sends an email created with the AWE XML format
   */
  @Test
  public void testXMLEmail() {
    String maintainName = "SchTskEmaRep";
    String variables = "";
    Exception ex = null;

    try {
      mockMvc.perform(post("/action/maintain/" + maintainName)
        .header("Authorization", "16617f0d-97ee-4f6b-ad54-905d6ce3c328")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{" + variables + "\"max\":30}")
        .accept(MediaType.APPLICATION_JSON))
        .andReturn();
    } catch (Exception e) {
      ex = e;
    }

    Assert.assertTrue(ex == null);
  }
}