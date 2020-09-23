package com.almis.awe.test.unit.scheduler;

import com.almis.awe.test.unit.database.AweSpringDatabaseTests;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class used for testing queries through ActionController
 *
 * @author jbellon
 */
@TestMethodOrder(Alphanumeric.class)
@Log4j2
@TestPropertySource({
  "classpath:hsql.properties"
})
public class SchedulerQueriesTest extends AweSpringDatabaseTests {

  /**
   * Asserts the JSON in the response
   *
   * @param queryName    Query name
   * @param result       Result
   * @param expectedRows Expected rows
   * @param page         Page
   * @param totalPages   Total pages
   * @param records      Total records
   * @return Result list
   * @throws Exception Test error
   */
  public ArrayNode assertResultJson(String queryName, String result, int expectedRows, int page, int totalPages, int records) throws Exception {
    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);
    ObjectNode fillAction = (ObjectNode) resultList.get(0);
    assertEquals("fill", fillAction.get("type").textValue());
    ObjectNode fillParameters = (ObjectNode) fillAction.get("parameters");
    assertEquals(1, fillParameters.size());
    ObjectNode dataList = (ObjectNode) fillParameters.get("datalist");
    assertEquals(page, dataList.get("page").asInt());
    ArrayNode dataListRows = (ArrayNode) dataList.get("rows");
    assertEquals(expectedRows, dataListRows.size());
    if (totalPages > -1) {
      assertTrue(totalPages <= dataList.get("total").asInt());
    }
    if (records > -1) {
      assertTrue(records <= dataList.get("records").asInt());
    }

    ObjectNode endLoad = (ObjectNode) resultList.get(1);
    assertEquals("end-load", endLoad.get("type").textValue());

    logger.debug("--------------------------------------------------------------------------------------");
    logger.debug("There are " + dataListRows.size() + " rows as a result of launching query " + queryName);
    logger.debug("--------------------------------------------------------------------------------------");

    return dataListRows;
  }

  /**
   * Performs the mock request and returns the response as a string
   *
   * @param queryName Query ID
   * @param variables Variables
   * @param expected  Expected result
   * @return Output
   * @throws Exception Error performing request
   */
  private String performRequest(String queryName, String variables, String expected) throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/action/data/" + queryName)
      .header("Authorization", "16617f0d-97ee-4f6b-ad54-905d6ce3c328")
      .content("{" + variables + "}")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .session(session))
      .andExpect(status().isOk())
      .andExpect(content().json(expected))
      .andReturn();
    return mvcResult.getResponse().getContentAsString();
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testExecutionsToPurge() throws Exception {
    String queryName = "getExecutionsToPurge";
    String variables = "\"taskId\":2,\"executions\":5";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"id\":1,\"executionId\":5},{\"id\":2,\"executionId\":4},{\"id\":3,\"executionId\":3},{\"id\":4,\"executionId\":2},{\"id\":5,\"executionId\":1}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, expected);
    logger.warn(result);
    assertResultJson(queryName, result, 5, 1, 1, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testExecutionsToPurgeBig() throws Exception {
    String queryName = "getExecutionsToPurge";
    String variables = "\"taskId\":2,\"executions\":12";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":0,\"rows\":[]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, expected);
    logger.warn(result);
    assertResultJson(queryName, result, 0, 1, 1, 0);
  }
}
