package com.almis.awe.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.skyscreamer.jsonassert.JSONAssert;
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
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@WithMockUser(username = "test", password = "test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
//@Ignore("Needs infrastructure")
public class MicroserviceTest extends TestUtil {

  // Logger
  private static Logger logger = LogManager.getLogger(MicroserviceTest.class);

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
  //@Test
  public void testSimpleMicroservice() throws Exception {
    doRestTest("CallAluMicroservice", "data", "", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":0,\"rows\":[]}}},{\"type\":\"end-load\",\"parameters\":{}}]");
  }

  /**
   * Rest test: Simple get query
   *
   * @throws Exception Test error
   */
  //@Test
  public void testSimpleRestService() throws Exception {
    doRestTest("CallAluAsRest", "data", "", "[{\"type\":\"fill\",\"parameters\":{\"datalist\":{\"total\":1,\"page\":1,\"records\":0,\"rows\":[]}}},{\"type\":\"end-load\",\"parameters\":{}}]");
  }
}