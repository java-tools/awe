package com.almis.awe.test.unit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class used for testing rest services through ActionController
 *
 * @author pgarcia
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@WithMockUser(username = "test", password = "test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RestServiceTest extends TestUtil {

  // Logger
  private static Logger logger = LogManager.getLogger(RestServiceTest.class);

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
   * Test a REST POST
   * @param name Target action
   * @param action Server action
   * @param parameters Extra parameters
   * @param expected Expected result
   * @throws Exception
   */
  private void doRestTest(String name, String action, String parameters, String expected) throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/action/" + action + "/" + name)
            .param("p", "{\"serverAction\":\"" + action + "\",\"targetAction\":\"" + name + "\"," + parameters +"\"t\":\"6c65626d637a6b6b5737504b3941745a414265653148684e6e7145555a362f704d744b4832766c4474436946706c55472b3738566b773d3d\",\"s\":\"16617f0d-97ee-4f6b-ad54-905d6ce3c328\",\"max\":30}")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andReturn();
    String result = mvcResult.getResponse().getContentAsString();
    logger.info(result);

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
    doRestTest("TestSimpleRestGet", "data", "", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":0,\"rows\":[]}}},{\"type\":\"end-load\",\"parameters\":{}}]");
  }

  /**
   * Rest test: Simple post query
   *
   * @throws Exception Test error
   */
  @Test
  public void testSimplePostData() throws Exception {
    doRestTest("TestSimpleRestPost", "data","", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":0,\"rows\":[]}}},{\"type\":\"end-load\",\"parameters\":{}}]");
  }

  /**
   * Rest test: Simple get maintain
   *
   * @throws Exception Test error
   */
  @Test
  public void testSimpleGetMaintain() throws Exception {
    doRestTest("TestSimpleRestGet", "maintain","", "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[]}}]");
  }

  /**
   * Rest test: Simple post maintain
   *
   * @throws Exception Test error
   */
  @Test
  public void testSimplePostMaintain() throws Exception {
    doRestTest("TestSimpleRestPost", "maintain","", "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[]}}]");
  }

  /**
   * Rest test: Complex get
   *
   * @throws Exception Test error
   */
  @Test
  public void testComplexGet() throws Exception {
    doRestTest("TestComplexRestGet", "data","", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"CrtTst\":1,\"id\":1}]}}},{\"type\":\"end-load\",\"parameters\":{}}]");
  }

  /**
   * Rest test: Complex post
   *
   * @throws Exception Test error
   */
  @Test
  public void testComplexPost() throws Exception {
    doRestTest("TestComplexRestPost", "maintain","", "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"DELETE\",\"rowsAffected\":1,\"parameterMap\":{\"Action\":\"T\",\"Scr\":\"testIncludeTargetUpd\",\"Act\":2,\"User\":\"testIncludeTarget\",\"_page_\":1,\"_max_\":30,\"Thm\":2,\"Nam\":\"testIncludeTargetUpd\",\"Date\":null}}]}}]");
  }

  /**
   * Rest test: Complex get with parameters
   *
   * @throws Exception Test error
   */
  @Test
  public void testComplexGetParameters() throws Exception {
    doRestTest("TestComplexRestGetParameters", "data","\"value\":\"1\",", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"CrtTst\":1,\"id\":1}]}}},{\"type\":\"end-load\",\"parameters\":{}}]");
  }

  /**
   * Rest test: Complex post with parameters
   *
   * @throws Exception Test error
   */
  @Test
  public void testComplexPostParameters() throws Exception {
    doRestTest("TestComplexRestPostParameters", "maintain","\"value\":1,", "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"DELETE\",\"rowsAffected\":1,\"parameterMap\":{\"Action\":\"T\",\"Scr\":\"testIncludeTarget\",\"Act\":1,\"User\":\"testIncludeTarget\",\"_page_\":1,\"_max_\":30,\"Thm\":1,\"Nam\":\"testIncludeTarget\",\"Date\":null}}]}}]");
  }

  /**
   * Rest test: Complex post with parameters
   *
   * @throws Exception Test error
   */
  @Test
  public void testComplexPostParametersJson() throws Exception {
    doRestTest("TestComplexRestPostParametersJson", "maintain","\"value\":1,", "[{\"type\":\"end-load\",\"parameters\":{}},{\"type\":\"message\",\"parameters\":{\"type\":\"ok\",\"title\":\"Operation successful\",\"message\":\"The selected maintain operation has been succesfully performed\",\"result_details\":[{\"operationType\":\"DELETE\",\"rowsAffected\":1,\"parameterMap\":{\"Action\":\"T\",\"Scr\":\"testIncludeTarget\",\"Act\":1,\"User\":\"testIncludeTarget\",\"_page_\":1,\"_max_\":30,\"Thm\":1,\"Nam\":\"testIncludeTarget\",\"Date\":null}}]}}]");
  }

  /**
   * Call a external rest API
   *
   * @throws Exception Test error
   */
  @Test
  public void testExternalRestApi() throws Exception {
    doRestTest("TestExternalRestApi", "data","\"value\":3,",  null);
  }

  /**
   * Call a external rest API
   *
   * @throws Exception Test error
   */
  @Test
  public void testPostmanRestApi() throws Exception {
    doRestTest("TestPostmanRestApi", "data","", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":1,\"rows\":[{\"acceptLanguage\":\"\",\"acceptEncoding\":\"gzip,deflate\",\"cookie\":\"\",\"method\":\"GET\",\"gzipped\":\"true\",\"postmanToken\":\"\",\"id\":1,\"cacheControl\":\"\",\"accept\":\"application/json, application/*+json\"}]}}},{\"type\":\"end-load\",\"parameters\":{}}]");
  }
}