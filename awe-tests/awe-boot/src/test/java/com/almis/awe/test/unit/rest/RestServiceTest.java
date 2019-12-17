package com.almis.awe.test.unit.rest;

import com.almis.awe.test.unit.categories.CIDatabaseTest;
import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class used for testing rest services through ActionController
 *
 * @author pgarcia
 */
@Log4j2
public class RestServiceTest extends AweSpringRestTests {

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
   * Test a REST POST
   *
   * @param name       Target action
   * @param action     Server action
   * @param parameters Extra parameters
   * @param expected   Expected result
   * @throws Exception
   */
  private void doRestTest(String name, String action, String parameters, String expected) throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/action/" + action + "/" + name)
      .header("Authorization", "16617f0d-97ee-4f6b-ad54-905d6ce3c328")
      .contentType(MediaType.APPLICATION_JSON)
      .content("{" + parameters + "\"max\":30}")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.warn(result);

    // Check expected
    if (expected != null) {
      JSONAssert.assertEquals(expected, result, false);
    }
  }

  /**
   * Rest test: Simple get query
   *
   * @throws Exception Test error
   */
  @Test
  public void testSimpleGetData() throws Exception {
    doRestTest("TestSimpleRestGet", "data", "", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":0,\"rows\":[]}}},{\"type\":\"end-load\"}]");
  }

  /**
   * Rest test: Simple post query
   *
   * @throws Exception Test error
   */
  @Test
  public void testSimplePostData() throws Exception {
    doRestTest("TestSimpleRestPost", "data", "", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":0,\"rows\":[]}}},{\"type\":\"end-load\"}]");
  }

  /**
   * Rest test: Simple get maintain
   *
   * @throws Exception Test error
   */
  @Test
  public void testSimpleGetMaintain() throws Exception {
    doRestTest("TestSimpleRestGet", "maintain", "", "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[]}}]");
  }

  /**
   * Rest test: Simple post maintain
   *
   * @throws Exception Test error
   */
  @Test
  public void testSimplePostMaintain() throws Exception {
    doRestTest("TestSimpleRestPost", "maintain", "", "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[]}}]");
  }

  /**
   * Rest test: Complex get
   *
   * @throws Exception Test error
   */
  @Test
  public void testComplexGet() throws Exception {
    doRestTest("TestComplexRestGet", "data", "", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"CrtTst\":1,\"id\":1}]}}},{\"type\":\"end-load\"}]");
  }

  /**
   * Rest test: Complex post
   *
   * @throws Exception Test error
   */
  @Test
  public void testComplexPost() throws Exception {
    doRestTest("TestComplexRestPost", "maintain", "", "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"DELETE\",\"rowsAffected\":1,\"parameterMap\":{\"Action\":\"T\",\"User\":\"testIncludeTarget\",\"_page_\":1,\"_max_\":30}}]}}]");
  }

  /**
   * Rest test: Complex get with parameters
   *
   * @throws Exception Test error
   */
  @Test
  public void testComplexGetParameters() throws Exception {
    doRestTest("TestComplexRestGetParameters", "data", "\"value\":\"1\",", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"CrtTst\":1,\"id\":1}]}}},{\"type\":\"end-load\"}]");
  }

  /**
   * Rest test: Complex post with parameters
   *
   * @throws Exception Test error
   */
  @Test
  public void testComplexPostParameters() throws Exception {
    doRestTest("TestComplexRestPostParameters", "maintain", "\"value\":1,", "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"DELETE\",\"rowsAffected\":1,\"parameterMap\":{\"Action\":\"T\",\"User\":\"testIncludeTarget\",\"_page_\":1,\"_max_\":30}}]}}]");
  }

  /**
   * Rest test: Complex post with parameters
   *
   * @throws Exception Test error
   */
  @Test
  public void testComplexPostParametersJson() throws Exception {
    doRestTest("TestComplexRestPostParametersJson", "maintain", "\"value\":1,", "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[{\"operationType\":\"DELETE\",\"rowsAffected\":1,\"parameterMap\":{\"Action\":\"T\",\"User\":\"testIncludeTarget\",\"_page_\":1,\"_max_\":30}}]}}]");
  }

  /**
   * Call a external rest API
   *
   * @throws Exception Test error
   */
  @Test
  public void testExternalRestApi() throws Exception {
    doRestTest("TestExternalRestApi", "data", "\"value\":3,", null);
  }

  /**
   * Call a external rest API
   *
   * @throws Exception Test error
   */
  @Test
  @Category(CIDatabaseTest.class)
  public void testPostmanRestApi() throws Exception {
    doRestTest("TestPostmanRestApi", "data", "", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"acceptLanguage\":\"\",\"acceptEncoding\":\"gzip,deflate\",\"cookie\":\"\",\"method\":\"GET\",\"gzipped\":\"true\",\"postmanToken\":\"\",\"id\":1,\"cacheControl\":\"\",\"accept\":\"application/json, application/*+json\"}]}}},{\"type\":\"end-load\"}]");
  }

  /**
   * Rest test: Complex post with parameters
   *
   * @throws Exception Test error
   */
  @Test
  public void testPostParameterList() throws Exception {
    doRestTest("TestComplexRestPostParametersList", "maintain", "\"stringList\":[\"tutu\", \"lala\", \"yoyo\"],\"integerList\":[4, 6, 7],\"dateList\":[\"23/04/2014\", \"22/05/2017\", \"07/01/2019\"],", "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[]}}]");
  }

  /**
   * Rest test: Complex post with parameters
   *
   * @throws Exception Test error
   */
  @Test
  public void testPostParameterListGetParameters() throws Exception {
    doRestTest("TestComplexRestPostParametersListGetParameters", "maintain", "\"stringList\":[\"tutu\", \"lala\", \"yoyo\"],\"integerList\":[4, 6, 7],\"dateList\":[\"23/04/2014\", \"22/05/2017\", \"07/01/2019\"],", "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[]}}]");
  }

  /**
   * Rest test: Complex post with parameters
   *
   * @throws Exception Test error
   */
  @Test
  public void testPostParameterListJson() throws Exception {
    doRestTest("TestComplexRestPostParametersListJson", "maintain", "\"stringList\":[\"tutu\", \"lala\", \"yoyo\"],\"integerList\":[4, 6, 7],\"dateList\":[\"23/04/2014\", \"22/05/2017\", \"07/01/2019\"],", "[{\"type\":\"end-load\"},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been successfully performed\",\"result_details\":[]}}]");
  }
}