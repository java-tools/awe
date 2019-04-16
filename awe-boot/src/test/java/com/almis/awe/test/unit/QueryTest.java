package com.almis.awe.test.unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
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
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(username = "test", password = "test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore("Generic class for database testing")
public class QueryTest extends TestUtil {

  private static final String DATABASE = "aweora2";
  // Logger
  private static Logger logger = LogManager.getLogger(QueryTest.class);

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void setup() throws Exception {
    super.setup();
  }

  /**
   * Asserts the JSON in the response
   *
   * @param queryName    query name
   * @param result       Result
   * @param expectedRows Expected rows
   * @return Result list
   * @throws Exception Test error
   */
  private ArrayNode assertResultJson(String queryName, String result, int expectedRows) throws Exception {
    return assertResultJson(queryName, result, expectedRows, 1, 1, expectedRows);
  }

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
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

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
   * @param database  Database
   * @param expected  Expected result
   * @return Output
   * @throws Exception Error performing request
   */
  private String performRequest(String queryName, String variables, String database, String expected) throws Exception {
    setParameter("database", database);
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
   * Performs the mock request and returns the response as a string
   *
   * @param queryName Query ID
   * @param variables Variables
   * @param database  Database
   * @return Output
   * @throws Exception Error performing request
   */
  private String performRequestWithoutCheckExpected(String queryName, String variables, String database) throws Exception {
    setParameter("database", database);
    MvcResult mvcResult = mockMvc.perform(post("/action/data/" + queryName)
      .header("Authorization", "16617f0d-97ee-4f6b-ad54-905d6ce3c328")
      .content("{" + variables + "}")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .session(session))
      .andExpect(status().isOk())
      .andReturn();
    return mvcResult.getResponse().getContentAsString();
  }

  /**
   * Performs the mock request and returns the response as a string
   *
   * @param queryName Query ID
   * @param variables Variables
   * @param database  Database
   * @return Output
   * @throws Exception Error performing request
   */
  private String performRequest(String queryName, String variables, String database) throws Exception {
    setParameter("database", database);
    MvcResult mvcResult = mockMvc.perform(post("/action/data/" + queryName)
      .header("Authorization", "16617f0d-97ee-4f6b-ad54-905d6ce3c328")
      .content("{" + variables + "}")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .session(session))
      .andExpect(status().isOk())
      .andReturn();
    return mvcResult.getResponse().getContentAsString();
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQuerySimpleGetAll() throws Exception {
    String queryName = "SimpleGetAll";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":12,\"rows\":[{\"Ide\":6},{\"Ide\":8},{\"Ide\":9},{\"Ide\":6},{\"Ide\":8},{\"Ide\":9},{\"Ide\":7},{\"Ide\":15},{\"Ide\":16},{\"Ide\":7},{\"Ide\":15},{\"Ide\":16}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 12, 1, 1, 12);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQuerySimpleGetValue() throws Exception {
    String queryName = "SimpleGetValue";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"IdeSit\":17}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQuerySubqueryInTable() throws Exception {
    String queryName = "SubqueryInTable";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":12,\"rows\":[{\"Asdf\":6},{\"Asdf\":8},{\"Asdf\":9},{\"Asdf\":6},{\"Asdf\":8},{\"Asdf\":9},{\"Asdf\":7},{\"Asdf\":15},{\"Asdf\":16},{\"Asdf\":7},{\"Asdf\":15},{\"Asdf\":16}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 12);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryTwoTables() throws Exception {
    String queryName = "QueryTwoTables";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":8,\"rows\":[{\"IdeModPro\":62,\"Nam\":\"Base\"},{\"IdeModPro\":65,\"Nam\":\"Base\"},{\"IdeModPro\":74,\"Nam\":\"Base\"},{\"IdeModPro\":937,\"Nam\":\"Base\"},{\"IdeModPro\":62,\"Nam\":\"Test\"},{\"IdeModPro\":65,\"Nam\":\"Test\"},{\"IdeModPro\":74,\"Nam\":\"Test\"},{\"IdeModPro\":937,\"Nam\":\"Test\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 8);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFieldFunctions() throws Exception {
    String queryName = "TestFieldFunctions";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"Sum\":24,\"Max\":2584,\"Avg\":10.16666666666666666666666666666666666667,\"Cnt\":12,\"Min\":60}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE);
    logger.debug(expected);

    ArrayNode dataListRows = assertResultJson(queryName, result, 1);

    // Test all keys
    for (JsonNode element : dataListRows) {
      ObjectNode component = (ObjectNode) element;
      assertEquals(24, component.get("Sum").asInt());
      assertEquals(2584, component.get("Max").asInt());
      assertEquals(10, component.get("Avg").asInt());
      assertEquals(12, component.get("Cnt").asInt());
      assertEquals(60, component.get("Min").asInt());
      logger.debug(component.toString());
    }

    logger.debug("-------------------------------------------");
    logger.debug("There are " + dataListRows.size() + " rows as a result of launching query " + queryName);
    logger.debug("-------------------------------------------");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQuerySubqueryInField() throws Exception {
    String queryName = "SubqueryInField";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":12,\"rows\":[{\"subquery\":\"Onate\"},{\"subquery\":\"Onate\"},{\"subquery\":\"Onate\"},{\"subquery\":\"Onate\"},{\"subquery\":\"Onate\"},{\"subquery\":\"Onate\"},{\"subquery\":\"Onate\"},{\"subquery\":\"Onate\"},{\"subquery\":\"Onate\"},{\"subquery\":\"Onate\"},{\"subquery\":\"Onate\"},{\"subquery\":\"Onate\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 12);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQuerySubqueryInFieldDistinct() throws Exception {
    String queryName = "SubqueryInFieldDistinct";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"subquery\":\"Onate\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryValueInField() throws Exception {
    String queryName = "QueryValueInField";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"one\":\"1\",\"IdeModPro\":62,\"id\":1},{\"one\":\"1\",\"IdeModPro\":65,\"id\":2},{\"one\":\"1\",\"IdeModPro\":74,\"id\":3},{\"one\":\"1\",\"IdeModPro\":937,\"id\":4}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 4);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryVariableInField() throws Exception {
    String queryName = "QueryVariableInField";
    String variables = "\"variable\": 1";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"IdeModPro\":62,\"1\":1},{\"IdeModPro\":65,\"1\":1},{\"IdeModPro\":74,\"1\":1},{\"IdeModPro\":937,\"1\":1}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    logger.debug(result);
    assertResultJson(queryName, result, 4);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryStaticVariableInField() throws Exception {
    String queryName = "QueryStaticVariableInField";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"IdeModPro\":62,\"1\":1},{\"IdeModPro\":65,\"1\":1},{\"IdeModPro\":74,\"1\":1},{\"IdeModPro\":937,\"1\":1}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 4);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQuerySimpleUnion() throws Exception {
    String queryName = "SimpleUnion";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":3,\"rows\":[{\"miau\":\"com.microsoft.sqlserver.jdbc.SQLServerDriver\"},{\"miau\":\"com.sybase.jdbc3.jdbc.SybDriver\"},{\"miau\":\"oracle.jdbc.driver.OracleDriver\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 3);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQuerySimpleUnionAll() throws Exception {
    String queryName = "SimpleUnionAll";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":12,\"rows\":[{\"Drv\":\"com.sybase.jdbc3.jdbc.SybDriver\"},{\"Drv\":\"com.microsoft.sqlserver.jdbc.SQLServerDriver\"},{\"Drv\":\"oracle.jdbc.driver.OracleDriver\"},{\"Drv\":\"oracle.jdbc.driver.OracleDriver\"},{\"Drv\":\"com.microsoft.sqlserver.jdbc.SQLServerDriver\"},{\"Drv\":\"com.sybase.jdbc3.jdbc.SybDriver\"},{\"Drv\":\"com.sybase.jdbc3.jdbc.SybDriver\"},{\"Drv\":\"com.microsoft.sqlserver.jdbc.SQLServerDriver\"},{\"Drv\":\"oracle.jdbc.driver.OracleDriver\"},{\"Drv\":\"oracle.jdbc.driver.OracleDriver\"},{\"Drv\":\"com.microsoft.sqlserver.jdbc.SQLServerDriver\"},{\"Drv\":\"com.sybase.jdbc3.jdbc.SybDriver\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 12);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQuerySimpleLeftJoin() throws Exception {
    String queryName = "SimpleLeftJoin";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":2,\"rows\":[{\"thm_name\":null,\"db_name\":\"Base\"},{\"thm_name\":null,\"db_name\":\"Test\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 2);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQuerySimpleRightJoinWithAlias() throws Exception {
    String queryName = "SimpleRightJoinWithAlias";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":16,\"rows\":[{\"thm_name\":\"adminflare\",\"db_name\":null},{\"thm_name\":\"amazonia\",\"db_name\":null},{\"thm_name\":\"asphalt\",\"db_name\":null},{\"thm_name\":\"clean\",\"db_name\":null},{\"thm_name\":\"default\",\"db_name\":null},{\"thm_name\":\"dust\",\"db_name\":null},{\"thm_name\":\"eclipse\",\"db_name\":null},{\"thm_name\":\"fresh\",\"db_name\":null},{\"thm_name\":\"frost\",\"db_name\":null},{\"thm_name\":\"grass\",\"db_name\":null},{\"thm_name\":\"purple-hills\",\"db_name\":null},{\"thm_name\":\"silver\",\"db_name\":null},{\"thm_name\":\"sky\",\"db_name\":null},{\"thm_name\":\"sunny\",\"db_name\":null},{\"thm_name\":\"sunset\",\"db_name\":null},{\"thm_name\":\"white\",\"db_name\":null}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 16);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarEq() throws Exception {
    String queryName = "FilterField-Var-Eq";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"IdeAweAppPar\":10},{\"IdeAweAppPar\":7},{\"IdeAweAppPar\":8},{\"IdeAweAppPar\":9}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 4);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldNoTableVarEq() throws Exception {
    String queryName = "FilterFieldNoTable-Var-Eq";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"IdeAweAppPar\":7},{\"IdeAweAppPar\":8},{\"IdeAweAppPar\":9},{\"IdeAweAppPar\":10}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 4);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarNe() throws Exception {
    String queryName = "FilterField-Var-Ne";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":2,\"page\":1,\"records\":35,\"rows\":[{\"IdeAweAppPar\":1,\"id\":1},{\"IdeAweAppPar\":2,\"id\":2},{\"IdeAweAppPar\":3,\"id\":3},{\"IdeAweAppPar\":4,\"id\":4},{\"IdeAweAppPar\":5,\"id\":5},{\"IdeAweAppPar\":6,\"id\":6},{\"IdeAweAppPar\":11,\"id\":7},{\"IdeAweAppPar\":12,\"id\":8},{\"IdeAweAppPar\":13,\"id\":9},{\"IdeAweAppPar\":14,\"id\":10},{\"IdeAweAppPar\":15,\"id\":11},{\"IdeAweAppPar\":16,\"id\":12},{\"IdeAweAppPar\":17,\"id\":13},{\"IdeAweAppPar\":18,\"id\":14},{\"IdeAweAppPar\":19,\"id\":15},{\"IdeAweAppPar\":20,\"id\":16},{\"IdeAweAppPar\":21,\"id\":17},{\"IdeAweAppPar\":22,\"id\":18},{\"IdeAweAppPar\":23,\"id\":19},{\"IdeAweAppPar\":24,\"id\":20},{\"IdeAweAppPar\":25,\"id\":21},{\"IdeAweAppPar\":26,\"id\":22},{\"IdeAweAppPar\":27,\"id\":23},{\"IdeAweAppPar\":28,\"id\":24},{\"IdeAweAppPar\":29,\"id\":25},{\"IdeAweAppPar\":30,\"id\":26},{\"IdeAweAppPar\":31,\"id\":27},{\"IdeAweAppPar\":32,\"id\":28},{\"IdeAweAppPar\":33,\"id\":29},{\"IdeAweAppPar\":34,\"id\":30}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 30);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarGe() throws Exception {
    String queryName = "FilterField-Var-Ge";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":2,\"page\":1,\"records\":39,\"rows\":[{\"IdeAweAppPar\":1,\"id\":1},{\"IdeAweAppPar\":2,\"id\":2},{\"IdeAweAppPar\":3,\"id\":3},{\"IdeAweAppPar\":4,\"id\":4},{\"IdeAweAppPar\":5,\"id\":5},{\"IdeAweAppPar\":6,\"id\":6},{\"IdeAweAppPar\":7,\"id\":7},{\"IdeAweAppPar\":8,\"id\":8},{\"IdeAweAppPar\":9,\"id\":9},{\"IdeAweAppPar\":10,\"id\":10},{\"IdeAweAppPar\":11,\"id\":11},{\"IdeAweAppPar\":12,\"id\":12},{\"IdeAweAppPar\":13,\"id\":13},{\"IdeAweAppPar\":14,\"id\":14},{\"IdeAweAppPar\":15,\"id\":15},{\"IdeAweAppPar\":16,\"id\":16},{\"IdeAweAppPar\":17,\"id\":17},{\"IdeAweAppPar\":18,\"id\":18},{\"IdeAweAppPar\":19,\"id\":19},{\"IdeAweAppPar\":20,\"id\":20},{\"IdeAweAppPar\":21,\"id\":21},{\"IdeAweAppPar\":22,\"id\":22},{\"IdeAweAppPar\":23,\"id\":23},{\"IdeAweAppPar\":24,\"id\":24},{\"IdeAweAppPar\":25,\"id\":25},{\"IdeAweAppPar\":26,\"id\":26},{\"IdeAweAppPar\":27,\"id\":27},{\"IdeAweAppPar\":28,\"id\":28},{\"IdeAweAppPar\":29,\"id\":29},{\"IdeAweAppPar\":30,\"id\":30}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 30);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarGeDouble() throws Exception {
    String queryName = "FilterField-Var-GeDouble";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":2,\"page\":1,\"records\":35,\"rows\":[{\"IdeAweAppPar\":1,\"id\":1},{\"IdeAweAppPar\":2,\"id\":2},{\"IdeAweAppPar\":3,\"id\":3},{\"IdeAweAppPar\":4,\"id\":4},{\"IdeAweAppPar\":5,\"id\":5},{\"IdeAweAppPar\":6,\"id\":6},{\"IdeAweAppPar\":11,\"id\":7},{\"IdeAweAppPar\":12,\"id\":8},{\"IdeAweAppPar\":13,\"id\":9},{\"IdeAweAppPar\":14,\"id\":10},{\"IdeAweAppPar\":15,\"id\":11},{\"IdeAweAppPar\":16,\"id\":12},{\"IdeAweAppPar\":17,\"id\":13},{\"IdeAweAppPar\":18,\"id\":14},{\"IdeAweAppPar\":19,\"id\":15},{\"IdeAweAppPar\":20,\"id\":16},{\"IdeAweAppPar\":21,\"id\":17},{\"IdeAweAppPar\":22,\"id\":18},{\"IdeAweAppPar\":23,\"id\":19},{\"IdeAweAppPar\":24,\"id\":20},{\"IdeAweAppPar\":25,\"id\":21},{\"IdeAweAppPar\":26,\"id\":22},{\"IdeAweAppPar\":27,\"id\":23},{\"IdeAweAppPar\":28,\"id\":24},{\"IdeAweAppPar\":29,\"id\":25},{\"IdeAweAppPar\":30,\"id\":26},{\"IdeAweAppPar\":31,\"id\":27},{\"IdeAweAppPar\":32,\"id\":28},{\"IdeAweAppPar\":33,\"id\":29},{\"IdeAweAppPar\":34,\"id\":30}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 30);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarGeFloat() throws Exception {
    String queryName = "FilterField-Var-GeFloat";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":2,\"page\":1,\"records\":35,\"rows\":[{\"IdeAweAppPar\":1,\"id\":1},{\"IdeAweAppPar\":2,\"id\":2},{\"IdeAweAppPar\":3,\"id\":3},{\"IdeAweAppPar\":4,\"id\":4},{\"IdeAweAppPar\":5,\"id\":5},{\"IdeAweAppPar\":6,\"id\":6},{\"IdeAweAppPar\":11,\"id\":7},{\"IdeAweAppPar\":12,\"id\":8},{\"IdeAweAppPar\":13,\"id\":9},{\"IdeAweAppPar\":14,\"id\":10},{\"IdeAweAppPar\":15,\"id\":11},{\"IdeAweAppPar\":16,\"id\":12},{\"IdeAweAppPar\":17,\"id\":13},{\"IdeAweAppPar\":18,\"id\":14},{\"IdeAweAppPar\":19,\"id\":15},{\"IdeAweAppPar\":20,\"id\":16},{\"IdeAweAppPar\":21,\"id\":17},{\"IdeAweAppPar\":22,\"id\":18},{\"IdeAweAppPar\":23,\"id\":19},{\"IdeAweAppPar\":24,\"id\":20},{\"IdeAweAppPar\":25,\"id\":21},{\"IdeAweAppPar\":26,\"id\":22},{\"IdeAweAppPar\":27,\"id\":23},{\"IdeAweAppPar\":28,\"id\":24},{\"IdeAweAppPar\":29,\"id\":25},{\"IdeAweAppPar\":30,\"id\":26},{\"IdeAweAppPar\":31,\"id\":27},{\"IdeAweAppPar\":32,\"id\":28},{\"IdeAweAppPar\":33,\"id\":29},{\"IdeAweAppPar\":34,\"id\":30}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 30);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarLe() throws Exception {
    String queryName = "FilterField-Var-Le";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"IdeAweAppPar\":7,\"id\":1},{\"IdeAweAppPar\":8,\"id\":2},{\"IdeAweAppPar\":9,\"id\":3},{\"IdeAweAppPar\":10,\"id\":4}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 4);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarGt() throws Exception {
    String queryName = "FilterField-Var-Gt";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":2,\"page\":1,\"records\":35,\"rows\":[{\"IdeAweAppPar\":1,\"id\":1},{\"IdeAweAppPar\":2,\"id\":2},{\"IdeAweAppPar\":3,\"id\":3},{\"IdeAweAppPar\":4,\"id\":4},{\"IdeAweAppPar\":5,\"id\":5},{\"IdeAweAppPar\":6,\"id\":6},{\"IdeAweAppPar\":11,\"id\":7},{\"IdeAweAppPar\":12,\"id\":8},{\"IdeAweAppPar\":13,\"id\":9},{\"IdeAweAppPar\":14,\"id\":10},{\"IdeAweAppPar\":15,\"id\":11},{\"IdeAweAppPar\":16,\"id\":12},{\"IdeAweAppPar\":17,\"id\":13},{\"IdeAweAppPar\":18,\"id\":14},{\"IdeAweAppPar\":19,\"id\":15},{\"IdeAweAppPar\":20,\"id\":16},{\"IdeAweAppPar\":21,\"id\":17},{\"IdeAweAppPar\":22,\"id\":18},{\"IdeAweAppPar\":23,\"id\":19},{\"IdeAweAppPar\":24,\"id\":20},{\"IdeAweAppPar\":25,\"id\":21},{\"IdeAweAppPar\":26,\"id\":22},{\"IdeAweAppPar\":27,\"id\":23},{\"IdeAweAppPar\":28,\"id\":24},{\"IdeAweAppPar\":29,\"id\":25},{\"IdeAweAppPar\":30,\"id\":26},{\"IdeAweAppPar\":31,\"id\":27},{\"IdeAweAppPar\":32,\"id\":28},{\"IdeAweAppPar\":33,\"id\":29},{\"IdeAweAppPar\":34,\"id\":30}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 30);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarLt() throws Exception {
    String queryName = "FilterField-Var-Lt";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"IdeAweAppPar\":7,\"id\":1},{\"IdeAweAppPar\":8,\"id\":2},{\"IdeAweAppPar\":9,\"id\":3},{\"IdeAweAppPar\":10,\"id\":4}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 4);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarIn() throws Exception {
    String queryName = "FilterField-Var-In";
    String variables = "\"list\":[0, 1]";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"IdeAweAppPar\":7,\"id\":1},{\"IdeAweAppPar\":8,\"id\":2},{\"IdeAweAppPar\":9,\"id\":3},{\"IdeAweAppPar\":10,\"id\":4}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 4);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarNotIn() throws Exception {
    String queryName = "FilterField-Var-NotIn";
    String variables = "\"list\":[0, 1]";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":2,\"page\":1,\"records\":35,\"rows\":[{\"IdeAweAppPar\":1,\"id\":1},{\"IdeAweAppPar\":2,\"id\":2},{\"IdeAweAppPar\":3,\"id\":3},{\"IdeAweAppPar\":4,\"id\":4},{\"IdeAweAppPar\":5,\"id\":5},{\"IdeAweAppPar\":6,\"id\":6},{\"IdeAweAppPar\":11,\"id\":7},{\"IdeAweAppPar\":12,\"id\":8},{\"IdeAweAppPar\":13,\"id\":9},{\"IdeAweAppPar\":14,\"id\":10},{\"IdeAweAppPar\":15,\"id\":11},{\"IdeAweAppPar\":16,\"id\":12},{\"IdeAweAppPar\":17,\"id\":13},{\"IdeAweAppPar\":18,\"id\":14},{\"IdeAweAppPar\":19,\"id\":15},{\"IdeAweAppPar\":20,\"id\":16},{\"IdeAweAppPar\":21,\"id\":17},{\"IdeAweAppPar\":22,\"id\":18},{\"IdeAweAppPar\":23,\"id\":19},{\"IdeAweAppPar\":24,\"id\":20},{\"IdeAweAppPar\":25,\"id\":21},{\"IdeAweAppPar\":26,\"id\":22},{\"IdeAweAppPar\":27,\"id\":23},{\"IdeAweAppPar\":28,\"id\":24},{\"IdeAweAppPar\":29,\"id\":25},{\"IdeAweAppPar\":30,\"id\":26},{\"IdeAweAppPar\":31,\"id\":27},{\"IdeAweAppPar\":32,\"id\":28},{\"IdeAweAppPar\":33,\"id\":29},{\"IdeAweAppPar\":34,\"id\":30}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 30);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldIsNull() throws Exception {
    String queryName = "FilterField-IsNull";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":21,\"rows\":[{\"IdeAweAppPar\":8,\"id\":1},{\"IdeAweAppPar\":16,\"id\":2},{\"IdeAweAppPar\":21,\"id\":3},{\"IdeAweAppPar\":22,\"id\":4},{\"IdeAweAppPar\":23,\"id\":5},{\"IdeAweAppPar\":24,\"id\":6},{\"IdeAweAppPar\":25,\"id\":7},{\"IdeAweAppPar\":26,\"id\":8},{\"IdeAweAppPar\":27,\"id\":9},{\"IdeAweAppPar\":28,\"id\":10},{\"IdeAweAppPar\":29,\"id\":11},{\"IdeAweAppPar\":30,\"id\":12},{\"IdeAweAppPar\":31,\"id\":13},{\"IdeAweAppPar\":32,\"id\":14},{\"IdeAweAppPar\":33,\"id\":15},{\"IdeAweAppPar\":34,\"id\":16},{\"IdeAweAppPar\":35,\"id\":17},{\"IdeAweAppPar\":36,\"id\":18},{\"IdeAweAppPar\":37,\"id\":19},{\"IdeAweAppPar\":38,\"id\":20},{\"IdeAweAppPar\":39,\"id\":21}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 21);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldIsNotNull() throws Exception {
    String queryName = "FilterField-IsNotNull";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":18,\"rows\":[{\"IdeAweAppPar\":5},{\"IdeAweAppPar\":10},{\"IdeAweAppPar\":12},{\"IdeAweAppPar\":13},{\"IdeAweAppPar\":14},{\"IdeAweAppPar\":15},{\"IdeAweAppPar\":17},{\"IdeAweAppPar\":18},{\"IdeAweAppPar\":20},{\"IdeAweAppPar\":1},{\"IdeAweAppPar\":2},{\"IdeAweAppPar\":3},{\"IdeAweAppPar\":4},{\"IdeAweAppPar\":6},{\"IdeAweAppPar\":7},{\"IdeAweAppPar\":9},{\"IdeAweAppPar\":11},{\"IdeAweAppPar\":19}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 18);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarLikeStringL() throws Exception {
    String queryName = "FilterField-Var-LikeStringL";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":3,\"rows\":[{\"IdeAweAppPar\":6,\"id\":1},{\"IdeAweAppPar\":11,\"id\":2},{\"IdeAweAppPar\":12,\"id\":3}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 3);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarLikeStringR() throws Exception {
    String queryName = "FilterField-Var-LikeStringR";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":0,\"rows\":[]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 0);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarLikeStringB() throws Exception {
    String queryName = "FilterField-Var-LikeStringB";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":17,\"rows\":[{\"IdeAweAppPar\":1,\"id\":1},{\"IdeAweAppPar\":2,\"id\":2},{\"IdeAweAppPar\":3,\"id\":3},{\"IdeAweAppPar\":4,\"id\":4},{\"IdeAweAppPar\":5,\"id\":5},{\"IdeAweAppPar\":6,\"id\":6},{\"IdeAweAppPar\":7,\"id\":7},{\"IdeAweAppPar\":8,\"id\":8},{\"IdeAweAppPar\":9,\"id\":9},{\"IdeAweAppPar\":10,\"id\":10},{\"IdeAweAppPar\":11,\"id\":11},{\"IdeAweAppPar\":12,\"id\":12},{\"IdeAweAppPar\":14,\"id\":13},{\"IdeAweAppPar\":15,\"id\":14},{\"IdeAweAppPar\":16,\"id\":15},{\"IdeAweAppPar\":17,\"id\":16},{\"IdeAweAppPar\":18,\"id\":17}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 17);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarLikeStringLIgnoreCase() throws Exception {
    String queryName = "FilterField-Var-LikeStringLIgnoreCase";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":3,\"rows\":[{\"IdeAweAppPar\":6,\"id\":1},{\"IdeAweAppPar\":11,\"id\":2},{\"IdeAweAppPar\":12,\"id\":3}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 3);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarLikeStringRIgnoreCase() throws Exception {
    String queryName = "FilterField-Var-LikeStringRIgnoreCase";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"IdeAweAppPar\":11,\"id\":1},{\"IdeAweAppPar\":12,\"id\":2},{\"IdeAweAppPar\":13,\"id\":3},{\"IdeAweAppPar\":14,\"id\":4}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 4);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldVarLikeStringBIgnoreCase() throws Exception {
    String queryName = "FilterField-Var-LikeStringBIgnoreCase";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":18,\"rows\":[{\"IdeAweAppPar\":1,\"id\":1},{\"IdeAweAppPar\":2,\"id\":2},{\"IdeAweAppPar\":3,\"id\":3},{\"IdeAweAppPar\":4,\"id\":4},{\"IdeAweAppPar\":5,\"id\":5},{\"IdeAweAppPar\":6,\"id\":6},{\"IdeAweAppPar\":7,\"id\":7},{\"IdeAweAppPar\":8,\"id\":8},{\"IdeAweAppPar\":9,\"id\":9},{\"IdeAweAppPar\":10,\"id\":10},{\"IdeAweAppPar\":11,\"id\":11},{\"IdeAweAppPar\":12,\"id\":12},{\"IdeAweAppPar\":13,\"id\":13},{\"IdeAweAppPar\":14,\"id\":14},{\"IdeAweAppPar\":15,\"id\":15},{\"IdeAweAppPar\":16,\"id\":16},{\"IdeAweAppPar\":17,\"id\":17},{\"IdeAweAppPar\":18,\"id\":18}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 18);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldFieldEq() throws Exception {
    String queryName = "FilterField-Field-Eq";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":3,\"rows\":[{\"IdeAweAppPar\":7},{\"IdeAweAppPar\":8},{\"IdeAweAppPar\":9}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 3);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldFieldEqCntTable() throws Exception {
    String queryName = "FilterField-Field-EqCntTable";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":3,\"rows\":[{\"IdeAweAppPar\":7},{\"IdeAweAppPar\":8},{\"IdeAweAppPar\":9}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 3);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryFilterFieldSubquery() throws Exception {
    String queryName = "FilterField-Subquery";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"Nam\":\"Onate\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryOrderBy() throws Exception {
    String queryName = "SimpleOrderBy";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":2,\"page\":1,\"records\":39,\"rows\":[{\"ParNam\":\"PwdPat\",\"Cat\":1,\"id\":1},{\"ParNam\":\"PwdMaxNumLog\",\"Cat\":1,\"id\":2},{\"ParNam\":\"PwdExp\",\"Cat\":1,\"id\":3},{\"ParNam\":\"MinPwd\",\"Cat\":1,\"id\":4},{\"ParNam\":\"Param9\",\"Cat\":2,\"id\":5},{\"ParNam\":\"Param8\",\"Cat\":2,\"id\":6},{\"ParNam\":\"Param7\",\"Cat\":2,\"id\":7},{\"ParNam\":\"Param6\",\"Cat\":2,\"id\":8},{\"ParNam\":\"Param5\",\"Cat\":2,\"id\":9},{\"ParNam\":\"Param4\",\"Cat\":2,\"id\":10},{\"ParNam\":\"Param3\",\"Cat\":2,\"id\":11},{\"ParNam\":\"Param2\",\"Cat\":2,\"id\":12},{\"ParNam\":\"Param19\",\"Cat\":2,\"id\":13},{\"ParNam\":\"Param18\",\"Cat\":2,\"id\":14},{\"ParNam\":\"Param17\",\"Cat\":2,\"id\":15},{\"ParNam\":\"Param16\",\"Cat\":2,\"id\":16},{\"ParNam\":\"Param15\",\"Cat\":2,\"id\":17},{\"ParNam\":\"Param14\",\"Cat\":2,\"id\":18},{\"ParNam\":\"Param13\",\"Cat\":2,\"id\":19},{\"ParNam\":\"Param12\",\"Cat\":2,\"id\":20},{\"ParNam\":\"Param11\",\"Cat\":2,\"id\":21},{\"ParNam\":\"Param10\",\"Cat\":2,\"id\":22},{\"ParNam\":\"Param1\",\"Cat\":2,\"id\":23},{\"ParNam\":\"MaxFntVer\",\"Cat\":2,\"id\":24},{\"ParNam\":\"MaxFntHor\",\"Cat\":2,\"id\":25},{\"ParNam\":\"DjrVerMar\",\"Cat\":2,\"id\":26},{\"ParNam\":\"DjrSubTitStl\",\"Cat\":2,\"id\":27},{\"ParNam\":\"DjrSepTck\",\"Cat\":2,\"id\":28},{\"ParNam\":\"DjrRmvLin\",\"Cat\":2,\"id\":29},{\"ParNam\":\"DjrRepPth\",\"Cat\":2,\"id\":30}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 30);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseSimplePagination() throws Exception {
    String queryName = "SimplePagination";
    String variables = "\"max\": 5";
    String result = performRequest(queryName, variables, DATABASE);

    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);
    ObjectNode fillAction = (ObjectNode) resultList.get(0);
    assertEquals("fill", fillAction.get("type").textValue());
    ObjectNode fillParameters = (ObjectNode) fillAction.get("parameters");
    assertEquals(1, fillParameters.size());
    ObjectNode dataList = (ObjectNode) fillParameters.get("datalist");
    assertEquals(8, dataList.get("total").asInt());
    assertEquals(1, dataList.get("page").asInt());
    assertEquals(39, dataList.get("records").asInt());
    ArrayNode dataListRows = (ArrayNode) dataList.get("rows");
    assertEquals(5, dataListRows.size());

    ObjectNode endLoad = (ObjectNode) resultList.get(1);
    assertEquals("end-load", endLoad.get("type").textValue());
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    // Test all keys
    for (JsonNode element : dataListRows) {
      ObjectNode component = (ObjectNode) element;
      logger.debug(component.toString());
    }

    logger.debug("-------------------------------------------");
    logger.debug("There are " + dataListRows.size() + " rows as a result of launching query " + queryName);
    logger.debug("-------------------------------------------");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseSimplePaginationPage1Max30() throws Exception {
    String queryName = "SimplePagination";
    String variables = "\"page\": 1, \"max\": 30";
    String result = performRequest(queryName, variables, DATABASE);

    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);
    ObjectNode fillAction = (ObjectNode) resultList.get(0);
    assertEquals("fill", fillAction.get("type").textValue());
    ObjectNode fillParameters = (ObjectNode) fillAction.get("parameters");
    assertEquals(1, fillParameters.size());
    ObjectNode dataList = (ObjectNode) fillParameters.get("datalist");
    assertEquals(2, dataList.get("total").asInt());
    assertEquals(1, dataList.get("page").asInt());
    assertEquals(39, dataList.get("records").asInt());
    ArrayNode dataListRows = (ArrayNode) dataList.get("rows");
    assertEquals(30, dataListRows.size());

    ObjectNode endLoad = (ObjectNode) resultList.get(1);
    assertEquals("end-load", endLoad.get("type").textValue());
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    // Test all keys
    for (JsonNode element : dataListRows) {
      ObjectNode component = (ObjectNode) element;
      logger.debug(component.toString());
    }

    logger.debug("-------------------------------------------");
    logger.debug("There are " + dataListRows.size() + " rows as a result of launching query " + queryName);
    logger.debug("-------------------------------------------");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseSimplePaginationPage1Max10() throws Exception {
    String queryName = "SimplePagination";
    String variables = "\"page\": 1, \"max\": 10";
    String result = performRequest(queryName, variables, DATABASE);

    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);
    ObjectNode fillAction = (ObjectNode) resultList.get(0);
    assertEquals("fill", fillAction.get("type").textValue());
    ObjectNode fillParameters = (ObjectNode) fillAction.get("parameters");
    assertEquals(1, fillParameters.size());
    ObjectNode dataList = (ObjectNode) fillParameters.get("datalist");
    assertEquals(4, dataList.get("total").asInt());
    assertEquals(1, dataList.get("page").asInt());
    assertEquals(39, dataList.get("records").asInt());
    ArrayNode dataListRows = (ArrayNode) dataList.get("rows");
    assertEquals(10, dataListRows.size());

    ObjectNode endLoad = (ObjectNode) resultList.get(1);
    assertEquals("end-load", endLoad.get("type").textValue());
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    // Test all keys
    for (JsonNode element : dataListRows) {
      ObjectNode component = (ObjectNode) element;
      logger.debug(component.toString());
    }

    logger.debug("-------------------------------------------");
    logger.debug("There are " + dataListRows.size() + " rows as a result of launching query " + queryName);
    logger.debug("-------------------------------------------");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseSimplePaginationPage2Max10() throws Exception {
    String queryName = "SimplePagination";
    String variables = "\"page\": 2, \"max\": 10";
    String result = performRequest(queryName, variables, DATABASE);

    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);
    ObjectNode fillAction = (ObjectNode) resultList.get(0);
    assertEquals("fill", fillAction.get("type").textValue());
    ObjectNode fillParameters = (ObjectNode) fillAction.get("parameters");
    assertEquals(1, fillParameters.size());
    ObjectNode dataList = (ObjectNode) fillParameters.get("datalist");
    assertEquals(4, dataList.get("total").asInt());
    assertEquals(2, dataList.get("page").asInt());
    assertEquals(39, dataList.get("records").asInt());
    ArrayNode dataListRows = (ArrayNode) dataList.get("rows");
    assertEquals(10, dataListRows.size());

    ObjectNode endLoad = (ObjectNode) resultList.get(1);
    assertEquals("end-load", endLoad.get("type").textValue());
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    // Test all keys
    for (JsonNode element : dataListRows) {
      ObjectNode component = (ObjectNode) element;
      logger.debug(component.toString());
    }

    logger.debug("-------------------------------------------");
    logger.debug("There are " + dataListRows.size() + " rows as a result of launching query " + queryName);
    logger.debug("-------------------------------------------");
  }

  /**
   *
   * TESTS FOR QUERIES ALREADY IN QUERIES.XML FILE
   *
   */

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryHaving() throws Exception {
    String queryName = "HavTstUni";
    String variables = "";

    String result = performRequest(queryName, variables, DATABASE);

    ArrayNode resultList = assertResultJson(queryName, result, 1);
    assertEquals(1, resultList.size());
    assertEquals(16, resultList.get(0).get("act").asInt());
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryQrySitModDbsOrd() throws Exception {
    String queryName = "QrySitModDbsOrd";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":12,\"rows\":[{\"Ord\":1,\"Als\":\"aweora1\",\"NamSit\":\"Madrid\",\"IdeSit\":10,\"IdeDbs\":6,\"NamMod\":\"Test\",\"IdeSitModDbs\":2580,\"IdeMod\":916},{\"Ord\":1,\"Als\":\"aweora1\",\"NamSit\":\"Madrid\",\"IdeSit\":10,\"IdeDbs\":6,\"NamMod\":\"Base\",\"IdeSitModDbs\":75,\"IdeMod\":28},{\"Ord\":1,\"Als\":\"aweora2\",\"NamSit\":\"Onate\",\"IdeSit\":17,\"IdeDbs\":7,\"NamMod\":\"Test\",\"IdeSitModDbs\":2579,\"IdeMod\":916},{\"Ord\":1,\"Als\":\"aweora2\",\"NamSit\":\"Onate\",\"IdeSit\":17,\"IdeDbs\":7,\"NamMod\":\"Base\",\"IdeSitModDbs\":60,\"IdeMod\":28},{\"Ord\":2,\"Als\":\"awesqs1\",\"NamSit\":\"Madrid\",\"IdeSit\":10,\"IdeDbs\":8,\"NamMod\":\"Test\",\"IdeSitModDbs\":2582,\"IdeMod\":916},{\"Ord\":2,\"Als\":\"awesqs1\",\"NamSit\":\"Madrid\",\"IdeSit\":10,\"IdeDbs\":8,\"NamMod\":\"Base\",\"IdeSitModDbs\":76,\"IdeMod\":28},{\"Ord\":3,\"Als\":\"awesybase1\",\"NamSit\":\"Madrid\",\"IdeSit\":10,\"IdeDbs\":9,\"NamMod\":\"Test\",\"IdeSitModDbs\":2584,\"IdeMod\":916},{\"Ord\":3,\"Als\":\"awesybase1\",\"NamSit\":\"Madrid\",\"IdeSit\":10,\"IdeDbs\":9,\"NamMod\":\"Base\",\"IdeSitModDbs\":77,\"IdeMod\":28},{\"Ord\":2,\"Als\":\"awesqs2\",\"NamSit\":\"Onate\",\"IdeSit\":17,\"IdeDbs\":15,\"NamMod\":\"Test\",\"IdeSitModDbs\":2581,\"IdeMod\":916},{\"Ord\":2,\"Als\":\"awesqs2\",\"NamSit\":\"Onate\",\"IdeSit\":17,\"IdeDbs\":15,\"NamMod\":\"Base\",\"IdeSitModDbs\":78,\"IdeMod\":28},{\"Ord\":3,\"Als\":\"awesybase2\",\"NamSit\":\"Onate\",\"IdeSit\":17,\"IdeDbs\":16,\"NamMod\":\"Test\",\"IdeSitModDbs\":2583,\"IdeMod\":916},{\"Ord\":3,\"Als\":\"awesybase2\",\"NamSit\":\"Onate\",\"IdeSit\":17,\"IdeDbs\":16,\"NamMod\":\"Base\",\"IdeSitModDbs\":79,\"IdeMod\":28}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 12);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryQrySitModDbsOrdTot() throws Exception {
    String queryName = "QrySitModDbsOrdTot";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":25,\"rows\":[{\"IdeSitModDbs\":2580,\"Als3\":\"aweora1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"1\",\"IdeMod\":916,\"IdeDbs\":6,\"NamMod\":\"Test\",\"id\":1},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"1\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-1\"},{\"IdeSitModDbs\":75,\"Als3\":\"aweora1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"1\",\"IdeMod\":28,\"IdeDbs\":6,\"NamMod\":\"Base\",\"id\":2},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"1\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-3\"},{\"IdeSitModDbs\":2579,\"Als3\":\"aweora2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"1\",\"IdeMod\":916,\"IdeDbs\":7,\"NamMod\":\"Test\",\"id\":3},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"1\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-5\"},{\"IdeSitModDbs\":60,\"Als3\":\"aweora2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"1\",\"IdeMod\":28,\"IdeDbs\":7,\"NamMod\":\"Base\",\"id\":4},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"1\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-7\"},{\"IdeSitModDbs\":2582,\"Als3\":\"awesqs1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"2\",\"IdeMod\":916,\"IdeDbs\":8,\"NamMod\":\"Test\",\"id\":5},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"2\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-9\"},{\"IdeSitModDbs\":76,\"Als3\":\"awesqs1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"2\",\"IdeMod\":28,\"IdeDbs\":8,\"NamMod\":\"Base\",\"id\":6},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"2\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-11\"},{\"IdeSitModDbs\":2584,\"Als3\":\"awesybase1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"3\",\"IdeMod\":916,\"IdeDbs\":9,\"NamMod\":\"Test\",\"id\":7},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"3\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-13\"},{\"IdeSitModDbs\":77,\"Als3\":\"awesybase1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"3\",\"IdeMod\":28,\"IdeDbs\":9,\"NamMod\":\"Base\",\"id\":8},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"3\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-15\"},{\"IdeSitModDbs\":2581,\"Als3\":\"awesqs2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"2\",\"IdeMod\":916,\"IdeDbs\":15,\"NamMod\":\"Test\",\"id\":9},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"2\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-17\"},{\"IdeSitModDbs\":78,\"Als3\":\"awesqs2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"2\",\"IdeMod\":28,\"IdeDbs\":15,\"NamMod\":\"Base\",\"id\":10},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"2\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-19\"},{\"IdeSitModDbs\":2583,\"Als3\":\"awesybase2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"3\",\"IdeMod\":916,\"IdeDbs\":16,\"NamMod\":\"Test\",\"id\":11},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"3\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-21\"},{\"IdeSitModDbs\":79,\"Als3\":\"awesybase2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"3\",\"IdeMod\":28,\"IdeDbs\":16,\"NamMod\":\"Base\",\"id\":12},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"3\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-23\"},{\"IdeSitModDbs\":null,\"Als3\":\"Total\",\"_style_\":\"TOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"24\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":null,\"id\":\"TOT-24\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 25);
    logger.debug(expected);
    logger.debug(result);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryQryUniTst() throws Exception {
    String queryName = "QryUniTst";
    String variables = "";

    String result = performRequest(queryName, variables, DATABASE);
    assertResultJson(queryName, result, 6);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryQryUniTstId() throws Exception {
    String queryName = "QryUniTstId";
    String variables = "";

    String result = performRequest(queryName, variables, DATABASE);
    assertResultJson(queryName, result, 6);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryQryChkPrg() throws Exception {
    String queryName = "QryChkPrg";
    String variables = "";

    String result = performRequest(queryName, variables, DATABASE);
    assertResultJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryQryEdiTst() throws Exception {
    String queryName = "QryEdiTst";
    String variables = "";

    String result = performRequest(queryName, variables, DATABASE);
    assertResultJson(queryName, result, 16);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryQryEdiTstChk() throws Exception {
    String queryName = "QryEdiTstChk";
    String variables = "";

    String result = performRequest(queryName, variables, DATABASE);
    assertResultJson(queryName, result, 16);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryQryEdiSug() throws Exception {
    String queryName = "QryEdiSug";
    String variables = "\"suggest\": \"fr\"";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":2,\"rows\":[{\"name\":\"frost\",\"value\":7,\"label\":\"Prueba - frost\"},{\"name\":\"fresh\",\"value\":8,\"label\":\"Prueba - fresh\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 2);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseCriteriaDate() throws Exception {
    String queryName = "CrtTstDat";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"TxtRea\":1000000.12123},{\"TxtRea\":1000000.12123},{\"TxtRea\":1000000.12123},{\"TxtRea\":1000000.12123}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 4);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTstUsrSug() throws Exception {
    String queryName = "TstUsrSug";
    String variables = "\"suggest\": \"ito\"";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":3,\"rows\":[{\"label\":\"jaimito (Jaimito)\",\"id\":1,\"value\":\"jaimito\",\"nom\":\"Jaimito\"},{\"label\":\"jorgito (Jorgito)\",\"id\":2,\"value\":\"jorgito\",\"nom\":\"Jorgito\"},{\"label\":\"juanito (Juanito)\",\"id\":3,\"value\":\"juanito\",\"nom\":\"Juanito\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 3);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseUserSuggest() throws Exception {
    String queryName = "TstUsrSugIde";
    String variables = "\"suggest\": 1";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":3,\"rows\":[{\"label\":1,\"id\":1,\"value\":1},{\"label\":811,\"id\":2,\"value\":811},{\"label\":1702,\"id\":3,\"value\":1702}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    logger.debug("Expected: " + expected);
    logger.debug("Result: " + result);
    assertResultJson(queryName, result, 3);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTstUsrSel() throws Exception {
    String queryName = "TstUsrSel";
    String variables = "\"suggest\": \"jaimito\"";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"label\":\"jaimito (Jaimito)\",\"id\":1,\"value\":\"jaimito\",\"nom\":\"Jaimito\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTstChrOneDatSrc() throws Exception {
    String queryName = "TstChrOneDatSrc";
    String variables = "";

    String result = performRequest(queryName, variables, DATABASE);
    assertResultJson(queryName, result, 30, 1, 32, 951);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTstChrOneDatSrcPagination() throws Exception {
    String queryName = "TstChrOneDatSrcPagination";
    String variables = "";

    String result = performRequest(queryName, variables, DATABASE);
    assertResultJson(queryName, result, 30, 1, 32, 951);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTstChrOTwoDatSrc() throws Exception {
    String queryName = "TstChrTwoDatSrc";
    String variables = "";

    String result = performRequest(queryName, variables, DATABASE);
    assertResultJson(queryName, result, 30, 1, 32, 951);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTstChrOTwoDatSrcPagination() throws Exception {
    String queryName = "TstChrTwoDatSrcPagination";
    String variables = "";

    String result = performRequest(queryName, variables, DATABASE);
    assertResultJson(queryName, result, 30, 1, 32, 951);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTstChrTwoSrcLab() throws Exception {
    String queryName = "TstChrTwoSrcLab";
    String variables = "";

    String result = performRequest(queryName, variables, DATABASE);
    assertResultJson(queryName, result, 30, 1, 32, 950);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseSugTstLst() throws Exception {
    String queryName = "SugTstLst";
    String variables = "\"suggest\": \"jaimito\"";

    String result = performRequest(queryName, variables, DATABASE);
    assertResultJson(queryName, result, 30, 1, 200, 6000);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseSugTstLstPagination() throws Exception {
    String queryName = "SugTstLstPagination";
    String variables = "\"suggest\": \"jaimito\"";

    String result = performRequest(queryName, variables, DATABASE);
    assertResultJson(queryName, result, 30, 1, 200, 6000);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseProModTrePro() throws Exception {
    String queryName = "ProModTrePro";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":15,\"rows\":[{\"parent\":null,\"TreGrd_id\":\"Proadministrator\",\"TreGrdEdi_parent\":null,\"TreGrd_lev\":{\"value\":\"1\",\"label\":\"1\",\"style\":\"text-danger\"},\"TreGrd_parent\":null,\"isLeaf\":\"false\",\"TreGrd_Nam\":\"administrator\",\"id\":\"Proadministrator\",\"Nam\":\"administrator\",\"TreGrdEdi_Nam\":\"administrator\",\"Lev\":\"1\",\"TreGrdEdi_id\":\"Proadministrator\",\"TreGrdEdi_lev\":\"1\"},{\"parent\":\"Proadministrator\",\"TreGrd_id\":\"Proadministrator-ModBase\",\"TreGrdEdi_parent\":\"Proadministrator\",\"TreGrd_lev\":{\"value\":\"2\",\"label\":\"2\",\"style\":\"text-success\"},\"TreGrd_parent\":\"Proadministrator\",\"isLeaf\":\"false\",\"TreGrd_Nam\":\"Base\",\"id\":\"Proadministrator-ModBase\",\"Nam\":\"Base\",\"TreGrdEdi_Nam\":\"Base\",\"Lev\":\"2\",\"TreGrdEdi_id\":\"Proadministrator-ModBase\",\"TreGrdEdi_lev\":\"2\"},{\"parent\":\"Proadministrator-ModBase\",\"TreGrd_id\":\"Proadministrator-ModBase-SitMadrid\",\"TreGrdEdi_parent\":\"Proadministrator-ModBase\",\"TreGrd_lev\":{\"value\":\"3\",\"label\":\"3\",\"style\":\"\"},\"TreGrd_parent\":\"Proadministrator-ModBase\",\"isLeaf\":\"true\",\"TreGrd_Nam\":\"Madrid\",\"id\":\"Proadministrator-ModBase-SitMadrid\",\"Nam\":\"Madrid\",\"TreGrdEdi_Nam\":\"Madrid\",\"Lev\":\"3\",\"TreGrdEdi_id\":\"Proadministrator-ModBase-SitMadrid\",\"TreGrdEdi_lev\":\"3\"},{\"parent\":\"Proadministrator-ModBase\",\"TreGrd_id\":\"Proadministrator-ModBase-SitOnate\",\"TreGrdEdi_parent\":\"Proadministrator-ModBase\",\"TreGrd_lev\":{\"value\":\"3\",\"label\":\"3\",\"style\":\"\"},\"TreGrd_parent\":\"Proadministrator-ModBase\",\"isLeaf\":\"true\",\"TreGrd_Nam\":\"Onate\",\"id\":\"Proadministrator-ModBase-SitOnate\",\"Nam\":\"Onate\",\"TreGrdEdi_Nam\":\"Onate\",\"Lev\":\"3\",\"TreGrdEdi_id\":\"Proadministrator-ModBase-SitOnate\",\"TreGrdEdi_lev\":\"3\"},{\"parent\":\"Proadministrator\",\"TreGrd_id\":\"Proadministrator-ModTest\",\"TreGrdEdi_parent\":\"Proadministrator\",\"TreGrd_lev\":{\"value\":\"2\",\"label\":\"2\",\"style\":\"text-success\"},\"TreGrd_parent\":\"Proadministrator\",\"isLeaf\":\"false\",\"TreGrd_Nam\":\"Test\",\"id\":\"Proadministrator-ModTest\",\"Nam\":\"Test\",\"TreGrdEdi_Nam\":\"Test\",\"Lev\":\"2\",\"TreGrdEdi_id\":\"Proadministrator-ModTest\",\"TreGrdEdi_lev\":\"2\"},{\"parent\":\"Proadministrator-ModTest\",\"TreGrd_id\":\"Proadministrator-ModTest-SitMadrid\",\"TreGrdEdi_parent\":\"Proadministrator-ModTest\",\"TreGrd_lev\":{\"value\":\"3\",\"label\":\"3\",\"style\":\"\"},\"TreGrd_parent\":\"Proadministrator-ModTest\",\"isLeaf\":\"true\",\"TreGrd_Nam\":\"Madrid\",\"id\":\"Proadministrator-ModTest-SitMadrid\",\"Nam\":\"Madrid\",\"TreGrdEdi_Nam\":\"Madrid\",\"Lev\":\"3\",\"TreGrdEdi_id\":\"Proadministrator-ModTest-SitMadrid\",\"TreGrdEdi_lev\":\"3\"},{\"parent\":\"Proadministrator-ModTest\",\"TreGrd_id\":\"Proadministrator-ModTest-SitOnate\",\"TreGrdEdi_parent\":\"Proadministrator-ModTest\",\"TreGrd_lev\":{\"value\":\"3\",\"label\":\"3\",\"style\":\"\"},\"TreGrd_parent\":\"Proadministrator-ModTest\",\"isLeaf\":\"true\",\"TreGrd_Nam\":\"Onate\",\"id\":\"Proadministrator-ModTest-SitOnate\",\"Nam\":\"Onate\",\"TreGrdEdi_Nam\":\"Onate\",\"Lev\":\"3\",\"TreGrdEdi_id\":\"Proadministrator-ModTest-SitOnate\",\"TreGrdEdi_lev\":\"3\"},{\"parent\":null,\"TreGrd_id\":\"Progeneral\",\"TreGrdEdi_parent\":null,\"TreGrd_lev\":{\"value\":\"1\",\"label\":\"1\",\"style\":\"text-danger\"},\"TreGrd_parent\":null,\"isLeaf\":\"false\",\"TreGrd_Nam\":\"general\",\"id\":\"Progeneral\",\"Nam\":\"general\",\"TreGrdEdi_Nam\":\"general\",\"Lev\":\"1\",\"TreGrdEdi_id\":\"Progeneral\",\"TreGrdEdi_lev\":\"1\"},{\"parent\":\"Progeneral\",\"TreGrd_id\":\"Progeneral-ModBase\",\"TreGrdEdi_parent\":\"Progeneral\",\"TreGrd_lev\":{\"value\":\"2\",\"label\":\"2\",\"style\":\"text-success\"},\"TreGrd_parent\":\"Progeneral\",\"isLeaf\":\"false\",\"TreGrd_Nam\":\"Base\",\"id\":\"Progeneral-ModBase\",\"Nam\":\"Base\",\"TreGrdEdi_Nam\":\"Base\",\"Lev\":\"2\",\"TreGrdEdi_id\":\"Progeneral-ModBase\",\"TreGrdEdi_lev\":\"2\"},{\"parent\":\"Progeneral-ModBase\",\"TreGrd_id\":\"Progeneral-ModBase-SitMadrid\",\"TreGrdEdi_parent\":\"Progeneral-ModBase\",\"TreGrd_lev\":{\"value\":\"3\",\"label\":\"3\",\"style\":\"\"},\"TreGrd_parent\":\"Progeneral-ModBase\",\"isLeaf\":\"true\",\"TreGrd_Nam\":\"Madrid\",\"id\":\"Progeneral-ModBase-SitMadrid\",\"Nam\":\"Madrid\",\"TreGrdEdi_Nam\":\"Madrid\",\"Lev\":\"3\",\"TreGrdEdi_id\":\"Progeneral-ModBase-SitMadrid\",\"TreGrdEdi_lev\":\"3\"},{\"parent\":\"Progeneral-ModBase\",\"TreGrd_id\":\"Progeneral-ModBase-SitOnate\",\"TreGrdEdi_parent\":\"Progeneral-ModBase\",\"TreGrd_lev\":{\"value\":\"3\",\"label\":\"3\",\"style\":\"\"},\"TreGrd_parent\":\"Progeneral-ModBase\",\"isLeaf\":\"true\",\"TreGrd_Nam\":\"Onate\",\"id\":\"Progeneral-ModBase-SitOnate\",\"Nam\":\"Onate\",\"TreGrdEdi_Nam\":\"Onate\",\"Lev\":\"3\",\"TreGrdEdi_id\":\"Progeneral-ModBase-SitOnate\",\"TreGrdEdi_lev\":\"3\"},{\"parent\":null,\"TreGrd_id\":\"Prooperator\",\"TreGrdEdi_parent\":null,\"TreGrd_lev\":{\"value\":\"1\",\"label\":\"1\",\"style\":\"text-danger\"},\"TreGrd_parent\":null,\"isLeaf\":\"false\",\"TreGrd_Nam\":\"operator\",\"id\":\"Prooperator\",\"Nam\":\"operator\",\"TreGrdEdi_Nam\":\"operator\",\"Lev\":\"1\",\"TreGrdEdi_id\":\"Prooperator\",\"TreGrdEdi_lev\":\"1\"},{\"parent\":\"Prooperator\",\"TreGrd_id\":\"Prooperator-ModBase\",\"TreGrdEdi_parent\":\"Prooperator\",\"TreGrd_lev\":{\"value\":\"2\",\"label\":\"2\",\"style\":\"text-success\"},\"TreGrd_parent\":\"Prooperator\",\"isLeaf\":\"false\",\"TreGrd_Nam\":\"Base\",\"id\":\"Prooperator-ModBase\",\"Nam\":\"Base\",\"TreGrdEdi_Nam\":\"Base\",\"Lev\":\"2\",\"TreGrdEdi_id\":\"Prooperator-ModBase\",\"TreGrdEdi_lev\":\"2\"},{\"parent\":\"Prooperator-ModBase\",\"TreGrd_id\":\"Prooperator-ModBase-SitMadrid\",\"TreGrdEdi_parent\":\"Prooperator-ModBase\",\"TreGrd_lev\":{\"value\":\"3\",\"label\":\"3\",\"style\":\"\"},\"TreGrd_parent\":\"Prooperator-ModBase\",\"isLeaf\":\"true\",\"TreGrd_Nam\":\"Madrid\",\"id\":\"Prooperator-ModBase-SitMadrid\",\"Nam\":\"Madrid\",\"TreGrdEdi_Nam\":\"Madrid\",\"Lev\":\"3\",\"TreGrdEdi_id\":\"Prooperator-ModBase-SitMadrid\",\"TreGrdEdi_lev\":\"3\"},{\"parent\":\"Prooperator-ModBase\",\"TreGrd_id\":\"Prooperator-ModBase-SitOnate\",\"TreGrdEdi_parent\":\"Prooperator-ModBase\",\"TreGrd_lev\":{\"value\":\"3\",\"label\":\"3\",\"style\":\"\"},\"TreGrd_parent\":\"Prooperator-ModBase\",\"isLeaf\":\"true\",\"TreGrd_Nam\":\"Onate\",\"id\":\"Prooperator-ModBase-SitOnate\",\"Nam\":\"Onate\",\"TreGrdEdi_Nam\":\"Onate\",\"Lev\":\"3\",\"TreGrdEdi_id\":\"Prooperator-ModBase-SitOnate\",\"TreGrdEdi_lev\":\"3\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 15, 1, 1, 15);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseProModTreProLoa() throws Exception {
    String queryName = "ProModTreProLoa";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":3,\"rows\":[{\"TreGrdEdiLoa_lev\":\"4\",\"parent\":null,\"TreGrdLoa_lev\":\"2\",\"TreGrdLoaEdi_parent\":null,\"TreGrdLoa_id\":\"Proadministrator\",\"isLeaf\":\"false\",\"TreGrdLoa_Nam\":\"administrator\",\"TreGrdLoaEdi_Nam\":\"administrator\",\"id\":\"Proadministrator\",\"Nam\":\"administrator\",\"TreGrdLoa_parent\":null,\"TreGrdLoaEdi_id\":\"Proadministrator\"},{\"TreGrdEdiLoa_lev\":\"4\",\"parent\":null,\"TreGrdLoa_lev\":\"2\",\"TreGrdLoaEdi_parent\":null,\"TreGrdLoa_id\":\"Progeneral\",\"isLeaf\":\"false\",\"TreGrdLoa_Nam\":\"general\",\"TreGrdLoaEdi_Nam\":\"general\",\"id\":\"Progeneral\",\"Nam\":\"general\",\"TreGrdLoa_parent\":null,\"TreGrdLoaEdi_id\":\"Progeneral\"},{\"TreGrdEdiLoa_lev\":\"4\",\"parent\":null,\"TreGrdLoa_lev\":\"2\",\"TreGrdLoaEdi_parent\":null,\"TreGrdLoa_id\":\"Prooperator\",\"isLeaf\":\"false\",\"TreGrdLoa_Nam\":\"operator\",\"TreGrdLoaEdi_Nam\":\"operator\",\"id\":\"Prooperator\",\"Nam\":\"operator\",\"TreGrdLoa_parent\":null,\"TreGrdLoaEdi_id\":\"Prooperator\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 3, 1, 1, 3);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseProModTreProBas() throws Exception {
    String queryName = "ProModTreProBas";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":15,\"rows\":[{\"parent\":null,\"id\":\"Proadministrator\",\"Nam\":\"administrator\",\"Lev\":\"1\",\"isLeaf\":\"false\"},{\"parent\":\"Proadministrator\",\"id\":\"Proadministrator-ModBase\",\"Nam\":\"Base\",\"Lev\":\"2\",\"isLeaf\":\"false\"},{\"parent\":\"Proadministrator-ModBase\",\"id\":\"Proadministrator-ModBase-SitMadrid\",\"Nam\":\"Madrid\",\"Lev\":\"3\",\"isLeaf\":\"true\"},{\"parent\":\"Proadministrator-ModBase\",\"id\":\"Proadministrator-ModBase-SitOnate\",\"Nam\":\"Onate\",\"Lev\":\"3\",\"isLeaf\":\"true\"},{\"parent\":\"Proadministrator\",\"id\":\"Proadministrator-ModTest\",\"Nam\":\"Test\",\"Lev\":\"2\",\"isLeaf\":\"false\"},{\"parent\":\"Proadministrator-ModTest\",\"id\":\"Proadministrator-ModTest-SitMadrid\",\"Nam\":\"Madrid\",\"Lev\":\"3\",\"isLeaf\":\"true\"},{\"parent\":\"Proadministrator-ModTest\",\"id\":\"Proadministrator-ModTest-SitOnate\",\"Nam\":\"Onate\",\"Lev\":\"3\",\"isLeaf\":\"true\"},{\"parent\":null,\"id\":\"Progeneral\",\"Nam\":\"general\",\"Lev\":\"1\",\"isLeaf\":\"false\"},{\"parent\":\"Progeneral\",\"id\":\"Progeneral-ModBase\",\"Nam\":\"Base\",\"Lev\":\"2\",\"isLeaf\":\"false\"},{\"parent\":\"Progeneral-ModBase\",\"id\":\"Progeneral-ModBase-SitMadrid\",\"Nam\":\"Madrid\",\"Lev\":\"3\",\"isLeaf\":\"true\"},{\"parent\":\"Progeneral-ModBase\",\"id\":\"Progeneral-ModBase-SitOnate\",\"Nam\":\"Onate\",\"Lev\":\"3\",\"isLeaf\":\"true\"},{\"parent\":null,\"id\":\"Prooperator\",\"Nam\":\"operator\",\"Lev\":\"1\",\"isLeaf\":\"false\"},{\"parent\":\"Prooperator\",\"id\":\"Prooperator-ModBase\",\"Nam\":\"Base\",\"Lev\":\"2\",\"isLeaf\":\"false\"},{\"parent\":\"Prooperator-ModBase\",\"id\":\"Prooperator-ModBase-SitMadrid\",\"Nam\":\"Madrid\",\"Lev\":\"3\",\"isLeaf\":\"true\"},{\"parent\":\"Prooperator-ModBase\",\"id\":\"Prooperator-ModBase-SitOnate\",\"Nam\":\"Onate\",\"Lev\":\"3\",\"isLeaf\":\"true\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 15, 1, 1, 15);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseProModTreMod() throws Exception {
    String queryName = "ProModTreMod";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"parent\":\"Proadministrator\",\"id\":\"Proadministrator-ModBase\",\"Nam\":\"Base\",\"Lev\":\"2\",\"isLeaf\":\"false\"},{\"parent\":\"Proadministrator\",\"id\":\"Proadministrator-ModTest\",\"Nam\":\"Test\",\"Lev\":\"2\",\"isLeaf\":\"false\"},{\"parent\":\"Progeneral\",\"id\":\"Progeneral-ModBase\",\"Nam\":\"Base\",\"Lev\":\"2\",\"isLeaf\":\"false\"},{\"parent\":\"Prooperator\",\"id\":\"Prooperator-ModBase\",\"Nam\":\"Base\",\"Lev\":\"2\",\"isLeaf\":\"false\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 4, 1, 1, 4);
    logger.debug(expected);
    logger.debug(result);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseProSitTreMod() throws Exception {
    String queryName = "ProSitTreMod";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":8,\"rows\":[{\"parent\":\"Proadministrator-ModBase\",\"id\":\"Proadministrator-ModBase-SitMadrid\",\"Nam\":\"Madrid\",\"Lev\":\"3\",\"isLeaf\":\"true\"},{\"parent\":\"Proadministrator-ModBase\",\"id\":\"Proadministrator-ModBase-SitOnate\",\"Nam\":\"Onate\",\"Lev\":\"3\",\"isLeaf\":\"true\"},{\"parent\":\"Proadministrator-ModTest\",\"id\":\"Proadministrator-ModTest-SitMadrid\",\"Nam\":\"Madrid\",\"Lev\":\"3\",\"isLeaf\":\"true\"},{\"parent\":\"Proadministrator-ModTest\",\"id\":\"Proadministrator-ModTest-SitOnate\",\"Nam\":\"Onate\",\"Lev\":\"3\",\"isLeaf\":\"true\"},{\"parent\":\"Progeneral-ModBase\",\"id\":\"Progeneral-ModBase-SitMadrid\",\"Nam\":\"Madrid\",\"Lev\":\"3\",\"isLeaf\":\"true\"},{\"parent\":\"Progeneral-ModBase\",\"id\":\"Progeneral-ModBase-SitOnate\",\"Nam\":\"Onate\",\"Lev\":\"3\",\"isLeaf\":\"true\"},{\"parent\":\"Prooperator-ModBase\",\"id\":\"Prooperator-ModBase-SitMadrid\",\"Nam\":\"Madrid\",\"Lev\":\"3\",\"isLeaf\":\"true\"},{\"parent\":\"Prooperator-ModBase\",\"id\":\"Prooperator-ModBase-SitOnate\",\"Nam\":\"Onate\",\"Lev\":\"3\",\"isLeaf\":\"true\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultJson(queryName, result, 8, 1, 1, 8);
    logger.debug(expected);
    logger.debug(result);
  }

  // *****************************************************************************************************************//
  // CASE WHEN ELSE TESTS
  // **************************************************************************************************************** //

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testCaseWhenElseDistinct() throws Exception {
    String queryName = "testCaseWhenElseDistinct";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"other\":3,\"another\":\"purple-hills\",\"Value\":null,\"label\":null,\"id\":1},{\"other\":3,\"another\":\"sunset\",\"Value\":1,\"label\":\"SUNSET\",\"id\":2},{\"other\":3,\"another\":\"purple-hills\",\"Value\":2,\"label\":\"SUNNY\",\"id\":3},{\"other\":3,\"another\":\"purple-hills\",\"Value\":3,\"label\":\"PURPLE-HILLS\",\"id\":4}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    logger.warn(expected);
    logger.warn(result);
    assertResultJson(queryName, result, 4, 1, 1, 4);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testCaseWhenElse() throws Exception {
    String queryName = "testCaseWhenElse";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":16,\"rows\":[{\"label\":\"SUNSET\",\"id\":1,\"value\":1},{\"label\":\"\",\"id\":2,\"value\":0},{\"label\":\"\",\"id\":3,\"value\":0},{\"label\":\"\",\"id\":4,\"value\":0},{\"label\":\"SUNNY\",\"id\":5,\"value\":2},{\"label\":\"PURPLE-HILLS\",\"id\":6,\"value\":3},{\"label\":\"\",\"id\":7,\"value\":0},{\"label\":\"\",\"id\":8,\"value\":0},{\"label\":\"\",\"id\":9,\"value\":0},{\"label\":\"\",\"id\":10,\"value\":0},{\"label\":\"\",\"id\":11,\"value\":0},{\"label\":\"\",\"id\":12,\"value\":0},{\"label\":\"\",\"id\":13,\"value\":0},{\"label\":\"\",\"id\":14,\"value\":0},{\"label\":\"\",\"id\":15,\"value\":0},{\"label\":\"\",\"id\":16,\"value\":0}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    logger.warn(expected);
    logger.warn(result);
    assertResultJson(queryName, result, 16, 1, 1, 16);
  }

  // *****************************************************************************************************************//
  // QUERY RESULT TESTS
  // **************************************************************************************************************** //

  private void assertQueryResultJson(String queryName, String result, int expectedRows) throws Exception {
    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);
    ObjectNode fillAction = (ObjectNode) resultList.get(0);
    assertEquals("fill", fillAction.get("type").textValue());
    ObjectNode fillParameters = (ObjectNode) fillAction.get("parameters");
    assertEquals(1, fillParameters.size());
    ObjectNode dataList = (ObjectNode) fillParameters.get("datalist");
    assertEquals(1, dataList.get("total").asInt());
    assertEquals(1, dataList.get("page").asInt());
    assertEquals(expectedRows, dataList.get("records").asInt());
    ArrayNode dataListRows = (ArrayNode) dataList.get("rows");
    assertEquals(expectedRows, dataListRows.size());

    ObjectNode endLoad = (ObjectNode) resultList.get(1);
    assertEquals("end-load", endLoad.get("type").textValue());
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    // Test all keys
    for (JsonNode element : dataListRows) {
      ObjectNode component = (ObjectNode) element;
      logger.debug(component.toString());
    }

    logger.debug("--------------------------------------------------------------------------------------");
    logger.debug("There are " + dataListRows.size() + " rows as a result of launching query " + queryName);
    logger.debug("--------------------------------------------------------------------------------------");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformNumber() throws Exception {
    String queryName = "TransformNumber";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"number\":\"811,00\",\"id\":4,\"value\":\"juanito\",\"email\":\"juanito@test.com\"},{\"number\":\"1.702,00\",\"id\":2,\"value\":\"jaimito\",\"email\":\"jaimito@test.com\"},{\"number\":\"1,00\",\"id\":5,\"value\":\"test\",\"email\":\"test@test.com\"},{\"number\":\"3,00\",\"id\":3,\"value\":\"jorgito\",\"email\":\"jorgito@test.com\"},{\"number\":\"2,00\",\"id\":1,\"value\":\"donald\",\"email\":\"donald@test.com\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformNumberPlain() throws Exception {
    String queryName = "TransformNumberPlain";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"number\":811,\"number_transformed\":811.0,\"id\":4,\"value\":\"juanito\",\"email\":\"juanito@test.com\"},{\"number\":1702,\"number_transformed\":1702.0,\"id\":2,\"value\":\"jaimito\",\"email\":\"jaimito@test.com\"},{\"number\":1,\"number_transformed\":1.0,\"id\":5,\"value\":\"test\",\"email\":\"test@test.com\"},{\"number\":3,\"number_transformed\":3.0,\"id\":3,\"value\":\"jorgito\",\"email\":\"jorgito@test.com\"},{\"number\":2,\"number_transformed\":2.0,\"id\":1,\"value\":\"donald\",\"email\":\"donald@test.com\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformDate() throws Exception {
    String queryName = "TransformDate";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"fecha\":null,\"id\":3},{\"fecha\":null,\"id\":2},{\"fecha\":\"04/11/2013\",\"id\":5},{\"fecha\":null,\"id\":4},{\"fecha\":\"23/10/2013\",\"id\":1}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformDateService() throws Exception {
    String queryName = "TransformDateService";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":3,\"rows\":[{\"date7\":\"15:06:23\",\"date6\":\"10/01/1978 15:06:23\",\"date5\":\"01/10/1978\",\"date4\":\"01/10/1978 15:06:23\",\"date3\":\"10-JAN-1978\",\"id\":1,\"date1\":\"10/01/1978\"},{\"date7\":\"03:30:12\",\"date6\":\"02/01/2015 03:30:12\",\"date5\":\"01/02/2015\",\"date4\":\"01/02/2015 03:30:12\",\"date3\":\"02-JAN-2015\",\"id\":2,\"date1\":\"02/01/2015\"},{\"date7\":\"13:26:55\",\"date6\":\"08/01/2020 13:26:55\",\"date5\":\"01/08/2020\",\"date4\":\"01/08/2020 13:26:55\",\"date3\":\"08-JAN-2020\",\"id\":3,\"date1\":\"08/01/2020\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 3);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformDateJavascript() throws Exception {
    String queryName = "TransformJavascriptDate";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"fecha\":null,\"id\":3},{\"fecha\":null,\"id\":2},{\"fecha\":\"11/04/2013\",\"id\":5},{\"fecha\":null,\"id\":4},{\"fecha\":\"10/23/2013\",\"id\":1}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformDateGeneric() throws Exception {
    String queryName = "TransformGenericDate";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"fecha\":null,\"id\":3},{\"fecha\":null,\"id\":2},{\"fecha\":\"04/11/2013 8:57\",\"id\":5},{\"fecha\":null,\"id\":4},{\"fecha\":\"23/10/2013 16:02\",\"id\":1}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformDateMilliseconds() throws Exception {
    String queryName = "TransformDateMilliseconds";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"fecha\":null,\"id\":3},{\"fecha\":null,\"id\":2},{\"fecha\":1383555422000,\"id\":5},{\"fecha\":null,\"id\":4},{\"fecha\":1382544122000,\"id\":1}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequestWithoutCheckExpected(queryName, variables, DATABASE);
    assertQueryResultJson(queryName, result, 5);

    ArrayNode expectedList = (ArrayNode) objectMapper.readTree(expected);
    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);

    ObjectNode fillExpectedAction = (ObjectNode) expectedList.get(0);
    ObjectNode fillResultAction = (ObjectNode) resultList.get(0);
    ObjectNode expectedParameters = (ObjectNode) fillExpectedAction.get("parameters");
    ObjectNode resultParameters = (ObjectNode) fillResultAction.get("parameters");
    ObjectNode expectedDatalist = (ObjectNode) expectedParameters.get("datalist");
    ObjectNode resultDatalist = (ObjectNode) resultParameters.get("datalist");
    ArrayNode expectedRows = (ArrayNode) expectedDatalist.get("rows");
    ArrayNode resultRows = (ArrayNode) resultDatalist.get("rows");
    ObjectNode expectedRow1 = (ObjectNode) expectedRows.get(2);
    ObjectNode resultRow1 = (ObjectNode) resultRows.get(4);
    ObjectNode expectedRow2 = (ObjectNode) expectedRows.get(4);
    ObjectNode resultRow2 = (ObjectNode) resultRows.get(0);

    assert (Math.abs(expectedRow1.get("fecha").asLong() - resultRow1.get("fecha").asLong()) < 7200001);
    assert (Math.abs(expectedRow2.get("fecha").asLong() - resultRow2.get("fecha").asLong()) < 7200001);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformDateRDB() throws Exception {
    String queryName = "TransformDateRDB";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"fecha\":\"23-OCT-2013\",\"id\":1},{\"fecha\":null,\"id\":2},{\"fecha\":null,\"id\":3},{\"fecha\":null,\"id\":4},{\"fecha\":\"04-NOV-2013\",\"id\":5}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequestWithoutCheckExpected(queryName, variables, DATABASE);
    logger.warn(result);
    assertQueryResultJson(queryName, result, 5);

    ArrayNode expectedList = (ArrayNode) objectMapper.readTree(expected);
    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);

    ObjectNode fillExpectedAction = (ObjectNode) expectedList.get(0);
    ObjectNode fillResultAction = (ObjectNode) resultList.get(0);
    ObjectNode expectedParameters = (ObjectNode) fillExpectedAction.get("parameters");
    ObjectNode resultParameters = (ObjectNode) fillResultAction.get("parameters");
    ObjectNode expectedDatalist = (ObjectNode) expectedParameters.get("datalist");
    ObjectNode resultDatalist = (ObjectNode) resultParameters.get("datalist");
    ArrayNode expectedRows = (ArrayNode) expectedDatalist.get("rows");
    ArrayNode resultRows = (ArrayNode) resultDatalist.get("rows");
    ObjectNode expectedRow1 = (ObjectNode) expectedRows.get(0);
    ObjectNode resultRow1 = (ObjectNode) resultRows.get(0);
    ObjectNode expectedRow2 = (ObjectNode) expectedRows.get(4);
    ObjectNode resultRow2 = (ObjectNode) resultRows.get(4);

    assertEquals(expectedRow1.get("fecha").textValue().toUpperCase(), resultRow1.get("fecha").textValue().toUpperCase());
    assertEquals(expectedRow2.get("fecha").textValue().toUpperCase(), resultRow2.get("fecha").textValue().toUpperCase());
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformTime() throws Exception {
    String queryName = "TransformTime";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"fecha\":null,\"id\":3},{\"fecha\":null,\"id\":2},{\"fecha\":\"08:57:02\",\"id\":5},{\"fecha\":null,\"id\":4},{\"fecha\":\"16:02:02\",\"id\":1}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformTimestamp() throws Exception {
    String queryName = "TransformTimestamp";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"fecha\":null,\"id\":3},{\"fecha\":null,\"id\":2},{\"fecha\":\"04/11/2013 08:57:02\",\"id\":5},{\"fecha\":null,\"id\":4},{\"fecha\":\"23/10/2013 16:02:02\",\"id\":1}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformTimestampJavascript() throws Exception {
    String queryName = "TransformJavascriptTimestamp";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"fecha\":null,\"id\":3},{\"fecha\":null,\"id\":2},{\"fecha\":\"11/04/2013 08:57:02\",\"id\":5},{\"fecha\":null,\"id\":4},{\"fecha\":\"10/23/2013 16:02:02\",\"id\":1}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformEncryptDecrypt() throws Exception {
    String queryName = "TransformEncryptDecrypt";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"codigo\":\"T81NvNhwVLs=\",\"descodificado\":\"hola\",\"nombre\":\"juanito\"},{\"codigo\":\"T81NvNhwVLs=\",\"descodificado\":\"hola\",\"nombre\":\"jaimito\"},{\"codigo\":\"T81NvNhwVLs=\",\"descodificado\":\"hola\",\"nombre\":\"test\"},{\"codigo\":\"T81NvNhwVLs=\",\"descodificado\":\"hola\",\"nombre\":\"jorgito\"},{\"codigo\":\"T81NvNhwVLs=\",\"descodificado\":\"hola\",\"nombre\":\"donald\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformArray() throws Exception {
    String queryName = "TransformArray";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"array\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\"],\"nombre\":\"juanito\"},{\"array\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\"],\"nombre\":\"jaimito\"},{\"array\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\"],\"nombre\":\"test\"},{\"array\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\"],\"nombre\":\"jorgito\"},{\"array\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\"],\"nombre\":\"donald\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformTextHTML() throws Exception {
    String queryName = "TransformTextHtml";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"text\":\"&lt;a href='tutu'&gt;aaa&lt;/a&gt;<br/>&lt;a href='tutu'&gt;aaa&lt;/a&gt;<br/>&lt;a href='tutu'&gt;aaa&lt;/a&gt;\",\"nombre\":\"juanito\"},{\"text\":\"&lt;a href='tutu'&gt;aaa&lt;/a&gt;<br/>&lt;a href='tutu'&gt;aaa&lt;/a&gt;<br/>&lt;a href='tutu'&gt;aaa&lt;/a&gt;\",\"nombre\":\"jaimito\"},{\"text\":\"&lt;a href='tutu'&gt;aaa&lt;/a&gt;<br/>&lt;a href='tutu'&gt;aaa&lt;/a&gt;<br/>&lt;a href='tutu'&gt;aaa&lt;/a&gt;\",\"nombre\":\"test\"},{\"text\":\"&lt;a href='tutu'&gt;aaa&lt;/a&gt;<br/>&lt;a href='tutu'&gt;aaa&lt;/a&gt;<br/>&lt;a href='tutu'&gt;aaa&lt;/a&gt;\",\"nombre\":\"jorgito\"},{\"text\":\"&lt;a href='tutu'&gt;aaa&lt;/a&gt;<br/>&lt;a href='tutu'&gt;aaa&lt;/a&gt;<br/>&lt;a href='tutu'&gt;aaa&lt;/a&gt;\",\"nombre\":\"donald\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformTextPlain() throws Exception {
    String queryName = "TransformTextPlain";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"text\":\"<a href='tutu'>aaa</a>\\n<a href='tutu'>aaa</a>\\n<a href='tutu'>aaa</a>\",\"nombre\":\"juanito\"},{\"text\":\"<a href='tutu'>aaa</a>\\n<a href='tutu'>aaa</a>\\n<a href='tutu'>aaa</a>\",\"nombre\":\"jaimito\"},{\"text\":\"<a href='tutu'>aaa</a>\\n<a href='tutu'>aaa</a>\\n<a href='tutu'>aaa</a>\",\"nombre\":\"test\"},{\"text\":\"<a href='tutu'>aaa</a>\\n<a href='tutu'>aaa</a>\\n<a href='tutu'>aaa</a>\",\"nombre\":\"jorgito\"},{\"text\":\"<a href='tutu'>aaa</a>\\n<a href='tutu'>aaa</a>\\n<a href='tutu'>aaa</a>\",\"nombre\":\"donald\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTransformTextUniline() throws Exception {
    String queryName = "TransformTextUniline";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"text\":\"<a href='tutu'>aaa</a> <a href='tutu'>aaa</a> <a href='tutu'>aaa</a>\",\"nombre\":\"juanito\"},{\"text\":\"<a href='tutu'>aaa</a> <a href='tutu'>aaa</a> <a href='tutu'>aaa</a>\",\"nombre\":\"jaimito\"},{\"text\":\"<a href='tutu'>aaa</a> <a href='tutu'>aaa</a> <a href='tutu'>aaa</a>\",\"nombre\":\"test\"},{\"text\":\"<a href='tutu'>aaa</a> <a href='tutu'>aaa</a> <a href='tutu'>aaa</a>\",\"nombre\":\"jorgito\"},{\"text\":\"<a href='tutu'>aaa</a> <a href='tutu'>aaa</a> <a href='tutu'>aaa</a>\",\"nombre\":\"donald\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTranslateBasic() throws Exception {
    String queryName = "TranslateBasic";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":16,\"rows\":[{\"active\":\"Yes\",\"value\":\"frost\"},{\"active\":\"Yes\",\"value\":\"silver\"},{\"active\":\"Yes\",\"value\":\"fresh\"},{\"active\":\"Yes\",\"value\":\"clean\"},{\"active\":\"Yes\",\"value\":\"default\"},{\"active\":\"Yes\",\"value\":\"adminflare\"},{\"active\":\"Yes\",\"value\":\"dust\"},{\"active\":\"Yes\",\"value\":\"white\"},{\"active\":\"Yes\",\"value\":\"asphalt\"},{\"active\":\"Yes\",\"value\":\"purple-hills\"},{\"active\":\"Yes\",\"value\":\"amazonia\"},{\"active\":\"Yes\",\"value\":\"sunset\"},{\"active\":\"Yes\",\"value\":\"sky\"},{\"active\":\"Yes\",\"value\":\"eclipse\"},{\"active\":\"Yes\",\"value\":\"grass\"},{\"active\":\"Yes\",\"value\":\"sunny\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 16);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseComputedBasic() throws Exception {
    String queryName = "ComputedBasic";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":5,\"rows\":[{\"label\":\"juanito (juanito@test.com)\",\"id\":4,\"value\":\"juanito\",\"email\":\"juanito@test.com\"},{\"label\":\"jaimito (jaimito@test.com)\",\"id\":2,\"value\":\"jaimito\",\"email\":\"jaimito@test.com\"},{\"label\":\"test (test@test.com)\",\"id\":5,\"value\":\"test\",\"email\":\"test@test.com\"},{\"label\":\"jorgito (jorgito@test.com)\",\"id\":3,\"value\":\"jorgito\",\"email\":\"jorgito@test.com\"},{\"label\":\"donald (donald@test.com)\",\"id\":1,\"value\":\"donald\",\"email\":\"donald@test.com\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 5);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseComputedEvalString() throws Exception {
    String queryName = "ComputedEvalString";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":16,\"rows\":[{\"label\":\"Manager\",\"value\":\"adminflare\"},{\"label\":\"No manager\",\"value\":\"amazonia\"},{\"label\":\"No manager\",\"value\":\"asphalt\"},{\"label\":\"No manager\",\"value\":\"clean\"},{\"label\":\"No manager\",\"value\":\"default\"},{\"label\":\"No manager\",\"value\":\"dust\"},{\"label\":\"No manager\",\"value\":\"eclipse\"},{\"label\":\"No manager\",\"value\":\"fresh\"},{\"label\":\"No manager\",\"value\":\"frost\"},{\"label\":\"No manager\",\"value\":\"grass\"},{\"label\":\"No manager\",\"value\":\"purple-hills\"},{\"label\":\"No manager\",\"value\":\"silver\"},{\"label\":\"No manager\",\"value\":\"sky\"},{\"label\":\"No manager\",\"value\":\"sunny\"},{\"label\":\"No manager\",\"value\":\"sunset\"},{\"label\":\"No manager\",\"value\":\"white\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 16);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseComputedEvalNumber() throws Exception {
    String queryName = "ComputedEvalNumber";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":16,\"rows\":[{\"label\":1,\"value\":\"adminflare\"},{\"label\":0,\"value\":\"amazonia\"},{\"label\":0,\"value\":\"asphalt\"},{\"label\":0,\"value\":\"clean\"},{\"label\":0,\"value\":\"default\"},{\"label\":0,\"value\":\"dust\"},{\"label\":0,\"value\":\"eclipse\"},{\"label\":0,\"value\":\"fresh\"},{\"label\":0,\"value\":\"frost\"},{\"label\":0,\"value\":\"grass\"},{\"label\":0,\"value\":\"purple-hills\"},{\"label\":0,\"value\":\"silver\"},{\"label\":0,\"value\":\"sky\"},{\"label\":0,\"value\":\"sunny\"},{\"label\":0,\"value\":\"sunset\"},{\"label\":0,\"value\":\"white\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 16);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseComputedEvalVariable() throws Exception {
    String queryName = "ComputedEvalVariable";
    String variables = "\"prueba\":\"lalala\"";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":16,\"rows\":[{\"variable\":\"lalala\",\"label\":1,\"value\":\"adminflare\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"amazonia\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"asphalt\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"clean\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"default\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"dust\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"eclipse\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"fresh\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"frost\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"grass\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"purple-hills\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"silver\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"sky\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"sunny\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"sunset\"},{\"variable\":\"lalala\",\"label\":0,\"value\":\"white\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 16);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseComputedEvalVariableValue() throws Exception {
    String queryName = "ComputedEvalVariableValue";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":16,\"rows\":[{\"variable\":\"prueba\",\"label\":1,\"value\":\"adminflare\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"amazonia\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"asphalt\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"clean\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"default\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"dust\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"eclipse\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"fresh\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"frost\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"grass\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"purple-hills\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"silver\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"sky\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"sunny\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"sunset\"},{\"variable\":\"prueba\",\"label\":0,\"value\":\"white\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 16);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseComputedEvalVariableProperty() throws Exception {
    String queryName = "ComputedEvalVariableProperty";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":16,\"rows\":[{\"variable\":\"awe\",\"label\":1,\"value\":\"adminflare\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"amazonia\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"asphalt\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"clean\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"default\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"dust\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"eclipse\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"fresh\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"frost\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"grass\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"purple-hills\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"silver\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"sky\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"sunny\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"sunset\"},{\"variable\":\"awe\",\"label\":0,\"value\":\"white\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 16);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseComputedEvalTransform() throws Exception {
    String queryName = "ComputedEvalTransform";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":16,\"rows\":[{\"label\":\"123.456,00\",\"value\":\"adminflare\"},{\"label\":\"0,24\",\"value\":\"amazonia\"},{\"label\":\"0,24\",\"value\":\"asphalt\"},{\"label\":\"0,24\",\"value\":\"clean\"},{\"label\":\"0,24\",\"value\":\"default\"},{\"label\":\"0,24\",\"value\":\"dust\"},{\"label\":\"0,24\",\"value\":\"eclipse\"},{\"label\":\"0,24\",\"value\":\"fresh\"},{\"label\":\"0,24\",\"value\":\"frost\"},{\"label\":\"0,24\",\"value\":\"grass\"},{\"label\":\"0,24\",\"value\":\"purple-hills\"},{\"label\":\"0,24\",\"value\":\"silver\"},{\"label\":\"0,24\",\"value\":\"sky\"},{\"label\":\"0,24\",\"value\":\"sunny\"},{\"label\":\"0,24\",\"value\":\"sunset\"},{\"label\":\"0,24\",\"value\":\"white\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 16);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseComputedEvalTranslate() throws Exception {
    String queryName = "ComputedEvalTranslate";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":16,\"rows\":[{\"label\":\"Yes\",\"value\":\"adminflare\"},{\"label\":\"No\",\"value\":\"amazonia\"},{\"label\":\"No\",\"value\":\"asphalt\"},{\"label\":\"No\",\"value\":\"clean\"},{\"label\":\"No\",\"value\":\"default\"},{\"label\":\"No\",\"value\":\"dust\"},{\"label\":\"No\",\"value\":\"eclipse\"},{\"label\":\"No\",\"value\":\"fresh\"},{\"label\":\"No\",\"value\":\"frost\"},{\"label\":\"No\",\"value\":\"grass\"},{\"label\":\"No\",\"value\":\"purple-hills\"},{\"label\":\"No\",\"value\":\"silver\"},{\"label\":\"No\",\"value\":\"sky\"},{\"label\":\"No\",\"value\":\"sunny\"},{\"label\":\"No\",\"value\":\"sunset\"},{\"label\":\"No\",\"value\":\"white\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 16);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseComputedEvalTransformTranslate() throws Exception {
    String queryName = "ComputedEvalTransformTranslate";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":16,\"rows\":[{\"label\":\"Yes\",\"value\":\"adminflare\"},{\"label\":\"No\",\"value\":\"amazonia\"},{\"label\":\"No\",\"value\":\"asphalt\"},{\"label\":\"No\",\"value\":\"clean\"},{\"label\":\"No\",\"value\":\"default\"},{\"label\":\"No\",\"value\":\"dust\"},{\"label\":\"No\",\"value\":\"eclipse\"},{\"label\":\"No\",\"value\":\"fresh\"},{\"label\":\"No\",\"value\":\"frost\"},{\"label\":\"No\",\"value\":\"grass\"},{\"label\":\"No\",\"value\":\"purple-hills\"},{\"label\":\"No\",\"value\":\"silver\"},{\"label\":\"No\",\"value\":\"sky\"},{\"label\":\"No\",\"value\":\"sunny\"},{\"label\":\"No\",\"value\":\"sunset\"},{\"label\":\"No\",\"value\":\"white\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 16);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseCompoundBasic() throws Exception {
    String queryName = "CompoundBasic";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":16,\"rows\":[{\"compuesto\":{\"value\":\"adminflare\",\"label\":\"Yes\"},\"id\":1},{\"compuesto\":{\"value\":\"amazonia\",\"label\":\"No\"},\"id\":2},{\"compuesto\":{\"value\":\"asphalt\",\"label\":\"No\"},\"id\":3},{\"compuesto\":{\"value\":\"clean\",\"label\":\"No\"},\"id\":4},{\"compuesto\":{\"value\":\"default\",\"label\":\"No\"},\"id\":5},{\"compuesto\":{\"value\":\"dust\",\"label\":\"No\"},\"id\":6},{\"compuesto\":{\"value\":\"eclipse\",\"label\":\"No\"},\"id\":7},{\"compuesto\":{\"value\":\"fresh\",\"label\":\"No\"},\"id\":8},{\"compuesto\":{\"value\":\"frost\",\"label\":\"No\"},\"id\":9},{\"compuesto\":{\"value\":\"grass\",\"label\":\"No\"},\"id\":10},{\"compuesto\":{\"value\":\"purple-hills\",\"label\":\"No\"},\"id\":11},{\"compuesto\":{\"value\":\"silver\",\"label\":\"No\"},\"id\":12},{\"compuesto\":{\"value\":\"sky\",\"label\":\"No\"},\"id\":13},{\"compuesto\":{\"value\":\"sunny\",\"label\":\"No\"},\"id\":14},{\"compuesto\":{\"value\":\"sunset\",\"label\":\"No\"},\"id\":15},{\"compuesto\":{\"value\":\"white\",\"label\":\"No\"},\"id\":16}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 16);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTotalizeTotal() throws Exception {
    String queryName = "TotalizeTotal";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":13,\"rows\":[{\"IdeSitModDbs\":60,\"Als3\":\"aweora2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"1,00\",\"IdeMod\":28,\"IdeDbs\":7,\"NamMod\":\"Base\",\"id\":1},{\"IdeSitModDbs\":75,\"Als3\":\"aweora1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"1,00\",\"IdeMod\":28,\"IdeDbs\":6,\"NamMod\":\"Base\",\"id\":2},{\"IdeSitModDbs\":76,\"Als3\":\"awesqs1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"2,00\",\"IdeMod\":28,\"IdeDbs\":8,\"NamMod\":\"Base\",\"id\":3},{\"IdeSitModDbs\":77,\"Als3\":\"awesybase1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"3,00\",\"IdeMod\":28,\"IdeDbs\":9,\"NamMod\":\"Base\",\"id\":4},{\"IdeSitModDbs\":78,\"Als3\":\"awesqs2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"2,00\",\"IdeMod\":28,\"IdeDbs\":15,\"NamMod\":\"Base\",\"id\":5},{\"IdeSitModDbs\":79,\"Als3\":\"awesybase2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"3,00\",\"IdeMod\":28,\"IdeDbs\":16,\"NamMod\":\"Base\",\"id\":6},{\"IdeSitModDbs\":2579,\"Als3\":\"aweora2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"1,00\",\"IdeMod\":916,\"IdeDbs\":7,\"NamMod\":\"Test\",\"id\":7},{\"IdeSitModDbs\":2580,\"Als3\":\"aweora1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"1,00\",\"IdeMod\":916,\"IdeDbs\":6,\"NamMod\":\"Test\",\"id\":8},{\"IdeSitModDbs\":2581,\"Als3\":\"awesqs2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"2,00\",\"IdeMod\":916,\"IdeDbs\":15,\"NamMod\":\"Test\",\"id\":9},{\"IdeSitModDbs\":2582,\"Als3\":\"awesqs1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"2,00\",\"IdeMod\":916,\"IdeDbs\":8,\"NamMod\":\"Test\",\"id\":10},{\"IdeSitModDbs\":2583,\"Als3\":\"awesybase2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"3,00\",\"IdeMod\":916,\"IdeDbs\":16,\"NamMod\":\"Test\",\"id\":11},{\"IdeSitModDbs\":2584,\"Als3\":\"awesybase1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"3,00\",\"IdeMod\":916,\"IdeDbs\":9,\"NamMod\":\"Test\",\"id\":12},{\"IdeSitModDbs\":null,\"Als3\":\"Total\",\"_style_\":\"TOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"24,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":null,\"id\":\"TOT-12\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 13);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTotalizeSubtotal() throws Exception {
    String queryName = "TotalizeSubtotal";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":24,\"rows\":[{\"IdeSitModDbs\":2580,\"Als3\":\"aweora1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"1,00\",\"IdeMod\":916,\"IdeDbs\":6,\"NamMod\":\"Test\",\"id\":1},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"1,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-1\"},{\"IdeSitModDbs\":75,\"Als3\":\"aweora1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"1,00\",\"IdeMod\":28,\"IdeDbs\":6,\"NamMod\":\"Base\",\"id\":2},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"1,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-3\"},{\"IdeSitModDbs\":2579,\"Als3\":\"aweora2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"1,00\",\"IdeMod\":916,\"IdeDbs\":7,\"NamMod\":\"Test\",\"id\":3},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"1,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-5\"},{\"IdeSitModDbs\":60,\"Als3\":\"aweora2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"1,00\",\"IdeMod\":28,\"IdeDbs\":7,\"NamMod\":\"Base\",\"id\":4},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"1,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-7\"},{\"IdeSitModDbs\":2582,\"Als3\":\"awesqs1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"2,00\",\"IdeMod\":916,\"IdeDbs\":8,\"NamMod\":\"Test\",\"id\":5},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"2,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-9\"},{\"IdeSitModDbs\":76,\"Als3\":\"awesqs1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"2,00\",\"IdeMod\":28,\"IdeDbs\":8,\"NamMod\":\"Base\",\"id\":6},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"2,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-11\"},{\"IdeSitModDbs\":2584,\"Als3\":\"awesybase1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"3,00\",\"IdeMod\":916,\"IdeDbs\":9,\"NamMod\":\"Test\",\"id\":7},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"3,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-13\"},{\"IdeSitModDbs\":77,\"Als3\":\"awesybase1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"3,00\",\"IdeMod\":28,\"IdeDbs\":9,\"NamMod\":\"Base\",\"id\":8},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"3,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-15\"},{\"IdeSitModDbs\":2581,\"Als3\":\"awesqs2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"2,00\",\"IdeMod\":916,\"IdeDbs\":15,\"NamMod\":\"Test\",\"id\":9},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"2,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-17\"},{\"IdeSitModDbs\":78,\"Als3\":\"awesqs2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"2,00\",\"IdeMod\":28,\"IdeDbs\":15,\"NamMod\":\"Base\",\"id\":10},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"2,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-19\"},{\"IdeSitModDbs\":2583,\"Als3\":\"awesybase2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"3,00\",\"IdeMod\":916,\"IdeDbs\":16,\"NamMod\":\"Test\",\"id\":11},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"3,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-21\"},{\"IdeSitModDbs\":79,\"Als3\":\"awesybase2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"3,00\",\"IdeMod\":28,\"IdeDbs\":16,\"NamMod\":\"Base\",\"id\":12},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"3,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-23\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 24);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseTotalizeTotalSubtotal() throws Exception {
    String queryName = "TotalizeTotalSubtotal";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":25,\"rows\":[{\"IdeSitModDbs\":2580,\"Als3\":\"aweora1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"1,00\",\"IdeMod\":916,\"IdeDbs\":6,\"NamMod\":\"Test\",\"id\":1},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"1,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-1\"},{\"IdeSitModDbs\":75,\"Als3\":\"aweora1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"1,00\",\"IdeMod\":28,\"IdeDbs\":6,\"NamMod\":\"Base\",\"id\":2},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"1,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-3\"},{\"IdeSitModDbs\":2579,\"Als3\":\"aweora2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"1,00\",\"IdeMod\":916,\"IdeDbs\":7,\"NamMod\":\"Test\",\"id\":3},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"1,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-5\"},{\"IdeSitModDbs\":60,\"Als3\":\"aweora2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"1,00\",\"IdeMod\":28,\"IdeDbs\":7,\"NamMod\":\"Base\",\"id\":4},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"1,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-7\"},{\"IdeSitModDbs\":2582,\"Als3\":\"awesqs1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"2,00\",\"IdeMod\":916,\"IdeDbs\":8,\"NamMod\":\"Test\",\"id\":5},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"2,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-9\"},{\"IdeSitModDbs\":76,\"Als3\":\"awesqs1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"2,00\",\"IdeMod\":28,\"IdeDbs\":8,\"NamMod\":\"Base\",\"id\":6},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"2,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-11\"},{\"IdeSitModDbs\":2584,\"Als3\":\"awesybase1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"3,00\",\"IdeMod\":916,\"IdeDbs\":9,\"NamMod\":\"Test\",\"id\":7},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"3,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-13\"},{\"IdeSitModDbs\":77,\"Als3\":\"awesybase1\",\"IdeSit\":10,\"NamSit\":\"Madrid\",\"Ord3\":\"3,00\",\"IdeMod\":28,\"IdeDbs\":9,\"NamMod\":\"Base\",\"id\":8},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"3,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-15\"},{\"IdeSitModDbs\":2581,\"Als3\":\"awesqs2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"2,00\",\"IdeMod\":916,\"IdeDbs\":15,\"NamMod\":\"Test\",\"id\":9},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"2,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-17\"},{\"IdeSitModDbs\":78,\"Als3\":\"awesqs2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"2,00\",\"IdeMod\":28,\"IdeDbs\":15,\"NamMod\":\"Base\",\"id\":10},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"2,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-19\"},{\"IdeSitModDbs\":2583,\"Als3\":\"awesybase2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"3,00\",\"IdeMod\":916,\"IdeDbs\":16,\"NamMod\":\"Test\",\"id\":11},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"3,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-21\"},{\"IdeSitModDbs\":79,\"Als3\":\"awesybase2\",\"IdeSit\":17,\"NamSit\":\"Onate\",\"Ord3\":\"3,00\",\"IdeMod\":28,\"IdeDbs\":16,\"NamMod\":\"Base\",\"id\":12},{\"IdeSitModDbs\":null,\"Als3\":null,\"_style_\":\"SUBTOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"3,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":\"Subtotal\",\"id\":\"TOT-23\"},{\"IdeSitModDbs\":null,\"Als3\":\"Total\",\"_style_\":\"TOTAL\",\"IdeSit\":null,\"NamSit\":null,\"Ord3\":\"24,00\",\"IdeMod\":null,\"IdeDbs\":null,\"NamMod\":null,\"id\":\"TOT-24\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 25);
  }

  // *****************************************************************************************************************//
  // SORT TESTS
  // **************************************************************************************************************** //

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseNoSort() throws Exception {
    String queryName = "SortGetAll";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":12,\"rows\":[{\"site\":10,\"database\":6,\"module\":916,\"id\":2580,\"order\":1},{\"site\":10,\"database\":6,\"module\":28,\"id\":75,\"order\":1},{\"site\":17,\"database\":7,\"module\":916,\"id\":2579,\"order\":1},{\"site\":17,\"database\":7,\"module\":28,\"id\":60,\"order\":1},{\"site\":10,\"database\":8,\"module\":28,\"id\":76,\"order\":2},{\"site\":10,\"database\":8,\"module\":916,\"id\":2582,\"order\":2},{\"site\":10,\"database\":9,\"module\":916,\"id\":2584,\"order\":3},{\"site\":10,\"database\":9,\"module\":28,\"id\":77,\"order\":3},{\"site\":17,\"database\":15,\"module\":916,\"id\":2581,\"order\":2},{\"site\":17,\"database\":15,\"module\":28,\"id\":78,\"order\":2},{\"site\":17,\"database\":16,\"module\":916,\"id\":2583,\"order\":3},{\"site\":17,\"database\":16,\"module\":28,\"id\":79,\"order\":3}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 12);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseSortOneFieldAsc() throws Exception {
    String queryName = "SortGetAll";
    String variables = "\"sort\":[{\"id\":\"order\",\"direction\":\"asc\"}]},";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":12,\"rows\":[{\"site\":10,\"database\":6,\"module\":916,\"id\":2580,\"order\":1},{\"site\":10,\"database\":6,\"module\":28,\"id\":75,\"order\":1},{\"site\":17,\"database\":7,\"module\":916,\"id\":2579,\"order\":1},{\"site\":17,\"database\":7,\"module\":28,\"id\":60,\"order\":1},{\"site\":10,\"database\":8,\"module\":916,\"id\":2582,\"order\":2},{\"site\":10,\"database\":8,\"module\":28,\"id\":76,\"order\":2},{\"site\":17,\"database\":15,\"module\":916,\"id\":2581,\"order\":2},{\"site\":17,\"database\":15,\"module\":28,\"id\":78,\"order\":2},{\"site\":10,\"database\":9,\"module\":916,\"id\":2584,\"order\":3},{\"site\":10,\"database\":9,\"module\":28,\"id\":77,\"order\":3},{\"site\":17,\"database\":16,\"module\":916,\"id\":2583,\"order\":3},{\"site\":17,\"database\":16,\"module\":28,\"id\":79,\"order\":3}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 12);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseSortOneFieldDesc() throws Exception {
    String queryName = "SortGetAll";
    String variables = "\"sort\":[{\"id\":\"order\",\"direction\":\"desc\"}]},";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":12,\"rows\":[{\"site\":10,\"database\":9,\"module\":916,\"id\":2584,\"order\":3},{\"site\":10,\"database\":9,\"module\":28,\"id\":77,\"order\":3},{\"site\":17,\"database\":16,\"module\":916,\"id\":2583,\"order\":3},{\"site\":17,\"database\":16,\"module\":28,\"id\":79,\"order\":3},{\"site\":10,\"database\":8,\"module\":916,\"id\":2582,\"order\":2},{\"site\":10,\"database\":8,\"module\":28,\"id\":76,\"order\":2},{\"site\":17,\"database\":15,\"module\":916,\"id\":2581,\"order\":2},{\"site\":17,\"database\":15,\"module\":28,\"id\":78,\"order\":2},{\"site\":10,\"database\":6,\"module\":28,\"id\":75,\"order\":1},{\"site\":10,\"database\":6,\"module\":916,\"id\":2580,\"order\":1},{\"site\":17,\"database\":7,\"module\":916,\"id\":2579,\"order\":1},{\"site\":17,\"database\":7,\"module\":28,\"id\":60,\"order\":1}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 12);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseSortTwoFieldsAscDesc() throws Exception {
    String queryName = "SortGetAll";
    String variables = "\"sort\":[{\"id\":\"order\",\"direction\":\"asc\"},{\"id\":\"module\",\"direction\":\"desc\"}]},";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":12,\"rows\":[{\"site\":10,\"database\":6,\"module\":916,\"id\":2580,\"order\":1},{\"site\":17,\"database\":7,\"module\":916,\"id\":2579,\"order\":1},{\"site\":10,\"database\":6,\"module\":28,\"id\":75,\"order\":1},{\"site\":17,\"database\":7,\"module\":28,\"id\":60,\"order\":1},{\"site\":10,\"database\":8,\"module\":916,\"id\":2582,\"order\":2},{\"site\":17,\"database\":15,\"module\":916,\"id\":2581,\"order\":2},{\"site\":10,\"database\":8,\"module\":28,\"id\":76,\"order\":2},{\"site\":17,\"database\":15,\"module\":28,\"id\":78,\"order\":2},{\"site\":10,\"database\":9,\"module\":916,\"id\":2584,\"order\":3},{\"site\":17,\"database\":16,\"module\":916,\"id\":2583,\"order\":3},{\"site\":10,\"database\":9,\"module\":28,\"id\":77,\"order\":3},{\"site\":17,\"database\":16,\"module\":28,\"id\":79,\"order\":3}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 12);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseSortTwoFieldsDescDesc() throws Exception {
    String queryName = "SortGetAll";
    String variables = "\"sort\":[{\"id\":\"order\",\"direction\":\"desc\"},{\"id\":\"module\",\"direction\":\"desc\"}]},";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":12,\"rows\":[{\"site\":10,\"database\":9,\"module\":916,\"id\":2584,\"order\":3},{\"site\":17,\"database\":16,\"module\":916,\"id\":2583,\"order\":3},{\"site\":10,\"database\":9,\"module\":28,\"id\":77,\"order\":3},{\"site\":17,\"database\":16,\"module\":28,\"id\":79,\"order\":3},{\"site\":10,\"database\":8,\"module\":916,\"id\":2582,\"order\":2},{\"site\":17,\"database\":15,\"module\":916,\"id\":2581,\"order\":2},{\"site\":10,\"database\":8,\"module\":28,\"id\":76,\"order\":2},{\"site\":17,\"database\":15,\"module\":28,\"id\":78,\"order\":2},{\"site\":10,\"database\":6,\"module\":916,\"id\":2580,\"order\":1},{\"site\":17,\"database\":7,\"module\":916,\"id\":2579,\"order\":1},{\"site\":10,\"database\":6,\"module\":28,\"id\":75,\"order\":1},{\"site\":17,\"database\":7,\"module\":28,\"id\":60,\"order\":1}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 12);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseSortThreeFieldsAscDescDesc() throws Exception {
    String queryName = "SortGetAll";
    String variables = "\"sort\":[{\"id\":\"order\",\"direction\":\"asc\"},{\"id\":\"database\",\"direction\":\"desc\"},{\"id\":\"module\",\"direction\":\"desc\"}]},";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":12,\"rows\":[{\"site\":17,\"database\":7,\"module\":916,\"id\":2579,\"order\":1},{\"site\":17,\"database\":7,\"module\":28,\"id\":60,\"order\":1},{\"site\":10,\"database\":6,\"module\":916,\"id\":2580,\"order\":1},{\"site\":10,\"database\":6,\"module\":28,\"id\":75,\"order\":1},{\"site\":17,\"database\":15,\"module\":916,\"id\":2581,\"order\":2},{\"site\":17,\"database\":15,\"module\":28,\"id\":78,\"order\":2},{\"site\":10,\"database\":8,\"module\":916,\"id\":2582,\"order\":2},{\"site\":10,\"database\":8,\"module\":28,\"id\":76,\"order\":2},{\"site\":17,\"database\":16,\"module\":916,\"id\":2583,\"order\":3},{\"site\":17,\"database\":16,\"module\":28,\"id\":79,\"order\":3},{\"site\":10,\"database\":9,\"module\":916,\"id\":2584,\"order\":3},{\"site\":10,\"database\":9,\"module\":28,\"id\":77,\"order\":3}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertQueryResultJson(queryName, result, 12);
  }

  // *****************************************************************************************************************//
  // VARIABLE TESTS
  // **************************************************************************************************************** //

  /**
   * Asserts the JSON in the response
   *
   * @param queryName    Query name
   * @param result       Query result
   * @param expectedRows Expected rows number
   * @return Value list
   * @throws Exception Test error
   */
  private ArrayNode assertResultVariablesJson(String queryName, String result, int expectedRows) throws Exception {
    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);
    ObjectNode fillAction = (ObjectNode) resultList.get(0);
    assertEquals("fill", fillAction.get("type").textValue());
    ObjectNode fillParameters = (ObjectNode) fillAction.get("parameters");
    assertEquals(1, fillParameters.size());
    ObjectNode dataList = (ObjectNode) fillParameters.get("datalist");
    assertEquals(1, dataList.get("total").asInt());
    assertEquals(1, dataList.get("page").asInt());
    assertEquals(expectedRows, dataList.get("records").asInt());
    ArrayNode dataListRows = (ArrayNode) dataList.get("rows");
    assertEquals(expectedRows, dataListRows.size());

    ObjectNode endLoad = (ObjectNode) resultList.get(1);
    assertEquals("end-load", endLoad.get("type").textValue());
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    // Test all keys
    for (JsonNode element : dataListRows) {
      ObjectNode component = (ObjectNode) element;
      logger.debug(component.toString());
    }

    logger.debug("-------------------------------------------");
    logger.debug("There are " + dataListRows.size() + " rows as a result of launching query " + queryName);
    logger.debug("-------------------------------------------");

    return dataListRows;
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseQueryVariableStringNull() throws Exception {
    String queryName = "VariableStringNull";
    String variables = "\"stringNull\":null";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":16,\"rows\":[{\"value\":\"adminflare\"},{\"value\":\"amazonia\"},{\"value\":\"asphalt\"},{\"value\":\"clean\"},{\"value\":\"default\"},{\"value\":\"dust\"},{\"value\":\"eclipse\"},{\"value\":\"fresh\"},{\"value\":\"frost\"},{\"value\":\"grass\"},{\"value\":\"purple-hills\"},{\"value\":\"silver\"},{\"value\":\"sky\"},{\"value\":\"sunny\"},{\"value\":\"sunset\"},{\"value\":\"white\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultVariablesJson(queryName, result, 16);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseStringHash() throws Exception {
    String queryName = "VariableStringHash";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"hashEncrypt\":\"A3Ee0dvjsWU=\",\"l1_nom\":\"test\",\"hashPbk\":\"ac7129c6b96e83745a953e80335a172d74a41d12d91b0e19a21c10cb8a861b65\",\"hashRipemd\":\"72c931bcdede01f4b5ef55a9a4f40405e3d516cb\",\"hashSha\":\"655e786674d9d3e77bc05ed1de37b4b6bc89f788829f9f3c679e7687b410c89b\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultVariablesJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseVariableDate() throws Exception {
    String queryName = "VariableDate";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"date\":\"23/10/1978\",\"l1_nom\":\"test\",\"id\":1,\"timestamp\":\"23/10/1978 15:03:01\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultVariablesJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseVariableSystemDate() throws Exception {
    String queryName = "VariableSystemDate";
    String variables = "";

    String result = performRequest(queryName, variables, DATABASE);

    ArrayNode data = assertResultVariablesJson(queryName, result, 1);

    // Retrieve output values
    ObjectNode firstRow = (ObjectNode) data.get(0);
    String date = firstRow.get("date").asText();
    String time = firstRow.get("time").asText();

    // Check current date vs retrieved date
    Date currentDate = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    Date parsedDate = sdf.parse(date + " " + time);
    logger.debug("Retrieved date: " + parsedDate.toString() + " - Current date: " + currentDate.toString());
    logger.debug("Difference between dates: " + (currentDate.getTime() - parsedDate.getTime()));

    // Check that the difference is less than a second
    assertTrue(parsedDate.getTime() - currentDate.getTime() < 1000);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseVariableNull() throws Exception {
    String queryName = "VariableNull";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"null\":null,\"l1_nom\":\"test\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultVariablesJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseVariableObject() throws Exception {
    String queryName = "VariableObject";
    String variables = "\"object\":{\"total\":1,\"page\":1,\"records\":1}";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"l1_nom\":\"test\",\"object\":\"{\\\"total\\\":1,\\\"page\\\":1,\\\"records\\\":1}\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultVariablesJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseVariableProperty() throws Exception {
    String propertyValue = "awe-boot";
    String queryName = "VariableProperty";
    String variables = "\"object\":{\"total\":1,\"page\":1,\"records\":1}";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"l1_nom\":\"test\",\"property\":\"" + propertyValue + "\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, propertyValue, expected);
    assertResultVariablesJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseVariableOptional() throws Exception {
    String queryName = "VariableOptional";
    String variables = "\"var1\":null,\"var2\":1";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"l1_nom\":\"test\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultVariablesJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseVariableList() throws Exception {
    String queryName = "VariableList";
    String variables = "\"list1\":null,\"list2\":[\"1\",\"2\",\"3\",\"4\"],\"list3\":[],\"list4\":[1,3,4,5]";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":2,\"rows\":[{\"l1_nom\":\"test\"},{\"l1_nom\":\"jorgito\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultVariablesJson(queryName, result, 2);
  }

  /**
   * Test of filter in subquery
   *
   * @throws Exception Test error
   */
  @Test
  public void testFilterInSubQuery() throws Exception {
    String queryName = "FilterInSubQuery";
    String variables = "\"test\":[\"test\",\"jaimito\",\"juanito\",\"jorgito\"]";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":4,\"rows\":[{\"l1_nom\":\"test\"},{\"l1_nom\":\"jorgito\"},{\"l1_nom\":\"jaimito\"},{\"l1_nom\":\"juanito\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultVariablesJson(queryName, result, 4);
  }

  // *****************************************************************************************************************//
  // SERVICE TESTS
  // **************************************************************************************************************** //

  /**
   * Asserts the JSON in the response
   *
   * @param queryName    Query name
   * @param result       result
   * @param expectedRows Expected rows
   * @return Result list
   * @throws Exception Test error
   */
  private ArrayNode assertResultServiceJson(String queryName, String result, int expectedRows) throws Exception {
    return assertResultServiceJson(queryName, result, expectedRows, 1, 1, expectedRows);
  }

  /**
   * Asserts the JSON in the response
   *
   * @param queryName    query name
   * @param result       result
   * @param expectedRows Expected rows
   * @param page         page
   * @param totalPages   total pages
   * @param records      records
   * @return Record list
   * @throws Exception Test error
   */
  private ArrayNode assertResultServiceJson(String queryName, String result, int expectedRows, int page, int totalPages, int records) throws Exception {
    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);
    ObjectNode fillAction = (ObjectNode) resultList.get(0);
    assertEquals("fill", fillAction.get("type").textValue());
    ObjectNode fillParameters = (ObjectNode) fillAction.get("parameters");
    assertEquals(1, fillParameters.size());
    ObjectNode dataList = (ObjectNode) fillParameters.get("datalist");
    assertEquals(totalPages, dataList.get("total").asInt());
    assertEquals(page, dataList.get("page").asInt());
    assertEquals(records, dataList.get("records").asInt());
    ArrayNode dataListRows = (ArrayNode) dataList.get("rows");
    assertEquals(expectedRows, dataListRows.size());

    ObjectNode endLoad = (ObjectNode) resultList.get(1);
    assertEquals("end-load", endLoad.get("type").textValue());
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    logger.debug("--------------------------------------------------------------------------------------");
    logger.debug("There are " + dataListRows.size() + " rows as a result of launching query " + queryName);
    logger.debug("--------------------------------------------------------------------------------------");

    return dataListRows;
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseDatalistNoParams() throws Exception {
    String queryName = "DatalistNoParams";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":3,\"rows\":[{\"id\":1,\"value\":\"0\"},{\"id\":2,\"value\":\"1\"},{\"id\":3,\"value\":\"2\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultServiceJson(queryName, result, 3);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseStringArrayNoParams() throws Exception {
    String queryName = "StringArrayNoParams";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":3,\"rows\":[{\"id\":1,\"value\":\"a\"},{\"id\":2,\"value\":\"b\"},{\"id\":3,\"value\":\"c\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultServiceJson(queryName, result, 3);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseStringArrayTwoStringsParams() throws Exception {
    String queryName = "ServiceQueryTwoParams";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"id\":1,\"IdeSitModDbsSrv\":\"QrySitModDbsOrd\",\"IdeSitSrv\":\"IdeSitModDbs,IdeSit,NamSit,IdeMod,NamMod,IdeDbs,Als,Ord\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultServiceJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseStringArrayNumberParam() throws Exception {
    String queryName = "StringArrayNumberParam";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"id\":1,\"value\":\"10\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultServiceJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseStringArrayLongParam() throws Exception {
    String queryName = "StringArrayLongParam";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"id\":1,\"value\":\"10\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultServiceJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseStringArrayDoubleParam() throws Exception {
    String queryName = "StringArrayDoubleParam";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"id\":1,\"value\":\"10.0\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultServiceJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseStringArrayFloatParam() throws Exception {
    String queryName = "StringArrayFloatParam";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"id\":1,\"value\":\"10.0\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultServiceJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseStringArrayBooleanParam() throws Exception {
    String queryName = "StringArrayBooleanParam";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"id\":1,\"value\":\"true\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultServiceJson(queryName, result, 1);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabasePagination() throws Exception {
    String queryName = "SimplePaginationService";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":3,\"page\":1,\"records\":65,\"rows\":[{\"id\":1,\"value\":\"0\"},{\"id\":2,\"value\":\"1\"},{\"id\":3,\"value\":\"2\"},{\"id\":4,\"value\":\"3\"},{\"id\":5,\"value\":\"4\"},{\"id\":6,\"value\":\"5\"},{\"id\":7,\"value\":\"6\"},{\"id\":8,\"value\":\"7\"},{\"id\":9,\"value\":\"8\"},{\"id\":10,\"value\":\"9\"},{\"id\":11,\"value\":\"10\"},{\"id\":12,\"value\":\"11\"},{\"id\":13,\"value\":\"12\"},{\"id\":14,\"value\":\"13\"},{\"id\":15,\"value\":\"14\"},{\"id\":16,\"value\":\"15\"},{\"id\":17,\"value\":\"16\"},{\"id\":18,\"value\":\"17\"},{\"id\":19,\"value\":\"18\"},{\"id\":20,\"value\":\"19\"},{\"id\":21,\"value\":\"20\"},{\"id\":22,\"value\":\"21\"},{\"id\":23,\"value\":\"22\"},{\"id\":24,\"value\":\"23\"},{\"id\":25,\"value\":\"24\"},{\"id\":26,\"value\":\"25\"},{\"id\":27,\"value\":\"26\"},{\"id\":28,\"value\":\"27\"},{\"id\":29,\"value\":\"28\"},{\"id\":30,\"value\":\"29\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultServiceJson(queryName, result, 30, 1, 3, 65);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabasePaginationMax10() throws Exception {
    String queryName = "SimplePaginationService";
    String variables = "\"max\": 10";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":7,\"page\":1,\"records\":65,\"rows\":[{\"id\":1,\"value\":\"0\"},{\"id\":2,\"value\":\"1\"},{\"id\":3,\"value\":\"2\"},{\"id\":4,\"value\":\"3\"},{\"id\":5,\"value\":\"4\"},{\"id\":6,\"value\":\"5\"},{\"id\":7,\"value\":\"6\"},{\"id\":8,\"value\":\"7\"},{\"id\":9,\"value\":\"8\"},{\"id\":10,\"value\":\"9\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultServiceJson(queryName, result, 10, 1, 7, 65);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseManagedPagination() throws Exception {
    String queryName = "SimpleManagedPaginationService";
    String variables = "\"page\": 2, \"max\": 10";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":7,\"page\":2,\"records\":65,\"rows\":[{\"id\":1,\"value\":\"10\"},{\"id\":2,\"value\":\"11\"},{\"id\":3,\"value\":\"12\"},{\"id\":4,\"value\":\"13\"},{\"id\":5,\"value\":\"14\"},{\"id\":6,\"value\":\"15\"},{\"id\":7,\"value\":\"16\"},{\"id\":8,\"value\":\"17\"},{\"id\":9,\"value\":\"18\"},{\"id\":10,\"value\":\"19\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultServiceJson(queryName, result, 10, 2, 7, 65);
  }

  // *****************************************************************************************************************//
  // INITIAL LOAD TESTS
  // **************************************************************************************************************** //

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  @WithAnonymousUser
  public void testCheckInitialQueryTarget() throws Exception {
    String expected = "[{\"type\":\"screen-data\",\"parameters\":{\"view\":\"base\",\"screenData\":{\"components\":[{\"id\":\"ComponentSelectEnum\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentSelectEnum\",\"label\":\"PARAMETER_SELECT\",\"optional\":true,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"targetAction\":\"Es1Es0\",\"visible\":true},\"model\":{\"selected\":[1],\"defaultValues\":[],\"values\":[{\"id\":1,\"label\":\"ENUM_NO\",\"value\":\"0\"},{\"id\":2,\"label\":\"ENUM_YES\",\"value\":\"1\"}]}},{\"id\":\"ComponentSelectQuery\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentSelectQuery\",\"label\":\"PARAMETER_SELECT\",\"optional\":true,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"targetAction\":\"TestComponentInitialLoadQuery\",\"visible\":true},\"model\":{\"selected\":[1],\"defaultValues\":[],\"values\":[{\"label\":\"test\",\"id\":1,\"value\":1},{\"label\":\"donald\",\"id\":2,\"value\":2},{\"label\":\"jorgito\",\"id\":3,\"value\":3},{\"label\":\"juanito\",\"id\":4,\"value\":811},{\"label\":\"jaimito\",\"id\":5,\"value\":1702}]}},{\"id\":\"WinDat\",\"controller\":{\"contextMenu\":[],\"dependencies\":[],\"label\":\"SCREEN_TEXT_DATA\",\"maximize\":true,\"visible\":true},\"model\":{\"selected\":[],\"defaultValues\":[],\"values\":[]}},{\"id\":\"ComponentSuggestValue\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentSuggestValue\",\"label\":\"PARAMETER_SUGGEST\",\"optional\":false,\"printable\":true,\"readonly\":false,\"serverAction\":\"data\",\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"targetAction\":\"TestComponentInitialLoadValue\",\"visible\":true},\"model\":{\"selected\":[1.0],\"defaultValues\":[1.0],\"values\":[{\"kk\":\"1\",\"value2\":1,\"label\":\"test\",\"id\":1,\"value\":1.0}]}},{\"id\":\"ComponentTextValue\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentTextValue\",\"label\":\"PARAMETER_TEXT\",\"optional\":false,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"targetAction\":\"TestComponentInitialLoadValue\",\"visible\":true},\"model\":{\"selected\":[1.0],\"defaultValues\":[1.0],\"values\":[{\"kk\":\"1\",\"value2\":1,\"label\":\"test\",\"id\":1,\"value\":1.0}]}}],\"messages\":{},\"actions\":[],\"screen\":{\"name\":\"TestInitialLoad\",\"title\":\"SCREEN_TITLE_BUTTON_TEST\",\"option\":\"test-initial-load\"}}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/screen-data")
      .header("Authorization", "e6144dad-6e67-499e-b74a-d1e600732e11")
      .content("{\"option\":\"test-initial-load\",\"view\":\"base\"}")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(expected))
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();

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
    assertEquals(5, screenDataComponents.size());
    assertEquals("TestInitialLoad", screenData.get("screen").get("name").textValue());
    assertEquals("test-initial-load", screenData.get("screen").get("option").textValue());

    ObjectNode endLoad = (ObjectNode) resultList.get(1);
    assertEquals("end-load", endLoad.get("type").textValue());
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    // Test all keys
    for (JsonNode element : screenDataComponents) {
      ObjectNode component = (ObjectNode) element;
      String key = component.get("id").asText();
      logger.debug(key + ": " + component.get("model").get("selected").toString());
    }

    logger.debug("-------------------------------------------");
    logger.debug("There are " + screenDataComponents.size() + " component in the screen " + screenData.get("screen").get("name"));
    logger.debug("-------------------------------------------");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  @WithMockUser(username = "LaloElMalo", roles = {"ADMIN", "USER"})
  public void testCheckInitialQuerySelectedValues() throws Exception {
    setParameter("user", "LaloElMalo");
    String expected = "[{\"type\":\"screen-data\",\"parameters\":{\"view\":\"base\",\"screenData\":{\"components\":[{\"id\":\"ComponentSelectEnum\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentSelectEnum\",\"label\":\"PARAMETER_SELECT\",\"optional\":true,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"targetAction\":\"Es1Es0\",\"value\":\"0\",\"visible\":true},\"model\":{\"selected\":[\"0\"],\"defaultValues\":[\"0\"],\"values\":[{\"id\":1,\"label\":\"ENUM_NO\",\"value\":\"0\"},{\"id\":2,\"label\":\"ENUM_YES\",\"value\":\"1\"}]}},{\"id\":\"ComponentSuggestCheckInitial\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":\"TestComponentInitialSuggestValue\",\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentSuggestCheckInitial\",\"label\":\"PARAMETER_SUGGEST\",\"optional\":false,\"printable\":true,\"readonly\":false,\"serverAction\":\"data\",\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"value\":\"1\",\"visible\":true},\"model\":{\"selected\":[\"1\"],\"defaultValues\":[\"1\"],\"values\":[]}},{\"id\":\"ComponentSelectQuery\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentSelectQuery\",\"label\":\"PARAMETER_SELECT\",\"optional\":true,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"targetAction\":\"TestComponentInitialLoadQuery\",\"value\":\"1\",\"visible\":true},\"model\":{\"selected\":[\"1\"],\"defaultValues\":[\"1\"],\"values\":[{\"label\":\"test\",\"id\":1,\"value\":1},{\"label\":\"donald\",\"id\":2,\"value\":2},{\"label\":\"jorgito\",\"id\":3,\"value\":3},{\"label\":\"juanito\",\"id\":4,\"value\":811},{\"label\":\"jaimito\",\"id\":5,\"value\":1702}]}},{\"id\":\"ComponentTextStaticValue\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentTextStaticValue\",\"label\":\"PARAMETER_TEXT\",\"optional\":false,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"value\":\"prueba\",\"visible\":true},\"model\":{\"selected\":[\"prueba\"],\"defaultValues\":[\"prueba\"],\"values\":[]}},{\"id\":\"WinDat\",\"controller\":{\"contextMenu\":[],\"dependencies\":[],\"label\":\"SCREEN_TEXT_DATA\",\"maximize\":true,\"visible\":true},\"model\":{\"selected\":[],\"defaultValues\":[],\"values\":[]}},{\"id\":\"ComponentSuggestValue\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentSuggestValue\",\"label\":\"PARAMETER_SUGGEST\",\"optional\":false,\"printable\":true,\"readonly\":false,\"serverAction\":\"data\",\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"targetAction\":\"TestComponentInitialLoadValue\",\"value\":\"1\",\"visible\":true},\"model\":{\"selected\":[1.0],\"defaultValues\":[1.0],\"values\":[{\"kk\":\"1\",\"value2\":1,\"label\":\"test\",\"id\":1,\"value\":1.0}]}},{\"id\":\"ComponentTextValue\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentTextValue\",\"label\":\"PARAMETER_TEXT\",\"optional\":false,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"targetAction\":\"TestComponentInitialLoadValue\",\"visible\":true},\"model\":{\"selected\":[1.0],\"defaultValues\":[1.0],\"values\":[{\"kk\":\"1\",\"value2\":1,\"label\":\"test\",\"id\":1,\"value\":1.0}]}},{\"id\":\"ComponentTextStaticSessionValue\",\"controller\":{\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentTextStaticSessionValue\",\"label\":\"PARAMETER_TEXT\",\"optional\":false,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"visible\":true},\"model\":{\"selected\":[\"LaloElMalo\"],\"defaultValues\":[\"LaloElMalo\"],\"values\":[]}},{\"id\":\"ComponentTextStaticPropertyValue\",\"controller\":{\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentTextStaticPropertyValue\",\"label\":\"PARAMETER_TEXT\",\"optional\":false,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"visible\":true},\"model\":{\"selected\":[\"awe-boot\"],\"defaultValues\":[\"awe-boot\"],\"values\":[]}}],\"messages\":{},\"actions\":[],\"screen\":{\"name\":\"TestInitialValues\",\"title\":\"SCREEN_TITLE_BUTTON_TEST\",\"option\":\"test-initial-values\"}}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    MvcResult mvcResult = mockMvc.perform(post("/action/screen-data")
      .header("Authorization", "e6144dad-6e67-499e-b74a-d1e600732e11")
      .content("{\"option\":\"test-initial-values\",\"view\":\"base\"}")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .session(session))
      .andExpect(status().isOk())
      .andExpect(content().json(expected))
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();

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
    assertEquals(9, screenDataComponents.size());
    assertEquals("TestInitialValues", screenData.get("screen").get("name").textValue());
    assertEquals("test-initial-values", screenData.get("screen").get("option").textValue());

    ObjectNode endLoad = (ObjectNode) resultList.get(1);
    assertEquals("end-load", endLoad.get("type").textValue());
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    // Test all keys
    for (JsonNode element : screenDataComponents) {
      ObjectNode component = (ObjectNode) element;
      String key = component.get("id").asText();
      logger.debug(key + ": " + component.get("model").get("selected").toString());
    }

    logger.debug("-------------------------------------------");
    logger.debug("There are " + screenDataComponents.size() + " component in the screen " + screenData.get("screen").get("name"));
    logger.debug("-------------------------------------------");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  @WithAnonymousUser
  public void testCheckInitialVariables() throws Exception {
    String expected = "[{\"type\":\"screen-data\",\"parameters\":{\"view\":\"base\",\"screenData\":{\"components\":[{\"id\":\"ComponentSelectEnum\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentSelectEnum\",\"label\":\"PARAMETER_SELECT\",\"optional\":true,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"targetAction\":\"Es1Es0\",\"value\":\"0\",\"visible\":true},\"model\":{\"selected\":[\"1\"],\"defaultValues\":[\"1\"],\"values\":[{\"id\":1,\"label\":\"ENUM_NO\",\"value\":\"0\"},{\"id\":2,\"label\":\"ENUM_YES\",\"value\":\"1\"}]}},{\"id\":\"ComponentSuggestCheckInitial\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":\"TestComponentInitialSuggestValue\",\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentSuggestCheckInitial\",\"label\":\"PARAMETER_SUGGEST\",\"optional\":false,\"printable\":true,\"readonly\":false,\"serverAction\":\"data\",\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"value\":\"1\",\"visible\":true},\"model\":{\"selected\":[\"1\"],\"defaultValues\":[\"1\"],\"values\":[]}},{\"id\":\"ComponentSelectQuery\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentSelectQuery\",\"label\":\"PARAMETER_SELECT\",\"optional\":true,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"targetAction\":\"TestComponentInitialLoadQuery\",\"value\":\"1\",\"visible\":true},\"model\":{\"selected\":[\"2\"],\"defaultValues\":[\"2\"],\"values\":[{\"label\":\"test\",\"id\":1,\"value\":1},{\"label\":\"donald\",\"id\":2,\"value\":2},{\"label\":\"jorgito\",\"id\":3,\"value\":3},{\"label\":\"juanito\",\"id\":4,\"value\":811},{\"label\":\"jaimito\",\"id\":5,\"value\":1702}]}},{\"id\":\"ComponentTextStaticValue\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentTextStaticValue\",\"label\":\"PARAMETER_TEXT\",\"optional\":false,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"value\":\"prueba\",\"visible\":true},\"model\":{\"selected\":[\"variableStatic\"],\"defaultValues\":[\"variableStatic\"],\"values\":[]}},{\"id\":\"WinDat\",\"controller\":{\"contextMenu\":[],\"dependencies\":[],\"label\":\"SCREEN_TEXT_DATA\",\"maximize\":true,\"visible\":true},\"model\":{\"selected\":[],\"defaultValues\":[],\"values\":[]}},{\"id\":\"ComponentSuggestValue\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentSuggestValue\",\"label\":\"PARAMETER_SUGGEST\",\"optional\":false,\"printable\":true,\"readonly\":false,\"serverAction\":\"data\",\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"targetAction\":\"TestComponentInitialLoadValue\",\"value\":\"1\",\"visible\":true},\"model\":{\"selected\":[1.0],\"defaultValues\":[1.0],\"values\":[{\"kk\":\"1\",\"value2\":1,\"label\":\"test\",\"id\":1,\"value\":1.0}]}},{\"id\":\"ComponentTextValue\",\"controller\":{\"checkEmpty\":true,\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentTextValue\",\"label\":\"PARAMETER_TEXT\",\"optional\":false,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"targetAction\":\"TestComponentInitialLoadValue\",\"visible\":true},\"model\":{\"selected\":[1.0],\"defaultValues\":[1.0],\"values\":[{\"kk\":\"1\",\"value2\":1,\"label\":\"test\",\"id\":1,\"value\":1.0}]}},{\"id\":\"ComponentTextStaticSessionValue\",\"controller\":{\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentTextStaticSessionValue\",\"label\":\"PARAMETER_TEXT\",\"optional\":false,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"visible\":true},\"model\":{\"selected\":[\"variableSession\"],\"defaultValues\":[\"variableSession\"],\"values\":[]}},{\"id\":\"ComponentTextStaticPropertyValue\",\"controller\":{\"checkInitial\":true,\"checkTarget\":false,\"checked\":false,\"contextMenu\":[],\"dependencies\":[],\"id\":\"ComponentTextStaticPropertyValue\",\"label\":\"PARAMETER_TEXT\",\"optional\":false,\"printable\":true,\"readonly\":false,\"strict\":true,\"style\":\"col-xs-6 col-sm-3 col-lg-2\",\"visible\":true},\"model\":{\"selected\":[\"variableProperty\"],\"defaultValues\":[\"variableProperty\"],\"values\":[]}}],\"messages\":{},\"actions\":[],\"screen\":{\"name\":\"TestInitialValues\",\"title\":\"SCREEN_TITLE_BUTTON_TEST\",\"option\":\"test-initial-values\"}}}},{\"type\":\"end-load\",\"parameters\":{}}]";
    String parameters = "\"SelectEnum\":\"1\",\"SelectQuery\":\"2\",\"InitialLoadValue\":\"otra\",\"StaticValue\":\"variableStatic\",\"SessionValue\":\"variableSession\",\"PropertyValue\":\"variableProperty\",";
    MvcResult mvcResult = mockMvc.perform(post("/action/screen-data")
      .header("Authorization", "e6144dad-6e67-499e-b74a-d1e600732e11")
      .content("{" + parameters + "\"option\":\"test-initial-values\",\"view\":\"base\"}")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(expected))
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();

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
    assertEquals(9, screenDataComponents.size());
    assertEquals("TestInitialValues", screenData.get("screen").get("name").textValue());
    assertEquals("test-initial-values", screenData.get("screen").get("option").textValue());

    ObjectNode endLoad = (ObjectNode) resultList.get(1);
    assertEquals("end-load", endLoad.get("type").textValue());
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    // Test all keys
    for (JsonNode element : screenDataComponents) {
      ObjectNode component = (ObjectNode) element;
      String key = component.get("id").asText();
      logger.debug(key + ": " + component.get("model").get("selected").toString());
    }

    logger.debug("-------------------------------------------");
    logger.debug("There are " + screenDataComponents.size() + " component in the screen " + screenData.get("screen").get("name"));
    logger.debug("-------------------------------------------");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  @WithAnonymousUser
  public void testCheckInitialQueryTargetVariables() throws Exception {
    String parameters = "\"SelectEnum\":\"1\",\"SelectQuery\":\"2\",\"InitialLoadValue\":\"otra\",\"StaticValue\":\"variableStatic\",\"SessionValue\":\"variableSession\",\"PropertyValue\":\"variableProperty\",";
    MvcResult mvcResult = mockMvc.perform(post("/action/screen-data")
      .header("Authorization", "e6144dad-6e67-499e-b74a-d1e600732e11")
      .content("{" + parameters + "\"option\":\"test-initial-values-load\",\"view\":\"base\"}")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();

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
    assertEquals(9, screenDataComponents.size());
    assertEquals("TestInitialValuesLoad", screenData.get("screen").get("name").textValue());
    assertEquals("test-initial-values-load", screenData.get("screen").get("option").textValue());

    ObjectNode endLoad = (ObjectNode) resultList.get(1);
    assertEquals("end-load", endLoad.get("type").textValue());
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    // Test all keys
    for (JsonNode element : screenDataComponents) {
      ObjectNode component = (ObjectNode) element;
      String key = component.get("id").asText();
      logger.debug(key + ": " + component.get("model").get("selected").toString());
    }

    logger.debug("-------------------------------------------");
    logger.debug("There are " + screenDataComponents.size() + " component in the screen " + screenData.get("screen").get("name"));
    logger.debug("-------------------------------------------");
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseGridsAndChartScreen() throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/action/screen-data")
      .header("Authorization", "e6144dad-6e67-499e-b74a-d1e600732e11")
      .content("{\"option\":\"grid-and-chart\",\"view\":\"report\"}")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    ArrayNode resultList = (ArrayNode) objectMapper.readTree(result);

    ObjectNode screenDataAction = (ObjectNode) resultList.get(0);
    assertEquals("screen-data", screenDataAction.get("type").textValue());
    ObjectNode screenDataParameters = (ObjectNode) screenDataAction.get("parameters");
    assertEquals(2, screenDataParameters.size());
    assertEquals("report", screenDataParameters.get("view").textValue());
    ObjectNode screenData = (ObjectNode) screenDataParameters.get("screenData");
    assertEquals(0, screenData.get("actions").size());
    assertEquals(0, screenData.get("messages").size());
    ArrayNode screenDataComponents = (ArrayNode) screenData.get("components");
    assertEquals(26, screenDataComponents.size());
    assertEquals("GrdChrPrn", screenData.get("screen").get("name").textValue());
    assertEquals("grid-and-chart", screenData.get("screen").get("option").textValue());

    ObjectNode endLoad = (ObjectNode) resultList.get(1);
    assertEquals("end-load", endLoad.get("type").textValue());
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    // Test all keys
    for (JsonNode element : screenDataComponents) {
      ObjectNode component = (ObjectNode) element;
      String key = component.get("id").asText();
      logger.debug(key + ": " + component.get("model").get("selected").toString());
    }

    logger.debug("-------------------------------------------");
    logger.debug("There are " + screenDataComponents.size() + " component in the screen " + screenData.get("screen").get("name"));
    logger.debug("-------------------------------------------");
  }

  // *****************************************************************************************************************//
  // ENUM TESTS
  // **************************************************************************************************************** //

  /**
   * Asserts the JSON in the response
   *
   * @param queryName    Query name
   * @param result       Query result
   * @param expectedRows Expected rows
   * @return Result data lines
   * @throws Exception Test error
   */
  private ArrayNode assertResultEnumJson(String queryName, String result, int expectedRows) throws Exception {
    return assertResultServiceJson(queryName, result, expectedRows, 1, 1, expectedRows);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseSimpleEnum() throws Exception {
    String queryName = "SimpleEnum";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":2,\"rows\":[{\"id\":1,\"value\":\"0\",\"label\":\"ENUM_NO\"},{\"id\":2,\"value\":\"1\",\"label\":\"ENUM_YES\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultServiceJson(queryName, result, 2);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseLongerEnum() throws Exception {
    String queryName = "LongerEnum";
    String variables = "";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":2,\"page\":1,\"records\":58,\"rows\":[{\"id\":1,\"value\":\"required\",\"label\":\"ENUM_SCR_ATR_REQUIRED\"},{\"id\":2,\"value\":\"validation\",\"label\":\"ENUM_SCR_ATR_VALIDATION\"},{\"id\":3,\"value\":\"style\",\"label\":\"ENUM_SCR_ATR_STYLE\"},{\"id\":4,\"value\":\"label\",\"label\":\"ENUM_SCR_ATR_LABEL\"},{\"id\":5,\"value\":\"value\",\"label\":\"ENUM_SCR_ATR_VALUE\"},{\"id\":6,\"value\":\"session\",\"label\":\"ENUM_SCR_ATR_SESSION\"},{\"id\":7,\"value\":\"variable\",\"label\":\"ENUM_SCR_ATR_VARIABLE\"},{\"id\":8,\"value\":\"component\",\"label\":\"ENUM_SCR_ATR_COMPONENT\"},{\"id\":9,\"value\":\"initialLoad\",\"label\":\"ENUM_SCR_ATR_INITLOAD\"},{\"id\":10,\"value\":\"format\",\"label\":\"ENUM_SCR_ATR_FORMAT\"},{\"id\":11,\"value\":\"numberFormat\",\"label\":\"ENUM_SCR_ATR_NUMFORMAT\"},{\"id\":12,\"value\":\"capitalize\",\"label\":\"ENUM_SCR_ATR_CAPITALIZE\"},{\"id\":13,\"value\":\"visible\",\"label\":\"ENUM_SCR_ATR_VISIBLE\"},{\"id\":14,\"value\":\"readonly\",\"label\":\"ENUM_SCR_ATR_READONLY\"},{\"id\":15,\"value\":\"checked\",\"label\":\"ENUM_SCR_ATR_CHECKED\"},{\"id\":16,\"value\":\"strict\",\"label\":\"ENUM_SCR_ATR_STRICT\"},{\"id\":17,\"value\":\"serverAction\",\"label\":\"ENUM_SCR_ATR_SERVERACTION\"},{\"id\":18,\"value\":\"targetAction\",\"label\":\"ENUM_SCR_ATR_TARGETACTION\"},{\"id\":19,\"value\":\"message\",\"label\":\"ENUM_SCR_ATR_MESSAGE\"},{\"id\":20,\"value\":\"formule\",\"label\":\"ENUM_SCR_ATR_FORMULE\"},{\"id\":21,\"value\":\"optional\",\"label\":\"ENUM_SCR_ATR_OPTIONAL\"},{\"id\":22,\"value\":\"max\",\"label\":\"ENUM_SCR_ATR_MAX\"},{\"id\":23,\"value\":\"movable\",\"label\":\"ENUM_SCR_ATR_MOVABLE\"},{\"id\":24,\"value\":\"printable\",\"label\":\"ENUM_SCR_ATR_PRINTABLE\"},{\"id\":25,\"value\":\"unit\",\"label\":\"ENUM_SCR_ATR_UNIT\"},{\"id\":26,\"value\":\"checkEmpty\",\"label\":\"ENUM_SCR_ATR_CHECKEMPTY\"},{\"id\":27,\"value\":\"checkInitial\",\"label\":\"ENUM_SCR_ATR_CHECKINITIAL\"},{\"id\":28,\"value\":\"checkTarget\",\"label\":\"ENUM_SCR_ATR_CHECKTARGET\"},{\"id\":29,\"value\":\"specific\",\"label\":\"ENUM_SCR_ATR_SPECIFIC\"},{\"id\":30,\"value\":\"timeout\",\"label\":\"ENUM_SCR_ATR_TIMEOUT\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultServiceJson(queryName, result, 30, 1, 2, 58);
  }

  /**
   * Test of launchAction method, of class ActionController.
   *
   * @throws Exception Test error
   */
  @Test
  public void testDatabaseLongerPaginatedEnum() throws Exception {
    String queryName = "LongerEnum";
    String variables = "\"max\": 45, \"page\": 1";
    String expected = "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":2,\"page\":1,\"records\":58,\"rows\":[{\"id\":1,\"value\":\"required\",\"label\":\"ENUM_SCR_ATR_REQUIRED\"},{\"id\":2,\"value\":\"validation\",\"label\":\"ENUM_SCR_ATR_VALIDATION\"},{\"id\":3,\"value\":\"style\",\"label\":\"ENUM_SCR_ATR_STYLE\"},{\"id\":4,\"value\":\"label\",\"label\":\"ENUM_SCR_ATR_LABEL\"},{\"id\":5,\"value\":\"value\",\"label\":\"ENUM_SCR_ATR_VALUE\"},{\"id\":6,\"value\":\"session\",\"label\":\"ENUM_SCR_ATR_SESSION\"},{\"id\":7,\"value\":\"variable\",\"label\":\"ENUM_SCR_ATR_VARIABLE\"},{\"id\":8,\"value\":\"component\",\"label\":\"ENUM_SCR_ATR_COMPONENT\"},{\"id\":9,\"value\":\"initialLoad\",\"label\":\"ENUM_SCR_ATR_INITLOAD\"},{\"id\":10,\"value\":\"format\",\"label\":\"ENUM_SCR_ATR_FORMAT\"},{\"id\":11,\"value\":\"numberFormat\",\"label\":\"ENUM_SCR_ATR_NUMFORMAT\"},{\"id\":12,\"value\":\"capitalize\",\"label\":\"ENUM_SCR_ATR_CAPITALIZE\"},{\"id\":13,\"value\":\"visible\",\"label\":\"ENUM_SCR_ATR_VISIBLE\"},{\"id\":14,\"value\":\"readonly\",\"label\":\"ENUM_SCR_ATR_READONLY\"},{\"id\":15,\"value\":\"checked\",\"label\":\"ENUM_SCR_ATR_CHECKED\"},{\"id\":16,\"value\":\"strict\",\"label\":\"ENUM_SCR_ATR_STRICT\"},{\"id\":17,\"value\":\"serverAction\",\"label\":\"ENUM_SCR_ATR_SERVERACTION\"},{\"id\":18,\"value\":\"targetAction\",\"label\":\"ENUM_SCR_ATR_TARGETACTION\"},{\"id\":19,\"value\":\"message\",\"label\":\"ENUM_SCR_ATR_MESSAGE\"},{\"id\":20,\"value\":\"formule\",\"label\":\"ENUM_SCR_ATR_FORMULE\"},{\"id\":21,\"value\":\"optional\",\"label\":\"ENUM_SCR_ATR_OPTIONAL\"},{\"id\":22,\"value\":\"max\",\"label\":\"ENUM_SCR_ATR_MAX\"},{\"id\":23,\"value\":\"movable\",\"label\":\"ENUM_SCR_ATR_MOVABLE\"},{\"id\":24,\"value\":\"printable\",\"label\":\"ENUM_SCR_ATR_PRINTABLE\"},{\"id\":25,\"value\":\"unit\",\"label\":\"ENUM_SCR_ATR_UNIT\"},{\"id\":26,\"value\":\"checkEmpty\",\"label\":\"ENUM_SCR_ATR_CHECKEMPTY\"},{\"id\":27,\"value\":\"checkInitial\",\"label\":\"ENUM_SCR_ATR_CHECKINITIAL\"},{\"id\":28,\"value\":\"checkTarget\",\"label\":\"ENUM_SCR_ATR_CHECKTARGET\"},{\"id\":29,\"value\":\"specific\",\"label\":\"ENUM_SCR_ATR_SPECIFIC\"},{\"id\":30,\"value\":\"timeout\",\"label\":\"ENUM_SCR_ATR_TIMEOUT\"},{\"id\":31,\"value\":\"field\",\"label\":\"ENUM_SCR_ATR_FIELD\"},{\"id\":32,\"value\":\"width\",\"label\":\"ENUM_SCR_ATR_WIDTH\"},{\"id\":33,\"value\":\"charLength\",\"label\":\"ENUM_SCR_ATR_CHARLENGTH\"},{\"id\":34,\"value\":\"align\",\"label\":\"ENUM_SCR_ATR_ALIGN\"},{\"id\":35,\"value\":\"inputType\",\"label\":\"ENUM_SCR_ATR_INPUTTYPE\"},{\"id\":36,\"value\":\"sortable\",\"label\":\"ENUM_SCR_ATR_SORTABLE\"},{\"id\":37,\"value\":\"hidden\",\"label\":\"ENUM_SCR_ATR_HIDDEN\"},{\"id\":38,\"value\":\"sendable\",\"label\":\"ENUM_SCR_ATR_SENDABLE\"},{\"id\":39,\"value\":\"summaryType\",\"label\":\"ENUM_SCR_ATR_SUMMARYTYPE\"},{\"id\":40,\"value\":\"formatter\",\"label\":\"ENUM_SCR_ATR_FORMATTER\"},{\"id\":41,\"value\":\"formatOptions\",\"label\":\"ENUM_SCR_ATR_FORMATOPTIONS\"},{\"id\":42,\"value\":\"frozen\",\"label\":\"ENUM_SCR_ATR_FROZEN\"},{\"id\":43,\"value\":\"position\",\"label\":\"ENUM_SCR_ATR_POSITION\"},{\"id\":44,\"value\":\"autoload\",\"label\":\"ENUM_SCR_ATR_AUTOLOAD\"},{\"id\":45,\"value\":\"totalize\",\"label\":\"ENUM_SCR_ATR_TOTALIZE\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]";

    String result = performRequest(queryName, variables, DATABASE, expected);
    assertResultServiceJson(queryName, result, 45, 1, 2, 58);
  }

  // *****************************************************************************************************************//
  // SECURITY TESTS
  // **************************************************************************************************************** //

  /**
   * Test check anonymous user query
   *
   * @throws Exception Error in test
   */
  @Test
  @WithAnonymousUser
  public void testCheckAnonymousUserQuery() throws Exception {
    String expected = "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"message\":\"Session has expired. Please reload\",\"title\":\"Error in maintain operation\",\"type\":\"error\"}},{\"type\":\"cancel\",\"parameters\":{}}]";

    mockMvc.perform(post("/action/maintain/SimpleSingleInsert")
      .header("Authorization", "16617f0d-97ee-4f6b-ad54-905d6ce3c328")
      .content("{\"max\":30}")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(expected))
      .andReturn();
  }

  /**
   * Test check authenticated user query
   *
   * @throws Exception Error in test
   */

  @Test
  @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
  public void testCheckAuthenticatedUserQuery() throws Exception {
    String queryName = "SimpleGetAll";
    String variables = "";
    MvcResult mvcResult = mockMvc.perform(post("/action/data/" + queryName)
      .header("Authorization", "16617f0d-97ee-4f6b-ad54-905d6ce3c328")
      .content("{"+ variables +"\"option\":\"grid-and-chart\",\"view\":\"base\"}")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();

    assertResultSecurityJson(queryName, result, 12, 1, 1, 12);
  }

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
  public ArrayNode assertResultSecurityJson(String queryName, String result, int expectedRows, int page, int totalPages, int records) throws Exception {
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
    ObjectNode endLoadParameters = (ObjectNode) endLoad.get("parameters");
    assertEquals(0, endLoadParameters.size());

    return dataListRows;
  }
}
